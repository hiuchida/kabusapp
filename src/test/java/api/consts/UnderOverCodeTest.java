package api.consts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnderOverCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(1, UnderOverCode.以下.intValue());
		assertEquals(2, UnderOverCode.以上.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("1", UnderOverCode.以下.toString());
		assertEquals("2", UnderOverCode.以上.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(UnderOverCode.以下, UnderOverCode.valueOf(1));
		assertEquals(UnderOverCode.以上, UnderOverCode.valueOf(2));
	}

}
