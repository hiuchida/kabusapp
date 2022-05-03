package v9_2;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import api.ApiErrorLog;
import io.swagger.client.ApiException;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import util.Consts;
import util.ExchangeUtil;
import util.FileUtil;
import util.GlobalConfigUtil;
import util.SendMailUtil;
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
	 * タブ文字。
	 */
	public static final String TAB = "\t";

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 新規注文設定を保存したファイルパス。事前に準備する。
	 */
	private static final String CFG_FILEPATH = DIRPATH + "MainEntryOrder_r4.cfg";
	/**
	 * 新規注文情報を保存したファイルパス。存在しなければ生成される。
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
	 * 新規注文設定ファイルのカラム数。
	 */
	public static final int MAX_COLS = 2;

	/**
	 * 新規注文ツール。
	 * 
	 * @param args 起動パラメータ。
	 * @throws ApiException
	 */
	public static void main(String[] args) throws ApiException {
		ApiErrorLog.init(MethodHandles.lookup().lookupClass(), Consts.VERSION);
		String X_API_KEY = LockedAuthorizedToken_r4.lockToken();
		try {
			new MainEntryOrder_r4(X_API_KEY).execute();
		} finally {
			LockedAuthorizedToken_r4.unlockToken();
		}
	}

	/**
	 * 時価情報・板情報を管理する。
	 */
	private BoardLogic_r4 boardLogic;

	/**
	 * 建玉情報を管理する。
	 */
	private PositionsLogic_r4 posLogic;

	/**
	 * 新規注文情報を管理する。
	 */
	private EntryOrdersLogic_r4 entryOrdersLogic;

	/**
	 * メール送信を管理する。
	 */
	private SendMailUtil sendMailUtil;

	/**
	 * 新規注文設定のマップ。
	 */
	private Map<String, String> configMap;

	/**
	 * 新規注文情報のマップ。
	 */
	private Map<String, String> orderMap;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public MainEntryOrder_r4(String X_API_KEY) {
		this.boardLogic = new BoardLogic_r4(X_API_KEY);
		this.posLogic = new PositionsLogic_r4(X_API_KEY);
		this.entryOrdersLogic = new EntryOrdersLogic_r4(X_API_KEY);
		this.sendMailUtil = new SendMailUtil(MAIL_FILEPATH);
	}

	/**
	 * 新規注文。
	 * 
	 * @throws ApiException 
	 */
	public void execute() throws ApiException {
		sendMailUtil.deleteMailFile();
		initConfig();
		refreshOrders();
		List<OrderInfo> entryList = entryOrdersLogic.execute();
		int exchange = ExchangeUtil.now();
		if (exchange > 0) {
			openOrder(exchange);
			for (OrderInfo oi : entryList) {
				if (oi.state == OrderInfo.STATE_NOT_ORDER) {
					sendEntryOrder(oi, exchange);
				}
			}
		}
		entryOrdersLogic.writeOrders();
		sendMailUtil.writeMailFile("EntryOrder");
	}

	/**
	 * 新規注文を実行する。
	 * 
	 * @param exchange 市場コード(Exchange)。
	 * @throws ApiException 
	 */
	private void openOrder(int exchange) throws ApiException {
		int curPrice = boardLogic.getCurPrice(SYMBOL + "@2");
		for (String key : orderMap.keySet()) {
			// Price,Side,Qty
			// 26745,S,1
			String val = orderMap.get(key);
			String[] cols = key.split(",");
			int basePrice = StringUtil.parseInt(cols[0]);
			String side = StringUtil.sideCode(cols[1]);
			int qty = StringUtil.parseInt(cols[2]);
			String msg = "key=" + key + ", basePrice=" + basePrice + ", side=" + side
					+ ", qty=" + qty + ", val=" + val;
			System.out.println("  > openOrder " + msg);
			FileUtil.printLog(LOG_FILEPATH, "openOrder", msg);

			String[] flds = val.split(",");
			if (flds.length == 1) {
				if ("R".equals(flds[0])) {
					if (checkOpenOrder(basePrice, side, curPrice)) {
						String uniqId = entryOrdersLogic.addOrder(basePrice, qty, side);
						String nval = "O," + uniqId;
						orderMap.put(key, nval);
						msg = "change " + val + " -> " + nval;
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
						orderMap.put(key, nval);
						msg = "change " + val + " -> " + nval;
						System.out.println("  > openOrder " + msg);
						FileUtil.printLog(LOG_FILEPATH, "openOrder", msg);
					}
				} else if ("C".equals(flds[0])) {
					String uniqId = flds[1];
					OrderInfo oi = entryOrdersLogic.getOrder(uniqId);
					if (OrderInfo.STATE_UNKNOWN < oi.state && oi.state < OrderInfo.STATE_FINISH) {
						String orderId = oi.orderId;
						msg = "orderId=" + orderId + ", price=" + oi.price + StringUtil.sideStr(oi.side) + ", qty=" + oi.orderQty;
						entryOrdersLogic.cancelOrder(orderId, msg);
						System.out.println("  > cancelOrder " + msg);
						FileUtil.printLog(LOG_FILEPATH, "cancelOrder", msg);
					} else if ((OrderInfo.STATE_CANCEL <= oi.state && oi.state <= OrderInfo.STATE_CLOSE)
							|| (OrderInfo.STATE_CANCEL_DELETE <= oi.state && oi.state <= OrderInfo.STATE_CLOSE_DELETE)) {
						String nval = "P";
						orderMap.put(key, nval);
						msg = "change " + val + " -> " + nval;
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
		if (oi.state < OrderInfo.STATE_FINISH) {
			return false;
		}
		return !posLogic.isValidExecutionId(oi.executionIds);
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
			sendMailUtil.addLine(msgMail);
		}
		return orderId;
	}

	/**
	 * 注文設定ファイルを初期化する。
	 */
	private void initConfig() {
		readOrders();
		readConfig();
		for (String key : configMap.keySet()) {
			String cval = configMap.get(key);
			String oval = orderMap.get(key);
			if ("R".equals(cval)) {
				if (oval != null) {
					if ("P".equals(oval)) {
						oval = "R";
						orderMap.put(key, oval);
						String msg = "  Resume " + key + ": " + oval;
						System.out.println("  > initConfig " + msg);
						FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
					} else if (oval.startsWith("P,")) {
						oval = "O," + oval.substring(2);
						orderMap.put(key, oval);
						String msg = "  Resume " + key + ": " + oval;
						System.out.println("  > initConfig " + msg);
						FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
					}
				} else {
					orderMap.put(key, cval);
					String msg = "  Register " + key + ": " + cval;
					System.out.println("  > initConfig " + msg);
					FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
				}
			} else if ("P".equals(cval)) {
				if (oval != null) {
					if ("R".equals(oval)) {
						oval = "P";
						orderMap.put(key, oval);
						String msg = "  Pause " + key + ": " + oval;
						System.out.println("  > initConfig " + msg);
						FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
					} else if (oval.startsWith("O,")) {
						oval = "P," + oval.substring(2);
						orderMap.put(key, oval);
						String msg = "  Pause " + key + ": " + oval;
						System.out.println("  > initConfig " + msg);
						FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
					}
				} else {
					orderMap.put(key, cval);
					String msg = "  Pause " + key + ": " + cval;
					System.out.println("  > initConfig " + msg);
					FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
				}
			} else if ("D".equals(cval)) {
				if (oval != null) {
					if ("R".equals(oval) || "P".equals(oval)) {
						orderMap.remove(key);
						String msg = "  Delete " + key + ": " + oval;
						System.out.println("  > initConfig " + msg);
						FileUtil.printLog(LOG_FILEPATH, "initConfig", msg);
					} else if (oval.startsWith("O,") || oval.startsWith("P,")) {
						oval = "C," + oval.substring(2);
						orderMap.put(key, oval);
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
	 * 設定ファイルを読み込む。不正なレコードは無視される。
	 */
	private void readConfig() {
		configMap = new TreeMap<>();
		List<String> lines = FileUtil.readAllLines(CFG_FILEPATH);
		System.out.println("--- " + CFG_FILEPATH + " ---");
		System.out.println("MainEntryOrder_r4.readConfig(): lines.size=" + lines.size());
		for (String s : lines) {
			if (s.startsWith("#")) {
				continue;
			}
			String[] cols = StringUtil.splitTab(s);
			if (cols.length != MAX_COLS) {
				System.out.println("Warning: SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			configMap.put(cols[0], cols[1]);
			System.out.println("  " + cols[0] + ": " + cols[1]);
		}
	}

	/**
	 * 注文約定情報ファイルを読み込む。不正なレコードは無視される。
	 */
	private void readOrders() {
		orderMap = new TreeMap<>();
		List<String> lines = FileUtil.readAllLines(TXT_FILEPATH);
		for (String s : lines) {
			if (s.startsWith("#")) {
				continue;
			}
			String[] cols = StringUtil.splitTab(s);
			if (cols.length != MAX_COLS) {
				System.out.println("Warning SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			orderMap.put(cols[0], cols[1]);
		}
		System.out.println("MainEntryOrder_r4.readOrders(): orderMap.size=" + orderMap.size());
		for (String key : orderMap.keySet()) {
			String val = orderMap.get(key);
			System.out.println("  " + key + ": " + val);
		}
	}

	/**
	 * 注文約定情報ファイルを書き込む。
	 */
	private void writeOrders() {
		System.out.println("MainEntryOrder_r4.writeOrders(): orderMap.size=" + orderMap.size());
		List<String> lines = new ArrayList<>();
		lines.add("# orderId   " + TAB + "value");
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
				String val = orderMap.get(key);
				lines.add(key + TAB + val);
				System.out.println("  " + key + ": " + val);
			}
			lines.add("");
		}
		FileUtil.writeAllLines(TXT_FILEPATH, lines);
	}

	/**
	 * 注文情報を再取得する。
	 */
	private void refreshOrders() {
		try {
			posLogic.execute();
		} catch (ApiException e) {
			e.printStackTrace();
		}
		try {
			entryOrdersLogic.execute();
		} catch (ApiException e) {
			e.printStackTrace();
		}
	}

}
