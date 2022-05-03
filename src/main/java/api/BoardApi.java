package api;

import java.lang.invoke.MethodHandles;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.BoardSuccess;

/**
 * 時価情報・板情報API。
 */
public class BoardApi {
	/**
	 * API実行クラス。
	 */
	private Class<?> clazz = MethodHandles.lookup().lookupClass();
	/**
	 * 認証済TOKEN。
	 */
	private String X_API_KEY;

	/**
	 * 情報API。
	 */
	private InfoApi infoApi = new InfoApi();

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public BoardApi(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	/**
	 * 時価情報・板情報API。
	 * 
	 * @param symbol 銘柄コード(Symbol)。
	 * @return 時価情報・板情報。
	 * @throws ApiException
	 */
	public BoardSuccess get(String symbol) throws ApiException {
		try {
			BoardSuccess bs = infoApi.boardGet(X_API_KEY, symbol);
			try {
				Thread.sleep(120); // 8.3req/sec
			} catch (Exception e) {
			}
			return bs;
		} catch (ApiException e) {
			ApiErrorLog.error(e, clazz, "get", symbol);
			throw e;
		}
	}

	/**
	 * 時価情報・板情報APIの現値。
	 * 
	 * @param symbol 銘柄コード(Symbol)。
	 * @return 現値。
	 * @throws ApiException
	 */
	public int getCurPrice(String symbol) throws ApiException {
		BoardSuccess bs = this.get(symbol);
		int curPrice = 0;
		Double d = bs.getCurrentPrice();
		if (d != null) {
			curPrice = (int) (double) d;
		}
		return curPrice;
	}

}
