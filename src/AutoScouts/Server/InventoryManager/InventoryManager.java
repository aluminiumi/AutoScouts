package AutoScouts;

class InventoryManager {
	InventoryManager() {
		System.out.println("InventoryManager");
		DBManager.printDB();
	}

	public String getDBDump() {
		return DBManager.getDBDump();
	}

}
