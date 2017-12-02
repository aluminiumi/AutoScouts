package AutoScouts;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrder {
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

	private String getFormattedSubtotal() {
		String subtotal = String.format("%1$,.2f",getSubtotal());
		String tax = String.format("%1$,.2f",getTaxTotal());
		String total = String.format("%1$,.2f",getTotal());
		String output = 
			"\n"+
			String.format("%-9s:%8s\n", "Subtotal", subtotal)+
			String.format("%-9s:%8s\n", "Tax", tax)+
			String.format("%-9s:%8s\n", "Total", total)+
			"\n";
		return output;
	}

	private String format(double p) {
		return String.format("%1$,.2f",p);
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
	
	public String toString() {
		String output = "";
		for(InventoryItem i : order) {
			output += String.format("ID#%-4s%-20s%7s", i.getId(), i.getName(), format(i.getPriceWithDiscount()));
			output += "\n";
		}
		output += "\n";
		output += "--------------------------------\n";
		output += getFormattedSubtotal();
		return output;
	}
}
