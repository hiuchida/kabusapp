package api.consts.stock;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MarginTradeTypeCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(1, MarginTradeTypeCode.制度信用.intValue());
		assertEquals(2, MarginTradeTypeCode.一般信用_長期.intValue());
		assertEquals(3, MarginTradeTypeCode.一般信用_デイトレ.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("1", MarginTradeTypeCode.制度信用.toString());
		assertEquals("2", MarginTradeTypeCode.一般信用_長期.toString());
		assertEquals("3", MarginTradeTypeCode.一般信用_デイトレ.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(MarginTradeTypeCode.制度信用, MarginTradeTypeCode.valueOf(1));
		assertEquals(MarginTradeTypeCode.一般信用_長期, MarginTradeTypeCode.valueOf(2));
		assertEquals(MarginTradeTypeCode.一般信用_デイトレ, MarginTradeTypeCode.valueOf(3));
	}

}
