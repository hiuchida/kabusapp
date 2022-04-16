package v7;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.PositionsDeriv;
import io.swagger.client.model.PositionsSuccess;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import io.swagger.client.model.RequestSendOrderDerivFutureReverseLimitOrder;
import util.FileUtil;
import util.StringUtil;
import v7.PositionsLogic.ExecutionInfo;
import v7.PositionsLogic.PosInfo;

/**
 * トレイル注文ツール。
 */
public class MainTrailOrder {
	/**
	 * タブ文字。
	 */
	public static final String TAB = "\t";

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * トレイル注文ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "MainTrailOrder.log";

	private String X_API_KEY;

	private static InfoApi infoApi = new InfoApi();

	public MainTrailOrder(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	public static void main(String[] args) throws ApiException {
		String X_API_KEY = LockedAuthorizedToken.lockToken();
		try {
			new MainTrailOrder(X_API_KEY).execute();
		} finally {
			LockedAuthorizedToken.unlockToken();
		}
	}

	public void execute() throws ApiException {
		OrdersLogic ol = new OrdersLogic(X_API_KEY);
		ol.execute();
		PositionsLogic pl = new PositionsLogic(X_API_KEY);
		List<PosInfo> highList = pl.execute();
		int exchange = exchange();
		if (exchange > 0) {
			for (PosInfo pi : highList) {
				if (pi.profitHigh < 100) { // TODO 200円以上でトリガー
					continue;
				}
				for (ExecutionInfo ei : pi.executionList) {
					String holdId = ei.executionId;
					if (ei.holdQty > 0) {
						String orderId = ol.getOrderId(holdId);
						if (orderId == null) {
							String msg = "not found " + pi.name + " " + holdId;
							System.out.println("  > " + msg);
							FileUtil.printLog(LOG_FILEPATH, "execute", msg);
							continue;
						}
						try {
							cancelOrder(ol, orderId, pi, holdId, ei.holdQty);
						} catch (ApiException e) {
							e.printStackTrace();
							String msg = "cancelOrder ERROR";
							System.out.println("  > " + msg);
							FileUtil.printLog(LOG_FILEPATH, "execute", msg);
							continue;
						}
						List<PositionsSuccess> psList = getPosition(pi.code);
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
					sendCloseOrder(ol, pi, ei, exchange, holdId);
				}
			}
			ol.writeOrders();
		}
	}

	/**
	 * 注文取消を実行する。
	 * 
	 * @param ol      注文約定情報を管理するツール。
	 * @param orderId 注文番号(ID)。
	 * @param pi      建玉情報。
	 * @param holdId  返済建玉ID(HoldID=ExecutionID)。
	 * @param holdQty 拘束数量（返済のために拘束されている数量）(HoldQty)。
	 * @throws ApiException
	 */
	private void cancelOrder(OrdersLogic ol, String orderId, PosInfo pi, String holdId, int holdQty) throws ApiException {
		String msg = "orderId=" + orderId + ", name=" + pi.name + ", holdId=" + holdId + ", holdQty=" + holdQty;
		System.out.println("  > " + msg);
		FileUtil.printLog(LOG_FILEPATH, "cancelOrder", msg);
		ol.cancelOrder(orderId, msg);
	}

	/**
	 * 返済注文を実行する。
	 * 
	 * @param ol       注文約定情報を管理するツール。
	 * @param pi       建玉情報。
	 * @param ei       約定数量情報。
	 * @param exchange 市場コード（Exchange）。
	 * @param holdId   約定番号（ExecutionID）。
	 * @throws ApiException 
	 */
	private void sendCloseOrder(OrdersLogic ol, PosInfo pi, ExecutionInfo ei, int exchange, String holdId) throws ApiException {
		RequestSendOrderDerivFuture body = new RequestSendOrderDerivFuture();
		body.setSymbol(pi.code);
		body.setExchange(exchange);
		body.setTradeType(2); // 返済
		body.setTimeInForce(2); // FAK
		body.setSide(sideReturn(pi.side));
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
			rlo.setTriggerPrice((double) triggerPrice(pi));
			rlo.setUnderOver(underOver(body.getSide()));
			rlo.setAfterHitOrderType(1); // 成行
			rlo.setAfterHitPrice(0.0); // 成行時0円
		}
		body.setReverseLimitOrder(rlo);
		
		String msg = "sendOrder code=" + body.getSymbol() + ", exchange=" + body.getExchange() + ", price=" + body.getPrice()
				+ StringUtil.sideStr(body.getSide()) + ", qty=" + body.getQty() + ", holdId=" + holdId;
		System.out.println("  > " + msg);
		FileUtil.printLog(LOG_FILEPATH, "sendCloseOrder", msg);
		ol.sendOrder(body, holdId);
	}

	/**
	 * 指定した残高照会を行う。
	 * 
	 * @param code 銘柄コード(Symbol)。
	 * @return 残高情報のリスト。
	 * @throws ApiException 
	 */
	private List<PositionsSuccess> getPosition(String code) throws ApiException {
		String product = null;
		String symbol = code;
		String sideParam = null;
		String addinfo = null;
		List<PositionsSuccess> response = infoApi.positionsGet(X_API_KEY, product, symbol, sideParam, addinfo);
		return response;
	}

	/**
	 * 数量の符号を取得する。
	 * 
	 * @param side 売買区分(Side)。
	 * @return 符号
	 */
	private int sign(String side) {
		switch (side) {
		case "1":
			return -1;
		case "2":
			return 1;
		default:
			throw new RuntimeException();
		}
	}

	/**
	 * 反対売買を取得する。
	 * 
	 * @param side 売買区分(Side)。
	 * @return 反対の売買区分(1 or 2)。
	 */
	private String sideReturn(String side) {
		switch (side) {
		case "1":
			return "2";
		case "2":
			return "1";
		default:
			throw new RuntimeException();
		}
	}
	
	/**
	 * トリガ価格を取得する。
	 * 
	 * @param pi 建玉情報。
	 * @return トリガ価格。
	 */
	private int triggerPrice(PosInfo pi) {
		int sign = sign(pi.side);
		int price = pi.price + (pi.profitHigh - 50) * sign; // TODO 逆指値100円
		return price;
	}

	/**
	 * 以上／以下を取得する。
	 * 
	 * @param side 売買区分(Side)。
	 * @return 以上／以下(1 or 2)。
	 */
	private int underOver(String side) {
		switch (side) {
		case "1":
			return 1;
		case "2":
			return 2;
		default:
			throw new RuntimeException();
		}
	}

	/**
	 * 現在時刻の市場コード（Exchange）を取得する。
	 * 
	 * @return 市場コード（Exchange）。
	 */
	private int exchange() {
		int ret = 0;
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		if (hour == 8 && 45 <= min) {
			ret = 23; // 日中
		} else if (9 <= hour && hour <= 14) {
			ret = 23; // 日中
		} else if (hour == 15 && min < 10 - 5) {
			ret = 23; // 日中
		}
		if (ret > 0) {
			System.out.println("Order of Day. hour=" + hour + ", min=" + min);
			return ret;
		}
		if (hour == 16 && 30 <= min) {
			ret = 24; // 夜間
		} else if (17 <= hour && hour <= 23) {
			ret = 24; // 夜間
		} else if (0 <= hour && hour <= 4) {
			ret = 24; // 夜間
		} else if (hour == 5 && min < 55 - 5) {
			ret = 24; // 夜間
		}
		if (ret > 0) {
			System.out.println("Order of Night. hour=" + hour + ", min=" + min);
			return ret;
		}
		System.out.println("Out of Order. hour=" + hour + ", min=" + min);
		return -1;
	}

}
