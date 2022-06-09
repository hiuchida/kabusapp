package api.consts;

/**
 * 取引区分コード。
 * @see api.OrdersApi cashmargin:取引区分
 */
public enum CashmarginCode {
	新規(2), 返済(3);

	private int id;

	private CashmarginCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static CashmarginCode valueOf(int id) {
		for (CashmarginCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
