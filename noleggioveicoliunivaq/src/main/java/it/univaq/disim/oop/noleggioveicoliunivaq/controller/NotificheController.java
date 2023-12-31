package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NotificaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Notifica;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
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

public class NotificheController implements Initializable, DataInitializable<Persona> {

	@FXML
	private TableView<Notifica> notificheTable;
	@FXML
	private TableColumn<Notifica, LocalDate> dataTableColumn;
	@FXML
	private TableColumn<Notifica, String> dataRitiroGestioneTableColumn;
	@FXML
	private TableColumn<Notifica, Button> visualizzaTableColumn;
	@FXML
	private Button homeButton;

	private Persona persona;
	private ViewDispatcher dispatcher;
	private NotificaService notificaService;

	public NotificheController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		notificaService = factory.getNotificaService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		/*
		 * Verifica se mostrare la data di gestione dell'assistenza richiesta
		 * oppure mostrare N.D. siccome si tratta di una notifica
		 * riguardante richieste di scrittura feedback oppure di 
		 * prenotazione di un noleggio
		 */
		dataRitiroGestioneTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Notifica, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Notifica, String> param) {
						if (param.getValue().getData() != null)
							return new SimpleStringProperty(param.getValue().getData().format(formatter));
						else
							return new SimpleStringProperty(" N.D. ");
					}
				});

		dataTableColumn.setCellValueFactory(new PropertyValueFactory<>("dataCreazione"));
		dataTableColumn.setStyle("-fx-alignment: CENTER;");
		dataRitiroGestioneTableColumn.setStyle("-fx-alignment: CENTER;");
		visualizzaTableColumn.setStyle("-fx-alignment: CENTER;");

	}

	@Override
	public void initializeData(Persona persona) {
		this.persona = persona;
		
		try {
			if (persona instanceof Utente) {
				visualizzaTableColumn.setCellValueFactory((CellDataFeatures<Notifica, Button> param) -> {
					final Button visualizzaButton = new Button("Visualizza");
					visualizzaButton.setCursor(Cursor.HAND);
					visualizzaButton.setOnAction((ActionEvent event) -> {
						ViewOrder.addLastView("notifiche");
						dispatcher.renderView("dettagliNotifica", param.getValue(), persona);
					});
					return new SimpleObjectProperty<Button>(visualizzaButton);
				});

				List<Notifica> notifiche = notificaService.visualizzaNotifiche(persona);
				ObservableList<Notifica> notificheData = FXCollections.observableArrayList(notifiche);
				notificheTable.setItems(notificheData);
			}
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}

	}

	@FXML
	public void homeAction() throws BusinessException {
		dispatcher.renderView("home", persona);
	}

}
