package logic;

import util.FileLockUtil;

/**
 * ファイルロックを管理するクラス。
 */
public class FileLockLogic {

	/**
	 * ロックファイルパス名。
	 */
	private String lockFilename;

	/**
	 * ファイルロック情報。
	 */
	private FileLockUtil.LockInfo lock = new FileLockUtil.LockInfo();

	/**
	 * コンストラクタ。
	 * 
	 * @param lockFilename ロックファイルパス名。
	 */
	public FileLockLogic(String lockFilename) {
		this.lockFilename = lockFilename;
	}

	/**
	 * ファイルロックする。
	 */
	public void lockFile() {
		boolean bFirst = true;
		while (true) {
			if (FileLockUtil.lock(lock, false, lockFilename)) {
				break;
			}
			if (bFirst) {
				System.out.println("Waiting for other processes to release file.");
				bFirst = false;
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * ファイルロックを解放する。
	 */
	public void unlockFile() {
		FileLockUtil.unlock(lock);
	}

}
