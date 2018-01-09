package pl.dev.bartek.messenger.exception;

public class DataNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 123456;
	
	public DataNotFoundException(String message) {
		super(message);
	}

}
