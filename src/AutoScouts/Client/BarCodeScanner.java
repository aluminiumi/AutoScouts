package AutoScouts;

import java.util.Scanner;

class BarCodeScanner {
	Scanner reader;
	Object caller;

	BarCodeScanner(Object caller) {
		reader = new Scanner(System.in);
		this.caller = caller;
	}

	public int scan() {
		return Integer.parseInt(reader.nextLine());
	}
	
	//This is needed when client is expecting inputs from
	//multiple sources, in uncertain order
	public void bufferedScan() {
		((ScannerHost)caller).receiveBufferedScan(Integer.parseInt(reader.nextLine()));
	}

	//Like bufferedScan(), but we are fed the input from
	//where we are sending it. This in effect allows pretending
	//to have multiple input devices when only one is available
	public void simulateBufferedScan(String in) {
		//System.out.println("simulateBufferedScan(): received "+in);
		((ScannerHost)caller).receiveBufferedScan(Integer.parseInt(in));
	}
}
