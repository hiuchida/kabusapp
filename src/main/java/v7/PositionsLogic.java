package v7;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import io.swagger.client.ApiException;
import io.swagger.client.api.InfoApi;
import io.swagger.client.model.PositionsSuccess;
import util.DateTimeUtil;
import util.FileUtil;
import util.StringUtil;

/**
 * 建玉情報を管理する。
 */
public class PositionsLogic {
	/**
	 * タブ文字。
	 */
	public static final String TAB = "\t";

	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 建玉情報を保存したファイルパス。存在しなければ生成される。
	 */
	private static final String TXT_FILEPATH = DIRPATH + "PositionsLogic.txt";
	/**
	 * 建玉情報ログのファイルパス。存在しなければ生成される。
	 */
	private static final String LOG_FILEPATH = DIRPATH + "PositionsLogic.log";

	/**
	 * 建玉情報クラス
	 */
	public static class PosInfo {
		/**
		 * 建玉情報ファイルのカラム数。
		 */
		public static final int MAX_COLS = 10;
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
			this.createDate = StringUtil.parseLong(cols[i++]);
			this.updateDate = StringUtil.parseLong(cols[i++]);
			this.executionList = new ArrayList<>();
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
			StringBuilder sb = new StringBuilder();
			sb.append("# ");
			sb.append("code   ").append(TAB);
			sb.append("name             ").append(TAB);
			sb.append("price").append(TAB);
			sb.append("side").append(TAB);
			sb.append("curPric").append(TAB);
			sb.append("high").append(TAB);
			sb.append("low").append(TAB);
			sb.append("createDate                            ").append(TAB);
			sb.append("updateDate                            ").append(TAB);
			sb.append("executionIds");
			return sb.toString();
		}

		/**
		 * インスタンスの主キー(code_price_sideStr)を生成する。
		 * 
		 * @return 主キー。
		 */
		public String getKey() {
			return getKey(code, price, side);
		}

