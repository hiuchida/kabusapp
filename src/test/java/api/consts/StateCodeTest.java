package api.consts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StateCodeTest {

	@Test
	public void intValueTest() {
		assertEquals(1, StateCode.待機.intValue());
		assertEquals(2, StateCode.処理中.intValue());
		assertEquals(3, StateCode.処理済.intValue());
		assertEquals(4, StateCode.訂正取消送信中.intValue());
		assertEquals(5, StateCode.終了.intValue());
	}

	@Test
	public void toStringTest() {
		assertEquals("1", StateCode.待機.toString());
		assertEquals("2", StateCode.処理中.toString());
		assertEquals("3", StateCode.処理済.toString());
		assertEquals("4", StateCode.訂正取消送信中.toString());
		assertEquals("5", StateCode.終了.toString());
	}

	@Test
	public void valueOfTest() {
		assertEquals(StateCode.待機, StateCode.valueOf(1));
		assertEquals(StateCode.処理中, StateCode.valueOf(2));
		assertEquals(StateCode.処理済, StateCode.valueOf(3));
		assertEquals(StateCode.訂正取消送信中, StateCode.valueOf(4));
		assertEquals(StateCode.終了, StateCode.valueOf(5));
	}

}
