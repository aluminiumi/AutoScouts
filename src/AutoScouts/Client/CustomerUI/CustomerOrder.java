package AutoScouts;

import java.util.ArrayList;
import java.util.List;

class CustomerOrder {
	List<InventoryItem> order;
	double taxRate = .0825;

	CustomerOrder() {
		order = new ArrayList<InventoryItem>();
	}
	
	public void add(InventoryItem i) {
		/*order.add(new InventoryItem(
			i.getId(),
			i.getName(),
			i.getPrice(),
			i.getDiscount(),
			i.getQty(),
			i.getMessageThresh());
		*/
		order.add(i);
	}

	//returns total before taxes
	public double getSubtotal() {
		double p = 0;
		for(InventoryItem i : order)
			p += i.getPriceWithDiscount();
		return p;
	}

	//returns total amount of taxes
	public double getTaxTotal() {
		return getSubtotal() * taxRate;
	}

	//returns total with tax added
	public double getTotal() {
		return getSubtotal() * (1+taxRate);
	}

	public int size() {
		return order.size();
	}

	public List<InventoryItem> getOrder() {
		return order;
	}
}
