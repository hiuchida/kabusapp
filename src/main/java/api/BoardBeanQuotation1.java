package api;

import com.google.gson.annotations.SerializedName;

/**
 * PUSH配信された時価情報の気配数量1本目。OffsetDateTime型はString型に変更。
 * 
 * @see io.swagger.client.model.BoardSuccessSell1
 * @see io.swagger.client.model.BoardSuccessBuy1
 */
public class BoardBeanQuotation1 {
	@SerializedName("Time")
	public String /* OffsetDateTime */ time = null;

	@SerializedName("Sign")
	public String sign = null;

	@SerializedName("Price")
	public Double price = null;

	@SerializedName("Qty")
	public Double qty = null;

	public BoardBeanQuotation1() {
	}

	/**
	 * Generate toString()
	 */
	@Override
	public String toString() {
		return "BoardBeanQuotation1 [time=" + time + ", sign=" + sign + ", price=" + price + ", qty=" + qty + "]";
	}

}
