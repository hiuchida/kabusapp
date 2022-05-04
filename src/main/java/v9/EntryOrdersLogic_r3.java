package v9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.api.OrderApi;
import io.swagger.client.model.OrderSuccess;
import io.swagger.client.model.OrdersSuccess;
import io.swagger.client.model.OrdersSuccessDetails;
import io.swagger.client.model.RequestCancelOrder;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import util.DateTimeUtil;
import util.FileUtil;
import util.StringUtil;

/**
 * 新規注文情報を管理する。
 */
public class EntryOrdersLogic_r3 {
	private static final String TRADE_PASSWORD = SendOrderConfig_r3.getPassword();

	/**
	 * タブ文字。
	 */
	public static final String TAB = "\t";

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 新規注文情報を保存したファイルパス。事前に準備し、uniqIdが振られて更新される。
	 */
	private static final String TXT_FILEPATH = DIRPATH + "EntryOrdersLogic_r3.txt";
	/**
	 * 新規注文ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "EntryOrdersLogic_r3.log";

	/**
	 * 新規注文情報クラス。
	 */
	public static class OrderInfo implements Comparable<OrderInfo> {
		/**
		 * 登録済、未発注。
		 */
		public static final int STATE_NOT_ORDER = -1;
		/**
		 * 発注済、注文ステータス不明。
		 */
		public static final int STATE_UNKNOWN = 0;
		/**
		 * 発注済。API:待機（発注待機）。
		 */
		public static final int STATE_WAIT = 1;
		/**
		 * 発注済。API:処理中（発注送信中）。
		 */
		public static final int STATE_SEND_ORDER = 2;
		/**
		 * 発注済。API:処理済（発注済・訂正済）。
		 */
		public static final int STATE_ORDER = 3;
		/**
		 * 発注済。API:訂正取消送信中。
		 */
		public static final int STATE_SEND_CANCEL = 4;
		/**
		 * 約定済。API:終了（発注エラー・取消済・全約定・失効・期限切れ）。
		 */
		public static final int STATE_FINISH = 5;
		/**
		 * 注文情報からキャンセル済。
		 */
		public static final int STATE_CANCEL = 6;
		/**
		 * 注文情報から返済済。
		 */
		public static final int STATE_CLOSE = 7;
		/**
		 * 注文情報から削除済（終了）。
		 */
		public static final int STATE_FINISH_DELETE = 95;
		/**
		 * 注文情報から削除済（キャンセル）。
		 */
		public static final int STATE_CANCEL_DELETE = 96;
		/**
		 * 注文情報から削除済（返済）。
		 */
		public static final int STATE_CLOSE_DELETE = 97;
		/**
		 * 新規注文情報ファイルのカラム数。
		 */
		public static final int MAX_COLS = 9;
		/**
		 * ユニークID。
		 */
		public String uniqId;
		/**
		 * 注文番号(ID)。
		 */
		public String orderId;
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
		 * @param uniqId ユニークID。
		 * @param price  値段(Price)。
		 * @param qty    発注数量(OrderQty)。
		 * @param side   売買区分(Side)。
		 */
		public OrderInfo(String uniqId, int price, int qty, String side) {
			this.uniqId = uniqId;
			this.orderId = "????????????????????";
			this.state = STATE_NOT_ORDER;
			this.price = price;
			this.orderQty = qty;
			this.side = side;
			this.createDate = System.currentTimeMillis();
			this.updateDate = System.currentTimeMillis();
			this.executionIds = "";
		}

		/**
		 * コンストラクタ（新規注文情報レコード）。
		 * 
		 * @param cols 新規注文情報ファイルの1レコードの全てのカラム文字列。
		 */
		public OrderInfo(String[] cols) {
			int i = 0;
			this.uniqId = cols[i++];
			this.orderId = cols[i++];
			this.state = StringUtil.parseInt(cols[i++]);
			this.price = StringUtil.parseInt(cols[i++]);
			this.orderQty = StringUtil.parseInt(cols[i++]);
			this.side = "" + StringUtil.parseInt(cols[i++]);
			this.createDate = StringUtil.parseLong(cols[i++]);
			this.updateDate = StringUtil.parseLong(cols[i++]);
			this.executionIds = cols[i++];
		}

