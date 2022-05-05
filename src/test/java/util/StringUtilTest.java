package util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringUtilTest {

	@Test
	public void parseStringATest() {
		String s1 = "abcdef";
		String a1 = StringUtil.parseString(s1, "def");
		assertEquals("abc", a1);
		String a2 = StringUtil.parseString(s1, "abc");
		assertEquals("", a2);
		String a3 = StringUtil.parseString(s1, "xyz");
		assertEquals("abcdef", a3);
	}

	@Test
	public void parseStringBTest() {
		String s1 = "abcdef";
		String a1 = StringUtil.parseString(s1, "ab", "ef");
		assertEquals("cd", a1);
		String a2 = StringUtil.parseString(s1, "abc", "def");
		assertEquals("", a2);
		String a3 = StringUtil.parseString(s1, "xyz", "def");
		assertNull(a3);
		String a4 = StringUtil.parseString(s1, "abc", "xyz");
		assertNull(a4);
	}

	@Test
	public void splitTabTest() {
		String TAB = "\t";
		String s1 = "a" + TAB + "b" + TAB + "c";
		String s2 = "a" + TAB + TAB + "c";
		String s3 = "a" + TAB + TAB;
		String s4 = TAB + "b" + TAB;
		String s5 = TAB + TAB + "c";
		String[] se1 = { "a", "b", "c", };
		String[] se2 = { "a", "", "c", };
		String[] se5 = { "", "", "c", };
		{
			String[] sa1 = StringUtil.splitTab(s1);
			String[] sa2 = StringUtil.splitTab(s2);
			String[] sa3 = StringUtil.splitTab(s3);
			String[] sa4 = StringUtil.splitTab(s4);
			String[] sa5 = StringUtil.splitTab(s5);
			String[] se3 = { "a", "", "", };
			String[] se4 = { "", "b", "", };
			assertArrayEquals(s1, se1, sa1);
			assertArrayEquals(s2, se2, sa2);
			assertArrayEquals(s3, se3, sa3);
			assertArrayEquals(s4, se4, sa4);
			assertArrayEquals(s5, se5, sa5);
		}
		{
			String[] sa1 = s1.split(TAB);
			String[] sa2 = s2.split(TAB);
			String[] sa3 = s3.split(TAB);
			String[] sa4 = s4.split(TAB);
			String[] sa5 = s5.split(TAB);
			String[] se3 = { "a", };
			String[] se4 = { "", "b", };
			assertArrayEquals(s1, se1, sa1);
			assertArrayEquals(s2, se2, sa2);
			assertArrayEquals(s3, se3, sa3);
			assertArrayEquals(s4, se4, sa4);
			assertArrayEquals(s5, se5, sa5);
		}
	}

	@Test
	public void splitCommaTest() {
		String COMMA = ",";
		String s1 = "a" + COMMA + "b" + COMMA + "c";
		String s2 = "a" + COMMA + COMMA + "c";
		String s3 = "a" + COMMA + COMMA;
		String s4 = COMMA + "b" + COMMA;
		String s5 = COMMA + COMMA + "c";
		String[] se1 = { "a", "b", "c", };
		String[] se2 = { "a", "", "c", };
		String[] se5 = { "", "", "c", };
		{
			String[] sa1 = StringUtil.splitComma(s1);
			String[] sa2 = StringUtil.splitComma(s2);
			String[] sa3 = StringUtil.splitComma(s3);
			String[] sa4 = StringUtil.splitComma(s4);
			String[] sa5 = StringUtil.splitComma(s5);
			String[] se3 = { "a", "", "", };
			String[] se4 = { "", "b", "", };
			assertArrayEquals(s1, se1, sa1);
			assertArrayEquals(s2, se2, sa2);
			assertArrayEquals(s3, se3, sa3);
			assertArrayEquals(s4, se4, sa4);
			assertArrayEquals(s5, se5, sa5);
		}
		{
			String[] sa1 = s1.split(COMMA);
			String[] sa2 = s2.split(COMMA);
			String[] sa3 = s3.split(COMMA);
			String[] sa4 = s4.split(COMMA);
			String[] sa5 = s5.split(COMMA);
			String[] se3 = { "a", };
			String[] se4 = { "", "b", };
			assertArrayEquals(s1, se1, sa1);
			assertArrayEquals(s2, se2, sa2);
			assertArrayEquals(s3, se3, sa3);
			assertArrayEquals(s4, se4, sa4);
			assertArrayEquals(s5, se5, sa5);
		}
	}

}
