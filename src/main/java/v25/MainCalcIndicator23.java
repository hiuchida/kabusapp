package v25;

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
public class MainCalcIndicator23 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * マージしたチャートデータファイルパス。
	 */
	private static final String CHART_TXT_FILEPATH = DIRPATH + "ChartData1m.txt";

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
		new MainCalcIndicator23().execute();
	}

	/**
	 * マージしたチャートデータを時系列に並べたリスト。
	 */
	private List<ChartInfo> chartList = new ArrayList<>();

	/**
	 * テクニカル指標(ボリンジャーバンド)を計算する。
	 */
	public void execute() {
		readChartData();
		chartList = fill0555_0559(chartList);
		printBollingerBands();
	}

	/**
	 * ボリンジャーバンド(25本)を表示する。
	 */
	private void printBollingerBands() {
		final int param1 = 25;
		long sqr = 0;
		int sum = 0;
		int cnt = 0;
		for (int i = 0; i < chartList.size(); i++) {
			ChartInfo ci = chartList.get(i);
			System.out.printf("%s,%d,%d", ci.date, ci.closePrice, ci.flag);
			int price = ci.closePrice;
			if (cnt < param1) {
				sqr += price * price;
				sum += price;
				cnt++;
			} else {
				int p_1 = chartList.get(i - param1).closePrice;
				sqr += price * price - p_1 * p_1;
				sum += price - p_1;
			}
			if (cnt == param1) {
				double mean = (double) sum / cnt;
				double variance = (double)sqr / cnt - mean * mean;
				double sd = Math.sqrt(variance);
				System.out.printf(",%.2f,%.2f,%.2f,%.2f,%.2f,%.2f", sd, mean - 2 * sd, mean - sd, mean, mean + sd, mean + 2 * sd);
			}
			System.out.println();
		}
	}
/*
	// 2重ループ
	private void printBollingerBands() {
		final int param1 = 25;
		for (int i = 0; i < chartList.size(); i++) {
			ChartInfo ci = chartList.get(i);
			System.out.printf("%s,%d,%d", ci.date, ci.closePrice, ci.flag);
			if (i >= param1 - 1) {
				long sqr = 0;
				int sum = 0;
				int cnt = 0;
				for (int j = i; j > i - param1; j--) {
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
*/
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
