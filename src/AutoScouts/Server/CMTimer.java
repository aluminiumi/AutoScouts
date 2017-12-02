/* At midnight each day, this class is responsible for notifying the
CommunicationManager that reports need to be gathered from the 
InventoryManager and TransactionManager and subsequently printed. */

package AutoScouts;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CMTimer {
	CommunicationManager cm;

	CMTimer() {

	}

	CMTimer(CommunicationManager cm) {
		System.out.println("CMTimer: started");
		this.cm = cm;
		Calendar date = Calendar.getInstance();
		date.set(date.get(Calendar.YEAR), //year
			date.get(Calendar.MONTH), //month
			date.get(Calendar.DAY_OF_MONTH)+1, //date
			0, //hour
			0, //minute
			0); //second
		//System.out.println(date);
		Timer timer = new Timer();
		timer.schedule(new CMTimerTask(this), date.getTime(), 1000*60*60*24); //every 24 hours
		//timer.schedule(new CMTimerTask(this), date.getTime(), 1000); //every second
	}

	public void PrintDailyReport() {
		cm.PrintDailyReport();
	}

	public void PrintInventoryMessage() {
		cm.PrintInventoryMessage();
	}

	class CMTimerTask extends TimerTask {
		CMTimer cmt;

		CMTimerTask(CMTimer cmt) {
			this.cmt = cmt;
		}

		public void run() {
			System.out.println("CMTimer: Sending notice to generate reports");
			cmt.PrintDailyReport();
			cmt.PrintInventoryMessage();
		}
	}
}
