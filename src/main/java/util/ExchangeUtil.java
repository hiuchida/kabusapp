package util;

import java.util.Calendar;

/**
 * 市場コード（Exchange）に関するユーティリティクラス。
 */
public class ExchangeUtil {

	/**
	 * 現在時刻の市場コード（Exchange）を取得する。
	 * 
	 * @return 市場コード（Exchange）。
	 */
	public static int now() {
		Calendar now = Calendar.getInstance();
		return exchange(now);
	}

	/**
	 * 指定時刻の市場コード（Exchange）を取得する。
	 * 
	 * @param now 指定時刻のカレンダー。
	 * @return 市場コード（Exchange）。
	 */
	public static int exchange(Calendar now) {
		int ret = 0;
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		if (hour == 8 && 45 <= min) {
			ret = 23; // 日中
		} else if (9 <= hour && hour <= 14) {
			ret = 23; // 日中
		} else if (hour == 15 && min < 10 - 5) {
			ret = 23; // 日中
		}
		if (ret > 0) {
			System.out.println("Order of Day. hour=" + hour + ", min=" + min);
			return ret;
		}
		if (hour == 16 && 30 <= min) {
			ret = 24; // 夜間
		} else if (17 <= hour && hour <= 23) {
			ret = 24; // 夜間
		} else if (0 <= hour && hour <= 4) {
			ret = 24; // 夜間
		} else if (hour == 5 && min < 55 - 5) {
			ret = 24; // 夜間
		}
		if (ret > 0) {
			System.out.println("Order of Night. hour=" + hour + ", min=" + min);
			return ret;
		}
		System.out.println("Out of Order. hour=" + hour + ", min=" + min);
		return -1;
	}

	private ExchangeUtil() {
	}

}
