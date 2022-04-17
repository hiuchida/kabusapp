package util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

public class ExchangeUtilTest {

	@Test
	public void exchangeTest() {
		int i = 0;
		Calendar c = Calendar.getInstance();
		for (int h = 0; h <= 4; h++) {
			for (int m = 0; m <= 59; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				int v = ExchangeUtil.exchange(c);
				assertEquals(24, v); // 夜間
				i++;
			}
		}
		{
			int h = 5;
			for (int m = 0; m <= 59; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				int v = ExchangeUtil.exchange(c);
				if (m <= 49) {
					assertEquals(24, v); // 夜間
				} else {
					assertEquals(-1, v); // 時間外
				}
				i++;
			}
		}
		for (int h = 6; h <= 7; h++) {
			for (int m = 0; m < 60; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				int v = ExchangeUtil.exchange(c);
				assertEquals(-1, v); // 時間外
				i++;
			}
		}
		{
			int h = 8;
			for (int m = 0; m <= 59; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				int v = ExchangeUtil.exchange(c);
				if (m <= 44) {
					assertEquals(-1, v); // 時間外
				} else {
					assertEquals(23, v); // 日中
				}
				i++;
			}
		}
		for (int h = 9; h <= 14; h++) {
			for (int m = 0; m < 60; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				int v = ExchangeUtil.exchange(c);
				assertEquals(23, v); // 日中
				i++;
			}
		}
		{
			int h = 15;
			for (int m = 0; m <= 59; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				int v = ExchangeUtil.exchange(c);
				if (m <= 4) {
					assertEquals(23, v); // 日中
				} else {
					assertEquals(-1, v); // 時間外
				}
				i++;
			}
		}
		{
			int h = 16;
			for (int m = 0; m <= 59; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				int v = ExchangeUtil.exchange(c);
				if (m <= 29) {
					assertEquals(-1, v); // 時間外
				} else {
					assertEquals(24, v); // 夜間
				}
				i++;
			}
		}
		for (int h = 17; h <= 23; h++) {
			for (int m = 0; m < 60; m++) {
				c.set(Calendar.HOUR_OF_DAY, h);
				c.set(Calendar.MINUTE, m);
				int v = ExchangeUtil.exchange(c);
				assertEquals(24, v); // 夜間
				i++;
			}
		}
		assertEquals(24 * 60, i); // テスト件数
	}

}
