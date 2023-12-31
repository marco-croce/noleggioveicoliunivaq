package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class PrenotazioneNonEffettuabileException extends Exception {

	public PrenotazioneNonEffettuabileException() {
	}

	public PrenotazioneNonEffettuabileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PrenotazioneNonEffettuabileException(String message, Throwable cause) {
		super(message, cause);
	}

	public PrenotazioneNonEffettuabileException(String message) {
		super(message);
	}

	public PrenotazioneNonEffettuabileException(Throwable cause) {
		super(cause);
	}

}
