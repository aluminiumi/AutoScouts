package AutoScouts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import AutoScouts.ConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public final class DBManager {
	private static Connection con;
	
	private DBManager() {
	}

	public static void printDB() {
		try {
			System.out.println("printDB(): ");
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Inventory;");
			ps.setString(1, accountID);
			ps.setString(2, password);
			ResultSet rs=ps.executeQuery();
			boolean status = rs.next();
			if(status) {
				System.out.println(new InventoryItem(rs.getInteger(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getInteger(5), rs.getInteger(6)));

			}
		} catch (Exception e) {
			System.out.println("readAccountInfo() Exception: "+e);
		}

	}

/*
	public static customer readAccountInfo(String accountID, String password) {
		try {
			System.out.println("readAccountInfo(): ");
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Accounts WHERE AccountID=? AND Password=MD5(CONCAT('secretsalt',?));");
			ps.setString(1, accountID);
			ps.setString(2, password);
			ResultSet rs=ps.executeQuery();
			boolean status = rs.next();
			if(status) {
				return new customer(rs.getString(1), rs.getLong(3), rs.getString(4));

			}
		} catch (Exception e) {
			System.out.println("readAccountInfo() Exception: "+e);
		}
		return null;
	}
	public static customer createAccount(String accountID, String password, long cardNum, String email) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement
			("INSERT INTO Accounts (AccountID,Password,CardNo,CustomerEmail) VALUES (?, MD5(CONCAT('secretsalt',?)), ?, ?);");
			ps.setString(1, accountID);
			ps.setString(2, password);
			ps.setLong(3, cardNum);
			ps.setString(4, email);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
		return readAccountInfo(accountID,password);
	}
	public static customer updateCardNo(String accountID, String password, long cardNum) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement
			("UPDATE Accounts SET CardNo = ? WHERE AccountID = ? AND Password = MD5(CONCAT('secretsalt',?));");

			ps.setLong(1, cardNum);
			ps.setString(2, accountID);
			ps.setString(3, password);
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
		return readAccountInfo(accountID,password);
	}
	
	public static boolean accountExists(String accountID) {
		boolean status = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Accounts WHERE AccountID=?;");
			ps.setString(1, accountID);
			ResultSet rs=ps.executeQuery();
			status = rs.next();
		} catch (Exception e) {
			System.out.println("Exception: "+e);
		}
		return status;
	}
	public static boolean accountExistsWithPassword(String accountID, String pass) {
		boolean status = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = ConnectionProvider.getCon();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM Accounts WHERE AccountID=? AND Password=MD5(CONCAT('secretsalt',?));");
			ps.setString(1, accountID);
			ps.setString(2, pass);
			ResultSet rs=ps.executeQuery();
			status = rs.next();
		} catch (Exception e) {
			System.out.println("Exception: "+e);
		}
		return status;
	}
*/

}	
