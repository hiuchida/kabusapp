package util;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

/**
 * ファイルロックに関するユーティリティクラス。
 */
public class FileLockUtil {

	/**
	 * ファイルロック情報
	 */
	public static class LockInfo {
		public FileChannel fc;
		public FileLock fl;
	}

	/**
	 * オープンオプションを取得する。
	 * 
	 * @param isShared true:共有ロック、false:排他ロック
	 * @return オープンオプション
	 */
	private static OpenOption[] getOpenOption(boolean isShared) {
		OpenOption[] options;
		OpenOption[] sharedOptions = { StandardOpenOption.CREATE, StandardOpenOption.READ };
		OpenOption[] exclusiveOptions = { StandardOpenOption.CREATE, StandardOpenOption.READ,
				StandardOpenOption.WRITE };
		if (isShared) {
			options = sharedOptions;
		} else {
			options = exclusiveOptions;
		}
		return options;
	}

	/**
	 * ファイルをロックする。
	 * 
	 * @param lockInfo ファイルロック情報
	 * @param isShared true:共有ロック、false:排他ロック
	 * @param lockPath ファイルパス
	 * @return true:成功、false:失敗
	 */
	public static boolean lock(LockInfo lockInfo, boolean isShared, String lockPath) {
		OpenOption[] options = getOpenOption(isShared);
		try {
			File lockFile = new File(lockPath);
			FileChannel fc = FileChannel.open(lockFile.toPath(), options);
			FileLock fl = fc.tryLock(0L, Long.MAX_VALUE, isShared);
			if (fl != null) {
				lockInfo.fc = fc;
				lockInfo.fl = fl;
				return true;
			}
			fc.close();
		} catch (IOException e) {
//			e.printStackTrace();
		}
		lockInfo.fc = null;
		lockInfo.fl = null;
		return false;
	}

	/**
	 * ファイルをアンロックする。
	 * 
	 * @param lockInfo ファイルロック情報
	 */
	public static void unlock(LockInfo lockInfo) {
		try {
			if (lockInfo.fl != null) {
				lockInfo.fl.release();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (lockInfo.fc != null) {
				lockInfo.fc.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private FileLockUtil() {
	}

}
