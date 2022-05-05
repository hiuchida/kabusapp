package v13;

/**
 * 5分足、30分足の時刻を列挙するツール。
 */
public class MainPrintChartTime {

	public static void main(String[] args) {
		System.out.println("Day5");
		printDay5();
		System.out.println();
		System.out.println("Night5");
		printNight5();
		System.out.println();
		System.out.println("Day30");
		printDay30();
		System.out.println();
		System.out.println("Night30");
		printNight30();
		System.out.println();
	}

	private static void printDay5() {
		int hour = 8;
		int min = 45;
		for (int i = 0; i < 79 + 1; i++) { // 79 = 13 * 6 + 1
			String time = String.format("%02d:%02d:00", hour, min);
			System.out.print(time + " ");
			min += 5;
			if (min >= 60) {
				hour++;
				min -= 60;
				System.out.println();
			}
		}
		System.out.println();
	}

	private static void printNight5() {
		int hour = 16;
		int min = 30;
		for (int i = 0; i < 163 + 1; i++) { // 163 = 27 * 6 + 1
			String time = String.format("%02d:%02d:00", hour, min);
			System.out.print(time + " ");
			min += 5;
			if (min >= 60) {
				hour++;
				min -= 60;
				System.out.println();
			}
			if (hour >= 24) {
				hour -= 24;
			}
		}
		System.out.println();
	}

	private static void printDay30() {
		int hour = 8;
		int min = 45;
		for (int i = 0; i < 14 + 1; i++) {
			String time = String.format("%02d:%02d:00", hour, min);
			System.out.print(time + " ");
			min += 30;
			if (min >= 60) {
				hour++;
				min -= 60;
			}
		}
		System.out.println();
	}

	private static void printNight30() {
		int hour = 16;
		int min = 30;
		for (int i = 0; i < 28 + 1; i++) {
			String time = String.format("%02d:%02d:00", hour, min);
			System.out.print(time + " ");
			min += 30;
			if (min >= 60) {
				hour++;
				min -= 60;
			}
			if (hour >= 24) {
				hour -= 24;
				System.out.println();
			}
		}
		System.out.println();
	}

}
