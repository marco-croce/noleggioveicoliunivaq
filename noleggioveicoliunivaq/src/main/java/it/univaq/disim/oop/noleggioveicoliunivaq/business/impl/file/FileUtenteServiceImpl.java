package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Sesso;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;

public class FileUtenteServiceImpl implements UtenteService {

	private String utenteFileName;

	public FileUtenteServiceImpl(String utenteFileName) {
		this.utenteFileName = utenteFileName;
	}

	@Override
	public Set<Utente> visualizzaUtenti() throws BusinessException {
		Set<Utente> utenti = new HashSet<>();
		try {
			FileData fileData = Utility.readAllRows(utenteFileName);
			for (String[] righe : fileData.getRighe()) {
				Utente utente = new Utente();
				utente.setNome(righe[0]);
				utente.setCognome(righe[1]);
				utente.setEmail(righe[2]);
				utente.setPassword(righe[3]);
				utente.setCodiceFiscale(righe[4]);
				utente.setTelefono(righe[5]);
				utente.setDataNascita(LocalDate.parse(righe[6]));
				utente.setSesso(Sesso.valueOf(righe[7]));
				utenti.add(utente);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
		return utenti;
	}

	@Override
	public void aggiungiUtente(Utente utente) throws BusinessException, UtenteEsistenteException {
		try {
			FileData fileData = Utility.readAllRows(utenteFileName);
			for (String[] righe : fileData.getRighe()) {
				if (righe[2].equals(utente.getEmail()))
					throw new UtenteEsistenteException();
			}
			try (PrintWriter writer = new PrintWriter(new File(utenteFileName))) {
				long contatore = fileData.getContatore();
				writer.println((contatore + 1));
				for (String[] righe : fileData.getRighe()) {
					writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
				StringBuilder row = new StringBuilder();
				row.append(utente.getNome());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(utente.getCognome());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(utente.getEmail());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(utente.getPassword());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(utente.getCodiceFiscale());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(utente.getTelefono());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(utente.getDataNascita().toString());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(utente.getSesso().toString());
				row.append(Utility.SEPARATORE_COLONNA);
				writer.println(row.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	@Override
	public void modificaUtente(Utente utente) throws BusinessException {
		try {
			FileData fileData = Utility.readAllRows(utenteFileName);
			try (PrintWriter writer = new PrintWriter(new File(utenteFileName))) {
				writer.println(fileData.getContatore());
				for (String[] righe : fileData.getRighe()) {
					if (righe[2].equals(utente.getEmail())) {
						StringBuilder row = new StringBuilder();
						row.append(utente.getNome());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(utente.getCognome());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(utente.getEmail());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(utente.getPassword());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(utente.getCodiceFiscale());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(utente.getTelefono());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(utente.getDataNascita().toString());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(utente.getSesso().toString());
						row.append(Utility.SEPARATORE_COLONNA);
						writer.println(row.toString());
					} else {
						writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	@Override
	public void eliminaUtente(String email) throws BusinessException {
		try {
			FileData fileData = Utility.readAllRows(utenteFileName);
			try (PrintWriter writer = new PrintWriter(new File(utenteFileName))) {
				writer.println(fileData.getContatore());
				for (String[] righe : fileData.getRighe()) {
					if (!(righe[2].equals(email))) {
						writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	@Override
	public Utente getUtente(String email) throws BusinessException, UtenteNotFoundException {
		Utente utente = new Utente();
		try {
			FileData fileData = Utility.readAllRows(utenteFileName);
			for (String[] righe : fileData.getRighe()) {
				if (righe[2].equals(email)) {
					utente.setNome(righe[0]);
					utente.setCognome(righe[1]);
					utente.setEmail(righe[2]);
					utente.setPassword(righe[3]);
					utente.setCodiceFiscale(righe[4]);
					utente.setTelefono(righe[5]);
					utente.setDataNascita(LocalDate.parse(righe[6]));
					utente.setSesso(Sesso.valueOf(righe[7]));
					return utente;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
		throw new UtenteNotFoundException();
	}
}
