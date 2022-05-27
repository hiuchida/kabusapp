package v20;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import api.SymbolNameApi;
import io.swagger.client.ApiException;
import io.swagger.client.model.SymbolNameSuccess;
import logic.FileLockLogic;
import util.Consts;
import util.DateTimeUtil;
import util.FileUtil;
import util.LockedAuthorizedTokenUtil;
import util.StringUtil;

/**
 * PUSH APIからチャートデータを作成するツール（複数銘柄対応）。
 */
@ClientEndpoint
public class MainChartData_r2 {
	public static final String WEBSOCKET_URI = "ws://localhost:18080/kabusapi/websocket";

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/chart/";
	/**
	 * チャートデータファイルパス。
	 */
	private static final String DB_FILENAME = "ChartData.csv";
	/**
	 * ファイルロック管理用0バイトのファイルパス。存在しなければ生成される。
	 */
	private static final String LOCK_FILENAME = "ChartData.lock";

	/**
	 * PUSH APIからチャートデータを作成する。
	 * 
	 * @throws IOException
	 * @throws DeploymentException
	 * @throws ApiException 
	 */
	public static void main(String[] args) throws DeploymentException, IOException, ApiException {
		ApiErrorLog.init(MethodHandles.lookup().lookupClass(), Consts.VERSION);
		String X_API_KEY = LockedAuthorizedTokenUtil.lockToken();
		LockedAuthorizedTokenUtil.unlockToken();
		new MainChartData_r2(X_API_KEY).execute();
	}

	/**
	 * 銘柄情報。
	 */
	public static class SymbolInfo {
		/**
		 * タイプ（1:株式、2:先物、3:ＯＰ、4:指数）。
		 */
		public int type;
		/**
		 * 銘柄コード。
		 */
		public String code;
		/**
		 * 市場コード。
		 */
		public int exchange;
		/**
		 * 保存先ディレクトリの接尾語。
		 */
		public String suffix;
		/**
		 * バッファリングされたチャートデータのリスト。
		 */
		private List<String> dataList = new ArrayList<>();
		/**
		 * 保存された最新のデータ。
		 */
		public String lastDataValue = "";
		/**
		 * チャートデータロックを管理する。
		 */
		public FileLockLogic fileLockLogic;
		
		public SymbolInfo(int type, String code, int exchange) {
			this.type = type;
			this.code = code;
			this.exchange = exchange;
			this.suffix = "";
			String dirPath = getDirPath();
			new File(dirPath).mkdirs();
			this.fileLockLogic = new FileLockLogic(dirPath + "/" + LOCK_FILENAME);
		}

		public SymbolInfo(int type, String code, int exchange, String suffix) {
			this.type = type;
			this.code = code;
			this.exchange = exchange;
			this.suffix = suffix;
			String dirPath = getDirPath();
			new File(dirPath).mkdirs();
			this.fileLockLogic = new FileLockLogic(dirPath + "/" + LOCK_FILENAME);
		}

		public String getDirName() {
			String dirName = code;
			if (suffix.length() > 0) {
				dirName = dirName + "_" + suffix;
			}
			return dirName;
		}
	
		private String getDirPath() {
			String dirPath = DIRPATH + getDirName();
			return dirPath;
		}
		
		public String getFilePath(String filename) {
			return getDirPath() + "/" + filename;
		}
	
		/**
		 * 受信したメッセージをバッファに追加する。スレッド同期するため、単純な処理にする。
		 * 
		 * @param message 受信したメッセージ。
		 */
		public synchronized void addChartData(String message) {
			dataList.add(message);
		}

		/**
		 * 受信したメッセージがバッファに存在するか確認する。スレッド同期するため、単純な処理にする。
		 * 
		 * @return true:存在する、false:存在しない。
		 */
		public synchronized boolean isExistsChartData() {
			return dataList.size() > 0;
		}

		/**
		 * 受信したメッセージをすべて取得し、バッファをクリアする。スレッド同期するため、単純な処理にする。
		 * 
		 * @return メッセージのリスト。
		 */
		public synchronized List<String> getAndClearChartData() {
			List<String> list = new ArrayList<>();
			if (dataList.size() > 0) {
				list.addAll(dataList);
				dataList.clear();
			}
			return list;
		}
	}

	/**
	 * 銘柄コード。
	 */
//	private String SYMBOL = GlobalConfigUtil.get("Symbol");

	/**
	 * 銘柄情報マップ。
	 */
	private Map<String, SymbolInfo> symbolMap = new TreeMap<>();

	/**
	 * 銘柄コード取得API。
	 */
	private SymbolNameApi symbolNameApi;

	/**
	 * 銘柄登録API。
	 */
	private RegisterEtcApi registerEtcApi;

