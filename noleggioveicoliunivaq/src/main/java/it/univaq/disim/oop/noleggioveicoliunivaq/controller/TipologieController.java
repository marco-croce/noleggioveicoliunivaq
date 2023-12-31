package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaNonEliminabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tipologia;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class TipologieController implements Initializable, DataInitializable<Object> {

    @FXML
    private TableView<Tipologia> tipologiaTable;
    @FXML
    private Label errorLabel;
    @FXML
    private TableColumn<Tipologia, String> nomeTableColumn;
    @FXML
    private TableColumn<Tipologia, Integer> idTableColumn;
    @FXML
    private TableColumn<Tipologia, Number> numeroVeicoliTableColumn;
    @FXML
    private TableColumn<Tipologia, Button> modificaTableColumn;
    @FXML
    private TableColumn<Tipologia, Button> eliminaTableColumn;
    @FXML
    private Button aggiungiTipologiaButton;

    private ViewDispatcher dispatcher;
    private TipologiaService tipologiaService;
    private VeicoloService veicoloService;

    public TipologieController() {
        dispatcher = ViewDispatcher.getInstance();
        NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
        tipologiaService = factory.getTipologiaService();
        veicoloService = factory.getVeicoloService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nomeTableColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        modificaTableColumn.setStyle("-fx-alignment: CENTER;");
        eliminaTableColumn.setStyle("-fx-alignment: CENTER;");

        numeroVeicoliTableColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Tipologia, Number>, ObservableValue<Number>>() {
                    @Override
                    public ObservableValue<Number> call(CellDataFeatures<Tipologia, Number> param) {
                        try {
                            int cont = veicoloService.getNumeroVeicoli(param.getValue());
                            SimpleIntegerProperty obsInt = new SimpleIntegerProperty(cont);
                            return obsInt;
                        } catch (BusinessException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });

        modificaTableColumn.setCellValueFactory((CellDataFeatures<Tipologia, Button> param) -> {
            final Button modificaButton = new Button("Modifica");
            modificaButton.setCursor(Cursor.HAND);
            modificaButton.setOnAction((ActionEvent event) -> {
                dispatcher.renderView("modificaTipologie", param.getValue());
            });
            return new SimpleObjectProperty<Button>(modificaButton);
        });

        eliminaTableColumn.setCellValueFactory((CellDataFeatures<Tipologia, Button> param) -> {
            final Button eliminaButton = new Button("Elimina");
            eliminaButton.setCursor(Cursor.HAND);
            eliminaButton.setStyle(" -fx-background-color :#d41737;");
            eliminaButton.setOnAction((ActionEvent event) -> {
                try {
                    tipologiaService.eliminaTipologia(param.getValue());
                    List<Tipologia> tipologie = tipologiaService.visualizzaTipologie();
                    ObservableList<Tipologia> tipologieData = FXCollections.observableArrayList(tipologie);
                    tipologiaTable.setItems(tipologieData);
                    dispatcher.renderView("tipologie", param.getValue());
                } catch (TipologiaNonEliminabileException e) {
                    errorLabel.setText("La tipologia non può essere eliminata!");
                    errorLabel.setStyle("-fx-border-color: red;");
                } catch (BusinessException e) {
                    dispatcher.renderError(e);
                }
            });
            return new SimpleObjectProperty<Button>(eliminaButton);
        });
    }

    @Override
    public void initializeData(Object object) {
        try {
            List<Tipologia> tipologie = tipologiaService.visualizzaTipologie();
            ObservableList<Tipologia> tipologieData = FXCollections.observableArrayList(tipologie);
            tipologiaTable.setItems(tipologieData);
        } catch (BusinessException e) {
            dispatcher.renderError(e);
        }
    }

    @FXML
    public void aggiungiTipologiaAction(ActionEvent event) throws ViewException {
        Tipologia tipologia = new Tipologia();
        dispatcher.renderView("modificaTipologie", tipologia);
    }

}
