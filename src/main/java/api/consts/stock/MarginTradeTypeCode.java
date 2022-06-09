package api.consts.stock;

/**
 * 信用取引区分。
 * @see api.SendoderApi body.marginTradeType:注文発注（株式）情報／信用取引区分。
 */
public enum MarginTradeTypeCode {
	制度信用(1), 一般信用_長期(2), 一般信用_デイトレ(3);

	private int id;

	private MarginTradeTypeCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static MarginTradeTypeCode valueOf(int id) {
		for (MarginTradeTypeCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
