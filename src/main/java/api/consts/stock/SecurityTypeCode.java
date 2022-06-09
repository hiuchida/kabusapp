package api.consts.stock;

/**
 * 商品種別。
 * @see api.SendoderApi body.tradeType:注文発注（株式）情報／商品種別。
 */
public enum SecurityTypeCode {
	株式(1);

	private int id;

	private SecurityTypeCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static SecurityTypeCode valueOf(int id) {
		for (SecurityTypeCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