	/**
	 * メインスレッド。
	 */
	private Thread mainThread;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public MainChartData_r2(String X_API_KEY) {
		this.symbolNameApi = new SymbolNameApi(X_API_KEY);
		this.registerEtcApi = new RegisterEtcApi(X_API_KEY);
		this.mainThread = Thread.currentThread();
		int yyyymm = 202206;
		for (int ym = yyyymm; ym <= 202209; ym++) {
			try {
				SymbolNameSuccess sns = symbolNameApi.getFuture("NK225mini", ym);
				String code = sns.getSymbol();
				symbolMap.put(code, new SymbolInfo(2, code, 2, "F" + ym));
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
		yyyymm = 202206;
		for (int ym = yyyymm; ym <= 202209; ym += 3) {
			try {
				SymbolNameSuccess sns = symbolNameApi.getFuture("NK225", ym);
				String code = sns.getSymbol();
				symbolMap.put(code, new SymbolInfo(2, code, 2, "FL" + ym));
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
		int basePrice = 27000;
		for (int d = -10 ; d <= 10; d++) {
			int price = basePrice + d * 125;
			try {
				SymbolNameSuccess sns = symbolNameApi.getOption(yyyymm, "C", price);
				String code = sns.getSymbol();
				symbolMap.put(code, new SymbolInfo(3, code, 2, "C" + price));
			} catch (ApiException e) {
				e.printStackTrace();
			}
			try {
				SymbolNameSuccess sns = symbolNameApi.getOption(yyyymm, "P", price);
				String code = sns.getSymbol();
				symbolMap.put(code, new SymbolInfo(3, code, 2, "P" + price));
			} catch (ApiException e) {
				e.printStackTrace();
			}
		}
		symbolMap.put("101", new SymbolInfo(4, "101", 1));
		symbolMap.put("9005", new SymbolInfo(1, "9005", 1));
//		symbolMap.put("USD/JPY", new SymbolInfo(4, "USDJPY", 300));
//		symbolMap.put("USDJPY", new SymbolInfo(4, "USDJPY", 300));
//		symbolMap.put("usdjpy", new SymbolInfo(4, "USDJPY", 300));
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
		registerEtcApi.removeAll();
		for (SymbolInfo si : symbolMap.values()) {
			registerEtcApi.put(si.code, si.exchange);
		}
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
		for (SymbolInfo si : symbolMap.values()) {
			if (si.isExistsChartData()) {
				si.fileLockLogic.lockFile();
				try (PrintWriter pw = FileUtil.writer(si.getFilePath(DB_FILENAME), FileUtil.UTF8, true)) {
					int writeCnt = 0;
					List<String> bufList = si.getAndClearChartData();
					for (String s : bufList) {
//						BoardBean bb = parseJson(s);
//						System.out.println(bb);
						String data = parseChartData(si, s);
						if (data != null) {
							pw.println(data);
							writeCnt++;
						}
					}
					String now = DateTimeUtil.nowToString();
					System.out.println(String.format("%s [%d] MainChartData.writeChartData(): code=%s, bufList.size=%d, writeCnt=%d", now, Thread.currentThread().getId(), si.getDirName(), bufList.size(), writeCnt));
					System.out.flush();
				} catch (IOException e) {
					String now = DateTimeUtil.nowToString();
					System.out.println(String.format("%s [%d] MainChartData.writeChartData(): code=%s, ERROR %s", now, Thread.currentThread().getId(), si.getDirName(), e.toString()));
					System.out.flush();
//					e.printStackTrace();
				} finally {
					si.fileLockLogic.unlockFile();
				}
			}
		}
	}

	/**
	 * 受信したメッセージを解析し、ファイル出力する文字列を作成する。
	 * 
	 * @param si      銘柄情報。
	 * @param message 受信したメッセージ。
	 * @return 出力文字列。対象外はnull。
	 */
	private String parseChartData(SymbolInfo si, String message) {
		// 銘柄コードを比較する
		int idx = message.indexOf("\"Symbol\":\"" + si.code + "\"");
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
		if (si.type != 4) {
			if (volume == null || "null".equals(volume)) {
				return null;
			}
		}

		// 保存された最新のデータと同じ場合はスキップする
		String dataValue;
		if (si.type != 4) {
			dataValue = price + "," + volume;
		} else {
			dataValue = price;
		}
		if (dataValue.equals(si.lastDataValue)) {
			return null;
		}
		si.lastDataValue = dataValue;

		String date;
		if (si.type != 4) {
			// 日時を切り出す。時間外は"TradingVolumeTime":nullのため、見つからない。
			date = StringUtil.parseString(message, "\"TradingVolumeTime\":\"", "\"");
		} else {
			// 日時を切り出す。時間外は"CurrentPriceTime":nullのため、見つからない。
			date = StringUtil.parseString(message, "\"CurrentPriceTime\":\"", "\"");
		}
		if (date == null) {
			return null;
		}

		// 日時から"T"と"+09:00"を取る
		date = date.substring(0, 10) + " " + date.substring(11, 19);
		
		return date + "," + dataValue;
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
		String code = StringUtil.parseString(message, "\"Symbol\":\"", "\"");
		if (code == null) {
			// 過去データからnullになることは有り得ない
			return;
		}
		SymbolInfo si = symbolMap.get(code);
		if (si == null) {
			// 別の手段で銘柄登録APIを実行した場合に受信する可能性があるが、無視する
			return;
		}
		si.addChartData(message);
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
