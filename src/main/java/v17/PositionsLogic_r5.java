package v17;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import api.PositionsApi;
import io.swagger.client.ApiException;
import io.swagger.client.model.PositionsSuccess;
import util.DateTimeUtil;
import util.FileUtil;
import util.StringUtil;

/**
 * 建玉情報を管理する。
 */
public class PositionsLogic_r5 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 建玉情報を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String TXT_FILEPATH = DIRPATH + "PositionsLogic_r5.txt";
	/**
	 * 建玉情報の削除情報を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String DEL_FILEPATH = DIRPATH + "PositionsLogic_r5.del";
	/**
	 * 建玉情報ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "PositionsLogic_r5.log";

	/**
	 * 建玉情報クラス
	 */
	public static class PosInfo implements Comparable<PosInfo> {
		/**
		 * 建玉情報ファイルのカラム数。
		 */
		public static final int MAX_COLS = 11;
		/**
		 * 銘柄コード(Symbol)。
		 */
		public String code;
		/**
		 * 銘柄名(SymbolName)。
		 */
		public String name;
		/**
		 * 値段(Price)。
		 */
		public int price;
		/**
		 * 売買区分(Side)。
		 */
		public String side;
		/**
		 * 現在値(CurrentPrice)。
		 */
		public int curPrice;
		/**
		 * 含み益の最大値。
		 */
		public int profitHigh;
		/**
		 * 含み損の最小値。
		 */
		public int profitLow;
		/**
		 * トリガ価格(TriggerPrice)。
		 */
		public int triggerPrice;
		/**
		 * 生成日時。
		 */
		public long createDate;
		/**
		 * 更新日時。
		 */
		public long updateDate;
		/**
		 * 約定数量情報のリスト。
		 */
		public List<ExecutionInfo> executionList;
		/**
		 * 約定数量情報の文字列（削除ログ用）。
		 */
		public String executionStr;

		/**
		 * コンストラクタ（新規作成）。
		 * 
		 * @param code  銘柄コード(Symbol)。
		 * @param name  銘柄名(SymbolName)。
		 * @param price 値段(Price)。
		 * @param side  売買区分(Side)。
		 */
		public PosInfo(String code, String name, int price, String side) {
			this.code = code;
			this.name = name;
			this.price = price;
			this.side = side;
			this.createDate = System.currentTimeMillis();
			this.executionList = new ArrayList<>();
			this.executionStr = "";
		}

		/**
		 * コンストラクタ（建玉情報レコード）。
		 * 
		 * @param cols 建玉情報ファイルの1レコードの全てのカラム文字列。
		 */
		public PosInfo(String[] cols) {
			int i = 0;
			this.code = cols[i++];
			this.name = cols[i++];
			this.price = StringUtil.parseInt(cols[i++]);
			this.side = "" + StringUtil.parseInt(cols[i++]);
			this.curPrice = StringUtil.parseInt(cols[i++]);
			this.profitHigh = StringUtil.parseInt(cols[i++]);
			this.profitLow = StringUtil.parseInt(cols[i++]);
			this.triggerPrice = StringUtil.parseInt(cols[i++]);
			this.createDate = StringUtil.parseLong(cols[i++]);
			this.updateDate = StringUtil.parseLong(cols[i++]);
			this.executionList = new ArrayList<>();
			this.executionStr = cols[i++];
		}

		/**
		 * 主キー(code_price_sideStr)を生成する。
		 * 
		 * @param code  銘柄コード(Symbol)。
		 * @param price 値段(Price)。
		 * @param side  売買区分(Side)。
		 * @return 主キー。
		 */
		public static String getKey(String code, int price, String side) {
			return code + "_" + price + "_" + StringUtil.sideStr(side);
		}

		/**
		 * 建玉情報ファイルのヘッダ文字列を生成する。
		 * 
		 * @return ヘッダ文字列。
		 */
		public static String toHeaderString() {
			String[] sa = new String[MAX_COLS];
			int i = 0;
			sa[i++] = "code   ";
			sa[i++] = "name             ";
			sa[i++] = "price(qty)";
			sa[i++] = "side";
			sa[i++] = "curPric";
			sa[i++] = "high";
			sa[i++] = "low";
			sa[i++] = "trigger";
			sa[i++] = "createDate                            ";
			sa[i++] = "updateDate                            ";
			sa[i++] = "executionIds";
			String val = "# " + StringUtil.joinTab(sa);
			return val;
		}

		/**
		 * 残数量（保有数量）(LeavesQty)を集計する。
		 * 
		 * @return 残数量（保有数量）(LeavesQty)。
		 */
		public int getLeavesQty() {
			int qty = 0;
			for (ExecutionInfo ei : executionList) {
				qty += ei.leavesQty;
			}
			return qty;
		}

		/**
		 * 拘束数量（返済のために拘束されている数量）(HoldQty)を集計する。
		 * 
		 * @return 拘束数量（返済のために拘束されている数量）(HoldQty)。
		 */
		public int getHoldQty() {
			int qty = 0;
			for (ExecutionInfo ei : executionList) {
				qty += ei.holdQty;
			}
			return qty;
		}

		/**
		 * インスタンスの主キー(code_price_sideStr)を取得する。
		 * 
		 * @return 主キー。
		 */
		public String getKey() {
			return getKey(code, price, side);
		}

		/**
		 * 約定数量情報のリストから文字列を設定する。
		 */
		public void setExecutionStr() {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < executionList.size(); j++) {
				if (j > 0) {
					sb.append(",");
				}
				sb.append(executionList.get(j));
			}
			this.executionStr = sb.toString();
		}

		/**
		 * 建玉情報ファイルのレコード文字列を生成する。
		 * 
		 * @return レコード文字列。
		 */
		public String toLineString() {
			String[] sa = new String[MAX_COLS];
			int i = 0;
			sa[i++] = code;
			sa[i++] = name;
			sa[i++] = price + "(" + getLeavesQty() + "-" + getHoldQty() + ")";
			sa[i++] = side + "(" + StringUtil.sideStr(side) + ")";
			sa[i++] = "" + curPrice;
			sa[i++] = "" + profitHigh;
			sa[i++] = "" + profitLow;
			sa[i++] = "" + triggerPrice;
			sa[i++] = createDate + "(" + DateTimeUtil.toString(createDate) + ")";
			sa[i++] = updateDate + "(" + DateTimeUtil.toString(updateDate) + ")";
			sa[i++] = executionStr;
			String val = StringUtil.joinTab(sa);
			return val;
		}

		@Override
		public int compareTo(PosInfo that) {
			String key1 = code + "_" + price + "_" + StringUtil.sideStr(side);
			String key2 = that.code + "_" + that.price + "_" + StringUtil.sideStr(that.side);
			return key1.compareTo(key2);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{name=").append(name);
			sb.append(", profitHigh=").append(profitHigh);
			sb.append(", profitLow=").append(profitLow);
			sb.append("}");
			return sb.toString();
		}
	}

	/**
	 * 約定数量情報クラス
	 */
	public static class ExecutionInfo {
		/**
		 * 約定番号（ExecutionID）。
		 */
		public String executionId;
		/**
		 * 残数量（保有数量）(LeavesQty)。
		 */
		public int leavesQty;
		/**
		 * 拘束数量（返済のために拘束されている数量）(HoldQty)。
		 */
		public int holdQty;

		/**
		 * コンストラクタ（新規作成）。
		 * 
		 * @param executionId 約定番号（ExecutionID）。
		 * @param leavesQty   残数量（保有数量）(LeavesQty)。
		 * @param holdQty     拘束数量（返済のために拘束されている数量）(HoldQty)。
		 */
		public ExecutionInfo(String executionId, Double leavesQty, Double holdQty) {
			this.executionId = executionId;
			this.leavesQty = (int) (double) leavesQty;
			this.holdQty = (int) (double) holdQty;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(executionId);
			sb.append("(").append(leavesQty);
			sb.append("-").append(holdQty);
			sb.append(")");
			return sb.toString();
		}
	}

	/**
	 * 残高照会API。
	 */
	private PositionsApi positionsApi;

	/**
	 * 建玉情報のマップ。
	 */
	private Map<String, PosInfo> posMap;
	/**
	 * 削除対象の建玉情報キーのセット。
	 */
	private Set<String> posKeySet;
	
	private ChartDataLogic chartDataLogic;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public PositionsLogic_r5(String X_API_KEY) {
		this.positionsApi = new PositionsApi(X_API_KEY);
		this.chartDataLogic = new ChartDataLogic();
	}

	/**
	 * 建玉情報を更新し、含み益を更新したリストを返す。
	 * 
	 * @return 含み益を更新した建玉情報リスト。
	 * @throws ApiException
	 */
	public List<PosInfo> execute() throws ApiException {
		readPositions();
		List<PositionsSuccess> response = positionsApi.get();
		System.out.println("PositionsLogic_r5.execute(): response.size=" + response.size());
		Set<String> highSet = new TreeSet<>();
		for (int i = 0; i < response.size(); i++) {
			PositionsSuccess pos = response.get(i);
			String id = pos.getExecutionID();
			String code = pos.getSymbol();
			String name = pos.getSymbolName();
			int price = (int) (double) pos.getPrice();
			String side = pos.getSide();
			String key = PosInfo.getKey(code, price, side);
			int sign = StringUtil.sign(side);
			ExecutionInfo ei = new ExecutionInfo(id, pos.getLeavesQty(), pos.getHoldQty());
			int qty = (int) (sign * ei.leavesQty);
			int curPrice = (int) (double) pos.getCurrentPrice();
			Integer type = pos.getSecurityType();
			if (type == null || type != 901 || qty == 0) {
				String msg = "get " + StringUtil.index(i + 1) + ": SKIP " + type + " " + code + " " + name + " "
						+ qty + " " + price + " " + StringUtil.sideStr(side) + " " + curPrice + " " + ei;
				System.out.println("  > execute " + msg);
				continue;
			}
			posKeySet.remove(key);
			if (curPrice != 0) {
				int profit = (curPrice - price) * sign;
				String msg = "get " + StringUtil.index(i + 1) + ": " + key + " " + type + " " + code + " " + name
						+ " " + qty + " " + price + " " + StringUtil.sideStr(side) + " " + curPrice + " " + profit + " "
						+ ei;
				System.out.println("  > execute " + msg);
				PosInfo pi = posMap.get(key);
				if (pi == null) {
					pi = new PosInfo(code, name, price, side);
					pi.profitHigh = profit;
					pi.profitLow = profit;
					posMap.put(key, pi);
					msg = "create " + key + " " + name + ": curPrice=" + curPrice + " profit=" + profit;
					System.out.println("  > " + msg);
					FileUtil.printLog(LOG_FILEPATH, "execute", msg);
				} else {
					int[] priceHighLow = chartDataLogic.searchHighLow(pi.updateDate);
					if (priceHighLow[0] > 0) {
						int profitHigh = (priceHighLow[0] - price) * sign;
						int profitLow = (priceHighLow[1] - price) * sign;
						if (profitHigh >= profitLow && profitHigh > profit) {
							msg = "priceHighLow profitHigh=" + profitHigh + " <- " + profit + ", highPrice=" + priceHighLow[0] + " <- " + curPrice;
							System.out.println("  > " + msg);
							FileUtil.printLog(LOG_FILEPATH, "execute", msg);
							profit = profitHigh;
							curPrice = priceHighLow[0];
						} else if (profitLow > profitHigh && profitLow > profit) {
							msg = "priceHighLow profitLow=" + profitLow + " <- " + profit + ", lowPrice=" + priceHighLow[1] + " <- " + curPrice;
							System.out.println("  > " + msg);
							FileUtil.printLog(LOG_FILEPATH, "execute", msg);
							profit = profitLow;
							curPrice = priceHighLow[1];
						}
					}
					if (pi.profitHigh < profit) {
						int delta = profit - pi.profitHigh;
						msg = "update " + key + " " + name + ": curPrice=" + curPrice + " profitHighDelta="
								+ delta + " (" + profit + " <- " + pi.profitHigh + ")";
						System.out.println("  > " + msg);
						FileUtil.printLog(LOG_FILEPATH, "execute", msg);
						pi.profitHigh = profit;
						highSet.add(key);
					}
					// profitLowは近い将来削除する予定。priceHighLowは考慮しない。
					// 本来はhigh,lowのどちらかしか更新されないはずだが、念のため両方チェック
					if (profit < pi.profitLow) {
						int delta = profit - pi.profitLow;
						msg = "update " + key + " " + name + ": curPrice=" + curPrice + " profitLowDelta="
								+ delta + " (" + profit + " <- " + pi.profitLow + ")";
						System.out.println("  > " + msg);
						FileUtil.printLog(LOG_FILEPATH, "execute", msg);
						pi.profitLow = profit;
					}
				}
				pi.executionList.add(ei);
				pi.curPrice = curPrice;
				pi.updateDate = System.currentTimeMillis();
			}
		}
		deletePositions();
		writePositions();
		List<PosInfo> highList = new ArrayList<>();
		for (String key : highSet) {
			PosInfo pi = posMap.get(key);
			highList.add(pi);
		}
		return highList;
	}

	/**
	 * メモリ上の建玉情報リストを返す。
	 * 
	 * @return 建玉情報リスト。
	 * @throws ApiException
	 */
	public List<PosInfo> getList() {
		List<PosInfo> list = new ArrayList<>();
		for (PosInfo pi : posMap.values()) {
			list.add(pi);
		}
		return list;
	}

	/**
	 * 指定した残高照会を行う。
	 * 
	 * @param code 銘柄コード(Symbol)。
	 * @return 残高情報のリスト。
	 * @throws ApiException 
	 */
	public List<PositionsSuccess> getPosition(String code) throws ApiException {
		List<PositionsSuccess> response = positionsApi.getSymbol(code);
		return response;
	}

	/**
	 * メモリ上の建玉に指定した約定番号が含まれるか？
	 * 
	 * @param executionIds　約定番号の列挙。
	 * @return true:含まれる、false:含まれない。
	 */
	public boolean isValidExecutionId(String executionIds) {
		String[] flds = StringUtil.splitComma(executionIds);
		for (String s : flds) {
			if (s.length() == 0) {
				continue;
			}
			String id = StringUtil.parseString(s, ":");
			PosInfo pos = this.getByExecutionId(id);
			if (pos != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * メモリ上の建玉情報を返す。
	 * 
	 * @param executionId 約定番号（ExecutionID）。
	 * @return 建玉情報。
	 * @throws ApiException
	 */
	public PosInfo getByExecutionId(String executionId) {
		for (PosInfo pi : posMap.values()) {
			for (ExecutionInfo ei : pi.executionList) {
				if (ei.executionId.equals(executionId)) {
					return pi;
				}
			}
		}
		return null;
	}

	/**
	 * 建玉情報ファイルを読み込む。不正なレコードは無視される。
	 */
	private void readPositions() {
		posMap = new TreeMap<>();
		posKeySet = new TreeSet<>();
		List<String> lines = FileUtil.readAllLines(TXT_FILEPATH);
		for (String s : lines) {
			if (s.startsWith("#")) {
				continue;
			}
			String[] cols = StringUtil.splitTab(s);
			if (cols.length != PosInfo.MAX_COLS) {
				System.out.println("Warning: SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			PosInfo pi = new PosInfo(cols);
			String key = pi.getKey();
			posMap.put(key, pi);
			posKeySet.add(key);
		}
		System.out.println("PositionsLogic_r5.readPositions(): posMap.size=" + posMap.size());
		for (String key : posMap.keySet()) {
			PosInfo pi = posMap.get(key);
			System.out.println("  " + key + ": " + pi);
		}
	}

	/**
	 * 決済済の建玉を削除する。
	 */
	private void deletePositions() {
		if (posKeySet.size() > 0) {
			try (PrintWriter pw = FileUtil.writer(DEL_FILEPATH, FileUtil.UTF8, true)) {
				System.out.println("PositionsLogic_r5.deletePositions(): posKeySet.size=" + posKeySet.size());
				for (String key : posKeySet) {
					PosInfo pi = posMap.get(key);
					FileUtil.printLogLine(pw, pi.toLineString());
					String msg = "delete " + key + " " + pi.name;
					System.out.println("  > deletePositions " + msg);
					FileUtil.printLog(LOG_FILEPATH, "deletePositions", msg);
					posMap.remove(key);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 建玉情報ファイルを書き込む。
	 */
	public void writePositions() {
		List<String> lines = new ArrayList<>();
		lines.add(PosInfo.toHeaderString());
		System.out.println("PositionsLogic_r5.writePositions(): posMap.size=" + posMap.size());
		List<PosInfo> list = new ArrayList<>();
		list.addAll(posMap.values());
		Collections.sort(list);
		for (PosInfo pi : list) {
			pi.setExecutionStr();
			lines.add(pi.toLineString());
			String key = pi.getKey();
			System.out.println("  " + key + ": " + pi);
		}
		FileUtil.writeAllLines(TXT_FILEPATH, lines);
	}

}
