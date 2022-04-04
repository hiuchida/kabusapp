package v4;

import io.swagger.client.ApiException;

/**
 * ログインを行い、認証済TOKENファイルを更新する。
 */
public class MainLogin {
	public static void main(String[] args) throws ApiException {
        String X_API_KEY = LockedAuthorizedToken.lockToken();
        System.out.println("Token: " + X_API_KEY);
        LockedAuthorizedToken.unlockToken();
	}

}
