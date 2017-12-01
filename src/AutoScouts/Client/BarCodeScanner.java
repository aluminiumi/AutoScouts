package AutoScouts;

import java.util.Scanner;

class BarCodeScanner {
	Scanner reader;

	BarCodeScanner() {
		reader = new Scanner(System.in);
	}

	public int scan() {
		return Integer.parseInt(reader.nextLine());
	}
}
