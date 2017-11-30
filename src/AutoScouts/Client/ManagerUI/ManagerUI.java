package AutoScouts;

import java.util.Scanner;

class ManagerUI extends Client {
	Scanner kbd;
	InventoryItem targetItem;
	boolean expectingItem = false;

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

	private void editIDScreen() {
		System.out.println("\nID: "+targetItem.getId());
		System.out.println("New ID: (or enter to leave unchanged)");
		String command = kbd.nextLine();
		if(!command.equals("")) {
			try {
				int id = Integer.parseInt(command);
				targetItem.setId(id);
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
				targetItem.setPrice(price);
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
				targetItem.setDiscount(discount);
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
				targetItem.setQty(qty);
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
				targetItem.setMessageThresh(thresh);
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

	private void updateInventoryItem(InventoryItem item) {
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

	public void requestInventoryItem(int id) {
		expectingItem = true;
		send("getitem "+id);
	}

	private void ViewOrUpdate() {
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
		System.out.println();
		System.out.print("Select menu option by number: ");
		String command = kbd.nextLine();
		int option = 0;
		try {
			option = Integer.parseInt(command);
		} catch (Exception e) {
			System.out.println(e);
			//initialScreen();
			return;
		}
		switch(option) {
			case 1: //View/Update selected
				ViewOrUpdate();
				break;
			default:
				System.out.println("Invalid option.");
				//initialScreen();
				return;
		}
		//if(command != null) {
		//}

	}

	private void formItem(String chunks[]) {
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

	protected void receive(String message) {
		String chunks[] = message.split(" ");
		switch(chunks[0]) {
			case "dump":
				for(int x = 1; x < chunks.length; x++)
					System.out.print(chunks[x]+" ");
				System.out.println();
				break;
			case "item":
				if(expectingItem) {
					expectingItem = false;
					formItem(chunks);
				} else {
					System.out.println("Server sent unexpected item information.");
				}
			case "Hello.":
				break;
			default:
				super.receive(message);
		}
	}

	protected void send(String message) {
		super.send(message);
	}

	private void go() {
		setQuietMode(false);
		connect();
		kbd = new Scanner(System.in);

		boolean workToDo = true;
		while(workToDo) {
			initialScreen();
		}
	}
}
