package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;

public class RAMUtenteServiceImpl implements UtenteService {

	private static Set<Utente> utenti = new HashSet<>();

	@Override
	public Set<Utente> visualizzaUtenti() throws BusinessException {
		return utenti;
	}

	@Override
	public void aggiungiUtente(Utente utente) throws BusinessException, UtenteEsistenteException {
		for (Utente u : utenti)
			if (u.getEmail().equals(utente.getEmail()))
				throw new UtenteEsistenteException();

		utenti.add(utente);
	}

	@Override
	public void modificaUtente(Utente utente) throws BusinessException {
		for (Iterator<Utente> i = utenti.iterator(); i.hasNext();) {
			Utente u = i.next();
			if (u.getEmail().equals(utente.getEmail())) {
				u.setNome(utente.getNome());
				u.setCognome(utente.getCognome());
				u.setEmail(utente.getEmail());
				u.setPassword(utente.getPassword());
				u.setDataNascita(utente.getDataNascita());
				u.setTelefono(utente.getTelefono());
			}
		}
	}

	@Override
	public void eliminaUtente(String email) throws BusinessException {
		for (Iterator<Utente> i = utenti.iterator(); i.hasNext();) {
			Utente utente = i.next();
			if (utente.getEmail().equals(email)) {
				i.remove();
			}
		}
	}

	@Override
	public Utente getUtente(String email) throws BusinessException, UtenteNotFoundException {
		for (Utente utente : utenti)
			if (utente.getEmail().equals(email))
				return utente;
		throw new UtenteNotFoundException();
	}

}
