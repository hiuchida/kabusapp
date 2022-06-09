package api.consts.stock;

/**
 * ヒット後執行条件コード。
 * 
 * @see api.SendoderApi body.reverseLimitOrder.afterHitOrderType:注文発注（株式）情報／ヒット後執行条件。
 */
public enum AfterHitOrderTypeSCode {
	成行(1), 指値(2), 不成(3);

	private int id;

	private AfterHitOrderTypeSCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static AfterHitOrderTypeSCode valueOf(int id) {
		for (AfterHitOrderTypeSCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
