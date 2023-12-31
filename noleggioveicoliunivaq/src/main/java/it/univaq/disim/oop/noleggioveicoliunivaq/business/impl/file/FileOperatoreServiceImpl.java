package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.OperatoreEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.OperatoreNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.OperatoreService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Operatore;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Sesso;

public class FileOperatoreServiceImpl implements OperatoreService {

	private String operatoreFileName;

	public FileOperatoreServiceImpl(String operatoreFileName) {
		this.operatoreFileName = operatoreFileName;
	}

	@Override
	public Set<Operatore> visualizzaOperatori() throws BusinessException {
		Set<Operatore> operatori = new HashSet<>();
		try {
			FileData fileData = Utility.readAllRows(operatoreFileName);
			for (String[] righe : fileData.getRighe()) {
				Operatore operatore = new Operatore();
				operatore.setNome(righe[0]);
				operatore.setCognome(righe[1]);
				operatore.setEmail(righe[2]);
				operatore.setPassword(righe[3]);
				operatore.setCodiceFiscale(righe[4]);
				operatore.setTelefono(righe[5]);
				operatore.setDataNascita(LocalDate.parse(righe[6]));
				operatore.setSesso(Sesso.valueOf(righe[7]));
				operatori.add(operatore);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
		return operatori;
	}

	@Override
	public void aggiungiOperatore(Operatore operatore) throws BusinessException, OperatoreEsistenteException {
		try {

			FileData fileData = Utility.readAllRows(operatoreFileName);
			for (String[] righe : fileData.getRighe()) {
				if (righe[2].equals(operatore.getEmail()))
					throw new OperatoreEsistenteException();
			}
			try (PrintWriter writer = new PrintWriter(new File(operatoreFileName))) {
				long contatore = fileData.getContatore();
				writer.println(contatore + 1);
				for (String[] righe : fileData.getRighe()) {
					writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
				StringBuilder row = new StringBuilder();
				row.append(operatore.getNome());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(operatore.getCognome());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(operatore.getEmail());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(operatore.getPassword());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(operatore.getCodiceFiscale());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(operatore.getTelefono());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(operatore.getDataNascita().toString());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(operatore.getSesso().toString());
				row.append(Utility.SEPARATORE_COLONNA);
				writer.println(row.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	@Override
	public void modificaOperatore(Operatore operatore) throws BusinessException {
		try {
			FileData fileData = Utility.readAllRows(operatoreFileName);
			try (PrintWriter writer = new PrintWriter(new File(operatoreFileName))) {
				writer.println(fileData.getContatore());
				for (String[] righe : fileData.getRighe()) {
					if (righe[2].equals(operatore.getEmail())) {
						StringBuilder row = new StringBuilder();
						row.append(operatore.getNome());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(operatore.getCognome());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(operatore.getEmail());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(operatore.getPassword());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(operatore.getCodiceFiscale());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(operatore.getTelefono());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(operatore.getDataNascita().toString());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(operatore.getSesso().toString());
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
	//Scrive sul file gli operarori se l'email della riga non è quella indicata
	public void eliminaOperatore(String email) throws BusinessException {
		try {
			FileData fileData = Utility.readAllRows(operatoreFileName);
			try (PrintWriter writer = new PrintWriter(new File(operatoreFileName))) {
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
	public Operatore getOperatore(String email) throws BusinessException, OperatoreNotFoundException {
		Operatore operatore = new Operatore();
		try {
			FileData fileData = Utility.readAllRows(operatoreFileName);
			for (String[] righe : fileData.getRighe()) {
				if (righe[2].equals(email)) {
					operatore.setNome(righe[0]);
					operatore.setCognome(righe[1]);
					operatore.setEmail(righe[2]);
					operatore.setPassword(righe[3]);
					operatore.setCodiceFiscale(righe[4]);
					operatore.setTelefono(righe[5]);
					operatore.setDataNascita(LocalDate.parse(righe[6]));
					operatore.setSesso(Sesso.valueOf(righe[7]));
					return operatore;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
		throw new OperatoreNotFoundException();
	}

}
