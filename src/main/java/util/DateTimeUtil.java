package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日付や時刻に関するユーティリティクラス。
 */
public class DateTimeUtil {

	/**
	 * 現在の日付と時刻を文字列で取得する。
	 * 
	 * @return 文字列。
	 */
	public static String nowToString() {
		return toString(new Date());
	}

	/**
	 * 日付文字列から日付型を取得する。
	 * 
	 * @param date 日付文字列。
	 * @return 日付型。
	 */
	public static Date parseString(String s) {
		if (s == null) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			return sdf.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 日付型から日付と時刻を文字列で取得する。
	 * 
	 * @param date 日付型。
	 * @return 文字列。
	 */
	public static String toString(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		return sdf.format(date);
	}

	/**
	 * 時刻long値から日付と時刻を文字列で取得する。
	 * 
	 * @param time 時刻long値。
	 * @return 文字列。
	 */
	public static String toString(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		return sdf.format(time);
	}

	private DateTimeUtil() {
	}

}
