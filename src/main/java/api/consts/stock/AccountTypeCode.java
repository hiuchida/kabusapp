package api.consts.stock;

/**
 * 口座種別。
 * @see api.SendoderApi body.accountType:注文発注（株式）情報／口座種別。
 */
public enum AccountTypeCode {
	一般(2), 特定(4), 法人(12);

	private int id;

	private AccountTypeCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static AccountTypeCode valueOf(int id) {
		for (AccountTypeCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
