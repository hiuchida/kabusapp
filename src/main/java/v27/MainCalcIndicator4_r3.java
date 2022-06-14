package v27;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import util.FileUtil;
import v27.MainCalcIndicator_r3.ChartInfo;

/**
 * テクニカル指標(HV20)を計算するクラス。
 */
public class MainCalcIndicator4_r3 implements CalcIndicator_r3 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * チャートデータディレクトリパス。
	 */
	private static final String DIR_CHARTPATH = DIRPATH + "chart/";
	/**
	 * テクニカル指標(HV20)のstdoutのファイル名。
	 */
	private static final String OUT_FILENAME = "CalcIndicator4.out";

	/**
	 * テクニカル指標(HV20)のstdoutのファイルパス。
	 */
	private String outFilePath;

	/**
	 * コンストラクタ。
	 * 
	 * @param name ディレクトリ名。
	 */
	public MainCalcIndicator4_r3(String name) {
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
			printHv(pw, chartList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * HV20を表示する。
	 * 
	 * @param pw        stdoutファイル。
	 * @param chartList マージしたチャートデータを時系列に並べたリスト。
	 */
	private void printHv(PrintWriter pw, List<ChartInfo> chartList) {
		int writeCnt = 0;
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
			pw.printf("%s,%d,%d", ci.date, ci.closePrice, ci.flag);
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
				double hv = sd * Math.sqrt(250 * 20 * 60);
				pw.printf(",%.5f,%.5f", sd, hv);
			}
			pw.println();
			writeCnt++;
		}
		System.out.println("MainCalcIndicator4_r3.printHv(): " + outFilePath + ", writeCnt=" + writeCnt);
	}

}
