package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Amministratore;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Operatore;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.MenuElement;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewOrder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

public class LayoutController implements Initializable, DataInitializable<Persona> {

	private static final MenuElement MENU_HOME = new MenuElement("HOME", "home");
	private static final MenuElement[] MENU_AMMINISTRATORI = { new MenuElement("Operatori", "operatori"),
			new MenuElement("Utenti", "utenti"), new MenuElement("Noleggi", "noleggi"),
			new MenuElement("Veicoli", "veicoli"), new MenuElement("Tipologie", "tipologie"),
			new MenuElement("Feedback", "feedback") };
	private static final MenuElement[] MENU_OPERATORI = { new MenuElement("Noleggi", "noleggi"),
			new MenuElement("Veicoli", "veicoli"), new MenuElement("Prenotazioni", "prenotazioniEffettuate"),
			new MenuElement("Interventi", "interventiRichiesti"), new MenuElement("Feedback", "feedback") };
	private static final MenuElement[] MENU_UTENTI = { new MenuElement("Veicoli", "veicoli"),
			new MenuElement("Preventivo", "preventivo"), new MenuElement("Prenota Veicolo", "prenotazione"),
			new MenuElement("Notifiche", "notifiche"), new MenuElement("Richiedi Intervento", "intervento") };

	@FXML
	private VBox menuBar;
	@FXML
	private ImageView logoutImage;

	private Persona persona;
	private ViewDispatcher dispatcher;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dispatcher = ViewDispatcher.getInstance();
	}

	@Override
	public void initializeData(Persona persona) {
		this.persona = persona;

		Button homeButton = createButton(MENU_HOME);
		homeButton.setStyle("-fx-background-color:  #FFE896; -fx-font-size: 14;");
		VBox.setMargin(homeButton, new Insets(5, 12.8, 5, 12.8));

		menuBar.getChildren().addAll(homeButton);
		Separator separator = new Separator();
		separator.setStyle("-fx-background-color:  #000000;");
		VBox.setMargin(separator, new Insets(0, 0, 5, 0));
		menuBar.getChildren().add(separator);

		if (persona instanceof Amministratore) {
			for (MenuElement menu : MENU_AMMINISTRATORI) {
				menuBar.getChildren().add(createButton(menu));
			}
		}
		if (persona instanceof Operatore) {
			for (MenuElement menu : MENU_OPERATORI) {
				menuBar.getChildren().add(createButton(menu));
			}
		}
		if (persona instanceof Utente) {
			for (MenuElement menu : MENU_UTENTI) {
				menuBar.getChildren().add(createButton(menu));
			}
		}
	}

	@FXML
	public void esciAction(MouseEvent event) {
		dispatcher.logout();
	}

	@FXML
	private Button createButton(MenuElement viewItem) {
		Button button = new Button(viewItem.getNome());
		button.setStyle(" -fx-font-size: 13; ");
		button.setStyle("-fx-focus-color: red;");
		button.setStyle("-fx-faint-focus-color: red;");
		button.setStyle("-fx-body-color: #FFE896;");
		button.setTextFill(Paint.valueOf("black"));
		button.setPrefHeight(50);
		button.setPrefWidth(130);
		button.setCursor(Cursor.HAND);
		button.setTextAlignment(TextAlignment.CENTER);
		button.setAlignment(Pos.CENTER);
		VBox.setMargin(button, new Insets(0, 12.8, 3, 12.8));
		button.setOnAction((ActionEvent event) -> {
			ViewOrder.deleteAllView();
			dispatcher.renderView(viewItem.getVista(), persona);
		});
		return button;
	}

}
