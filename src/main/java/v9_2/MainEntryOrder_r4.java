package v9_2;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import api.ApiErrorLog;
import io.swagger.client.ApiException;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import logic.BoardLogic;
import logic.SendMailLogic;
import util.Consts;
import util.ExchangeUtil;
import util.FileUtil;
import util.GlobalConfigUtil;
import util.LockedAuthorizedTokenUtil;
import util.ScheduleUtil;
import util.StringUtil;
import v9_2.EntryOrdersLogic_r4.OrderInfo;

/**
 * 新規注文ツール。
 */
public class MainEntryOrder_r4 {
	/**
	 * 銘柄コード(Symbol)。 
	 */
	private static final String SYMBOL = GlobalConfigUtil.get("Symbol"); // "167060019";
	/**
	 * 銘柄名(SymbolName)。
	 */
	private static final String SYMBOL_NAME = GlobalConfigUtil.get("SymbolName"); // "日経225mini 22/06";
	/**
	 * 有効期限(ExpireDay)。
	 */
	private static final int EXPIRE_DAY = GlobalConfigUtil.getInt("ExpireDay", 0); // 当日0
	/**
	 * スキップする天井の値幅（curPrice-basePrice）。
	 */
	private static final int SKIP_PRICE_DELTA_CEIL = GlobalConfigUtil.getInt("SkipPriceDeltaCeil", 100); // 100
	/**
	 * スキップする床の値幅（curPrice-basePrice）。
	 */
	private static final int SKIP_PRICE_DELTA_FLOOR = GlobalConfigUtil.getInt("SkipPriceDeltaFloor", -100); // -100

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 新規注文依頼設定を保存したファイルパス。事前に準備する。
	 */
	private static final String CFG_FILEPATH = DIRPATH + "MainEntryOrder_r4.cfg";
	/**
	 * 新規注文依頼情報を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String TXT_FILEPATH = DIRPATH + "MainEntryOrder_r4.txt";
	/**
	 * 新規注文ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "MainEntryOrder_r4.log";
	/**
	 * メール本文を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String MAIL_FILEPATH = DIRPATH + "MainEntryOrder_r4.mail";
	/**
	 * 新規注文依頼設定ファイルのカラム数。
	 */
	public static final int CFG_MAX_COLS = 2;
	/**
	 * 新規注文依頼情報ファイルのカラム数。
	 */
	public static final int TXT_MAX_COLS = 3;

	/**
	 * 注文依頼情報クラス。
	 */
	public static class ReqInfo {
		/**
		 * リクエストID。
		 */
		public String reqId;
		/**
		 * ステータス。
		 */
		public String status;
		/**
		 * コメント。
		 */
		public String comment;

		/**
		 * コンストラクタ。
		 * 
		 * @param reqId   リクエストID。
		 * @param status  ステータス。
		 * @param comment コメント。
		 */
		public ReqInfo(String reqId, String status, String comment) {
			this.reqId = reqId;
			this.status = status;
			this.comment = comment;
		}

		/**
		 * 注文依頼情報ファイルのヘッダ文字列を生成する。
		 * 
		 * @return ヘッダ文字列。
		 */
		public static String toHeaderString() {
			String[] sa = new String[3];
			int i = 0;
			sa[i++] = "requestId";
			sa[i++] = "status  ";
			sa[i++] = "comment";
			String val = "# " + StringUtil.joinTab(sa);
			return val;
		}

		/**
		 * インスタンスの主キー(reqId)を取得する。
		 * 
		 * @return 主キー。
		 */
		public String getKey() {
			return reqId;
		}

		/**
		 * 注文依頼情報ファイルのレコード文字列を生成する。
		 * 
		 * @return レコード文字列。
		 */
		public String toLineString() {
			String[] sa = new String[3];
			int i = 0;
			sa[i++] = reqId;
			sa[i++] = status;
			sa[i++] = comment;
			String val = StringUtil.joinTab(sa);
			return val;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(reqId);
			sb.append(": ").append(status);
			sb.append(" (").append(comment).append(")");
			return sb.toString();
		}
	}

	/**
	 * 新規注文ツール。
	 * 
	 * @param args 起動パラメータ。
	 * @throws ApiException
	 */
	public static void main(String[] args) throws ApiException {
		ApiErrorLog.init(MethodHandles.lookup().lookupClass(), Consts.VERSION);
		String X_API_KEY = LockedAuthorizedTokenUtil.lockToken();
		try {
			new MainEntryOrder_r4(X_API_KEY).execute();
		} finally {
			LockedAuthorizedTokenUtil.unlockToken();
		}
	}

	/**
	 * 時価情報・板情報を管理する。
	 */
	private BoardLogic boardLogic;

	/**
	 * 新規注文情報を管理する。
	 */
	private EntryOrdersLogic_r4 entryOrdersLogic;

