package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Alimentazione;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Operatore;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Stato;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tipologia;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class VeicoliController implements Initializable, DataInitializable<Object> {

	@FXML
	private TableView<Veicolo> veicoliTable;
	@FXML
	private DatePicker dalDatePicker;
	@FXML
	private DatePicker alDatePicker;
	@FXML
	private Button visualizzaButton;
	@FXML
	private Button aggiungiVeicoloButton;
	@FXML
	private ComboBox<Stato> statoVeicoloComboBox;
	@FXML
	private ComboBox<Tipologia> tipologiaVeicoloComboBox;
	@FXML
	private ComboBox<Alimentazione> alimentazioneVeicoloComboBox;
	@FXML
	private TableColumn<Veicolo, String> marcaTableColumn;
	@FXML
	private TableColumn<Veicolo, String> modelloTableColumn;
	@FXML
	private TableColumn<Veicolo, String> targaTableColumn;
	@FXML
	private TableColumn<Veicolo, Integer> kmTableColumn;
	@FXML
	private TableColumn<Veicolo, Stato> statoTableColumn;
	@FXML
	private TableColumn<Veicolo, Button> azioniTableColumn;
	@FXML
	private TableColumn<Veicolo, Button> dettagliTableColumn;
	@FXML
	private TableColumn<Veicolo, Button> tariffeTableColumn;
	@FXML
	private Button visualizzaConsumo;
	@FXML
	private TextField daTextField;
	@FXML
	private TextField aTextField;
	@FXML
	private Label statoLabel;
	@FXML
	private Label tipologiaLabel;
	@FXML
	private Label alimentazioneLabel;

	private ViewDispatcher dispatcher;
	private static VeicoloService veicoloService;
	private static TipologiaService tipologiaService;
	private Persona persona;

	public VeicoliController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		tipologiaService = factory.getTipologiaService();
		veicoloService = factory.getVeicoloService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		marcaTableColumn.setCellValueFactory(new PropertyValueFactory<>("marca"));
		modelloTableColumn.setCellValueFactory(new PropertyValueFactory<>("modello"));
		targaTableColumn.setCellValueFactory(new PropertyValueFactory<>("targa"));
		kmTableColumn.setCellValueFactory(new PropertyValueFactory<>("chilometriPercorsi"));
		statoTableColumn.setCellValueFactory(new PropertyValueFactory<>("stato"));
		azioniTableColumn.setCellValueFactory(new PropertyValueFactory<>("modifica"));
		statoVeicoloComboBox.getItems().setAll(Stato.values());
		alimentazioneVeicoloComboBox.getItems().setAll(Alimentazione.values());

		try {
			tipologiaVeicoloComboBox
					.setItems(FXCollections.observableArrayList(tipologiaService.visualizzaTipologie()));
			tipologiaVeicoloComboBox.setConverter(new StringConverter<Tipologia>() {
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
			dispatcher.renderError(e);
		}

		azioniTableColumn.setStyle("-fx-alignment: CENTER;");
		dettagliTableColumn.setStyle("-fx-alignment: CENTER;");
		tariffeTableColumn.setStyle("-fx-alignment: CENTER;");

		azioniTableColumn.setCellValueFactory((CellDataFeatures<Veicolo, Button> param) -> {
			final Button modificaButton = new Button("Modifica/Elimina");
			modificaButton.setCursor(Cursor.HAND);
			modificaButton.setOnAction((ActionEvent event) -> {
				dispatcher.renderView("modificaVeicoli", param.getValue());
			});
			return new SimpleObjectProperty<Button>(modificaButton);
		});

		tariffeTableColumn.setCellValueFactory((CellDataFeatures<Veicolo, Button> param) -> {
			final Button tariffa = new Button("Tariffe");
			tariffa.setCursor(Cursor.HAND);
			tariffa.setOnAction((ActionEvent event) -> {
				dispatcher.renderView("tariffe", param.getValue());
			});
			return new SimpleObjectProperty<Button>(tariffa);
		});

		dettagliTableColumn.setCellValueFactory((CellDataFeatures<Veicolo, Button> param) -> {
			final Button dettagliButton = new Button("Dettagli");
			dettagliButton.setCursor(Cursor.HAND);
			dettagliButton.setOnAction((ActionEvent event) -> {
				ViewOrder.addLastView("veicoli");
				dispatcher.renderView("dettagliVeicolo", param.getValue(), persona);
			});
			return new SimpleObjectProperty<Button>(dettagliButton);
		});

		dalDatePicker.setDayCellFactory(picker -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				if (alDatePicker.getValue() == null)
					setDisable(empty || date.compareTo(today) < 0);
				else
					setDisable(empty || date.compareTo(today) < 0 || date.compareTo(alDatePicker.getValue()) > 0);
			}
		});

		alDatePicker.setDayCellFactory(picker -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate today = LocalDate.now();
				if (dalDatePicker.getValue() == null)
					setDisable(empty || date.compareTo(today) < 0);
				else
					setDisable(empty || date.compareTo(today) < 0 || date.compareTo(dalDatePicker.getValue()) < 0);
			}
		});

	}

	@Override
	public void initializeData(Object object) {
		try {
			if (object instanceof Utente) {
				this.persona = (Persona) object;
				azioniTableColumn.setCellValueFactory((CellDataFeatures<Veicolo, Button> param) -> {
					final Button modificaButton = new Button("Recensioni");
					modificaButton.setCursor(Cursor.HAND);
					modificaButton.setOnAction((ActionEvent event) -> {
						dispatcher.renderView("feedbackVeicolo", param.getValue(), persona);
						ViewOrder.addLastView("veicoli");
					});
					return new SimpleObjectProperty<Button>(modificaButton);
				});
				aggiungiVeicoloButton.setVisible(false);
				statoVeicoloComboBox.setVisible(false);
				tariffeTableColumn.setVisible(false);
				statoLabel.setVisible(false);
				List<Veicolo> veicoli = veicoloService.visualizzaVeicoli(Stato.DISPONIBILE);
				ObservableList<Veicolo> veicoliData = FXCollections.observableArrayList(veicoli);
				veicoliTable.setItems(veicoliData);
			} else if (object instanceof Operatore) {
				this.persona = (Persona) object;
				azioniTableColumn.setCellValueFactory((CellDataFeatures<Veicolo, Button> param) -> {
					final Button modificaButton = new Button("Recensioni");
					modificaButton.setCursor(Cursor.HAND);
					modificaButton.setOnAction((ActionEvent event) -> {
						dispatcher.renderView("feedbackVeicolo", param.getValue(), persona);
						ViewOrder.addLastView("veicoli");
					});
					return new SimpleObjectProperty<Button>(modificaButton);
				});
				aggiungiVeicoloButton.setVisible(false);
				tariffeTableColumn.setVisible(false);
				List<Veicolo> veicoli = veicoloService.visualizzaVeicoli();
				ObservableList<Veicolo> veicoliData = FXCollections.observableArrayList(veicoli);
				veicoliTable.setItems(veicoliData);

			}

			else {
				List<Veicolo> veicoli = veicoloService.visualizzaVeicoli();
				ObservableList<Veicolo> veicoliData = FXCollections.observableArrayList(veicoli);
				veicoliTable.setItems(veicoliData);
			}
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
	}

	@FXML
	public void aggiungiVeicoloAction(ActionEvent event) throws ViewException {
		Veicolo veicolo = new Veicolo();
		dispatcher.renderView("modificaVeicoli", veicolo);
	}

	public void visualizzaConsumo() {
		if (!(daTextField.getText().isEmpty()) && !(aTextField.getText().isEmpty())) {
			try {
				List<Veicolo> veicoli = veicoloService.visualizzaVeicoli(Double.parseDouble(daTextField.getText()),
						Double.parseDouble(aTextField.getText()));
				ObservableList<Veicolo> veicoliData = FXCollections.observableArrayList(veicoli);
				veicoliTable.setItems(veicoliData);
			} catch (BusinessException e) {
				dispatcher.renderError(e);
			}
			dalDatePicker.setValue(null);
			alDatePicker.setValue(null);
			statoVeicoloComboBox.getSelectionModel().clearSelection();
			tipologiaVeicoloComboBox.getSelectionModel().clearSelection();
			alimentazioneVeicoloComboBox.getSelectionModel().clearSelection();

		}
	}

	public void visualizzaPerStato() {
		if (statoVeicoloComboBox.getValue() != null) {
			try {
				List<Veicolo> veicoli = veicoloService.visualizzaVeicoli(statoVeicoloComboBox.getValue());
				ObservableList<Veicolo> veicoliData = FXCollections.observableArrayList(veicoli);
				veicoliTable.setItems(veicoliData);
			} catch (BusinessException e) {
				dispatcher.renderError(e);
			}
			dalDatePicker.setValue(null);
			alDatePicker.setValue(null);
			tipologiaVeicoloComboBox.getSelectionModel().clearSelection();
			alimentazioneVeicoloComboBox.getSelectionModel().clearSelection();
			daTextField.setText(null);
			aTextField.setText(null);
		}
	}

	public void visualizzaPerAlimentazione() {
		if (alimentazioneVeicoloComboBox.getValue() != null) {
			try {
				List<Veicolo> veicoli = veicoloService.visualizzaVeicoli(alimentazioneVeicoloComboBox.getValue());
				ObservableList<Veicolo> veicoliData = FXCollections.observableArrayList(veicoli);
				veicoliTable.setItems(veicoliData);
			} catch (BusinessException e) {
				dispatcher.renderError(e);
			}
			dalDatePicker.setValue(null);
			alDatePicker.setValue(null);
			statoVeicoloComboBox.getSelectionModel().clearSelection();
			tipologiaVeicoloComboBox.getSelectionModel().clearSelection();
			daTextField.setText(null);
			aTextField.setText(null);
		}
	}

	public void visualizzaPerTipologia() {
		if (tipologiaVeicoloComboBox.getValue() != null) {
			try {
				List<Veicolo> veicoli = veicoloService.visualizzaVeicoli(tipologiaVeicoloComboBox.getValue());
				ObservableList<Veicolo> veicoliData = FXCollections.observableArrayList(veicoli);
				veicoliTable.setItems(veicoliData);
			} catch (BusinessException e) {
				dispatcher.renderError(e);
			}
			dalDatePicker.setValue(null);
			alDatePicker.setValue(null);
			statoVeicoloComboBox.getSelectionModel().clearSelection();
			alimentazioneVeicoloComboBox.getSelectionModel().clearSelection();
			daTextField.setText(null);
			aTextField.setText(null);
		}
	}

	public void visualizzaPerIntervallo() {
		if (alDatePicker.getValue() != null && dalDatePicker.getValue() != null) {
			try {
				List<Veicolo> veicoli = veicoloService.visualizzaVeicoli(dalDatePicker.getValue(),
						alDatePicker.getValue());
				ObservableList<Veicolo> veicoliData = FXCollections.observableArrayList(veicoli);
				veicoliTable.setItems(veicoliData);
			} catch (BusinessException e) {
				dispatcher.renderError(e);
			}

			statoVeicoloComboBox.getSelectionModel().clearSelection();
			tipologiaVeicoloComboBox.getSelectionModel().clearSelection();
			alimentazioneVeicoloComboBox.getSelectionModel().clearSelection();
			daTextField.setText(null);
			aTextField.setText(null);
		}
	}

}
