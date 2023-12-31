package it.univaq.disim.oop.noleggioveicoliunivaq.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Sesso;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class HomeController implements Initializable, DataInitializable<Persona> {

	@FXML
	private Label benvenutoLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void initializeData(Persona persona) {
		StringBuilder testo = new StringBuilder();
		if (persona.getSesso().equals(Sesso.UOMO))
			testo.append("Benvenuto ");
		else
			testo.append("Benvenuta ");
		testo.append(persona.getNome());
		testo.append(" ");
		testo.append(persona.getCognome());
		testo.append(" !");
		benvenutoLabel.setText(testo.toString());
	}

}
