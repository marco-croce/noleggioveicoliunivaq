package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.FeedbackService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NotificaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Notifica;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ScritturaFeedbackController implements Initializable, DoubleDataInitializable<Notifica, Persona> {

	@FXML
	private TextField messaggioTextField;
	@FXML
	private Button scriviButton;
	@FXML
	private Button indietroButton;

	private FeedbackService feedbackService;
	private NoleggioService noleggioService;
	private NotificaService notificaService;
	private Persona persona;
	private Notifica notifica;
	private ViewDispatcher dispatcher;

	public ScritturaFeedbackController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		feedbackService = factory.getFeedbackService();
		noleggioService = factory.getNoleggioService();
		notificaService = factory.getNotificaService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		scriviButton.disableProperty().bind(messaggioTextField.textProperty().isEmpty());
	}

	@Override
	public void initializeData(Notifica n, Persona p) {
		this.notifica = n;
		this.persona = p;
	}

	@FXML
	public void scriviAction() {
		Veicolo veicoloFeedback = null;
		
		// Ricerca del veicolo riguardante la notifica
		try {
			for (Noleggio n : noleggioService.visualizzaNoleggiInRiconsegna(notifica.getDataCreazione()))
				if (n.getPrenotazione().getUtente().getEmail().equals(persona.getEmail()))
					veicoloFeedback = n.getVeicolo();
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}

		try {
			feedbackService.scriviFeedback(messaggioTextField.getText(), notifica.getUtente(), veicoloFeedback);
			//Modifica il messaggio della notifica inserendo un testo
			//di ringraziamento per aver lasciato il feedback sul veicolo
			notificaService.modificaNotifica(notifica);
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
		ViewOrder.getLastView();
		dispatcher.renderView(ViewOrder.getLastView(), persona);

	}

	@FXML
	public void indietroAction() {
		dispatcher.renderView(ViewOrder.getLastView(), notifica, persona);
	}

}
