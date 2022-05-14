package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * グローバル設定ファイルを管理する。
 */
public class GlobalConfigUtil {
	/**
	 * 設定ファイルのカラム数。
	 */
	public static final int MAX_COLS = 2;

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * グローバル設定を保存したファイルパス。事前に準備する。
	 */
	private static final String CFG_FILEPATH = DIRPATH + "Global.cfg";

	/**
	 * 設定のマップ。
	 */
	private static Map<String, String> map = new TreeMap<>();

	static {
		readConfig();
	}

	/**
	 * 設定ファイルを読み込む。不正なレコードは無視される。
	 */
	public static void readConfig() {
		List<String> lines = FileUtil.readAllLines(CFG_FILEPATH);
		System.out.println("--- " + CFG_FILEPATH + " ---");
		System.out.println("GlobalConfigUtil.readConfig(): lines.size=" + lines.size());
		for (String s : lines) {
			if (s.length() == 0) {
				continue;
			}
			if (s.startsWith("#")) {
				continue;
			}
			String[] cols = StringUtil.splitTab(s);
			if (cols.length != MAX_COLS) {
				System.out.println("Warning: SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			map.put(cols[0], cols[1]);
		}
	}

	/**
	 * 設定キーのリストを取得する。
	 * 
	 * @return 設定キーのリスト。
	 */
	public static List<String> getKeys() {
		List<String> list = new ArrayList<>();
		list.addAll(map.keySet());
		return list;
	}

	/**
	 * 設定値の文字列を取得する。
	 * 
	 * @param key 設定キー。
	 * @return 設定値。
	 */
	public static String get(String key) {
		String val = map.get(key);
		return val;
	}

	/**
	 * 設定値の数値を取得する。
	 * 
	 * @param key 設定キー。
	 * @param def デフォルト値。
	 * @return 設定値。
	 */
	public static int getInt(String key, int def) {
		String val = get(key);
		if (val == null) {
			return def;
		}
		try {
			return Integer.parseInt(val);
		} catch (Exception e) {
		}
		return def;
	}

	/**
	 * 設定値の文字列を設定する。
	 * 
	 * @param key 設定キー。
	 * @param val 設定値の文字列。
	 */
	public void put(String key, String val) {
		map.put(key, val);
	}

	private GlobalConfigUtil() {
	}

}
