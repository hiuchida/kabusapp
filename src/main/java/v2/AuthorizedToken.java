package v2;

import io.swagger.client.ApiException;
import io.swagger.client.api.AuthApi;
import io.swagger.client.model.RequestToken;
import io.swagger.client.model.TokenSuccess;

public class AuthorizedToken {
	private static AuthorizedToken singleton = new AuthorizedToken();

	public static AuthorizedToken getInstance() {
		return singleton;
	}

	public static String getToken() throws ApiException {
		return singleton.initToken();
	}

	private String token = null;
	
	private AuthorizedToken() {
	}

	public String initToken() throws ApiException {
		if (token == null) {
			AuthApi authApi = new AuthApi();
			RequestToken body = new RequestToken();
	        body.setApIPassword("YourPassword");
	        TokenSuccess response = authApi.tokenPost(body);
	        System.out.println(response);
	        token = response.getToken();
		}
		return token;
	}

}
