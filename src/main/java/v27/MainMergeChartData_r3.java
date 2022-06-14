package v27;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import logic.ChartTime1mLogic;
import logic.FileLockLogic;
import util.FileUtil;
import util.StringUtil;

/**
 * 保存した4本値チャートデータと、PUSH APIで受信したチャートデータをマージした4本値を出力するツール。
 */
public class MainMergeChartData_r3 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * チャートＤＢディレクトリパス。
	 */
	private static final String DIR_DBPATH = DIRPATH + "db/";
	/**
	 * 1分足の4本値チャートＤＢファイル名。
	 */
	private static final String DB_FILENAME = "ChartData1m.db";
	/**
	 * チャートデータディレクトリパス。
	 */
	private static final String DIR_CHARTPATH = DIRPATH + "chart/";
	/**
	 * PUSH APIで受信したチャートデータファイル名。
	 */
	private static final String CHART_CSV_FILENAME = "ChartData.csv";
	/**
	 * ファイルロック管理用0バイトのファイル名。存在しなければ生成される。
	 */
	private static final String LOCK_FILENAME = "ChartData.lock";
	/**
	 * マージしたチャートデータファイル名。
	 */
	private static final String CHART_TXT_FILENAME = "ChartData1m_r3.txt";

	/**
	 * チャート情報クラス。
	 */
	public static class ChartInfo {
		/**
		 * 4本値チャートＤＢファイルのカラム数。
		 */
		public static final int MAX_COLS = 6;
		/**
		 * 時間足の場合は日時。日足の場合は日付。
		 */
		public String date;
		/**
		 * 始値。
		 */
		public int openPrice;
		/**
		 * 高値。
		 */
		public int highPrice;
		/**
		 * 安値。
		 */
		public int lowPrice;
		/**
		 * 終値。
		 */
		public int closePrice;
		/**
		 * データフラグ。0:データなし、1:4本値のデータ、2:PUSH APIで取得したデータ、3:コピーされたデータ。
		 */
		public int flag;

		/**
		 * コンストラクタ（データなし）。
		 * 
		 * @param date 時間足の場合は日時。日足の場合は日付。
		 */
		public ChartInfo(String date) {
			this.date = date;
			this.openPrice = 0;
			this.highPrice = 0;
			this.lowPrice = 0;
			this.closePrice = 0;
			this.flag = 0;
		}

		/**
		 * コンストラクタ（4本値チャートＤＢファイル）。
		 * 
		 * @param cols 4本値チャートＤＢファイルの1レコードの全てのカラム文字列。
		 */
		public ChartInfo(String[] cols) {
			int i = 0;
			this.date = cols[i++];
			this.openPrice = StringUtil.parseInt(cols[i++]);
			this.highPrice = StringUtil.parseInt(cols[i++]);
			this.lowPrice = StringUtil.parseInt(cols[i++]);
			this.closePrice = StringUtil.parseInt(cols[i++]);
			i++;
			this.flag = 1;
		}

		/**
		 * コンストラクタ（PUSH APIで受信したチャートデータ）。
		 * 
		 * @param date  時間足の場合は日時。日足の場合は日付。
		 * @param price 現値。
		 */
		public ChartInfo(String date, int price) {
			this.date = date;
			this.openPrice = price;
			this.highPrice = price;
			this.lowPrice = price;
			this.closePrice = price;
			this.flag = 2;
		}

		/**
		 * マージしたチャートデータファイルのヘッダ文字列を生成する。
		 * 
		 * @return ヘッダ文字列。
		 */
		public static String toHeaderString() {
			String[] sa = new String[6];
			int i = 0;
			sa[i++] = "date             ";
			sa[i++] = "open";
			sa[i++] = "high";
			sa[i++] = "low";
			sa[i++] = "close";
			sa[i++] = "flag";
			String val = "# " + StringUtil.joinTab(sa);
			return val;
		}

		/**
		 * インスタンスの主キー(date)を取得する。
		 * 
		 * @return 主キー。
		 */
		public String getKey() {
			return date;
		}

		/**
		 * マージしたチャートデータファイルのレコード文字列を生成する。
		 * 
		 * @return レコード文字列。
		 */
		public String toLineString() {
			String[] sa = new String[6];
			int i = 0;
			sa[i++] = date;
			sa[i++] = "" + openPrice;
			sa[i++] = "" + highPrice;
			sa[i++] = "" + lowPrice;
			sa[i++] = "" + closePrice;
			sa[i++] = "" + flag;
			String val = StringUtil.joinTab(sa);
			return val;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{date=").append(date);
			sb.append(", close=").append(closePrice);
			sb.append(", flag=").append(flag);
			sb.append("}");
			return sb.toString();
		}
	}

	/**
	 * ディレクトリ名のリスト。
	 */
	private static Set<String> nameSet;

	/**
	 * 保存した4本値チャートデータの終値と、PUSH APIで受信したチャートデータをマージする。
	 */
	public static void main(String[] args) {
		listChartFiles(DIR_CHARTPATH);
		System.out.println(nameSet);
		for (String name : nameSet) {
			new MainMergeChartData_r3(name).execute();
		}
	}

	/**
	 * チャートデータディレクトリ名からディレクトリ名リストを作成する。
	 * 
	 * @param dirpath チャートデータディレクトリパス。
	 */
	private static void listChartFiles(String dirpath) {
		nameSet = new TreeSet<>();
		File dir = new File(dirpath);
		for (File f : dir.listFiles()) {
			if (f.isFile()) {
				continue;
			}
			String name = f.getName();
			nameSet.add(name);
		}
	}

	/**
	 * チャートＤＢファイルパス。
	 */
	private String dbFilePath;
	/**
	 * マージしたチャートデータを時系列に並べたマップ。
	 */
	private Map<String, ChartInfo> chartMap = new TreeMap<>();
	/**
	 * チャートデータファイルパス。
	 */
	private String csvFilePath;
	/**
	 * マージしたチャートデータファイルパス。
	 */
	private String txtFilePath;
	/**
	 * チャートデータロックを管理する。
	 */
	private FileLockLogic fileLockLogic;

	/**
	 * コンストラクタ。
	 * 
	 * @param name ディレクトリ名。
	 */
	public MainMergeChartData_r3(String name) {
		String code = StringUtil.parseString(name, "_");
		String dirDbPath = DIR_DBPATH + code;
		this.dbFilePath = dirDbPath + "/" + DB_FILENAME;
		String dirChartPath = DIR_CHARTPATH + name;
		this.csvFilePath = dirChartPath + "/" + CHART_CSV_FILENAME;
		this.txtFilePath = dirChartPath + "/" + CHART_TXT_FILENAME;
		this.fileLockLogic = new FileLockLogic(dirChartPath + "/" + LOCK_FILENAME);
	}

	/**
	 * 保存した4本値チャートデータの終値と、PUSH APIで受信したチャートデータをマージする。
	 */
	public void execute() {
		read1mChartData();
		readCsvChartData();
		writeChartMap();
	}

	/**
	 * 1分足の4本値チャートＤＢファイルから終値を読み込む。
	 */
	private void read1mChartData() {
		List<String> lines = FileUtil.readAllLines(dbFilePath);
		int readCnt = 0;
		for (String s : lines) {
			if (s.startsWith("#")) {
				continue;
			}
			String[] cols = StringUtil.splitComma(s);
			if (cols.length != ChartInfo.MAX_COLS) {
				System.out.println("Warning: SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			ChartInfo ci = new ChartInfo(cols);
			String key = ci.getKey();
			chartMap.put(key, ci);
			readCnt++;
		}
		System.out.println("MainMergeChartData_r3.read1mChartData(): " + dbFilePath + ", readCnt=" + readCnt);
	}

	/**
	 * PUSH APIで受信したチャートデータファイルから現値を読み込む。
	 */
	private void readCsvChartData() {
		fileLockLogic.lockFile();
		List<String> lines;
		try {
			lines = FileUtil.readAllLines(csvFilePath);
		} finally {
			fileLockLogic.unlockFile();
		}
		int readCnt = 0;
		for (String s : lines) {
			if (s.startsWith("#")) {
				continue;
			}
			String[] cols = StringUtil.splitComma(s);
			if (cols.length < 2) {
				System.out.println("Warning: SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			String datetime = cols[0];
			int price = (int) StringUtil.parseDouble(cols[1]);
			String date = datetime.substring(0, 10).replaceAll("-", "/");
			String time = datetime.substring(11);
			String startTime = ChartTime1mLogic.search(time);
			if (startTime == null) {
				continue;
			}
			String key = date + " " + startTime;
			ChartInfo ci = chartMap.get(key);
			if (ci == null) {
				ci = new ChartInfo(key, price);
				chartMap.put(key, ci);
				readCnt++;
			} else {
				// 4本値は確定値なので上書きしない
				if (ci.flag != 1) {
					// 高値が更新された場合のみ上書きする
					if (ci.highPrice < price) {
						ci.highPrice = price;
					}
					// 安値が更新された場合のみ上書きする
					if (ci.lowPrice > price) {
						ci.lowPrice = price;
					}
					// PUSH APIのデータは、時系列にソートされている前提で、常に新しい現値で上書きする
					ci.closePrice = price;
					readCnt++;
				}
			}
		}
		System.out.println("MainMergeChartData_r3.readCsvChartData(): " + csvFilePath + ", readCnt=" + readCnt);
	}

	/**
	 * マージしたチャートデータファイルを書き込む。
	 */
	private void writeChartMap() {
		List<String> lines = new ArrayList<>();
		lines.add(ChartInfo.toHeaderString());
		System.out.println("MainMergeChartData_r3.writeChartMap(): " + txtFilePath + ", chartMap.size=" + chartMap.size());
		for (String key : chartMap.keySet()) {
			ChartInfo ci = chartMap.get(key);
			lines.add(ci.toLineString());
		}
		FileUtil.writeAllLines(txtFilePath, lines);
	}

}
