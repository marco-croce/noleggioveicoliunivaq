package it.univaq.disim.oop.noleggioveicoliunivaq.business;

@SuppressWarnings("serial")
public class VeicoloEsistenteException extends BusinessException {

	public VeicoloEsistenteException() {
	}

	public VeicoloEsistenteException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public VeicoloEsistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public VeicoloEsistenteException(String message) {
		super(message);
	}

	public VeicoloEsistenteException(Throwable cause) {
		super(cause);
	}

}
