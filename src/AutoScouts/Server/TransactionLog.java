package AutoScouts;

import java.util.ArrayList;
import java.util.List;

class TransactionLog {
	private List<Transaction> transList;

	TransactionLog() {
		transList = new ArrayList<Transaction>();
	}

	public void add(Transaction t) {
		//System.out.println("TransactionLog: add()");
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

	public List<Transaction> getTransactionList() {
		return transList;
	}

	public TransactionLog consolidateTransactions() {
		//List<Transaction> newlist = new ArrayList<Transaction>();
		TransactionLog newlist = new TransactionLog();
		for(Transaction t : transList) {
			boolean newcontainsalready = false;

			//update in the new list if we have it already
			for(Transaction newt : newlist.getTransactionList()) {
				if(t.getItem() == newt.getItem()) {
					newt.incrementQty(t.getQty());
					newt.addToPrice(t.getPrice());
					newcontainsalready = true;
					break;
				}
			}

			//otherwise just add it to the new list
			if(!newcontainsalready) {
				newlist.add(t);
			}
		}
		return newlist;
	}

	public String toString() {
		String output = "";
		if(transList.size() == 0)
			output += "NO TRANSACTIONS\n";
		else {
			for(Transaction t : transList)
				output += t.toString()+"\n";
		}
		output += "TOTAL REVENUE:"+String.format("%1$,.2f", this.getTotalRevenue());
		return output;	
	}
}
