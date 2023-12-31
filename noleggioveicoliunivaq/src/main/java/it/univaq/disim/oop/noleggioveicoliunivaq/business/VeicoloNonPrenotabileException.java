package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class VeicoloNonPrenotabileException extends Exception {

	public VeicoloNonPrenotabileException() {
	}

	public VeicoloNonPrenotabileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public VeicoloNonPrenotabileException(String message, Throwable cause) {
		super(message, cause);
	}

	public VeicoloNonPrenotabileException(String message) {
		super(message);
	}

	public VeicoloNonPrenotabileException(Throwable cause) {
		super(cause);
	}

}
