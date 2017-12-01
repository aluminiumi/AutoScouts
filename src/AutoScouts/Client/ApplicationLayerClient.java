/* This class maintains properties and methods common to each of
CustomerUI, RestockerUI, and ManagerUI, but which are too specific
to be in the general Client class. */


package AutoScouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ApplicationLayerClient extends Client {
	InventoryItem targetItem;
	boolean expectingItem = false;

	public static void main(String args[]) {
		System.out.println("CustomerUI");
		if(args.length == 0)
			new ApplicationLayerClient();
		else if(args.length == 1)
			new ApplicationLayerClient(args[0]);
		else
			new ApplicationLayerClient(args[0], Integer.parseInt(args[1]));
	}

	ApplicationLayerClient() {
		super();
		go();
	}

	ApplicationLayerClient(String destAddress) {
		super(destAddress);
		go();
	}
	
	ApplicationLayerClient(String destAddress, int portNum) {
		super(destAddress, portNum);
		go();
	}

	private void go() {
		setQuietMode(false);
		connect();

		/*boolean workToDo = true;
		while(workToDo) {
			Scanner kbd = new Scanner(System.in);
			String command = kbd.nextLine();
			if(command != null) {
				send(command);
			}
		}*/
	}

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
			case "Okay.":
				break;
			default:
				System.out.println("Unexpected response from server.");
				super.receive(message);
		}
	}

	protected void send(String message) {
		if(message.startsWith("getitem "))
			expectingItem = true;
		super.send(message);
	}

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


}
