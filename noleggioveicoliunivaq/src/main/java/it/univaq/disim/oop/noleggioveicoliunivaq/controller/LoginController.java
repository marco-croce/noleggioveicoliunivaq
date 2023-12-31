package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PersonaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable, DataInitializable<Object> {

	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label loginErrorLabel;
	@FXML
	private Button loginButton;
	@FXML
	private Button signInButton;

	private ViewDispatcher dispatcher;
	private PersonaService personaService;

	public LoginController() {
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		personaService = factory.getPersonaService();
		dispatcher = ViewDispatcher.getInstance();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginButton.disableProperty()
				.bind(emailField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty()));
	}

	@FXML
	public void loginAction(ActionEvent event) {

		try {
			Persona persona = personaService.autenticazione(emailField.getText(), passwordField.getText());
			dispatcher.loggedIn(persona);
		} catch (UtenteNotFoundException e) {
			loginErrorLabel.setText("Username e/o password errati!");
			loginErrorLabel.setStyle("-fx-border-color: red;");
		} catch (BusinessException e) {
			dispatcher.renderError(e);
		}

	}

	@FXML
	public void signInAction(ActionEvent event) throws ViewException {
		dispatcher.signInView();
	}

}
