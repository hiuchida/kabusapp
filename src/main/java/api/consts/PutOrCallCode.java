package api.consts;

/**
 * コール or プットコード。
 * @see api.SymbolNameApi putOrCall:コール or プット
 */
public enum PutOrCallCode {
	PUT("P"), CALL("C");

	private String code;

	private PutOrCallCode(String code) {
		this.code = code;
	}

	public String codeValue() {
		return code;
	}

	public static PutOrCallCode valueOfCode(String code) {
		for (PutOrCallCode e : values()) {
			if (e.code.equals(code)) {
				return e;
			}
		}
		throw new IllegalArgumentException("No enum constant code=" + code);
	}

}
