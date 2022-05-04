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

import api.ApiErrorLog;
import api.RegisterEtcApi;
import io.swagger.client.ApiException;
import util.Consts;
import util.FileUtil;
import util.GlobalConfigUtil;
import v9_2.LockedAuthorizedToken_r4;

/**
 * PUSH APIからチャートデータを作成するツール。
 */
@ClientEndpoint
public class MainChartData {
	public static final String WEBSOCKET_URI = "ws://localhost:18080/kabusapi/websocket";

	/**
	 * タブ文字。
	 */
	public static final String TAB = "\t";

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * チャートデータファイルパス。
	 */
	private static final String DB_FILEPATH = DIRPATH + "ChartData.csv";

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
	 * バッファリングされた最新の価格。
	 */
	private String lastPrice = "";

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public MainChartData(String X_API_KEY) {
		this.registerEtcApi = new RegisterEtcApi(X_API_KEY);
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
		Session session = container.connectToServer(this, uri);
		// 銘柄登録
		registerEtcApi.put(SYMBOL, 2);
		// イベントループ
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			writeChartData();
		}
	}

	/**
	 * チャートデータファイルを書き込む。成功したらバッファをクリアする。メッセージの解析はスレッド同期せずに行う。
	 */
	private void writeChartData() {
		List<String> bufList = getAndClearChartData();
		if (bufList.size() > 0) {
			int writeCnt = 0;
			try (PrintWriter pw = FileUtil.writer(DB_FILEPATH, FileUtil.UTF8, true)) {
				for (String s : bufList) {
					String data = parseChartData(s);
					if (data != null) {
						pw.println(data);
						writeCnt++;
					}
				}
				System.out.println("MainChartData.writeChartData(): bufList.size=" + bufList.size() + ", writeCnt=" + writeCnt);
			} catch (IOException e) {
				e.printStackTrace();
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

		// 価格を切り出す
		String str2 = "\"CurrentPrice\":";
		int idx2 = message.indexOf(str2);
		if (idx2 < 0) {
			return null;
		}
		int idx3 = message.indexOf(",", idx2 + str2.length());
		if (idx3 < 0) {
			return null;
		}
		String price = message.substring(idx2 + str2.length(), idx3);
		
		// バッファリングされた最新の価格と同じ場合はスキップする
		if (price.equals(lastPrice)) {
			return null;
		}
		lastPrice = price;
		
		// 日時を切り出す
		String str4 = "\"CurrentPriceTime\":\"";
		int idx4 = message.indexOf(str4);
		if (idx4 < 0) {
			return null;
		}
		int idx5 = message.indexOf("\"", idx4 + str4.length());
		if (idx5 < 0) {
			return null;
		}
		String date = message.substring(idx4 + str4.length(), idx5);
		
		// 日時から"T"と"+09:00"を取る
		date = date.substring(0, 10) + " " + date.substring(11, 19);
		
		return date + "," + price;
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
		System.out.println("onOpen:" + session);
	}

	@OnMessage
	public void onMessage(String message) {
//		System.out.println("onMessge：" + message);
		addChartData(message);
	}

	@OnError
	public void onError(Throwable th) {
		System.out.println("onError：" + th.getMessage());
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("onClose:" + session);
	}

}
