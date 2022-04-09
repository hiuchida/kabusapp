package v6;

import io.swagger.client.ApiException;
import io.swagger.client.api.OrderApi;
import io.swagger.client.model.OrderSuccess;
import io.swagger.client.model.RequestCancelOrder;
import v4.LockedAuthorizedToken;

/**
 * 注文取り消しする。
 */
public class MainCancelOrder {
	private static final String TRADE_PASSWORD = SendOrderConfig.getPassword();
	private static String X_API_KEY;
	private static OrderApi orderApi = new OrderApi();

	public static void main(String[] args) throws ApiException {
		X_API_KEY = LockedAuthorizedToken.lockToken();
		try {
			String orderId = "20220409A02N73777873";  // 注文番号
			RequestCancelOrder body = new RequestCancelOrder();
			body.setPassword(TRADE_PASSWORD);
			body.setOrderId(orderId);
			OrderSuccess response = orderApi.cancelorderPut(body, X_API_KEY);
			System.out.println(response);
		} finally {
			LockedAuthorizedToken.unlockToken();
		}
	}

}
