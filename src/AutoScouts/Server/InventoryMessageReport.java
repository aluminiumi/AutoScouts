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
		for(InventoryMessage imsg : report) {
			if(imsg.getId() == newid)
				return;
		}
		report.add(newimsg);
	}

	public String toString() {
		if(report.size() == 0)
			return "Inventory levels sufficient.\n";

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
