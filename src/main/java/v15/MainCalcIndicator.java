package v15;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import util.FileUtil;
import util.StringUtil;

/**
 * テクニカル指標(SMA6,SMA12,SMA24)を計算するクラス。
 */
public class MainCalcIndicator {
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
	 * テクニカル指標(SMA6,SMA12,SMA24)を計算する。
	 */
	public static void main(String[] args) {
		new MainCalcIndicator().execute();
	}

	/**
	 * マージしたチャートデータを時系列に並べたリスト。
	 */
	private List<ChartInfo> chartList = new ArrayList<>();

	/**
	 * テクニカル指標(SMA6,SMA12,SMA24)を計算する。
	 */
	public void execute() {
		readChartData();
		chartList = fill0555(chartList);
		printSma();
	}

	/**
	 * SMA6,SMA12,SMA24を表示する。
	 */
	private void printSma() {
		int sum6 = 0;
		int cnt6 = 0;
		int sum12 = 0;
		int cnt12 = 0;
		int sum24 = 0;
		int cnt24 = 0;
		for (int i = 0; i < chartList.size(); i++) {
			ChartInfo ci = chartList.get(i);
			if (cnt6 < 6) {
				sum6 += ci.closePrice;
				cnt6++;
			} else {
				sum6 += ci.closePrice - chartList.get(i - 6).closePrice;
			}
			if (cnt12 < 12) {
				sum12 += ci.closePrice;
				cnt12++;
			} else {
				sum12 += ci.closePrice - chartList.get(i - 12).closePrice;
			}
			if (cnt24 < 24) {
				sum24 += ci.closePrice;
				cnt24++;
			} else {
				sum24 += ci.closePrice - chartList.get(i - 24).closePrice;
			}
			System.out.printf("%s,%d,%d", ci.date, ci.closePrice, ci.flag);
			if (cnt6 == 6) {
				System.out.printf(",%.2f", ((double) sum6 / cnt6));
				if (cnt12 == 12) {
					System.out.printf(",%.2f", ((double) sum12 / cnt12));
					if (cnt24 == 24) {
						System.out.printf(",%.2f", ((double) sum24 / cnt24));
					}
				}
			}
			System.out.println();
		}
	}
/*
	// 2重ループ
	private void printSma() {
		for (int i = 0; i < chartList.size(); i++) {
			ChartInfo ci = chartList.get(i);
			System.out.printf("%s,%d,%d", ci.date, ci.closePrice, ci.flag);
			if (i >= 5) {
				int sum = 0;
				int cnt = 0;
				for (int j = i; j > i - 6; j--) {
					ChartInfo ci2 = chartList.get(j);
					sum += ci2.closePrice;
					cnt++;
				}
				System.out.printf(",%.2f", ((double) sum / cnt));
			}
			if (i >= 11) {
				int sum = 0;
				int cnt = 0;
				for (int j = i; j > i - 12; j--) {
					ChartInfo ci2 = chartList.get(j);
					sum += ci2.closePrice;
					cnt++;
				}
				System.out.printf(",%.2f", ((double) sum / cnt));
			}
			if (i >= 23) {
				int sum = 0;
				int cnt = 0;
				for (int j = i; j > i - 24; j--) {
					ChartInfo ci2 = chartList.get(j);
					sum += ci2.closePrice;
					cnt++;
				}
				System.out.printf(",%.2f", ((double) sum / cnt));
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
