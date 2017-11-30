package AutoScouts;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class CommunicationManager implements Runnable {
	InventoryManager im;
	TransactionManager tm;
	Socket clientSocket;
	ReportPrinter rprinter;

	public static void main(String args[]) {
		if(args.length == 1) {
			ConnectionProvider.setCredFile(args[0]);
		}
		CommunicationManager cm = new CommunicationManager();
	}

	//initial server startup uses this constructor
	CommunicationManager() {
		System.out.println("CommunicationManager: started");
		im = new InventoryManager();
		tm = new TransactionManager(im);
		rprinter = new ReportPrinter();
		new CMTimer(this);
		startServer();

	}

	//new threads launched use this constructor
	CommunicationManager(TransactionManager newtm, InventoryManager newim, ReportPrinter newrprinter, Socket newclientSocket) {
		tm = newtm;
		im = newim;
		rprinter = newrprinter;
		clientSocket = newclientSocket;
	}

	//this is called during initialization
	private void startServer() {
		boolean workToDo = true;
		int portNum = 41114;
		while(portNum < 65535) {
			try (
				ServerSocket serverSocket = new ServerSocket(portNum);
			) {
				System.out.println("Listening for connections on port "+portNum+"...");
				while(workToDo)
					new Thread(new CommunicationManager(tm, im, rprinter, serverSocket.accept())).start();
			} catch (IOException e) {
				System.out.println("startServer(): Exception: "+e);
				portNum++;
			}
		}
	}

	//this method is run in a new thread every time a new client connects to the server
	public void run() {
		System.out.println("CommMan: New socket thread started.");
		try {
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				String inputLine, outputLine;
	        	    
				// Initiate conversation with client
				out.println("Hello.");
		
				while ((inputLine = in.readLine()) != null) {
					//System.out.println("Client: "+inputLine);
					if (inputLine.equals("Bye."))
						break;
					else {
						processClientInput(inputLine, out);
					}
				}
				System.out.println("CommMan: Client closed connection.");
		} catch(NullPointerException e) {
			System.out.println("CommMan: run(): NullPointerException:");
		} catch(Exception e) {
			System.out.println("CommMan: run(): Exception: "+e);
		}
		System.out.println("CommMan: run(): Exiting socket thread.");
	}

	//this method handles data sent by clients (CustomerUI, ManagerUI, RestockerUI)
	public void processClientInput(String input, PrintWriter out) {
		System.out.println("Client: "+input);
		String chunks[] = input.split(" ");
		switch(chunks[0]) {
			case "dump": //not used, just for debugging
				String dump = im.getDBDump();
				System.out.println(dump);
				String dumplines[] = dump.split("\n");
				for(String line : dumplines)
					out.println("dump "+line);
				break;
			case "printreports": //not used, just for debugging
				PrintDailyReport();
				PrintInventoryMessage();
				out.println("Okay.");
				break;
			case "getitem": //used by CustomerUI, RestockerUI, ManagerUI
				try{
					int i = Integer.parseInt(chunks[1]);
					out.println("item "+im.getInventoryItem(i));
				} catch (Exception e) {
					System.out.println("CommMan: "+e);
				}
				break;
			case "sold": //used by CustomerUI after purchase
				try{
					int i = Integer.parseInt(chunks[1]);
					tm.processSaleOfItem(i);
				} catch (Exception e) {
					System.out.println("CommMan: "+e);
				}
				break;
			case "incitemqty": //used by RestockerUI
				try{
					int i = Integer.parseInt(chunks[1]);
					int q = Integer.parseInt(chunks[2]);
					im.incrementItemQty(i, q);
				} catch (Exception e) {
					System.out.println("CommMan: "+e);
				}
				break;
			case "updateitem": //used by ManagerUI
				int id = 0;
				try {
					id = Integer.parseInt(chunks[2]);
				} catch (Exception e) {
					System.out.println(e);
				}
				switch(chunks[1]) {
					case "name":
						int iter;
						String name = "";
						for(iter = 3; iter < chunks.length-1; iter++) {
							name += chunks[iter]+" ";
						}
						name += chunks[iter];
						im.setItemName(id, name);
						break;
					case "price":
						try {
							Double price = Double.parseDouble(chunks[3]);
							im.setItemPrice(id, price);
						} catch (Exception e) {
							System.out.println(e);
						}
						break;
					case "discount":
						try {
							Double discount = Double.parseDouble(chunks[3]);
							im.setItemDiscount(id, discount);
						} catch (Exception e) {
							System.out.println(e);
						}
						break;
					case "qty":
						try {
							int qty = Integer.parseInt(chunks[3]);
							im.setItemQty(id, qty);
						} catch (Exception e) {
							System.out.println(e);
						}
						break;
					case "threshold":
						try {
							int thresh = Integer.parseInt(chunks[3]);
							im.setItemThresh(id, thresh);
						} catch (Exception e) {
							System.out.println(e);
						}
						break;
					default:
						System.out.println("CommMan: Bad updateitem argument from client.");
				}
				break;
			default:
				System.out.println("CommMan: Unexpected input from client.");
		}

	}

	//this is called by CMTimer
	public void PrintDailyReport() {
		//System.out.println("CommMan: PrintDailyReport()");
		rprinter.print(tm.getDailyReport());
		tm.resetDailyReport();
	}

	//this is called by CMTimer
	public void PrintInventoryMessage() {
		//System.out.println("CommMan: PrintInventoryMessage()");
		rprinter.print(im.getInventoryMessageReport());
		im.resetInventoryMessageReport();
	}

}
