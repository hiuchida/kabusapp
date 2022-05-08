package v15;

import java.util.ArrayList;
import java.util.List;

import util.FileUtil;
import util.StringUtil;

/**
 * テクニカル指標を計算するクラス。
 */
public class MainCalcIndicator {
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
		 * データフラグ。0:データなし、1:4本値のデータ、2:PUSH APIで取得したデータ。
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
	 * テクニカル指標を計算する。
	 */
	public static void main(String[] args) {
		new MainCalcIndicator().execute();
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
			if (cnt6 == 6) {
				System.out.printf("%s,%.2f", ci.date, ((double) sum6 / cnt6));
				if (cnt12 == 12) {
					System.out.printf(",%.2f", ((double) sum12 / cnt12));
					if (cnt24 == 24) {
						System.out.printf(",%.2f", ((double) sum24 / cnt24));
					}
				}
				System.out.println();
			}
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

}