package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * ファイルに関するユーティリティクラス。
 */
public class FileUtil {

	/**
	 * エンコーディング：UTF-8。
	 */
	public static final String UTF8 = "UTF-8";

	/**
	 * ファイル単体を削除する。
	 * 
	 * @param file ファイルパス。
	 */
	public static void deleteFile(String file) {
		File f = new File(file);
		f.delete();
	}

	/**
	 * 入力ファイルを開く。
	 * 
	 * @param filepath ファイルパス。
	 * @param encoding エンコーディング。
	 * @return 入力ファイル。
	 * @throws IOException 例外。
	 */
	public static BufferedReader reader(String filepath, String encoding) throws IOException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(filepath), encoding));
	}

	/**
	 * 出力ファイルを開く。
	 * 
	 * @param filepath ファイルパス。
	 * @param encoding エンコーディング。
	 * @return 出力ファイル。
	 * @throws IOException 例外。
	 */
	public static PrintWriter writer(String filepath, String encoding) throws IOException {
		return new PrintWriter(new OutputStreamWriter(new FileOutputStream(filepath), encoding));
	}

	/**
	 * 出力ファイルを開く。
	 * 
	 * @param filepath ファイルパス。
	 * @param encoding エンコーディング。
	 * @param bAppend  true:追記、false:上書き
	 * @return 出力ファイル。
	 * @throws IOException 例外。
	 */
	public static PrintWriter writer(String filepath, String encoding, boolean bAppend) throws IOException {
		return new PrintWriter(new OutputStreamWriter(new FileOutputStream(filepath, bAppend), encoding));
	}

	/**
	 * ファイルの先頭1行を取得する。
	 * 
	 * @param filepath ファイルパス。
	 * @return ファイルの先頭1行の文字列。ファイルが存在しない場合は空文字列を返す。
	 */
	public static String readOneLine(String filepath) {
		if (!new File(filepath).exists()) {
			return "";
		}
		try (BufferedReader br = reader(filepath, FileUtil.UTF8)) {
			String line = br.readLine();
			if (line == null) {
				return "";
			}
			return line;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * ファイルの先頭1行を保存する。
	 * 
	 * @param filepath ファイルパス。
	 * @param line     先頭1行の文字列。
	 */
	public static void writeOneLine(String filepath, String line) {
		try (PrintWriter pw = writer(filepath, FileUtil.UTF8)) {
			pw.print(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ファイルのすべての行を取得する。
	 * 
	 * @param filepath ファイルパス。
	 * @return ファイルのすべての行のリスト。ファイルが存在しない場合は0件のリストを返す。
	 */
	public static List<String> readAllLines(String filepath) {
		List<String> list = new ArrayList<>();
		if (!new File(filepath).exists()) {
			return list;
		}
		try (BufferedReader br = reader(filepath, FileUtil.UTF8)) {
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ファイルのすべての行を保存する。
	 * 
	 * @param filepath ファイルパス。
	 * @param lines    ファイルのすべての行のリスト。
	 */
	public static void writeAllLines(String filepath, List<String> lines) {
		try (PrintWriter pw = writer(filepath, FileUtil.UTF8)) {
			for (String s : lines) {
				pw.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ログファイルに追記する。
	 * 
	 * @param filepath ログファイルパス。
	 * @param method   メソッド名。
	 * @param msg      メッセージ文字列。
	 */
	public static void printLog(String filepath, String method, String msg) {
		try (PrintWriter pw = FileUtil.writer(filepath, FileUtil.UTF8, true)) {
			printLogLine(pw, method + "(): " + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ログ行を追記する。
	 * 
	 * @param pw  出力ファイル。
	 * @param msg メッセージ文字列。
	 */
	public static void printLogLine(PrintWriter pw, String msg) {
		String now = DateTimeUtil.nowToString();
		pw.println(now + " " + msg);
	}

	private FileUtil() {
	}

}