		/**
		 * 建玉情報ファイルのレコード文字列を生成する。
		 * 
		 * @return レコード文字列。
		 */
		public String toLineString() {
			StringBuilder sb = new StringBuilder();
			sb.append(code).append(TAB);
			sb.append(name).append(TAB);
			sb.append(price).append(TAB);
			sb.append(side).append("(").append(StringUtil.sideStr(side)).append(")").append(TAB);
			sb.append(curPrice).append(TAB);
			sb.append(profitHigh).append(TAB);
			sb.append(profitLow).append(TAB);
			sb.append(createDate).append("(").append(DateTimeUtil.toString(createDate)).append(")").append(TAB);
			sb.append(updateDate).append("(").append(DateTimeUtil.toString(updateDate)).append(")").append(TAB);
			for (int i = 0; i < executionList.size(); i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(executionList.get(i));
			}
			return sb.toString();
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
	 * 認証済TOKEN。
	 */
	private String X_API_KEY;

	/**
	 * 情報API。
	 */
	private InfoApi infoApi = new InfoApi();

	/**
	 * 建玉情報のマップ。
	 */
	private Map<String, PosInfo> posMap;
	/**
	 * 削除対象の建玉情報キーのセット。
	 */
	private Set<String> posKeySet;

	/**
	 * コンストラクタ。
	 * 
	 * @param X_API_KEY 認証済TOKEN。
	 */
	public PositionsLogic(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	/**
	 * 建玉情報を更新し、含み益を更新したリストを返す。
	 * 
	 * @return 含み益を更新した建玉情報リスト。
	 * @throws ApiException
	 */
	public List<PosInfo> execute() throws ApiException {
		readPositions();
		String product = null;
		String symbol = null;
		String sideParam = null;
		String addinfo = null;
		List<PositionsSuccess> response = infoApi.positionsGet(X_API_KEY, product, symbol, sideParam, addinfo);
		System.out.println("PositionsLogic.execute(): response.size=" + response.size());
		Set<String> highSet = new TreeSet<>();
		for (int i = 0; i < response.size(); i++) {
			PositionsSuccess pos = response.get(i);
			String id = pos.getExecutionID();
			String code = pos.getSymbol();
			String name = pos.getSymbolName();
			String side = pos.getSide();
			int sign = StringUtil.sign(side);
			ExecutionInfo ei = new ExecutionInfo(id, pos.getLeavesQty(), pos.getHoldQty());
			int qty = (int) (sign * ei.leavesQty);
			int price = (int) (double) pos.getPrice();
			int curPrice = (int) (double) pos.getCurrentPrice();
			Integer type = pos.getSecurityType();
			if (type != null && type == 901 && qty != 0 && curPrice != 0) {
				int profit = ((curPrice - price) * sign);
				String key = PosInfo.getKey(code, price, side);
				System.out.println("  " + index(i + 1) + ": " + key + " " + type + " " + code + " " + name + " "
						+ qty + " " + price + " " + StringUtil.sideStr(side) + " " + curPrice + " " + profit + " " + ei);
				PosInfo pi = posMap.get(key);
				if (pi == null) {
					pi = new PosInfo(code, name, price, side);
					pi.profitHigh = profit;
					pi.profitLow = profit;
					posMap.put(key, pi);
					String msg = "create " + key + " " + name + ": curPrice=" + curPrice + " profit=" + profit;
					System.out.println("  > " + msg);
					FileUtil.printLog(LOG_FILEPATH, "execute", msg);
				} else {
					if (pi.profitHigh < profit) {
						int delta = profit - pi.profitHigh;
						String msg = "update " + key + " " + name + ": curPrice=" + curPrice + " profitHighDelta="
								+ delta + " (" + profit + " <- " + pi.profitHigh + ")";
						System.out.println("  > " + msg);
						FileUtil.printLog(LOG_FILEPATH, "execute", msg);
						pi.profitHigh = profit;
						highSet.add(key);
					}
					// 本来はhigh,lowのどちらかしか更新されないはずだが、念のため両方チェック
					if (profit < pi.profitLow) {
						int delta = profit - pi.profitLow;
						String msg = "update " + key + " " + name + ": curPrice=" + curPrice + " profitLowDelta="
								+ delta + " (" + profit + " <- " + pi.profitLow + ")";
						System.out.println("  > " + msg);
						FileUtil.printLog(LOG_FILEPATH, "execute", msg);
						pi.profitLow = profit;
					}
				}
				pi.executionList.add(ei);
				pi.curPrice = curPrice;
				pi.updateDate = System.currentTimeMillis();
				posKeySet.remove(key);
			} else {
				System.out.println("  " + index(i + 1) + ": SKIP " + type + " " + code + " " + name + " " + qty
						+ " " + price + " " + StringUtil.sideStr(side) + " " + curPrice + " " + ei);
			}
		}
		writePositions();
		List<PosInfo> highList = new ArrayList<>();
		for (String key : highSet) {
			PosInfo pi = posMap.get(key);
			highList.add(pi);
		}
		return highList;
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
			String[] cols = s.split(TAB);
			if (cols.length < PosInfo.MAX_COLS) {
				System.out.println("Warning: SKIP cols.length=" + cols.length + ", line=" + s);
				continue;
			}
			PosInfo pi = new PosInfo(cols);
			String key = pi.getKey();
			posMap.put(key, pi);
			posKeySet.add(key);
		}
		System.out.println("PositionsLogic.readPositions(): posMap.size=" + posMap.size());
		for (String key : posMap.keySet()) {
			PosInfo pi = posMap.get(key);
			System.out.println("  " + key + ": " + pi);
		}
	}

	/**
	 * 建玉情報ファイルを書き込む。決済済の建玉は削除される。
	 */
	private void writePositions() {
		System.out.println("PositionsLogic.writePositions(): posKeySet.size=" + posKeySet.size());
		for (String key : posKeySet) {
			PosInfo pi = posMap.get(key);
			String msg = "delete " + key + " " + pi.name;
			System.out.println("  > " + msg);
			FileUtil.printLog(LOG_FILEPATH, "writePositions", msg);
			posMap.remove(key);
		}
		System.out.println("PositionsLogic.writePositions(): posMap.size=" + posMap.size());
		for (String key : posMap.keySet()) {
			PosInfo pi = posMap.get(key);
			System.out.println("  " + key + ": " + pi);
		}
		List<String> lines = new ArrayList<>();
		lines.add(PosInfo.toHeaderString());
		for (String key : posMap.keySet()) {
			PosInfo pi = posMap.get(key);
			lines.add(pi.toLineString());
		}
		FileUtil.writeAllLines(TXT_FILEPATH, lines);
	}

	private String index(int idx) {
		return String.format("%02d", idx);
	}

}
