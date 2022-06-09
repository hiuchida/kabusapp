package v6;

import java.util.ArrayList;
import java.util.List;

import api.consts.SideCode;
import api.consts.UnderOverCode;
import api.consts.deliv.AfterHitOrderTypeDCode;
import api.consts.deliv.ExchangeDCode;
import api.consts.deliv.FrontOrderTypeDCode;
import api.consts.deliv.TimeInForceCode;
import api.consts.deliv.TradeTypeCode;
import io.swagger.client.ApiException;
import io.swagger.client.api.OrderApi;
import io.swagger.client.model.OrderSuccess;
import io.swagger.client.model.PositionsDeriv;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import io.swagger.client.model.RequestSendOrderDerivFutureReverseLimitOrder;
import v4.LockedAuthorizedToken;

/**
 * 先物を返済注文する。
 */
public class MainCloseOrder {
	private static final String TRADE_PASSWORD = SendOrderConfig.getPassword();
	private static String X_API_KEY;
    private static OrderApi orderApi = new OrderApi();

	/**
	 * 逆指値に成行を指定
	 */
	public static void main(String[] args) throws ApiException {
		X_API_KEY = LockedAuthorizedToken.lockToken();
		try {
	        RequestSendOrderDerivFuture body = new RequestSendOrderDerivFuture();
	        body.setPassword(TRADE_PASSWORD);
	        body.setSymbol("167060019"); // 日経225mini 22/06
	        body.setExchange(ExchangeDCode.日中.intValue());
			body.setTradeType(TradeTypeCode.返済.intValue());
	        body.setTimeInForce(TimeInForceCode.FAK.intValue());
	        body.setSide("" + SideCode.売.intValue());
	        body.setQty(1); // 注文数量
	        List<PositionsDeriv> pdl = new ArrayList<>();
	        {
	        	PositionsDeriv pd = new PositionsDeriv();
	        	pd.setHoldID("E2022040601ZS8"); // 返済建玉ID
	        	pd.setQty(1); // 返済建玉数量
	        	pdl.add(pd);
	        }
	        body.setClosePositions(pdl);
	        body.setFrontOrderType(FrontOrderTypeDCode.逆指値.intValue());
	        body.setPrice(0.0); // 注文価格
	        body.setExpireDay(0); // 注文有効期限
			RequestSendOrderDerivFutureReverseLimitOrder rlo = new RequestSendOrderDerivFutureReverseLimitOrder();
			{ // 値段が25000円以下になったら、MO(FAK)で注文発注
				rlo.setTriggerPrice(25000.0); // トリガー価格
				rlo.setUnderOver(UnderOverCode.以下.intValue());
				rlo.setAfterHitOrderType(AfterHitOrderTypeDCode.成行.intValue());
				rlo.setAfterHitPrice(0.0); // 成行は0円
			}
			body.setReverseLimitOrder(rlo);
	        OrderSuccess response = orderApi.sendoderFuturePost(body, X_API_KEY);
	        System.out.println(response);        
		} finally {
			LockedAuthorizedToken.unlockToken();
		}
	}

	/**
	 * 逆指値に指値を指定
	 */
	public static void sample1(String[] args) throws ApiException {
		X_API_KEY = LockedAuthorizedToken.lockToken();
		try {
	        RequestSendOrderDerivFuture body = new RequestSendOrderDerivFuture();
	        body.setPassword(TRADE_PASSWORD);
	        body.setSymbol("167060019"); // 日経225mini 22/06
	        body.setExchange(ExchangeDCode.日通し.intValue());
			body.setTradeType(TradeTypeCode.返済.intValue());
	        body.setTimeInForce(TimeInForceCode.FAS.intValue());
	        body.setSide("" + SideCode.売.intValue());
	        body.setQty(1); // 注文数量
	        List<PositionsDeriv> pdl = new ArrayList<>();
	        {
	        	PositionsDeriv pd = new PositionsDeriv();
	        	pd.setHoldID("E2022040601ZS8"); // 返済建玉ID
	        	pd.setQty(1); // 返済建玉数量
	        	pdl.add(pd);
	        }
	        body.setClosePositions(pdl);
	        body.setFrontOrderType(FrontOrderTypeDCode.逆指値.intValue());
	        body.setPrice(0.0); // 注文価格
	        body.setExpireDay(0); // 注文有効期限
			RequestSendOrderDerivFutureReverseLimitOrder rlo = new RequestSendOrderDerivFutureReverseLimitOrder();
			{ // 値段が25000円以下になったら、指値25010円で注文発注
				rlo.setTriggerPrice(25000.0); // トリガー価格
				rlo.setUnderOver(UnderOverCode.以下.intValue());
				rlo.setAfterHitOrderType(AfterHitOrderTypeDCode.指値.intValue());
				rlo.setAfterHitPrice(25010.0); // 指値価格
			}
			body.setReverseLimitOrder(rlo);
	        OrderSuccess response = orderApi.sendoderFuturePost(body, X_API_KEY);
	        System.out.println(response);        
		} finally {
			LockedAuthorizedToken.unlockToken();
		}
	}

}
