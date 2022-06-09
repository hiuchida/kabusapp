package api.consts.stock;

/**
 * 市場コード（株式用）。
 * @see api.SendoderApi body.tradeType:注文発注（株式）情報／市場コード。
 */
public enum ExchangeSCode {
	東証(1), 名証(3), 福証(5), 札証(6);

	private int id;

	private ExchangeSCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static ExchangeSCode valueOf(int id) {
		for (ExchangeSCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
