package api.consts;

/**
 * 先物コード。
 * @see api.SymbolNameApi futureCode:先物コード
 */
public enum FutureCode {
	日経平均先物("NK225"), 日経225mini先物("NK225mini"), TOPIX先物("TOPIX"), ミニTOPIX先物("TOPIXmini"), 東証マザーズ先物("MOTHERS"),
	JPX日経400先物("JPX400"), NYダウ先物("DOW"), 日経平均VI先物("VI"), TOPIXCore30先物("Core30"), 東証REIT指数先物("REIT");

	private String code;

	private FutureCode(String code) {
		this.code = code;
	}

	public String codeValue() {
		return code;
	}

	public static FutureCode valueOfCode(String code) {
		for (FutureCode e : values()) {
			if (e.code.equals(code)) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant code=" + code);
	}

}
