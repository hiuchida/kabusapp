package v9_2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import api.CancelorderApi;
import api.OrdersApi;
import api.SendoderFutureApi;
import io.swagger.client.ApiException;
import io.swagger.client.model.OrderSuccess;
import io.swagger.client.model.OrdersSuccess;
import io.swagger.client.model.RequestCancelOrder;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import util.FileUtil;
import util.SendOrderConfigUtil;
import util.StringUtil;

/**
 * 返済注文約定情報を管理する。
 */
public class CloseOrdersLogic_r4 {
	private static final String TRADE_PASSWORD = SendOrderConfigUtil.getPassword();

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 返済注文情報を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String TXT_FILEPATH = DIRPATH + "CloseOrdersLogic_r4.txt";
	/**
	 * 返済注文情報の削除情報を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String DEL_FILEPATH = DIRPATH + "CloseOrdersLogic_r4.del";
	/**
	 * 返済注文情報ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "CloseOrdersLogic_r4.log";
	/**
	 * 返済注文情報ファイルのカラム数。
	 */
	public static final int MAX_COLS = 3;

	/**
	 * 返済注文情報クラス。
	 */
	public static class CloseInfo {
		/**
		 * 注文番号(ID)。
		 */
		public String orderId;
		/**
		 * 約定番号（ExecutionID）。
		 */
		public String executionId;
		/**
		 * コメント。
		 */
		public String comment;

		/**
		 * コンストラクタ。
		 * 
		 * @param orderId     注文番号(ID)。
		 * @param executionId 約定番号（ExecutionID）。
		 * @param comment     コメント。
		 */
		public CloseInfo(String orderId, String executionId, String comment) {
			this.orderId = orderId;
			this.executionId = executionId;
			this.comment = comment;
		}

		/**
		 * 返済注文情報ファイルのヘッダ文字列を生成する。
		 * 
		 * @return ヘッダ文字列。
		 */
		public static String toHeaderString() {
			String[] sa = new String[3];
			int i = 0;
			sa[i++] = "orderId           ";
			sa[i++] = "executionId";
			sa[i++] = "comment";
			String val = "# " + StringUtil.joinTab(sa);
			return val;
		}

		/**
		 * インスタンスの主キー(orderId)を取得する。
		 * 
		 * @return 主キー。
		 */
		public String getKey() {
			return orderId;
		}

		/**
		 * 注文依頼情報ファイルのレコード文字列を生成する。
		 * 
		 * @return レコード文字列。
		 */
		public String toLineString() {
			String[] sa = new String[3];
			int i = 0;
			sa[i++] = orderId;
			sa[i++] = executionId;
			sa[i++] = comment;
			String val = StringUtil.joinTab(sa);
			return val;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(orderId);
			sb.append(": ").append(executionId);
			sb.append(" (").append(comment).append(")");
			return sb.toString();
		}
	}

	/**
	 * 注文約定照会API。
	 */
	private OrdersApi ordersApi;

	/**
	 * 注文発注（先物）API。
	 */
	private SendoderFutureApi sendoderFutureApi;

	/**
	 * 注文取消API。
	 */
	private CancelorderApi cancelorderApi;

	/**
	 * 返済注文約定情報のマップ。
	 */
	private Map<String, CloseInfo> orderMap;
	/**
	 * 削除対象の返済注文約定情報キーのセット。
	 */
	private Set<String> orderKeySet;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public CloseOrdersLogic_r4(String X_API_KEY) {
		this.ordersApi = new OrdersApi(X_API_KEY);
		this.sendoderFutureApi = new SendoderFutureApi(X_API_KEY);
		this.cancelorderApi = new CancelorderApi(X_API_KEY);
	}

