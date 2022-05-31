package logic;

/**
 * 1分足の時刻を管理するクラス。
 */
public class ChartTime1mLogic {

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
		time = time.substring(0, 5) + ":00";
		if (time.compareTo("00:00") < 0) { // 00:00より小さい
			return null;
		} else if (time.compareTo("06:01") < 0) { // 06:05より小さい
			return time;
		} else if (time.compareTo("08:45") < 0) { // 08:45より小さい
			return null;
		} else if (time.compareTo("15:16") < 0) { // 15:20より小さい
			return time;
		} else if (time.compareTo("16:30") < 0) { // 16:30より小さい
			return null;
		} else if (time.compareTo("24:00") < 0) { // 24:00より小さい
			return time;
		}
		return null;
	}

	private ChartTime1mLogic() {
	}

}
