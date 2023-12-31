package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class TipologiaEsistenteException extends BusinessException {

	public TipologiaEsistenteException() {
	}

	public TipologiaEsistenteException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TipologiaEsistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public TipologiaEsistenteException(String message) {
		super(message);
	}

	public TipologiaEsistenteException(Throwable cause) {
		super(cause);
	}

}
