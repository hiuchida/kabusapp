package api.consts.stock;

/**
 * 執行条件コード。
 * 
 * @see api.SendoderApi body.side:注文発注（株式）情報／執行条件。
 */
public enum FrontOrderTypeSCode {
	成行(10), 寄成_前場(13), 寄成_後場(14), 引成_前場(15), 引成_後場(16), IOC成行(17), 指値(20), 寄指_前場(21), 寄指_後場(22), 引指_前場(23), 引指_後場(24),
	不成_前場(25), 不成_後場(26), IOC指値(27), 逆指値(30);

	private int id;

	private FrontOrderTypeSCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static FrontOrderTypeSCode valueOf(int id) {
		for (FrontOrderTypeSCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
