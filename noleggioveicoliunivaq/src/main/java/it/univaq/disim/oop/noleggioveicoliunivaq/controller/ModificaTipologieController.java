package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tipologia;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ModificaTipologieController implements Initializable, DataInitializable<Tipologia> {

	@FXML
	private TextField nome;
	@FXML
	private TextField id;
	@FXML
	private Label errorLabel;
	@FXML
	private Button salvaButton;
	@FXML
	private Button annullaButton;

	private ViewDispatcher dispatcher;
	private TipologiaService tipologiaService;
	private Tipologia tipologia;

	public ModificaTipologieController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		tipologiaService = factory.getTipologiaService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		salvaButton.disableProperty().bind(nome.textProperty().isEmpty().or(id.textProperty().isEmpty()));
	}

	@Override
	public void initializeData(Tipologia tipologia) {
		this.tipologia = tipologia;

		if (tipologia.getId() == null) {
			nome.setText(null);
			id.setText(tipologiaService.getIdCounter().toString());

		} else {
			nome.setText(tipologia.getNome());
			id.setText(tipologia.getId().toString());
		}
	}

	@FXML
	public void salvaAction(ActionEvent event) {

		try {
			tipologia.setNome(nome.getText());

			// Serve per verificare se l'amministratore ha cliccato sul pulsante
			// Aggiungi Veicolo oppure sul pulsante Modifica
			if (tipologia.getId() == null) {
				tipologia.setId(Integer.parseInt(id.getText()));
				tipologiaService.aggiungiTipologia(tipologia);
			} else {
				tipologia.setId(Integer.parseInt(id.getText()));
				tipologiaService.modificaTipologia(tipologia);
			}

			dispatcher.renderView("tipologie", tipologia);

		} catch (TipologiaEsistenteException e) {
			errorLabel.setText("La tipologia esiste già");
			errorLabel.setStyle("-fx-border-color: red;");
			nome.setText(null);
			id.setText(tipologiaService.getIdCounter().toString());
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}

	}

	@FXML
	public void annullaAction(ActionEvent event) throws ViewException {
		dispatcher.renderView("tipologie", null);
	}

}
