package it.univaq.disim.oop.noleggioveicoliunivaq.business;

import java.time.LocalDate;

import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;

public interface NoleggioService {

	void aggiungiNoleggio(Noleggio noleggio) throws BusinessException;

	List<Noleggio> visualizzaNoleggiInCorso() throws BusinessException;

	List<Noleggio> visualizzaNoleggi() throws BusinessException;

	List<Noleggio> visualizzaNoleggiAssistiti() throws BusinessException;

	List<Noleggio> visualizzaNoleggiInRiconsegna(LocalDate data) throws BusinessException;

	Noleggio getNoleggio(Integer id) throws BusinessException, NoleggioNotFoundException;

	void gestioneRiconsegna(Noleggio noleggio, Integer km, Double costo) throws BusinessException;

}
