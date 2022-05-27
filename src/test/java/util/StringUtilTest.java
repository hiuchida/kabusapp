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
	public void joinTabATest() {
		String s1 = "ab";
		String s2 = "cd";
		String a1 = StringUtil.joinTab(s1, s2);
		String a2 = StringUtil.joinTab("", s2);
		String a3 = StringUtil.joinTab(s1, "");
		assertEquals("ab\tcd", a1);
		assertEquals("\tcd", a2);
		assertEquals("ab\t", a3);
	}

	@Test
	public void joinTabBTest() {
		String[] sa1 = { "ab", "cd", "ef" };
		String[] sa2 = { "", "cd", "ef" };
		String[] sa3 = { "ab", "", "ef" };
		String[] sa4 = { "ab", "cd", "" };
		String s1 = StringUtil.joinTab(sa1);
		String s2 = StringUtil.joinTab(sa2);
		String s3 = StringUtil.joinTab(sa3);
		String s4 = StringUtil.joinTab(sa4);
		assertEquals("ab\tcd\tef", s1);
		assertEquals("\tcd\tef", s2);
		assertEquals("ab\t\tef", s3);
		assertEquals("ab\tcd\t", s4);
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

	@Test
	public void joinCommaATest() {
		String s1 = "ab";
		String s2 = "cd";
		String a1 = StringUtil.joinComma(s1, s2);
		String a2 = StringUtil.joinComma("", s2);
		String a3 = StringUtil.joinComma(s1, "");
		assertEquals("ab,cd", a1);
		assertEquals(",cd", a2);
		assertEquals("ab,", a3);
	}

	@Test
	public void joinCommaBTest() {
		String[] sa1 = { "ab", "cd", "ef" };
		String[] sa2 = { "", "cd", "ef" };
		String[] sa3 = { "ab", "", "ef" };
		String[] sa4 = { "ab", "cd", "" };
		String s1 = StringUtil.joinComma(sa1);
		String s2 = StringUtil.joinComma(sa2);
		String s3 = StringUtil.joinComma(sa3);
		String s4 = StringUtil.joinComma(sa4);
		assertEquals("ab,cd,ef", s1);
		assertEquals(",cd,ef", s2);
		assertEquals("ab,,ef", s3);
		assertEquals("ab,cd,", s4);
	}

}
