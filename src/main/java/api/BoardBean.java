package api;

import com.google.gson.annotations.SerializedName;

/**
 * PUSH配信された時価情報Beanクラス。Sell1～Sell10、Buy1～Buy10を除く。OffsetDateTime型はString型に変更。
 * @see io.swagger.client.model.BoardSuccess
 */
public class BoardBean {
	@SerializedName("Symbol")
	public String symbol = null;

	@SerializedName("SymbolName")
	public String symbolName = null;

	@SerializedName("Exchange")
	public Integer exchange = null;

	@SerializedName("ExchangeName")
	public String exchangeName = null;

	@SerializedName("CurrentPrice")
	public Double currentPrice = null;

	@SerializedName("CurrentPriceTime")
	public String /* OffsetDateTime */ currentPriceTime = null;

	@SerializedName("CurrentPriceChangeStatus")
	public String currentPriceChangeStatus = null;

	@SerializedName("CurrentPriceStatus")
	public Integer currentPriceStatus = null;

	@SerializedName("CalcPrice")
	public Double calcPrice = null;

	@SerializedName("PreviousClose")
	public Double previousClose = null;

	@SerializedName("PreviousCloseTime")
	public String /* OffsetDateTime */ previousCloseTime = null;

	@SerializedName("ChangePreviousClose")
	public Double changePreviousClose = null;

	@SerializedName("ChangePreviousClosePer")
	public Double changePreviousClosePer = null;

	@SerializedName("OpeningPrice")
	public Double openingPrice = null;

	@SerializedName("OpeningPriceTime")
	public String /* OffsetDateTime */ openingPriceTime = null;

	@SerializedName("HighPrice")
	public Double highPrice = null;

	@SerializedName("HighPriceTime")
	public String /* OffsetDateTime */ highPriceTime = null;

	@SerializedName("LowPrice")
	public Double lowPrice = null;

	@SerializedName("LowPriceTime")
	public String /* OffsetDateTime */ lowPriceTime = null;

	@SerializedName("TradingVolume")
	public Double tradingVolume = null;

	@SerializedName("TradingVolumeTime")
	public String /* OffsetDateTime */ tradingVolumeTime = null;

	@SerializedName("VWAP")
	public Double VWAP = null;

	@SerializedName("TradingValue")
	public Double tradingValue = null;

	@SerializedName("BidQty")
	public Double bidQty = null;

	@SerializedName("BidPrice")
	public Double bidPrice = null;

	@SerializedName("BidTime")
	public String /* OffsetDateTime */ bidTime = null;

	@SerializedName("BidSign")
	public String bidSign = null;

	@SerializedName("MarketOrderSellQty")
	public Double marketOrderSellQty = null;

	@SerializedName("AskQty")
	public Double askQty = null;

	@SerializedName("AskPrice")
	public Double askPrice = null;

	@SerializedName("AskTime")
	public String /* OffsetDateTime */ askTime = null;

	@SerializedName("AskSign")
	public String askSign = null;

	@SerializedName("MarketOrderBuyQty")
	public Double marketOrderBuyQty = null;

	@SerializedName("OverSellQty")
	public Double overSellQty = null;

	@SerializedName("UnderBuyQty")
	public Double underBuyQty = null;

	@SerializedName("TotalMarketValue")
	public Double totalMarketValue = null;

	@SerializedName("ClearingPrice")
	public Double clearingPrice = null;

	@SerializedName("IV")
	public Double IV = null;

	@SerializedName("Gamma")
	public Double gamma = null;

	@SerializedName("Theta")
	public Double theta = null;

	@SerializedName("Vega")
	public Double vega = null;

	@SerializedName("Delta")
	public Double delta = null;

	@SerializedName("SecurityType")
	public Integer securityType = null;

	public BoardBean() {
	}

	/**
	 * Generate toString()
	 */
	@Override
	public String toString() {
		return "BoardBean [symbol=" + symbol + ", symbolName=" + symbolName + ", exchange=" + exchange
				+ ", exchangeName=" + exchangeName + ", currentPrice=" + currentPrice + ", currentPriceTime="
				+ currentPriceTime + ", currentPriceChangeStatus=" + currentPriceChangeStatus + ", currentPriceStatus="
				+ currentPriceStatus + ", calcPrice=" + calcPrice + ", previousClose=" + previousClose
				+ ", previousCloseTime=" + previousCloseTime + ", changePreviousClose=" + changePreviousClose
				+ ", changePreviousClosePer=" + changePreviousClosePer + ", openingPrice=" + openingPrice
				+ ", openingPriceTime=" + openingPriceTime + ", highPrice=" + highPrice + ", highPriceTime="
				+ highPriceTime + ", lowPrice=" + lowPrice + ", lowPriceTime=" + lowPriceTime + ", tradingVolume="
				+ tradingVolume + ", tradingVolumeTime=" + tradingVolumeTime + ", VWAP=" + VWAP + ", tradingValue="
				+ tradingValue + ", bidQty=" + bidQty + ", bidPrice=" + bidPrice + ", bidTime=" + bidTime + ", bidSign="
				+ bidSign + ", marketOrderSellQty=" + marketOrderSellQty + ", askQty=" + askQty + ", askPrice="
				+ askPrice + ", askTime=" + askTime + ", askSign=" + askSign + ", marketOrderBuyQty="
				+ marketOrderBuyQty + ", overSellQty=" + overSellQty + ", underBuyQty=" + underBuyQty
				+ ", totalMarketValue=" + totalMarketValue + ", clearingPrice=" + clearingPrice + ", IV=" + IV
				+ ", gamma=" + gamma + ", theta=" + theta + ", vega=" + vega + ", delta=" + delta + ", securityType="
				+ securityType + "]";
	}

}
