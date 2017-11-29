package AutoScouts;

class TransactionManager {
	CommunicationManager cm;
	TransactionLog transLog;

	TransactionManager() {
		System.out.println("TransactionManager");
		transLog = new TransactionLog();
	}

	public TransactionLog getDailyReport() {
		System.out.println("TransactionManager: PrintDailyReport()");

		//prepare daily report

		//send daily report back to CM
		return transLog;
	}

	public void resetDailyReport() {
		transLog = new TransactionLog();
	}

}
