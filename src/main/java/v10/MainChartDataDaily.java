package v10;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import util.FileUtil;
import util.StringUtil;

/**
 * チャートデータのＤＢを作成するツール。
 */
public class MainChartDataDaily {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * チャートデータディレクトリパス。
	 */
	private static final String DIR_FILEPATH = DIRPATH + "chart";
	/**
	 * チャートＤＢファイルパスのテンプレート。
	 */
	private static final String DB_FILEPATH = DIRPATH + "ChartData%s.db";

	/**
	 * チャートデータからチャートカレンダーを作成する。
	 */
	private static MainChartCalendar chartCalendar;

	/**
	 * チャートデータのＤＢを作成する。
	 */
	public static void main(String[] args) {
		try {
			chartCalendar = new MainChartCalendar();
			chartCalendar.initCalendar();
			new MainChartDataDaily("1d").execute();
		} finally {
		}
	}

	/**
	 * チャート種別。
	 */
	private String chartType;
	/**
	 * チャートＤＢファイルパス。存在しなければ生成される。
	 */
	private String dbFilePath;
	/**
	 * チャートＤＢマップ。
	 */
	private Map<String, String> dbMap;

	/**
	 * コンストラクタ。
	 * 
	 * @param chartType チャート種別。
	 */
	public MainChartDataDaily(String chartType) {
		this.chartType = chartType;
		this.dbFilePath = String.format(DB_FILEPATH, chartType);
	}

	/**
	 * チャートデータのＤＢを作成する。
	 */
	private void execute() {
		readChartDB();
		readAllChart(DIR_FILEPATH);
		writeChartDB();
	}

	/**
	 * チャートＤＢファイルを読み込む。
	 */
	private void readChartDB() {
		dbMap = new TreeMap<>();
		List<String> lines = FileUtil.readAllLines(dbFilePath);
		for (String s : lines) {
			if (s.startsWith("#")) {
				continue;
			}
			String key = getKey(s);
			dbMap.put(key, s);
		}
		System.out.println("MainChartDataDaily.readChartDB(): dbMap.size=" + dbMap.size());
	}

	/**
	 * チャートＤＢファイルを書き込む。
	 */
	private void writeChartDB() {
		System.out.println("MainChartDataDaily.writeChartDB(): dbMap.size=" + dbMap.size());
		List<String> lines = new ArrayList<>();
		lines.add("# date,open,high,low,close,volume");
		for (String key : dbMap.keySet()) {
			String val = dbMap.get(key);
			lines.add(val);
		}
		FileUtil.writeAllLines(dbFilePath, lines);
	}

	/**
	 * 複数のチャートデータファイルを読み込む。
	 * 
	 * @param dirpath チャートデータディレクトリパス。
	 */
	private void readAllChart(String dirpath) {
		String filter;
		switch (chartType) {
		case "1d":
			filter = "日足";
			break;
		default:
			return;
		}
		File dir = new File(dirpath);
		for (File f : dir.listFiles()) {
			if (!f.getName().endsWith(".csv")) {
				continue;
			}
			if (f.getName().indexOf(filter) < 0) {
				continue;
			}
			readChart(f.getPath());
		}
	}

	/**
	 * チャートデータファイルを読み込む。
	 * 
	 * @param filepath チャートデータファイルパス。
	 */
	private void readChart(String filepath) {
		List<String> lines = FileUtil.readAllLines(filepath);
		System.out.print("MainChartDataDaily.readChart(): " + filepath + ", lines.size=" + lines.size());
		int addCnt = 0;
		int updCnt = 0;
		for (String s : lines) {
			if (s.startsWith("#")) {
				continue;
			}
			if (s.length() < 10) {
				continue;
			}
			if (s.charAt(4) != '/' || s.charAt(7) != '/') {
				continue;
			}
			// 日足は終値を基準日にする。
//			// 日足は常に前営業日にする。
//			{
//				String odate = s.substring(0, 10);
//				String ndate = chartCalendar.searchCalendar(odate);
//				if (ndate == null) {
//					continue;
//				}
//				s = s.replaceAll(odate, ndate);
//			}
			String key = getKey(s);
			String nval = s;
			String oval = dbMap.get(key);
			if (oval == null) {
				dbMap.put(key, nval);
				addCnt++;
			} else if (!oval.equals(nval)) {
				String[] cols1 = StringUtil.splitComma(oval);
				String[] cols2 = StringUtil.splitComma(nval);
				int vol1 = Integer.parseInt(cols1[5]);
				int vol2 = Integer.parseInt(cols2[5]);
				if (vol1 < vol2) {
					dbMap.put(key, nval);
					updCnt++;
				}
			}
		}
		System.out.println(",  add " + addCnt + ", upd " + updCnt);
	}

	/**
	 * チャート種別に対応するキー文字列を取得する。
	 * 
	 * @param line 行文字列。
	 * @return キー文字列。
	 */
	private String getKey(String line) {
		String date = line.substring(0, 10);
		return date;
	}

}
