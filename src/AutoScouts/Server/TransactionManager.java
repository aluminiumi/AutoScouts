package AutoScouts;

class TransactionManager {
	CommunicationManager cm;
	InventoryManager im;
	TransactionLog transLog;

	TransactionManager(InventoryManager im) {
		this.im = im;
		System.out.println("TransactionManager: started");
		transLog = new TransactionLog();
	}

	//This method is called by CommunicationManager after purchase by CustomerUI.
	//In theory, an overloaded implementation could support quantities > 1, but
	//this would require updating CustomerUI and CommunicationManager to support it.
	public void processSaleOfItem(int i) {
		//add to TransactionLog
		InventoryItem item = im.getInventoryItem(i);
		transLog.add(new Transaction(i, item.getPriceWithDiscount(), 1));
		
		//notify InventoryManager to update inventory
		im.decrementItemQty(i);
	}

	//This method is called by CommunicationManager when CMTimer calls
	//for reports to be generated.
	public TransactionLog getDailyReport() {
		/*
		2. System reads transaction log to compute the 
		number of items sold for each product and the
		total revenue for the day.
		*/

		//prepare and send back daily report
		//(consolidate Transactions into new TransactionLog, for ease)
		return transLog.consolidateTransactions();
	}

	//This method is called by CommunicationManager immediately following
	//reports generation, to clear it.
	public void resetDailyReport() {
		transLog = new TransactionLog();
	}

}
