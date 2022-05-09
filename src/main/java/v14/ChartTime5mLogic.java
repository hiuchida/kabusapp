package v14;

/**
 * 5分足の時刻を管理するクラス。
 */
public class ChartTime5mLogic {
	/**
	 * 日中(08:45-15:20)の時刻一覧。
	 */
	private static String[] time1 = new String[79 + 1]; // 79 = 13 * 6 + 1
	/**
	 * 夜間(16:30-24:00)の時刻一覧。
	 */
	private static String[] time2 = new String[90 + 1]; // 90 = 15 * 6
	/**
	 * 夜間(00:00-06:05)の時刻一覧。
	 */
	private static String[] time3 = new String[73 + 1]; // 73 = 12 * 6 + 1

	static {
		init1();
		init2();
		init3();
	}

	/**
	 * 日中(08:45-15:20)の時刻一覧の初期化。
	 */
	private static void init1() {
		int hour = 8;
		int min = 45;
		for (int i = 0; i < time1.length; i++) {
			String time = String.format("%02d:%02d:00", hour, min);
			time1[i] = time;
			min += 5;
			if (min >= 60) {
				hour++;
				min -= 60;
			}
		}
	}

	/**
	 * 夜間(16:30-24:00)の時刻一覧。
	 */
	private static void init2() {
		int hour = 16;
		int min = 30;
		for (int i = 0; i < time2.length; i++) {
			String time = String.format("%02d:%02d:00", hour, min);
			time2[i] = time;
			min += 5;
			if (min >= 60) {
				hour++;
				min -= 60;
			}
		}
	}

	/**
	 * 夜間(00:00-06:05)の時刻一覧。
	 */
	private static void init3() {
		int hour = 0;
		int min = 0;
		for (int i = 0; i < time3.length; i++) {
			String time = String.format("%02d:%02d:00", hour, min);
			time3[i] = time;
			min += 5;
// 05:55が無い日もあるが、リストに含める。
//			if ("05:50:00".equals(time)) {
//				min += 5;
//			}
			if (min >= 60) {
				hour++;
				min -= 60;
			}
		}
	}

	/**
	 * 指定した時刻が所属する時刻を検索する。
	 * 
	 * @param time 現値の時刻。
	 * @return 所属する時刻。範囲外の場合はnull。
	 */
	public static String search(String time) {
		if (time == null) {
			return null;
		}
		if (time.compareTo(time3[0]) < 0) { // 00:00より小さい
			return null;
		} else if (time.compareTo(time3[time3.length - 1]) < 0) { // 06:05より小さい
			return searchTable(time3, time);
		} else if (time.compareTo(time1[0]) < 0) { // 08:45より小さい
			return null;
		} else if (time.compareTo(time1[time1.length - 1]) < 0) { // 15:20より小さい
			return searchTable(time1, time);
		} else if (time.compareTo(time2[0]) < 0) { // 16:30より小さい
			return null;
		} else if (time.compareTo(time2[time2.length - 1]) < 0) { // 24:00より小さい
			return searchTable(time2, time);
		}
		return null;
	}

	/**
	 * 時刻の一覧から時刻を検索する。
	 * 
	 * @param times 検索する時刻の一覧。
	 * @param time  現値の時刻。
	 * @return 所属する時刻。範囲外の場合はnull。
	 */
	private static String searchTable(String[] times, String time) {
		for (int i = 0; i < times.length - 1; i++) {
			if (times[i].compareTo(time) <= 0 && time.compareTo(times[i + 1]) < 0) {
				return times[i];
			}
		}
		return null;
	}

	private ChartTime5mLogic() {
	}

}
