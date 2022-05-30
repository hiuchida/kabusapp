package t3;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class MainReadPart {

	public static void main(String[] args) throws IOException {
		new MainReadPart().execute(args[0]);
	}

	public void execute(String filepath) throws IOException {
		try (RandomAccessFile raf = new RandomAccessFile(new File(filepath), "r")) {
			long startPointer = 0;
			while (true) {
				LongReference lr = new LongReference();
				List<String> lines = readPartLines(lr, raf, startPointer, 10);
				if (lines.size() <= 0) {
					System.out.printf("%6d,%2d,%s\n", startPointer, 0, "EOF");
					break;
				}
				System.out.printf("%6d,%2d,%s\n", startPointer, lines.size(), lines.get(0));
				startPointer = lr.val;
			}
		} finally {
		}
	}

	/**
	 * 指定したオフセットから指定した行を読み込む。
	 * 
	 * @param lr           読み込んだ後のファイルオフセットを返す。
	 * @param raf          ランダムアクセスファイル。
	 * @param startPointer ファイルオフセット。
	 * @param maxCnt       最大行数。
	 * @return 読み込んだ行のリスト。
	 * @throws IOException
	 */
	public List<String> readPartLines(LongReference lr, RandomAccessFile raf, long startPointer, int maxCnt) throws IOException {
		long lastPointer = startPointer;
		List<String> lines = new ArrayList<>();
		raf.seek(startPointer);
//		System.out.println("startPointer=" + startPointer);
		for (int i = 0; i < maxCnt; i++) {
			String line = raf.readLine();
			if (line == null) {
				break;
			}
			line = new String(line.getBytes("ISO-8859-1"), "UTF-8");
			lines.add(line);
			lastPointer = raf.getFilePointer();
//			System.out.println("lastPointer[" + i + "]=" + lastPointer);
		}
		lr.val = lastPointer;
		return lines;
	}

}
