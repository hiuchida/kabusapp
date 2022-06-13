package api.consts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SideCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(1, SideCode.売.intValue());
		assertEquals(2, SideCode.買.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("1", SideCode.売.toString());
		assertEquals("2", SideCode.買.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(SideCode.売, SideCode.valueOf(1));
		assertEquals(SideCode.買, SideCode.valueOf(2));
	}

}
