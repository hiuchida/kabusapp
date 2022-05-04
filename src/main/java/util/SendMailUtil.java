package util;

import java.util.ArrayList;
import java.util.List;

/**
 * メール送信に関するユーティリティクラス。
 */
public class SendMailUtil {
	/**
	 * メール送信先。
	 */
	private static final String MAIL_TO = GlobalConfigUtil.get("MailTo");
	/**
	 * メール送信元。
	 */
	private static final String MAIL_FROM = GlobalConfigUtil.get("MailFrom");

	/**
	 * メール本文を保存したファイルパス。
	 */
	private String mailFilePath;

	/**
	 * 通知メール本文。
	 */
	private List<String> mailList = new ArrayList<>();

	/**
	 * コンストラクタ。
	 * 
	 * @param mailFilePath メール本文を保存したファイルパス。
	 */
	public SendMailUtil(String mailFilePath) {
		this.mailFilePath = mailFilePath;
	}

	/**
	 * メール本文を追加する。
	 * 
	 * @param msg メール本文。
	 */
	public void addLine(String msg) {
		mailList.add(msg);
	}

	/**
	 * メール本文ファイルを書き込む。
	 * 
	 * @param titlePrefix メールタイトル。
	 */
	public void writeMailFile(String titlePrefix) {
		if (mailList.size() <= 0) {
			return;
		}
		String title = "";
		String line = mailList.get(0);
		int idx1 = line.indexOf("trigger");
		if (idx1 >= 0) {
			int idx2 = line.indexOf(",", idx1);
			title = line.substring(idx1, idx2);
		} else {
			idx1 = line.indexOf("price");
			if (idx1 >= 0) {
				int idx2 = line.indexOf(",", idx1);
				title = line.substring(idx1, idx2);
			}
		}
		
		List<String> lines = new ArrayList<>();
		lines.add("To: " + MAIL_TO);
		lines.add("From: " + MAIL_FROM);
		lines.add("Subject: " + titlePrefix + " " + title);
		lines.add("");
		lines.add(DateTimeUtil.nowToString());
		lines.add("---");
		for (String s : mailList) {
			lines.add(s);
		}
		lines.add("---");
		FileUtil.writeAllLines(mailFilePath, lines);
	}

	/**
	 * メール本文ファイルを削除する。
	 */
	public void deleteMailFile() {
		FileUtil.deleteFile(mailFilePath);
	}

}
