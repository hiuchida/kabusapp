package api.consts.stock;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExchangeSCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(1, ExchangeSCode.東証.intValue());
		assertEquals(3, ExchangeSCode.名証.intValue());
		assertEquals(5, ExchangeSCode.福証.intValue());
		assertEquals(6, ExchangeSCode.札証.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("1", ExchangeSCode.東証.toString());
		assertEquals("3", ExchangeSCode.名証.toString());
		assertEquals("5", ExchangeSCode.福証.toString());
		assertEquals("6", ExchangeSCode.札証.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(ExchangeSCode.東証, ExchangeSCode.valueOf(1));
		assertEquals(ExchangeSCode.名証, ExchangeSCode.valueOf(3));
		assertEquals(ExchangeSCode.福証, ExchangeSCode.valueOf(5));
		assertEquals(ExchangeSCode.札証, ExchangeSCode.valueOf(6));
	}

}
