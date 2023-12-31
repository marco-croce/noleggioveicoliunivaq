package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Intervento;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Notifica;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DettagliNotificaController implements Initializable, DoubleDataInitializable<Object, Persona> {

	@FXML
	private TextField messaggioTextField;
	@FXML
	private Button homeButton;
	@FXML
	private Button feedbackButton;

	private Persona persona;
	private ViewDispatcher dispatcher;
	private Notifica notifica;
	private Intervento intervento;

	public DettagliNotificaController() {
		dispatcher = ViewDispatcher.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	/*
	 * Si ha come parametro Object per visualizzare sia il messaggio che è stato
	 * settato nella richiesta di intervento sia le altre notifiche
	 */
	@Override
	public void initializeData(Object n, Persona p) {
		this.persona = p;

		if (n instanceof Notifica) {
			notifica = (Notifica) n;
			messaggioTextField.setText(notifica.getMessaggio());
			// Nasconde il feedbackButton se la notifica non riguarda i feedback
			if (!(notifica.getMessaggio().equals("Lascia un feedback sul veicolo!")))
				feedbackButton.setVisible(false);
		}

		if (n instanceof Intervento) {
			intervento = (Intervento) n;
			messaggioTextField.setText(intervento.getMessaggio());
			feedbackButton.setVisible(false);
		}

	}

	@FXML
	public void feedbackAction() {
		ViewOrder.addLastView("dettagliNotifica");
		dispatcher.renderView("scritturaFeedback", notifica, persona);
	}

	@FXML
	public void homeAction() {
		dispatcher.renderView(ViewOrder.getLastView(), persona);
	}

}
