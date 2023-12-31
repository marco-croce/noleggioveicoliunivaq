package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PasswordDiverseException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PersonaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteEsistenteException;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class SignInController implements Initializable, DataInitializable<Object> {

	@FXML
	private TextField nomeField;
	@FXML
	private TextField cognomeField;
	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private PasswordField confermaPasswordField;
	@FXML
	private TextField codiceFiscaleField;
	@FXML
	private TextField numeroTelefonicoField;
	@FXML
	private DatePicker dataNascitaDate;
	@FXML
	private Button signInButton;
	@FXML
	private Button loginButton;
	@FXML
	private RadioButton uomoRadio;
	@FXML
	private RadioButton donnaRadio;
	@FXML
	private Label signInErrorLabel;

	private ViewDispatcher dispatcher;

	private PersonaService personaService;

	public SignInController() {
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		personaService = factory.getPersonaService();
		dispatcher = ViewDispatcher.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		signInButton.disableProperty().bind(nomeField.textProperty().isEmpty().or(cognomeField.textProperty().isEmpty())
				.or(emailField.textProperty().isEmpty()).or(passwordField.textProperty().isEmpty())
				.or(confermaPasswordField.textProperty().isEmpty()).or(codiceFiscaleField.textProperty().isEmpty())
				.or(numeroTelefonicoField.textProperty().isEmpty()).or(dataNascitaDate.valueProperty().isNull()));
	}

	public void controllaPassword() throws PasswordDiverseException {
		if (!(passwordField.getText().equals(confermaPasswordField.getText())))
			throw new PasswordDiverseException();
	}

	public Sesso controllaSesso() {
		if (donnaRadio.isSelected())
			return Sesso.DONNA;
		else
			return Sesso.UOMO;
	}

	@FXML
	public void signInAction(ActionEvent event) {

		try {
			controllaPassword();
			Utente utente = personaService.registrazione(nomeField.getText(), cognomeField.getText(),
					emailField.getText(), passwordField.getText(), codiceFiscaleField.getText(),
					numeroTelefonicoField.getText(), dataNascitaDate.getValue(), controllaSesso());
			dispatcher.loggedIn(utente);
		} catch (PasswordDiverseException e) {
			signInErrorLabel.setText("Le password non corrispondono!");
			signInErrorLabel.setStyle("-fx-border-color: red;");
		} catch (UtenteEsistenteException e) {
			signInErrorLabel.setText("Utente già registrato!");
			signInErrorLabel.setStyle("-fx-border-color: red;");
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}

	}

	@FXML
	public void loginAction(ActionEvent event) throws ViewException {
		dispatcher.loginView();
	}

}
