package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class PrenotazioneNotFoundException extends Exception {

	public PrenotazioneNotFoundException() {
		super();
	}

	public PrenotazioneNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PrenotazioneNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrenotazioneNotFoundException(String message) {
		super(message);
	}

	public PrenotazioneNotFoundException(Throwable cause) {
		super(cause);
	}

}
