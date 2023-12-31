package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class PasswordDiverseException extends BusinessException {

	public PasswordDiverseException() {

	}

	public PasswordDiverseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PasswordDiverseException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordDiverseException(String message) {
		super(message);
	}

	public PasswordDiverseException(Throwable cause) {
		super(cause);
	}

}
