package AutoScouts;

public class CashDispenser {
	int qtyOfPennies = 10;
	int qtyOfNickels = 10;
	int qtyOfDimes = 10;
	int qtyOfQuarters = 10;
	int qtyOfOnes = 10;
	int qtyOfFives = 10;
	int qtyOfTens = 10;
	int qtyOfTwenties = 10;
	int qtyOfFifties = 10;
	int qtyOfHundreds = 10;
	int dispenserDelay = 600;

	CashDispenser() {

	}

	CashDispenser(int p, int n, int d, int q, int o, int f, int t, int w, int v, int h) {
		qtyOfPennies = p;
		qtyOfNickels = n;
		qtyOfDimes = d;
		qtyOfQuarters = q;
		qtyOfOnes = o;
		qtyOfFives = f;
		qtyOfTens = t;
		qtyOfTwenties = w;
		qtyOfFifties = v;
		qtyOfHundreds = h;
	}

	public int dispense(double amount) {
		if(getValueOfAllContained() < amount) {
			System.out.println("Insufficient cash available to dispense change.");
			return 1;
		}
		//System.out.println("Dispensing "+String.format("%1$,.2f",amount));
		while(amount >= 100 && qtyOfHundreds > 0) {
			dispenseHundred();
			sleep();
			amount -= 100;
		}
		while(amount >= 50 && qtyOfFifties > 0) {
			dispenseFifty();
			sleep();
			amount -= 50;
		}
		while(amount >= 20 && qtyOfTwenties > 0) {
			dispenseTwenty();
			sleep();
			amount -= 20;
		}
		while(amount >= 10 && qtyOfTens > 0) {
			dispenseTen();
			sleep();
			amount -= 10;
		}
		while(amount >= 5 && qtyOfFives > 0) {
			dispenseFive();
			sleep();
			amount -= 5;
		}
		while(amount >= 1 && qtyOfOnes > 0) {
			dispenseOne();
			sleep();
			amount -= 1;
		}
		while(amount >= .25 && qtyOfQuarters > 0) {
			dispenseQuarter();
			sleep();
			amount -= .25;
		}
		while(amount >= .10 && qtyOfDimes > 0) {
			dispenseDime();
			sleep();
			amount -= .10;
		}
		while(amount >= .05 && qtyOfNickels > 0) {
			dispenseNickel();
			sleep();
			amount -= .05;
		}
		while(amount >= 0 && qtyOfPennies > 0) {
			dispensePenny();
			sleep();
			amount -= .01;
		}
		if(amount > 0) {
			System.out.println("See store associate for remaining $"+String.format("%1$,.2f",amount));
			sleep(5000);
		}
		return 0; //no error
	}

	public void addPenny() {
		qtyOfPennies++;
	}

	public void addNickel() {
		qtyOfNickels++;
	}

	public void addDime() {
		qtyOfDimes++;
	}

	public void addQuarter() {
		qtyOfQuarters++;
	}

	public void addOne() {
		qtyOfOnes++;
	}

	public void addFive() {
		qtyOfFives++;
	}

	public void addTen() {
		qtyOfTens++;
	}

	public void addTwenty() {
		qtyOfTwenties++;
	}

	public void addFifty() {
		qtyOfFifties++;
	}

	public void addHundred() {
		qtyOfHundreds++;
	}

	private void dispensePenny() {
		System.out.println("Dispensing penny.");
		qtyOfPennies--;
	}

	private void dispenseNickel() {
		System.out.println("Dispensing nickel.");
		qtyOfNickels--;
	}

	private void dispenseDime() {
		System.out.println("Dispensing dime.");
		qtyOfDimes--;
	}

	private void dispenseQuarter() {
		System.out.println("Dispensing quarter.");
		qtyOfQuarters--;
	}

	private void dispenseOne() {
		System.out.println("Dispensing one.");
		qtyOfOnes--;
	}

	private void dispenseFive() {
		System.out.println("Dispensing five.");
		qtyOfFives--;
	}

	private void dispenseTen() {
		System.out.println("Dispensing ten.");
		qtyOfTens--;
	}

	private void dispenseTwenty() {
		System.out.println("Dispensing twenty.");
		qtyOfTwenties--;
	}

	private void dispenseFifty() {
		System.out.println("Dispensing fifty.");
		qtyOfFifties--;
	}

	private void dispenseHundred() {
		System.out.println("Dispensing hundred.");
		qtyOfHundreds--;
	}
	private double getValueInPennies() {
		return qtyOfPennies*.01;
	}

	private double getValueInNickels() {
		return qtyOfNickels*.05;
	}

	private double getValueInDimes() {
		return qtyOfDimes*.1;
	}

	private double getValueInQuarters() {
		return qtyOfQuarters*.25;
	}

	private double getValueInOnes() {
		return qtyOfOnes;
	}

	private double getValueInFives() {
		return qtyOfFives*5;
	}

	private double getValueInTens() {
		return qtyOfTens*10;
	}

	private double getValueInTwenties() {
		return qtyOfTwenties*20;
	}

	private double getValueInFifties() {
		return qtyOfFifties*50;
	}

	private double getValueInHundreds() {
		return qtyOfHundreds*100;
	}

	private double getValueInCoins() {
		return getValueInQuarters()+getValueInDimes()+getValueInNickels()+getValueInPennies();
	}

	private double getValueInBills() {
		return getValueInHundreds()+getValueInFifties()+getValueInTwenties()+
			getValueInTens()+getValueInFives()+getValueInOnes();
	}

	public double getValueOfAllContained() {
		return getValueInBills()+getValueInCoins();
	}

	private void sleep() {
		sleep(dispenserDelay);
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