	/**
	 * メール送信を管理する。
	 */
	private SendMailLogic sendMailLogic;

	/**
	 * 新規注文依頼設定のマップ。
	 */
	private Map<String, String> configMap;

	/**
	 * 新規注文依頼情報のマップ。
	 */
	private Map<String, ReqInfo> orderMap;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public MainEntryOrder_r4(String X_API_KEY) {
		this.boardLogic = new BoardLogic(X_API_KEY);
		this.entryOrdersLogic = new EntryOrdersLogic_r4(X_API_KEY);
		this.sendMailLogic = new SendMailLogic(MAIL_FILEPATH);
	}

	/**
	 * 新規注文。
	 * 
	 * @throws ApiException 
	 */
	public void execute() throws ApiException {
		sendMailLogic.deleteMailFile();
		if (!ScheduleUtil.now()) {
			return;
		}
		initConfig();
		entryOrdersLogic.execute();
		openOrder();
		int exchange = ExchangeUtil.now();
		if (exchange > 0) {
			List<OrderInfo> entryList = entryOrdersLogic.execute();
			for (OrderInfo oi : entryList) {
				if (oi.state == OrderInfo.STATE_NOT_ORDER) {
					sendEntryOrder(oi, exchange);
				}
			}
		}
		for (String key : orderMap.keySet()) {
			ReqInfo ri = orderMap.get(key);
			String val = ri.status;
			if (val.startsWith("O,") || val.startsWith("P,") || val.startsWith("C,")) {
				String uniqId = val.substring(2);
				OrderInfo oi = entryOrdersLogic.getOrder(uniqId);
				if (oi != null) {
					ri.comment = "# " + oi.orderId + "," + String.format("%2d", oi.state)
							+ "," + oi.price + StringUtil.sideStr(oi.side) + "x" + oi.orderQty
							+ "," + oi.executionIds;
				} else {
					ri.comment = "# ?";
				}
			} else {
				ri.comment = "# ";
			}
		}
		entryOrdersLogic.writeOrders();
		sendMailLogic.writeMailFile("EntryOrder");
		writeOrders();
	}

	/**
	 * 新規注文を実行する。
	 * 
	 * @throws ApiException 
	 */
	private void openOrder() throws ApiException {
		int curPrice = boardLogic.getCurPrice(SYMBOL + "@2");
		if (curPrice == 0) {
			return;
		}
		for (String key : orderMap.keySet()) {
			// Price,Side,Qty
			// 26745,S,1
			ReqInfo ri = orderMap.get(key);
			String val = ri.status;
			String[] cols = StringUtil.splitComma(key);
			int basePrice = StringUtil.parseInt(cols[0]);
			String side = StringUtil.sideCode(cols[1]);
			int qty = StringUtil.parseInt(cols[2]);
			String msg = "key=" + key + ", basePrice=" + basePrice + ", side=" + side
					+ ", qty=" + qty + ", val=" + val;
			System.out.println("  > openOrder " + msg);
			FileUtil.printLog(LOG_FILEPATH, "openOrder", msg);

			String[] flds = StringUtil.splitComma(val);
			if (flds.length == 1) {
				if ("R".equals(flds[0])) {
					if (checkOpenOrder(basePrice, side, curPrice)) {
						String uniqId = entryOrdersLogic.addOrder(basePrice, qty, side);
						String nval = "O," + uniqId;
						ri.status = nval;
						msg = "change " + key + ": " + val + " -> " + nval;
						System.out.println("  > openOrder " + msg);
						FileUtil.printLog(LOG_FILEPATH, "openOrder", msg);
					}
				}
			} else {
				if ("O".equals(flds[0])) {
					String uniqId = flds[1];
					if (checkOpenOrder(uniqId, basePrice, side, curPrice)) {
						uniqId = entryOrdersLogic.addOrder(basePrice, qty, side);
						String nval = "O," + uniqId;
						ri.status = nval;
						msg = "change " + key + ": " + val + " -> " + nval;
						System.out.println("  > openOrder " + msg);
						FileUtil.printLog(LOG_FILEPATH, "openOrder", msg);
					}
				} else if ("C".equals(flds[0])) {
					String uniqId = flds[1];
					OrderInfo oi = entryOrdersLogic.getOrder(uniqId);
					if (OrderInfo.STATE_UNKNOWN < oi.state && oi.state < OrderInfo.STATE_FINISH) {
						String orderId = oi.orderId;
						msg = "cancel " + key + ": orderId=" + orderId + ", price=" + oi.price + StringUtil.sideStr(oi.side) + ", qty=" + oi.orderQty;
						entryOrdersLogic.cancelOrder(orderId, msg);
						System.out.println("  > cancelOrder " + msg);
						FileUtil.printLog(LOG_FILEPATH, "cancelOrder", msg);
					} else if ((OrderInfo.STATE_CANCEL <= oi.state && oi.state <= OrderInfo.STATE_CLOSE)
							|| (OrderInfo.STATE_CANCEL_DELETE <= oi.state && oi.state <= OrderInfo.STATE_CLOSE_DELETE)) {
						String nval = "P";
						ri.status = nval;
						msg = "change " + key + ": " + val + " -> " + nval;
						System.out.println("  > cancelOrder " + msg);
						FileUtil.printLog(LOG_FILEPATH, "cancelOrder", msg);
					}
				}
			}
		}
		writeOrders();
		entryOrdersLogic.writeOrders();
	}

