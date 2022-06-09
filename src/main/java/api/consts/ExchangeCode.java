package api.consts;

/**
 * 市場コード。
 * @see api.BoardApi symbol:銘柄コード（[銘柄コード]@[市場コード]）
 */
public enum ExchangeCode {
	東証(1), 名証(3), 福証(5), 札証(6), 日通し(2), 日中(23), 夜間(24);

	private int id;

	private ExchangeCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static ExchangeCode valueOf(int id) {
		for (ExchangeCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
