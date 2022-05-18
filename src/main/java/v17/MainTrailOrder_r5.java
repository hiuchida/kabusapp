package v17;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import api.ApiErrorLog;
import io.swagger.client.ApiException;
import io.swagger.client.model.PositionsDeriv;
import io.swagger.client.model.PositionsSuccess;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import io.swagger.client.model.RequestSendOrderDerivFutureReverseLimitOrder;
import logic.SendMailLogic;
import util.Consts;
import util.ExchangeUtil;
import util.FileUtil;
import util.StringUtil;
import v17.PositionsLogic_r5.ExecutionInfo;
import v17.PositionsLogic_r5.PosInfo;
import v9_2.CloseOrdersLogic_r4;
import v9_2.LockedAuthorizedToken_r4;

/**
 * トレイル注文ツール。
 */
public class MainTrailOrder_r5 {
	/**
	 * API実行クラス。
	 */
	private static Class<?> clazz = MethodHandles.lookup().lookupClass();

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * トレイル注文ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "MainTrailOrder_r5.log";
	/**
	 * メール本文を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String MAIL_FILEPATH = DIRPATH + "MainTrailOrder_r5.mail";

	/**
	 * トレイル注文ツール。
	 * 
	 * @param args 起動パラメータ。
	 * @throws ApiException
	 */
	public static void main(String[] args) throws ApiException {
		ApiErrorLog.init(clazz, Consts.VERSION);
		String X_API_KEY = LockedAuthorizedToken_r4.lockToken();
		try {
			new MainTrailOrder_r5(X_API_KEY).execute();
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
	public MainTrailOrder_r5(String X_API_KEY) {
		this.closeOrderLogic = new CloseOrdersLogic_r4(X_API_KEY);
		this.posLogic = new PositionsLogic_r5(X_API_KEY);
		this.sendMailLogic = new SendMailLogic(MAIL_FILEPATH);
	}

	/**
	 * トレイル注文。
	 * 
	 * @throws ApiException 
	 */
	public void execute() throws ApiException {
		sendMailLogic.deleteMailFile();
		closeOrderLogic.execute();
		List<PosInfo> highList = posLogic.execute();
		int exchange = ExchangeUtil.now();
		if (exchange > 0) {
			for (PosInfo pi : highList) {
				int triggerPrice = triggerPrice(pi);
				if (triggerPrice <= 0) {
					continue;
				}
				if (pi.triggerPrice > 0) {
					int sign = StringUtil.sign(pi.side);
					int delta = (triggerPrice - pi.triggerPrice) * sign;
					if (delta <= 0) {
						String msg = "trigger level up " + pi.name
								+ "price=" + pi.price + StringUtil.sideStr(pi.side)
								+ ", triggerPrice=" + triggerPrice + ", pi.triggerPrice=" + pi.triggerPrice;
						System.out.println("  > " + msg);
						FileUtil.printLog(LOG_FILEPATH, "execute", msg);
						continue;
					}
				}
				pi.triggerPrice = triggerPrice;
				for (ExecutionInfo ei : pi.executionList) {
					String holdId = ei.executionId;
					if (ei.holdQty > 0) {
						String orderId = closeOrderLogic.getOrderId(holdId);
						if (orderId == null) {
							String msg = "not found " + pi.name + " " + holdId;
							System.out.println("  > " + msg);
							FileUtil.printLog(LOG_FILEPATH, "execute", msg);
							continue;
						}
						try {
							cancelOrder(orderId, pi, holdId, ei.holdQty);
						} catch (ApiException e) {
							e.printStackTrace();
							String msg = "cancelOrder ERROR";
							System.out.println("  > " + msg);
							FileUtil.printLog(LOG_FILEPATH, "execute", msg);
							continue;
						}
						List<PositionsSuccess> psList = posLogic.getPosition(pi.code);
						for (PositionsSuccess ps : psList) {
							String id = ps.getExecutionID();
							if (holdId.equals(id)) {
								ei.leavesQty = (int) (double) ps.getLeavesQty();
								ei.holdQty = (int) (double) ps.getHoldQty();
							}
						}
						if ((ei.leavesQty - ei.holdQty) <= 0) {
							String msg = "zero qty " + pi.name + " " + holdId;
							System.out.println("  > " + msg);
							FileUtil.printLog(LOG_FILEPATH, "execute", msg);
							continue;
						}
					}
					sendCloseOrder(pi, ei, exchange, holdId);
				}
			}
			closeOrderLogic.writeOrders();
			posLogic.writePositions();
		}
		sendMailLogic.writeMailFile("TrailOrder");
	}

	/**
	 * 注文取消を実行する。
	 * 
	 * @param orderId 注文番号(ID)。
	 * @param pi      建玉情報。
	 * @param holdId  返済建玉ID(HoldID=ExecutionID)。
	 * @param holdQty 拘束数量（返済のために拘束されている数量）(HoldQty)。
	 * @throws ApiException
	 */
	private void cancelOrder(String orderId, PosInfo pi, String holdId, int holdQty) throws ApiException {
		String msg = "orderId=" + orderId + ", name=" + pi.name + ", holdId=" + holdId + ", holdQty=" + holdQty;
		System.out.println("  > cancelOrder " + msg);
		FileUtil.printLog(LOG_FILEPATH, "cancelOrder", msg);
		closeOrderLogic.cancelOrder(orderId, msg);
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
		int triggerPrice = pi.triggerPrice;
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
		int high = pi.profitHigh;
		if (high < 50) {
			return 0;
		}
		int base = 200;
		int range;
		if (high < base) {
			range = 25;
		} else if (high < 50 + base) {
			range = 50;
		} else if (high < 100 + base) {
			range = 75;
		} else if (high < 200 + base) {
			range = 100;
		} else if (high < 300 + base) {
			range = 125;
		} else if (high < 400 + base) {
			range = 150;
		} else if (high < 500 + base) {
			range = 175;
		} else if (high < 700 + base) {
			range = 200;
		} else if (high < 900 + base) {
			range = 225;
		} else {
			range = 250;
		}
		int sign = StringUtil.sign(pi.side);
		int price = pi.price + (high - range) * sign;
		return price;
	}

}
