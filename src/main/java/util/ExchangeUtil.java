package util;

import java.util.Calendar;

import api.consts.deliv.ExchangeDCode;

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
		String s = TimeUtil.toString(now.getTime());
		if ("08:45".compareTo(s) <= 0 && s.compareTo("15:05") < 0) {
			ret = ExchangeDCode.日中.intValue();
			System.out.println("Order of Day. " + s);
			return ret;
		}
		if ("16:30".compareTo(s) <= 0 && s.compareTo("23:59") <= 0) {
			ret = ExchangeDCode.夜間.intValue();
		} else if ("00:00".compareTo(s) <= 0 && s.compareTo("05:50") < 0) {
			ret = ExchangeDCode.夜間.intValue();
		}
		if (ret > 0) {
			System.out.println("Order of Night. " + s);
			return ret;
		}
		System.out.println("Out of Order. " + s);
//		int hour = now.get(Calendar.HOUR_OF_DAY);
//		int min = now.get(Calendar.MINUTE);
//		if (hour == 8 && 45 <= min) {
//			ret = ExchangeDCode.日中.intValue();
//		} else if (9 <= hour && hour <= 14) {
//			ret = ExchangeDCode.日中.intValue();
//		} else if (hour == 15 && min < 10 - 5) {
//			ret = ExchangeDCode.日中.intValue();
//		}
//		if (ret > 0) {
//			System.out.println("Order of Day. hour=" + hour + ", min=" + min);
//			return ret;
//		}
//		if (hour == 16 && 30 <= min) {
//			ret = ExchangeDCode.夜間.intValue();
//		} else if (17 <= hour && hour <= 23) {
//			ret = ExchangeDCode.夜間.intValue();
//		} else if (0 <= hour && hour <= 4) {
//			ret = ExchangeDCode.夜間.intValue();
//		} else if (hour == 5 && min < 55 - 5) {
//			ret = ExchangeDCode.夜間.intValue();
//		}
//		if (ret > 0) {
//			System.out.println("Order of Night. hour=" + hour + ", min=" + min);
//			return ret;
//		}
//		System.out.println("Out of Order. hour=" + hour + ", min=" + min);
		return -1;
	}

	private ExchangeUtil() {
	}

}
