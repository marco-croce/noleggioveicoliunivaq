package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DettagliVeicoliController implements Initializable, DoubleDataInitializable<Veicolo, Persona> {

	@FXML
	private Label marcaLabel;
	@FXML
	private Label modelloLabel;
	@FXML
	private Label targaLabel;
	@FXML
	private Label coloreLabel;
	@FXML
	private Label potenzaLabel;
	@FXML
	private Label cilindrataLabel;
	@FXML
	private Label kmLabel;
	@FXML
	private Label nPorteLabel;
	@FXML
	private Label nPostiLabel;
	@FXML
	private Label consumoLabel;
	@FXML
	private Label statoLabel;
	@FXML
	private Label alimentazioneLabel;
	@FXML
	private Label cambioLabel;
	@FXML
	private Label tipologiaLabel;
	@FXML
	private Button indietroButton;

	private ViewDispatcher dispatcher;
	private Persona persona;

	public DettagliVeicoliController() {
		dispatcher = ViewDispatcher.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void initializeData(Veicolo veicolo, Persona persona) {
		this.persona = persona;

		marcaLabel.setText(veicolo.getMarca());
		modelloLabel.setText(veicolo.getModello());
		targaLabel.setText(veicolo.getTarga());
		coloreLabel.setText(veicolo.getColore());
		potenzaLabel.setText(veicolo.getPotenza().toString());
		cilindrataLabel.setText(veicolo.getCilindrata().toString());
		kmLabel.setText(veicolo.getChilometriPercorsi().toString());
		nPorteLabel.setText(veicolo.getNumeroPorte().toString());
		nPostiLabel.setText(veicolo.getNumeroPosti().toString());
		consumoLabel.setText(veicolo.getConsumo().toString());
		statoLabel.setText(veicolo.getStato().toString());
		alimentazioneLabel.setText(veicolo.getAlimentazione().toString());
		cambioLabel.setText(veicolo.getCambio().toString());
		tipologiaLabel.setText(veicolo.getTipologia().getNome());
	}

	@FXML
	public void indietroAction(ActionEvent event) throws ViewException {
		dispatcher.renderView(ViewOrder.getLastView(), persona);
	}

}
