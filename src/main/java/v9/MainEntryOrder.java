package v9;

import java.util.List;

import io.swagger.client.ApiException;
import io.swagger.client.model.RequestSendOrderDerivFuture;
import util.ExchangeUtil;
import util.FileUtil;
import util.StringUtil;
import v7.LockedAuthorizedToken;
import v9.EntryOrdersLogic.OrderInfo;

/**
 * 新規注文ツール。
 */
public class MainEntryOrder {
	/**
	 * 銘柄コード(Symbol)。 
	 */
	private static final String SYMBOL = "167060019";
	/**
	 * 銘柄名(SymbolName)。
	 */
	private static final String SYMBOL_NAME = "日経225mini 22/06";
	/**
	 * 有効期限(ExpireDay)。
	 */
	private static final int EXPIRE_DAY = 20220506;

	/**
	 * タブ文字。
	 */
	public static final String TAB = "\t";

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 新規注文ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "MainEntryOrder.log";

	/**
	 * 新規注文ツール。
	 * 
	 * @param args 起動パラメータ。
	 * @throws ApiException
	 */
	public static void main(String[] args) throws ApiException {
		String X_API_KEY = LockedAuthorizedToken.lockToken();
		try {
			new MainEntryOrder(X_API_KEY).execute();
		} finally {
			LockedAuthorizedToken.unlockToken();
		}
	}

	/**
	 * 新規注文情報を管理する。
	 */
	private EntryOrdersLogic entryOrdersLogic;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public MainEntryOrder(String X_API_KEY) {
		this.entryOrdersLogic = new EntryOrdersLogic(X_API_KEY);
	}

	/**
	 * 新規注文。
	 * 
	 * @throws ApiException 
	 */
	public void execute() throws ApiException {
		List<OrderInfo> entryList = entryOrdersLogic.execute();
		int exchange = ExchangeUtil.now();
		if (exchange > 0) {
			for (OrderInfo oi : entryList) {
				if (oi.state < OrderInfo.STATE_ORDERED) {
					String orderId = sendEntryOrder(oi, exchange);
					oi.orderId = orderId;
					oi.state = OrderInfo.STATE_ORDERED;
				}
			}
		}
		entryOrdersLogic.writeOrders();
	}

	/**
	 * 新規注文を実行する。
	 * 
	 * @param oi       新規注文情報。
	 * @param exchange 市場コード(Exchange)。
	 * @return 注文番号(ID)。
	 * @throws ApiException 
	 */
	private String sendEntryOrder(OrderInfo oi, int exchange) throws ApiException {
		exchange = 2; // 日通し
		int expireDay = EXPIRE_DAY;
		RequestSendOrderDerivFuture body = new RequestSendOrderDerivFuture();
		body.setSymbol(SYMBOL);
		body.setExchange(exchange);
		body.setTradeType(1); // 新規
		body.setTimeInForce(1); // FAS
		body.setSide(oi.side);
		body.setQty(oi.orderQty);
		body.setFrontOrderType(20); // 指値
		body.setPrice((double) oi.price);
		body.setExpireDay(expireDay);

		String msg;
		{
			StringBuilder sb = new StringBuilder();
			sb.append("ENTRY:{").append(SYMBOL).append(" ").append(SYMBOL_NAME).append(" ");
			sb.append(StringUtil.exchangeStr(exchange));
			sb.append(" ").append(expireDay);
			sb.append(" price=").append(oi.price).append(StringUtil.sideStr(oi.side));
			sb.append(", qty=").append(oi.orderQty);
			sb.append("}");
			msg = sb.toString();
			System.out.println("  > sendEntryOrder " + msg);
			FileUtil.printLog(LOG_FILEPATH, "sendEntryOrder", msg);
		}
		String orderId = entryOrdersLogic.sendOrder(oi.uniqId, body, msg);
		return orderId;
	}

}
