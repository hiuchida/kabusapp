package api;

import java.lang.invoke.MethodHandles;
import java.util.List;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.PositionsSuccess;

/**
 * 残高照会API。
 */
public class PositionsApi {
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
	public PositionsApi(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	/**
	 * 残高照会API。
	 * 
	 * @return 残高照会のリスト。
	 * @throws ApiException
	 */
	public List<PositionsSuccess> get() throws ApiException {
		try {
			String product = null;
			String symbol = null;
			String side = null;
			String addinfo = null;
			List<PositionsSuccess> lps = infoApi.positionsGet(X_API_KEY, product, symbol, side, addinfo);
			try {
				Thread.sleep(120); // 8.3req/sec
			} catch (Exception e) {
			}
			return lps;
		} catch (ApiException e) {
			ApiErrorLog.error(e, clazz, "get", "");
			throw e;
		}
	}

	/**
	 * 残高照会API。
	 * 
	 * @param symbol 銘柄コード(Symbol)。
	 * @return 残高照会のリスト。
	 * @throws ApiException
	 */
	public List<PositionsSuccess> getSymbol(String symbol) throws ApiException {
		try {
			String product = null;
			String side = null;
			String addinfo = null;
			List<PositionsSuccess> lps = infoApi.positionsGet(X_API_KEY, product, symbol, side, addinfo);
			try {
				Thread.sleep(120); // 8.3req/sec
			} catch (Exception e) {
			}
			return lps;
		} catch (ApiException e) {
			ApiErrorLog.error(e, clazz, "getSymbol", symbol);
			throw e;
		}
	}

}
