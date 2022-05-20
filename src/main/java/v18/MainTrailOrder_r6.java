package v18;

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
import util.LockedAuthorizedTokenUtil;
import util.StringUtil;
import v18.PositionsLogic_r6.PosInfo;

/**
 * トレイル注文ツール。
 */
public class MainTrailOrder_r6 {
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
	private static final String LOG_FILEPATH = DIRPATH + "MainTrailOrder_r6.log";
	/**
	 * メール本文を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String MAIL_FILEPATH = DIRPATH + "MainTrailOrder_r6.mail";

	/**
	 * トレイル注文ツール。
	 * 
	 * @param args 起動パラメータ。
	 * @throws ApiException
	 */
	public static void main(String[] args) throws ApiException {
		ApiErrorLog.init(clazz, Consts.VERSION);
		String X_API_KEY = LockedAuthorizedTokenUtil.lockToken();
		try {
			new MainTrailOrder_r6(X_API_KEY).execute();
		} finally {
			LockedAuthorizedTokenUtil.unlockToken();
		}
	}

	/**
	 * 注文約定情報を管理する。
	 */
	private CloseOrdersLogic_r6 closeOrderLogic;

	/**
	 * 建玉情報を管理する。
	 */
	private PositionsLogic_r6 posLogic;

	/**
	 * メール送信を管理する。
	 */
	private SendMailLogic sendMailLogic;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public MainTrailOrder_r6(String X_API_KEY) {
		this.closeOrderLogic = new CloseOrdersLogic_r6(X_API_KEY);
		this.posLogic = new PositionsLogic_r6(X_API_KEY);
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
				String holdId = pi.executionId;
				if (pi.holdQty > 0) {
					String orderId = closeOrderLogic.getOrderId(holdId);
					if (orderId == null) {
						String msg = "executionId not found holdId=" + holdId + ", price=" + pi.price + StringUtil.sideStr(pi.side);
						System.out.println("  > " + msg);
						FileUtil.printLog(LOG_FILEPATH, "execute", msg);
						continue;
					}
					try {
						cancelOrder(orderId, pi);
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
							pi.leavesQty = (int) (double) ps.getLeavesQty();
							pi.holdQty = (int) (double) ps.getHoldQty();
						}
					}
					if ((pi.leavesQty - pi.holdQty) <= 0) {
						String msg = "zero qty " + pi.name + " " + holdId;
						System.out.println("  > " + msg);
						FileUtil.printLog(LOG_FILEPATH, "execute", msg);
						continue;
					}
				}
				sendCloseOrder(pi, exchange);
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
	 * @throws ApiException
	 */
	private void cancelOrder(String orderId, PosInfo pi) throws ApiException {
		String msg = "orderId=" + orderId + ", name=" + pi.name + ", holdId=" + pi.executionId + ", holdQty=" + pi.holdQty;
		System.out.println("  > cancelOrder " + msg);
		FileUtil.printLog(LOG_FILEPATH, "cancelOrder", msg);
		closeOrderLogic.cancelOrder(orderId, msg);
	}

	/**
	 * 返済注文を実行する。
	 * 
	 * @param pi       建玉情報。
	 * @param exchange 市場コード（Exchange）。
	 * @return 注文番号(ID)。
	 * @throws ApiException 
	 */
	private String sendCloseOrder(PosInfo pi, int exchange) throws ApiException {
		int triggerPrice = pi.triggerPrice;
		RequestSendOrderDerivFuture body = new RequestSendOrderDerivFuture();
		body.setSymbol(pi.code);
		body.setExchange(exchange);
		body.setTradeType(2); // 返済
		body.setTimeInForce(2); // FAK
		body.setSide(StringUtil.sideReturn(pi.side));
		body.setQty(pi.leavesQty - pi.holdQty);
		List<PositionsDeriv> pdl = new ArrayList<>();
		{
			PositionsDeriv pd = new PositionsDeriv();
			pd.setHoldID(pi.executionId);
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
			sb.append(", holdId=").append(pi.executionId);
			sb.append("}");
			msg = sb.toString();
			System.out.println("  > sendCloseOrder " + msg);
			FileUtil.printLog(LOG_FILEPATH, "sendCloseOrder", msg);
		}
		String orderId = closeOrderLogic.sendOrder(body, pi.executionId, msg);
		{
			StringBuilder sb = new StringBuilder();
			sb.append("CLOSE:{").append(pi.name).append(" ").append(StringUtil.exchangeStr(exchange));
			sb.append(" price=").append(pi.price).append(StringUtil.sideStr(pi.side));
			sb.append(", qty=").append(body.getQty());
			sb.append(", trigger=").append(triggerPrice).append(StringUtil.sideStr(body.getSide()));
			sb.append("(").append(delta).append(")");
			sb.append(", holdId=").append(pi.executionId);
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
