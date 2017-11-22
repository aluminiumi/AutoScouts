package AutoScouts;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class CommunicationManager implements Runnable {
	TransactionManager tm;
	InventoryManager im;
	Socket clientSocket;

	public static void main(String args[]) {
		CommunicationManager cm = new CommunicationManager();
	}

	CommunicationManager() {
		System.out.println("CommunicationManager");
		tm = new TransactionManager();
		im = new InventoryManager();
		startServer();

	}

	CommunicationManager(TransactionManager newtm, InventoryManager newim, Socket newclientSocket) {
		tm = newtm;
		im = newim;
		clientSocket = newclientSocket;
	}

	public void startServer() {
		boolean workToDo = true;
		int portNum = 41114;
		while(portNum < 65535) {
			try (
				ServerSocket serverSocket = new ServerSocket(portNum);
			) {
				System.out.println("Listening on port "+portNum);
				while(workToDo)
					new Thread(new CommunicationManager(tm, im, serverSocket.accept())).start();
			} catch (IOException e) {
				System.out.println("startServer(): Exception: "+e);
				portNum++;
			}
		}
	}

	public void run() {
		System.out.println("New socket thread started.");
		try {
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				String inputLine, outputLine;
	        	    
				// Initiate conversation with client
				out.println("Hello.");
		
				while ((inputLine = in.readLine()) != null) {
					//System.out.println("Client: "+inputLine);
					outputLine = inputLine;
					out.println(outputLine);
					if (outputLine.equals("Bye."))
						break;
					else {
						processClientInput(inputLine, out);
					}
				}
				System.out.println("Client closed connection.");
		} catch(Exception e) {
			System.out.println("run(): Exception: "+e);
		}
		System.out.println("Exiting socket thread.");
	}

	public void processClientInput(String input, PrintWriter out) {
		System.out.println("Client: "+input);
		if(input.equals("dump")) {
			String dump = im.getDBDump();
			System.out.println(dump);
			String dumplines[] = dump.split("\n");
			for(String line : dumplines)
				out.println(line);
		}
	}
}
