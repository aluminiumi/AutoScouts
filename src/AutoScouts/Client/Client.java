package AutoScouts;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

class Client {
	String destAddress = "localhost";
	int portNum = 41114;
	PrintWriter sout;
	String sendBuffer;
	boolean workToDo = true;

	Client() {
	}

	Client(String destAddress) {
		this.destAddress = destAddress;
	} 

	Client(String destAddress, int portNum) {
		this.destAddress = destAddress;
		this.portNum = portNum;
	}
	
	public void send(String message) {
		System.out.println("Sending: "+message);
		sout.println(message);
	}

	public void receive(String message) {
		System.out.println("Received: "+message);
		if (message.equals("Bye."))
			System.exit(0);
	}

	public Thread connect() {
		Thread x = new Thread(new ConnectionHandler());
		x.start();
		return x;
	}

	public void die() {
		workToDo = false;
	}

	class ConnectionHandler implements Runnable {
		Thread sender;
		Thread listener;

		ConnectionHandler() {

		}

		public void run() {
			try (
				Socket cSocket = new Socket(destAddress, portNum);
			)
			{
				Thread sender = new Thread(new ClientSenderThread(cSocket));
				Thread listener = new Thread(new ClientListenerThread(cSocket));
				sender.start();
				listener.start();
				this.sender = sender;
				this.listener = listener;
				try {
					while(workToDo) {
						Thread.sleep(1000);
					}
				} catch(InterruptedException e) {
					System.out.println("InterruptedException: "+e);
				}
			} catch(IOException e) {
				System.out.println("Exception: "+e);
			}
			System.out.println("ConnectionHandler: Closing connection.");
		}
	}
	


	class ClientSenderThread implements Runnable {
		Socket cSocket;

		public void run() {
			try { 
				PrintWriter out = new PrintWriter(cSocket.getOutputStream(), true);
				sout = out;
				boolean workToDo = true;
				while (workToDo) {
					if(sendBuffer != null) {
						send(sendBuffer);
						sendBuffer = null;
					}
					Thread.sleep(1000);
				}
			} catch(Exception e) {
				System.out.println("ClientSenderThread: Exception: "+e);
			}
			System.out.println("ClientSenderThread: Server closed connection.");
		}

		ClientSenderThread(Socket cSocket) {
			System.out.println("ClientSenderThread: starting");
			this.cSocket = cSocket;
		}
	}

	class ClientListenerThread implements Runnable {
		Socket cSocket;

		public void run() {
			String fromServer;
			try { 
				BufferedReader in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
				while ((fromServer = in.readLine()) != null) {
					receive(fromServer);
				}
			} catch(Exception e) {
				System.out.println("ClientListenerThread: Exception: "+e);
			}
			System.out.println("ClientListenerThread: Server closed connection.");
		}

		ClientListenerThread(Socket cSocket) {
			System.out.println("ClientListenerThread: starting");
			this.cSocket = cSocket;
		}
	}
}
