package v9;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.swagger.client.ApiException;
import io.swagger.client.api.OrderApi;
import io.swagger.client.model.OrderSuccess;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import util.DateTimeUtil;
import util.FileUtil;
import util.StringUtil;
import v7.SendOrderConfig;

/**
 * 新規注文情報を管理する。
 */
public class EntryOrdersLogic {
	private static final String TRADE_PASSWORD = SendOrderConfig.getPassword();

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
	private static final String TXT_FILEPATH = DIRPATH + "EntryOrdersLogic.txt";
	/**
	 * 新規注文ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "EntryOrdersLogic.log";

	/**
	 * 新規注文情報クラス
	 */
	public static class OrderInfo implements Comparable<OrderInfo> {
		/**
		 * 登録済、未発注。
		 */
		public static final int STATE_NOT_ORDER = -1;
		/**
		 * 発注済、注文ステータス不明。
		 */
		public static final int STATE_ORDERED = 0;
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
		 * @param flds   フィールド配列。
		 */
		public OrderInfo(String uniqId, String[] flds) {
			this.uniqId = uniqId;
			this.orderId = "????????????????????";
			this.state = STATE_NOT_ORDER;
			this.price = StringUtil.parseInt(flds[0]);
			this.side = StringUtil.sideCode(flds[1]);
			this.orderQty = StringUtil.parseInt(flds[2]);
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
		 * インスタンスの主キー(orderId)を取得する。
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
	 * 注文API。
	 */
	private OrderApi orderApi = new OrderApi();

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public EntryOrdersLogic(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	/**
	 * メモリ上の新規注文情報リストを返す。
	 * 
	 * @return 新規注文情報リスト。
	 * @throws ApiException
	 */
	public List<OrderInfo> getList() {
		List<OrderInfo> list = new ArrayList<>();
		for (OrderInfo oi : orderMap.values()) {
			list.add(oi);
		}
		return list;
	}

	/**
	 * 注文発注を実行する。
	 * 
	 * @param body 注文発注（先物）情報。
	 * @param msg  ログメッセージ。
	 * @return 注文番号(ID)。
	 * @throws ApiException
	 */
	public String sendOrder(RequestSendOrderDerivFuture body, String msg) throws ApiException {
		FileUtil.printLog(LOG_FILEPATH, "sendOrder", msg);
		
		body.setPassword(TRADE_PASSWORD);
		OrderSuccess response = orderApi.sendoderFuturePost(body, X_API_KEY);
		System.out.println(response);
        try {
        	Thread.sleep(240); // 4.2req/sec
        } catch (Exception e) {
        }
        String orderId = response.getOrderId();
        return orderId;
	}

	/**
	 * 新規注文情報ファイルを読み込む。不正なレコードは無視される。
	 */
	public void readEntryOrders() {
		orderMap = new TreeMap<>();
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
		}
		for (String s : newLines) {
			String[] flds = s.split(",");
			if (flds.length != 3) {
				System.out.println("Warning: SKIP new order=" + s);
			}
			long tim = System.currentTimeMillis();
			while (orderMap.containsKey("" + tim)) {
				tim++;
			}
			String key = "" + tim;
			OrderInfo oi = new OrderInfo(key, flds);
			orderMap.put(key, oi);
		}
		System.out.println("EntryOrdersLogic.readEntryOrders(): orderMap.size=" + orderMap.size());
		for (String key : orderMap.keySet()) {
			OrderInfo oi = orderMap.get(key);
			System.out.println("  " + key + ": " + oi);
		}
	}

	/**
	 * 新規注文情報ファイルを書き込む。
	 */
	public void writeEntryOrders() {
		System.out.println("EntryOrdersLogic.writeEntryOrders(): orderMap.size=" + orderMap.size());
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
