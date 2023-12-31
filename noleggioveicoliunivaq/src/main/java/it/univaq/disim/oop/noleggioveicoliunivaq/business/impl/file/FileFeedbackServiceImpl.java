package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.FeedbackService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Feedback;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class FileFeedbackServiceImpl implements FeedbackService {

	private String feedbackFileName;
	private UtenteService utenteService;
	private VeicoloService veicoloService;

	public FileFeedbackServiceImpl(String feedbackFileName, UtenteService utenteService,
			VeicoloService veicoloService) {
		this.feedbackFileName = feedbackFileName;
		this.utenteService = utenteService;
		this.veicoloService = veicoloService;
	}

	@Override
	public List<Feedback> visualizzaFeedbacks() throws BusinessException {
		List<Feedback> feedbacks = new ArrayList<>();
		try {
			FileData fileData = Utility.readAllRows(feedbackFileName);
			for (String[] righe : fileData.getRighe()) {
				Feedback feedback = new Feedback();
				feedback.setId(Integer.parseInt(righe[0]));
				feedback.setTesto(righe[1]);
				feedback.setData(LocalDateTime.parse(righe[2]));
				feedback.setUtente(utenteService.getUtente(righe[3]));
				feedback.setVeicolo(veicoloService.getVeicolo(righe[4]));
				feedbacks.add(feedback);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		} catch (VeicoloNotFoundException e) {
			e.printStackTrace();
		}
		return feedbacks;
	}

	@Override
	public List<Feedback> visualizzaFeedbacks(Veicolo veicolo) throws BusinessException {
		List<Feedback> feedbacks = new ArrayList<>();
		try {
			FileData fileData = Utility.readAllRows(feedbackFileName);
			for (String[] righe : fileData.getRighe()) {
				if (righe[4].equals(veicolo.getTarga())) {
					Feedback feedback = new Feedback();
					feedback.setId(Integer.parseInt(righe[0]));
					feedback.setTesto(righe[1]);
					feedback.setData(LocalDateTime.parse(righe[2]));
					feedback.setUtente(utenteService.getUtente(righe[3]));
					feedback.setVeicolo(veicoloService.getVeicolo(righe[4]));
					feedbacks.add(feedback);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		} catch (VeicoloNotFoundException e) {
			e.printStackTrace();
		}
		return feedbacks;
	}

	@Override
	public void scriviFeedback(String testo, Utente u, Veicolo v) throws BusinessException {
		try {
			FileData fileData = Utility.readAllRows(feedbackFileName);
			try (PrintWriter writer = new PrintWriter(new File(feedbackFileName))) {
				long contatore = fileData.getContatore();
				writer.println((contatore + 1));
				for (String[] righe : fileData.getRighe()) {
					writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
				StringBuilder row = new StringBuilder();
				row.append(contatore);
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(testo);
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(LocalDateTime.now().toString());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(u.getEmail());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(v.getTarga());
				row.append(Utility.SEPARATORE_COLONNA);
				writer.println(row.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

}
