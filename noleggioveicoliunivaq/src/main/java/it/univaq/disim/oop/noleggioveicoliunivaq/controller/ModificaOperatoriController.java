package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.OperatoreEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.OperatoreService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Operatore;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Sesso;
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

public class ModificaOperatoriController implements Initializable, DataInitializable<Operatore> {

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
	private OperatoreService operatoriService;
	private Operatore operatore;

	public ModificaOperatoriController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		operatoriService = factory.getOperatoreService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		salvaButton.disableProperty()
				.bind(nome.textProperty().isEmpty().or(cognome.textProperty().isEmpty())
						.or(password.textProperty().isEmpty()).or(codiceFiscale.textProperty().isEmpty())
						.or(telefono.textProperty().isEmpty()).or(dataNascita.valueProperty().isNull()));
	}

	@Override
	public void initializeData(Operatore operatore) {
		this.operatore = operatore;

		this.nome.setText(operatore.getNome());
		this.cognome.setText(operatore.getCognome());
		this.password.setText(operatore.getPassword());
		this.codiceFiscale.setText(operatore.getCodiceFiscale());
		this.telefono.setText(operatore.getTelefono());
		this.dataNascita.setValue(operatore.getDataNascita());
		this.email.setText(operatore.getEmail());
		if ((Sesso.DONNA).equals(operatore.getSesso()))
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
			operatore.setNome(nome.getText());
			operatore.setCognome(cognome.getText());
			operatore.setPassword(password.getText());
			operatore.setCodiceFiscale(codiceFiscale.getText());
			operatore.setTelefono(telefono.getText());
			operatore.setDataNascita(dataNascita.getValue());
			operatore.setSesso(controllaSesso());

			// Verifica se l'amministratore ha richiesto un'aggiunta oppure una modifica
			if (operatore.getEmail() == null) {
				operatore.setEmail(email.getText());
				operatoriService.aggiungiOperatore(operatore);
			} else {
				operatore.setEmail(email.getText());
				operatoriService.modificaOperatore(operatore);
			}

			dispatcher.renderView("operatori", operatore);

		} catch (BusinessException e) {
			dispatcher.renderError(e);
		} catch (OperatoreEsistenteException e) {
			errorLabel.setText("L'operatore esiste già!");
			errorLabel.setStyle("-fx-border-color: red;");
			nome.setText(null);
			cognome.setText(null);
			password.setText(null);
			codiceFiscale.setText(null);
			telefono.setText(null);
			dataNascita.setValue(null);
			email.setText(null);
			if (operatore.getSesso().equals(Sesso.UOMO))
				this.uomoRadio.setSelected(true);
			else
				this.donnaRadio.setSelected(true);
		}
	}

	@FXML
	public void annullaAction(ActionEvent event) throws ViewException {
		dispatcher.renderView("operatori", operatore);
	}

}
