package api.consts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ProductCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(0, ProductCode.すべて.intValue());
		assertEquals(1, ProductCode.現物.intValue());
		assertEquals(2, ProductCode.信用.intValue());
		assertEquals(3, ProductCode.先物.intValue());
		assertEquals(4, ProductCode.OP.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("0", ProductCode.すべて.toString());
		assertEquals("1", ProductCode.現物.toString());
		assertEquals("2", ProductCode.信用.toString());
		assertEquals("3", ProductCode.先物.toString());
		assertEquals("4", ProductCode.OP.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(ProductCode.すべて, ProductCode.valueOf(0));
		assertEquals(ProductCode.現物, ProductCode.valueOf(1));
		assertEquals(ProductCode.信用, ProductCode.valueOf(2));
		assertEquals(ProductCode.先物, ProductCode.valueOf(3));
		assertEquals(ProductCode.OP, ProductCode.valueOf(4));
	}

}
