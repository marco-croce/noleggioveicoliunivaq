package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TariffaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Categoria;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tariffa;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;
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

public class TariffeController implements Initializable, DataInitializable<Veicolo> {
    
	@FXML
    private TableView<Tariffa> tariffeTable;
    @FXML
    private Button aggiungiTariffaButton;
    @FXML
    private Button indietroButton;
    @FXML
    private TableColumn<Tariffa, Categoria> categoriaTableColumn;
    @FXML
    private TableColumn<Tariffa, Double> prezzoTableColumn;
    @FXML
    private TableColumn<Tariffa, Integer> idTableColumn;
    @FXML
    private TableColumn<Tariffa, Button> modificaTableColumn;
    @FXML
    private TableColumn<Tariffa, Button> eliminaTableColumn;
    @FXML
    private TableColumn<Tariffa, Integer> nVeicoliTableColumn;

    private ViewDispatcher dispatcher;
    private static TariffaService tariffaService;
    private Veicolo veicolo;

    public TariffeController() {
        dispatcher = ViewDispatcher.getInstance();
        NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
        tariffaService = factory.getTariffaService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoriaTableColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        prezzoTableColumn.setCellValueFactory(new PropertyValueFactory<>("prezzo"));
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        modificaTableColumn.setCellValueFactory((CellDataFeatures<Tariffa, Button> param) -> {
            final Button modificaButton = new Button("Modifica");
            modificaButton.setCursor(Cursor.HAND);
            modificaButton.setOnAction((ActionEvent event) -> {
                dispatcher.renderView("modificaTariffe", param.getValue(), veicolo);
            });
            return new SimpleObjectProperty<Button>(modificaButton);
        });

        eliminaTableColumn.setCellValueFactory((CellDataFeatures<Tariffa, Button> param) -> {
            final Button eliminaButton = new Button("Elimina");
            eliminaButton.setCursor(Cursor.HAND);
            eliminaButton.setOnAction((ActionEvent event) -> {
                try {
                    tariffaService.eliminaTariffa(param.getValue());
                    List<Tariffa> tariffe = tariffaService.visualizzaTariffe(veicolo);
                    ObservableList<Tariffa> tariffeData = FXCollections.observableArrayList(tariffe);
                    tariffeTable.setItems(tariffeData);
                    dispatcher.renderView("tariffe", veicolo);
                } catch (BusinessException e) {
                    dispatcher.renderError(e);
                }
            });
            return new SimpleObjectProperty<Button>(eliminaButton);
        });
    }

    @Override
    public void initializeData(Veicolo veicolo) {
        
    	try {
            this.veicolo = veicolo;
            List<Tariffa> tariffe = tariffaService.visualizzaTariffe(veicolo);
            ObservableList<Tariffa> tariffeData = FXCollections.observableArrayList(tariffe);
            tariffeTable.setItems(tariffeData);
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
    	
    }

    @FXML
    public void aggiungiTariffaAction(ActionEvent event) throws ViewException {
        dispatcher.renderView("modificaTariffe", new Tariffa(), veicolo);
    }

    @FXML
    public void annullaAction(ActionEvent event) throws ViewException {
		dispatcher.renderView("veicoli", null);
    }

}
