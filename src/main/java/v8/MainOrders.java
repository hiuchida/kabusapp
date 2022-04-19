package v8;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.OrdersSuccess;
import io.swagger.client.model.OrdersSuccessDetails;
import util.DateTimeUtil;
import util.FileUtil;
import util.StringUtil;
import v7.LockedAuthorizedToken;

/**
 * 注文約定照会を管理するツール。
 */
public class MainOrders {
	/**
	 * タブ文字。
	 */
	public static final String TAB = "\t";

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 注文約定情報を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String TXT_FILEPATH = DIRPATH + "MainOrders.txt";
	/**
	 * 注文約定情報ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "MainOrders.log";

	private static String X_API_KEY;

	private static InfoApi infoApi = new InfoApi();

	/**
	 * 注文約定情報クラス
	 */
	public static class OrderInfo implements Comparable<OrderInfo> {
		/**
		 * 注文約定情報ファイルのカラム数。
		 */
		public static final int MAX_COLS = 11;
		/**
		 * 注文番号(ID)。
		 */
		public String orderId;
		/**
		 * 銘柄コード(Symbol)。
		 */
		public String code;
		/**
		 * 銘柄名(SymbolName)。
		 */
		public String name;
		/**
		 * 状態(State)。
		 */
		public int state;
		/**
		 * 値段(Price)。
		 */
		public int price;
		/**
		 * 発注数量(OrderQty)。
		 */
		public int orderQty;
		/**
		 * 売買区分(Side)。
		 */
		public String side;
		/**
		 * 取引区分(CashMargin)。
		 */
		public int cashMargin;
		/**
		 * 生成日時。
		 */
		public long createDate;
		/**
		 * 更新日時。
		 */
		public long updateDate;
		/**
		 * 約定番号(ExecutionID)と値段(Price)と数量(Qty)のリスト。
		 */
		public String executionIds;

		/**
		 * コンストラクタ（新規作成）。
		 * 
		 * @param orderId 注文番号(ID)。
		 * @param code    銘柄コード(Symbol)。
		 * @param name    銘柄名(SymbolName)。
		 */
		public OrderInfo(String orderId, String code, String name) {
			this.orderId = orderId;
			this.code = code;
			this.name = name;
			this.createDate = System.currentTimeMillis();
		}

		/**
		 * コンストラクタ（注文約定情報レコード）。
		 * 
		 * @param cols 注文約定情報ファイルの1レコードの全てのカラム文字列。
		 */
		public OrderInfo(String[] cols) {
			int i = 0;
			this.orderId = cols[i++];
			this.code = cols[i++];
			this.name = cols[i++];
			this.state = StringUtil.parseInt(cols[i++]);
			this.price = StringUtil.parseInt(cols[i++]);
			this.orderQty = StringUtil.parseInt(cols[i++]);
			this.side = "" + StringUtil.parseInt(cols[i++]);
			this.cashMargin = StringUtil.parseInt(cols[i++]);
			this.createDate = StringUtil.parseLong(cols[i++]);
			this.updateDate = StringUtil.parseLong(cols[i++]);
			this.executionIds = cols[i++];
		}

		/**
		 * 注文約定情報ファイルのヘッダ文字列を生成する。
		 * 
		 * @return ヘッダ文字列。
		 */
		public static String toHeaderString() {
			StringBuilder sb = new StringBuilder();
			sb.append("# ");
			sb.append("orderId           ").append(TAB);
			sb.append("code     ").append(TAB);
			sb.append("name             ").append(TAB);
			sb.append("state").append(TAB);
			sb.append("price").append(TAB);
			sb.append("qty").append(TAB);
			sb.append("side").append(TAB);
			sb.append("cashMar").append(TAB);
			sb.append("createDate                            ").append(TAB);
			sb.append("updateDate                            ").append(TAB);
			sb.append("executionIds");
			return sb.toString();
		}

		/**
		 * 注文約定情報ファイルのレコード文字列を生成する。
		 * 
		 * @return レコード文字列。
		 */
		public String toLineString() {
			StringBuilder sb = new StringBuilder();
			sb.append(orderId).append(TAB);
			sb.append(code).append(TAB);
			sb.append(name).append(TAB);
			sb.append(state).append(TAB);
			sb.append(price).append(TAB);
			sb.append(orderQty).append(TAB);
			sb.append(side).append("(").append(StringUtil.sideStr(side)).append(")").append(TAB);
			sb.append(cashMargin).append(TAB);
			sb.append(createDate).append("(").append(DateTimeUtil.toString(createDate)).append(")").append(TAB);
			sb.append(updateDate).append("(").append(DateTimeUtil.toString(updateDate)).append(")").append(TAB);
			sb.append(executionIds);
			return sb.toString();
		}

		@Override
		public int compareTo(OrderInfo that) {
			String key1 = code + "_" + price + "_" + StringUtil.sideStr(side);
			String key2 = that.code + "_" + that.price + "_" + StringUtil.sideStr(that.side);
			return key1.compareTo(key2);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{orderId=").append(orderId);
			sb.append(", name=").append(name);
			sb.append(", price=").append(price);
			sb.append("}");
			return sb.toString();
		}
	}

