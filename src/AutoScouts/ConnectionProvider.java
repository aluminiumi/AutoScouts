package AutoScouts;

import java.sql.*;

public class ConnectionProvider {
	private static Connection con = null;
	
	public static Connection getCon() {
		try {
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/autoscouts?"+ "user=cs3365&password=reedsgroup");
		} catch (Exception e) { 

		}
		return con;
	}

}



