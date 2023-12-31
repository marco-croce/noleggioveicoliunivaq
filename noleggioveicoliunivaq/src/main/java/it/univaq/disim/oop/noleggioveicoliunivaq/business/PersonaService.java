package it.univaq.disim.oop.noleggioveicoliunivaq.business;

import java.time.LocalDate;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Sesso;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;

public interface PersonaService {

	Persona autenticazione(String email, String password) throws UtenteNotFoundException, BusinessException;

	Utente registrazione(String nome, String cognome, String email, String password, String codiceFiscale,
			String telefono, LocalDate dataNascita, Sesso sesso) throws BusinessException, UtenteEsistenteException;

}
