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
import v4.LockedAuthorizedToken;

/**
 * 建玉情報を管理するツール。
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
		 * 約定番号(ExecutionID)の配列をカンマ区切り。
		 */
		public String executionIds;

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
			this.executionIds = "?";
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
			this.executionIds = "?";
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
			sb.append(executionIds);
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

	private String X_API_KEY;

	private InfoApi infoApi = new InfoApi();

	/**
	 * 建玉情報のマップ。
	 */
	private Map<String, PosInfo> posMap;
	/**
	 * 削除対象の建玉情報キーのセット。
	 */
	private Set<String> posKeySet;

	public PositionsLogic(String X_API_KEY) {
		this.X_API_KEY = X_API_KEY;
	}

	public static void main(String[] args) throws ApiException {
		String X_API_KEY = LockedAuthorizedToken.lockToken();
		try {
			new PositionsLogic(X_API_KEY).execute();
		} finally {
			LockedAuthorizedToken.unlockToken();
		}
	}

	public void execute() throws ApiException {
		readPositions();
		String product = null;
		String symbol = null;
		String sideParam = null;
		String addinfo = null;
		List<PositionsSuccess> response = infoApi.positionsGet(X_API_KEY, product, symbol, sideParam, addinfo);
		System.out.println("main(): response.size=" + response.size());
		for (int i = 0; i < response.size(); i++) {
			PositionsSuccess pos = response.get(i);
			String id = pos.getExecutionID();
			String code = pos.getSymbol();
			String name = pos.getSymbolName();
			String side = pos.getSide();
			int sign = sign(side);
			int leaves = (int) (double) pos.getLeavesQty();
			int hold = (int) (double) pos.getHoldQty();
			int qty = (int) (sign * leaves);
			int price = (int) (double) pos.getPrice();
			int curPrice = (int) (double) pos.getCurrentPrice();
			Integer type = pos.getSecurityType();
			if (type != null && type == 901 && qty != 0 && curPrice != 0) {
				int profit = ((curPrice - price) * sign);
				String key = PosInfo.getKey(code, price, side);
				System.out.println("  " + index(i + 1) + ": " + key + " " + type + " " + code + " " + name + " "
						+ qty + " " + price + " " + curPrice + " " + profit);
				PosInfo pi = posMap.get(key);
				if (pi == null) {
					pi = new PosInfo(code, name, price, side);
					pi.profitHigh = profit;
					pi.profitLow = profit;
					pi.executionIds = id + "(" + leaves + "-" + hold + ")";
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
					if (pi.executionIds.equals("?")) {
						pi.executionIds = id + "(" + leaves + "-" + hold + ")";
					} else {
						pi.executionIds = pi.executionIds + "," + id + "(" + leaves + "-" + hold + ")";
					}
				}
				pi.curPrice = curPrice;
				pi.updateDate = System.currentTimeMillis();
				posKeySet.remove(key);
			} else {
				System.out.println("  " + index(i + 1) + ": SKIP " + type + " " + code + " " + name + " " + qty
						+ " " + price + " " + curPrice);
			}
		}
		writePositions();
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
		System.out.println("readPositions(): posMap.size=" + posMap.size());
		for (String key : posMap.keySet()) {
			PosInfo pi = posMap.get(key);
			System.out.println("  " + key + ": " + pi);
		}
	}

	/**
	 * 建玉情報ファイルを書き込む。決済済の建玉は削除される。
	 */
	private void writePositions() {
		System.out.println("writePositions(): posKeySet.size=" + posKeySet.size());
		for (String key : posKeySet) {
			PosInfo pi = posMap.get(key);
			String msg = "delete " + key + " " + pi.name;
			System.out.println("  > " + msg);
			FileUtil.printLog(LOG_FILEPATH, "writePositions", msg);
			posMap.remove(key);
		}
		System.out.println("writePositions(): posMap.size=" + posMap.size());
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

	/**
	 * 数量の符号を取得する。
	 * 
	 * @param side 売買区分(Side)。
	 * @return 符号
	 */
	private int sign(String side) {
		switch (side) {
		case "1":
			return -1;
		case "2":
			return 1;
		default:
			throw new RuntimeException();
		}
	}

	private String index(int idx) {
		return String.format("%02d", idx);
	}

}
