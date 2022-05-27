package v21;

import com.google.gson.Gson;

import api.BoardBean;

public class MainParseBoardJson {

	public static void main(String[] args) {
		new MainParseBoardJson().execute();
	}

	public static final String JSON1 = "{\"OverSellQty\":470000.0,\"UnderBuyQty\":273600.0,\"TotalMarketValue\":936055074248.0,\"MarketOrderSellQty\":0.0,\"MarketOrderBuyQty\":0.0,\"BidTime\":\"2022-05-27T13:50:08+09:00\",\"AskTime\":\"2022-05-27T13:50:08+09:00\",\"Exchange\":1,\"ExchangeName\":\"東証プ\",\"TradingVolume\":661400.0,\"TradingVolumeTime\":\"2022-05-27T13:49:31+09:00\",\"VWAP\":1501.4233,\"TradingValue\":993041400.0,\"BidQty\":3200.0,\"BidPrice\":1498.0,\"BidSign\":\"0101\",\"Sell1\":{\"Price\":1498.0,\"Qty\":3200.0,\"Sign\":\"0101\",\"Time\":\"2022-05-27T13:50:08+09:00\"},\"Sell2\":{\"Price\":1499.0,\"Qty\":5300.0},\"Sell3\":{\"Price\":1500.0,\"Qty\":5700.0},\"Sell4\":{\"Price\":1501.0,\"Qty\":7500.0},\"Sell5\":{\"Price\":1502.0,\"Qty\":10000.0},\"Sell6\":{\"Price\":1503.0,\"Qty\":12600.0},\"Sell7\":{\"Price\":1504.0,\"Qty\":21600.0},\"Sell8\":{\"Price\":1505.0,\"Qty\":18000.0},\"Sell9\":{\"Price\":1506.0,\"Qty\":5000.0},\"Sell10\":{\"Price\":1507.0,\"Qty\":4600.0},\"AskQty\":2400.0,\"AskPrice\":1497.0,\"AskSign\":\"0101\",\"Buy1\":{\"Price\":1497.0,\"Qty\":2400.0,\"Sign\":\"0101\",\"Time\":\"2022-05-27T13:50:08+09:00\"},\"Buy2\":{\"Price\":1496.0,\"Qty\":9000.0},\"Buy3\":{\"Price\":1495.0,\"Qty\":11100.0},\"Buy4\":{\"Price\":1494.0,\"Qty\":14400.0},\"Buy5\":{\"Price\":1493.0,\"Qty\":7800.0},\"Buy6\":{\"Price\":1492.0,\"Qty\":34300.0},\"Buy7\":{\"Price\":1491.0,\"Qty\":9200.0},\"Buy8\":{\"Price\":1490.0,\"Qty\":37800.0},\"Buy9\":{\"Price\":1489.0,\"Qty\":5300.0},\"Buy10\":{\"Price\":1488.0,\"Qty\":8000.0},\"Symbol\":\"9005\",\"SymbolName\":\"東急\",\"CurrentPrice\":1498.0,\"CurrentPriceTime\":\"2022-05-27T13:49:31+09:00\",\"CurrentPriceChangeStatus\":\"0056\",\"CurrentPriceStatus\":1,\"CalcPrice\":1498.0,\"PreviousClose\":1506.00000,\"PreviousCloseTime\":\"2022-05-26T00:00:00+09:00\",\"ChangePreviousClose\":-8.00000,\"ChangePreviousClosePer\":-0.53,\"OpeningPrice\":1518.0,\"OpeningPriceTime\":\"2022-05-27T09:00:00+09:00\",\"HighPrice\":1518.0,\"HighPriceTime\":\"2022-05-27T09:00:00+09:00\",\"LowPrice\":1491.0,\"LowPriceTime\":\"2022-05-27T10:43:55+09:00\",\"SecurityType\":1}";
	public static final String JSON2 = "{\"ClearingPrice\":26740.0000,\"Exchange\":2,\"ExchangeName\":\"大阪日通し\",\"TradingVolume\":522319.0,\"TradingVolumeTime\":\"2022-05-25T08:45:01+09:00\",\"VWAP\":26674.0318,\"TradingValue\":1393235360000.0,\"BidQty\":2880.0,\"BidPrice\":26780.0,\"BidSign\":\"0101\",\"Sell1\":{\"Price\":26780.0,\"Qty\":2880.0,\"Sign\":\"0101\"},\"Sell2\":{\"Price\":26785.0,\"Qty\":24.0},\"Sell3\":{\"Price\":26790.0,\"Qty\":80.0},\"Sell4\":{\"Price\":26795.0,\"Qty\":121.0},\"Sell5\":{\"Price\":26800.0,\"Qty\":246.0},\"Sell6\":{\"Price\":26805.0,\"Qty\":67.0},\"Sell7\":{\"Price\":26810.0,\"Qty\":146.0},\"Sell8\":{\"Price\":26815.0,\"Qty\":86.0},\"Sell9\":{\"Price\":26820.0,\"Qty\":156.0},\"Sell10\":{\"Price\":26825.0,\"Qty\":203.0},\"AskQty\":2873.0,\"AskPrice\":26780.0,\"AskSign\":\"0101\",\"Buy1\":{\"Price\":26780.0,\"Qty\":14.0,\"Sign\":\"0101\"},\"Buy2\":{\"Price\":26775.0,\"Qty\":125.0},\"Buy3\":{\"Price\":26770.0,\"Qty\":83.0},\"Buy4\":{\"Price\":26765.0,\"Qty\":59.0},\"Buy5\":{\"Price\":26760.0,\"Qty\":76.0},\"Buy6\":{\"Price\":26755.0,\"Qty\":59.0},\"Buy7\":{\"Price\":26750.0,\"Qty\":69.0},\"Buy8\":{\"Price\":26745.0,\"Qty\":55.0},\"Buy9\":{\"Price\":26740.0,\"Qty\":210.0},\"Buy10\":{\"Price\":26735.0,\"Qty\":39.0},\"Symbol\":\"167060019\",\"SymbolName\":\"日経225mini 22/06\",\"CurrentPrice\":26785.0,\"CurrentPriceTime\":\"2022-05-25T08:45:01+09:00\",\"CurrentPriceChangeStatus\":\"0057\",\"CurrentPriceStatus\":1,\"CalcPrice\":26680.0000,\"PreviousClose\":26740.00000,\"PreviousCloseTime\":\"2022-05-24T00:00:00+09:00\",\"ChangePreviousClose\":45.00000,\"ChangePreviousClosePer\":0.17,\"OpeningPrice\":null,\"OpeningPriceTime\":null,\"HighPrice\":26840.0,\"HighPriceTime\":\"2022-05-24T20:08:53+09:00\",\"LowPrice\":26510.0,\"LowPriceTime\":\"2022-05-24T23:51:18+09:00\",\"SecurityType\":901}";
	public static final String JSON3 = "{\"IV\":19.53,\"Gamma\":0.000199,\"Theta\":-7.4614,\"Vega\":12.2253,\"Delta\":0.1360,\"Exchange\":2,\"ExchangeName\":\"大阪日通し\",\"TradingVolume\":1115.0,\"TradingVolumeTime\":\"2022-05-25T08:45:01+09:00\",\"VWAP\":64.8224,\"TradingValue\":72277000.0,\"BidQty\":4.0,\"BidPrice\":75.0,\"BidSign\":\"0101\",\"Sell1\":{\"Price\":75.0,\"Qty\":4.0,\"Sign\":\"0101\"},\"Sell2\":{\"Price\":76.0,\"Qty\":5.0},\"Sell3\":{\"Price\":77.0,\"Qty\":10.0},\"Sell4\":{\"Price\":78.0,\"Qty\":10.0},\"Sell5\":{\"Price\":80.0,\"Qty\":1.0},\"Sell6\":{\"Price\":87.0,\"Qty\":1.0},\"Sell7\":{\"Price\":88.0,\"Qty\":2.0},\"Sell8\":{\"Price\":90.0,\"Qty\":1.0},\"Sell9\":{\"Price\":94.0,\"Qty\":1.0},\"Sell10\":{\"Price\":95.0,\"Qty\":1.0},\"AskQty\":5.0,\"AskPrice\":63.0,\"AskSign\":\"0101\",\"Buy1\":{\"Price\":63.0,\"Qty\":5.0,\"Sign\":\"0101\"},\"Buy2\":{\"Price\":62.0,\"Qty\":7.0},\"Buy3\":{\"Price\":61.0,\"Qty\":10.0},\"Buy4\":{\"Price\":60.0,\"Qty\":1.0},\"Buy5\":{\"Price\":55.0,\"Qty\":1.0},\"Buy6\":{\"Price\":49.0,\"Qty\":1.0},\"Buy7\":{\"Price\":46.0,\"Qty\":1.0},\"Buy8\":{\"Price\":45.0,\"Qty\":2.0},\"Buy9\":{\"Price\":43.0,\"Qty\":1.0},\"Buy10\":{\"Price\":41.0,\"Qty\":3.0},\"Symbol\":\"197068018\",\"SymbolName\":\"日経平均オプション 22/06 コール 28000\",\"CurrentPrice\":74.0,\"CurrentPriceTime\":\"2022-05-25T08:45:01+09:00\",\"CurrentPriceChangeStatus\":\"\",\"CurrentPriceStatus\":1,\"CalcPrice\":74.0,\"PreviousClose\":68.00000,\"PreviousCloseTime\":\"2022-05-24T00:00:00+09:00\",\"ChangePreviousClose\":6.00000,\"ChangePreviousClosePer\":8.82,\"OpeningPrice\":70.0,\"OpeningPriceTime\":\"2022-05-24T16:30:19+09:00\",\"HighPrice\":79.0,\"HighPriceTime\":\"2022-05-24T20:08:47+09:00\",\"LowPrice\":50.0,\"LowPriceTime\":\"2022-05-24T23:48:49+09:00\",\"SecurityType\":103}";
	public static final String JSON4 = "{\"Symbol\":\"101\",\"SymbolName\":\"日経平均\",\"CurrentPrice\":26783.61,\"CurrentPriceTime\":\"2022-05-27T13:56:25+09:00\",\"CurrentPriceChangeStatus\":\"0058\",\"CurrentPriceStatus\":null,\"CalcPrice\":26604.84,\"PreviousClose\":26604.84000,\"PreviousCloseTime\":\"2022-05-26T00:00:00+09:00\",\"ChangePreviousClose\":178.77000,\"ChangePreviousClosePer\":0.67,\"OpeningPrice\":26947.8,\"OpeningPriceTime\":\"2022-05-27T09:00:05+09:00\",\"HighPrice\":26996.7,\"HighPriceTime\":\"2022-05-27T09:06:05+09:00\",\"LowPrice\":26731.6,\"LowPriceTime\":\"2022-05-27T12:52:50+09:00\",\"SecurityType\":0}";
	public void execute() {
		String[] ary = { JSON1, JSON2, JSON3, JSON4 };
		for (String s : ary) {
			BoardBean bb = parseJson(s);
			System.out.println(bb);
		}
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
