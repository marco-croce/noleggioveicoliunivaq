package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.InterventoService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NotificaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Intervento;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Notifica;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class GestioneInterventiController implements Initializable, DoubleDataInitializable<Intervento, Persona> {

	@FXML
	private Button applicaButton;
	@FXML
	private Button indietroButton;
	@FXML
	private RadioButton sostituzioneRadio;// RadioButton SI
	@FXML
	private RadioButton nonSostituzioneRadio;// RadioButton NO
	@FXML
	private TextField messaggioTextField;
	@FXML
	private DatePicker dataInterventoDate;
	@FXML
	private Button veicoloButton;

	private Persona operatore;
	private ViewDispatcher dispatcher;
	private Intervento intervento;
	private NotificaService notificaService;
	private InterventoService interventoService;

	public GestioneInterventiController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		notificaService = factory.getNotificaService();
		interventoService = factory.getInterventoService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		applicaButton.disableProperty()
				.bind(messaggioTextField.textProperty().isEmpty().or(dataInterventoDate.valueProperty().isNull()));

		// Blocca la scelta del veicolo se il RadioButton "NO" è selezionato
		veicoloButton.disableProperty().bind(nonSostituzioneRadio.selectedProperty());

	}

	@Override
	public void initializeData(Intervento intervento, Persona operatore) {
		this.intervento = intervento;
		this.operatore = operatore;

		/*
		 * Permette di selezionare soltanto le date comprese all'interno del periodo di
		 * noleggio
		 */
		dataInterventoDate.setDayCellFactory(d -> new DateCell() {
			@Override
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				LocalDate dateMax = intervento.getNoleggio().getDataRiconsegna();
				setDisable(empty || date.compareTo(LocalDate.now()) < 0 || date.compareTo(dateMax) > 0);
			}
		});

		try {
			if (interventoService.getVeicoloSostitutivo() != null) {
				nonSostituzioneRadio.setSelected(false);
				sostituzioneRadio.setSelected(true);
			}
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}

	}

	@FXML
	public void scegliVeicoloAction() {
		try {
			interventoService.setInterventoGestito(intervento);
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
		ViewOrder.addLastView("gestioneInterventi");
		dispatcher.renderView("sceltaVeicolo", operatore);
	}

	@FXML
	public void applicaAction() {

		try {
			interventoService.gestisciIntervento(intervento, dataInterventoDate.getValue().atTime(10, 0));
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		Notifica notifica = new Notifica(LocalDate.now(), dataInterventoDate.getValue().atTime(10, 0),
				messaggioTextField.getText(), intervento.getUtente());

		try {
			notificaService.inviaNotifica(notifica);
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}

		dispatcher.renderView(ViewOrder.getLastView(), operatore);
	}

	@FXML
	public void indietroAction() {
		dispatcher.renderView(ViewOrder.getLastView(), operatore);
	}

}
