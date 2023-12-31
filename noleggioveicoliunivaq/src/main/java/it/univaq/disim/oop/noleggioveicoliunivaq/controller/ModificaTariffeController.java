package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.CategoriaNonAggiungibileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TariffaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Categoria;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tariffa;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ModificaTariffeController implements Initializable, DoubleDataInitializable<Tariffa, Veicolo> {

	@FXML
	private ComboBox<Categoria> categoria;
	@FXML
	private TextField prezzo;
	@FXML
	private TextField id;
	@FXML
	private Button salvaButton;
	@FXML
	private Button annullaButton;
	@FXML
	private Label errorLabel;

	private ViewDispatcher dispatcher;
	private TariffaService tariffaService;
	private Tariffa tariffa;
	private Veicolo veicolo;

	public ModificaTariffeController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		tariffaService = factory.getTariffaService();
	}

	@FXML
	public void annullaAction(ActionEvent event) throws ViewException {
		dispatcher.renderView("tariffe", veicolo);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		salvaButton.disableProperty().bind(categoria.valueProperty().isNull().or(prezzo.textProperty().isEmpty()));
	}

	@Override
	public void initializeData(Tariffa tariffa, Veicolo veicolo) {
		this.tariffa = tariffa;
		this.veicolo = veicolo;

		categoria.getItems().setAll(Categoria.values());

		if (!(tariffa.getId() == null)) {
			this.categoria.setValue(tariffa.getCategoria());
			this.prezzo.setText(tariffa.getPrezzo().toString());
			this.id.setText(tariffa.getId().toString());
		} else {
			this.categoria.setValue(null);
			this.prezzo.setText(null);
			try {
				id.setText(tariffaService.getIdCounter().toString());
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	public void salvaAction(ActionEvent event) {

		try {
			tariffa.setCategoria(categoria.getValue());
			tariffa.setPrezzo(Double.parseDouble(prezzo.getText()));
			if (tariffa.getId() == null) {
				tariffa.setId(Integer.parseInt(id.getText()));
				tariffaService.aggiungiTariffa(tariffa, veicolo);
			} else {
				tariffaService.modificaTariffa(tariffa);
			}
			dispatcher.renderView("tariffe", veicolo);
		} catch (CategoriaNonAggiungibileException e) {
			categoria.setValue(null);
			prezzo.setText(null);
			errorLabel.setText("La tariffa esiste già");
			errorLabel.setStyle("-fx-border-color: red;");
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}

	}

}
