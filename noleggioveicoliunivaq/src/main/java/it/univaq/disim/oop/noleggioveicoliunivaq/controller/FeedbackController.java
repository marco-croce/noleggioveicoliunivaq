package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.FeedbackService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Feedback;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class FeedbackController implements Initializable, DataInitializable<Persona> {

	@FXML
	private TableView<Feedback> feedbackTable;
	@FXML
	private TableColumn<Feedback, String> utenteTableColumn;
	@FXML
	private TableColumn<Feedback, String> dataTableColumn;
	@FXML
	private TableColumn<Feedback, String> veicoloTableColumn;
	@FXML
	private TableColumn<Feedback, String> messaggioTableColumn;
	@FXML
	private Button indietroButton;

	private ViewDispatcher dispatcher;
	private Persona persona;
	private FeedbackService feedbackService;

	public FeedbackController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		feedbackService = factory.getFeedbackService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		messaggioTableColumn.setCellValueFactory(new PropertyValueFactory<>("testo"));
		dataTableColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

		utenteTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Feedback, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Feedback, String> param) {
						return new SimpleStringProperty(param.getValue().getUtente().getEmail());
					}
				});

		dataTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Feedback, String>, ObservableValue<String>>() {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Feedback, String> param) {
						return new SimpleStringProperty(param.getValue().getData().format(formatter));
					}
				});

		veicoloTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Feedback, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<Feedback, String> param) {
						return new SimpleStringProperty(param.getValue().getVeicolo().getTarga());
					}
				});

	}

	@Override
	public void initializeData(Persona persona) {
		this.persona = persona;

		try {
			List<Feedback> feedbacks = feedbackService.visualizzaFeedbacks();
			ObservableList<Feedback> feedbackData = FXCollections.observableArrayList(feedbacks);
			feedbackTable.setItems(feedbackData);

		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
	}

	@FXML
	public void tornaIndietroAction(ActionEvent event) throws ViewException {
		dispatcher.renderView(ViewOrder.getLastView(), persona);
	}

}
