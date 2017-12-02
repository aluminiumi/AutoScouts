package AutoScouts;

public interface ScannerHost {
	public void receiveBufferedScan(String device, int in);	
	public void receiveBufferedScanLong(String device, long in);	
}
