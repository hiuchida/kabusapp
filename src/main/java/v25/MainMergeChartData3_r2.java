package v25;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import logic.ChartTime1mLogic;
import logic.FileLockLogic;
import util.FileUtil;
import util.StringUtil;

/**
 * 保存した4本値チャートデータと、PUSH APIで受信したチャートデータをマージした4本値を出力するツール。
 */
public class MainMergeChartData3_r2 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 1分足の4本値チャートＤＢファイルパス。
	 */
	private static final String CHART_1M_FILEPATH = DIRPATH + "ChartData1m.db";
	/**
	 * PUSH APIで受信したチャートデータファイルパス。
	 */
	private static final String CHART_CSV_FILEPATH = DIRPATH + "ChartData.csv";
	/**
	 * ファイルロック管理用0バイトのファイルパス。存在しなければ生成される。
	 */
	private static final String LOCK_FILEPATH = DIRPATH + "ChartData.lock";
	/**
	 * マージしたチャートデータファイルパス。
	 */
	private static final String CHART_TXT_FILEPATH = DIRPATH + "ChartData1m_r2.txt";

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
	 * 保存した4本値チャートデータの終値と、PUSH APIで受信したチャートデータをマージする。
	 */
	public static void main(String[] args) {
		new MainMergeChartData3_r2().execute();
	}

	/**
	 * マージしたチャートデータを時系列に並べたマップ。
	 */
	private Map<String, ChartInfo> chartMap = new TreeMap<>();

	/**
	 * チャートデータロックを管理する。
	 */
	private FileLockLogic fileLockLogic;

	/**
	 * コンストラクタ。
	 */
	public MainMergeChartData3_r2() {
		this.fileLockLogic = new FileLockLogic(LOCK_FILEPATH);
	}

	/**
	 * 保存した4本値チャートデータの終値と、PUSH APIで受信したチャートデータをマージする。
	 */
	public void execute() {
		read5mChartData();
		readCsvChartData();
		writeChartMap();
	}

	/**
	 * 1分足の4本値チャートＤＢファイルから終値を読み込む。
	 */
	private void read5mChartData() {
		List<String> lines = FileUtil.readAllLines(CHART_1M_FILEPATH);
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
		System.out.println("MainMergeChartData3_r2.read5mChartData(): readCnt=" + readCnt);
	}

	/**
	 * PUSH APIで受信したチャートデータファイルから現値を読み込む。
	 */
	private void readCsvChartData() {
		fileLockLogic.lockFile();
		List<String> lines;
		try {
			lines = FileUtil.readAllLines(CHART_CSV_FILEPATH);
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
		System.out.println("MainMergeChartData3_r2.readCsvChartData(): readCnt=" + readCnt);
	}

	/**
	 * マージしたチャートデータファイルを書き込む。
	 */
	private void writeChartMap() {
		List<String> lines = new ArrayList<>();
		lines.add(ChartInfo.toHeaderString());
		System.out.println("MainMergeChartData3_r2.writeChartMap(): chartMap.size=" + chartMap.size());
		for (String key : chartMap.keySet()) {
			ChartInfo ci = chartMap.get(key);
			lines.add(ci.toLineString());
		}
		FileUtil.writeAllLines(CHART_TXT_FILEPATH, lines);
	}

}
