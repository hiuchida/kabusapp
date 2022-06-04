package a1;

/**
 * 重複した値の二分探索
 */
public class MainBinarySearch {

	public static void main(String[] args) {
		int[] ia1 = { 10, 20, 30, 40, 50 };
		search(ia1, 5, 55, 5);
		int[] ia2 = { 50, 50, 50, 50, 90 };
		search(ia2, 30, 110, 20);
	}

	private static void search(int[] a, int start, int end, int step) {
		for (int i = start; i <= end; i += step) {
			int rc = binarySearch(a, i);
			System.out.println(i + " " + rc);
		}
		System.out.println();
		for (int i = start; i <= end; i += step) {
			int rc = binarySearch_r2(a, i);
			System.out.println(i + " " + rc);
		}
		System.out.println();
	}

	/**
	 * @see java.util.Arrays.binarySearch
	 */
	public static int binarySearch(int[] a, int key) {
		return binarySearch0(a, 0, a.length, key);
	}

	/**
	 * @see java.util.Arrays.binarySearch0
	 */
	private static int binarySearch0(int[] a, int fromIndex, int toIndex, int key) {
		int low = fromIndex;
		int high = toIndex - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			int midVal = a[mid];

			if (midVal < key)
				low = mid + 1;
			else if (midVal > key)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}

	/**
	 * 重複した値の二分探索
	 */
	public static int binarySearch_r2(int[] a, int key) {
		return binarySearch0_r2(a, 0, a.length, key);
	}

	/**
	 * 重複した値の二分探索
	 */
	private static int binarySearch0_r2(int[] a, int fromIndex, int toIndex, int key) {
		int low = fromIndex;
		int high = toIndex;

		while (low < high) {
			int mid = (low + high) >>> 1;
			int midVal = a[mid];

			if (midVal < key)
				low = mid + 1;
			else
				high = mid;
		}
		if (low < toIndex && a[low] == key)
			return low;
		return -(low + 1); // key not found.
	}

}
