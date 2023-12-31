package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNonEliminabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Alimentazione;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Cambio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Stato;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tipologia;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class ModificaVeicoliController implements Initializable, DataInitializable<Veicolo> {

	@FXML
	private TextField marca;
	@FXML
	private TextField modello;
	@FXML
	private TextField targa;
	@FXML
	private TextField colore;
	@FXML
	private TextField potenza;
	@FXML
	private TextField kmPercorsi;
	@FXML
	private TextField nPorte;
	@FXML
	private TextField nPosti;
	@FXML
	private TextField consumo;
	@FXML
	private ComboBox<Stato> stato;
	@FXML
	private ComboBox<Alimentazione> alimentazione;
	@FXML
	private ComboBox<Cambio> cambio;
	@FXML
	private ComboBox<Tipologia> tipologia;
	@FXML
	private TextField cilindrata;
	@FXML
	private Button salvaButton;
	@FXML
	private Button eliminaButton;
	@FXML
	private Button annullaButton;
	@FXML
	private Label errorLabel;

	private ViewDispatcher dispatcher;
	private VeicoloService veicoloService;
	private TipologiaService tipologiaService;
	private Veicolo veicolo;

	public ModificaVeicoliController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		tipologiaService = factory.getTipologiaService();
		veicoloService = factory.getVeicoloService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		salvaButton.disableProperty()
				.bind(marca.textProperty().isEmpty().or(modello.textProperty().isEmpty())
						.or(targa.textProperty().isEmpty()).or(colore.textProperty().isEmpty())
						.or(potenza.textProperty().isEmpty()).or(kmPercorsi.textProperty().isEmpty())
						.or(nPorte.textProperty().isEmpty()).or(nPosti.textProperty().isEmpty())
						.or(consumo.textProperty().isEmpty()).or(stato.valueProperty().isNull())
						.or(alimentazione.valueProperty().isNull()).or(cambio.valueProperty().isNull())
						.or(tipologia.valueProperty().isNull()).or(cilindrata.textProperty().isEmpty()));
	}

	@Override
	public void initializeData(Veicolo veicolo) {
		this.veicolo = veicolo;

		stato.getItems().setAll(Stato.values());
		alimentazione.getItems().setAll(Alimentazione.values());
		cambio.getItems().setAll(Cambio.values());

		try {
			tipologia.setItems(FXCollections.observableArrayList(tipologiaService.visualizzaTipologie()));
			tipologia.setConverter(new StringConverter<Tipologia>() {
				@Override
				public String toString(Tipologia tipologia) {
					if (tipologia == null) {
						return "";
					} else {
						return tipologia.getNome();
					}
				}

				@Override
				public Tipologia fromString(String s) {

					return null;
				}
			});
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		if (veicolo.getTarga() == null) {
			eliminaButton.disableProperty();
			marca.setText(null);
			modello.setText(null);
			targa.setText(null);
			colore.setText(null);
			kmPercorsi.setText(null);
			nPorte.setText(null);
			nPosti.setText(null);
			consumo.setText(null);
			stato.setValue(null);
			alimentazione.setValue(null);
			potenza.setText(null);
			cambio.setValue(null);
			tipologia.setValue(null);
			cilindrata.setText(null);
		} else {
			marca.setText(veicolo.getMarca());
			modello.setText(veicolo.getModello());
			targa.setText(veicolo.getTarga());
			colore.setText(veicolo.getColore());
			kmPercorsi.setText(veicolo.getChilometriPercorsi().toString());
			nPorte.setText(veicolo.getNumeroPorte().toString());
			nPosti.setText(veicolo.getNumeroPosti().toString());
			consumo.setText(veicolo.getConsumo().toString());
			stato.setValue(veicolo.getStato());
			alimentazione.setValue(veicolo.getAlimentazione());
			potenza.setText(veicolo.getPotenza().toString());
			cambio.setValue(veicolo.getCambio());
			tipologia.setValue(veicolo.getTipologia());
			cilindrata.setText(veicolo.getCilindrata().toString());
		}
	}

	@FXML
	public void salvaAction(ActionEvent event) {
		try {
			veicolo.setMarca(marca.getText());
			veicolo.setModello(modello.getText());
			veicolo.setColore(colore.getText());
			veicolo.setChilometriPercorsi(Integer.parseInt(kmPercorsi.getText()));
			veicolo.setNumeroPorte(Integer.parseInt(nPorte.getText()));
			veicolo.setNumeroPosti(Integer.parseInt(nPosti.getText()));
			veicolo.setConsumo(Double.parseDouble(consumo.getText()));
			veicolo.setStato(stato.getValue());
			veicolo.setAlimentazione(alimentazione.getValue());
			veicolo.setPotenza(Integer.parseInt(potenza.getText()));
			veicolo.setCambio(cambio.getValue());
			veicolo.setTipologia(tipologia.getValue());
			veicolo.setCilindrata(Integer.parseInt(cilindrata.getText()));

			// Serve per verificare se l'amministratore ha cliccato sul pulsante
			// Aggiungi Veicolo oppure sul pulsante Modifica
			if (veicolo.getTarga() == null) {
				veicolo.setTarga(targa.getText());
				veicoloService.aggiungiVeicolo(veicolo);
			} else {
				veicolo.setTarga(targa.getText());
				veicoloService.modificaVeicolo(veicolo);
			}

			dispatcher.renderView("veicoli", veicolo);

		} catch (VeicoloEsistenteException e) {
			errorLabel.setText("Il veicolo estiste già!");
			errorLabel.setStyle("-fx-border-color: red;");
			targa.setText(null);
		} catch (NumberFormatException e) {
			errorLabel.setText("Inserire valori validi");
			errorLabel.setStyle("-fx-border-color: red;");
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
	}

	@FXML
	public void annullaAction(ActionEvent event) throws ViewException {
		dispatcher.renderView("veicoli", null);
	}

	@FXML
	public void eliminaAction(ActionEvent event) throws ViewException {

		try {
			veicoloService.eliminaVeicolo(veicolo);
			dispatcher.renderView("veicoli", veicolo);
		} catch (VeicoloNonEliminabileException e) {
			errorLabel.setText("Il veicolo non può essere eliminato!");
			errorLabel.setStyle("-fx-border-color: red;");
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}

	}

}