	/**
	 * 注文約定情報のマップ。
	 */
	static Map<String, OrderInfo> orderMap;
	/**
	 * 削除対象の注文約定情報キーのセット。
	 */
	static Set<String> orderKeySet;

	public static void main(String[] args) throws ApiException {
		X_API_KEY = LockedAuthorizedToken.lockToken();
		try {
			readOrders();
	        String product = null;
			String idParam = null;
	        String updtime = null;
	        String details = null;
	        String symbol = null;
			String stateParam = null;
			String sideParam = null;
	        String cashmargin = null;
			List<OrdersSuccess> response = infoApi.ordersGet(X_API_KEY, product, idParam, updtime, details, symbol,
					stateParam, sideParam, cashmargin);
			System.out.println("main(): response.size=" + response.size());
			for (int i = 0; i < response.size(); i++) {
				OrdersSuccess order = response.get(i);
				String orderId = order.getID();
				String code = order.getSymbol();
				String name = order.getSymbolName();
				int price = (int) (double) order.getPrice();
				int orderQty = (int) (double) order.getOrderQty();
				String side = order.getSide();
				int state = order.getState();
				int exchange = order.getExchange();
				int cashMargin = order.getCashMargin();
				String executionIds = "";
				for (OrdersSuccessDetails osd : order.getDetails()) {
					String executionId = osd.getExecutionID();
					if (executionId != null) {
						int executionPrice = (int) (double) osd.getPrice();
						int executionQty = (int) (double) osd.getQty();
						if (executionIds.length() > 0) {
							executionIds = executionIds + ",";
						}
						executionIds = executionIds + executionId + ":" + executionPrice + "x" + executionQty;
					}
				}
				System.out.println("  " + StringUtil.index(i + 1) + ": " + orderId + " " + code + " " + name + " " + state
						+ " " + exchange + " " + cashMargin + " " + price + " x" + orderQty + " " + executionIds);
				if (exchange != 2 && exchange != 23 && exchange != 24) { // 先物OP以外
					continue;
				}
				OrderInfo oi = orderMap.get(orderId);
				if (oi == null) {
					oi = new OrderInfo(orderId, code, name);
					orderMap.put(orderId, oi);
					String msg = "create " + orderId + " " + code + " " + name;
					System.out.println("  > " + msg);
					FileUtil.printLog(LOG_FILEPATH, "main", msg);
				} else {
				}
				oi.state = state;
				oi.price = price;
				oi.orderQty = orderQty;
				oi.side = side;
				oi.cashMargin = cashMargin;
				oi.updateDate = System.currentTimeMillis();
				oi.executionIds = executionIds;
				orderKeySet.remove(orderId);
			}
			deleteOrders();
			writeOrders();
		} finally {
			LockedAuthorizedToken.unlockToken();
		}
	}

	/**
	 * 注文約定情報ファイルを読み込む。不正なレコードは無視される。
	 */
	private static void readOrders() {
		orderMap = new TreeMap<>();
		orderKeySet = new TreeSet<>();
		List<String> lines = FileUtil.readAllLines(TXT_FILEPATH);
		for (String s : lines) {
			if (s.startsWith("#")) {
				continue;
			}
			String[] cols = s.split(TAB);
			if (cols.length < OrderInfo.MAX_COLS) {
				System.out.println("Warning: SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			OrderInfo oi = new OrderInfo(cols);
			String key = oi.orderId;
			orderMap.put(key, oi);
			orderKeySet.add(key);
		}
		System.out.println("readOrders(): orderMap.size=" + orderMap.size());
		for (String key : orderMap.keySet()) {
			OrderInfo oi = orderMap.get(key);
			System.out.println("  " + key + ": " + oi);
		}
	}

	/**
	 * 決済済の注文約定は削除する。
	 */
	private static void deleteOrders() {
		if (orderKeySet.size() > 0) {
			System.out.println("deleteOrders(): orderKeySet.size=" + orderKeySet.size());
			for (String key : orderKeySet) {
				OrderInfo oi = orderMap.get(key);
				String msg = "delete " + key + " " + oi.name;
				System.out.println("  > " + msg);
				FileUtil.printLog(LOG_FILEPATH, "writeOrders", msg);
				orderMap.remove(key);
			}
		}
	}

	/**
	 * 注文約定情報ファイルを書き込む。
	 */
	private static void writeOrders() {
		System.out.println("writeOrders(): orderMap.size=" + orderMap.size());
		for (String key : orderMap.keySet()) {
			OrderInfo oi = orderMap.get(key);
			System.out.println("  " + key + ": " + oi);
		}
		List<String> lines = new ArrayList<>();
		lines.add(OrderInfo.toHeaderString());
		for (String key : orderMap.keySet()) {
			OrderInfo oi = orderMap.get(key);
			lines.add(oi.toLineString());
		}
		FileUtil.writeAllLines(TXT_FILEPATH, lines);
	}

}
