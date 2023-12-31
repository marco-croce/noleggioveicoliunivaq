package it.univaq.disim.oop.noleggioveicoliunivaq.business;

import java.time.LocalDate;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Prenotazione;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tariffa;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public interface PrenotazioneService {

	List<Prenotazione> visualizzaPrenotazioni() throws BusinessException;

	void prenota(Prenotazione prenotazione, Integer chilometraggio, LocalDate dataRiconsegna, Double costo)
			throws BusinessException, PrenotazioneNonEffettuabileException, VeicoloNonPrenotabileException;

	public Double calcolaCosto(Tariffa tariffa, int periodo);

	public Tariffa scegliTariffa(int periodo) throws BusinessException;

	void setVeicoloScelto(String targa) throws BusinessException;

	Veicolo getVeicoloScelto() throws BusinessException;

	void aggiungiPrenotazione(Prenotazione p) throws BusinessException;

	Prenotazione getPrenotazione(Integer id) throws BusinessException, PrenotazioneNotFoundException;

}
