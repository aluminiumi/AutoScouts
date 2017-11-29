package AutoScouts;

class ReportPrinter extends Printer {
	ReportPrinter() {

	}

	public int print(String s) {
		System.out.println("ReportPrinter: print()");

		//In a real setting, would forward to printer device
		//Instead, we just print to screen.
		System.out.println(s);
		return 0;
	}

	public int print(TransactionLog tl) {
		System.out.println("ReportPrinter: print(TransactionLog)");
		print(tl.toString());
		return 0;
	}

	public int print(InventoryMessageReport imr) {
		System.out.println("ReportPrinter: print(InventoryMessageReport)");
		print(imr.toString());
		return 0;
	}
}
