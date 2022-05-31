package v9;

import java.util.ArrayList;
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
import io.swagger.client.model.RequestCancelOrder;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import util.FileUtil;
import util.StringUtil;

/**
 * 返済注文約定情報を管理する。
 */
public class CloseOrdersLogic_r3 {
	private static final String TRADE_PASSWORD = SendOrderConfig_r3.getPassword();

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 返済注文情報を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String TXT_FILEPATH = DIRPATH + "CloseOrdersLogic_r3.txt";
	/**
	 * 返済注文情報ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "CloseOrdersLogic_r3.log";
	/**
	 * 返済注文情報ファイルのカラム数。
	 */
	public static final int MAX_COLS = 2;

	/**
	 * 認証済TOKEN。
	 */
	private String X_API_KEY;

	/**
	 * 情報API。
	 */
	private InfoApi infoApi = new InfoApi();

	/**
	 * 注文API。
	 */
	private OrderApi orderApi = new OrderApi();

	/**
	 * 返済注文約定情報のマップ。
	 */
	private Map<String, String> orderMap;
	/**
	 * 削除対象の返済注文約定情報キーのセット。
	 */
	private Set<String> orderKeySet;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public CloseOrdersLogic_r3(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	/**
	 * 返済注文約定情報を更新する。
	 */
	public void execute() throws ApiException {
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
		System.out.println("CloseOrdersLogic_r3.execute(): response.size=" + response.size());
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
			Integer cashMargin = order.getCashMargin();
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
			String val = orderMap.get(orderId);
			if (val == null) {
				val = "?";
				orderMap.put(orderId, val);
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
			String val = orderMap.get(key);
			if (exectionId.equals(val)) {
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
		OrderSuccess response = orderApi.cancelorderPut(body, X_API_KEY);
		System.out.println(response);
        try {
        	Thread.sleep(240); // 4.2req/sec
        } catch (Exception e) {
        }
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
		OrderSuccess response = orderApi.sendoderFuturePost(body, X_API_KEY);
		System.out.println(response);
        try {
        	Thread.sleep(240); // 4.2req/sec
        } catch (Exception e) {
        }
        String orderId = response.getOrderId();
        registerOrder(orderId, holdId);
        return orderId;
	}

	/**
	 * 返済注文約定情報を登録する。
	 * 
	 * @param orderId 注文番号(ID)。
	 * @param holdId  約定番号（ExecutionID）。
	 */
	private void registerOrder(String orderId, String holdId) {
		String msg = "orderId=" + orderId + ", holdId=" + holdId;
		FileUtil.printLog(LOG_FILEPATH, "registerOrder", msg);
		System.out.println("  > registerOrder " + msg);

        orderMap.put(orderId, holdId);
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
			orderMap.put(cols[0], cols[1]);
			orderKeySet.add(cols[0]);
		}
		System.out.println("CloseOrdersLogic_r3.readOrders(): orderMap.size=" + orderMap.size());
		for (String key : orderMap.keySet()) {
			String val = orderMap.get(key);
			System.out.println("  " + key + ": " + val);
		}
	}

	/**
	 * 決済済の返済注文を削除する。
	 */
	private void deleteOrders() {
		if (orderKeySet.size() > 0) {
			System.out.println("CloseOrdersLogic_r3.deleteOrders(): orderKeySet.size=" + orderKeySet.size());
			for (String key : orderKeySet) {
				String val = orderMap.get(key);
				String msg = "delete orderId=" + key + ", holdId=" + val;
				System.out.println("  > " + msg);
				FileUtil.printLog(LOG_FILEPATH, "deleteOrders", msg);
				orderMap.remove(key);
			}
		}
	}

	/**
	 * 返済注文約定情報ファイルを書き込む。
	 */
	public void writeOrders() {
		System.out.println("CloseOrdersLogic_r3.writeOrders(): orderMap.size=" + orderMap.size());
		List<String> lines = new ArrayList<>();
		String line = StringUtil.joinTab("orderId           ", "executionId");
		lines.add("# " + line);
		for (String key : orderMap.keySet()) {
			String val = orderMap.get(key);
			line = StringUtil.joinTab(key, val);
			lines.add(line);
			System.out.println("  " + key + ": " + val);
		}
		FileUtil.writeAllLines(TXT_FILEPATH, lines);
	}

}
