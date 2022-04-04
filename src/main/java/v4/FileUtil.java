package v4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * ファイルに関するユーティリティクラス。
 */
public class FileUtil {

	/**
	 * エンコーディング：UTF-8。
	 */
	public static final String UTF8 = "UTF-8";

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
	 * @param line 先頭1行の文字列。
	 */
	public static void writeOneLine(String filepath, String line) {
		try (PrintWriter pw = writer(filepath, FileUtil.UTF8)) {
			pw.print(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private FileUtil() {
	}

}
