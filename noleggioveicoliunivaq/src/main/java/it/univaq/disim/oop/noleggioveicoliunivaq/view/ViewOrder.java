package it.univaq.disim.oop.noleggioveicoliunivaq.view;

import java.util.ArrayList;
import java.util.List;

public class ViewOrder {

	// Lista che contiene tutte le viste aperte in sequenza
	private static List<String> viste = new ArrayList<>();
	// Contatore del numero di viste aperte in sequenza
	private static int counter = 0;

	// Metodo per aggiungere la vista aperta alla lista
	public static void addLastView(String view) {
		viste.add(counter++, view);
	}

	// Metodo per conoscere la vista da riaprire ed eliminarla dalla lista
	public static String getLastView() {
		counter--;
		String vista = viste.get(counter);
		viste.remove(counter);
		return vista;
	}

	// Metodo per eliminare tutte le viste dalla lista quando si effettua il logout
	public static void deleteAllView() {
		viste.clear();
		counter = 0;
	}

}
