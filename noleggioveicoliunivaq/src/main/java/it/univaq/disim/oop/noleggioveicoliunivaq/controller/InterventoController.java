package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.InterventoService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Intervento;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class InterventoController implements Initializable, DataInitializable<Utente> {

	@FXML
	private TextField messaggioTextField;
	@FXML
	private Button richiestaButton;
	@FXML
	private Button homeButton;
	@FXML
	private Label richiestaLabel;

	private Utente utente;
	private ViewDispatcher dispatcher;
	private Intervento intervento;
	private NoleggioService noleggioService;
	private InterventoService interventoService;

	public InterventoController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		noleggioService = factory.getNoleggioService();
		interventoService = factory.getInterventoService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		richiestaButton.disableProperty().bind(messaggioTextField.textProperty().isEmpty());
	}

	@Override
	public void initializeData(Utente utente) {
		this.utente = utente;
	}

	@FXML
	public void richiestaAction() throws BusinessException, PrenotazioneNotFoundException, NoleggioNotFoundException {

		for (Noleggio n : noleggioService.visualizzaNoleggiInCorso()) {
			if (n.getPrenotazione().getUtente().getEmail().equals(utente.getEmail())) {
				intervento = new Intervento(LocalDate.now(), messaggioTextField.getText(), utente, n, false);
				n.addIntervento(intervento);
				interventoService.richiediIntervento(intervento);
				richiestaLabel.setText("Richiesta di intervento effettuata con successo!");
				richiestaLabel.setStyle("-fx-border-color: yellow;");
				messaggioTextField.setText(null);
				intervento = null;
				return;
			}
		}

		richiestaLabel.setText("Non ha nessun noleggio in corso!");
		richiestaLabel.setStyle("-fx-border-color: red;");
		messaggioTextField.setText(null);
	}

	@FXML
	public void homeAction() throws BusinessException {
		dispatcher.renderView("home", utente);
	}

}
