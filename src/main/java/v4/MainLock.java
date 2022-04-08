package v4;

import java.util.Scanner;

import io.swagger.client.ApiException;

/**
 * ログインを行い、キー入力されるまでファイルロックする。
 */
public class MainLock {
	public static void main(String[] args) throws ApiException {
		LockedAuthorizedToken.lockToken();
		try {
			Scanner sc = new Scanner(System.in);
			System.out.println("File Locking...");
			System.out.println("Press the ENTER key when you're ready.");
			sc.nextLine();
			System.out.println("File Unlocked.");
		} finally {
			LockedAuthorizedToken.unlockToken();
		}
	}
}
