package AutoScouts;

import java.util.Scanner;

class NumberReaderDevice {
	Scanner reader;
	Object caller;
	String devicename;

	NumberReaderDevice(Object caller, String name) {
		reader = new Scanner(System.in);
		this.caller = caller;
		devicename = name;
	}

	public int scan() {
		return Integer.parseInt(reader.nextLine());
	}
	
	//This is needed when client is expecting inputs from
	//multiple sources, in uncertain order
	public void bufferedScan() {
		((ScannerHost)caller).receiveBufferedScan(devicename, Integer.parseInt(reader.nextLine()));
	}

	//Like bufferedScan(), but we are fed the input from
	//where we are sending it. This in effect allows pretending
	//to have multiple input devices when only one is available
	public void simulateBufferedScan(String in) {
		//System.out.println("simulateBufferedScan(): received "+in);
		((ScannerHost)caller).receiveBufferedScan(devicename, Integer.parseInt(in));
	}
}
