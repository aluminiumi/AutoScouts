package AutoScouts;

public class CoinAcceptor extends NumberReaderDevice {
	CoinAcceptor(Object caller) {
		super(caller, "coin");
	}

	CoinAcceptor(Object caller, String name) {
		super(caller, name);
	}
}
