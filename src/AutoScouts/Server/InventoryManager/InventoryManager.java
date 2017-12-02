package AutoScouts;

public class InventoryManager {
	InventoryMessageReport imr;

	InventoryManager() {
		System.out.println("InventoryManager: started");
		imr = new InventoryMessageReport(this);
	}
	
	//This method is called by CommunicationManager when CMTimer
	//requires reports to be generated.
	public InventoryMessageReport getInventoryMessageReport() {
		//System.out.println("InventoryManager: PrintInventoryMessage()");
		return imr;
	}

	//This method is called by CommunicationManager immediately
	//after reports are generate, to clear it.
	public void resetInventoryMessageReport() {
		//System.out.println("InventoryManager: resetInventoryMessageReport()");
		imr = new InventoryMessageReport(this);
	}

	//This method is called by CommunicationManager and
	//InventoryMessageReport, and is needed everywhere.
	public InventoryItem getInventoryItem(int i) {
		return DBManager.getInventoryItem(i);
	}

	//This method is called by TransactionManager to indicate
	//that one of something has been sold, and should be deducted
	//from the database.
	public void decrementItemQty(int itemid) {
		InventoryItem item = this.getInventoryItem(itemid);
		int newqty = item.getQty()-1;
		if(newqty >= 0) {
			DBManager.updateItemQty(itemid, newqty);
		
			//check threshold
			if(newqty < item.getMessageThresh()) {
				//buffer new inventory message for printing
				imr.add(new InventoryMessage(itemid));
			}
		} else {
			System.out.println("InventoryManager: Halted attempt to reduce quantity of item "+itemid+" below zero.");
		}
	}

	public void incrementItemQty(int itemid, int qty) {	
		InventoryItem item = this.getInventoryItem(itemid);
		DBManager.updateItemQty(itemid, item.getQty()+qty);
	}

	//This method is used by ManagerUI via CommunicationManager
	public void setItemQty(int itemid, int qty) {
		InventoryItem item = this.getInventoryItem(itemid);

		if(qty >= 0) {
			DBManager.updateItemQty(itemid, qty);
			
			//check threshold
			if(qty < item.getMessageThresh()) {
				//buffer new inventory message for printing
				imr.add(new InventoryMessage(itemid));
			}
		} else {
			System.out.println("InventoryManager: Halted attempt to reduce quantity of item "+itemid+" below zero.");
		}
	}
	
	//This method is used by ManagerUI via CommunicationManager
	public void setItemThresh(int itemid, int thresh) {
		InventoryItem item = this.getInventoryItem(itemid);

		if(thresh >= 0) {
			DBManager.updateItemThresh(itemid, thresh);
			
			//check threshold
			if(item.getQty() < thresh) {
				//buffer new inventory message for printing
				imr.add(new InventoryMessage(itemid));
			}
		} else {
			System.out.println("InventoryManager: Halted attempt to reduce threshold of item "+itemid+" below zero.");
		}
	}

	//This method is used by ManagerUI via CommunicationManager
	public void setItemName(int itemid, String name) {
		InventoryItem item = this.getInventoryItem(itemid);
		DBManager.updateItemName(itemid, name);
	}
	
	//This method is used by ManagerUI via CommunicationManager
	public void setItemPrice(int itemid, double price) {
		InventoryItem item = this.getInventoryItem(itemid);
		
		if(price >= 0) {
			DBManager.updateItemPrice(itemid, price);
		} else {
			System.out.println("InventoryManager: Halted attempt to reduce price of item "+itemid+" below zero.");
		}
	}

	//This method is used by ManagerUI via CommunicationManager
	public void setItemDiscount(int itemid, double discount) {
		InventoryItem item = this.getInventoryItem(itemid);
		
		if(discount <= 1) {
			DBManager.updateItemDiscount(itemid, discount);
		} else {
			System.out.println("InventoryManager: Halted attempt to set discount of item "+itemid+" above 100%.");
		}
	}

	//This method is used by RestockerUI via CommunicationManager
	//It must sanitize all variables before forwarding to DBManager
	public InventoryItem createItem(int id, String name, double price, double discount, int qty, int threshold) {
		//prevent invalid ID numbers
		if(id < 0)
			return null;

		//prevent writing if ID already exists in DB
		if(getInventoryItem(id) != null)
			return null;
		
		//prevent negative prices
		if(price < 0)
			return null;

		//prevent discounts above 100%
		if(discount > 1)
			return null;

		//prevent negative quantities
		if(qty < 0)
			return null;

		//prevent negative thresholds
		if(threshold < 0)
			return null;

		return DBManager.createItem(id, name, price, discount, qty, threshold);
	}

	//This method is used by ManagerUI via CommunicationManager 
	//when a list of items is being retrieved.
	public String getDBDump() {
		return DBManager.getDBDump();
	}

}
