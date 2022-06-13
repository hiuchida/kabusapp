package v27;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import util.FileUtil;
import util.StringUtil;

/**
 * チャートデータのＤＢを作成するツール。
 */
public class MainChartDataHourly_r3 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * チャートデータディレクトリパス。
	 */
	private static final String DIR_FILEPATH = DIRPATH + "download";
	/**
	 * チャートデータディレクトリパス。
	 */
	private static final String DIR_DBPATH = DIRPATH + "db";
	/**
	 * チャートＤＢファイルパスのテンプレート。
	 */
	private static final String DB_FILENAME = "ChartData%s.db";

	/**
	 * チャートデータからチャートカレンダーを作成する。
	 */
	private static MainChartCalendar_r3 chartCalendar;

	/**
	 * 銘柄コードのリスト。
	 */
	private static Set<String> codeSet;

	/**
	 * チャートデータのＤＢを作成する。
	 */
	public static void main(String[] args) {
		listChartFiles(DIR_FILEPATH);
		chartCalendar = new MainChartCalendar_r3();
		chartCalendar.initCalendar();
		for (String code : codeSet) {
			new MainChartDataHourly_r3(code, "60m").execute();
			new MainChartDataHourly_r3(code, "30m").execute();
			new MainChartDataHourly_r3(code, "20m").execute();
			new MainChartDataHourly_r3(code, "15m").execute();
			new MainChartDataHourly_r3(code, "10m").execute();
			new MainChartDataHourly_r3(code, "5m").execute();
			new MainChartDataHourly_r3(code, "3m").execute();
			new MainChartDataHourly_r3(code, "1m").execute();
		}
	}

	/**
	 * 複数のチャートデータファイルを読み込む。
	 * 
	 * @param dirpath チャートデータディレクトリパス。
	 */
	private static void listChartFiles(String dirpath) {
		codeSet = new TreeSet<>();
		File dir = new File(dirpath);
		for (File f : dir.listFiles()) {
			String name = f.getName();
			if (!name.endsWith(".csv")) {
				continue;
			}
			String code = StringUtil.parseString(name, "_");
			codeSet.add(code);
		}
	}

	/**
	 * 銘柄コード。
	 */
	private String code;
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
	 * @param code      銘柄コード。
	 * @param chartType チャート種別。
	 */
	public MainChartDataHourly_r3(String code, String chartType) {
		this.code = code;
		this.chartType = chartType;
		String dirPath = DIR_DBPATH + "/" + code;
		FileUtil.mkdirs(dirPath);
		this.dbFilePath = dirPath + "/" + String.format(DB_FILENAME, chartType);
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
		if (dbMap.size() > 0) {
			System.out.println("MainChartDataHourly.readChartDB(): " + dbFilePath + ", dbMap.size=" + dbMap.size());
		}
	}

	/**
	 * チャートＤＢファイルを書き込む。
	 */
	private void writeChartDB() {
		if (dbMap.size() > 0) {
			System.out.println("MainChartDataHourly.writeChartDB(): " + dbFilePath + ", dbMap.size=" + dbMap.size());
			List<String> lines = new ArrayList<>();
			lines.add("# date,open,high,low,close,volume");
			for (String key : dbMap.keySet()) {
				String val = dbMap.get(key);
				lines.add(val);
			}
			FileUtil.writeAllLines(dbFilePath, lines);
		} else {
			FileUtil.deleteFile(dbFilePath);
		}
	}

	/**
	 * 複数のチャートデータファイルを読み込む。
	 * 
	 * @param dirpath チャートデータディレクトリパス。
	 */
	private void readAllChart(String dirpath) {
		String filter;
		switch (chartType) {
		case "60m":
			filter = "60分足";
			break;
		case "30m":
			filter = "30分足";
			break;
		case "20m":
			filter = "20分足";
			break;
		case "15m":
			filter = "15分足";
			break;
		case "10m":
			filter = "10分足";
			break;
		case "5m":
			filter = "5分足";
			break;
		case "3m":
			filter = "3分足";
			break;
		case "1m":
			filter = "1分足";
			break;
		default:
			return;
		}
		File dir = new File(dirpath);
		for (File f : dir.listFiles()) {
			String name = f.getName();
			if (!name.endsWith(".csv")) {
				continue;
			}
			if (!name.startsWith(code)) {
				continue;
			}
			if (name.indexOf(filter) < 0) {
				continue;
			}
			readChart(f.getPath());
			moveArchive(f);
		}
	}

	/**
	 * チャートデータファイルを読み込む。
	 * 
	 * @param filepath チャートデータファイルパス。
	 */
	private void readChart(String filepath) {
		List<String> lines = FileUtil.readAllLines(filepath);
		System.out.print("MainChartDataHourly.readChart(): " + filepath + ", lines.size=" + lines.size());
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
			if (s.length() < 19) {
				continue;
			}
			if (s.charAt(13) != ':' || s.charAt(16) != ':') {
				continue;
			}
			// 時間足は以下の3通りに条件分岐する。
			// 08時から15時：そのまま
			// 16時から23時：前営業日にする
			// 00時から06時：前営業日の翌日にする
			int hh = Integer.parseInt(s.substring(11, 13));
			if (0 <= hh && hh <= 6 || 16 <= hh && hh <= 23) {
				String odate = s.substring(0, 10);
				String ndate = chartCalendar.searchCalendar(odate);
				if (ndate == null) {
					continue;
				}
				if (0 <= hh && hh <= 6) {
					ndate = chartCalendar.nextDay(ndate);
				}
				s = s.replaceAll(odate, ndate);
			}
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
		String date = line.substring(0, 19);
		return date;
	}

	private void moveArchive(File f) {
		File pf = f.getParentFile();
		File cf = new File(pf, "archive");
		cf.mkdirs();
		File of = new File(cf, f.getName());
		f.renameTo(of);
	}

}
