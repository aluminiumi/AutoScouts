package AutoScouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class CustomerUI extends Client {
	private CustomerOrder co;
	private boolean expectingItem = false;

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
		connect();

		co = new CustomerOrder();
		boolean workToDo = true;
		while(workToDo) {
			Scanner kbd = new Scanner(System.in);
			String command = kbd.nextLine();
			if(command != null) {
				send(command);
			}
		}
	}
	
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
						System.out.println("New subtotal: "+co.getSubtotal());

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
	}

	protected void send(String message) {
		if(message.startsWith("getitem "))
			expectingItem = true;
		super.send(message);
	}

	private void ScanItem(int i) {
		send("getitem "+i);
		expectingItem = true;
	}

	public void NewScreen() {
		co = new CustomerOrder();
	}

	public void ReportToTransManager() {
		for(InventoryItem item : co.getOrder()) {
			send("sold "+item.getId());
		}
	}

	class CustomerOrder {
		List<InventoryItem> order;

		CustomerOrder() {
			order = new ArrayList<InventoryItem>();
		}
		
		public void add(InventoryItem i) {
			order.add(i);
		}

		public double getSubtotal() {
			double p = 0;
			for(InventoryItem i : order)
				p += i.getPrice();
			return p;
		}

		public int size() {
			return order.size();
		}

		public List<InventoryItem> getOrder() {
			return order;
		}
	}
}
