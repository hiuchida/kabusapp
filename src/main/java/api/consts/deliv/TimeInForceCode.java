package api.consts.deliv;

/**
 * 有効期間条件。
 * @see api.SendoderFutureApi body.timeInForce:注文発注（株式）情報／有効期間条件。
 * @see api.SendoderOptionApi body.timeInForce:注文発注（株式）情報／有効期間条件。
 */
public enum TimeInForceCode {
	FAS(1), FAK(2), FOK(3);

	private int id;

	private TimeInForceCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static TimeInForceCode valueOf(int id) {
		for (TimeInForceCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
