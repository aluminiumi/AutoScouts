package AutoScouts;

class ReceiptPrinter extends Printer {
	private String header = ""+ 
		"########################################\n"+
		"##               Receipt              ##\n"+
		"##                                    ##\n";

	private String footer = ""+
		"##                                    ##\n"+
		"########################################\n";

	ReceiptPrinter() {

	}

	public int print(CustomerOrder co) {
		return print(co.toString());
	}

	public int print(String s) {
		System.out.println("print()");
		System.out.print(header);
		String chunks[] = s.split("\n");
		for(String line : chunks) {
			System.out.format("## %34s ##\n", line);
		}
		System.out.println(footer);
		return 0;
	}
}
