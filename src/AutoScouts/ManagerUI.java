package AutoScouts;

import java.util.Scanner;

class ManagerUI extends Client {
	public static void main(String args[]) {
		System.out.println("ManagerUI");
		if(args.length == 0) 
			new ManagerUI("localhost");
		else
			new ManagerUI(args[0]);
	}
	
	ManagerUI(String destAddress) {
		super(destAddress);
		connect();

		boolean workToDo = true;
		while(workToDo) {
			Scanner kbd = new Scanner(System.in);
			String command = kbd.nextLine();
			if(command != null) {
				send(command);
			}
		}
	}

}
