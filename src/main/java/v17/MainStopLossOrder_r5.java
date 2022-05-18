package v17;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import api.ApiErrorLog;
import io.swagger.client.ApiException;
import io.swagger.client.model.PositionsDeriv;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import io.swagger.client.model.RequestSendOrderDerivFutureReverseLimitOrder;
import logic.SendMailLogic;
import util.Consts;
import util.ExchangeUtil;
import util.FileUtil;
import util.GlobalConfigUtil;
import util.StringUtil;
import v17.PositionsLogic_r5.ExecutionInfo;
import v17.PositionsLogic_r5.PosInfo;
import v9_2.CloseOrdersLogic_r4;
import v9_2.LockedAuthorizedToken_r4;

/**
 * ストップロス注文ツール。
 */
public class MainStopLossOrder_r5 {
	/**
	 * API実行クラス。
	 */
	private static Class<?> clazz = MethodHandles.lookup().lookupClass();
	/**
	 * ストップロス注文を発注する含み損。
	 */
	private static final int STOP_LOSS_ORDER_START = GlobalConfigUtil.getInt("StopLossOrderStart", -500);
	/**
	 * 建玉の価格からの値幅。
	 */
	private static final int STOP_LOSS_PRICE_RANGE = GlobalConfigUtil.getInt("StopLossPriceRange", 1000);

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * ストップロス注文ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "MainStopLossOrder_r5.log";
	/**
	 * メール本文を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String MAIL_FILEPATH = DIRPATH + "MainStopLossOrder_r5.mail";

	/**
	 * ストップロス注文ツール。
	 * 
	 * @param args 起動パラメータ。
	 * @throws ApiException
	 */
	public static void main(String[] args) throws ApiException {
		ApiErrorLog.init(clazz, Consts.VERSION);
		String X_API_KEY = LockedAuthorizedToken_r4.lockToken();
		try {
			new MainStopLossOrder_r5(X_API_KEY).execute();
		} finally {
			LockedAuthorizedToken_r4.unlockToken();
		}
	}

	/**
	 * 注文約定情報を管理する。
	 */
	private CloseOrdersLogic_r4 closeOrderLogic;

	/**
	 * 建玉情報を管理する。
	 */
	private PositionsLogic_r5 posLogic;

	/**
	 * メール送信を管理する。
	 */
	private SendMailLogic sendMailLogic;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public MainStopLossOrder_r5(String X_API_KEY) {
		this.closeOrderLogic = new CloseOrdersLogic_r4(X_API_KEY);
		this.posLogic = new PositionsLogic_r5(X_API_KEY);
		this.sendMailLogic = new SendMailLogic(MAIL_FILEPATH);
	}

	/**
	 * ストップロス注文。
	 * 
	 * @throws ApiException 
	 */
	public void execute() throws ApiException {
		sendMailLogic.deleteMailFile();
		closeOrderLogic.execute();
		posLogic.execute();
		List<PosInfo> posList = posLogic.getList();
		int exchange = ExchangeUtil.now();
		if (exchange > 0) {
			for (PosInfo pi : posList) {
				int sign = StringUtil.sign(pi.side);
				int profit = (pi.curPrice - pi.price) * sign;
				if (profit >= STOP_LOSS_ORDER_START) {
					String msg = "skip price=" + pi.price + StringUtil.sideStr(pi.side)
							+ ", " + profit + " >= " + STOP_LOSS_ORDER_START;
					System.out.println("  > execute " + msg);
					FileUtil.printLog(LOG_FILEPATH, "execute", msg);
					continue;
				}
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
		sendMailLogic.writeMailFile("StopLossOrder");
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
		body.setExpireDay(0); // 当日
		if (triggerPrice == 0) {
			// 逆指値注文がエラーとなる価格の場合は、成行注文する
			body.setFrontOrderType(120); // 成行
			body.setPrice(0.0); // 成行時0円
			triggerPrice = pi.curPrice;
		} else {
			body.setFrontOrderType(30); // 逆指値
			body.setPrice(0.0); // 逆指値時0円
			RequestSendOrderDerivFutureReverseLimitOrder rlo = new RequestSendOrderDerivFutureReverseLimitOrder();
			{
				rlo.setTriggerPrice((double) triggerPrice);
				rlo.setUnderOver(StringUtil.underOver(body.getSide()));
				rlo.setAfterHitOrderType(1); // 成行
				rlo.setAfterHitPrice(0.0); // 成行時0円
			}
			body.setReverseLimitOrder(rlo);
		}
		
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
		{
			StringBuilder sb = new StringBuilder();
			sb.append("CLOSE:{").append(pi.name).append(" ").append(StringUtil.exchangeStr(exchange));
			sb.append(" price=").append(pi.price).append(StringUtil.sideStr(pi.side));
			sb.append(", qty=").append(body.getQty());
			sb.append(", trigger=").append(triggerPrice).append(StringUtil.sideStr(body.getSide()));
			sb.append("(").append(delta).append(")");
			sb.append(", holdId=").append(holdId);
			sb.append("}");
			String msgMail = sb.toString();
			sendMailLogic.addLine(msgMail);
		}
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
		int profit = (pi.curPrice - pi.price) * sign;
		if (profit <= -1 * STOP_LOSS_PRICE_RANGE) {
			// 逆指値注文がエラーとなる価格の場合は、成行注文する
			return 0;
		}
		int price = pi.price + -1 * STOP_LOSS_PRICE_RANGE * sign;
		return price;
	}

}