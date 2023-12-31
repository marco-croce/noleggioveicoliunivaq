package it.univaq.disim.oop.noleggioveicoliunivaq.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Noleggio {

	private Integer id;
	private Integer chilometraggioPrevisto;
	private Double costo;
	private LocalDate dataRiconsegna;
	private boolean pagamentoEffettuato;
	private Prenotazione prenotazione;
	private Veicolo veicolo;

	private Set<Intervento> interventi = new HashSet<>();
	private List<Veicolo> veicoliSostitutivi = new ArrayList<>();

	public Noleggio(Integer chilometraggioPrevisto, LocalDate dataRiconsegna, boolean pagamentoEffettuato,
			Prenotazione prenotazione, Veicolo veicolo, Integer id) {
		this.chilometraggioPrevisto = chilometraggioPrevisto;
		this.dataRiconsegna = dataRiconsegna;
		this.pagamentoEffettuato = pagamentoEffettuato;
		this.prenotazione = prenotazione;
		this.veicolo = veicolo;
		this.id = id;
	}

	public Noleggio(Integer chilometraggioPrevisto, LocalDate dataRiconsegna, boolean pagamentoEffettuato,
			Prenotazione prenotazione, Veicolo veicolo) {
		this.chilometraggioPrevisto = chilometraggioPrevisto;
		this.dataRiconsegna = dataRiconsegna;
		this.pagamentoEffettuato = pagamentoEffettuato;
		this.prenotazione = prenotazione;
		this.veicolo = veicolo;
	}

	public Noleggio() {

	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getChilometraggioPrevisto() {
		return this.chilometraggioPrevisto;
	}

	public void setChilometraggioPrevisto(Integer chilometraggioPrevisto) {
		this.chilometraggioPrevisto = chilometraggioPrevisto;
	}

	public Double getCosto() {
		return this.costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public LocalDate getDataRiconsegna() {
		return this.dataRiconsegna;
	}

	public void setDataRiconsegna(LocalDate dataRiconsegna) {
		this.dataRiconsegna = dataRiconsegna;
	}

	public boolean isPagamentoEffettuato() {
		return this.pagamentoEffettuato;
	}

	public void setPagamentoEffettuato(boolean pagamentoEffettuato) {
		this.pagamentoEffettuato = pagamentoEffettuato;
	}

	public Prenotazione getPrenotazione() {
		return this.prenotazione;
	}

	public void setPrenotazione(Prenotazione prenotazione) {
		this.prenotazione = prenotazione;
	}

	public Veicolo getVeicolo() {
		return this.veicolo;
	}

	public void setVeicolo(Veicolo v) {
		this.veicolo = v;
	}

	public Set<Intervento> getInterventi() {
		return this.interventi;
	}

	public void addIntervento(Intervento intervento) {
		this.interventi.add(intervento);
	}

	public List<Veicolo> getVeicoliSostitutivi() {
		return this.veicoliSostitutivi;
	}

	public void addVeicoloSostitutivo(Veicolo veicolo) {
		this.veicoliSostitutivi.add(veicolo);
	}

}
