package util;

import java.util.Calendar;

/**
 * 稼働スケジュールに関するユーティリティクラス。
 */
public class ScheduleUtil {

	/**
	 * 現在時刻の稼働状況を取得する。
	 * 
	 * @return true:稼働時間、false:非稼働時間。
	 */
	public static boolean now() {
		Calendar now = Calendar.getInstance();
		return isOperation(now);
	}

	/**
	 * 指定時刻の稼働状況を取得する。
	 * 
	 * @param now 指定時刻のカレンダー。
	 * @return true:稼働時間、false:非稼働時間。
	 */
	public static boolean isOperation(Calendar now) {
		String s = TimeUtil.toString(now.getTime());
		if ("07:45".compareTo(s) <= 0 && s.compareTo("08:15") < 0) {
			System.out.println("Out of Operation. " + s);
			return false;
		}
		return true;
	}

	private ScheduleUtil() {
	}

}
