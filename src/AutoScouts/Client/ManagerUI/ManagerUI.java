package AutoScouts;

import java.util.Scanner;

public class ManagerUI extends ApplicationLayerClient {
	Scanner kbd;
	//InventoryItem targetItem;
	//boolean expectingItem = false;

	public static void main(String args[]) {
		System.out.println("ManagerUI: started");
		if(args.length == 0) 
			new ManagerUI();
		else if(args.length == 1)
			new ManagerUI(args[0]);
		else
			new ManagerUI(args[0], Integer.parseInt(args[1]));
	}

	ManagerUI() {
		super();
		go();
	}
	
	ManagerUI(String destAddress) {
		super(destAddress);
		go();
	}

	ManagerUI(String destAddress, int portNum) {
		super(destAddress, portNum);
		go();
	}

	//this method is not used, but implemented anyway
	private void editIDScreen() {
		//TODO: make sure values are sane
		System.out.println("\nID: "+targetItem.getId());
		System.out.println("New ID: (or enter to leave unchanged)");
		String command = kbd.nextLine();
		if(!command.equals("")) {
			try {
				int id = Integer.parseInt(command);
				if(id > 0)
					targetItem.setId(id);
				else {
					System.out.println("ID must be positive.");
					editIDScreen();
					return;
				}
			} catch (Exception e) {
				System.out.println(e);
				editIDScreen();
				return;
			}
		}
	}

	private String prompt(String descriptor) {
		System.out.println("Provide new value (or just hit enter to leave unchanged)");
		System.out.print("New "+descriptor+": ");
		return kbd.nextLine();
	}

	private void editDescScreen() {
		String propertyname = "Desc";
		System.out.println("\nOriginal "+propertyname+": "+targetItem.getName());
		String command = prompt(propertyname);
		if(!command.equals("")) {
			targetItem.setName(command);
		}
	}
	
	private void editPriceScreen() {
		String propertyname = "Price";
		System.out.println("\nOriginal "+propertyname+": "+targetItem.getPrice());
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
	
	private void editDiscountScreen() {
		String propertyname = "Discount";
		System.out.println("\nOriginal "+propertyname+": "+targetItem.getDiscount());
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

	private void editQtyScreen() {
		String propertyname = "On Hand Quantity";
		System.out.println("\nOriginal "+propertyname+": "+targetItem.getQty());
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

	private void editThreshScreen() {
		String propertyname = "Minimum Threshold";
		System.out.println("\nOriginal "+propertyname+": "+targetItem.getMessageThresh());
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

	private void updateInventoryItemDesc(int id, String desc) {
		send("updateitem name "+id+" "+desc);
	}

	private void updateInventoryItemPrice(int id, Double price) {
		send("updateitem price "+id+" "+price);
	}

	private void updateInventoryItemDiscount(int id, Double discount) {
		send("updateitem discount "+id+" "+discount);
	}

	private void updateInventoryItemQty(int id, int qty) {
		send("updateitem qty "+id+" "+qty);
	}

	private void updateInventoryItemThresh(int id, int thresh) {
		send("updateitem threshold "+id+" "+thresh);
	}

	private void printReports() {
		send("printreports");
	}

	public void updateInventoryItem(InventoryItem item) {
		int id = item.getId();
		updateInventoryItemDesc(id, item.getName());
		updateInventoryItemPrice(id, item.getPrice());
		updateInventoryItemDiscount(id, item.getDiscount());
		updateInventoryItemQty(id, item.getQty());
		updateInventoryItemThresh(id, item.getMessageThresh());
	}

	private void showEditScreen() {
		//editIDScreen();
		editDescScreen();
		editPriceScreen();
		editDiscountScreen();
		editQtyScreen();
		editThreshScreen();
		updateInventoryItem(targetItem);
		int id = targetItem.getId();
		targetItem = null;
		requestInventoryItem(id);
		System.out.println("Retrieving updated item from server.");
		if(!displayInventoryItem(id)) {
			System.out.println("Problem retrieving updated item information from server.");
		}
		System.out.println("\n\nReturning to main menu.");
	}

	public boolean displayInventoryItem(int id) {
		int trycounter = 0;
		while(targetItem == null && trycounter < 8) {
			try {
				Thread.sleep(500);
				trycounter++;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		if(targetItem == null) {
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
/*
	public void requestInventoryItem(int id) {
		targetItem = null;
		expectingItem = true;
		send("getitem "+id);
	}
*/
	public void ViewOrUpdate() {
		send("dump");
		try{
			Thread.sleep(1000);
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.print("Select item by ID number: ");
		String command = kbd.nextLine();
		int option = 0;
		try {
			option = Integer.parseInt(command);
		} catch (Exception e) {
			System.out.println(e);
			ViewOrUpdate();
			return;
		}
		requestInventoryItem(option);
		if(!displayInventoryItem(option))
			return;
		System.out.print("Edit? (y/N): ");
		command = kbd.nextLine();
		if(command.equalsIgnoreCase("y") || command.equalsIgnoreCase("yes")) {
			showEditScreen();
		} else {
			return;
		}
		
	}

	private void initialScreen() {
		targetItem = null;
		System.out.println("\n\n\nMenu Options");
		System.out.println("------------");
		System.out.println();
		System.out.println("1. View/Update");
		System.out.println("2. Print Reports (for demonstration purposes)");
		System.out.println();
		System.out.print("Select menu option by number: ");
		String command = kbd.nextLine();
		int option = 0;
		try {
			option = Integer.parseInt(command);
		} catch (Exception e) {
			System.out.println(e);
			return;
		}
		switch(option) {
			case 1: //View/Update selected
				ViewOrUpdate();
				break;
			case 2:
				printReports();
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
	protected void receive(String message) {
		String chunks[] = message.split(" ");
		switch(chunks[0]) {
			case "dump":
				for(int x = 1; x < chunks.length; x++)
					System.out.print(chunks[x]+" ");
				System.out.println();
				break;
			default:
				super.receive(message);
		}
	}
/*
	protected void send(String message) {
		super.send(message);
	}
*/
	private void go() {
		//setQuietMode(false);
		//connect();
		kbd = new Scanner(System.in);

		boolean workToDo = true;
		while(workToDo) {
			initialScreen();
		}
	}
}
