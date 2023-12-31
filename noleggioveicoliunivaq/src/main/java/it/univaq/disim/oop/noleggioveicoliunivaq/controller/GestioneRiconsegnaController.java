package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Categoria;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Operatore;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tariffa;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GestioneRiconsegnaController implements Initializable, DoubleDataInitializable<Noleggio, Operatore> {

	@FXML
	private TextField chilometriTextField;
	@FXML
	private Button verificaButton;
	@FXML
	private Button registraButton;
	@FXML
	private Button indietroButton;
	@FXML
	private Label costoLabel;
	@FXML
	private Label veicoloLabel;

	private ViewDispatcher dispatcher;
	private Persona operatore;
	private Noleggio noleggio;
	private NoleggioService noleggioService;

	private double costoFinale = 0;
	private int kmFinali = 0;

	// Tariffa chilometrica di default
	Tariffa tariffaChilometrica = new Tariffa(1.0, Categoria.CHILOMETRICO, 0);

	public GestioneRiconsegnaController() {
		dispatcher = ViewDispatcher.getInstance();
		NoleggioVeicoliUnivaqBusinessFactory factory = NoleggioVeicoliUnivaqBusinessFactory.getInstance();
		this.noleggioService = factory.getNoleggioService();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		verificaButton.disableProperty().bind(chilometriTextField.textProperty().isEmpty());

		/*
		 * Disabilitato di default perchè deve essere attivo solo dopo che è stato
		 * calcolato il costo finale del noleggio
		 */
		registraButton.setDisable(true);
	}

	@Override
	public void initializeData(Noleggio n, Operatore o) {
		this.operatore = o;
		this.noleggio = n;
		// Mostra la targa del veicolo da riconsegnare all'interno della Label
		veicoloLabel.setText("Chilometraggio Finale Veicolo - " + noleggio.getVeicolo().getTarga());
	}

	@FXML
	public void verificaAction() {
		// Non è più disabilitato perchè è stato calcolato il costo finale
		registraButton.setDisable(false);

		int kmVeicolo = noleggio.getVeicolo().getChilometriPercorsi();
		int kmVeicoloFinali = Integer.parseInt(chilometriTextField.getText());
		int kmExtra = kmVeicoloFinali - kmVeicolo - noleggio.getChilometraggioPrevisto();
		double costoExtra = 0;
		// Il costo finale viene inizializzato al csoto previsto in fase di prenotazione
		costoFinale = noleggio.getCosto();

		/*
		 * Verifica se l'utente ha percorso più chilometri di quelli previsti e cerca
		 * una tariffa chilometrica da applicare per essi
		 */
		if (kmExtra > 0) {
			for (Tariffa t : noleggio.getVeicolo().getTariffe()) {
				if (t.getCategoria() == Categoria.CHILOMETRICO)
					tariffaChilometrica = t;
				break;
			}

			/*
			 * Se non vengono trovate tariffe chilometrica legate al veicolo si applica un
			 * costo standard per ogni chilometroExtra percorso, ovvero 1 € / km, come
			 * definito nella variabile d'istanza tariffaChilometrica
			 */
			costoExtra = kmExtra * tariffaChilometrica.getPrezzo();

			costoFinale += costoExtra;

			kmFinali = kmVeicoloFinali;
		}

		// Mostra all'utente il costo finale del noleggio
		costoLabel.setText("Costo finale del noleggio: " + Double.toString(costoFinale) + " €");

	}

	@FXML
	public void registraAction() {
		kmFinali = Integer.parseInt(chilometriTextField.getText());

		try {
			noleggioService.gestioneRiconsegna(noleggio, kmFinali, costoFinale);

			// Verifica se sono presenti veicoli sostitutivi nel noleggio
			if (noleggio.getVeicoliSostitutivi().size() != 0) {
				costoLabel.setText("Inserisci il chilometraggio del veicolo sostitutivo");
				chilometriTextField.setText(null);
				verificaButton.setVisible(false);
				registraButton.setText("Registra chilometraggio");
				veicoloLabel.setText("Chilometraggio Finale Veicolo - "
						+ noleggio.getVeicoliSostitutivi().get(noleggio.getVeicoliSostitutivi().size() - 1).getTarga());
				return;
			}

			dispatcher.renderView(ViewOrder.getLastView(), operatore);
		} catch (BusinessException e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void indietroAction() {
		dispatcher.renderView(ViewOrder.getLastView(), operatore);
	}

}
