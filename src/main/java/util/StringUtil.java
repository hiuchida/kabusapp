package util;

/**
 * 文字列に関するユーティリティクラス。
 */
public class StringUtil {

	/**
	 * インデックス番号から2桁の文字列を生成する。
	 * 
	 * @param idx インデックス番号。
	 * @return 文字列。
	 */
	public static String index(int idx) {
		return String.format("%02d", idx);
	}

	/**
	 * 数量の符号を取得する。
	 * 
	 * @param side 売買区分(Side)。
	 * @return 符号
	 */
	public static int sign(String side) {
		switch (side) {
		case "1":
			return -1;
		case "2":
			return 1;
		default:
			throw new RuntimeException();
		}
	}

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
	 * 反対売買を取得する。
	 * 
	 * @param side 売買区分(Side)。
	 * @return 反対の売買区分(1 or 2)。
	 */
	public static String sideReturn(String side) {
		switch (side) {
		case "1":
			return "2";
		case "2":
			return "1";
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
