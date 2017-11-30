package AutoScouts;

import java.util.List;
import java.util.ArrayList;

class InventoryMessageReport {
	List<InventoryMessage> report;
	InventoryManager imanager;

	InventoryMessageReport(InventoryManager im) {
		imanager = im;
		report = new ArrayList<InventoryMessage>();
	}

	public void reset() {
		report = new ArrayList<InventoryMessage>();
	}

	//should add or update
	public void add(InventoryMessage newimsg) {
		int newid = newimsg.getId();

		//check if message already exists for this item
		for(InventoryMessage imsg : report) {
			if(imsg.getId() == newid) {
				System.out.println("InventoryMessageReport: Item "+newid+" already in deficit");
				return;
			}
		}

		System.out.println("InventoryMessageReport: Item "+newid+" is below threshold.");
		report.add(newimsg);
	}

	//This method is used just before printing to ensure that items
	//are still short of thresholds, as they may have been restocked
	//or had levels/thresholds updated since the message was added
	private void reassessMessageReport() {
		List<InventoryMessage> newreport = new ArrayList<InventoryMessage>();
		for(InventoryMessage imsg : report) {
			InventoryItem i = imanager.getInventoryItem(imsg.getId());
			if(i.getQty() < i.getMessageThresh())
				newreport.add(new InventoryMessage(i.getId()));

		}
		report = newreport;
	}

	public String toString() {
		if(report.size() == 0)
			return "NO NEW INVENTORY MESSAGES";

		reassessMessageReport();

		String output = "";
		for(InventoryMessage imsg : report) {
			InventoryItem i = imanager.getInventoryItem(imsg.getId());
			output += 	"ID:"+i.getId()+" "+
					"NAME:"+i.getName()+" "+
					"QTY:"+i.getQty()+" "+
					"THRSH:"+i.getMessageThresh()+" "+
					"DFCT:"+(i.getMessageThresh()-i.getQty())+"\n";
		}
		return output;
	}
}
