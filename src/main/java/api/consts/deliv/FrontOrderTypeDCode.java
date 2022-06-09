package api.consts.deliv;

/**
 * 執行条件コード。
 * 
 * @see api.SendoderFutureApi body.side:注文発注（先物）情報／執行条件。
 * @see api.SendoderOptionApi body.side:注文発注（オプション）情報／執行条件。
 */
public enum FrontOrderTypeDCode {
	引成_派生(18), 指値(20), 引指_派生(28), 逆指値(30), 成行(120);

	private int id;

	private FrontOrderTypeDCode(int id) {
		this.id = id;
	}

	public int intValue() {
		return id;
	}

	public static FrontOrderTypeDCode valueOf(int id) {
		for (FrontOrderTypeDCode e : values()) {
			if (e.id == id) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant id=" + id);
	}

}
