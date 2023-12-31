package it.univaq.disim.oop.noleggioveicoliunivaq.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Notifica {

	private Integer id;
	private LocalDate dataCreazione;
	private LocalDateTime data;
	private String messaggio;
	private Utente utente;

	public Notifica(LocalDate dataCreazione, LocalDateTime data, String messaggio, Utente utente) {
		this.dataCreazione = dataCreazione;
		this.data = data;
		this.messaggio = messaggio;
		this.utente = utente;
	}

	public Notifica(LocalDate dataCreazione, String messaggio, Utente utente) {
		this.dataCreazione = dataCreazione;
		this.messaggio = messaggio;
		this.utente = utente;
	}

	public Notifica() {

	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setDataCreazione(LocalDate l) {
		this.dataCreazione = l;
	}

	public LocalDate getDataCreazione() {
		return this.dataCreazione;
	}

	public void setData(LocalDateTime d) {
		this.data = d;
	}

	public LocalDateTime getData() {
		return this.data;
	}

	public void setMessaggio(String m) {
		this.messaggio = m;
	}

	public String getMessaggio() {
		return this.messaggio;
	}

	public void setUtente(Utente u) {
		this.utente = u;
	}

	public Utente getUtente() {
		return this.utente;
	}
}
