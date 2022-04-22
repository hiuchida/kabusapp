package util;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class StringUtilTest {

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

}
