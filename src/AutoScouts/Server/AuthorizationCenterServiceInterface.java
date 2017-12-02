package AutoScouts;

import java.util.Random;

public class AuthorizationCenterServiceInterface {
	AuthorizationCenterServiceInterface() {
		System.out.println("AuthCenterInterface: started");
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public int authorizeCard(long cardno, int pin, double cost) {
		sleep(3000);

		if(cardno == 11111111)
			return -1;
		if(cardno == 22222222)
			return -2;
		if(cardno == 33333333)
			return -3;

		int result = (new Random().nextInt(100));

		if(result < 60) { //approved
			return new Random().nextInt(99999999) + 10000000; //auth number
		}
		if(result < 70) { //auth bad insuff
			return -1;
		}
		if(result < 80) { //auth bad badpin
			return -2;
		}
		if(result < 90) { //auth bad norecog
			return -3;
		}
		return -4; //auth bad deactiv
	}

	public int authorizeCard(long cardno, double cost) {
		sleep(3000);
		
		if(cardno == 11111111)
			return -1;
		if(cardno == 22222222)
			return -2;
		if(cardno == 33333333)
			return -3;


		int result = (new Random().nextInt(100));

		if(result < 70) { //approved
			return new Random().nextInt(99999999) + 10000000; //auth number
		}
		if(result < 80) { //auth bad insuff
			return -1;
		}
		if(result < 90) { //auth bad norecog
			return -3;
		}
		return -4; //auth bad deactiv
	}
}
