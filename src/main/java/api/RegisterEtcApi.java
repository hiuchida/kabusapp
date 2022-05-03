package api;

import java.lang.invoke.MethodHandles;

import io.swagger.client.ApiException;
import io.swagger.client.api.RegisterApi;
import io.swagger.client.model.RegistSuccess;
import io.swagger.client.model.RequestRegister;
import io.swagger.client.model.RequestRegisterSymbols;

/**
 * 銘柄登録API。
 */
public class RegisterEtcApi {
	/**
	 * API実行クラス。
	 */
	private Class<?> clazz = MethodHandles.lookup().lookupClass();
	/**
	 * 認証済TOKEN。
	 */
	private String X_API_KEY;

	/**
	 * 銘柄登録API。
	 */
	private RegisterApi registerApi = new RegisterApi();

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public RegisterEtcApi(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	/**
	 * 1銘柄の銘柄登録API。
	 * 
	 * @param symbol   銘柄コード(Symbol)。
	 * @param exchange 市場コード(Exchange)。
	 * @return 銘柄登録。
	 * @throws ApiException
	 */
	public RegistSuccess put(String symbol, int exchange) throws ApiException {
        RequestRegister body = new RequestRegister();
        RequestRegisterSymbols item = new RequestRegisterSymbols();
        item.setSymbol(symbol);
        item.setExchange(exchange);
        body.addSymbolsItem(item);
		try {
			RegistSuccess rs = registerApi.registerPut(body, X_API_KEY);
			try {
				Thread.sleep(120); // 8.3req/sec
			} catch (Exception e) {
			}
			return rs;
		} catch (ApiException e) {
			ApiErrorLog.error(e, clazz, "put", symbol + "," + exchange);
			throw e;
		}
	}

}
