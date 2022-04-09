package v6;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.api.OrderApi;
import io.swagger.client.model.OrderSuccess;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import io.swagger.client.model.SymbolNameSuccess;
import v4.LockedAuthorizedToken;

/**
 * 先物を新規注文する。
 */
public class MainOpenOrder {
	private static final String TRADE_PASSWORD = SendOrderConfig.getPassword();
	private static String X_API_KEY;
	private static InfoApi infoApi = new InfoApi();
	private static OrderApi orderApi = new OrderApi();

	public static void main(String[] args) throws ApiException {
		X_API_KEY = LockedAuthorizedToken.lockToken();
		try {
			String code = "";
			{
				int derivMonth = 202206; // 167060019:日経225mini 22/06
				SymbolNameSuccess sns = infoApi.symbolnameFutureGet(X_API_KEY, derivMonth, "NK225mini");
				System.out.println(sns);
				code = sns.getSymbol();
			}
			{
				RequestSendOrderDerivFuture body = new RequestSendOrderDerivFuture();
				body.setPassword(TRADE_PASSWORD);
				body.setSymbol(code);
				body.setExchange(2); // 日通し
				body.setTradeType(1); // 新規
				body.setTimeInForce(1); // FAS
				body.setSide("2"); // 買
				body.setQty(1); // 注文数量
				body.setFrontOrderType(20); // 指値
				body.setPrice(25000.0); // 注文価格
				body.setExpireDay(0); // 注文有効期限
				OrderSuccess response = orderApi.sendoderFuturePost(body, X_API_KEY);
				System.out.println(response);
			}
		} finally {
			LockedAuthorizedToken.unlockToken();
		}
	}

}
