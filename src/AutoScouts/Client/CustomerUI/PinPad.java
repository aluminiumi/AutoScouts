package AutoScouts;

public class PinPad extends NumberReaderDevice {
	PinPad(Object caller) {
		super(caller, "pinpad");
	}

	PinPad(Object caller, String name) {
		super(caller, name);
	}
}
