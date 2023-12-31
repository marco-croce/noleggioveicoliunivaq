package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class NoleggioNotFoundException extends Exception {

	public NoleggioNotFoundException() {
	}

	public NoleggioNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoleggioNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoleggioNotFoundException(String message) {
		super(message);
	}

	public NoleggioNotFoundException(Throwable cause) {
		super(cause);
	}

}
