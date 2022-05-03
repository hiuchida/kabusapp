package api;

import java.lang.invoke.MethodHandles;

import io.swagger.client.ApiException;
import io.swagger.client.api.OrderApi;
import io.swagger.client.model.OrderSuccess;
import io.swagger.client.model.RequestCancelOrder;

/**
 * 注文取消API。
 */
public class CancelorderApi {
	/**
	 * API実行クラス。
	 */
	private Class<?> clazz = MethodHandles.lookup().lookupClass();
	/**
	 * 認証済TOKEN。
	 */
	private String X_API_KEY;

	/**
	 * 注文API。
	 */
	private OrderApi orderApi = new OrderApi();

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public CancelorderApi(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	/**
	 * 注文取消API。
	 * 
	 * @param body 注文取消情報。
	 * @return 注文取消情報。
	 * @throws ApiException
	 */
	public OrderSuccess put(RequestCancelOrder body) throws ApiException {
		try {
			OrderSuccess os = orderApi.cancelorderPut(body, X_API_KEY);
			try {
				Thread.sleep(240); // 4.2req/sec
			} catch (Exception e) {
			}
			return os;
		} catch (ApiException e) {
			ApiErrorLog.error(e, clazz, "put", toString(body));
			throw e;
		}
	}

	/**
	 * 文字列表現を取得する。
	 * 
	 * @param body 注文取消情報。
	 * @return 文字列表現。
	 */
	private String toString(RequestCancelOrder body) {
		return body.getOrderId();
	}

}
