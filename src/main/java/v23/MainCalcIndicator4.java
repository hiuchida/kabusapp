package v23;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import util.FileUtil;
import util.StringUtil;

/**
 * テクニカル指標(HV20)を計算するクラス。
 */
public class MainCalcIndicator4 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * マージしたチャートデータファイルパス。
	 */
	private static final String CHART_TXT_FILEPATH = DIRPATH + "ChartData5m.txt";

	/**
	 * チャート情報クラス。
	 */
	public static class ChartInfo {
		/**
		 * マージしたチャートデータファイルのカラム数。
		 */
		public static final int MAX_COLS = 3;
		/**
		 * 時間足の場合は日時。日足の場合は日付。
		 */
		public String date;
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
			this.closePrice = price;
			this.flag = 3;
		}

		/**
		 * マージしたチャートデータファイルのヘッダ文字列を生成する。
		 * 
		 * @return ヘッダ文字列。
		 */
		public static String toHeaderString() {
			String[] sa = new String[3];
			int i = 0;
			sa[i++] = "date             ";
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
			String[] sa = new String[3];
			int i = 0;
			sa[i++] = date;
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
	 * テクニカル指標(HV20)を計算する。
	 */
	public static void main(String[] args) {
		new MainCalcIndicator4().execute();
	}

	/**
	 * マージしたチャートデータを時系列に並べたリスト。
	 */
	private List<ChartInfo> chartList = new ArrayList<>();

	/**
	 * テクニカル指標(HV20)を計算する。
	 */
	public void execute() {
		readChartData();
		chartList = fill0555(chartList);
		printHv();
	}

	/**
	 * HV20を表示する。
	 */
	private void printHv() {
		if (chartList.size() < 2) {
			return;
		}
		// 2件目以降の前日比の自然対数を求める
		double[] ratio = new double[chartList.size()];
		for (int i = 1; i < chartList.size(); i++) {
			int p1 = chartList.get(i).closePrice;
			int p0 = chartList.get(i - 1).closePrice;
			double r = (double) p1 / (double) p0;
			ratio[i] = Math.log(r);
		}
		// 標準偏差を求める
		double sqr = 0;
		double sum = 0;
		int cnt = 0;
		for (int i = 1; i < ratio.length; i++) {
			ChartInfo ci = chartList.get(i);
			System.out.printf("%s,%d,%d", ci.date, ci.closePrice, ci.flag);
			double r = ratio[i];
			if (cnt < 20) {
				sqr += r * r;
				sum += r;
				cnt++;
			} else {
				double r_20 = ratio[i - 20];
				sqr += r * r - r_20 * r_20;
				sum += r - r_20;
			}
			if (cnt == 20) {
				double mean = (double) sum / cnt;
				double variance = (double)sqr / cnt - mean * mean;
				double sd = Math.sqrt(variance);
				double hv = sd * Math.sqrt(250 * 20 * 60 / 5);
				System.out.printf(",%.5f,%.5f", sd, hv);
			}
			System.out.println();
		}
	}

	/**
	 * マージしたチャートデータを読み込む。
	 */
	private void readChartData() {
		List<String> lines = FileUtil.readAllLines(CHART_TXT_FILEPATH);
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
		System.out.println("MainCalcIndicator.readChartData(): chartList.size=" + chartList.size());
	}

	/**
	 * 05:55が抜けているチャートデータをflag=3で埋める。
	 * 
	 * @param chartList チャートデータのリスト。
	 * @return 更新されたチャートデータのリスト。
	 */
	private List<ChartInfo> fill0555(List<ChartInfo> chartList) {
		Map<String, ChartInfo> map = new TreeMap<>();
		Set<String> dateSet = new TreeSet<>();
		for (ChartInfo ci : chartList) {
			String key = ci.getKey();
			map.put(key, ci);
			String date = ci.date.substring(0, 10);
			dateSet.add(date);
		}
		for (String date : dateSet) {
			String date0550 = date + " 05:50:00";
			String date0555 = date + " 05:55:00";
			ChartInfo ci0550 = map.get(date0550);
			ChartInfo ci0555 = map.get(date0555);
			if (ci0550 != null && ci0555 == null) {
				ci0555 = new ChartInfo(date0555, ci0550.closePrice);
				map.put(date0555, ci0555);
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
