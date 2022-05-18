package v9_2;

import api.ApisoftlimitApi;
import api.TokenApi;
import io.swagger.client.ApiException;
import logic.FileLockLogic;
import util.FileUtil;

/**
 * 認証済TOKENをファイルロック管理する。
 */
public class LockedAuthorizedToken_r4 {
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
	private static LockedAuthorizedToken_r4 singleton = new LockedAuthorizedToken_r4();
	/**
	 * 認証済TOKENロックを管理する。
	 */
	private static FileLockLogic fileLockLogic = new FileLockLogic(LOCK_FILEPATH);

	/**
	 * シングルトンインスタンスを取得する。
	 * 
	 * @return シングルトンインスタンス。
	 */
	public static LockedAuthorizedToken_r4 getInstance() {
		return singleton;
	}

	/**
	 * ファイルロックを取得し、認証済TOKENを取得する。
	 * 
	 * @return 認証済TOKEN。
	 * @throws ApiException
	 */
	public static String lockToken() throws ApiException {
		fileLockLogic.lockFile();
		return singleton.initToken();
	}

	/**
	 * ファイルロックを解放する。
	 */
	public static void unlockToken() {
		fileLockLogic.unlockFile();
	}

	/**
	 * トークン発行API。
	 */
	private TokenApi tokenApi;

	/**
	 * コンストラクタ。
	 */
	private LockedAuthorizedToken_r4() {
		this.tokenApi = new TokenApi();
	}

	/**
	 * ファイルからTOKENを取得し、有効かどうか調べ、無効なら認証APIを呼び出し、認証済TOKENを取得する。
	 * 
	 * @return 認証済TOKEN。
	 * @throws ApiException
	 */
	private String initToken() throws ApiException {
		String token = FileUtil.readOneLine(TXT_FILEPATH);
		boolean bAvailable = false;
		if (token != null && token.length() > 0) {
			bAvailable = ping(token);
		}
		String msg = "check token=" + token + ", bAvailable=" + bAvailable;
		System.out.println("LockedAuthorizedToken_r4.initToken(): " + msg);
		FileUtil.printLog(LOG_FILEPATH, "initToken", msg);
		if (!bAvailable) {
			try {
				token = auth();
				msg = "save  token=" + token;
				System.out.println("  > auth " + msg);
				FileUtil.printLog(LOG_FILEPATH, "initToken", msg);
				FileUtil.writeOneLine(TXT_FILEPATH, token);
			} catch (ApiException e) {
				msg = "error token=" + token;
				System.out.println("  > auth " + msg);
				FileUtil.printLog(LOG_FILEPATH, "initToken", msg);
				FileUtil.writeOneLine(TXT_FILEPATH, "");
				throw e;
			}
		}
		return token;
	}

	/**
	 * 指定したTOKENが有効かどうか調べる。
	 * 
	 * @param token 認証済TOKEN。
	 * @return true:有効、false:無効。
	 */
	private boolean ping(String token) {
		String X_API_KEY = token;
		try {
			ApisoftlimitApi apisoftlimitApi = new ApisoftlimitApi(X_API_KEY);
			apisoftlimitApi.get();
			return true;
		} catch (ApiException e) {
			return false;
		}
	}

	/**
	 * 認証APIを呼び出し、認証済TOKENを取得する。
	 * 
	 * @return 認証済TOKEN。
	 * @throws ApiException
	 */
	private String auth() throws ApiException {
		String pwd = FileUtil.readOneLine(PWD_FILEPATH);
		return tokenApi.postToken(pwd);
	}

}
