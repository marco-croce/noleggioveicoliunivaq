package it.univaq.disim.oop.noleggioveicoliunivaq.domain;

import java.time.LocalDate;

public class Utente extends Persona {

	public Utente(String nome, String cognome, String email, String password, String codiceFiscale, String telefono,
			LocalDate dataNascita, Sesso sesso) {
		super(nome, cognome, email, password, codiceFiscale, telefono, dataNascita, sesso);
	}

	public Utente() {

	}

}