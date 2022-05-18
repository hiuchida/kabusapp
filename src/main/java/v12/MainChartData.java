package v12;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.google.gson.Gson;

import api.ApiErrorLog;
import api.BoardBean;
import api.RegisterEtcApi;
import io.swagger.client.ApiException;
import logic.FileLockLogic;
import util.Consts;
import util.DateTimeUtil;
import util.FileUtil;
import util.GlobalConfigUtil;
import util.StringUtil;
import v9_2.LockedAuthorizedToken_r4;

/**
 * PUSH APIからチャートデータを作成するツール。
 */
@ClientEndpoint
public class MainChartData {
	public static final String WEBSOCKET_URI = "ws://localhost:18080/kabusapi/websocket";

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * チャートデータファイルパス。
	 */
	private static final String DB_FILEPATH = DIRPATH + "ChartData.csv";
	/**
	 * ファイルロック管理用0バイトのファイルパス。存在しなければ生成される。
	 */
	private static final String LOCK_FILEPATH = DIRPATH + "ChartData.lock";

	/**
	 * PUSH APIからチャートデータを作成する。
	 * 
	 * @throws IOException
	 * @throws DeploymentException
	 * @throws ApiException 
	 */
	public static void main(String[] args) throws DeploymentException, IOException, ApiException {
		ApiErrorLog.init(MethodHandles.lookup().lookupClass(), Consts.VERSION);
		String X_API_KEY = LockedAuthorizedToken_r4.lockToken();
		LockedAuthorizedToken_r4.unlockToken();
		new MainChartData(X_API_KEY).execute();
	}

	/**
	 * 銘柄コード。
	 */
	private String SYMBOL = GlobalConfigUtil.get("Symbol");

	/**
	 * 銘柄登録API。
	 */
	private RegisterEtcApi registerEtcApi;

	/**
	 * バッファリングされたチャートデータのリスト。
	 */
	private List<String> dataList = new ArrayList<>();

	/**
	 * バッファリングされた最新の売買高。
	 */
	private String lastVolume = "";

	/**
	 * メインスレッド。
	 */
	private Thread mainThread;

	/**
	 * チャートデータロックを管理する。
	 */
	private FileLockLogic fileLockLogic;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public MainChartData(String X_API_KEY) {
		this.registerEtcApi = new RegisterEtcApi(X_API_KEY);
		this.mainThread = Thread.currentThread();
		this.fileLockLogic = new FileLockLogic(LOCK_FILEPATH);
	}

