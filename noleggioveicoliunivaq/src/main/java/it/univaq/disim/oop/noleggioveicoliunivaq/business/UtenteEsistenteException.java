package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class UtenteEsistenteException extends Exception {

	public UtenteEsistenteException() {
	}

	public UtenteEsistenteException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UtenteEsistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtenteEsistenteException(String message) {
		super(message);
	}

	public UtenteEsistenteException(Throwable cause) {
		super(cause);
	}

}
