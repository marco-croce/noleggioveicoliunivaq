package it.univaq.disim.oop.noleggioveicoliunivaq.business;

import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;

public interface UtenteService {

	Set<Utente> visualizzaUtenti() throws BusinessException;

	void aggiungiUtente(Utente utente) throws BusinessException, UtenteEsistenteException;

	void modificaUtente(Utente utente) throws BusinessException;

	void eliminaUtente(String email) throws BusinessException;

	Utente getUtente(String email) throws BusinessException, UtenteNotFoundException;

}
