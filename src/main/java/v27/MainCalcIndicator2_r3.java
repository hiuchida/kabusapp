package v27;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import util.FileUtil;
import v27.MainCalcIndicator_r3.ChartInfo;

/**
 * テクニカル指標(ボリンジャーバンド)を計算するクラス。
 */
public class MainCalcIndicator2_r3 implements CalcIndicator_r3 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * チャートデータディレクトリパス。
	 */
	private static final String DIR_CHARTPATH = DIRPATH + "chart/";
	/**
	 * テクニカル指標(ボリンジャーバンド)のstdoutのファイル名。
	 */
	private static final String OUT_FILENAME = "CalcIndicator2.out";

	/**
	 * テクニカル指標(ボリンジャーバンド)のstdoutのファイルパス。
	 */
	private String outFilePath;

	/**
	 * コンストラクタ。
	 * 
	 * @param name ディレクトリ名。
	 */
	public MainCalcIndicator2_r3(String name) {
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
			printBollingerBands(pw, chartList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ボリンジャーバンド(25本)を表示する。
	 * 
	 * @param pw        stdoutファイル。
	 * @param chartList マージしたチャートデータを時系列に並べたリスト。
	 */
	private void printBollingerBands(PrintWriter pw, List<ChartInfo> chartList) {
		int writeCnt = 0;
		final int param1 = 25;
		long sqr = 0;
		int sum = 0;
		int cnt = 0;
		for (int i = 0; i < chartList.size(); i++) {
			ChartInfo ci = chartList.get(i);
			pw.printf("%s,%d,%d", ci.date, ci.closePrice, ci.flag);
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
				pw.printf(",%.2f,%.2f,%.2f,%.2f,%.2f,%.2f", sd, mean - 2 * sd, mean - sd, mean, mean + sd, mean + 2 * sd);
			}
			pw.println();
			writeCnt++;
		}
		System.out.println("MainCalcIndicator2_r3.printBollingerBands(): " + outFilePath + ", writeCnt=" + writeCnt);
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

}
