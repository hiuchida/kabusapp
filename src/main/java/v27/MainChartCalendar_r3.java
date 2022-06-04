package v27;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import util.DateUtil;
import util.FileUtil;

/**
 * チャートデータからチャートカレンダーを作成するツール。
 */
public class MainChartCalendar_r3 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * チャートデータディレクトリパス。
	 */
	private static final String DIR_FILEPATH = DIRPATH + "chart";
	/**
	 * チャートデータディレクトリパス。
	 */
	private static final String DIR_DBPATH = DIRPATH + "db";
	/**
	 * チャートカレンダーファイルパス。存在しなければ生成される。
	 */
	private static final String TXT_FILEPATH = DIR_DBPATH + "/" + "ChartCalendar.db";

	/**
	 * チャートデータからチャートカレンダーを作成する。
	 */
	public static void main(String[] args) {
		new MainChartCalendar_r3().execute();
	}

	/**
	 * チャートカレンダーのセット。
	 */
	private Set<String> calendarSet;
	/**
	 * チャートカレンダーのリスト。
	 */
	private List<String> calendarList;
	
	/**
	 * コンストラクタ。
	 */
	public MainChartCalendar_r3() {
	}

	/**
	 * 検索用にチャートカレンダーファイルを読み込む。
	 */
	public void initCalendar() {
		readCalendar();
		calendarList = new ArrayList<>();
		calendarList.addAll(calendarSet);
	}

	/**
	 * 前営業日の日付を検索する。
	 * 
	 * @param date 今日の日付。
	 * @return 昨日の日付。存在しない場合はnull。
	 */
	public String searchCalendar(String date) {
		int idx = Collections.binarySearch(calendarList, date);
		if (idx > 0) {
			return calendarList.get(idx - 1);
		}
		return null;
	}

	/**
	 * 翌日の日付を検索する。
	 * 
	 * @param date 今日の日付。
	 * @return 翌日の日付。
	 */
	public String nextDay(String date) {
		Date now = DateUtil.parseString(date);
		if (now != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			String next = DateUtil.toString(cal.getTime());
			return next;
		}
		return null;
	}

	/**
	 * チャートデータからチャートカレンダーを作成する。
	 */
	private void execute() {
		new File(DIR_DBPATH).mkdirs();
		readCalendar();
		readAllChartData(DIR_FILEPATH);
		writeCalendar();
	}

	/**
	 * チャートカレンダーファイルを読み込む。
	 */
	private void readCalendar() {
		calendarSet = new TreeSet<>();
		List<String> lines = FileUtil.readAllLines(TXT_FILEPATH);
		for (String s : lines) {
			if (s.startsWith("#")) {
				continue;
			}
			calendarSet.add(s);
		}
		System.out.println("MainChartCalendar.readCalendar(): calendarSet.size=" + calendarSet.size());
	}

	/**
	 * チャートカレンダーファイルを書き込む。
	 */
	private void writeCalendar() {
		System.out.println("MainChartCalendar.writeCalendar(): calendarSet.size=" + calendarSet.size());
		List<String> lines = new ArrayList<>();
		lines.add("# date");
		lines.addAll(calendarSet);
		FileUtil.writeAllLines(TXT_FILEPATH, lines);
	}

	/**
	 * 複数のチャートデータファイルを読み込む。
	 * 
	 * @param dirpath チャートデータディレクトリパス。
	 */
	private void readAllChartData(String dirpath) {
		File dir = new File(dirpath);
		for (File f : dir.listFiles()) {
			if (!f.getName().endsWith(".csv")) {
				continue;
			}
			if (f.getName().indexOf("週足") >= 0) {
				continue;
			}
			if (f.getName().indexOf("月足") >= 0) {
				continue;
			}
			readChartData(f.getPath());
		}
	}

	/**
	 * チャートデータファイルを読み込む。
	 * 
	 * @param filepath チャートデータファイルパス。
	 */
	private void readChartData(String filepath) {
		List<String> lines = FileUtil.readAllLines(filepath);
		System.out.print("MainChartCalendar.readChartData(): " + filepath + ", lines.size=" + lines.size());
		int cnt = 0;
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
			String date = s.substring(0, 10);
			if (!calendarSet.contains(date)) {
				calendarSet.add(date);
				cnt++;
			}
		}
		System.out.println(",  add " + cnt);
	}

}
