package v21;

import com.google.gson.Gson;

import api.BoardBean;

public class MainParseBoardJson {

	public static void main(String[] args) {
		new MainParseBoardJson().execute();
	}

	public static final String JSON1 = "{\"ClearingPrice\":26740.0000,\"Exchange\":2,\"ExchangeName\":\"大阪日通し\",\"TradingVolume\":522319.0,\"TradingVolumeTime\":\"2022-05-25T08:45:01+09:00\",\"VWAP\":26674.0318,\"TradingValue\":1393235360000.0,\"BidQty\":2880.0,\"BidPrice\":26780.0,\"BidSign\":\"0101\",\"Sell1\":{\"Price\":26780.0,\"Qty\":2880.0,\"Sign\":\"0101\"},\"Sell2\":{\"Price\":26785.0,\"Qty\":24.0},\"Sell3\":{\"Price\":26790.0,\"Qty\":80.0},\"Sell4\":{\"Price\":26795.0,\"Qty\":121.0},\"Sell5\":{\"Price\":26800.0,\"Qty\":246.0},\"Sell6\":{\"Price\":26805.0,\"Qty\":67.0},\"Sell7\":{\"Price\":26810.0,\"Qty\":146.0},\"Sell8\":{\"Price\":26815.0,\"Qty\":86.0},\"Sell9\":{\"Price\":26820.0,\"Qty\":156.0},\"Sell10\":{\"Price\":26825.0,\"Qty\":203.0},\"AskQty\":2873.0,\"AskPrice\":26780.0,\"AskSign\":\"0101\",\"Buy1\":{\"Price\":26780.0,\"Qty\":14.0,\"Sign\":\"0101\"},\"Buy2\":{\"Price\":26775.0,\"Qty\":125.0},\"Buy3\":{\"Price\":26770.0,\"Qty\":83.0},\"Buy4\":{\"Price\":26765.0,\"Qty\":59.0},\"Buy5\":{\"Price\":26760.0,\"Qty\":76.0},\"Buy6\":{\"Price\":26755.0,\"Qty\":59.0},\"Buy7\":{\"Price\":26750.0,\"Qty\":69.0},\"Buy8\":{\"Price\":26745.0,\"Qty\":55.0},\"Buy9\":{\"Price\":26740.0,\"Qty\":210.0},\"Buy10\":{\"Price\":26735.0,\"Qty\":39.0},\"Symbol\":\"167060019\",\"SymbolName\":\"日経225mini 22/06\",\"CurrentPrice\":26785.0,\"CurrentPriceTime\":\"2022-05-25T08:45:01+09:00\",\"CurrentPriceChangeStatus\":\"0057\",\"CurrentPriceStatus\":1,\"CalcPrice\":26680.0000,\"PreviousClose\":26740.00000,\"PreviousCloseTime\":\"2022-05-24T00:00:00+09:00\",\"ChangePreviousClose\":45.00000,\"ChangePreviousClosePer\":0.17,\"OpeningPrice\":null,\"OpeningPriceTime\":null,\"HighPrice\":26840.0,\"HighPriceTime\":\"2022-05-24T20:08:53+09:00\",\"LowPrice\":26510.0,\"LowPriceTime\":\"2022-05-24T23:51:18+09:00\",\"SecurityType\":901}";

	public void execute() {
		BoardBean bb = parseJson(JSON1);
		System.out.println(bb);
	}

	/**
	 * 受信したメッセージJSONを解析し、時価情報Beanを生成する。
	 * 
	 * @param message 受信したメッセージ。
	 * @return 時価情報Bean。
	 */
	private BoardBean parseJson(String message) {
		Gson gson = new Gson();
		BoardBean bb = gson.fromJson(message, BoardBean.class);
		return bb;
	}

}
