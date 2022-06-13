package api.consts;

/**
 * 以上／以下コード。
 * 
 * @see api.SendoderApi body.reverseLimitOrder.side:注文発注（株式）情報／以上／以下。
 * @see api.SendoderFutureApi body.reverseLimitOrder.side:注文発注（先物）情報／以上／以下。
 * @see api.SendoderOptionApi body.reverseLimitOrder.side:注文発注（オプション）情報／以上／以下。
 */
public enum UnderOverCode {
	以下(1), 以上(2);

	private int id;

	private UnderOverCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	@Override
	public String toString() {
		return Integer.toString(id);
	}

	public static UnderOverCode valueOf(int id) {
		for (UnderOverCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
