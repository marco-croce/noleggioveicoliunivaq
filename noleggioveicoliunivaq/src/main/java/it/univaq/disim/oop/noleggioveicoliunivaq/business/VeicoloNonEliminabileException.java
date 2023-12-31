package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class VeicoloNonEliminabileException extends BusinessException {

	public VeicoloNonEliminabileException() {
	}

	public VeicoloNonEliminabileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public VeicoloNonEliminabileException(String message, Throwable cause) {
		super(message, cause);
	}

	public VeicoloNonEliminabileException(String message) {
		super(message);
	}

	public VeicoloNonEliminabileException(Throwable cause) {
		super(cause);
	}

}
