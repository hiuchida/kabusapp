package v7;

import io.swagger.client.ApiException;
import io.swagger.client.api.AuthApi;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.ApiSoftLimitResponse;
import io.swagger.client.model.RequestToken;
import io.swagger.client.model.TokenSuccess;
import util.FileLockUtil;
import util.FileUtil;

/**
 * 認証済TOKENをファイルロック管理する。
 */
public class LockedAuthorizedToken {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 1行目にAPIパスワードを保存したファイルパス。事前に準備する。
	 */
	private static final String PWD_FILEPATH = DIRPATH + "LockedAuthorizedToken.pwd";
	/**
	 * 1行目に認証済TOKENを保存したファイルパス。存在しなければ生成される。
	 */
	private static final String TXT_FILEPATH = DIRPATH + "LockedAuthorizedToken.txt";
	/**
	 * ファイルロック管理用0バイトのファイルパス。存在しなければ生成される。
	 */
	private static final String LOCK_FILEPATH = DIRPATH + "LockedAuthorizedToken.lock";
	/**
	 * ファイルロック管理ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "LockedAuthorizedToken.log";
	
	/**
	 * シングルトンインスタンス。
	 */
	private static LockedAuthorizedToken singleton = new LockedAuthorizedToken();
	/**
	 * ファイルロック情報。
	 */
	private static FileLockUtil.LockInfo lock = new FileLockUtil.LockInfo();

	/**
	 * シングルトンインスタンスを取得する。
	 * @return シングルトンインスタンス。
	 */
	public static LockedAuthorizedToken getInstance() {
		return singleton;
	}

	/**
	 * ファイルロックを取得し、認証済TOKENを取得する。
	 * @return 認証済TOKEN。
	 * @throws ApiException
	 */
	public static String lockToken() throws ApiException {
		boolean bFirst = true;
		while (true) {
			if (FileLockUtil.lock(lock, false, LOCK_FILEPATH)) {
				break;
			}
			if (bFirst) {
				System.out.println("Waiting for other processes to finish.");
				bFirst = false;
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {}
		}
		return singleton.initToken();
	}

	/**
	 * ファイルロックを解放する。
	 */
	public static void unlockToken() {
		FileLockUtil.unlock(lock);
	}

	private LockedAuthorizedToken() {
	}

	/**
	 * ファイルからTOKENを取得し、有効かどうか調べ、無効なら認証APIを呼び出し、認証済TOKENを取得する。
	 * @return 認証済TOKEN。
	 * @throws ApiException
	 */
	private String initToken() throws ApiException {
		String token = FileUtil.readOneLine(TXT_FILEPATH);
		boolean bAvailable = false;
		if (token != null && token.length() > 0) {
			bAvailable = ping(token);
		}
		FileUtil.printLog(LOG_FILEPATH, "initToken", "check token=" + token + ", bAvailable=" + bAvailable);
		if (!bAvailable) {
			try {
				token = auth();
				FileUtil.printLog(LOG_FILEPATH, "initToken", "save  token=" + token);
				FileUtil.writeOneLine(TXT_FILEPATH, token);
			} catch (ApiException e) {
				FileUtil.printLog(LOG_FILEPATH, "initToken", "error token=" + token);
				FileUtil.writeOneLine(TXT_FILEPATH, "");
				throw e;
			}
		}
		return token;
	}

	/**
	 * 指定したTOKENが有効かどうか調べる。
	 * @param token 認証済TOKEN。
	 * @return true:有効、false:無効。
	 */
	private boolean ping(String token) {
		InfoApi infoApi = new InfoApi();
        String X_API_KEY = token;
        try {
			ApiSoftLimitResponse response = infoApi.apisoftlimitGet(X_API_KEY);
	        System.out.println(response);        
			return true;
		} catch (ApiException e) {
			return false;
		}
	}

	/**
	 * 認証APIを呼び出し、認証済TOKENを取得する。
	 * @return 認証済TOKEN。
	 * @throws ApiException
	 */
	private String auth() throws ApiException {
		AuthApi authApi = new AuthApi();
		RequestToken body = new RequestToken();
		String pwd = FileUtil.readOneLine(PWD_FILEPATH);
        body.setApIPassword(pwd);
        TokenSuccess response = authApi.tokenPost(body);
        System.out.println(response);
        return response.getToken();
	}

}
