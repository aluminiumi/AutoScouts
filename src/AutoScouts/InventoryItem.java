package AutoScouts;

public class InventoryItem {

    private int id;
    private String name;
    private double price;
    private double discount;
    private int qty;
    private int messageThresh;

    InventoryItem() {

    }

    InventoryItem(int i, String n, double p, double d, int q, int t) {
        id = i;
        name = n;
        price = p;
        discount = d;
        qty = q;
        messageThresh = t;
    }

    public String toString() {
        return id+" "+name+" "+price+" "+discount+" "+qty+" "+messageThresh;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscount() {
        return discount;
    }

    //we assume discounts are percentage based
    public double getPriceWithDiscount() {
        return price*(1-discount);
    }

    public int getQty() {
        return qty;
    }

    public int getMessageThresh() {
        return messageThresh;
    }

    public void setId(int newId) {
        id = newId;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setPrice(double newPrice) {
        price = newPrice;
    }

    public void setDiscount(double newDiscount) {
        discount = newDiscount;
    }

    public void setQty(int newQty) {
        qty = newQty;
    }

    public void setMessageThresh(int newMessageThresh) {
        messageThresh = newMessageThresh;
    }

    public void subtractQty(int itemsTaken) {
        qty -= itemsTaken;
    }

    public void addQty(int itemsAdded) {
        qty += itemsAdded;
    }
}
