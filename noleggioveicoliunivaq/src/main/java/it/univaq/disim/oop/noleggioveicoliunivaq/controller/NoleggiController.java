package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Amministratore;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class NoleggiController implements Initializable, DataInitializable<Persona> {

	@FXML
	private TableView<Noleggio> noleggiTable;
	@FXML
	private Button visualizzaNoleggiConRichieste;
	@FXML
	private Button visualizzaNolegginCorso;
	@FXML
	private TableColumn<Noleggio, String> utenteTableColumn;
	@FXML
	private TableColumn<Noleggio, String> veicoloTableColumn;
	@FXML
	private TableColumn<Noleggio, String> dataInizioTableColumn;
	@FXML
	private TableColumn<Noleggio, String> dataFineTableColumn;
	@FXML
	private TableColumn<Noleggio, Integer> kmTableColumn;
	@FXML
	private TableColumn<Noleggio, Button> dettagliTableColumn;
	@FXML
	private TableColumn<Noleggio, Button> riconsegnaTableColumn;
	@FXML
	private DatePicker dataRiconsegna;
	@FXML
	private Label dataLabel;

	private ViewDispatcher dispatcher;
	private Persona persona;
	private NoleggioService noleggioService;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public NoleggiController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		noleggioService = factory.getNoleggioService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		utenteTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Noleggio, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Noleggio, String> param) {
						return new SimpleObjectProperty<>(param.getValue().getPrenotazione().getUtente().getEmail());
					}
				});

		dataInizioTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Noleggio, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Noleggio, String> param) {
						return new SimpleObjectProperty<>(
								param.getValue().getPrenotazione().getDataRitiro().format(formatter).toString());
					}
				});
		dataFineTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Noleggio, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Noleggio, String> param) {
						return new SimpleObjectProperty<>(
								param.getValue().getDataRiconsegna().format(formatter).toString());
					}
				});

		veicoloTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Noleggio, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Noleggio, String> param) {
						return new SimpleObjectProperty<>(param.getValue().getVeicolo().getTarga());
					}
				});

		kmTableColumn.setCellValueFactory(new PropertyValueFactory<>("chilometraggioPrevisto"));

		dettagliTableColumn.setCellValueFactory((CellDataFeatures<Noleggio, Button> param) -> {
			final Button modificaButton = new Button("Dettagli");
			modificaButton.setCursor(Cursor.HAND);
			modificaButton.setOnAction((ActionEvent event) -> {
				dispatcher.renderView("dettagliNoleggio", param.getValue(), persona);
			});
			return new SimpleObjectProperty<Button>(modificaButton);
		});

		dataRiconsegna.setDayCellFactory(picker -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				setDisable(empty || date.compareTo(today) < 0);
			}
		});

		riconsegnaTableColumn.setCellValueFactory((CellDataFeatures<Noleggio, Button> param) -> {
			final Button gestisciButton = new Button("Gestisci");
			gestisciButton.setCursor(Cursor.HAND);
			if (param.getValue().isPagamentoEffettuato())
				gestisciButton.setDisable(true);
			if (!(param.getValue().getDataRiconsegna().isEqual(LocalDate.now())))
				gestisciButton.setDisable(true);
			if (persona instanceof Amministratore)
				gestisciButton.setVisible(false);
			gestisciButton.setOnAction((ActionEvent event) -> {
				ViewOrder.addLastView("noleggi");
				dispatcher.renderView("gestioneRiconsegna", param.getValue(), persona);
			});
			return new SimpleObjectProperty<Button>(gestisciButton);
		});

		utenteTableColumn.setStyle("-fx-alignment: CENTER;");
		veicoloTableColumn.setStyle("-fx-alignment: CENTER;");
		dataInizioTableColumn.setStyle("-fx-alignment: CENTER;");
		dataFineTableColumn.setStyle("-fx-alignment: CENTER;");
		kmTableColumn.setStyle("-fx-alignment: CENTER;");
		dettagliTableColumn.setStyle("-fx-alignment: CENTER;");
		riconsegnaTableColumn.setStyle("-fx-alignment: CENTER;");

	}

	@Override
	public void initializeData(Persona persona) {

		/*
		 * Verifica se la persona che ha effettuato il login è un amministratore oppure
		 * un operatore così da nascondere il DatePicker per visualizzare i noleggi in
		 * riconsegna in un giorno oppure il pulsante per visualizzare i noleggi con
		 * richieste di assistenza
		 */
		if (persona instanceof Amministratore) {
			try {
				this.persona = persona;
				List<Noleggio> noleggi = noleggioService.visualizzaNoleggiInCorso();
				ObservableList<Noleggio> noleggiData = FXCollections.observableArrayList(noleggi);
				noleggiTable.setItems(noleggiData);
			} catch (BusinessException e) {
				dispatcher.renderError(e);
			}
			dataRiconsegna.setVisible(false);
			dataLabel.setVisible(false);
		} else {
			try {
				this.persona = persona;
				List<Noleggio> noleggi = noleggioService.visualizzaNoleggiInCorso();
				ObservableList<Noleggio> noleggiData = FXCollections.observableArrayList(noleggi);
				noleggiTable.setItems(noleggiData);
			} catch (BusinessException e) {
				dispatcher.renderError(e);
			}
			visualizzaNoleggiConRichieste.setVisible(false);

		}
	}

	public void visualizzaInCorso() {
		try {
			List<Noleggio> noleggi = noleggioService.visualizzaNoleggiInCorso();
			ObservableList<Noleggio> noleggiData = FXCollections.observableArrayList(noleggi);
			noleggiTable.setItems(noleggiData);
			dataRiconsegna.setValue(null);
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
	}

	public void visualizzaConIntervento() {
		try {
			List<Noleggio> noleggi = noleggioService.visualizzaNoleggiAssistiti();
			ObservableList<Noleggio> noleggiData = FXCollections.observableArrayList(noleggi);
			noleggiTable.setItems(noleggiData);
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
	}

	public void visualizzaPerData() {
		if (dataRiconsegna.getValue() != null) {
			try {
				List<Noleggio> noleggi = noleggioService.visualizzaNoleggiInRiconsegna(dataRiconsegna.getValue());
				ObservableList<Noleggio> noleggiData = FXCollections.observableArrayList(noleggi);
				noleggiTable.setItems(noleggiData);
			} catch (BusinessException e) {
				dispatcher.renderError(e);
			}
		}

	}

}
