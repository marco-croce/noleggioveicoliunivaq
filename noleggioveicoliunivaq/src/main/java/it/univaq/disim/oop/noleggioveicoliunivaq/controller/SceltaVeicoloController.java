package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.InterventoService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Stato;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SceltaVeicoloController implements Initializable, DataInitializable<Persona> {

	@FXML
	private TableView<Veicolo> veicoliTable;
	@FXML
	private TableColumn<Veicolo, String> marcaTableColumn;
	@FXML
	private TableColumn<Veicolo, String> modelloTableColumn;
	@FXML
	private TableColumn<Veicolo, String> coloreTableColumn;
	@FXML
	private TableColumn<Veicolo, Integer> potenzaTableColumn;
	@FXML
	private TableColumn<Veicolo, Integer> postiTableColumn;
	@FXML
	private TableColumn<Veicolo, Double> consumoTableColumn;
	@FXML
	private TableColumn<Veicolo, Button> dettaglioTableColumn;
	@FXML
	private TableColumn<Veicolo, Button> sceltaTableColumn;

	private ViewDispatcher dispatcher;
	private VeicoloService veicolo;
	private PrenotazioneService prenotazione;
	private InterventoService interventi;

	private String oldView;
	private Persona persona;

	public SceltaVeicoloController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		veicolo = factory.getVeicoloService();
		prenotazione = factory.getPrenotazioneService();
		interventi = factory.getInterventoService();
		oldView = ViewOrder.getLastView();// Vista precedente
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		marcaTableColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
		marcaTableColumn.setStyle("-fx-alignment: CENTER;");
		modelloTableColumn.setCellValueFactory(new PropertyValueFactory<>("modello"));
		modelloTableColumn.setStyle("-fx-alignment: CENTER;");
		coloreTableColumn.setCellValueFactory(new PropertyValueFactory<>("colore"));
		coloreTableColumn.setStyle("-fx-alignment: CENTER;");
		potenzaTableColumn.setCellValueFactory(new PropertyValueFactory<>("potenza"));
		potenzaTableColumn.setStyle("-fx-alignment: CENTER;");
		postiTableColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPosti"));
		postiTableColumn.setStyle("-fx-alignment: CENTER;");
		consumoTableColumn.setCellValueFactory(new PropertyValueFactory<>("consumo"));
		consumoTableColumn.setStyle("-fx-alignment: CENTER;");
		dettaglioTableColumn.setCellValueFactory((CellDataFeatures<Veicolo, Button> param) -> {
			final Button dettaglioButton = new Button("Dettaglio");
			dettaglioButton.setCursor(Cursor.HAND);
			dettaglioButton.setOnAction((ActionEvent event) -> {
				ViewOrder.addLastView("sceltaVeicolo");
				dispatcher.renderView("dettagliVeicolo", param.getValue(), persona);
			});
			return new SimpleObjectProperty<Button>(dettaglioButton);
		});
		dettaglioTableColumn.setStyle("-fx-alignment: CENTER;");

		sceltaTableColumn.setCellValueFactory((CellDataFeatures<Veicolo, Button> param) -> {
			final Button sceltaButton = new Button("Seleziona");
			sceltaButton.setCursor(Cursor.HAND);
			sceltaButton.setOnAction((ActionEvent event) -> {
				/*
				 * In base al valore della stringa oldView verifica se si sta effettuando una
				 * prenotazione, un preventivo oppure una gestione di un intervento
				 */
				if ("prenotazione".equals(oldView)) {
					try {
						prenotazione.setVeicoloScelto(param.getValue().getTarga());
					} catch (BusinessException e) {
						dispatcher.renderError(e);
					}
					dispatcher.renderView("prenotazione", persona);
				}
				if ("preventivo".equals(oldView)) {
					try {
						prenotazione.setVeicoloScelto(param.getValue().getTarga());
					} catch (BusinessException e) {
						dispatcher.renderError(e);
					}
					dispatcher.renderView("preventivo", persona);
				}
				if ("gestioneInterventi".equals(oldView)) {
					try {
						interventi.setVeicoloSostitutivo(param.getValue());
					} catch (BusinessException e) {
						dispatcher.renderError(e);
					}
					try {
						dispatcher.renderView("gestioneInterventi", interventi.getInterventoGestito(), persona);
						interventi.setInterventoGestito(null);
					} catch (BusinessException e) {
						dispatcher.renderError(e);
					}
				}
			});
			return new SimpleObjectProperty<Button>(sceltaButton);
		});
		sceltaTableColumn.setStyle("-fx-alignment: CENTER;");

	}

	@Override
	public void initializeData(Persona persona) {
		this.persona = persona;
		List<Veicolo> veicoli;
		try {

			/*
			 * Se la persona è un utente deve visualizzare sia i veicoli disponibili che
			 * quelli in noleggio perchè può effettuare preventivi o prenotazioni anche di
			 * veicoli in noleggio, mentre l'operatore deve eventualmente sostituire un
			 * veicolo solo con veicoli disponibili
			 */
			if (persona instanceof Utente)
				veicoli = veicolo.visualizzaVeicoli(Stato.DISPONIBILE, Stato.IN_NOLEGGIO);
			else
				veicoli = veicolo.visualizzaVeicoli(Stato.DISPONIBILE);
			ObservableList<Veicolo> veicoliData = FXCollections.observableArrayList(veicoli);
			veicoliTable.setItems(veicoliData);
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}

	}

}
