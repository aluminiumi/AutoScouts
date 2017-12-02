package AutoScouts;

import java.util.Scanner;

public class CustomerUI extends ApplicationLayerClient implements ScannerHost {
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
	private long cardInput;
	private int pinpadInput;
	private double cashValueInserted = 0;
	private int authResult = 0;

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
		authResult = 0;
		wipeInputs();
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
			case 3: System.out.println("[1. YES] [2. NO]");
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
			cr.simulateBufferedScanLong(stripped); 
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
		wipeInputs();
		boolean cancelPressed = false;
		boolean totalPressed = false;
		boolean subtotalPressed = false;
		String specialMessage = "";
		while(!cancelPressed && !totalPressed) {
			clearScreen();
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
			System.out.println("\nPlease scan items.");
			System.out.println();
			printMenu(0);
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
			sleep(150);
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

	private int processCardInput(long input) {
		if(input > 0) {
			return 0;
		}
		return 1; //error
	}

  	//Assumption: card type can be deduced from the card #
	//first digit of card < 5 is debit
	//first digit of card >= 5 is credit
	//0 represents debit cards, 1 credit.
	private int identifyCardType(long card) {
		int firstdigit = Math.floor(card / Math.pow(10, 16));
		if(firstdigit < 5) //debit
			return 0;
		return 1; //credit
	}

	//private boolean checkPinValidity(int pin) {
	//	return pin < 10000;
	//}
  
	//Assumption: the pin must be 4 digits and pins padded with 0's are represented by int without the 0's,
	//eg. if PIN inputted was "0037", pin variable will be integer "37".
	private int processPinInput(int input) {
		if(input >= 0 && input <= 9999) { //ensure 4-digit PIN
			return 0;
		}
		return 1; //error
	}

	private void processAuthResult(String chunks[]) {
		if(chunks[1].equals("ok")) {
			authResult = Integer.parseInt(chunks[2]);
		} else {
			switch(chunks[2]) {
				case "insuff":
					authResult = -1;
				case "badpin":
					authResult = -2;
				case "norecog":
					authResult = -3;
				case "deactiv":
					authResult = -4;
				
			}
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
		clearScreen();
		wipeInputs();
		PrintSubtotal();
		System.out.println("Select a payment method.\n");
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


	//Assumtion: Every cards has 16 digits and card # padded with 0's are represented by int without the 0's
	//eg. if card Number was "0000 0012 3456 7890", the card variable will be long "1234567890"
	private boolean cardIsValid(long card) {
		int firstdigit = Math.floor(card / Math.pow(10, 16));
		if(firstdigit != 0) 
			return (card < 10000000000000000 && card > 999999999999999);
		else
			return (card < 10000000000000000);
	}

  /*
	private void PayByCardScreen() {
		//TODO: implement

		boolean cancelCheckoutPressed = false;
		boolean cancelPaymentPressed = false;
		boolean isValidCard = false;
		boolean isValidPin = false;

		long cardNo = -1;
		int pin = -1;
		int cardType = -1;
		int authNo = -1;

		double total = co.getSubtotal();

		do {

			//insert card to read until valid
			while(!isValidCard) {
				System.out.println("Insert the card");
				System.out.println("(Note: In lieu of actual devices, precede input");
				System.out.println(" with 'card ' to simulate inputting a card)");
				System.out.println();

				printMenu(2);
				try {
					simulateMultipleDevices();

					if (cardInput != 0) {
						isValidCard = checkCardValidity(cardNo);
						if (isValidCard) {
							cardNo = cardInput;
						}
						cardInput = 0;
					}

					if(screenInput != 0) {
						switch(screenInput) {
							case 1: //cancel checkout pressed
								cancelCheckoutPressed = true;
								break;
							case 2: //cancel payment pressed
								cancelPaymentPressed = true;
								break;
							default:
								break;
						}
						screenInput = 0;
					}
				} catch (Exception e) {
					System.out.println(e);
				}
				if(cancelCheckoutPressed) { 
					return; //return to the welcome screen
				}
				if(cancelPaymentPressed) {
					PromptForPaymentType(); //return to payment type prompt
					return;
				}
			}
			//debit or credit
			cardType = identifyCardType(cardNo);

			//pin # if debit
			if (cardType == 0) {
				while (!isValidPin) {
					System.out.println("Input the pin");
					System.out.println("(Note: In lieu of actual devices, precede input");
					System.out.println(" with 'pin ' to simulate inputting a pin)");
					System.out.println();

					try {
						simulateMultipleDevices();

						if (pinpadInput != 0) {
							isValidPin = checkPinValidity(pinpadInput);
							if (isValidPin) {
								pin = pinpadInput;
							}

							pinpadInput = 0;
						}

						//screen input happened
						if(screenInput != 0) {
							switch(screenInput) {
								case 1: //cancel checkout pressed
									cancelCheckoutPressed = true;
									break;
								case 2: //cancel payment pressed
									cancelPaymentPressed = true;
									break;
								default:
									break;
							}
							screenInput = 0;
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}

			//send to auth
			authNo = SendAuthorizationRequest(total, cardType, cardNo, pin);

		//Assumption: auth. number of 0 means the card was declined
		} while (authNo == 0);

		//print receipt
		rp.print(String.format("%04d\%04d", cardNo % 1000, authNo % 1000));
    */

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void PayByCardScreen() {
		boolean cancelCheckoutPressed = false;
		boolean cancelPaymentPressed = false;
		long cardno = 0;
		String specialMessage = "";
		wipeInputs();

		cardno = getCardNumber();
		if(cardno == -1) {
			cancelCheckoutPressed = true;
			cardno = 0;
		} else if(cardno == -2) {
			cancelPaymentPressed = true;
			cardno = 0;
		}
		if(cardno != 0) { //card was swiped/inserted
			if(cardIsValid(cardno)) {
				int pin;
				int cardtype = identifyCardType(cardno);
				if(cardtype == 0 && isDebit()) {
					pin = PromptForPin();
					if(pin == -1) { //cancel checkout pressed
						cancelCheckoutPressed = true;
					} else if(pin == -2) { //cancel payment pressed
						cancelPaymentPressed = true;
					} else {
						System.out.println("Authorizing...");
						authDebit(cardno, pin); //expect result in authResult
					}
				} else { //credit, not debit
					System.out.println("Authorizing...");
					authCredit(cardno); //expect result in authResult
				}
			} else {
				System.out.println("Card not valid. Must by 16 digits");
				PromptForPaymentType();
				return;
			}
		}
		for(int x=0; x<60; x++) {
			if(authResult != 0) { //response from server about card
				String msg = "";
				switch(authResult) {
					case -1: //auth bad insuff
						msg = "Insufficient funds on card.";
						break;
					case -2: //auth bad badpin
						msg = "PIN incorrect.";
						break;
					case -3: //auth bad norecog
						msg = "Card not recognized.";
						break;
					case -4: //auth bad deactiv
						msg = "This card has been deactivated.";
						break;
					default: //auth ok ######
						System.out.println("Approved.");
						sleep(4000);
						clearScreen();
						completeCardTransaction(cardno, authResult);
						return; //return to welcome screen
				}
				System.out.println(msg);
				cr.ejectCard();
				sleep(4000);
				PayByCardScreen();
			}
			if(cancelCheckoutPressed) {
				return; //return to the welcome screen
			}
			if(cancelPaymentPressed) {
				PromptForPaymentType(); //return to payment type prompt
				return;
			}
			sleep(100);
		}

		//authorization timed out
		System.out.println("Authorization timed out.");
		PayByCardScreen();
	}

	//called by PayByCardScreen()
	private long getCardNumber() {
		wipeInputs();
		long cardno = 0;
		String specialMessage = "";
		while(cardno == 0) {
			clearScreen();
			System.out.println("(Note: In lieu of actual devices, precede input");
			System.out.println(" with 'card ' to simulate inputting a card or");
			System.out.println(" with 'pin ' to simulate inputting a PIN.)");
			System.out.println();
			PrintSubtotal();
			System.out.println();
			if(!(specialMessage == null) && !specialMessage.equals("")) {
				System.out.println(specialMessage);
				specialMessage = "";
			}
			System.out.println("INSERT CARD NOW\n");
			printMenu(2);
			try {
				simulateMultipleDevices(); //necessary when physical barcode scanner absent

				//check for card reader input
				if(cardInput != 0) { //card inserted
					if(processCardInput(cardInput) != 0) {
						//error happened
						specialMessage = "Unable to read card.\n";
						cr.ejectCard();
					} else {
						cardno = cardInput;
					}
					cardInput = 0;
					
				}
				//check for button/key presses
				if(screenInput != 0) { //screen input happened
					switch(screenInput) {
						case 1: //cancel checkout pressed
							return -1;
						case 2: //subtotal pressed
							return -2;
						default:
							break;
					}
					screenInput = 0;
				}
				sleep(150);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return cardno;
	}

	//called by PayByCardScreen()
	private boolean isDebit() {
		wipeInputs();
		clearScreen();
		System.out.println("Is this a debit card?");
		printMenu(3);
		while(true) {
			simulateMultipleDevices();

			//check for button/key presses
			if(screenInput != 0) { //screen input happened
				switch(screenInput) {
					case 1: //yes pressed
						return true;
					case 2: //no pressed
						return false;
					default:
						break;
				}
				screenInput = 0;
			}
			sleep(150);
		}
	}
	
	private void wipeInputs() {
		screenInput = 0;
		cardInput = 0;
		billInput = 0;
		coinInput = 0;
		barcodeInput = 0;
		pinpadInput = 0;
	}

	//called by PayByCardScreen()
	private int PromptForPin() {
		wipeInputs();
		clearScreen();
		String specialMessage = "";
		System.out.println("(Note: In lieu of actual devices, precede input");
		System.out.println(" with 'card ' to simulate inputting a card or");
		System.out.println(" with 'pin ' to simulate inputting a PIN.)");
		System.out.println();
		System.out.println("Enter 4-digit PIN: \n");
		printMenu(2);
		while(true) {
			if(!(specialMessage == null) && !specialMessage.equals("")) {
				System.out.println(specialMessage);
				specialMessage = "";
			}
			simulateMultipleDevices();

			//check for PIN pad input
			if(pinpadInput != 0) { //PIN inserted
				if(processPinInput(pinpadInput) != 0) {
					//error happened
					specialMessage = "Invalid PIN number. Try again.\n";
				} else {
					return pinpadInput;
				}
				pinpadInput = 0;
			}

			//check for button/key presses
			if(screenInput != 0) { //screen input happened
				switch(screenInput) {
					case 1: //cancel checkout pressed
						return -1;
					case 2: //cancel payment pressed
						return -2;
					default:
						break;
				}
				screenInput = 0;
			}
			sleep(150);
		}
	}

	private void PayByCashScreen() {
		boolean cancelCheckoutPressed = false;
		boolean cancelPaymentPressed = false;
		boolean cashSufficient = false;
		String specialMessage = "";
		wipeInputs();
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
				sleep(150);
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
			if(cashValueInserted > co.getTotal()) { //dispense change
				System.out.println("Change is being dispensed...");
				if(cd.dispense(cashValueInserted-co.getTotal()) != 0) {
					System.out.println("Unable to make change. See store associate.");
					sleep(5000);
				}
			}
			completeCashTransaction();	
			return; //return to welcome screen
		}
	}


	public void completeCashTransaction() {
		System.out.println("Printing receipt...");
		sleep(2000);
		rp.print(co); //print receipt

		sleep(8000);
		clearScreen();
		System.out.println("Please take your receipt.");
		sleep(7000);
		ReportToTransManager();
	}

	public void completeCardTransaction(long cardno, int authnum) {
		
		System.out.println("Printing receipts...");
		sleep(2000);
		rp.print(co, (int)(cardno%10000), authnum); //print receipt

		sleep(8000);
		clearScreen();
		System.out.println("Please take your receipt.");
		sleep(7000);
		ReportToTransManager();
	}

	protected void receive(String message) {
		String chunks[] = message.split(" ");
		switch(chunks[0]) {
			case "auth":
				processAuthResult(chunks);
				break;
			default:
				super.receive(message);
		}
	}

	public void ReportToTransManager() {
		for(InventoryItem item : co.getOrder()) {
			send("sold "+item.getId());
		}
	}

	private void authDebit(long cardno, int pin) {
			send("authdebit "+cardno+" "+pin+" "+co.getSubtotal());
	}

	private void authCredit(long cardno) {
			send("authcredit "+cardno+" "+co.getSubtotal());
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
			case "pinpad":
				pinpadInput = in;
				break;
			default:
				break;
		}
	}

	public void receiveBufferedScanLong(String device, long in) {
		switch(device) {
			case "card":
				cardInput = in;
				break;
			default:
				break;
		}
	}
}
