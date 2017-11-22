package AutoScouts;

import java.util.Scanner;

class CustomerUI extends Client {
	public static void main(String args[]) {
		System.out.println("CustomerUI");
		if(args.length == 0)
			new CustomerUI();
		else if(args.length == 1)
			new CustomerUI(args[0]);
		else
			new CustomerUI(args[0], Integer.parseInt(args[1]));
	}

	CustomerUI() {
		super();
		go();
	}

	CustomerUI(String destAddress) {
		super(destAddress);
		go();
	}
	
	CustomerUI(String destAddress, int portNum) {
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
