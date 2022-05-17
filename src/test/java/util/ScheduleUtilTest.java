package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

public class ScheduleUtilTest {

	@Test
	public void isOperationTest() {
		int i = 0;
		Calendar c = Calendar.getInstance();
		for (int h = 0; h <= 6; h++) {
			for (int m = 0; m <= 59; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				assertTrue(ScheduleUtil.isOperation(c));
				i++;
			}
		}
		{
			int h = 7;
			for (int m = 0; m <= 59; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				if (m <= 44) {
					assertTrue(ScheduleUtil.isOperation(c));
				} else {
					assertFalse(ScheduleUtil.isOperation(c));
				}
				i++;
			}
		}
		{
			int h = 8;
			for (int m = 0; m <= 59; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				if (m <= 14) {
					assertFalse(ScheduleUtil.isOperation(c));
				} else {
					assertTrue(ScheduleUtil.isOperation(c));
				}
				i++;
			}
		}
		for (int h = 9; h <= 23; h++) {
			for (int m = 0; m < 60; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				assertTrue(ScheduleUtil.isOperation(c));
				i++;
			}
		}
		assertEquals(24 * 60, i); // テスト件数
	}

}
