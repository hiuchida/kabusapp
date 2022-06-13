package api.consts.stock;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AfterHitOrderTypeSCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(1, AfterHitOrderTypeSCode.成行.intValue());
		assertEquals(2, AfterHitOrderTypeSCode.指値.intValue());
		assertEquals(3, AfterHitOrderTypeSCode.不成.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("1", AfterHitOrderTypeSCode.成行.toString());
		assertEquals("2", AfterHitOrderTypeSCode.指値.toString());
		assertEquals("3", AfterHitOrderTypeSCode.不成.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(AfterHitOrderTypeSCode.成行, AfterHitOrderTypeSCode.valueOf(1));
		assertEquals(AfterHitOrderTypeSCode.指値, AfterHitOrderTypeSCode.valueOf(2));
		assertEquals(AfterHitOrderTypeSCode.不成, AfterHitOrderTypeSCode.valueOf(3));
	}

}