	/**
	 * 既存注文と残高をチェックする。
	 * 
	 * @param basePrice  基準の値段。
	 * @param side       売買区分(Side)。
	 * @param curPrice   現値。
	 * @return true:発注する、false:スキップ
	 * @throws ApiException 
	 */
	private boolean checkOpenOrder(int basePrice, String side, int curPrice) {
		int sign = StringUtil.sign(side);
		int delta = (curPrice - basePrice) * sign;
		if (delta > SKIP_PRICE_DELTA_CEIL || delta < SKIP_PRICE_DELTA_FLOOR) {
			return false;
		}
		return true;
	}

	/**
	 * 既存注文と残高をチェックする。
	 * 
	 * @param uniqId     ユニークID。
	 * @param basePrice  基準の値段。
	 * @param side       売買区分(Side)。
	 * @param curPrice   現値。
	 * @return true:発注する、false:スキップ
	 * @throws ApiException 
	 */
	private boolean checkOpenOrder(String uniqId, int basePrice, String side, int curPrice) {
		int sign = StringUtil.sign(side);
		int delta = (curPrice - basePrice) * sign;
		if (delta > SKIP_PRICE_DELTA_CEIL || delta < SKIP_PRICE_DELTA_FLOOR) {
			return false;
		}
		OrderInfo oi = entryOrdersLogic.getOrder(uniqId);
		if (oi == null) {
			return false;
		}
		boolean rc = false;
		switch (oi.state) {
		case OrderInfo.STATE_CANCEL:
		case OrderInfo.STATE_CLOSE:
		case OrderInfo.STATE_CANCEL_DELETE:
		case OrderInfo.STATE_CLOSE_DELETE:
			rc = true;
		}
		return rc;
	}

	/**
	 * 新規注文を実行する。
	 * 
	 * @param oi       新規注文情報。
	 * @param exchange 市場コード(Exchange)。
	 * @return 注文番号(ID)。
	 * @throws ApiException 
	 */
	private String sendEntryOrder(OrderInfo oi, int exchange) throws ApiException {
		exchange = 2; // 日通し
		int expireDay = EXPIRE_DAY;
		RequestSendOrderDerivFuture body = new RequestSendOrderDerivFuture();
		body.setSymbol(SYMBOL);
		body.setExchange(exchange);
		body.setTradeType(1); // 新規
		body.setTimeInForce(1); // FAS
		body.setSide(oi.side);
		body.setQty(oi.orderQty);
		body.setFrontOrderType(20); // 指値
		body.setPrice((double) oi.price);
		body.setExpireDay(expireDay);

		String msg;
		{
			StringBuilder sb = new StringBuilder();
			sb.append("ENTRY:{").append(SYMBOL).append(" ").append(SYMBOL_NAME).append(" ");
			sb.append(StringUtil.exchangeStr(exchange));
			sb.append(" ").append(expireDay);
			sb.append(" price=").append(oi.price).append(StringUtil.sideStr(oi.side));
			sb.append(", qty=").append(oi.orderQty);
			sb.append("}");
			msg = sb.toString();
			System.out.println("  > sendEntryOrder " + msg);
			FileUtil.printLog(LOG_FILEPATH, "sendEntryOrder", msg);
		}
		String orderId = entryOrdersLogic.sendOrder(oi, body, msg);
		{
			StringBuilder sb = new StringBuilder();
			sb.append("ENTRY:{").append(SYMBOL_NAME).append(" ").append(StringUtil.exchangeStr(exchange));
			sb.append(" ").append(expireDay);
			sb.append(" price=").append(oi.price).append(StringUtil.sideStr(oi.side));
			sb.append(", qty=").append(oi.orderQty);
			sb.append("}");
			String msgMail = sb.toString();
			sendMailLogic.addLine(msgMail);
		}
		return orderId;
	}

