package api.consts.deliv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TimeInForceCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(1, TimeInForceCode.FAS.intValue());
		assertEquals(2, TimeInForceCode.FAK.intValue());
		assertEquals(3, TimeInForceCode.FOK.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("1", TimeInForceCode.FAS.toString());
		assertEquals("2", TimeInForceCode.FAK.toString());
		assertEquals("3", TimeInForceCode.FOK.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(TimeInForceCode.FAS, TimeInForceCode.valueOf(1));
		assertEquals(TimeInForceCode.FAK, TimeInForceCode.valueOf(2));
		assertEquals(TimeInForceCode.FOK, TimeInForceCode.valueOf(3));
	}

}
