package it.univaq.disim.oop.noleggioveicoliunivaq.domain;

import java.time.LocalDate;

public class Amministratore extends Persona {

	private static Amministratore admin = new Amministratore("admin", "admin", "admin@admin.com", "password",
			"MMNTRE01A25A500F", "3336060555", LocalDate.of(2000, 1, 1), Sesso.UOMO);

	public static Amministratore getAmministratore() {
		return admin;
	}

	private Amministratore(String nome, String cognome, String email, String password, String codiceFiscale,
			String telefono, LocalDate dataNascita, Sesso sesso) {
		super(nome, cognome, email, password, codiceFiscale, telefono, dataNascita, sesso);
	}

}
