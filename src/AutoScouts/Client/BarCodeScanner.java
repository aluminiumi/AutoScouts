package AutoScouts;

public class BarCodeScanner extends NumberReaderDevice {
	BarCodeScanner(Object caller) {
		super(caller, "barcode");
	}
	
	BarCodeScanner(Object caller, String name) {
		super(caller, name);
	}
}
