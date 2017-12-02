package AutoScouts;

public class ReceiptPrinter extends Printer {
	private final String header = ""+ 
		"########################################\n"+
		"##               Receipt              ##\n"+
		"##                                    ##\n";

	private final String footer = ""+
		"##                                    ##\n"+
		"########################################\n";

	private final String signature = "\n\n\nSignature: ____________\n";

	private final String customercopy = "            Customer Copy\n";

	private final String storecopy = "               Store Copy\n";

	ReceiptPrinter() {

	}

	public int print(CustomerOrder co) {
		return print(co.toString());
	}

	public int print(CustomerOrder co, int cardno, int authnum) {
		String headeradd = String.format("Card#:%-10s Auth#:%8s\n", cardno, authnum);
		print(customercopy+headeradd+co+signature);
		return print(storecopy+headeradd+co+signature);
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
