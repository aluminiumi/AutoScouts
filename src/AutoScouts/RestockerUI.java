package AutoScouts;

import java.util.Scanner;

class RestockerUI extends Client {
	public static void main(String args[]) {
		System.out.println("RestockerUI");
		if(args.length == 0)
			new RestockerUI("localhost");
		else
			new RestockerUI(args[0]);
	}

	RestockerUI(String destAddress) {
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
