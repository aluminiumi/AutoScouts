package AutoScouts;

import java.util.Scanner;

class CustomerUI extends ApplicationLayerClient implements ScannerHost {
	private CustomerOrder co;
	BarCodeScanner bs;
	Scanner kbd;
	int scannerInput;
	int screenInput;

	public static void main(String args[]) {
		System.out.println("CustomerUI");
		if(args.length == 0)
			new CustomerUI();
		else if(args.length == 1)
			new CustomerUI(args[0]);
		else
			new CustomerUI(args[0], Integer.parseInt(args[1]));
	}

	CustomerUI() {
		super();
		go();
	}

	CustomerUI(String destAddress) {
		super(destAddress);
		go();
	}
	
	CustomerUI(String destAddress, int portNum) {
		super(destAddress, portNum);
		go();
	}

	private void go() {
		//setQuietMode(true);
		bs = new BarCodeScanner(this);
		kbd = new Scanner(System.in);
		co = new CustomerOrder();
		boolean workToDo = true;
		while(workToDo) {
			initialScreen();
		}
	}

	private void clearScreen() {
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+
					"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+
					"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+
					"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}

	private void initialScreen() {
		targetItem = null;
		co = new CustomerOrder();
		clearScreen();
		System.out.println("Welcome!");
		System.out.println("------------");
		System.out.println();
		System.out.println("To Start Checkout, Press ENTER");
		System.out.println();
		String command = kbd.nextLine();
		PromptToScan();
	}

	private void printMenu(int i) {
		switch(i) {
			case 0:
				System.out.println("[1. CANCEL CHECKOUT] [2. SUBTOTAL] [3. TOTAL]");
				break;
			case 1:
				System.out.println("[1. CANCEL CHECKOUT]"); 
				System.out.println("[2. PAY BY CREDIT/DEBIT CARD] [3. PAY BY CASH]");
				break;
			case 2:
				System.out.println("[1. CANCEL CHECKOUT] [2. CANCEL PAYMENT]");
				break;
			default:
				break;
		}
	}

	//called by PromptToScan()
	//This method allows us to pretend we have multiple input devices
	private void simulateMultipleDevices() {
		String input = kbd.nextLine();
		if(input.startsWith("scan ")) {
			String stripped = input.substring(input.indexOf("scan ")+5, input.length());
			System.out.println(stripped);
			bs.simulateBufferedScan(stripped); 
			//expect result back in scannerInput
		} else {
			screenInput = Integer.parseInt(input);
		}
	}

	private void PromptToScan() {
		boolean cancelPressed = false;
		boolean totalPressed = false;
		boolean subtotalPressed = false;
		String specialMessage = "";
		while(!cancelPressed && !totalPressed) {
			clearScreen();
			System.out.println("Please scan items.");
			System.out.println("(Note: In lieu of actual scanner device, precede input");
			System.out.println(" with 'scan ' to simulate barcode scanning)");
			System.out.println();
			String price = "";
			for(InventoryItem item : co.getOrder()) {
				price = String.format("%1$,.2f", item.getPriceWithDiscount());
				System.out.format("%-40s%10s\n", item.getName(), price);
			}
			System.out.println();
			if(subtotalPressed) {
				PrintSubtotal();
				System.out.println();
				subtotalPressed = false;
			}
			if(!(specialMessage == null) && !specialMessage.equals("")) {
				System.out.println(specialMessage);
				specialMessage = "";
			}
			printMenu(0);
			try {
				simulateMultipleDevices(); //necessary when physical barcode scanner absent

				//check for barcode scanner input
				if(scannerInput != 0) { //scanner input happened
					if(processBarcodeScannedInput(scannerInput) != 0) {
						//error happened
						specialMessage = "Unrecognized item. See store associate.\n";
					}
					scannerInput = 0;
				}
				//check for button/key presses
				if(screenInput != 0) { //screen input happened
					switch(screenInput) {
						case 1: //cancel checkout pressed
							cancelPressed = true;
							break;
						case 2: //subtotal pressed
							subtotalPressed = true;
							break;
						case 3: //total pressed
							totalPressed = true;
							break;
					}
					screenInput = 0;
				}
				Thread.sleep(150);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		if(cancelPressed) { 
			return; //return to the welcome screen
		} else if(totalPressed) {
			PrintSubtotal();
			PromptForPaymentType();
		}

	}

	//This method is called by PromptToScan() every time a product is 
	//scanned by the barcode scanner
	private int processBarcodeScannedInput(int scannerInput) {
		if(itemExists(scannerInput)) {
			co.add(targetItem);
			return 0;
		} else {
			return 1; //flag error to caller
		}
	}

	private void PrintSubtotal() {
		String subtotal = String.format("%1$,.2f",co.getSubtotal());
		String tax = String.format("%1$,.2f",co.getTaxTotal());
		String total = String.format("%1$,.2f",co.getTotal());
		System.out.println();
		System.out.format("%-9s:%8s\n", "Subtotal", subtotal);
		System.out.format("%-9s:%8s\n", "Tax", tax);
		System.out.format("%-9s:%8s\n", "Total", total);
		System.out.println();
	}

	private void PromptForPaymentType() {
		//TODO: implement
		System.out.println("Select a payment method.");
		printMenu(1);
		int input;
		try {
			String input = kbd.nextLine();
			input = Integer.parseInt(input);
		} catch (Exception e) {
			System.out.println(e);
			PromptForPaymentType();
			return;
		}
		switch(input) {
			case 1: //cancel checkout
				return; //return to welcome screen
			case 2: //pay by card
				PayByCardScreen();
				break;
			case 3: //pay by cash
				PayByCashScreen();
				break;
			default:
				PromptForPaymentType();
				return;
		}
	}

	private void PayByCardScreen() {
		//TODO: implement
		printMenu(2);
	}

	private void PayByCashScreen() {
		//TODO: implement
		printMenu(2);
	}

	/*
	protected void receive(String message) {
		System.out.println("Server: "+message);
		String chunks[] = message.split(" ");
		switch(chunks[0]) {
			case "item":
				if(expectingItem) {
					try{
						int len = chunks.length;
						int id = Integer.parseInt(chunks[1]);
						double price = Double.parseDouble(chunks[len-4]);
						double discount = Double.parseDouble(chunks[len-3]);
						String desc = chunks[2];
						int iter = 3;
						while(len > 7) {
							desc += " "+chunks[iter];
							len--;
							iter++;
						}
						InventoryItem item = new InventoryItem(
							id, desc, price, discount, 0, 0);
						co.add(item);
						System.out.println("Item scanned: "+item);
						System.out.println("New subtotal: "+String.format("%1$,.2f", co.getSubtotal()));

					} catch (Exception e) {
						System.out.println("Server sent malformed item information. "+e);
					}
					expectingItem = false;
				} else {
					System.out.println("Server sent unexpected item information.");
				}
				break;
			case "Hello.":
			case "Okay.":
				break;
			default:
				System.out.println("Unexpected response from server.");
		}
	}*/
/*
	protected void send(String message) {
		if(message.startsWith("getitem "))
			expectingItem = true;
		super.send(message);
	}
*/
	private void ScanItem(int i) {
		send("getitem "+i);
		expectingItem = true;
	}

	public void ReportToTransManager() {
		for(InventoryItem item : co.getOrder()) {
			send("sold "+item.getId());
		}
	}

	//satisfies ScannerHost interface
	//buffers inputs from physical scanner device
	//useful when multiple input devices are present
	public void receiveBufferedScan(int in) {
		scannerInput = in;
		//System.out.println("Scanned: "+in);
	}
}
