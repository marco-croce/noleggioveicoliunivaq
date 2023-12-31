package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NotificaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Notifica;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Prenotazione;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class GestionePrenotazioniController implements Initializable, DoubleDataInitializable<Prenotazione, Persona> {

	@FXML
	private Button inviaButton;
	@FXML
	private Button indietroButton;
	@FXML
	private TextArea messaggioTextArea;

	private Persona operatore;
	private ViewDispatcher dispatcher;
	private Prenotazione prenotazione;
	private NotificaService notificaService;

	public GestionePrenotazioniController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		notificaService = factory.getNotificaService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		inviaButton.disableProperty().bind(messaggioTextArea.textProperty().isEmpty());
	}

	@Override
	public void initializeData(Prenotazione prenotazione, Persona operatore) {
		this.prenotazione = prenotazione;
		this.operatore = operatore;
	}

	@FXML
	public void inviaAction() {

		Notifica notifica = new Notifica(LocalDate.now(), messaggioTextArea.getText(),
				(Utente) prenotazione.getUtente());

		try {
			notificaService.inviaNotifica(notifica);
			prenotazione.setGestito(true);// Imposta la prenotazione come gestita
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
		dispatcher.renderView(ViewOrder.getLastView(), operatore);

	}

	@FXML
	public void indietroAction() {
		dispatcher.renderView(ViewOrder.getLastView(), operatore);
	}

}
