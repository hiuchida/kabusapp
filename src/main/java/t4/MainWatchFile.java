package t4;

import java.io.File;
import java.util.List;

import util.FileUtil;

public class MainWatchFile {

	public static void main(String[] args) {
		new MainWatchFile().execute(args[0]);
	}

	public void execute(String filepath) {
		File f = new File(filepath);
		if (!f.exists()) {
			System.out.println("Not exist " + filepath);
			return;
		}
		List<String> lines = FileUtil.readAllLines(filepath);
		long lastModified = f.lastModified();
		long lastLen = f.length();
		for (String s : lines) {
			System.out.println(s);
		}
		System.out.println("lastModified=" + lastModified + ", lastLen=" + lastLen);
		while (true) {
			boolean bChange = false;
			long mod = f.lastModified();
			if (lastModified != mod) {
				long len = f.length();
				if (lastLen != len) {
					bChange = true;
				}
			}
			if (bChange) {
				lines = FileUtil.readNextLines(filepath, lastLen);
				lastModified = f.lastModified();
				lastLen = f.length();
				for (String s : lines) {
					System.out.println(s);
				}
				System.out.println("lastModified=" + lastModified + ", lastLen=" + lastLen);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
		}
	}

}
