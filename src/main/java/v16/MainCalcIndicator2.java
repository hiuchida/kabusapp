package v16;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import util.FileUtil;
import util.StringUtil;

/**
 * テクニカル指標(ボリンジャーバンド)を計算するクラス。
 */
public class MainCalcIndicator2 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * マージしたチャートデータファイルパス。
	 */
	private static final String CHART_TXT_FILEPATH = DIRPATH + "ChartData.txt";

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
	 * テクニカル指標(ボリンジャーバンド)を計算する。
	 */
	public static void main(String[] args) {
		new MainCalcIndicator2().execute();
	}

	/**
	 * マージしたチャートデータを時系列に並べたリスト。
	 */
	private List<ChartInfo> chartList = new ArrayList<>();

	/**
	 * 保存した4本値チャートデータの終値と、PUSH APIで受信したチャートデータをマージする。
	 */
	public void execute() {
		readChartData();
		chartList = fill0555(chartList);
		printBollingerBands();
	}

	/**
	 * ボリンジャーバンド(12本)を表示する。
	 */
	private void printBollingerBands() {
		for (int i = 0; i < chartList.size(); i++) {
			ChartInfo ci = chartList.get(i);
			long sqr = 0;
			int sum = 0;
			int cnt = 0;
			System.out.printf("%s,%d,%d", ci.date, ci.closePrice, ci.flag);
			if (i >= 11) {
				for (int j = i; j > i - 12; j--) {
					ChartInfo ci2 = chartList.get(j);
					sqr += ci2.closePrice * ci2.closePrice;
					sum += ci2.closePrice;
					cnt++;
				}
				double mean = (double) sum / cnt;
				double variance = (double)sqr / cnt - mean * mean;
				double sd = Math.sqrt(variance);
				System.out.printf(",%.2f,%.2f,%.2f,%.2f,%.2f,%.2f", sd, mean - 2 * sd, mean - sd, mean, mean + sd, mean + 2 * sd);
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
