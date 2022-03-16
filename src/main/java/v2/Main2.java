package v2;

import java.util.List;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.PositionsSuccess;

public class Main2 {
	private static InfoApi infoApi = new InfoApi();
	
	public static void main(String[] args) throws ApiException {
        String X_API_KEY = AuthorizedToken.getToken();
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
