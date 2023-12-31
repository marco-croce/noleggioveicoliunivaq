package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Sesso;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class ModificaUtentiController implements Initializable, DataInitializable<Utente> {

	@FXML
	private TextField nome;
	@FXML
	private TextField cognome;
	@FXML
	private TextField password;
	@FXML
	private TextField codiceFiscale;
	@FXML
	private TextField telefono;
	@FXML
	private TextField email;
	@FXML
	private DatePicker dataNascita;
	@FXML
	private Button salvaButton;
	@FXML
	private Button annullaButton;
	@FXML
	private Label errorLabel;
	@FXML
	private RadioButton uomoRadio;
	@FXML
	private RadioButton donnaRadio;

	private ViewDispatcher dispatcher;
	private UtenteService utenteService;
	private Utente utente;

	public ModificaUtentiController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		utenteService = factory.getUtenteService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		salvaButton.disableProperty()
				.bind(nome.textProperty().isEmpty().or(cognome.textProperty().isEmpty())
						.or(password.textProperty().isEmpty()).or(codiceFiscale.textProperty().isEmpty())
						.or(telefono.textProperty().isEmpty()).or(dataNascita.valueProperty().isNull()));
	}

	@Override
	public void initializeData(Utente utente) {
		this.utente = utente;
		this.nome.setText(utente.getNome());
		this.cognome.setText(utente.getCognome());
		this.password.setText(utente.getPassword());
		this.codiceFiscale.setText(utente.getCodiceFiscale());
		this.telefono.setText(utente.getTelefono());
		this.dataNascita.setValue(utente.getDataNascita());
		this.email.setText(utente.getEmail());
		if ((Sesso.DONNA).equals(utente.getSesso()))
			this.donnaRadio.setSelected(true);
		else
			this.uomoRadio.setSelected(true);
	}

	public Sesso controllaSesso() {
		if (donnaRadio.isSelected())
			return Sesso.DONNA;
		else
			return Sesso.UOMO;
	}

	@FXML
	public void salvaAction(ActionEvent event) {
		try {
			utente.setNome(nome.getText());
			utente.setCognome(cognome.getText());
			utente.setPassword(password.getText());
			utente.setCodiceFiscale(codiceFiscale.getText());
			utente.setTelefono(telefono.getText());
			utente.setDataNascita(dataNascita.getValue());
			utente.setSesso(controllaSesso());

			// Verifica se l'amministratore ha richiesto un'aggiunta oppure una modifica
			if (utente.getEmail() == null) {
				utente.setEmail(email.getText());
				utenteService.aggiungiUtente(utente);
			} else {
				utente.setEmail(email.getText());
				utenteService.modificaUtente(utente);
			}

			dispatcher.renderView("utenti", utente);

		} catch (UtenteEsistenteException e) {
			errorLabel.setText("L'utente esiste già!");
			errorLabel.setStyle("-fx-border-color: red;");
			nome.setText(null);
			cognome.setText(null);
			password.setText(null);
			codiceFiscale.setText(null);
			telefono.setText(null);
			dataNascita.setValue(null);
			email.setText(null);
			if (utente.getSesso().equals(Sesso.UOMO))
				this.uomoRadio.setSelected(true);
			else
				this.donnaRadio.setSelected(true);
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}
	}

	@FXML
	public void annullaAction(ActionEvent event) throws ViewException {
		dispatcher.renderView("utenti", utente);
	}

}
