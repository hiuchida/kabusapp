package api.consts.stock;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FrontOrderTypeSCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(10, FrontOrderTypeSCode.成行.intValue());
		assertEquals(13, FrontOrderTypeSCode.寄成_前場.intValue());
		assertEquals(14, FrontOrderTypeSCode.寄成_後場.intValue());
		assertEquals(15, FrontOrderTypeSCode.引成_前場.intValue());
		assertEquals(16, FrontOrderTypeSCode.引成_後場.intValue());
		assertEquals(17, FrontOrderTypeSCode.IOC成行.intValue());
		assertEquals(20, FrontOrderTypeSCode.指値.intValue());
		assertEquals(21, FrontOrderTypeSCode.寄指_前場.intValue());
		assertEquals(22, FrontOrderTypeSCode.寄指_後場.intValue());
		assertEquals(23, FrontOrderTypeSCode.引指_前場.intValue());
		assertEquals(24, FrontOrderTypeSCode.引指_後場.intValue());
		assertEquals(25, FrontOrderTypeSCode.不成_前場.intValue());
		assertEquals(26, FrontOrderTypeSCode.不成_後場.intValue());
		assertEquals(27, FrontOrderTypeSCode.IOC指値.intValue());
		assertEquals(30, FrontOrderTypeSCode.逆指値.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("10", FrontOrderTypeSCode.成行.toString());
		assertEquals("13", FrontOrderTypeSCode.寄成_前場.toString());
		assertEquals("14", FrontOrderTypeSCode.寄成_後場.toString());
		assertEquals("15", FrontOrderTypeSCode.引成_前場.toString());
		assertEquals("16", FrontOrderTypeSCode.引成_後場.toString());
		assertEquals("17", FrontOrderTypeSCode.IOC成行.toString());
		assertEquals("20", FrontOrderTypeSCode.指値.toString());
		assertEquals("21", FrontOrderTypeSCode.寄指_前場.toString());
		assertEquals("22", FrontOrderTypeSCode.寄指_後場.toString());
		assertEquals("23", FrontOrderTypeSCode.引指_前場.toString());
		assertEquals("24", FrontOrderTypeSCode.引指_後場.toString());
		assertEquals("25", FrontOrderTypeSCode.不成_前場.toString());
		assertEquals("26", FrontOrderTypeSCode.不成_後場.toString());
		assertEquals("27", FrontOrderTypeSCode.IOC指値.toString());
		assertEquals("30", FrontOrderTypeSCode.逆指値.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(FrontOrderTypeSCode.成行, FrontOrderTypeSCode.valueOf(10));
		assertEquals(FrontOrderTypeSCode.寄成_前場, FrontOrderTypeSCode.valueOf(13));
		assertEquals(FrontOrderTypeSCode.寄成_後場, FrontOrderTypeSCode.valueOf(14));
		assertEquals(FrontOrderTypeSCode.引成_前場, FrontOrderTypeSCode.valueOf(15));
		assertEquals(FrontOrderTypeSCode.引成_後場, FrontOrderTypeSCode.valueOf(16));
		assertEquals(FrontOrderTypeSCode.IOC成行, FrontOrderTypeSCode.valueOf(17));
		assertEquals(FrontOrderTypeSCode.指値, FrontOrderTypeSCode.valueOf(20));
		assertEquals(FrontOrderTypeSCode.寄指_前場, FrontOrderTypeSCode.valueOf(21));
		assertEquals(FrontOrderTypeSCode.寄指_後場, FrontOrderTypeSCode.valueOf(22));
		assertEquals(FrontOrderTypeSCode.引指_前場, FrontOrderTypeSCode.valueOf(23));
		assertEquals(FrontOrderTypeSCode.引指_後場, FrontOrderTypeSCode.valueOf(24));
		assertEquals(FrontOrderTypeSCode.不成_前場, FrontOrderTypeSCode.valueOf(25));
		assertEquals(FrontOrderTypeSCode.不成_後場, FrontOrderTypeSCode.valueOf(26));
		assertEquals(FrontOrderTypeSCode.IOC指値, FrontOrderTypeSCode.valueOf(27));
		assertEquals(FrontOrderTypeSCode.逆指値, FrontOrderTypeSCode.valueOf(30));
	}

}
