package it.univaq.disim.oop.noleggioveicoliunivaq.domain;

import java.time.LocalDateTime;

public class Feedback {

	private Integer id;
	private String testo;
	private LocalDateTime data;
	private Utente utente;
	private Veicolo veicolo;

	public Feedback(String testo, LocalDateTime data, Utente utente, Veicolo veicolo, Integer id) {
		this.testo = testo;
		this.data = data;
		this.utente = utente;
		this.veicolo = veicolo;
		this.id = id;
	}

	public Feedback(String testo, LocalDateTime data, Utente utente, Veicolo veicolo) {
		this.testo = testo;
		this.data = data;
		this.utente = utente;
		this.veicolo = veicolo;
	}

	public Feedback() {

	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTesto() {
		return this.testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public LocalDateTime getData() {
		return this.data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Utente getUtente() {
		return this.utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Veicolo getVeicolo() {
		return this.veicolo;
	}

	public void setVeicolo(Veicolo veicolo) {
		this.veicolo = veicolo;
	}

}
