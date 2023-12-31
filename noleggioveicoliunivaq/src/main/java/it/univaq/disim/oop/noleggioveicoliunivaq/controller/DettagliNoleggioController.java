package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DettagliNoleggioController implements Initializable, DoubleDataInitializable<Noleggio, Persona> {

	@FXML
	private Label idLabel;
	@FXML
	private Label kmLabel;
	@FXML
	private Label dataInizioLabel;
	@FXML
	private Label dataFineLabel;
	@FXML
	private Label pagamentoLabel;
	@FXML
	private Label idPrenotazioneLabel;
	@FXML
	private Label veicoloLabel;
	@FXML
	private Label interventiLabel;
	@FXML
	private Label utenteLabel;
	@FXML
	private Button indietroButton;

	private ViewDispatcher dispatcher;
	private Persona persona;

	public DettagliNoleggioController() {
		dispatcher = ViewDispatcher.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void initializeData(Noleggio noleggio, Persona persona) {
		this.persona = persona;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		idLabel.setText(noleggio.getId().toString());
		kmLabel.setText(noleggio.getChilometraggioPrevisto().toString());
		dataInizioLabel.setText(noleggio.getPrenotazione().getDataRitiro().format(formatter).toString());
		dataFineLabel.setText(noleggio.getDataRiconsegna().format(formatter).toString());

		if (noleggio.isPagamentoEffettuato()) {
			pagamentoLabel.setText("Pagato");
		} else {
			pagamentoLabel.setText("Da pagare");
		}

		idPrenotazioneLabel.setText(noleggio.getPrenotazione().getId().toString());
		veicoloLabel.setText(noleggio.getVeicolo().getTarga());

		if (noleggio.getPrenotazione().isGestito())
			interventiLabel.setText("Interventi Richiesti");
		else
			interventiLabel.setText("Nessun Intervento");

		utenteLabel.setText(noleggio.getPrenotazione().getUtente().getEmail().toString());
	}

	@FXML
	public void indietroAction(ActionEvent event) throws ViewException {
		dispatcher.renderView("noleggi", persona);
	}

}