		/**
		 * 新規注文情報ファイルのヘッダ文字列を生成する。
		 * 
		 * @return ヘッダ文字列。
		 */
		public static String toHeaderString() {
			StringBuilder sb = new StringBuilder();
			sb.append("# ");
			sb.append("uniqId     ").append(TAB);
			sb.append("orderId             ").append(TAB);
			sb.append("state").append(TAB);
			sb.append("price").append(TAB);
			sb.append("qty").append(TAB);
			sb.append("side").append(TAB);
			sb.append("createDate                            ").append(TAB);
			sb.append("updateDate                            ").append(TAB);
			sb.append("executionIds");
			return sb.toString();
		}

		/**
		 * インスタンスの主キー(uniqId)を取得する。
		 * 
		 * @return 主キー。
		 */
		public String getKey() {
			return uniqId;
		}

		/**
		 * 新規注文情報ファイルのレコード文字列を生成する。
		 * 
		 * @return レコード文字列。
		 */
		public String toLineString() {
			StringBuilder sb = new StringBuilder();
			sb.append(uniqId).append(TAB);
			sb.append(orderId).append(TAB);
			sb.append(state).append(TAB);
			sb.append(price).append(TAB);
			sb.append(orderQty).append(TAB);
			sb.append(side).append("(").append(StringUtil.sideStr(side)).append(")").append(TAB);
			sb.append(createDate).append("(").append(DateTimeUtil.toString(createDate)).append(")").append(TAB);
			sb.append(updateDate).append("(").append(DateTimeUtil.toString(updateDate)).append(")").append(TAB);
			sb.append(executionIds);
			return sb.toString();
		}

		@Override
		public int compareTo(OrderInfo that) {
			String key1 = this.uniqId;
			String key2 = that.uniqId;
			return key1.compareTo(key2);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{uniqId=").append(uniqId);
			sb.append(", orderId=").append(orderId);
			sb.append(", price=").append(price);
			sb.append("}");
			return sb.toString();
		}
	}

	/**
	 * 認証済TOKEN。
	 */
	private String X_API_KEY;
	
	/**
	 * 新規注文情報のマップ。
	 */
	private Map<String, OrderInfo> orderMap;
	/**
	 * orderIdとuniqIdのマップ。
	 */
	private Map<String, String> idMap;
	/**
	 * 削除対象の新規注文情報キーのセット。
	 */
	private Set<String> orderKeySet;

	/**
	 * 建玉情報を管理する。
	 */
	private PositionsLogic_r3 posLogic;

	/**
	 * 情報API。
	 */
	private InfoApi infoApi = new InfoApi();

	/**
	 * 注文API。
	 */
	private OrderApi orderApi = new OrderApi();

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public EntryOrdersLogic_r3(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
		this.posLogic = new PositionsLogic_r3(X_API_KEY);
	}

