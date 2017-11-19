package AutoScouts;

public class InventoryItem {

    int id;
    String name;
    double price;
    double discount;
    int qty;
    int messageThresh;

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

    public int getQty() {
        return qty;
    }

    public int getMessageThresh() {
        return messageThresh;
    }

    void setId(int newId) {
        id = newId;
    }

    void setName(String newName) {
        name = newName;
    }

    void setPrice(double newPrice) {
        price = newPrice;
    }

    void setDiscount(double newDiscount) {
        discount = newDiscount;
    }

    void setQty(int newQty) {
        qty = newQty;
    }

    void setMessageThresh(int newMessageThresh) {
        messageThresh = newMessageThresh;
    }

    void subtractQty(int itemsTaken) {
        qty -= itemsTaken;
    }

    void addQty(int itemsAdded) {
        qty += itemsAdded;
    }
}
