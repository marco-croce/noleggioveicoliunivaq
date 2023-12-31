package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class OperatoreNotFoundException extends Exception {

	public OperatoreNotFoundException() {
		super();
	}

	public OperatoreNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public OperatoreNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperatoreNotFoundException(String message) {
		super(message);
	}

	public OperatoreNotFoundException(Throwable cause) {
		super(cause);
	}

}