	/**
	 * 返済注文約定情報を更新する。
	 */
	public void execute() throws ApiException {
		readOrders();
		List<OrdersSuccess> response = ordersApi.get();
		System.out.println("CloseOrdersLogic_r4.execute(): response.size=" + response.size());
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
			System.out.println("  " + StringUtil.index(i + 1) + ": " + orderId + " " + code + " " + name + " " + state + " "
					+ exchange + " " + cashMargin + " " + price + " " + StringUtil.sideStr(side) + " " + orderQty);
			if (state == 5) { // 終了（発注エラー・取消済・全約定・失効・期限切れ）
				continue;
			}
			if (exchange != 2 && exchange != 23 && exchange != 24) { // 先物OP以外
				continue;
			}
			if (cashMargin != 3) { // 返済以外
				continue;
			}
			CloseInfo ci = orderMap.get(orderId);
			if (ci == null) {
				ci = new CloseInfo(orderId, "?", "# ?");
				orderMap.put(orderId, ci);
				String msg = "create " + orderId + " " + code + " " + name;
				System.out.println("  > " + msg);
				FileUtil.printLog(LOG_FILEPATH, "execute", msg);
			} else {
			}
			orderKeySet.remove(orderId);
		}
		deleteOrders();
		writeOrders();
	}

	/**
	 * 返済注文約定情報を検索する。
	 * 
	 * @param exectionId 約定番号(ExecutionID)。
	 * @return 注文番号(ID)。
	 */
	public String getOrderId(String exectionId) {
		for (String key : orderMap.keySet()) {
			CloseInfo ci = orderMap.get(key);
			if (exectionId.equals(ci.executionId)) {
				return key;
			}
		}
		return null;
	}

	/**
	 * 返済注文取消を実行する。
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
		OrderSuccess response = cancelorderApi.put(body);
		System.out.println(response);
        unregisterOrder(orderId);
	}

	/**
	 * 返済注文発注を実行する。
	 * 
	 * @param body   注文発注（先物）情報。
	 * @param holdId 約定番号（ExecutionID）。
	 * @param msg    ログメッセージ。
	 * @return 注文番号(ID)。
	 * @throws ApiException
	 */
	public String sendOrder(RequestSendOrderDerivFuture body, String holdId, String msg) throws ApiException {
		FileUtil.printLog(LOG_FILEPATH, "sendOrder", msg);
		
		body.setPassword(TRADE_PASSWORD);
		OrderSuccess response = sendoderFutureApi.post(body);
		System.out.println(response);
        String orderId = response.getOrderId();
        registerOrder(orderId, holdId, msg);
        return orderId;
	}

	/**
	 * 返済注文約定情報を登録する。
	 * 
	 * @param orderId 注文番号(ID)。
	 * @param holdId  約定番号（ExecutionID）。
	 * @param comment コメント。
	 */
	private void registerOrder(String orderId, String holdId, String comment) {
		String msg = "orderId=" + orderId + ", holdId=" + holdId;
		FileUtil.printLog(LOG_FILEPATH, "registerOrder", msg);
		System.out.println("  > registerOrder " + msg);

		CloseInfo ci = new CloseInfo(orderId, holdId, "# " + comment);
        orderMap.put(orderId, ci);
	}

	/**
	 * 返済注文約定情報を登録解除する。
	 * 
	 * @param orderId 注文番号(ID)。
	 */
	private void unregisterOrder(String orderId) {
		String msg = "orderId=" + orderId;
		FileUtil.printLog(LOG_FILEPATH, "unregisterOrder", msg);

		orderMap.remove(orderId);
	}

	/**
	 * 返済注文約定情報ファイルを読み込む。不正なレコードは無視される。
	 */
	private void readOrders() {
		orderMap = new TreeMap<>();
		orderKeySet = new TreeSet<>();
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
			CloseInfo ci = new CloseInfo(cols[0], cols[1], cols[2]);
			String key = ci.getKey();
			orderMap.put(key, ci);
			orderKeySet.add(key);
		}
		System.out.println("CloseOrdersLogic_r4.readOrders(): orderMap.size=" + orderMap.size());
		for (String key : orderMap.keySet()) {
			CloseInfo ci = orderMap.get(key);
			String val = ci.executionId;
			System.out.println("  " + key + ": " + val);
		}
	}

	/**
	 * 決済済の返済注文を削除する。
	 */
	private void deleteOrders() {
		if (orderKeySet.size() > 0) {
			try (PrintWriter pw = FileUtil.writer(DEL_FILEPATH, FileUtil.UTF8, true)) {
				System.out.println("CloseOrdersLogic_r4.deleteOrders(): orderKeySet.size=" + orderKeySet.size());
				for (String key : orderKeySet) {
					CloseInfo ci = orderMap.get(key);
					String line = ci.toLineString();
					FileUtil.printLogLine(pw, line);
					String msg = "delete orderId=" + key + ", holdId=" + ci.executionId;
					System.out.println("  > deleteOrders " + msg);
					FileUtil.printLog(LOG_FILEPATH, "deleteOrders", msg);
					orderMap.remove(key);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 返済注文約定情報ファイルを書き込む。
	 */
	public void writeOrders() {
		System.out.println("CloseOrdersLogic_r4.writeOrders(): orderMap.size=" + orderMap.size());
		List<String> lines = new ArrayList<>();
		lines.add(CloseInfo.toHeaderString());
		for (String key : orderMap.keySet()) {
			CloseInfo ci = orderMap.get(key);
			lines.add(ci.toLineString());
			String val = ci.executionId;
			System.out.println("  " + key + ": " + val);
		}
		FileUtil.writeAllLines(TXT_FILEPATH, lines);
	}

}
