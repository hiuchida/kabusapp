package api.consts.deliv;

/**
 * ヒット後執行条件コード。
 * 
 * @see api.SendoderFutureApi body.reverseLimitOrder.afterHitOrderType:注文発注（先物）情報／ヒット後執行条件。
 * @see api.SendoderOptionApi body.reverseLimitOrder.afterHitOrderType:注文発注（オプション）情報／ヒット後執行条件。
 */
public enum AfterHitOrderTypeDCode {
	成行(1), 指値(2);

	private int id;

	private AfterHitOrderTypeDCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static AfterHitOrderTypeDCode valueOf(int id) {
		for (AfterHitOrderTypeDCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
