package AutoScouts;

class InventoryManager {
	InventoryMessageReport imr;

	InventoryManager() {
		System.out.println("InventoryManager");
		imr = new InventoryMessageReport(this);
		DBManager.printDB(); //TODO: remove this, later
	}

	public void resetInventoryMessageReport() {
		imr = new InventoryMessageReport(this);
	}

	public InventoryMessageReport getInventoryMessageReport() {
		System.out.println("InventoryManager: PrintInventoryMessage()");
		return imr;
	}

	public InventoryItem getInventoryItem(int i) {
		return DBManager.getInventoryItem(i);
	}

	public String getDBDump() {
		return DBManager.getDBDump();
	}

}
