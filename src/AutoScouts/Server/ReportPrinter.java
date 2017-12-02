package AutoScouts;

public class ReportPrinter extends Printer {
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

		String header = "######################\n"+
				"# BEGIN DAILY REPORT #\n"+
				"######################\n";
		String footer = "\n######################\n"+
				"#  END DAILY REPORT  #\n"+
				"######################\n";
		//System.out.println("ReportPrinter: print(TransactionLog)");
		if(tl != null) {
			print(header+tl.toString()+footer);
		} else {
			print(header+"NO TRANSACTIONS"+footer);
		}
		return 0;
	}

	public int print(InventoryMessageReport imr) {
		String header = "##############################\n"+
				"# BEGIN INVENTORY MSG REPORT #\n"+
				"##############################\n";
		String footer = "\n##############################\n"+
				"#  END INVENTORY MSG REPORT  #\n"+
				"##############################\n";
		//System.out.println("ReportPrinter: print(InventoryMessageReport)");
		if(imr != null) {
			print(header+imr.toString()+footer);
		} else {
			print(header+"NO NEW INVENTORY MESSAGES"+footer);
		}
		return 0;
	}
}
