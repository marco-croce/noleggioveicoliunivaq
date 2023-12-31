package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
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

public class UtentiController implements Initializable, DataInitializable<Persona> {

	@FXML
	private TableView<Utente> utentiTable;
	@FXML
	private TableColumn<Utente, String> nomeTableColumn;
	@FXML
	private TableColumn<Utente, String> cognomeTableColumn;
	@FXML
	private TableColumn<Utente, String> emailTableColumn;
	@FXML
	private TableColumn<Utente, String> telefonoTableColumn;
	@FXML
	private TableColumn<Utente, LocalDate> dataDiNascitaTableColumn;
	@FXML
	private TableColumn<Utente, Button> modificaTableColumn;
	@FXML
	private TableColumn<Utente, Button> cancellaTableColumn;
	@FXML
	private Button aggiungiUtenteButton;

	private ViewDispatcher dispatcher;
	private UtenteService utenteService;

	public UtentiController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		utenteService = factory.getUtenteService();
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

		modificaTableColumn.setCellValueFactory((CellDataFeatures<Utente, Button> param) -> {

			final Button modificaButton = new Button("Modifica");
			modificaButton.setCursor(Cursor.HAND);
			modificaButton.setOnAction((ActionEvent event) -> {
				dispatcher.renderView("modificaUtenti", param.getValue());
			});
			return new SimpleObjectProperty<Button>(modificaButton);
		});

		cancellaTableColumn.setCellValueFactory((CellDataFeatures<Utente, Button> param) -> {
			final Button cancellaButton = new Button("Cancella");
			cancellaButton.setCursor(Cursor.HAND);
			cancellaButton.setOnAction((ActionEvent event) -> {
				try {
					utenteService.eliminaUtente(param.getValue().getEmail());
					Set<Utente> utenti = utenteService.visualizzaUtenti();
					ObservableList<Utente> UtentiData = FXCollections.observableArrayList(utenti);
					utentiTable.setItems(UtentiData);
				} catch (BusinessException e) {
					dispatcher.renderError(e);
				}
				dispatcher.renderView("utenti", param.getValue());
			});
			return new SimpleObjectProperty<Button>(cancellaButton);
		});

	}

	@Override
	public void initializeData(Persona persona) {
		try {
			Set<Utente> utenti = utenteService.visualizzaUtenti();
			ObservableList<Utente> utentiData = FXCollections.observableArrayList(utenti);
			utentiTable.setItems(utentiData);
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
	}

	@FXML
	public void aggiungiUtenteAction(ActionEvent event) throws ViewException {
		Utente utente = new Utente();
		dispatcher.renderView("modificaUtenti", utente);
	}

}
