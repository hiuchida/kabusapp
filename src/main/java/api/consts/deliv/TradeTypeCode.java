package api.consts.deliv;

/**
 * 取引区分コード。
 * @see api.SendoderFutureApi body.tradeType:注文発注（先物）情報／市場コード。
 * @see api.SendoderOptionApi body.tradeType:注文発注（オプション）情報／市場コード。
 */
public enum TradeTypeCode {
	新規(1), 返済(2);

	private int id;

	private TradeTypeCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static TradeTypeCode valueOf(int id) {
		for (TradeTypeCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
