package v27;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import util.FileUtil;
import v27.MainCalcIndicator_r3.ChartInfo;

/**
 * テクニカル指標(MACD(5,20,9))を計算するクラス。
 */
public class MainCalcIndicator3_r3 implements CalcIndicator_r3 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * チャートデータディレクトリパス。
	 */
	private static final String DIR_CHARTPATH = DIRPATH + "chart/";
	/**
	 * テクニカル指標(MACD(5,20,9))のstdoutのファイル名。
	 */
	private static final String OUT_FILENAME = "CalcIndicator3.out";

	/**
	 * テクニカル指標(MACD(5,20,9))のstdoutのファイルパス。
	 */
	private String outFilePath;

	/**
	 * コンストラクタ。
	 * 
	 * @param name ディレクトリ名。
	 */
	public MainCalcIndicator3_r3(String name) {
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
			printMacd(pw, chartList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * MACD(5,20,9)を表示する。
	 * 
	 * @param pw        stdoutファイル。
	 * @param chartList マージしたチャートデータを時系列に並べたリスト。
	 */
	private void printMacd(PrintWriter pw, List<ChartInfo> chartList) {
		int writeCnt = 0;
		if (chartList.size() < 2) {
			return;
		}
		// 平滑定数を事前に計算する
		final double a5 = 2.0 / (5 + 1);
		final double a20 = 2.0 / (20 + 1);
		final double a9 = 2.0 / (9 + 1);
		// 初項を初期化する
		ChartInfo ci = chartList.get(0);
		double ema5 = ci.closePrice;
		double ema20 = ci.closePrice;
		double signal9 = Double.MIN_VALUE;
		for (int i = 1; i < chartList.size(); i++) {
			ci = chartList.get(i);
			pw.printf("%s,%d,%d", ci.date, ci.closePrice, ci.flag);
			ema5 += a5 * (ci.closePrice - ema5);
			ema20 += a20 * (ci.closePrice - ema20);
			double macd = ema5 - ema20;
			pw.printf(",%.2f,%.2f,%.2f", ema5, ema20, macd);
			if (signal9 == Double.MIN_VALUE) {
				signal9 = macd;
			} else {
				signal9 += a9 * (macd - signal9);
				pw.printf(",%.2f", signal9);
			}
			pw.println();
			writeCnt++;
		}
		System.out.println("MainCalcIndicator3_r3.printMacd(): " + outFilePath + ", writeCnt=" + writeCnt);
	}

}
