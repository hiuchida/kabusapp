package v17;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import logic.FileLockLogic;
import util.DateTimeUtil;
import util.FileUtil;
import util.StringUtil;

/**
 * チャートデータ情報を管理する。
 */
public class ChartDataLogic {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/chart/";
	/**
	 * PUSH APIで受信したチャートデータファイルパス。
	 */
	private static final String CSV_FILENAME = "ChartData.csv";
	/**
	 * ファイルロック管理用0バイトのファイルパス。存在しなければ生成される。
	 */
	private static final String LOCK_FILENAME = "ChartData.lock";

	/**
	 * チャートデータ情報。
	 */
	public static class ChartInfo {
		/**
		 * チャートデータファイルのカラム数。
		 */
		public static final int MAX_COLS = 3;
		/**
		 * 日時。
		 */
		public String date;
		/**
		 * 現値。
		 */
		public int price;
		/**
		 * 売買高。
		 */
		public int volume;
		
		/**
		 * コンストラクタ（チャートデータレコード）。
		 * 
		 * @param cols チャートデータファイルの1レコードの全てのカラム文字列。
		 */
		public ChartInfo(String[] cols) {
			int i = 0;
			this.date = cols[i++].replaceAll("-", "/");
			this.price = (int) StringUtil.parseDouble(cols[i++]);
			this.volume = (int) StringUtil.parseDouble(cols[i++]);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{date=").append(date);
			sb.append(", price=").append(price);
			sb.append(", volume=").append(volume);
			sb.append("}");
			return sb.toString();
		}
	}

	/**
	 * チャートファイル情報。
	 */
	public static class ChartFile {
		/**
		 * 銘柄コード。
		 */
		public String code;
		/**
		 * 保存先ディレクトリ名。
		 */
		public String dirname;
		/**
		 * チャートデータ情報のリスト。
		 */
		public List<ChartInfo> chartList;
		/**
		 * チャート検索履歴のマップ。
		 */
		public Map<String, int[]> searchMap;
		/**
		 * チャートデータロックを管理する。
		 */
		public FileLockLogic fileLockLogic;

		/**
		 * コンストラクタ。
		 * 
		 * @param code    銘柄コード。
		 * @param dirname 保存先ディレクトリ名。
		 */
		public ChartFile(String code, String dirname) {
			this.code = code;
			this.dirname = dirname;
			String filepath = DIRPATH + dirname + "/" + LOCK_FILENAME;
			this.fileLockLogic = new FileLockLogic(filepath);
			readCsvChartData();
		}

		/**
		 * 指定した日時以降の高値と安値を検索する。検索した結果はキャッシュする。
		 * 
		 * @param startDate 指定した日時。
		 * @return 高値と安値。古いデータしか存在しない場合、高値=安値=0を返す。
		 */
		public int[] searchHighLow(long startDate) {
			String date = DateTimeUtil.toString(startDate);
			date = date.substring(0, 19);
			int[] vals = searchMap.get(date);
			if (vals != null) {
				return vals;
			}
			vals = new int[2];
			int idx = searchIdx(date);
			if (idx < 0) {
				if (chartList.size() > 0) {
					System.out.println("ChartDataLogic.searchHighLow(): idx=" + idx + ", last_ci=" + chartList.get(chartList.size() - 1));
				} else {
					System.out.println("ChartDataLogic.searchHighLow(): idx=" + idx + ", last_ci=null");
				}
				vals[0] = 0;
				vals[1] = 0;
				searchMap.put(date, vals);
				return vals;
			}
			System.out.println("ChartDataLogic.searchHighLow(): idx=" + idx + ", ci=" + chartList.get(idx));
			vals[0] = 0;
			vals[1] = Integer.MAX_VALUE;
			for (int i = idx; i < chartList.size(); i++) {
				int price = chartList.get(i).price;
				if (vals[0] < price) {
					vals[0] = price;
				}
				if (price < vals[1]) {
					vals[1] = price;
				}
			}
			System.out.println("ChartDataLogic.searchHighLow(): vals[0]=" + vals[0] + ", vals[1]=" + vals[1]);
			searchMap.put(date, vals);
			return vals;
		}

		/**
		 * 逐次検索する。
		 * 
		 * @param startDate 指定した日時。
		 * @return
		 */
		private int searchIdx(String startDate) {
			System.out.println("ChartDataLogic.searchIdx(): startDate=" + startDate);
			for (int i = 0; i < chartList.size(); i++) {
				ChartInfo ci = chartList.get(i);
				if (startDate.compareTo(ci.date) <= 0) {
					return i;
				}
			}
			return -1;
		}

		/**
		 * チャートファイルを読み込む。
		 */
		private void readCsvChartData() {
			String filepath = DIRPATH + dirname + "/" + CSV_FILENAME;
			chartList = new ArrayList<>();
			searchMap = new TreeMap<>();
			fileLockLogic.lockFile();
			try {
				List<String> lines = FileUtil.readAllLines(filepath);
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
					chartList.add(ci);
				}
				System.out.println("ChartDataLogic.readCsvChartData(): dirname=" + dirname + ", chartList.size=" + chartList.size());
			} finally {
				fileLockLogic.unlockFile();
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{code=").append(code);
			sb.append(", dirname=").append(dirname);
			sb.append(", chartList.size=").append(chartList.size());
			sb.append("}");
			return sb.toString();
		}
	}

	/**
	 * チャートファイル情報のマップ。
	 */
	private Map<String, ChartFile> chartFileMap;

	/**
	 * コンストラクタ。
	 */
	public ChartDataLogic() {
		this.chartFileMap = new TreeMap<>();
	}

	/**
	 * 指定した日時以降の高値と安値を検索する。検索した結果はキャッシュする。
	 * 
	 * @param code      銘柄コード。
	 * @param startDate 指定した日時。
	 * @return 高値と安値。古いデータしか存在しない場合、高値=安値=0を返す。
	 */
	public int[] searchHighLow(String code, long startDate) {
		ChartFile chartFile = chartFileMap.get(code);
		if (chartFile == null) {
			String dirname = searchDirname(code);
			if (dirname == null) {
				System.out.println("ChartDataLogic.searchHighLow(): not exist code=" + code);
				int[] vals = new int[2];
				vals[0] = 0;
				vals[1] = 0;
				return vals;
			}
			chartFile = new ChartFile(code, dirname);
			chartFileMap.put(code, chartFile);
		}
		return chartFile.searchHighLow(startDate);
	}

	/**
	 * 指定した銘柄コードの保存先ディレクトリ名を検索する。
	 * 
	 * @param code 銘柄コード。
	 * @return 保存先ディレクトリ名を返す。
	 */
	private String searchDirname(String code) {
		File dir = new File(DIRPATH);
		for (File f : dir.listFiles()) {
			if (f.isFile()) {
				continue;
			}
			String dirname = f.getName();
			String prefix = StringUtil.parseString(dirname, "_");
			if (prefix.equals(code)) {
				return dirname;
			}
		}
		return null;
	}

}
