package api.consts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExchangeCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(1, ExchangeCode.東証.intValue());
		assertEquals(3, ExchangeCode.名証.intValue());
		assertEquals(5, ExchangeCode.福証.intValue());
		assertEquals(6, ExchangeCode.札証.intValue());
		assertEquals(2, ExchangeCode.日通し.intValue());
		assertEquals(23, ExchangeCode.日中.intValue());
		assertEquals(24, ExchangeCode.夜間.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("1", ExchangeCode.東証.toString());
		assertEquals("3", ExchangeCode.名証.toString());
		assertEquals("5", ExchangeCode.福証.toString());
		assertEquals("6", ExchangeCode.札証.toString());
		assertEquals("2", ExchangeCode.日通し.toString());
		assertEquals("23", ExchangeCode.日中.toString());
		assertEquals("24", ExchangeCode.夜間.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(ExchangeCode.東証, ExchangeCode.valueOf(1));
		assertEquals(ExchangeCode.名証, ExchangeCode.valueOf(3));
		assertEquals(ExchangeCode.福証, ExchangeCode.valueOf(5));
		assertEquals(ExchangeCode.札証, ExchangeCode.valueOf(6));
		assertEquals(ExchangeCode.日通し, ExchangeCode.valueOf(2));
		assertEquals(ExchangeCode.日中, ExchangeCode.valueOf(23));
		assertEquals(ExchangeCode.夜間, ExchangeCode.valueOf(24));
	}

}
