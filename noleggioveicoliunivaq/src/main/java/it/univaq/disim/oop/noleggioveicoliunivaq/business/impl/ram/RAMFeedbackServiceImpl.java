package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.FeedbackService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Feedback;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class RAMFeedbackServiceImpl implements FeedbackService {

	private static List<Feedback> feedbacks = new ArrayList<>();
	private static Integer idCounter = 1;

	@Override
	public List<Feedback> visualizzaFeedbacks() throws BusinessException {
		return feedbacks;
	}

	@Override
	public List<Feedback> visualizzaFeedbacks(Veicolo veicolo) throws BusinessException {
		List<Feedback> result = new ArrayList<>();
		for (Feedback feedback : feedbacks) {
			if (feedback.getVeicolo().getTarga().equals(veicolo.getTarga()))
				result.add(feedback);
		}
		return result;
	}

	@Override
	public void scriviFeedback(String testo, Utente u, Veicolo v) throws BusinessException {
		feedbacks.add(new Feedback(testo, LocalDateTime.now(), u, v, idCounter++));
	}
}