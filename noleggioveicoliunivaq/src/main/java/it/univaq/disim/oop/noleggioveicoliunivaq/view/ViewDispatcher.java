package it.univaq.disim.oop.noleggioveicoliunivaq.view;

import java.io.IOException;

import it.univaq.disim.oop.noleggioveicoliunivaq.controller.DataInitializable;
import it.univaq.disim.oop.noleggioveicoliunivaq.controller.DoubleDataInitializable;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ViewDispatcher {

	private static final String FXML_SUFFIX = ".fxml";
	private static final String RESOURCE_BASE = "/viste/";
	private static ViewDispatcher instance = new ViewDispatcher();

	private Stage stage;
	private BorderPane layout;

	private ViewDispatcher() {
	}

	public static ViewDispatcher getInstance() {
		return instance;
	}

	public void loginView(Stage stage) throws ViewException {
		this.stage = stage;
		Parent loginView = loadView("login").getView();
		Scene scene = new Scene(loginView);
		stage.setScene(scene);
		stage.show();
	}

	// Torna alla vista di Login partendo dalla vista di SignIn
	// Non è presente il parametro stage perrchè è già settato
	public void loginView() throws ViewException {
		Parent loginView = loadView("login").getView();
		Scene scene = new Scene(loginView);
		stage.setScene(scene);
	}

	public void signInView() throws ViewException {
		Parent signInView = loadView("signin").getView();
		Scene scene = new Scene(signInView);
		stage.setScene(scene);
	}

	public void loggedIn(Persona persona) {
		try {
			View<Persona> layoutView = loadView("layout");
			// Inizializza la persona nel controller
			// Utilizzato per la visualizzazione dei menu a seconda se e' amministratore,
			// operatore oppure utente
			DataInitializable<Persona> layoutController = layoutView.getController();
			layoutController.initializeData(persona);

			layout = (BorderPane) layoutView.getView();
			renderView("home", persona);
			Scene scene = new Scene(layout);
			scene.getStylesheets().add(getClass().getResource(RESOURCE_BASE + "styles.css").toExternalForm());
			stage.setScene(scene);
		} catch (ViewException e) {
			renderError(e);
		}
	}

	public void logout() {
		try {
			Parent loginView = loadView("login").getView();
			Scene scene = new Scene(loginView);
			stage.setScene(scene);
		} catch (ViewException e) {
			renderError(e);
		}
	}

	// Effettua il render della vista inizializzando due dati nel controller
	public <T> void renderView(String viewName, T data) {
		try {
			View<T> view = loadView(viewName);
			DataInitializable<T> controller = view.getController();
			controller.initializeData(data);
			layout.setCenter(view.getView());
		} catch (ViewException e) {
			renderError(e);
		}
	}

	// Effettua il render della vista inizializzando due dati nel controller
	public <T, S> void renderView(String viewName, T data, S data2) {
		try {
			ViewDouble<T, S> view = loadViewDouble(viewName);
			DoubleDataInitializable<T, S> controller = view.getDoubleController();
			controller.initializeData(data, data2);
			layout.setCenter(view.getView());
		} catch (ViewException e) {
			renderError(e);
		}
	}

	public void renderError(Exception e) {
		e.printStackTrace();
		System.exit(1);
	}

	// Carica la vista all'interno del metodo renderView con un dato
	private <T> View<T> loadView(String viewName) throws ViewException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_BASE + viewName + FXML_SUFFIX));
			Parent parent = (Parent) loader.load();
			return new View<>(parent, loader.getController());
		} catch (IOException e) {
			throw new ViewException(e);
		}
	}

	// Carica la vista all'interno del metodo renderView con due dati
	private <T, S> ViewDouble<T, S> loadViewDouble(String viewName) throws ViewException {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_BASE + viewName + FXML_SUFFIX));
			Parent parent = (Parent) loader.load();
			return new ViewDouble<>(parent, loader.getController());
		} catch (IOException e) {
			throw new ViewException(e);
		}
	}

}
