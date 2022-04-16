package util;

/**
 * 文字列に関するユーティリティクラス。
 */
public class StringUtil {

	/**
	 * ポジション文字列(L or S)を取得する。
	 * 
	 * @param side 売買区分(Side)。
	 * @return ポジション(L or S)。
	 */
	public static String sideStr(String side) {
		switch (side) {
		case "1":
			return "S";
		case "2":
			return "L";
		default:
			throw new RuntimeException();
		}
	}

	/**
	 * "("以降を除外して、数値文字列を数値に変換する。
	 * 
	 * @param s 数値文字列。
	 * @return 数値。
	 */
	public static int parseInt(String s) {
		if (s == null) {
			return 0;
		}
		int idx = s.indexOf("(");
		if (idx >= 0) {
			s = s.substring(0, idx);
		}
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * "("以降を除外して、数値文字列を数値に変換する。
	 * 
	 * @param s 数値文字列。
	 * @return 数値。
	 */
	public static long parseLong(String s) {
		if (s == null) {
			return 0;
		}
		int idx = s.indexOf("(");
		if (idx >= 0) {
			s = s.substring(0, idx);
		}
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private StringUtil() {
	}

}