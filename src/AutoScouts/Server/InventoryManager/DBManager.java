/* This class does no-frills database operations. You tell it to do it, it
does it. The InventoryManager class should be the only class which should
call the DBManager, and should make sure operation queries are safe. */

package AutoScouts;

//import AutoScouts.ConnectionProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public final class DBManager {
	private static Connection con;
	
	private DBManager() {
	}

	//this method exists for testing/debugging purposes
	public static void printDB() {
		System.out.println("printDB(): ");
		System.out.println(getDBDump());
	}

	//returns entire contents of Inventory table
	public static String getDBDump() {
		String output = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Inventory;");
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				output += new InventoryItem(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getInt(5), rs.getInt(6))+"\n";
			}

		} catch (Exception e) {
			System.out.println("getDBDump() Exception: "+e);
		}
		return output;
	}

	//returns one specific row from Inventory table
	public static InventoryItem getInventoryItem(int i) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Inventory WHERE id=?;");
			ps.setInt(1, i);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				return new InventoryItem(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getInt(5), rs.getInt(6));
			}

		} catch (Exception e) {
			System.out.println("getInventoryItem() Exception: "+e);
		}
		return null;
	}

	//updates quantity of one item in table
	public static void updateItemQty(int itemid, int qty) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement("UPDATE Inventory SET Qty=? WHERE id=?;");
			ps.setInt(1, qty);
			ps.setInt(2, itemid);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateItemQty() Exception: "+e);
		}
	}

	//updates name/desc of one item in table
	public static void updateItemName(int itemid, String name) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement("UPDATE Inventory SET ItemName=? WHERE id=?;");
			ps.setString(1, name);
			ps.setInt(2, itemid);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateItemName() Exception: "+e);
		}
	}

	//updates threshold of one item in table
	public static void updateItemThresh(int itemid, int thresh) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement("UPDATE Inventory SET Threshold=? WHERE id=?;");
			ps.setInt(1, thresh);
			ps.setInt(2, itemid);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateItemThresh() Exception: "+e);
		}
	}

	//updates price of one item in table
	public static void updateItemPrice(int itemid, double price) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement("UPDATE Inventory SET Price=? WHERE id=?;");
			ps.setFloat(1, (float)price);
			ps.setInt(2, itemid);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateItemPrice() Exception: "+e);
		}
	}

	//updates discount of one item in table
	public static void updateItemDiscount(int itemid, double discount) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement("UPDATE Inventory SET Discount=? WHERE id=?;");
			ps.setFloat(1, (float)discount);
			ps.setInt(2, itemid);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateItemDiscount() Exception: "+e);
		}
	}

	//adds new row to Inventory table
	public static InventoryItem createItem(int id, String name, double price, double discount, int qty, int threshold) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement
			("INSERT INTO Inventory (id,ItemName,Price,Discount,Qty,Threshold) VALUES (?,?,?,?,?,?);");
			ps.setInt(1, id);
			ps.setString(2, name);
			ps.setFloat(3, (float)price);
			ps.setFloat(4, (float)discount);
			ps.setInt(5, qty);
			ps.setInt(6, threshold);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("createItem() Exception: "+e);
		}
		return getInventoryItem(id);

	}
}	
