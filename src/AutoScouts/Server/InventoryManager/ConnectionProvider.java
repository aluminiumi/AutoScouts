package AutoScouts;

import java.sql.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;
import java.util.List;

public class ConnectionProvider {
	private static Connection con = null;
	private static String dbloginfile = "dblogin.txt";
	private static String dbhost;
	private static String dbport;
	private static String dbname;
	private static String username;
	private static String password;

	private static boolean readCredFile() {
		List<String> lines;
		try {   
			lines = Files.readAllLines(Paths.get(dbloginfile));
		} catch (NoSuchFileException e) {
			return false;
		} catch (IOException e) {
			System.out.println(e);
			return false;
		}
		dbhost = lines.get(0);
		dbport = lines.get(1);
		dbname = lines.get(2);
		username = lines.get(3);
		password = lines.get(4);
		//System.out.println("Loaded file: "+dbloginfile);
		return true;
	}

	private static boolean readCredFile(String newdbloginfile) {
		dbloginfile = newdbloginfile;
		return readCredFile();
	}

	public static void setCredFile(String newdbloginfile) {
		dbloginfile = newdbloginfile;
	}
	
	public static Connection getCon() {
		String connectstring;
		if(readCredFile() || readCredFile("dist/"+dbloginfile)) {
			try {
				connectstring = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?user="+username+"&password="+password;
				con=DriverManager.getConnection(connectstring);
				return con;
			} catch (Exception e) {
				System.out.println("ConnMan: getCon(): "+e);
			}
		} else {	
			System.out.println("Unable to find file "+dbloginfile+".");
			System.out.println("Error: DB credential file not found. Please run server with path to file.");
			System.out.println("Example: java -jar AutoScouts.jar /path/to/dblogin.txt");
		}
		return null;
	}

}



