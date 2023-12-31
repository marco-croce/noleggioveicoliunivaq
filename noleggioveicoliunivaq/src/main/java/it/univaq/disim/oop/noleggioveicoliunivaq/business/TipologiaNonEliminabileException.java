package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class TipologiaNonEliminabileException extends BusinessException {

	public TipologiaNonEliminabileException() {
	}

	public TipologiaNonEliminabileException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TipologiaNonEliminabileException(String message, Throwable cause) {
		super(message, cause);
	}

	public TipologiaNonEliminabileException(String message) {
		super(message);
	}

	public TipologiaNonEliminabileException(Throwable cause) {
		super(cause);
	}

}
