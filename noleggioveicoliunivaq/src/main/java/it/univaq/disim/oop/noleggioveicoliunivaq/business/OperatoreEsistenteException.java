package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class OperatoreEsistenteException extends Exception {

	public OperatoreEsistenteException() {
	}

	public OperatoreEsistenteException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public OperatoreEsistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperatoreEsistenteException(String message) {
		super(message);
	}

	public OperatoreEsistenteException(Throwable cause) {
		super(cause);
	}

}
