package AutoScouts;

class BillAcceptor extends NumberReaderDevice {
	BillAcceptor(Object caller) {
		super(caller, "bill");
	}

	BillAcceptor(Object caller, String name) {
		super(caller, name);
	}
}
