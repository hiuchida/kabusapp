package v27;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import util.FileUtil;
import v27.MainCalcIndicator_r3.ChartInfo;

/**
 * テクニカル指標(SMA5,SMA25,SMA75)を計算するクラス。
 */
public class MainCalcIndicator1_r3 implements CalcIndicator_r3 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * チャートデータディレクトリパス。
	 */
	private static final String DIR_CHARTPATH = DIRPATH + "chart/";
	/**
	 * テクニカル指標(SMA5,SMA25,SMA75)のstdoutのファイル名。
	 */
	private static final String OUT_FILENAME = "CalcIndicator1.out";

	/**
	 * テクニカル指標(SMA5,SMA25,SMA75)のstdoutのファイルパス。
	 */
	private String outFilePath;

	/**
	 * コンストラクタ。
	 * 
	 * @param name ディレクトリ名。
	 */
	public MainCalcIndicator1_r3(String name) {
		String dirChartPath = DIR_CHARTPATH + name;
		this.outFilePath = dirChartPath + "/" + OUT_FILENAME;
	}

	/**
	 * テクニカル指標を計算する。
	 * 
	 * @param chartList マージしたチャートデータを時系列に並べたリスト。
	 */
	public void execute(List<ChartInfo> chartList) {
		try (PrintWriter pw = FileUtil.writer(outFilePath, FileUtil.UTF8)) {
			printSma(pw, chartList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * SMA5,SMA25,SMA75を表示する。
	 * 
	 * @param pw        stdoutファイル。
	 * @param chartList マージしたチャートデータを時系列に並べたリスト。
	 */
	private void printSma(PrintWriter pw, List<ChartInfo> chartList) {
		int writeCnt = 0;
		final int param1 = 5;
		final int param2 = 25;
		final int param3 = 75;
		int sum1 = 0;
		int cnt1 = 0;
		int sum2 = 0;
		int cnt2 = 0;
		int sum3 = 0;
		int cnt3 = 0;
		for (int i = 0; i < chartList.size(); i++) {
			ChartInfo ci = chartList.get(i);
			if (cnt1 < param1) {
				sum1 += ci.closePrice;
				cnt1++;
			} else {
				sum1 += ci.closePrice - chartList.get(i - param1).closePrice;
			}
			if (cnt2 < param2) {
				sum2 += ci.closePrice;
				cnt2++;
			} else {
				sum2 += ci.closePrice - chartList.get(i - param2).closePrice;
			}
			if (cnt3 < param3) {
				sum3 += ci.closePrice;
				cnt3++;
			} else {
				sum3 += ci.closePrice - chartList.get(i - param3).closePrice;
			}
			pw.printf("%s,%d,%d", ci.date, ci.closePrice, ci.flag);
			if (cnt1 == param1) {
				pw.printf(",%.2f", ((double) sum1 / cnt1));
				if (cnt2 == param2) {
					pw.printf(",%.2f", ((double) sum2 / cnt2));
					if (cnt3 == param3) {
						pw.printf(",%.2f", ((double) sum3 / cnt3));
					}
				}
			}
			pw.println();
			writeCnt++;
		}
		System.out.println("MainCalcIndicator1_r3.printSma(): " + outFilePath + ", writeCnt=" + writeCnt);
	}
/*
	// 2重ループ
	private void printSma() {
		final int param1 = 5;
		final int param2 = 25;
		final int param3 = 75;
		for (int i = 0; i < chartList.size(); i++) {
			ChartInfo ci = chartList.get(i);
			System.out.printf("%s,%d,%d", ci.date, ci.closePrice, ci.flag);
			if (i >= param1 - 1) {
				int sum = 0;
				int cnt = 0;
				for (int j = i; j > i - param1; j--) {
					ChartInfo ci2 = chartList.get(j);
					sum += ci2.closePrice;
					cnt++;
				}
				System.out.printf(",%.2f", ((double) sum / cnt));
			}
			if (i >= param2 - 1) {
				int sum = 0;
				int cnt = 0;
				for (int j = i; j > i - param2; j--) {
					ChartInfo ci2 = chartList.get(j);
					sum += ci2.closePrice;
					cnt++;
				}
				System.out.printf(",%.2f", ((double) sum / cnt));
			}
			if (i >= param3 - 1) {
				int sum = 0;
				int cnt = 0;
				for (int j = i; j > i - param3; j--) {
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

}
