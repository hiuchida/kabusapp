package v6;

import api.consts.SideCode;
import api.consts.deliv.ExchangeDCode;
import api.consts.deliv.FrontOrderTypeDCode;
import api.consts.deliv.TimeInForceCode;
import api.consts.deliv.TradeTypeCode;
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
				body.setExchange(ExchangeDCode.日通し.intValue());
				body.setTradeType(TradeTypeCode.新規.intValue());
				body.setTimeInForce(TimeInForceCode.FAS.intValue());
				body.setSide("" + SideCode.買.intValue());
				body.setQty(1); // 注文数量
				body.setFrontOrderType(FrontOrderTypeDCode.指値.intValue());
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
