package api.consts;

/**
 * 売買区分コード。
 * @see api.OrdersApi side:売買区分
 * @see api.PositionsApi side:売買区分
 * @see api.SendoderApi body.side:注文発注（株式）情報／売買区分。
 * @see api.SendoderFutureApi body.side:注文発注（先物）情報／売買区分。
 * @see api.SendoderOptionApi body.side:注文発注（オプション）情報／売買区分。
 */
public enum SideCode {
	売(1), 買(2);

	private int id;

	private SideCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static SideCode valueOf(int id) {
		for (SideCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
