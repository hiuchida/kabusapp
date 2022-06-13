package api.consts.stock;

/**
 * 資産区分（預り区分）。
 * @see api.SendoderApi body.fundType:注文発注（株式）情報／資産区分（預り区分）。
 */
public enum FundTypeCode {
	現物売("  "), 保護("02"), 信用代用("AA"), 信用取引("11");

	private String code;

	private FundTypeCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code;
	}

	public static FundTypeCode valueOfCode(String code) {
		for (FundTypeCode e : values()) {
			if (e.code.equals(code)) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant code=" + code);
	}

}
