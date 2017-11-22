package AutoScouts;

import java.util.Scanner;

class CustomerUI extends Client {
	public static void main(String args[]) {
		System.out.println("CustomerUI");
		if(args.length == 0)
			new CustomerUI("localhost");
		else
			new CustomerUI(args[0]);
	}

	CustomerUI(String destAddress) {
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
