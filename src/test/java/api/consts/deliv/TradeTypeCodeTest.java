package api.consts.deliv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TradeTypeCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(1, TradeTypeCode.新規.intValue());
		assertEquals(2, TradeTypeCode.返済.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("1", TradeTypeCode.新規.toString());
		assertEquals("2", TradeTypeCode.返済.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(TradeTypeCode.新規, TradeTypeCode.valueOf(1));
		assertEquals(TradeTypeCode.返済, TradeTypeCode.valueOf(2));
	}

}
