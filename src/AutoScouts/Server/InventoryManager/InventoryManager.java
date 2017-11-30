package AutoScouts;

class InventoryManager {
	InventoryMessageReport imr;

	InventoryManager() {
		System.out.println("InventoryManager: started");
		imr = new InventoryMessageReport(this);
		//DBManager.printDB(); //TODO: remove this, later
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

	//This method is used by ManagerUI
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

	//This method is for testing purposes.
	public String getDBDump() {
		return DBManager.getDBDump();
	}

}
