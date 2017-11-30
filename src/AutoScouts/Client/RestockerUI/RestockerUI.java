package AutoScouts;

import java.util.Scanner;

class RestockerUI extends Client {
	public static void main(String args[]) {
		System.out.println("RestockerUI");
		if(args.length == 0)
			new RestockerUI();
		else if(args.length == 1)
			new RestockerUI(args[0]);
		else
			new CustomerUI(args[0], Integer.parseInt(args[1]));
	}

	RestockerUI() {
		super();
		go();
	}

	RestockerUI(String destAddress) {
		super(destAddress);
		go();
	}
	
	RestockerUI(String destAddress, int portNum) {
		super(destAddress, portNum);
		go();
	}

	private void go() {
		setQuietMode(false);
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
