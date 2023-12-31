package it.univaq.disim.oop.noleggioveicoliunivaq.business;

import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Operatore;

public interface OperatoreService {

	Set<Operatore> visualizzaOperatori() throws BusinessException;

	void aggiungiOperatore(Operatore operatore) throws BusinessException, OperatoreEsistenteException;

	void modificaOperatore(Operatore operatore) throws BusinessException;

	void eliminaOperatore(String email) throws BusinessException;

	Operatore getOperatore(String email) throws BusinessException, OperatoreNotFoundException;

}
