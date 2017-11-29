package AutoScouts;

class InventoryManager {
	InventoryMessageReport imr;

	InventoryManager() {
		System.out.println("InventoryManager");
		imr = new InventoryMessageReport();
		DBManager.printDB(); //TODO: remove this, later
	}

	public void resetInventoryMessageReport() {
		imr = new InventoryMessageReport();
	}

	public InventoryMessageReport getInventoryMessageReport() {
		System.out.println("InventoryManager: PrintInventoryMessage()");
		return imr;
	}

	public String getDBDump() {
		return DBManager.getDBDump();
	}

}