	/**
	 * 注文依頼設定ファイルを初期化する。
	 */
	private void initConfig() {
		readOrders();
		readConfig();
		for (String key : configMap.keySet()) {
			String cval = configMap.get(key);
			ReqInfo ri = orderMap.get(key);
			String oval = "";
			if (ri != null) {
				oval = ri.status;
			}
			if ("R".equals(cval)) {
				if (ri != null) {
					if ("P".equals(oval)) {
						oval = "R";
						ri.status = oval;
						String msg = "  Resume " + key + ": " + oval;
						System.out.println("  > initConfig " + msg);
						FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
					} else if (oval.startsWith("P,")) {
						oval = "O," + oval.substring(2);
						ri.status = oval;
						String msg = "  Resume " + key + ": " + oval;
						System.out.println("  > initConfig " + msg);
						FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
					}
				} else {
					putReq(key, cval, "");
					String msg = "  Register " + key + ": " + cval;
					System.out.println("  > initConfig " + msg);
					FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
				}
			} else if ("P".equals(cval)) {
				if (ri != null) {
					if ("R".equals(oval)) {
						oval = "P";
						ri.status = oval;
						String msg = "  Pause " + key + ": " + oval;
						System.out.println("  > initConfig " + msg);
						FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
					} else if (oval.startsWith("O,")) {
						oval = "P," + oval.substring(2);
						ri.status = oval;
						String msg = "  Pause " + key + ": " + oval;
						System.out.println("  > initConfig " + msg);
						FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
					}
				} else {
					putReq(key, cval, "");
					String msg = "  Pause " + key + ": " + cval;
					System.out.println("  > initConfig " + msg);
					FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
				}
			} else if ("D".equals(cval)) {
				if (ri != null) {
					if ("R".equals(oval) || "P".equals(oval)) {
						orderMap.remove(key);
						String msg = "  Delete " + key + ": " + oval;
						System.out.println("  > initConfig " + msg);
						FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
					} else if (oval.startsWith("O,") || oval.startsWith("P,")) {
						oval = "C," + oval.substring(2);
						ri.status = oval;
						String msg = "  Delete " + key + ": " + oval;
						System.out.println("  > initConfig " + msg);
						FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
					}
				} else {
				}
			}
		}
		writeOrders();
	}

	/**
	 * 注文依頼設定ファイルを読み込む。不正なレコードは無視される。
	 */
	private void readConfig() {
		configMap = new TreeMap<>();
		List<String> lines = FileUtil.readAllLines(CFG_FILEPATH);
		System.out.println("--- " + CFG_FILEPATH + " ---");
		System.out.println("MainEntryOrder_r4.readConfig(): lines.size=" + lines.size());
		for (String s : lines) {
			if (s.length() == 0) {
				continue;
			}
			if (s.startsWith("#")) {
				continue;
			}
			String[] cols = StringUtil.splitTab(s);
			if (cols.length != CFG_MAX_COLS) {
				System.out.println("Warning: SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			configMap.put(cols[0], cols[1]);
			System.out.println("  " + cols[0] + ": " + cols[1]);
		}
	}

	/**
	 * 注文依頼情報ファイルを読み込む。不正なレコードは無視される。
	 */
	private void readOrders() {
		orderMap = new TreeMap<>();
		List<String> lines = FileUtil.readAllLines(TXT_FILEPATH);
		for (String s : lines) {
			if (s.length() == 0) {
				continue;
			}
			if (s.startsWith("#")) {
				continue;
			}
			String[] cols = StringUtil.splitTab(s);
			if (cols.length < TXT_MAX_COLS) {
				System.out.println("Warning SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			putReq(cols[0], cols[1], cols[2]);
		}
		System.out.println("MainEntryOrder_r4.readOrders(): orderMap.size=" + orderMap.size());
		for (String key : orderMap.keySet()) {
			String val = orderMap.get(key).status;
			System.out.println("  " + key + ": " + val);
		}
	}

	/**
	 * 注文依頼情報を追加する。
	 * 
	 * @param reqId   リクエストID。
	 * @param status  ステータス。
	 * @param comment コメント。
	 */
	private void putReq(String reqId, String status, String comment) {
		ReqInfo ri = new ReqInfo(reqId, status, comment);
		String key = ri.getKey();
		orderMap.put(key, ri);
	}

	/**
	 * 注文依頼情報ファイルを書き込む。
	 */
	private void writeOrders() {
		System.out.println("MainEntryOrder_r4.writeOrders(): orderMap.size=" + orderMap.size());
		List<String> lines = new ArrayList<>();
		lines.add(ReqInfo.toHeaderString());
		lines.add("");
		for (int i = 0; i < 2; i++) {
			for (String key : orderMap.keySet()) {
				if (i == 0) {
					if (key.indexOf(",L,") < 0) {
						continue;
					}
				} else {
					if (key.indexOf(",S,") < 0) {
						continue;
					}
				}
				ReqInfo ri = orderMap.get(key);
				lines.add(ri.toLineString());
				String val = ri.status;
				System.out.println("  " + key + ": " + val);
			}
			lines.add("");
		}
		FileUtil.writeAllLines(TXT_FILEPATH, lines);
	}

}
