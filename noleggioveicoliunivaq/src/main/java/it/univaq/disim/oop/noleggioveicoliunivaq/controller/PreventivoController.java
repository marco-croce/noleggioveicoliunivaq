package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneNonEffettuabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNonPrenotabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Prenotazione;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tariffa;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class PreventivoController implements Initializable, DataInitializable<Utente> {

	@FXML
	private Button scegliVeicoloButton;
	@FXML
	private DatePicker dataRitiroDate;
	@FXML
	private DatePicker dataRiconsegnaDate;
	@FXML
	private ComboBox<Integer> chilometraggioCombo;
	@FXML
	private Button prenotaButton;
	@FXML
	private Button calcolaButton;
	@FXML
	private Button homeButton;
	@FXML
	private Label costoLabel;
	@FXML
	private Label prenotazioneLabel;

	private ViewDispatcher dispatcher;
	private Utente utente;
	private PrenotazioneService prenotazioneService;
	private Prenotazione prenotazione;
	private Double costo;

	public PreventivoController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		prenotazioneService = factory.getPrenotazioneService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		chilometraggioCombo.getItems().addAll(50, 100, 250, 500);
		calcolaButton.disableProperty().bind(dataRitiroDate.valueProperty().isNull()
				.or(dataRiconsegnaDate.valueProperty().isNull().or(chilometraggioCombo.valueProperty().isNull())));
		prenotaButton.disableProperty().bind(costoLabel.textProperty().isNull());

		/*
		 * In base alla data odierna disabilita tutte le date precedenti a quella
		 * odierna
		 */
		dataRitiroDate.setDayCellFactory(picker -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				if (dataRiconsegnaDate.getValue() == null)
					setDisable(empty || date.compareTo(today) < 0);
				else
					setDisable(empty || date.compareTo(today) < 0 || date.compareTo(dataRiconsegnaDate.getValue()) > 0);
			}
		});

		/*
		 * In base alla data inserita nel DatePicker dataRitiroDate, se essa è stata
		 * inserita, disabilita tutte le date precedenti ad essa
		 */
		dataRiconsegnaDate.setDayCellFactory(picker -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				if (dataRitiroDate.getValue() == null)
					setDisable(empty || date.compareTo(today) < 0);
				else
					setDisable(empty || date.compareTo(today) < 0 || date.compareTo(dataRitiroDate.getValue()) < 0);
			}
		});
	}

	@Override
	public void initializeData(Utente utente) {
		this.utente = utente;

	}

	@FXML
	public void scegliVeicoloAction() throws ViewException, BusinessException {
		ViewOrder.addLastView("preventivo");
		dispatcher.renderView("sceltaVeicolo", utente);
	}

	@FXML
	public void prenotaAction() throws BusinessException {
		if (prenotazioneService.getVeicoloScelto() == null) {
			prenotazioneLabel.setText("Scegliere il veicolo che si vuole prenotare!");
			prenotazioneLabel.setStyle("-fx-border-color: red;");
			dataRitiroDate.setValue(null);
			dataRiconsegnaDate.setValue(null);
			chilometraggioCombo.valueProperty().setValue(null);
			return;
		}

		prenotazione = new Prenotazione(dataRitiroDate.getValue(), utente);

		try {
			prenotazioneService.prenota(prenotazione, chilometraggioCombo.getSelectionModel().getSelectedItem(),
					dataRiconsegnaDate.getValue(), costo);
		} catch (VeicoloNonPrenotabileException e) {
			prenotazioneLabel.setText("Veicolo non disponibile in quel periodo di tempo!");
			prenotazioneLabel.setStyle("-fx-border-color: red;");
			dataRitiroDate.setValue(null);
			dataRiconsegnaDate.setValue(null);
			chilometraggioCombo.valueProperty().setValue(null);
		} catch (PrenotazioneNonEffettuabileException e) {
			prenotazioneLabel.setText("Hai già prenotato un noleggio in quel periodo di tempo!");
			prenotazioneLabel.setStyle("-fx-border-color: red;");
			dataRitiroDate.setValue(null);
			dataRiconsegnaDate.setValue(null);
			chilometraggioCombo.valueProperty().setValue(null);
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
		dataRitiroDate.setValue(null);
		dataRiconsegnaDate.setValue(null);
		chilometraggioCombo.valueProperty().setValue(null);
		prenotazioneLabel.setText("Prenotazione effettuata!");
		prenotazioneLabel.setStyle("-fx-border-color: yellow;");
	}

	@FXML
	public void calcolaAction() throws BusinessException {
		if (prenotazioneService.getVeicoloScelto() == null) {
			prenotazioneLabel.setText("Scegliere il veicolo da prenotare!");
			prenotazioneLabel.setStyle("-fx-border-color: red;");
			dataRitiroDate.setValue(null);
			dataRiconsegnaDate.setValue(null);
			chilometraggioCombo.valueProperty().setValue(null);
			return;
		}

		int periodo = (int) Duration
				.between(dataRitiroDate.getValue().atStartOfDay(), dataRiconsegnaDate.getValue().atStartOfDay())
				.toDays();

		// Evita di avere un periodo uguale a zero se
		// Data di ritiro e di riconsegna coincidono
		periodo++;

		Tariffa tariffaScelta = prenotazioneService.scegliTariffa(periodo);
		costo = prenotazioneService.calcolaCosto(tariffaScelta, periodo);

		costoLabel.setText("Il costo previsto per il noleggio" + " è " + costo + " €");
	}

	@FXML
	public void homeAction() throws BusinessException {
		ViewOrder.deleteAllView();
		dispatcher.loggedIn(utente);
	}

}
