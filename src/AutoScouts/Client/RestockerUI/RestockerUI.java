package AutoScouts;

import java.util.Scanner;
import java.util.NoSuchElementException;

public class RestockerUI extends ApplicationLayerClient implements ScannerHost {
	Scanner kbd;
	BarCodeScanner bs;
	//InventoryItem targetItem;
	boolean expectingItem = false;

	public static void main(String args[]) {
		System.out.println("RestockerUI");
		if(args.length == 0)
			new RestockerUI();
		else if(args.length == 1)
			new RestockerUI(args[0]);
		else
			new CustomerUI(args[0], Integer.parseInt(args[1]));
	}

	RestockerUI() {
		super();
		go();
	}

	RestockerUI(String destAddress) {
		super(destAddress);
		go();
	}
	
	RestockerUI(String destAddress, int portNum) {
		super(destAddress, portNum);
		go();
	}

	public boolean displayInventoryItem(int id) {
		if(!itemExists(id)) {
			System.out.println("No item selected.");
			return false;
		} else {
			System.out.println("ID: "+targetItem.getId());
			System.out.println("Desc: "+targetItem.getName());
			System.out.println("Price: "+targetItem.getPrice());
			System.out.println("Discount: "+targetItem.getDiscount());
			System.out.println("On Hand: "+targetItem.getQty());
			System.out.println("Threshold: "+targetItem.getMessageThresh());
			return true;
		}
	}

	//called by various edit screen methods
	private String prompt(String descriptor) {
		System.out.print(descriptor+": ");
		String input = kbd.nextLine();
		if(input.equals(""))
			return prompt(descriptor);
		return input;
	}

	//called by NewItemScreen()
	private void editDescScreen() {
		String propertyname = "Description";
		String command = prompt(propertyname);
		if(!command.equals("")) {
			targetItem.setName(command);
		}
	}
       
	//called by NewItemScreen()
	private void editPriceScreen() {
		String propertyname = "Price";
		String command = prompt(propertyname);
		if(!command.equals("")) {
			try {
				double price = Double.parseDouble(command);
				if(price >= 0)
					targetItem.setPrice(price);
				else {
					System.out.println("Price must be non-negative.");
					editPriceScreen();
					return;
				}
			} catch (Exception e) {
				System.out.println(e);
				editPriceScreen();
				return;
			}
		}
	}
       
	//called by NewItemScreen()
	private void editDiscountScreen() {
		String propertyname = "Discount";
		String command = prompt(propertyname);
		if(!command.equals("")) {
			try {   
				double discount = Double.parseDouble(command);
				if(discount <= 1)
					targetItem.setDiscount(discount);
				else {
					System.out.println("Discount must be less than or equal to 1");
					editDiscountScreen();
					return;
				}
			} catch (Exception e) {
				System.out.println(e);
				editDiscountScreen();
				return;
			}
		}
	}
	
	//called by NewItemScreen()
	private void editQtyScreen() {
		String propertyname = "On Hand Quantity";
		String command = prompt(propertyname);
		if(!command.equals("")) {
			try {   
				int qty = Integer.parseInt(command);
				if(qty >= 0)
					targetItem.setQty(qty);
				else {
					System.out.println("Quantity cannot be negative.");
					editQtyScreen();
					return;
				}
			} catch (Exception e) {
				System.out.println(e);
				editQtyScreen();
				return;
			}
		}
	}

	//called by NewItemScreen()
	private void editThreshScreen() {
		String propertyname = "Minimum Threshold";
		String command = prompt(propertyname);
		if(!command.equals("")) {
			try {
				int thresh = Integer.parseInt(command);
				if(thresh >= 0)
					targetItem.setMessageThresh(thresh);
				else {
					System.out.println("Threshold cannot be negative.");
					editThreshScreen();
					return;
				}
			} catch (Exception e) {
				System.out.println(e);
				editThreshScreen();
				return;
			}
		}
	}

	//called by NewItemScreen()
	//sends item creation request to server
	public void AddNewItem(InventoryItem item) {
		int id = item.getId();
		int threshold = item.getMessageThresh();
		int qty = item.getQty();
		double price = item.getPrice();
		double discount = item.getDiscount();
		String name = item.getName();
		send("newitem "+id+" "+threshold+" "+qty+" "+price+" "+discount+" "+name);
	}

