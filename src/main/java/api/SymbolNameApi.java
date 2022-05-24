package api;

import java.lang.invoke.MethodHandles;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.SymbolNameSuccess;

/**
 * 銘柄コード取得API。
 */
public class SymbolNameApi {
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
	public SymbolNameApi(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	/**
	 * 先物銘柄コード取得API。
	 * 
	 * @param futureCode 先物コード（NK225:日経平均先物、NK225mini:日経225mini先物など）。
	 * @param derivMonth 限月（yyyyMM形式）。
	 * @return 銘柄コード。
	 * @throws ApiException
	 */
	public SymbolNameSuccess getFuture(String futureCode, int derivMonth) throws ApiException {
		try {
			SymbolNameSuccess sns = infoApi.symbolnameFutureGet(X_API_KEY, derivMonth, futureCode);
			try {
				Thread.sleep(120); // 8.3req/sec
			} catch (Exception e) {
			}
			return sns;
		} catch (ApiException e) {
			ApiErrorLog.error(e, clazz, "getOption", futureCode + "," + derivMonth);
			throw e;
		}
	}

	/**
	 * オプション銘柄コード取得API。
	 * 
	 * @param derivMonth  限月（yyyyMM形式）。
	 * @param putOrCall   コール or プット。
	 * @param strikePrice 権利行使価格。
	 * @return 銘柄コード。
	 * @throws ApiException
	 */
	public SymbolNameSuccess getOption(int derivMonth, String putOrCall, int strikePrice) throws ApiException {
		try {
			SymbolNameSuccess sns = infoApi.symbolnameOptionGet(X_API_KEY, derivMonth, putOrCall, strikePrice);
			try {
				Thread.sleep(120); // 8.3req/sec
			} catch (Exception e) {
			}
			return sns;
		} catch (ApiException e) {
			ApiErrorLog.error(e, clazz, "getOption", derivMonth + "," + putOrCall + "," + strikePrice);
			throw e;
		}
	}

}
