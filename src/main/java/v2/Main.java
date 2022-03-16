package v2;

import java.util.List;

import io.swagger.client.ApiException;
import io.swagger.client.api.AuthApi;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.PositionsSuccess;
import io.swagger.client.model.RequestToken;
import io.swagger.client.model.TokenSuccess;

public class Main {
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
	        String product = null;
	        String symbol = null;
	        String side = null;
	        String addinfo = null;
	        List<PositionsSuccess> response = infoApi.positionsGet(X_API_KEY, product, symbol, side, addinfo);
	        for (int i = 0; i < response.size(); i++) {
		        System.out.println((i + 1) + ": " + response.get(i));
	        }
		}
	}

}
