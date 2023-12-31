package it.univaq.disim.oop.noleggioveicoliunivaq.business;

import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Feedback;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public interface FeedbackService {

	List<Feedback> visualizzaFeedbacks() throws BusinessException;

	List<Feedback> visualizzaFeedbacks(Veicolo veicolo) throws BusinessException;

	void scriviFeedback(String testo, Utente u, Veicolo v) throws BusinessException;

}
