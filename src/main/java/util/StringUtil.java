package util;

import java.util.ArrayList;
import java.util.List;

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
	 * 市場名を取得する。
	 * 
	 * @param exchange 市場コード(Exchange)。
	 * @return 市場名。
	 */
	public static String exchangeStr(int exchange) {
		switch (exchange) {
		case 2:
			return "日通し";
		case 23:
			return " 日中 ";
		case 24:
			return " 夜間 ";
		default:
			throw new RuntimeException();
		}
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
	 * ポジション(1 or 2)を取得する。
	 * 
	 * @param side ポジション(L or S)。
	 * @return 売買区分(Side)。
	 */
	public static String sideCode(String side) {
		switch (side) {
		case "S":
			return "1";
		case "L":
			return "2";
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
	 * 以上／以下を取得する。
	 * 
	 * @param side 売買区分(Side)。
	 * @return 以上／以下(1 or 2)。
	 */
	public static int underOver(String side) {
		switch (side) {
		case "1":
			return 1;
		case "2":
			return 2;
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

	/**
	 * 指定した文字以降を除外して、文字列を取得する。
	 * 
	 * @param s 文字列。
	 * @param d 区切り文字列。
	 * @return 文字列。
	 */
	public static String parseString(String s, String d) {
		if (s == null) {
			return s;
		}
		int idx = s.indexOf(d);
		if (idx >= 0) {
			s = s.substring(0, idx);
		}
		return s;
	}

	/**
	 * タブ文字で分割する。String.split()と異なり、行末の空文字列にも対応する。
	 * 
	 * @param s 文字列。
	 * @return 分割した文字列の配列。
	 */
	public static String[] splitTab(String s) {
		List<String> list = new ArrayList<>();
		while (true) {
			int idx = s.indexOf('\t');
			if (idx < 0) {
				list.add(s);
				break;
			}
			list.add(s.substring(0, idx));
			s = s.substring(idx + 1);
		}
		String[] ary = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ary[i] = list.get(i);
		}
		return ary;
	}

	/**
	 * カンマ文字で分割する。String.split()と異なり、行末の空文字列にも対応する。
	 * 
	 * @param s 文字列。
	 * @return 分割した文字列の配列。
	 */
	public static String[] splitComma(String s) {
		List<String> list = new ArrayList<>();
		while (true) {
			int idx = s.indexOf(',');
			if (idx < 0) {
				list.add(s);
				break;
			}
			list.add(s.substring(0, idx));
			s = s.substring(idx + 1);
		}
		String[] ary = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ary[i] = list.get(i);
		}
		return ary;
	}

	private StringUtil() {
	}

}
