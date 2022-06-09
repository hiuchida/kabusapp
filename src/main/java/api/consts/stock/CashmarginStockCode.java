package api.consts.stock;

/**
 * 取引区分コード（株式用）。
 * @see api.SendoderApi body.cashmargin:注文発注（株式）情報／取引区分。
 */
public enum CashmarginStockCode {
	現物(1), 新規(2), 返済(3);

	private int id;

	private CashmarginStockCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static CashmarginStockCode valueOf(int id) {
		for (CashmarginStockCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