	/**
	 * 新規注文情報を更新する。
	 * 
	 * @return 新規注文情報リスト。
	 * @throws ApiException
	 */
	public List<OrderInfo> execute() throws ApiException {
		posLogic.execute();
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
		System.out.println("EntryOrdersLogic_r3.execute(): response.size=" + response.size());
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
					+ " " + exchange + " " + cashMargin + " " + price + StringUtil.sideStr(side) + " x" + orderQty + " " + executionIds);
			if (exchange != 2 && exchange != 23 && exchange != 24) { // 先物OP以外
				continue;
			}
			if (cashMargin != 2) { // 新規以外
				continue;
			}
			String uniqId = idMap.get(orderId);
			if (uniqId == null) {
				continue;
			}
			OrderInfo oi = orderMap.get(uniqId);
			if (oi != null) {
				int oldState = oi.state;
				if (OrderInfo.STATE_UNKNOWN <= oi.state && oi.state < OrderInfo.STATE_FINISH) {
					oi.state = state;
					oi.executionIds = executionIds;
					oi.updateDate = System.currentTimeMillis();
				} else if (oi.state == OrderInfo.STATE_FINISH) {
					if (oi.executionIds.length() == 0) {
						oi.state = OrderInfo.STATE_CANCEL;
					} else {
						if (!posLogic.isValidExecutionId(executionIds)) {
							oi.state = OrderInfo.STATE_CLOSE;
						}
					}
				}
				if (oldState != oi.state) {
					String msg = "changeState " + oi.uniqId + " " + oi.orderId + " " + oldState + "->" + oi.state;
					System.out.println("  > execute " + msg);
					FileUtil.printLog(LOG_FILEPATH, "execute", msg);
				}
				orderKeySet.remove(uniqId);
			}
		}
		deleteOrders();
		writeOrders();
		List<OrderInfo> list = new ArrayList<>();
		for (OrderInfo oi : orderMap.values()) {
			list.add(oi);
		}
		return list;
	}

	/**
	 * 注文依頼を追加する。
	 * 
	 * @param price 値段(Price)。
	 * @param qty   発注数量(OrderQty)。
	 * @param side  売買区分(Side)。
	 * @return ユニークID。
	 */
	public String addOrder(int price, int qty, String side) {
		long tim = System.currentTimeMillis();
		while (orderMap.containsKey("" + tim)) {
			tim++;
		}
		String key = "" + tim;
		OrderInfo oi = new OrderInfo(key, price, qty, side);
		orderMap.put(key, oi);
		if (oi.state != OrderInfo.STATE_NOT_ORDER) {
			idMap.put(oi.orderId, oi.uniqId);
		}
		orderKeySet.add(key);
		String msg = "create " + key + " " + oi.orderId + " " + oi.state;
		System.out.println("  > addOrder " + msg);
		FileUtil.printLog(LOG_FILEPATH, "addOrder", msg);
		return oi.uniqId;
	}

	/**
	 * メモリ上の新規注文情報を返す。
	 * 
	 * @param uniqId ユニークID。
	 * @return 新規注文情報。
	 * @throws ApiException
	 */
	public OrderInfo getOrder(String uniqId) {
		return orderMap.get(uniqId);
	}

	/**
	 * 注文取消を実行する。
	 * 
	 * @param orderId 注文番号(ID)。
	 * @param msg     ログメッセージ。
	 * @throws ApiException
	 */
	public void cancelOrder(String orderId, String msg) throws ApiException {
		FileUtil.printLog(LOG_FILEPATH, "cancelOrder", msg);
		
		RequestCancelOrder body = new RequestCancelOrder();
		body.setPassword(TRADE_PASSWORD);
		body.setOrderId(orderId);
		OrderSuccess response = orderApi.cancelorderPut(body, X_API_KEY);
		try {
			Thread.sleep(240); // 4.2req/sec
		} catch (Exception e) {
		}
		System.out.println(response);
	}

	/**
	 * 注文発注を実行する。
	 * 
	 * @param oi     新規注文情報。
	 * @param body   注文発注（先物）情報。
	 * @param msg    ログメッセージ。
	 * @return 注文番号(ID)。
	 * @throws ApiException
	 */
	public String sendOrder(OrderInfo oi, RequestSendOrderDerivFuture body, String msg) throws ApiException {
		FileUtil.printLog(LOG_FILEPATH, "sendOrder", msg);
		
		body.setPassword(TRADE_PASSWORD);
		OrderSuccess response = orderApi.sendoderFuturePost(body, X_API_KEY);
		System.out.println(response);
        try {
        	Thread.sleep(240); // 4.2req/sec
        } catch (Exception e) {
        }
        String orderId = response.getOrderId();
        int oldState = oi.state;
		oi.orderId = orderId;
		if (oi.state == OrderInfo.STATE_NOT_ORDER) {
			oi.state = OrderInfo.STATE_UNKNOWN;
		}
        idMap.put(orderId, oi.uniqId);
        if (oldState != oi.state) {
        	msg = "changeState " + oi.uniqId + " " + oi.orderId + " " + oldState + "->" + oi.state;
        	System.out.println("  > sendOrder " + msg);
        	FileUtil.printLog(LOG_FILEPATH, "sendOrder", msg);
        }
        return orderId;
	}

	/**
	 * 新規注文情報ファイルを読み込む。不正なレコードは無視される。
	 */
	public void readOrders() {
		orderMap = new TreeMap<>();
		idMap = new TreeMap<>();
		orderKeySet = new TreeSet<>();
		List<String> lines = FileUtil.readAllLines(TXT_FILEPATH);
		List<String> newLines = new ArrayList<>();
		for (String s : lines) {
			if (s.startsWith("#")) {
				continue;
			}
			String[] cols = StringUtil.splitTab(s);
			if (cols[0].length() == 0) {
				if (cols.length < 2 || cols[1].length() == 0) {
					continue;
				}
				newLines.add(cols[1]);
				continue;
			}
			if (cols.length != OrderInfo.MAX_COLS) {
				System.out.println("Warning: SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			OrderInfo oi = new OrderInfo(cols);
			String key = oi.getKey();
			orderMap.put(key, oi);
			if (oi.state != OrderInfo.STATE_NOT_ORDER) {
				idMap.put(oi.orderId, oi.uniqId);
			}
			orderKeySet.add(key);
		}
		for (String s : newLines) {
			String[] flds = StringUtil.splitComma(s);
			if (flds.length != 3) {
				System.out.println("Warning: SKIP new order=" + s);
				continue;
			}
			int price = StringUtil.parseInt(flds[0]);
			String side = StringUtil.sideCode(flds[1]);
			int qty = StringUtil.parseInt(flds[2]);
			addOrder(price, qty, side);
		}
		System.out.println("EntryOrdersLogic_r3.readOrders(): orderMap.size=" + orderMap.size());
		for (String key : orderMap.keySet()) {
			OrderInfo oi = orderMap.get(key);
			System.out.println("  " + key + ": " + oi);
		}
	}

	/**
	 * 終了済の注文を論理削除する。
	 */
	private void deleteOrders() {
		if (orderKeySet.size() > 0) {
			System.out.println("EntryOrdersLogic_r3.deleteOrders(): orderKeySet.size=" + orderKeySet.size());
			for (String key : orderKeySet) {
				OrderInfo oi = orderMap.get(key);
				int oldState = oi.state;
				if (OrderInfo.STATE_WAIT <= oi.state && oi.state <= OrderInfo.STATE_FINISH) {
					oi.state = OrderInfo.STATE_FINISH_DELETE;
				} else if (oi.state == OrderInfo.STATE_FINISH_DELETE) {
					if (oi.executionIds.length() == 0) {
						oi.state = OrderInfo.STATE_CANCEL_DELETE;
					} else {
						if (!posLogic.isValidExecutionId(oi.executionIds)) {
							oi.state = OrderInfo.STATE_CLOSE_DELETE;
						} else {
							continue;
						}
					}
				} else if (oi.state == OrderInfo.STATE_CANCEL) {
					oi.state = OrderInfo.STATE_CANCEL_DELETE;
				} else if (oi.state == OrderInfo.STATE_CLOSE) {
					oi.state = OrderInfo.STATE_CLOSE_DELETE;
				} else {
					continue;
				}
				if (oldState != oi.state) {
					String msg = "changeState " + oi.uniqId + " " + oi.orderId + " " + oldState + "->" + oi.state;
					System.out.println("  > deleteOrders " + msg);
					FileUtil.printLog(LOG_FILEPATH, "deleteOrders", msg);
				}
			}
		}
	}

	/**
	 * 新規注文情報ファイルを書き込む。
	 */
	public void writeOrders() {
		System.out.println("EntryOrdersLogic_r3.writeOrders(): orderMap.size=" + orderMap.size());
		List<String> lines = new ArrayList<>();
		lines.add(OrderInfo.toHeaderString());
		List<OrderInfo> list = new ArrayList<>();
		list.addAll(orderMap.values());
		Collections.sort(list);
		for (OrderInfo oi : list) {
			lines.add(oi.toLineString());
			String key = oi.getKey();
			System.out.println("  " + key + ": " + oi);
		}
		FileUtil.writeAllLines(TXT_FILEPATH, lines);
	}

}