	/**
	 * PUSH APIからチャートデータを作成する。
	 * 
	 * @throws IOException
	 * @throws DeploymentException
	 * @throws ApiException 
	 */
	public void execute() throws DeploymentException, IOException, ApiException {
		// WebSocket初期化
		URI uri = URI.create(WEBSOCKET_URI);
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		final Session session = container.connectToServer(this, uri);
		// 銘柄登録
		registerEtcApi.put(SYMBOL, 2);
		// シャットダウンハンドラ
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					session.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mainThread.interrupt();
				try {
					mainThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String now = DateTimeUtil.nowToString();
				System.out.println(String.format("%s [%d] ShutdownHook: Done", now, Thread.currentThread().getId()));
				System.out.flush();
			}
		});
		// イベントループ
		while (session.isOpen()) {
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			writeChartData();
		}
		String now = DateTimeUtil.nowToString();
		System.out.println(String.format("%s [%d] MainChartData.execute: Done", now, Thread.currentThread().getId()));
		System.out.flush();
	}

	/**
	 * チャートデータファイルを書き込む。成功したらバッファをクリアする。メッセージの解析はスレッド同期せずに行う。
	 */
	private void writeChartData() {
		if (isExistsChartData()) {
			fileLockLogic.lockFile();
			try (PrintWriter pw = FileUtil.writer(DB_FILEPATH, FileUtil.UTF8, true)) {
				int writeCnt = 0;
				List<String> bufList = getAndClearChartData();
				for (String s : bufList) {
//					BoardBean bb = parseJson(s);
//					System.out.println(bb);
					String data = parseChartData(s);
					if (data != null) {
						pw.println(data);
						writeCnt++;
					}
				}
				String now = DateTimeUtil.nowToString();
				System.out.println(String.format("%s [%d] MainChartData.writeChartData(): bufList.size=%d, writeCnt=%d", now, Thread.currentThread().getId(), bufList.size(), writeCnt));
				System.out.flush();
			} catch (IOException e) {
				String now = DateTimeUtil.nowToString();
				System.out.println(String.format("%s [%d] MainChartData.writeChartData(): ERROR %s", now, Thread.currentThread().getId(), e.toString()));
				System.out.flush();
//				e.printStackTrace();
			} finally {
				fileLockLogic.unlockFile();
			}
		}
	}

	/**
	 * 受信したメッセージを解析し、ファイル出力する文字列を作成する。
	 * 
	 * @param message 受信したメッセージ。
	 * @return 出力文字列。対象外はnull。
	 */
	private String parseChartData(String message) {
		// 銘柄コードを比較する
		int idx = message.indexOf("\"Symbol\":\"" + SYMBOL + "\"");
		if (idx < 0) {
			return null;
		}

		// 価格を切り出す。時間外は"CurrentPrice":nullのため、"null"が返る。
		String price = StringUtil.parseString(message, "\"CurrentPrice\":", ",");
		if (price == null || "null".equals(price)) {
			return null;
		}
		
		// 売買高を切り出す。時間外は"TradingVolume":nullのため、"null"が返る。
		String volume = StringUtil.parseString(message, "\"TradingVolume\":", ",");
		if (volume == null || "null".equals(volume)) {
			return null;
		}

		// バッファリングされた最新の売買高と同じ場合はスキップする
		if (volume.equals(lastVolume)) {
			return null;
		}
		lastVolume = volume;

		// 日時を切り出す。時間外は"CurrentPriceTime":nullのため、見つからない。
		String date = StringUtil.parseString(message, "\"CurrentPriceTime\":\"", "\"");
		if (date == null) {
			return null;
		}

		// 日時から"T"と"+09:00"を取る
		date = date.substring(0, 10) + " " + date.substring(11, 19);
		
		return date + "," + price + "," + volume;
	}

	/**
	 * 受信したメッセージJSONを解析し、時価情報Beanを生成する。
	 * 
	 * @param message 受信したメッセージ。
	 * @return 時価情報Bean。
	 */
	private BoardBean parseJson(String message) {
		Gson gson = new Gson();
		BoardBean bb = gson.fromJson(message, BoardBean.class);
		return bb;
	}

	/**
	 * 受信したメッセージをバッファに追加する。スレッド同期するため、単純な処理にする。
	 * 
	 * @param message 受信したメッセージ。
	 */
	private synchronized void addChartData(String message) {
		dataList.add(message);
	}

	/**
	 * 受信したメッセージがバッファに存在するか確認する。スレッド同期するため、単純な処理にする。
	 * 
	 * @return true:存在する、false:存在しない。
	 */
	private synchronized boolean isExistsChartData() {
		return dataList.size() > 0;
	}

	/**
	 * 受信したメッセージをすべて取得し、バッファをクリアする。スレッド同期するため、単純な処理にする。
	 * 
	 * @return メッセージのリスト。
	 */
	private synchronized List<String> getAndClearChartData() {
		List<String> list = new ArrayList<>();
		if (dataList.size() > 0) {
			list.addAll(dataList);
			dataList.clear();
		}
		return list;
	}

	@OnOpen
	public void onOpen(Session session) {
		String now = DateTimeUtil.nowToString();
		System.out.println(String.format("%s [%d] onOpen:%s", now, Thread.currentThread().getId(), session.toString()));
		System.out.flush();
	}

	@OnMessage
	public void onMessage(String message) {
//		String now = DateTimeUtil.nowToString();
//		System.out.println(String.format("%s [%d] onMessge:%s", now, Thread.currentThread().getId(), message));
//		System.out.flush();
		addChartData(message);
	}

	@OnError
	public void onError(Throwable th) {
		String now = DateTimeUtil.nowToString();
		System.out.println(String.format("%s [%d] onError:%s", now, Thread.currentThread().getId(), th.getMessage()));
		System.out.flush();
	}

	@OnClose
	public void onClose(Session session) {
		String now = DateTimeUtil.nowToString();
		System.out.println(String.format("%s [%d] onClose:%s", now, Thread.currentThread().getId(), session.toString()));
		System.out.flush();
	}

}
