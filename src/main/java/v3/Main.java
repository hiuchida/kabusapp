package v3;

import java.util.List;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.BoardSuccess;
import io.swagger.client.model.PositionsSuccess;
import v2.AuthorizedToken;

public class Main {
	private static InfoApi infoApi = new InfoApi();
	
	public static void main(String[] args) throws ApiException {
		{
	        String X_API_KEY = AuthorizedToken.getToken();
	        String product = null;
	        String symbol = null;
	        String side = null;
	        String addinfo = null;
	        List<PositionsSuccess> response = infoApi.positionsGet(X_API_KEY, product, symbol, side, addinfo);

	        double totalDelta = 0.0;
	        for (int i = 0; i < response.size(); i++) {
		        PositionsSuccess pos = response.get(i);
		        int sign = sign(pos.getSide());
		        int qty = (int)(sign * pos.getLeavesQty());
		        Integer type = pos.getSecurityType();
		        if (type == null) { // ※先物・オプション銘柄以外はnull
		        	continue;
		        }
		        Double delta = null;
		        switch (type) {
		        case 101: // 日経225先物
		        	delta = 1.0;
		        	break;
		        case 901: // 日経平均225ミニ先物
		        	delta = 0.1;
		        	break;
		        case 103: // 日経225OP
		        	BoardSuccess board = board(pos.getSymbol());
		        	delta = board.getDelta();
		        	break;
		        default:
		        	break;
		        }
		        if (delta != null) {
			        System.out.println((i + 1) + ": " + pos.getSymbolName() + " " + qty + " " + delta + " " + (qty * delta));
			        totalDelta += (qty * delta);
		        }
	        }
	        System.out.println("total: " + totalDelta);
		}
	}

	static int sign(String side) {
		switch (side) {
		case "1":
			return -1;
		case "2":
			return 1;
		default:
			throw new RuntimeException();
		}
	}

	static BoardSuccess board(String symbol) throws ApiException {
        String X_API_KEY = AuthorizedToken.getToken();
        BoardSuccess response = infoApi.boardGet(X_API_KEY, symbol + "@2");
        try {
        	Thread.sleep(120); // 8.3req/sec
        } catch (Exception e) {
        }
        return response;
	}

}
