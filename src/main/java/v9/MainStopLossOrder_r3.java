package v9;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.ApiException;
import io.swagger.client.model.PositionsDeriv;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import io.swagger.client.model.RequestSendOrderDerivFutureReverseLimitOrder;
import util.ExchangeUtil;
import util.FileUtil;
import util.StringUtil;
import v9.PositionsLogic_r3.ExecutionInfo;
import v9.PositionsLogic_r3.PosInfo;

/**
 * ストップロス注文ツール。
 */
public class MainStopLossOrder_r3 {
	/**
	 * 建玉の価格からの値幅。
	 */
	private static final int STOP_LOSS_PRICE_RANGE = 1000;

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
	private static final String LOG_FILEPATH = DIRPATH + "MainStopLossOrder_r3.log";

	/**
	 * ストップロス注文ツール。
	 * 
	 * @param args 起動パラメータ。
	 * @throws ApiException
	 */
	public static void main(String[] args) throws ApiException {
		String X_API_KEY = LockedAuthorizedToken_r3.lockToken();
		try {
			new MainStopLossOrder_r3(X_API_KEY).execute();
		} finally {
			LockedAuthorizedToken_r3.unlockToken();
		}
	}

	/**
	 * 注文約定情報を管理する。
	 */
	private CloseOrdersLogic_r3 closeOrderLogic;

	/**
	 * 建玉情報を管理する。
	 */
	private PositionsLogic_r3 posLogic;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public MainStopLossOrder_r3(String X_API_KEY) {
		this.closeOrderLogic = new CloseOrdersLogic_r3(X_API_KEY);
		this.posLogic = new PositionsLogic_r3(X_API_KEY);
	}

	/**
	 * ストップロス注文。
	 * 
	 * @throws ApiException 
	 */
	public void execute() throws ApiException {
		closeOrderLogic.execute();
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
			closeOrderLogic.writeOrders();
		}
	}

	/**
	 * 返済注文を実行する。
	 * 
	 * @param pi       建玉情報。
	 * @param ei       約定数量情報。
	 * @param exchange 市場コード（Exchange）。
	 * @param holdId   約定番号（ExecutionID）。
	 * @return 注文番号(ID)。
	 * @throws ApiException 
	 */
	private String sendCloseOrder(PosInfo pi, ExecutionInfo ei, int exchange, String holdId) throws ApiException {
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
		
		int sign = StringUtil.sign(pi.side);
		int delta = (triggerPrice - pi.price) * sign;
		String msg;
		{
			StringBuilder sb = new StringBuilder();
			sb.append("CLOSE:{").append(pi.code).append(" ").append(pi.name).append(" ");
			sb.append(StringUtil.exchangeStr(exchange));
			sb.append(" price=").append(pi.price).append(StringUtil.sideStr(pi.side));
			sb.append(", qty=").append(body.getQty());
			sb.append(", trigger=").append(triggerPrice).append(StringUtil.sideStr(body.getSide()));
			sb.append("(").append(delta).append(")");
			sb.append(", holdId=").append(holdId);
			sb.append("}");
			msg = sb.toString();
			System.out.println("  > sendCloseOrder " + msg);
			FileUtil.printLog(LOG_FILEPATH, "sendCloseOrder", msg);
		}
		String orderId = closeOrderLogic.sendOrder(body, holdId, msg);
		return orderId;
	}

	/**
	 * トリガ価格を取得する。
	 * 
	 * @param pi 建玉情報。
	 * @return トリガ価格。
	 */
	private int triggerPrice(PosInfo pi) {
		int sign = StringUtil.sign(pi.side);
		int price = pi.price + -1 * STOP_LOSS_PRICE_RANGE * sign;
		return price;
	}

}
