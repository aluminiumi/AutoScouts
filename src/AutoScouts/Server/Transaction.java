package AutoScouts;

class Transaction {
	private int item;
	private double price;
	private int quantity;

	Transaction() {

	}

	Transaction(int i, double p, int q) {
		item = i;
		price = p;
		quantity = q;
	}

	public int getItem() {
		return item;
	}

	public int getQty() {
		return quantity;
	}
	
	public double getPrice() {
		return price;
	}

	//this is used by TransactionLog's consolidateTransactions()
	public void incrementQty(int i) {
		quantity += i;
	}

	//this is used by TransactionLog's consolidateTransactions()
	public void addToPrice(double p) {
		price += p;
	}
}
