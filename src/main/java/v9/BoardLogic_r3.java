package v9;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.BoardSuccess;

/**
 * 時価情報・板情報を管理する。
 */
public class BoardLogic_r3 {

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
	public BoardLogic_r3(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	/**
	 * 指定した銘柄の時価を取得する。
	 * 
	 * @param code 銘柄コード(Symbol)。
	 * @return 現値。
	 * @throws ApiException
	 */
	public int getCurPrice(String code) throws ApiException {
		BoardSuccess bs = infoApi.boardGet(X_API_KEY, code);
		try {
			Thread.sleep(120); // 8.3req/sec
		} catch (Exception e) {
		}
		int curPrice = 0;
		Double d = bs.getCurrentPrice();
		if (d != null) {
			curPrice = (int) (double) d;
		}
		return curPrice;
	}

}
