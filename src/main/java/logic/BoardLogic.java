package logic;

import api.BoardApi;
import io.swagger.client.ApiException;

/**
 * 時価情報・板情報を管理する。
 */
public class BoardLogic {

	/**
	 * 時価情報・板情報API。
	 */
	private BoardApi boardApi;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public BoardLogic(String X_API_KEY) {
		this.boardApi = new BoardApi(X_API_KEY);
	}

	/**
	 * 指定した銘柄の時価を取得する。
	 * 
	 * @param code 銘柄コード(Symbol)。
	 * @return 現値。
	 * @throws ApiException
	 */
	public int getCurPrice(String code) throws ApiException {
		int curPrice = boardApi.getCurPrice(code);
		return curPrice;
	}

}
