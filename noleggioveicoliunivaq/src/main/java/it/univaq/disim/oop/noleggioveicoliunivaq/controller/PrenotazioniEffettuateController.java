package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Prenotazione;
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

public class PrenotazioniEffettuateController implements Initializable, DataInitializable<Persona> {

    @FXML
    private TableView<Prenotazione> prenotazioniTable;
    @FXML
    private TableColumn<Prenotazione, LocalDate> dataTableColumn;
    @FXML
    private TableColumn<Prenotazione, String> utenteTableColumn;
    @FXML
    private TableColumn<Prenotazione, String> veicoloTableColumn;
    @FXML
    private TableColumn<Prenotazione, String> gestitoTableColumn;
    @FXML
    private TableColumn<Prenotazione, Button> gestisciTableColumn;

    private Persona persona;
    private ViewDispatcher dispatcher;
    private PrenotazioneService prenotazioneService;

    public PrenotazioniEffettuateController() {
        dispatcher = ViewDispatcher.getInstance();
        NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
        prenotazioneService = factory.getPrenotazioneService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataTableColumn.setCellValueFactory(new PropertyValueFactory<>("dataRitiro"));

        gestitoTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Prenotazione, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Prenotazione, String> param) {
                        if (param.getValue().isGestito())
                            return new SimpleStringProperty("SI");
                        else
                            return new SimpleStringProperty("NO");
                    }
                });

        utenteTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Prenotazione, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Prenotazione, String> param) {
                        return new SimpleStringProperty(param.getValue().getUtente().getEmail());
                    }
                });

        veicoloTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Prenotazione, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Prenotazione, String> param) {
                        return new SimpleStringProperty(param.getValue().getNoleggio().getVeicolo().getTarga());
                    }
                });
    }

    @Override
    public void initializeData(Persona persona) {
        this.persona = persona;

        gestisciTableColumn.setCellValueFactory((CellDataFeatures<Prenotazione, Button> param) -> {
            final Button gestisciButton = new Button("Gestisci");
            gestisciButton.setCursor(Cursor.HAND);
            if (param.getValue().isGestito())
                gestisciButton.setDisable(true);
            gestisciButton.setOnAction((ActionEvent event) -> {
                ViewOrder.addLastView("prenotazioniEffettuate");
                dispatcher.renderView("gestionePrenotazioni", param.getValue(), this.persona);
            });
            return new SimpleObjectProperty<Button>(gestisciButton);
        });

        try {
            List<Prenotazione> prenotazioni = prenotazioneService.visualizzaPrenotazioni();
            ObservableList<Prenotazione> prenotazioniData = FXCollections.observableArrayList(prenotazioni);
            prenotazioniTable.setItems(prenotazioniData);
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }

    }

}
