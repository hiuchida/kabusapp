package v9;

import util.FileUtil;

/**
 * 注文発注に関する設定情報を管理する。
 */
public class SendOrderConfig_r3 {
	/**
	 * 基準パス。
	 */
	private static final String DIRPATH = "/tmp/";
	/**
	 * 1行目に注文パスワードを保存したファイルパス。事前に準備する。
	 */
	private static final String PWD_FILEPATH = DIRPATH + "SendOrderConfig.pwd";

	/**
	 * シングルトンインスタンス。
	 */
	private static SendOrderConfig_r3 singleton = new SendOrderConfig_r3();

	/**
	 * シングルトンインスタンスを取得する。
	 * @return シングルトンインスタンス。
	 */
	public static SendOrderConfig_r3 getInstance() {
		return singleton;
	}

	/**
	 * 注文パスワードを取得する。
	 * @return 注文パスワード。
	 */
	public static String getPassword() {
		return singleton.initPassword();
	}

	private SendOrderConfig_r3() {
	}

	/**
	 * 設定ファイルを読み込んで、注文パスワードを取得する。
	 * @return 注文パスワード。
	 */
	private String initPassword() {
		String pwd = FileUtil.readOneLine(PWD_FILEPATH);
		return pwd;
	}

}
