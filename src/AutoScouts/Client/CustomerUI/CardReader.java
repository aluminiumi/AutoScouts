package AutoScouts;

class CardReader extends NumberReaderDevice {
	CardReader(Object caller) {
		super(caller, "card");
	}

	CardReader(Object caller, String name) {
		super(caller, name);
	}
}
