package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import java.time.LocalDate;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.OperatoreService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PersonaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Amministratore;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Operatore;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Sesso;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;

public class RAMPersonaServiceImpl implements PersonaService {

	private static Amministratore admin;
	private static OperatoreService operatori;
	private static UtenteService utenti;

	public RAMPersonaServiceImpl() {
		admin = Amministratore.getAmministratore();
		operatori = new RAMOperatoreServiceImpl();
		utenti = new RAMUtenteServiceImpl();
	}

	@Override
	public Persona autenticazione(String email, String password) throws BusinessException {

		if (email.equals(admin.getEmail()) && password.equals(admin.getPassword()))
			return admin;

		for (Operatore operatore : operatori.visualizzaOperatori()) {
			if (email.equals(operatore.getEmail()) && password.equals(operatore.getPassword()))
				return operatore;
		}

		for (Utente utente : utenti.visualizzaUtenti()) {
			if (email.equals(utente.getEmail()) && password.equals(utente.getPassword()))
				return utente;
		}

		throw new UtenteNotFoundException();
	}

	@Override
	public Utente registrazione(String nome, String cognome, String email, String password, String codiceFiscale,
			String telefono, LocalDate dataNascita, Sesso sesso) throws BusinessException, UtenteEsistenteException {

		Utente utente = new Utente(nome, cognome, email, password, codiceFiscale, telefono, dataNascita, sesso);
		utenti.aggiungiUtente(utente);

		return utente;
	}

}
