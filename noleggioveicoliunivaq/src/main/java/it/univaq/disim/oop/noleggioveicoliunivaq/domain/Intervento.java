package it.univaq.disim.oop.noleggioveicoliunivaq.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Intervento {

	private Integer id;
	private LocalDateTime dataIntervento;
	private LocalDate dataRichiestaIntervento;
	private String messaggio;
	private Utente utente;
	private Noleggio noleggio;
	private boolean sostituito;
	private boolean gestito;

	public Intervento(LocalDate dataRichiestaIntervento, String messaggio, Utente utente, Noleggio noleggio,
			boolean gestito) {
		this.dataRichiestaIntervento = dataRichiestaIntervento;
		this.messaggio = messaggio;
		this.gestito = gestito;
		this.utente = utente;
		this.noleggio = noleggio;
	}

	public Intervento() {

	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public LocalDateTime getDataIntervento() {
		return this.dataIntervento;
	}

	public void setDataIntervento(LocalDateTime dataIntervento) {
		this.dataIntervento = dataIntervento;
	}

	public LocalDate getDataRichiestaIntervento() {
		return this.dataRichiestaIntervento;
	}

	public void setDataRichiestaIntervento(LocalDate dataRichiestaIntervento) {
		this.dataRichiestaIntervento = dataRichiestaIntervento;
	}

	public String getMessaggio() {
		return this.messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public Utente getUtente() {
		return this.utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Noleggio getNoleggio() {
		return this.noleggio;
	}

	public void setNoleggio(Noleggio noleggio) {
		this.noleggio = noleggio;
	}

	public boolean isSostituito() {
		return this.sostituito;
	}

	public void setSostituito(boolean sostituito) {
		this.sostituito = sostituito;
	}

	public boolean isGestito() {
		return this.gestito;
	}

	public void setGestito(boolean gestito) {
		this.gestito = gestito;
	}

}
