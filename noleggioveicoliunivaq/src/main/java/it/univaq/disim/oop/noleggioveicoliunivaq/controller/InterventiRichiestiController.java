package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.InterventoService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Intervento;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;

public class InterventiRichiestiController implements Initializable, DataInitializable<Persona> {

	@FXML
	private TableView<Intervento> interventiTable;
	@FXML
	private Button homeButton;
	@FXML
	private TableColumn<Intervento, LocalDate> dataRichiestaTableColumn;
	@FXML
	private TableColumn<Intervento, Button> messaggioTableColumn;
	@FXML
	private TableColumn<Intervento, String> gestioneTableColumn;
	@FXML
	private TableColumn<Intervento, Button> gestisciTableColumn;

	private Persona operatore;
	private ViewDispatcher dispatcher;
	private InterventoService interventoService;

	public InterventiRichiestiController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		interventoService = factory.getInterventoService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dataRichiestaTableColumn.setCellValueFactory(new PropertyValueFactory<>("dataRichiestaIntervento"));

		/*
		 * Verifica il valore della variabile booleana gestito ed inserisce all'interno
		 * della tabella "SI" oppure "NO" in base all'avvenuta gestione dell'intervento
		 * o meno
		 */
		gestioneTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Intervento, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Intervento, String> param) {
						if (param.getValue().isGestito())
							return new SimpleStringProperty("SI");
						else
							return new SimpleStringProperty("NO");
					}
				});

		dataRichiestaTableColumn.setStyle("-fx-alignment: CENTER;");
		messaggioTableColumn.setStyle("-fx-alignment: CENTER;");
		gestioneTableColumn.setStyle("-fx-alignment: CENTER;");
		gestisciTableColumn.setStyle("-fx-alignment: CENTER;");
	}

	@Override
	public void initializeData(Persona operatore) {
		this.operatore = operatore;

		messaggioTableColumn.setCellValueFactory((CellDataFeatures<Intervento, Button> param) -> {
			final Button visualizzaButton = new Button("Visualizza");
			visualizzaButton.setCursor(Cursor.HAND);
			visualizzaButton.setOnAction((ActionEvent event) -> {
				ViewOrder.addLastView("interventiRichiesti");
				dispatcher.renderView("dettagliNotifica", param.getValue(), operatore);
			});
			return new SimpleObjectProperty<Button>(visualizzaButton);
		});

		gestisciTableColumn.setCellValueFactory((CellDataFeatures<Intervento, Button> param) -> {
			final Button gestisciButton = new Button("Gestisci");
			gestisciButton.setCursor(Cursor.HAND);

			/*
			 * Disabilita il pulsante gestisciButton se l'intervento è stato già gestito
			 * dall'operatore
			 */
			if (param.getValue().isGestito())
				gestisciButton.setDisable(true);

			gestisciButton.setOnAction((ActionEvent event) -> {
				ViewOrder.addLastView("interventiRichiesti");
				dispatcher.renderView("gestioneInterventi", param.getValue(), operatore);
			});
			return new SimpleObjectProperty<Button>(gestisciButton);
		});

		try {
			List<Intervento> interventi;
			interventi = interventoService.visualizzaInterventi();
			ObservableList<Intervento> interventiData = FXCollections.observableArrayList(interventi);
			interventiTable.setItems(interventiData);
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}

	}

	@FXML
	public void homeAction() throws BusinessException {
		dispatcher.renderView("home", operatore);
	}

}
