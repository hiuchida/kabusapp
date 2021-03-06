package v4;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.BoardSuccess;
import util.DateTimeUtil;

public class Main {
	private static InfoApi infoApi = new InfoApi();
	
	public static void main(String[] args) throws ApiException {
		for (int i = 0; i < 2; i++) {
	        String X_API_KEY = LockedAuthorizedToken.lockToken();
	        try {
		        println("lockToken");
		        String symbol = "9433@1"; // ＫＤＤＩ
		        BoardSuccess response = infoApi.boardGet(X_API_KEY, symbol);
		        println("CurrentPrice: " + response.getCurrentPrice());
		        println("sleeping...");
		        try {
		        	Thread.sleep(1000);
		        } catch (Exception e) {}
		        println("awake");
	        } finally {
				LockedAuthorizedToken.unlockToken();
		        println("unlockToken");
		        println("sleeping...");
		        try {
		        	Thread.sleep(1000);
		        } catch (Exception e) {}
		        println("awake");
	        }
	    }
	}

	private static void println(String msg) {
		String now = DateTimeUtil.nowToString();
        System.out.println(now + " " + msg);
	}

}
