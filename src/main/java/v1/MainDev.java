package v1;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.AuthApi;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.BoardSuccess;
import io.swagger.client.model.RequestToken;
import io.swagger.client.model.TokenSuccess;

public class MainDev {
	static {
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:18081/kabusapi");
		Configuration.setDefaultApiClient(client);
	}

	private static AuthApi authApi = new AuthApi();
	private static InfoApi infoApi = new InfoApi();
	
	public static void main(String[] args) throws ApiException {
		String token = null;
		{
			RequestToken body = new RequestToken();
	        body.setApIPassword("YourPassword");
	        TokenSuccess response = authApi.tokenPost(body);
	        System.out.println(response);
	        token = response.getToken();
		}
		{
	        String X_API_KEY = token;
	        String symbol = "9433@1"; // ＫＤＤＩ
	        BoardSuccess response = infoApi.boardGet(X_API_KEY, symbol);
	        System.out.println(response);
	    }
	}

}
