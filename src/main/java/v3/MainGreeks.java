package v3;

import java.util.List;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.BoardSuccess;
import io.swagger.client.model.PositionsSuccess;
import v2.AuthorizedToken;

/**
 * 残高のGreeksを集計する。
 */
public class MainGreeks {
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
	        double totalGanma = 0.0;
	        double totalTheta = 0.0;
	        double totalVega = 0.0;
	        int idx = 0;
	        for (int i = 0; i < response.size(); i++) {
		        PositionsSuccess pos = response.get(i);
		        String code = pos.getSymbol();
		        String name = pos.getSymbolName();
		        int sign = sign(pos.getSide());
		        int qty = (int)(sign * pos.getLeavesQty());
		        if (qty == 0) { // 数量0は除外
		        	continue;
		        }
		        Integer type = pos.getSecurityType();
		        if (type == null) { // ※先物・オプション銘柄以外はnull
		        	continue;
		        }
		        Double delta = null;
		        double ganma = 0.0;
		        double theta = 0.0;
		        double vega = 0.0;
		        double iv = 0.0;
		        switch (type) {
		        case 101: // 日経225先物
		        	delta = 1.0;
		        	break;
		        case 901: // 日経平均225ミニ先物
		        	delta = 0.1;
		        	break;
		        case 103: // 日経225OP
		        	BoardSuccess board = board(code);
		        	delta = board.getDelta();
		        	ganma = board.getGamma() * 100.0;
		        	theta = board.getTheta();
		        	vega = board.getVega();
		        	iv = board.getIV();
		        	break;
		        default:
		        	break;
		        }
		        if (delta != null) {
			        System.out.println((++idx) + ": " + name + " " + qty + " "
			        		+ delta + " " + doubleStr(qty * delta) + " " + ganma + " " + doubleStr(qty * ganma) + " "
			        		+ theta + " " + doubleStr(qty * theta) + " " + vega + " " + doubleStr(qty * vega) + " "
			        		+ iv);
			        totalDelta += (qty * delta);
			        totalGanma += (qty * ganma);
			        totalTheta += (qty * theta);
			        totalVega += (qty * vega);
		        }
	        }
	        System.out.println("total: " + doubleStr(totalDelta) + " " + doubleStr(totalGanma)
	        	+ " " + doubleStr(totalTheta) + " " + doubleStr(totalVega));
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

	static String doubleStr(double val) {
		return String.format("%6.4f", val);
	}

}
