package api;

import com.google.gson.annotations.SerializedName;

/**
 * PUSH配信された時価情報の気配数量2本目～10本目。
 * 
 * @see io.swagger.client.model.BoardSuccessSell2
 * @see io.swagger.client.model.BoardSuccessBuy2
 */
public class BoardBeanQuotation2 {
	@SerializedName("Price")
	public Double price = null;

	@SerializedName("Qty")
	public Double qty = null;

	public BoardBeanQuotation2() {
	}

	/**
	 * Generate toString()
	 */
	@Override
	public String toString() {
		return "BoardBeanQuotation2 [price=" + price + ", qty=" + qty + "]";
	}

}
