package it.univaq.disim.oop.noleggioveicoliunivaq.domain;

import java.time.LocalDate;

public class Persona {

	private String nome;
	private String cognome;
	private String email;
	private String password;
	private String codiceFiscale;
	private String telefono;
	private LocalDate dataNascita;
	private Sesso sesso;

	public Persona(String nome, String cognome, String email, String password, String codiceFiscale, String telefono,
			LocalDate dataNascita, Sesso sesso) {
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
		this.codiceFiscale = codiceFiscale;
		this.telefono = telefono;
		this.dataNascita = dataNascita;
		this.sesso = sesso;
	}

	public Persona() {

	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCodiceFiscale() {
		return this.codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public LocalDate getDataNascita() {
		return this.dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public Sesso getSesso() {
		return this.sesso;
	}

	public void setSesso(Sesso sesso) {
		this.sesso = sesso;
	}

}
