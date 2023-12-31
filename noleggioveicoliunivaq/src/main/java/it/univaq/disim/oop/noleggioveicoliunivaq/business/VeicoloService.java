package it.univaq.disim.oop.noleggioveicoliunivaq.business;

import java.time.LocalDate;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Alimentazione;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Stato;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tipologia;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public interface VeicoloService {

	List<Veicolo> visualizzaVeicoli() throws BusinessException;

	List<String> visualizzaNomiTipologie() throws BusinessException;

	List<Veicolo> visualizzaVeicoli(Stato stato) throws BusinessException;

	List<Veicolo> visualizzaVeicoli(Stato stato, Stato stato1) throws BusinessException;

	List<Veicolo> visualizzaVeicoli(Alimentazione alimentazione) throws BusinessException;

	List<Veicolo> visualizzaVeicoli(Tipologia tipologia) throws BusinessException;

	List<Veicolo> visualizzaVeicoli(LocalDate dataInizio, LocalDate dataFine) throws BusinessException;

	public Integer getNumeroVeicoli(Tipologia t) throws BusinessException;

	void aggiungiVeicolo(Veicolo v) throws BusinessException, VeicoloEsistenteException;

	void eliminaVeicolo(Veicolo v) throws BusinessException, VeicoloNonEliminabileException;

	void modificaVeicolo(Veicolo v) throws BusinessException;

	List<Veicolo> visualizzaVeicoli(double parseDouble, double parseDouble2) throws BusinessException;

	Veicolo getVeicolo(String targa) throws BusinessException, VeicoloNotFoundException;

}
