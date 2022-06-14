package v27;

import java.util.List;

import v27.MainCalcIndicator_r3.ChartInfo;

/**
 * テクニカル指標を計算するインターフェイス。
 */
public interface CalcIndicator_r3 {
	/**
	 * テクニカル指標を計算する。
	 * 
	 * @param chartList マージしたチャートデータを時系列に並べたリスト。
	 */
	public void execute(List<ChartInfo> chartList);

}
