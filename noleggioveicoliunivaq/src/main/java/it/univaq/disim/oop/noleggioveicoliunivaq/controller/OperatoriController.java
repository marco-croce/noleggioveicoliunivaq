package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.OperatoreService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Operatore;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
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

public class OperatoriController implements Initializable, DataInitializable<Persona> {

	@FXML
	private TableView<Operatore> operatoriTable;
	@FXML
	private TableColumn<Operatore, String> nomeTableColumn;
	@FXML
	private TableColumn<Operatore, String> cognomeTableColumn;
	@FXML
	private TableColumn<Operatore, String> emailTableColumn;
	@FXML
	private TableColumn<Operatore, String> telefonoTableColumn;
	@FXML
	private TableColumn<Operatore, LocalDate> dataDiNascitaTableColumn;
	@FXML
	private TableColumn<Operatore, Button> modificaTableColumn;
	@FXML
	private TableColumn<Operatore, Button> cancellaTableColumn;
	@FXML
	private Button aggiungiOperatoreButton;

	private ViewDispatcher dispatcher;
	private OperatoreService operatoreService;

	public OperatoriController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		operatoreService = factory.getOperatoreService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomeTableColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
		nomeTableColumn.setStyle("-fx-alignment: CENTER;");
		cognomeTableColumn.setCellValueFactory(new PropertyValueFactory<>("cognome"));
		cognomeTableColumn.setStyle("-fx-alignment: CENTER;");
		emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		emailTableColumn.setStyle("-fx-alignment: CENTER;");
		telefonoTableColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
		telefonoTableColumn.setStyle("-fx-alignment: CENTER;");
		dataDiNascitaTableColumn.setCellValueFactory(new PropertyValueFactory<>("dataNascita"));
		dataDiNascitaTableColumn.setStyle("-fx-alignment: CENTER;");
		modificaTableColumn.setStyle("-fx-alignment: CENTER;");
		cancellaTableColumn.setStyle("-fx-alignment: CENTER;");

		modificaTableColumn.setCellValueFactory((CellDataFeatures<Operatore, Button> param) -> {
			final Button modificaButton = new Button("Modifica");
			modificaButton.setCursor(Cursor.HAND);
			modificaButton.setOnAction((ActionEvent event) -> {
				dispatcher.renderView("modificaOperatori", param.getValue());
			});
			return new SimpleObjectProperty<Button>(modificaButton);
		});

		cancellaTableColumn.setCellValueFactory((CellDataFeatures<Operatore, Button> param) -> {
			final Button cancellaButton = new Button("Cancella");
			cancellaButton.setCursor(Cursor.HAND);
			cancellaButton.setOnAction((ActionEvent event) -> {
				try {
					operatoreService.eliminaOperatore(param.getValue().getEmail());
					Set<Operatore> operatori = operatoreService.visualizzaOperatori();
					ObservableList<Operatore> OperatoriData = FXCollections.observableArrayList(operatori);
					operatoriTable.setItems(OperatoriData);
				} catch (BusinessException e) {
					dispatcher.renderError(e);
				}
				dispatcher.renderView("operatori", param.getValue());
			});
			return new SimpleObjectProperty<Button>(cancellaButton);
		});
	}

	@Override
	public void initializeData(Persona persona) {
		try {
			Set<Operatore> operatori = operatoreService.visualizzaOperatori();
			ObservableList<Operatore> operatoriData = FXCollections.observableArrayList(operatori);
			operatoriTable.setItems(operatoriData);
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
	}

	@FXML
	public void aggiungiOperatoreAction(ActionEvent event) throws ViewException {
		Operatore operatore = new Operatore();
		dispatcher.renderView("modificaOperatori", operatore);
	}

}
