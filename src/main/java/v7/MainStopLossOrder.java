package v7;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.ApiException;
import io.swagger.client.model.PositionsDeriv;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import io.swagger.client.model.RequestSendOrderDerivFutureReverseLimitOrder;
import util.ExchangeUtil;
import util.FileUtil;
import util.StringUtil;
import v7.PositionsLogic.ExecutionInfo;
import v7.PositionsLogic.PosInfo;

/**
 * ストップロス注文ツール。
 */
public class MainStopLossOrder {
	/**
	 * 建玉の価格からの値幅。
	 */
	private static final int TRIGGER_PRICE_RANGE = 1000; // TODO 逆指値1000円

	/**
	 * タブ文字。
	 */
	public static final String TAB = "\t";

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * ストップロス注文ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "MainStopLossOrder.log";

	/**
	 * ストップロス注文ツール。
	 * 
	 * @param args 起動パラメータ。
	 * @throws ApiException
	 */
	public static void main(String[] args) throws ApiException {
		String X_API_KEY = LockedAuthorizedToken.lockToken();
		try {
			new MainStopLossOrder(X_API_KEY).execute();
		} finally {
			LockedAuthorizedToken.unlockToken();
		}
	}

	/**
	 * 認証済TOKEN。
	 */
//	private String X_API_KEY;
	
	/**
	 * 注文約定情報を管理する。
	 */
	private OrdersLogic orderLogic;

	/**
	 * 建玉情報を管理する。
	 */
	private PositionsLogic posLogic;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public MainStopLossOrder(String X_API_KEY) {
//		this.X_API_KEY = X_API_KEY;
		this.orderLogic = new OrdersLogic(X_API_KEY);
		this.posLogic = new PositionsLogic(X_API_KEY);
	}

	/**
	 * ストップロス注文。
	 */
	public void execute() throws ApiException {
		orderLogic.execute();
		posLogic.execute();
		List<PosInfo> posList = posLogic.getList();
		int exchange = ExchangeUtil.now();
		if (exchange > 0) {
			for (PosInfo pi : posList) {
				for (ExecutionInfo ei : pi.executionList) {
					String holdId = ei.executionId;
					if ((ei.leavesQty - ei.holdQty) <= 0) {
						continue;
					}
					sendCloseOrder(pi, ei, exchange, holdId);
				}
			}
			orderLogic.writeOrders();
		}
	}

	/**
	 * 返済注文を実行する。
	 * 
	 * @param pi       建玉情報。
	 * @param ei       約定数量情報。
	 * @param exchange 市場コード（Exchange）。
	 * @param holdId   約定番号（ExecutionID）。
	 * @throws ApiException 
	 */
	private void sendCloseOrder(PosInfo pi, ExecutionInfo ei, int exchange, String holdId) throws ApiException {
		int triggerPrice = triggerPrice(pi);
		RequestSendOrderDerivFuture body = new RequestSendOrderDerivFuture();
		body.setSymbol(pi.code);
		body.setExchange(exchange);
		body.setTradeType(2); // 返済
		body.setTimeInForce(2); // FAK
		body.setSide(StringUtil.sideReturn(pi.side));
		body.setQty(ei.leavesQty - ei.holdQty);
		List<PositionsDeriv> pdl = new ArrayList<>();
		{
			PositionsDeriv pd = new PositionsDeriv();
			pd.setHoldID(holdId);
			pd.setQty(body.getQty());
			pdl.add(pd);
		}
		body.setClosePositions(pdl);
		body.setFrontOrderType(30); // 逆指値
		body.setPrice(0.0); // 逆指値時0円
		body.setExpireDay(0); // 当日
		RequestSendOrderDerivFutureReverseLimitOrder rlo = new RequestSendOrderDerivFutureReverseLimitOrder();
		{
			rlo.setTriggerPrice((double) triggerPrice);
			rlo.setUnderOver(StringUtil.underOver(body.getSide()));
			rlo.setAfterHitOrderType(1); // 成行
			rlo.setAfterHitPrice(0.0); // 成行時0円
		}
		body.setReverseLimitOrder(rlo);
		
		String msg = "code=" + body.getSymbol() + ", exchange=" + body.getExchange() + ", price=" + triggerPrice
				+ StringUtil.sideStr(body.getSide()) + ", qty=" + body.getQty() + ", holdId=" + holdId;
		System.out.println("  > sendOrder " + msg);
		FileUtil.printLog(LOG_FILEPATH, "sendCloseOrder", msg);
		orderLogic.sendOrder(body, holdId, msg);
	}

	/**
	 * トリガ価格を取得する。
	 * 
	 * @param pi 建玉情報。
	 * @return トリガ価格。
	 */
	private int triggerPrice(PosInfo pi) {
		int sign = StringUtil.sign(pi.side);
		int price = pi.price + -1 * TRIGGER_PRICE_RANGE * sign;
		return price;
	}

}
