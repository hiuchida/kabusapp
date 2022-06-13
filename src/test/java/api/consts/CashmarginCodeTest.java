package api.consts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CashmarginCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(2, CashmarginCode.新規.intValue());
		assertEquals(3, CashmarginCode.返済.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("2", CashmarginCode.新規.toString());
		assertEquals("3", CashmarginCode.返済.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(CashmarginCode.新規, CashmarginCode.valueOf(2));
		assertEquals(CashmarginCode.返済, CashmarginCode.valueOf(3));
	}

}
