package it.univaq.disim.oop.noleggioveicoliunivaq;

import it.univaq.disim.oop.noleggioveicoliunivaq.view.ViewDispatcher;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class NoleggioVeicoliUnivaqApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		// Inserimento logo università
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/viste/logo.png")));
		ViewDispatcher dispatcher = ViewDispatcher.getInstance();
		dispatcher.loginView(stage);
	}

}
