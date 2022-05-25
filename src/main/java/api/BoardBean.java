package api;

import com.google.gson.annotations.SerializedName;

/**
 * PUSH配信された時価情報Beanクラス。OffsetDateTime型はString型に変更。
 * 
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

	@SerializedName("Sell1")
	public BoardBeanQuotation1 sell1 = null;

	@SerializedName("Sell2")
	public BoardBeanQuotation2 sell2 = null;

	@SerializedName("Sell3")
	public BoardBeanQuotation2 sell3 = null;

	@SerializedName("Sell4")
	public BoardBeanQuotation2 sell4 = null;

	@SerializedName("Sell5")
	public BoardBeanQuotation2 sell5 = null;

	@SerializedName("Sell6")
	public BoardBeanQuotation2 sell6 = null;

	@SerializedName("Sell7")
	public BoardBeanQuotation2 sell7 = null;

	@SerializedName("Sell8")
	public BoardBeanQuotation2 sell8 = null;

	@SerializedName("Sell9")
	public BoardBeanQuotation2 sell9 = null;

	@SerializedName("Sell10")
	public BoardBeanQuotation2 sell10 = null;

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

	@SerializedName("Buy1")
	public BoardBeanQuotation1 buy1 = null;

	@SerializedName("Buy2")
	public BoardBeanQuotation2 buy2 = null;

	@SerializedName("Buy3")
	public BoardBeanQuotation2 buy3 = null;

	@SerializedName("Buy4")
	public BoardBeanQuotation2 buy4 = null;

	@SerializedName("Buy5")
	public BoardBeanQuotation2 buy5 = null;

	@SerializedName("Buy6")
	public BoardBeanQuotation2 buy6 = null;

	@SerializedName("Buy7")
	public BoardBeanQuotation2 buy7 = null;

	@SerializedName("Buy8")
	public BoardBeanQuotation2 buy8 = null;

	@SerializedName("Buy9")
	public BoardBeanQuotation2 buy9 = null;

	@SerializedName("Buy10")
	public BoardBeanQuotation2 buy10 = null;

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
				+ bidSign + ", marketOrderSellQty=" + marketOrderSellQty + ", sell1=" + sell1 + ", sell2=" + sell2
				+ ", sell3=" + sell3 + ", sell4=" + sell4 + ", sell5=" + sell5 + ", sell6=" + sell6 + ", sell7=" + sell7
				+ ", sell8=" + sell8 + ", sell9=" + sell9 + ", sell10=" + sell10 + ", askQty=" + askQty + ", askPrice="
				+ askPrice + ", askTime=" + askTime + ", askSign=" + askSign + ", marketOrderBuyQty="
				+ marketOrderBuyQty + ", buy1=" + buy1 + ", buy2=" + buy2 + ", buy3=" + buy3 + ", buy4=" + buy4
				+ ", buy5=" + buy5 + ", buy6=" + buy6 + ", buy7=" + buy7 + ", buy8=" + buy8 + ", buy9=" + buy9
				+ ", buy10=" + buy10 + ", overSellQty=" + overSellQty + ", underBuyQty=" + underBuyQty
				+ ", totalMarketValue=" + totalMarketValue + ", clearingPrice=" + clearingPrice + ", IV=" + IV
				+ ", gamma=" + gamma + ", theta=" + theta + ", vega=" + vega + ", delta=" + delta + ", securityType="
				+ securityType + "]";
	}

}
