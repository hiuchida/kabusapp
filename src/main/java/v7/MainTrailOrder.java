package v7;

import java.util.ArrayList;
import java.util.List;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.PositionsDeriv;
import io.swagger.client.model.PositionsSuccess;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import io.swagger.client.model.RequestSendOrderDerivFutureReverseLimitOrder;
import util.ExchangeUtil;
import util.FileUtil;
import util.StringUtil;
import v7.PositionsLogic.ExecutionInfo;
import v7.PositionsLogic.PosInfo;

/**
 * トレイル注文ツール。
 */
public class MainTrailOrder {
	/**
	 * 含み益がこの値以上になった後に返済発注する。
	 */
	private static final int PROFIT_HIGH_THRESHOLD = 200; // TODO 200円以上でトリガー
	/**
	 * 含み益の最大値からの値幅。
	 */
	private static final int TRIGGER_PRICE_RANGE = 100; // TODO 逆指値100円

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

	/**
	 * トレイル注文ツール。
	 * 
	 * @param args 起動パラメータ。
	 * @throws ApiException
	 */
	public static void main(String[] args) throws ApiException {
		String X_API_KEY = LockedAuthorizedToken.lockToken();
		try {
			new MainTrailOrder(X_API_KEY).execute();
		} finally {
			LockedAuthorizedToken.unlockToken();
		}
	}

	/**
	 * 認証済TOKEN。
	 */
	private String X_API_KEY;
	
	/**
	 * 注文約定情報を管理する。
	 */
	private OrdersLogic orderLogic;

	/**
	 * 建玉情報を管理する。
	 */
	private PositionsLogic posLogic;

	/**
	 * 情報API。
	 */
	private InfoApi infoApi = new InfoApi();

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public MainTrailOrder(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
		this.orderLogic = new OrdersLogic(X_API_KEY);
		this.posLogic = new PositionsLogic(X_API_KEY);
	}

	/**
	 * トレイル注文。
	 * 
	 * @throws ApiException 
	 */
	public void execute() throws ApiException {
		orderLogic.execute();
		List<PosInfo> highList = posLogic.execute();
		int exchange = ExchangeUtil.now();
		if (exchange > 0) {
			for (PosInfo pi : highList) {
				if (pi.profitHigh < PROFIT_HIGH_THRESHOLD) {
					continue;
				}
				for (ExecutionInfo ei : pi.executionList) {
					String holdId = ei.executionId;
					if (ei.holdQty > 0) {
						String orderId = orderLogic.getOrderId(holdId);
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
					sendCloseOrder(pi, ei, exchange, holdId);
				}
			}
			orderLogic.writeOrders();
		}
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
		orderLogic.cancelOrder(orderId, msg);
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
		
		String msg = "code=" + body.getSymbol() + ", exchange=" + body.getExchange() + ", price=" + triggerPrice
				+ StringUtil.sideStr(body.getSide()) + ", qty=" + body.getQty() + ", holdId=" + holdId;
		System.out.println("  > sendOrder " + msg);
		FileUtil.printLog(LOG_FILEPATH, "sendCloseOrder", msg);
		String orderId = orderLogic.sendOrder(body, holdId, msg);
		return orderId;
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
	 * トリガ価格を取得する。
	 * 
	 * @param pi 建玉情報。
	 * @return トリガ価格。
	 */
	private int triggerPrice(PosInfo pi) {
		int sign = StringUtil.sign(pi.side);
		int price = pi.price + (pi.profitHigh - TRIGGER_PRICE_RANGE) * sign;
		return price;
	}

}
