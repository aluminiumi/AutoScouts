package AutoScouts;

import java.util.ArrayList;
import java.util.List;

class TransactionLog {
	private List<Transaction> transList;

	TransactionLog() {
		transList = new ArrayList<Transaction>();
	}

	public void add(Transaction t) {
		transList.add(t);
	}

	public Transaction get(int i) {
		return transList.get(i);
	}
	
	public int getSize() {
		return transList.size();
	}

	public double getTotalRevenue() {
		double total = 0;
		for(Transaction t : transList)
			total += t.getPrice();
		return total;
	}

	public TransactionLog consolidateTransactions() {
		//TODO: implement
		return null;
	}

	public String toString() {
		String output = "";
		for(Transaction t : transList)
			output += t.toString()+"\n";
		output += "Total Revenue: "+this.getTotalRevenue();
		return output;	
	}
}
