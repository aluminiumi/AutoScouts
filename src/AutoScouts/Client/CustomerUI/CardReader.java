package AutoScouts;

public class CardReader extends NumberReaderDevice {
	CardReader(Object caller) {
		super(caller, "card");
	}

	CardReader(Object caller, String name) {
		super(caller, name);
	}

	public void ejectCard() {
		System.out.println("Ejecting card...");
	}

	public void simulateBufferedScanLong(String in) {
		((ScannerHost)caller).receiveBufferedScanLong(devicename, Long.parseLong(in));
	}

}
