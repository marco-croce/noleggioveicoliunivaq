package it.univaq.disim.oop.noleggioveicoliunivaq.domain;

import java.util.HashSet;
import java.util.Set;

public class Veicolo {

	private String marca, modello, targa, colore;
	private Integer potenza, cilindrata, chilometriPercorsi, numeroPorte, numeroPosti;
	private Double consumo;
	private Stato stato;
	private Alimentazione alimentazione;
	private Cambio cambio;
	private Tipologia tipologia;
	private Set<Feedback> feedbacks = new HashSet<>();
	private Set<Tariffa> tariffe = new HashSet<>();
	private Set<Noleggio> noleggi = new HashSet<>();

	public Veicolo() {

	}

	public Veicolo(String marca, String modello, String targa, String colore, Integer potenza, Integer cilindrata,
			Integer chilometriPercorsi, Integer numeroPorte, Integer numeroPosti, Double consumo, Stato stato,
			Alimentazione alimentazione, Cambio cambio, Tipologia tipologia, Tariffa tariffa) {
		this.marca = marca;
		this.modello = modello;
		this.targa = targa;
		this.colore = colore;
		this.potenza = potenza;
		this.cilindrata = cilindrata;
		this.chilometriPercorsi = chilometriPercorsi;
		this.numeroPorte = numeroPorte;
		this.numeroPosti = numeroPosti;
		this.consumo = consumo;
		this.stato = stato;
		this.alimentazione = alimentazione;
		this.cambio = cambio;
		this.tipologia = tipologia;
		this.tariffe.add(tariffa);
	}

	public String getMarca() {
		return this.marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModello() {
		return this.modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getTarga() {
		return this.targa;
	}

	public void setTarga(String targa) {
		this.targa = targa;
	}

	public String getColore() {
		return this.colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public Integer getPotenza() {
		return this.potenza;
	}

	public void setPotenza(Integer potenza) {
		this.potenza = potenza;
	}

	public Integer getCilindrata() {
		return this.cilindrata;
	}

	public void setCilindrata(Integer cilindrata) {
		this.cilindrata = cilindrata;
	}

	public Integer getChilometriPercorsi() {
		return this.chilometriPercorsi;
	}

	public void setChilometriPercorsi(Integer chilometriPercorsi) {
		this.chilometriPercorsi = chilometriPercorsi;
	}

	public Integer getNumeroPorte() {
		return this.numeroPorte;
	}

	public void setNumeroPorte(Integer numeroPorte) {
		this.numeroPorte = numeroPorte;
	}

	public Integer getNumeroPosti() {
		return this.numeroPosti;
	}

	public void setNumeroPosti(Integer numeroPosti) {
		this.numeroPosti = numeroPosti;
	}

	public Double getConsumo() {
		return this.consumo;
	}

	public void setConsumo(Double consumo) {
		this.consumo = consumo;
	}

	public Stato getStato() {
		return this.stato;
	}

	public void setStato(Stato stato) {
		this.stato = stato;
	}

	public Alimentazione getAlimentazione() {
		return this.alimentazione;
	}

	public void setAlimentazione(Alimentazione alimentazione) {
		this.alimentazione = alimentazione;
	}

	public Cambio getCambio() {
		return this.cambio;
	}

	public void setCambio(Cambio cambio) {
		this.cambio = cambio;
	}

	public Set<Feedback> getFeedbacks() {
		return this.feedbacks;
	}

	public void addFeedback(Feedback feedback) {
		this.feedbacks.add(feedback);
	}

	public Tipologia getTipologia() {
		return this.tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public Set<Tariffa> getTariffe() {
		return this.tariffe;
	}

	public void addTariffa(Tariffa tariffa) {
		this.tariffe.add(tariffa);
	}

	public Set<Noleggio> getNoleggi() {
		return this.noleggi;
	}

	public void addNoleggio(Noleggio noleggio) {
		this.noleggi.add(noleggio);
	}

}
