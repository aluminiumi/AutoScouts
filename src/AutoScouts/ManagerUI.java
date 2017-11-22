package AutoScouts;

import java.util.Scanner;

class ManagerUI extends Client {
	public static void main(String args[]) {
		System.out.println("ManagerUI");
		if(args.length == 0) 
			new ManagerUI();
		else if(args.length == 1)
			new ManagerUI(args[0]);
		else
			new ManagerUI(args[0], Integer.parseInt(args[1]));
	}

	ManagerUI() {
		super();
		go();
	}
	
	ManagerUI(String destAddress) {
		super(destAddress);
		go();
	}

	ManagerUI(String destAddress, int portNum) {
		super(destAddress, portNum);
		go();
	}

	private void go() {
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
