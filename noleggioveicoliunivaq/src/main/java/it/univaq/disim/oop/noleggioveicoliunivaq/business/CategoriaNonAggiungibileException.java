package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class CategoriaNonAggiungibileException extends Exception {

	public CategoriaNonAggiungibileException() {
	}

	public CategoriaNonAggiungibileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CategoriaNonAggiungibileException(String message, Throwable cause) {
		super(message, cause);
	}

	public CategoriaNonAggiungibileException(String message) {
		super(message);
	}

	public CategoriaNonAggiungibileException(Throwable cause) {
		super(cause);
	}

}