package AutoScouts;

import java.util.Scanner;

class CustomerUI extends ApplicationLayerClient implements ScannerHost {
	private CustomerOrder co;
	private ReceiptPrinter rp;
	private CashDispenser cd;
	private BarCodeScanner bs;
	private PinPad pp;
	private CardReader cr;
	private CoinAcceptor ca;
	private BillAcceptor ba;
	private Scanner kbd;
	private int screenInput;
	private int barcodeInput;
	private int billInput;
	private int coinInput;
	private int cardInput;
	private int pinpadInput;
	private double cashValueInserted = 0;

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
		rp = new ReceiptPrinter();
		cd = new CashDispenser();
		bs = new BarCodeScanner(this);
		pp = new PinPad(this);
		cr = new CardReader(this);
		ca = new CoinAcceptor(this);
		ba = new BillAcceptor(this);
		kbd = new Scanner(System.in);
		co = new CustomerOrder();
		boolean workToDo = true;
		while(workToDo) {
			initialScreen();
		}
	}

	private void clearScreen() {
		System.out.println(
			"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+
			"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+
			"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"+
			"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}

	private void initialScreen() {
		targetItem = null;
		cashValueInserted = 0;
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
			bs.simulateBufferedScan(stripped); 
			//expect result back in barcodeInput
		} else if(input.startsWith("bill ")) {
			String stripped = input.substring(input.indexOf("bill ")+5, input.length());
			ba.simulateBufferedScan(stripped); 
			//expect result back in billInput
		} else if(input.startsWith("coin ")) {
			String stripped = input.substring(input.indexOf("coin ")+5, input.length());
			ca.simulateBufferedScan(stripped); 
			//expect result back in coinInput
		} else if(input.startsWith("card ")) {
			String stripped = input.substring(input.indexOf("card ")+5, input.length());
			cr.simulateBufferedScan(stripped); 
			//expect result back in cardInput
		} else if(input.startsWith("pin ")) {
			String stripped = input.substring(input.indexOf("pin ")+4, input.length());
			pp.simulateBufferedScan(stripped); 
			//expect result back in pinpadInput
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
				if(barcodeInput != 0) { //scanner input happened
					if(processBarcodeScannedInput(barcodeInput) != 0) {
						//error happened
						specialMessage = "Unrecognized item. See store associate.\n";
					}
					barcodeInput = 0;
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
						default:
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
			PromptForPaymentType();
		}

	}

	//This method is called by PromptToScan() every time a product is 
	//scanned by the barcode scanner
	private int processBarcodeScannedInput(int input) {
		if(itemExists(input)) {
			co.add(targetItem);
			return 0;
		} else {
			return 1; //flag error to caller
		}
	}

	//This method is called by PromptToScan() every time a product is 
	//scanned by the barcode scanner
	private int processCoinInput(int input) {
		switch(input) {
			case 1:
				cd.addPenny();
				cashValueInserted+=.01;
				break;
			case 5:
				cd.addNickel();
				cashValueInserted+=.05;
				break;
			case 10:
				cd.addDime();
				cashValueInserted+=.1;
				break;
			case 25:
				cd.addQuarter();
				cashValueInserted+=.25;
				break;
			default:
				return 1; //flag error to caller
		}
		return 0;
	}

	//This method is called by PromptToScan() every time a product is 
	//scanned by the barcode scanner
	private int processBillInput(int input) {
		switch(input) {
			case 1:
				cd.addOne();
				cashValueInserted+=1;
				break;
			case 5:
				cd.addFive();
				cashValueInserted+=5;
				break;
			case 10:
				cd.addTen();
				cashValueInserted+=10;
				break;
			case 20:
				cd.addTwenty();
				cashValueInserted+=20;
				break;
			case 50:
				cd.addFifty();
				cashValueInserted+=50;
				break;
			case 100:
				cd.addHundred();
				cashValueInserted+=100;
				break;
			default:
				return 1; //flag error to caller
		}
		return 0;
	
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
		clearScreen();
		PrintSubtotal();
		System.out.println("Select a payment method.");
		printMenu(1);
		int input;
		try {
			String kbdinput = kbd.nextLine();
			input = Integer.parseInt(kbdinput);
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
		boolean cancelCheckoutPressed = false;
		boolean cancelPaymentPressed = false;
		boolean cashSufficient = false;
		String specialMessage = "";
		while(!cancelCheckoutPressed && !cancelPaymentPressed && !cashSufficient) {
			clearScreen();
			if(cashValueInserted < co.getTotal()) {
				System.out.println("Insert cash now.");
				System.out.println("(Note: In lieu of actual devices, precede input");
				System.out.println(" with 'bill ' to simulate inputting a bill or");
				System.out.println(" with 'coin ' to simulate inputting a coin.)");
				System.out.println("(Valid values for bills: 1, 5, 10, 20, 50, 100)");
				System.out.println("(Valid values for coins: 1, 5, 10, 25)");
				System.out.println();
				PrintSubtotal();
				System.out.println();
				System.out.println("Amount inserted: "+String.format("%1$,.2f",cashValueInserted));
				System.out.println("Amount remaining: "+String.format("%1$,.2f",co.getTotal()-cashValueInserted));
				System.out.println();
				if(!(specialMessage == null) && !specialMessage.equals("")) {
					System.out.println(specialMessage);
					specialMessage = "";
				}
				printMenu(2);
				try {
					simulateMultipleDevices(); //necessary when physical barcode scanner absent
	
					//check for bill scanner input
					if(billInput != 0) { //bill inserted
						if(processBillInput(billInput) != 0) {
							//error happened
							specialMessage = "Unrecognized bill..\n";
						}
						billInput = 0;
					}
	
					//check for coin scanner input
					if(coinInput != 0) { //coin inserted
						if(processCoinInput(coinInput) != 0) {
							//error happened
							specialMessage = "Unrecognized coin.\n";
						}
						coinInput = 0;
					}
	
					//check for button/key presses
					if(screenInput != 0) { //screen input happened
						switch(screenInput) {
							case 1: //cancel checkout pressed
								cancelCheckoutPressed = true;
								break;
							case 2: //subtotal pressed
								cancelPaymentPressed = true;
								break;
							default:
								break;
						}
						screenInput = 0;
					}
					Thread.sleep(150);
				} catch (Exception e) {
					System.out.println(e);
				}
			} else {
				cashSufficient = true;
			}
		}
		if(cancelCheckoutPressed) { 
			return; //return to the welcome screen
		}
		if(cancelPaymentPressed) {
			PromptForPaymentType(); //return to payment type prompt
			return;
		}
		if(cashSufficient) {
			clearScreen();
			try {
				if(cashValueInserted > co.getTotal()) { //dispense change
					System.out.println("Change is being dispensed...");
					if(cd.dispense(cashValueInserted-co.getTotal()) != 0) {
						System.out.println("Unable to make change. See store associate.");
						Thread.sleep(5000);
					}
				}
				System.out.println("Printing receipt...");
				Thread.sleep(2000);
				rp.print(co); //print receipt

				Thread.sleep(11000);
				clearScreen();
				System.out.println("Please take your receipt.");
				Thread.sleep(7000);
				ReportToTransManager();
				
			} catch (Exception e) {
				System.out.println(e);
			}
			return; //return to welcome screen
		}
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

	public void ReportToTransManager() {
		for(InventoryItem item : co.getOrder()) {
			send("sold "+item.getId());
		}
	}

	//Satisfies ScannerHost interface
	//Buffers inputs from physical scanner devices
	//Useful when multiple input devices are present
	public void receiveBufferedScan(String device, int in) {
		switch(device) {
			case "barcode":
				barcodeInput = in;
				break;
			case "bill":
				billInput = in;
				break;
			case "coin":
				coinInput = in;
				break;
			case "card":
				cardInput = in;
				break;
			case "pinpad":
				pinpadInput = in;
				break;
			default:
				break;
		}
	}
}
