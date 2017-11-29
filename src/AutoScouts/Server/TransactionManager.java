package AutoScouts;

class TransactionManager {
	CommunicationManager cm;
	InventoryManager im;
	TransactionLog transLog;

	TransactionManager(InventoryManager im) {
		this.im = im;
		System.out.println("TransactionManager");
		transLog = new TransactionLog();
	}

	public void processSaleOfItem(int i) {
		//TODO: add to TransactionLog
		//TODO: notify InventoryManager to update inventory
	}

	public TransactionLog getDailyReport() {
		System.out.println("TransactionManager: PrintDailyReport()");

		/*
		2. System reads transaction log to compute the 
		number of items sold for each product and the
		total revenue for the day.
		*/

		//prepare and send back daily report
		//(consolidate Transactions into new TransactionLog, for ease)
		return transLog.consolidateTransactions();
	}

	public void resetDailyReport() {
		transLog = new TransactionLog();
	}

}
