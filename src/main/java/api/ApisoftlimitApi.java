package api;

import java.lang.invoke.MethodHandles;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.ApiSoftLimitResponse;

/**
 * ソフトリミットAPI。
 */
public class ApisoftlimitApi {
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
	public ApisoftlimitApi(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	/**
	 * ソフトリミットAPI。
	 * 
	 * @return ソフトリミット。
	 * @throws ApiException
	 */
	public ApiSoftLimitResponse get() throws ApiException {
		try {
			ApiSoftLimitResponse aslr = infoApi.apisoftlimitGet(X_API_KEY);
			try {
				Thread.sleep(120); // 8.3req/sec
			} catch (Exception e) {
			}
			return aslr;
		} catch (ApiException e) {
			ApiErrorLog.error(e, clazz, "get", "");
			throw e;
		}
	}

}
