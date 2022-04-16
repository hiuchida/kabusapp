package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 時刻に関するユーティリティクラス。
 */
public class TimeUtil {

	/**
	 * 日付型から時刻を文字列で取得する。
	 * 
	 * @param date 日付型。
	 * @return 文字列。
	 */
	public static String toString(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(date);
	}

	private TimeUtil() {
	}

}
