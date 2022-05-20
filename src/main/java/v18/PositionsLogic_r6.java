package v18;

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
import v17.ChartDataLogic;

/**
 * 建玉情報を管理する。
 */
public class PositionsLogic_r6 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 建玉情報を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String TXT_FILEPATH = DIRPATH + "PositionsLogic_r6.txt";
	/**
	 * 建玉情報の削除情報を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String DEL_FILEPATH = DIRPATH + "PositionsLogic_r6.del";
	/**
	 * 建玉情報ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "PositionsLogic_r6.log";

	/**
	 * 建玉情報クラス
	 */
	public static class PosInfo implements Comparable<PosInfo> {
		/**
		 * 建玉情報ファイルのカラム数。
		 */
		public static final int MAX_COLS = 13;
		/**
		 * 約定番号（ExecutionID）。
		 */
		public String executionId;
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
		 * 残数量（保有数量）(LeavesQty)。
		 */
		public int leavesQty;
		/**
		 * 拘束数量（返済のために拘束されている数量）(HoldQty)。
		 */
		public int holdQty;
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
		 * コンストラクタ（新規作成）。
		 * 
		 * @param id    約定番号（ExecutionID）。
		 * @param code  銘柄コード(Symbol)。
		 * @param name  銘柄名(SymbolName)。
		 * @param price 値段(Price)。
		 * @param side  売買区分(Side)。
		 */
		public PosInfo(String id, String code, String name, int price, String side) {
			this.executionId = id;
			this.code = code;
			this.name = name;
			this.price = price;
			this.side = side;
			this.createDate = System.currentTimeMillis();
		}

		/**
		 * コンストラクタ（建玉情報レコード）。
		 * 
		 * @param cols 建玉情報ファイルの1レコードの全てのカラム文字列。
		 */
		public PosInfo(String[] cols) {
			int i = 0;
			this.executionId = cols[i++];
			this.code = cols[i++];
			this.name = cols[i++];
			this.price = StringUtil.parseInt(cols[i++]);
			this.leavesQty = StringUtil.parseInt(cols[i++]);
			this.holdQty = StringUtil.parseInt(cols[i++]);
			this.side = "" + StringUtil.parseInt(cols[i++]);
			this.curPrice = StringUtil.parseInt(cols[i++]);
			this.profitHigh = StringUtil.parseInt(cols[i++]);
			this.profitLow = StringUtil.parseInt(cols[i++]);
			this.triggerPrice = StringUtil.parseInt(cols[i++]);
			this.createDate = StringUtil.parseLong(cols[i++]);
			this.updateDate = StringUtil.parseLong(cols[i++]);
		}

		/**
		 * 建玉情報ファイルのヘッダ文字列を生成する。
		 * 
		 * @return ヘッダ文字列。
		 */
		public static String toHeaderString() {
			String[] sa = new String[MAX_COLS];
			int i = 0;
			sa[i++] = "executionId";
			sa[i++] = "code     ";
			sa[i++] = "name             ";
			sa[i++] = "price";
			sa[i++] = "leavesQ";
			sa[i++] = "holdQty";
			sa[i++] = "side";
			sa[i++] = "curPric";
			sa[i++] = "high";
			sa[i++] = "low";
			sa[i++] = "trigger";
			sa[i++] = "createDate                            ";
			sa[i++] = "updateDate                            ";
			String val = "# " + StringUtil.joinTab(sa);
			return val;
		}

		/**
		 * インスタンスの主キー(code_price_sideStr)を取得する。
		 * 
		 * @return 主キー。
		 */
		public String getKey() {
			return executionId;
		}

		/**
		 * 建玉情報ファイルのレコード文字列を生成する。
		 * 
		 * @return レコード文字列。
		 */
		public String toLineString() {
			String[] sa = new String[MAX_COLS];
			int i = 0;
			sa[i++] = executionId;
			sa[i++] = code;
			sa[i++] = name;
			sa[i++] = "" + price;
			sa[i++] = "" + leavesQty;
			sa[i++] = "" + holdQty;
			sa[i++] = side + "(" + StringUtil.sideStr(side) + ")";
			sa[i++] = "" + curPrice;
			sa[i++] = "" + profitHigh;
			sa[i++] = "" + profitLow;
			sa[i++] = "" + triggerPrice;
			sa[i++] = createDate + "(" + DateTimeUtil.toString(createDate) + ")";
			sa[i++] = updateDate + "(" + DateTimeUtil.toString(updateDate) + ")";
			String val = StringUtil.joinTab(sa);
			return val;
		}

		@Override
		public int compareTo(PosInfo that) {
			String key1 = this.code + "_" + this.side + "_" + this.price + "_" + this.executionId;
			String key2 = that.code + "_" + that.side + "_" + that.price + "_" + that.executionId;
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
	public PositionsLogic_r6(String X_API_KEY) {
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
		System.out.println("PositionsLogic_r6.execute(): response.size=" + response.size());
		Set<String> highSet = new TreeSet<>();
		for (int i = 0; i < response.size(); i++) {
			PositionsSuccess pos = response.get(i);
			String id = pos.getExecutionID();
			String key = id;
			String code = pos.getSymbol();
			String name = pos.getSymbolName();
			int price = (int) (double) pos.getPrice();
			int leavesQty = (int) (double) pos.getLeavesQty();
			int holdQty = (int) (double) pos.getHoldQty();
			String side = pos.getSide();
			int sign = StringUtil.sign(side);
			int curPrice = (int) (double) pos.getCurrentPrice();
			Integer type = pos.getSecurityType();
			if (type == null || type != 901 || leavesQty == 0) {
				String msg = "get " + StringUtil.index(i + 1) + ": SKIP " + id + " " + type + " " + code + " "
						+ name + " " + price + " " + StringUtil.sideStr(side)
						+ "(" + leavesQty + "-" + holdQty + ") " + curPrice;
				System.out.println("  > execute " + msg);
				continue;
			}
			posKeySet.remove(key);
			if (curPrice != 0) {
				int profit = (curPrice - price) * sign;
				String msg = "get " + StringUtil.index(i + 1) + ": " + id + " " + key + " " + type + " " + code
						+ " " + name + " " + price + " " + StringUtil.sideStr(side)
						+ "(" + leavesQty + "-" + holdQty + ") " + " " + curPrice + " " + profit;
				System.out.println("  > execute " + msg);
				FileUtil.printLog(LOG_FILEPATH, "execute", msg);
				PosInfo pi = posMap.get(key);
				if (pi == null) {
					pi = new PosInfo(id, code, name, price, side);
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
				pi.leavesQty = leavesQty;
				pi.holdQty = holdQty;
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
		PosInfo pi = posMap.get(executionId);
		return pi;
	}

	/**
	 * 建玉情報ファイルを読み込む。不正なレコードは無視される。
	 */
	public void readPositions() {
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
		System.out.println("PositionsLogic_r6.readPositions(): posMap.size=" + posMap.size());
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
				System.out.println("PositionsLogic_r6.deletePositions(): posKeySet.size=" + posKeySet.size());
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
		System.out.println("PositionsLogic_r6.writePositions(): posMap.size=" + posMap.size());
		List<PosInfo> list = new ArrayList<>();
		list.addAll(posMap.values());
		Collections.sort(list);
		for (PosInfo pi : list) {
			lines.add(pi.toLineString());
			String key = pi.getKey();
			System.out.println("  " + key + ": " + pi);
		}
		FileUtil.writeAllLines(TXT_FILEPATH, lines);
	}

}
