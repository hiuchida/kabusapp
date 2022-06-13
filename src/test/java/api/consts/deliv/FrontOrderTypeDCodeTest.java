package api.consts.deliv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FrontOrderTypeDCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(18, FrontOrderTypeDCode.引成_派生.intValue());
		assertEquals(20, FrontOrderTypeDCode.指値.intValue());
		assertEquals(28, FrontOrderTypeDCode.引指_派生.intValue());
		assertEquals(30, FrontOrderTypeDCode.逆指値.intValue());
		assertEquals(120, FrontOrderTypeDCode.成行.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("18", FrontOrderTypeDCode.引成_派生.toString());
		assertEquals("20", FrontOrderTypeDCode.指値.toString());
		assertEquals("28", FrontOrderTypeDCode.引指_派生.toString());
		assertEquals("30", FrontOrderTypeDCode.逆指値.toString());
		assertEquals("120", FrontOrderTypeDCode.成行.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(FrontOrderTypeDCode.引成_派生, FrontOrderTypeDCode.valueOf(18));
		assertEquals(FrontOrderTypeDCode.指値, FrontOrderTypeDCode.valueOf(20));
		assertEquals(FrontOrderTypeDCode.引指_派生, FrontOrderTypeDCode.valueOf(28));
		assertEquals(FrontOrderTypeDCode.逆指値, FrontOrderTypeDCode.valueOf(30));
		assertEquals(FrontOrderTypeDCode.成行, FrontOrderTypeDCode.valueOf(120));
	}

}