	//called by PromptForScan() if item is new
	private void NewItemScreen(int id) {
		System.out.println("Item is unrecognized. Please provide details for item.");
		targetItem = new InventoryItem();
		targetItem.setId(id);
		editDescScreen();
		editPriceScreen();
		editDiscountScreen();
		editQtyScreen();
		editThreshScreen();
		AddNewItem(targetItem);
		displayInventoryItem(id);
	}

	//called by PromptForQuantity()
	//sends item qty increment request to server
	public void UpdateQuantity(int id, int qty) {
		send("incitemqty "+id+" "+qty);
	}

	//called by PromptForScan() if item exists in DB
	private void PromptForQuantity(int id) {
		System.out.print("Provide quantity to be added to inventory: ");
		String in = kbd.nextLine();
		try {
			int qty = Integer.parseInt(in);
			if(qty < 0) {
				System.out.println("Quantity cannot be negative.");
				PromptForQuantity(id);
				return;
			} else {
				UpdateQuantity(id, qty);
			}
		} catch (Exception e) {
			PromptForQuantity(id);
		}
		displayInventoryItem(id);
	}
/*
	public void requestInventoryItem(int id) {
		targetItem = null;
		expectingItem = true;
		send("getitem "+id);
	}

	public boolean itemExists(int id) {
		requestInventoryItem(id);
		int trycounter = 0;
		while(targetItem == null && trycounter < 8) {
			try {
				Thread.sleep(500);
				trycounter++;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		if(targetItem == null)
			return false;
		else
			return true;
	}
*/
	private void PromptForScan() {
		System.out.print("Scan item now: ");
		int id = bs.scan();
		if(itemExists(id)) {
			System.out.println("Item scanned: "+targetItem.getName());
			PromptForQuantity(id);
		} else { //is a new item or unknown id number
			NewItemScreen(id);
		}
	}

	private void initialScreen() {
		targetItem = null;
		System.out.println("\n\n\nMenu Options");
		System.out.println("------------");
		System.out.println();
		System.out.println("1. Restock");
		System.out.println("2. Exit");
		System.out.println();
		System.out.print("Select menu option by number: ");
		String command = kbd.nextLine();
		int option = 0;
		try {   
			option = Integer.parseInt(command);
		} catch (NoSuchElementException e) {
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Initial Screen" + e);
			return;
		}
		switch(option) {
			case 1: //Restock selected
				PromptForScan();
				break;
			case 2: //Exit selected
				System.exit(0);
				break;
			default:
				System.out.println("Invalid option.");
				return;
		}
	}
/*
	private void formItem(String chunks[]) {
		if(chunks[1].equals("null"))
			targetItem = null;
		else {
			try{	
				int len = chunks.length;
				int id = Integer.parseInt(chunks[1]);
				double price = Double.parseDouble(chunks[len-4]);
				double discount = Double.parseDouble(chunks[len-3]);
				int qty = Integer.parseInt(chunks[len-2]);
				int threshold = Integer.parseInt(chunks[len-1]);
				String desc = chunks[2];
				int iter = 3;
				while(len > 7) {
					desc += " "+chunks[iter];
					len--;
					iter++;
				}
				targetItem = new InventoryItem(id, desc, price, discount, qty, threshold);	 
			} catch (Exception e) {
				System.out.println("Server sent malformed item information. "+e);
			}
		}
	}
*/
/*
	protected void receive(String message) {
		String chunks[] = message.split(" ");
		switch(chunks[0]) {
			case "item":
				if(expectingItem) {
					expectingItem = false;
					formItem(chunks);
				} else {
					System.out.println("Server sent unexpected item information.");
				}
				break;
			case "Hello.":
				break;
			default:
				super.receive(message);
		}
	}

	protected void send(String message) {
		super.send(message);
	}
*/

	//used to satisfy ScannerHost interface, but this class does
	//not really require buffered scans
	public void receiveBufferedScan(String device, int in) {

	}

	public void receiveBufferedScanLong(String device, long in) {

	}

	private void go() {
		//setQuietMode(false);
		//connect();
		kbd = new Scanner(System.in);
		bs = new BarCodeScanner(this);
		
		boolean workToDo = true;
		while(workToDo) {
			try{
				initialScreen();
			} catch (Exception e) {
				System.out.println("Go" + e);
			}
		}
	}
}
