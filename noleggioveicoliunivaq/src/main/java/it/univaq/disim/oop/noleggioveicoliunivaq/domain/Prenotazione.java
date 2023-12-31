package it.univaq.disim.oop.noleggioveicoliunivaq.domain;

import java.time.LocalDate;

public class Prenotazione {

	private Integer id;
	private LocalDate dataRitiro;
	private boolean gestito;
	private Utente utente;
	private Noleggio noleggio;

	public Prenotazione(LocalDate dataRitiro, Utente utente, Integer id) {
		this.id = id;
		this.dataRitiro = dataRitiro;
		this.utente = utente;
	}

	public Prenotazione(LocalDate dataRitiro, Utente utente) {
		this.dataRitiro = dataRitiro;
		this.utente = utente;
	}

	public Prenotazione() {

	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getDataRitiro() {
		return this.dataRitiro;
	}

	public void setDataRitiro(LocalDate dataRitiro) {
		this.dataRitiro = dataRitiro;
	}

	public boolean isGestito() {
		return this.gestito;
	}

	public void setGestito(Boolean gestito) {
		this.gestito = gestito;
	}

	public Utente getUtente() {
		return this.utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public void setNoleggio(Noleggio n) {
		this.noleggio = n;
	}

	public Noleggio getNoleggio() {
		return this.noleggio;
	}

}
