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
 * テクニカル指標を計算するクラス。
 */
public class MainCalcIndicator_r3 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * チャートデータディレクトリパス。
	 */
	private static final String DIR_CHARTPATH = DIRPATH + "chart/";
	/**
	 * マージしたチャートデータファイル名。
	 */
	private static final String CHART_TXT_FILENAME = "ChartData1m_r3.txt";

	/**
	 * チャート情報クラス。
	 */
	public static class ChartInfo {
		/**
		 * マージしたチャートデータファイルのカラム数。
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
		 * コンストラクタ（マージしたチャートデータファイル）。
		 * 
		 * @param cols マージしたチャートデータファイルの1レコードの全てのカラム文字列。
		 */
		public ChartInfo(String[] cols) {
			int i = 0;
			this.date = cols[i++];
			this.openPrice = StringUtil.parseInt(cols[i++]);
			this.highPrice = StringUtil.parseInt(cols[i++]);
			this.lowPrice = StringUtil.parseInt(cols[i++]);
			this.closePrice = StringUtil.parseInt(cols[i++]);
			this.flag = StringUtil.parseInt(cols[i++]);
		}

		/**
		 * コンストラクタ（別のチャートデータをコピーする）。
		 * 
		 * @param date  時間足の場合は日時。日足の場合は日付。
		 * @param price コピーする値。
		 */
		public ChartInfo(String date, int price) {
			this.date = date;
			this.openPrice = price;
			this.highPrice = price;
			this.lowPrice = price;
			this.closePrice = price;
			this.flag = 3;
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
	 * テクニカル指標を計算する。
	 */
	public static void main(String[] args) {
		listChartFiles(DIR_CHARTPATH);
		System.out.println(nameSet);
		for (String name : nameSet) {
			new MainCalcIndicator_r3(name).execute();
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
	 * マージしたチャートデータファイルパス。
	 */
	private String txtFilePath;
	/**
	 * マージしたチャートデータを時系列に並べたリスト。
	 */
	private List<ChartInfo> chartList = new ArrayList<>();
	/**
	 * テクニカル指標を計算するクラス。
	 */
	private List<CalcIndicator_r3> calcList = new ArrayList<>();

	/**
	 * コンストラクタ。
	 * 
	 * @param name ディレクトリ名。
	 */
	public MainCalcIndicator_r3(String name) {
		String dirChartPath = DIR_CHARTPATH + name;
		this.txtFilePath = dirChartPath + "/" + CHART_TXT_FILENAME;
		calcList.add(new MainCalcIndicator1_r3(name));
		calcList.add(new MainCalcIndicator2_r3(name));
		calcList.add(new MainCalcIndicator3_r3(name));
		calcList.add(new MainCalcIndicator4_r3(name));
	}

	/**
	 * テクニカル指標を計算する。
	 */
	public void execute() {
		readChartData();
		chartList = fill0555_0559(chartList);
		for (CalcIndicator_r3 ci : calcList) {
			ci.execute(chartList);
		}
	}

	/**
	 * マージしたチャートデータを読み込む。
	 */
	private void readChartData() {
		List<String> lines = FileUtil.readAllLines(txtFilePath);
		for (String s : lines) {
			if (s.startsWith("#")) {
				continue;
			}
			String[] cols = StringUtil.splitTab(s);
			if (cols.length != ChartInfo.MAX_COLS) {
				System.out.println("Warning: SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			ChartInfo ci = new ChartInfo(cols);
			chartList.add(ci);
		}
		System.out.println("MainCalcIndicator_r3.readChartData(): " + txtFilePath + ", chartList.size=" + chartList.size());
	}

	/**
	 * 05:55-05:59が抜けているチャートデータをflag=3で埋める。
	 * 
	 * @param chartList チャートデータのリスト。
	 * @return 更新されたチャートデータのリスト。
	 */
	private List<ChartInfo> fill0555_0559(List<ChartInfo> chartList) {
		Map<String, ChartInfo> map = new TreeMap<>();
		Set<String> dateSet = new TreeSet<>();
		for (ChartInfo ci : chartList) {
			String key = ci.getKey();
			map.put(key, ci);
			String date = ci.date.substring(0, 10);
			dateSet.add(date);
		}
		for (String date : dateSet) {
			String date0554 = date + " 05:54:00";
			ChartInfo ci0554 = map.get(date0554);
			if (ci0554 != null) {
				for (int i = 55; i <= 59; i++) {
					String date0555_0559 = String.format("%s 05:%d:00", date, i);
					ChartInfo ci0555_0559 = map.get(date0555_0559);
					if (ci0555_0559 == null) {
						ci0555_0559 = new ChartInfo(date0555_0559, ci0554.closePrice);
						map.put(date0555_0559, ci0555_0559);
					}
				}
			}
		}
		List<ChartInfo> list = new ArrayList<>();
		for (String key : map.keySet()) {
			ChartInfo ci = map.get(key);
			list.add(ci);
		}
		return list;
	}

}
