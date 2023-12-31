package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class VeicoloNotFoundException extends Exception {

	public VeicoloNotFoundException() {
	}

	public VeicoloNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public VeicoloNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public VeicoloNotFoundException(String message) {
		super(message);
	}

	public VeicoloNotFoundException(Throwable cause) {
		super(cause);
	}

}
