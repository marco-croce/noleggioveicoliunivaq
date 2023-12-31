package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.InterventoService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Intervento;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class FileInterventoServiceImpl implements InterventoService {

	private String interventoFileName;
	private UtenteService utenteService;
	private NoleggioService noleggioService;

	private static Veicolo veicolo;
	private static Intervento interventoGestito;

	public FileInterventoServiceImpl(String interventoFileName, UtenteService utenteService,
			NoleggioService noleggioService) {
		this.interventoFileName = interventoFileName;
		this.utenteService = utenteService;
		this.noleggioService = noleggioService;
	}

	@Override
	public List<Intervento> visualizzaInterventi() throws BusinessException {
		List<Intervento> interventi = new ArrayList<>();
		try {
			FileData fileData = Utility.readAllRows(interventoFileName);
			for (String[] colonne : fileData.getRighe()) {
				// 1, 2001-3-1 10:00, 2021-2-2, ciao, 2, 4, false, false
				Intervento intervento = new Intervento();
				intervento.setId(Integer.parseInt(colonne[0]));
				if (!colonne[1].equals("-"))
					intervento.setDataIntervento(LocalDateTime.parse(colonne[1]));
				intervento.setDataRichiestaIntervento(LocalDate.parse(colonne[2]));
				intervento.setMessaggio(colonne[3]);
				intervento.setUtente(utenteService.getUtente(colonne[4]));
				intervento.setNoleggio(noleggioService.getNoleggio(Integer.parseInt(colonne[5])));
				intervento.setSostituito(Boolean.parseBoolean(colonne[6]));
				intervento.setGestito(Boolean.parseBoolean(colonne[7]));
				interventi.add(intervento);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		} catch (NoleggioNotFoundException e) {
			e.printStackTrace();
		}
		return interventi;
	}

	@Override
	public void richiediIntervento(Intervento intervento) throws BusinessException {
		try {
			FileData fileData = Utility.readAllRows(interventoFileName);
			try (PrintWriter writer = new PrintWriter(new File(interventoFileName))) {
				long contatore = fileData.getContatore();
				writer.println((contatore + 1));
				for (String[] righe : fileData.getRighe()) {
					writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
				// 1, 2001-3-1 10:00, 2021-2-2, ciao, 2, 4, false, false
				StringBuilder row = new StringBuilder();
				row.append(contatore);
				row.append(Utility.SEPARATORE_COLONNA);
				row.append("-");
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(intervento.getDataRichiestaIntervento().toString());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(intervento.getMessaggio());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(intervento.getUtente().getEmail());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(intervento.getNoleggio().getId().toString());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(intervento.isSostituito());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(intervento.isGestito());
				writer.println(row.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	@Override
	public void setVeicoloSostitutivo(Veicolo v) throws BusinessException {
		veicolo = v;
	}

	@Override
	public Veicolo getVeicoloSostitutivo() throws BusinessException {
		return veicolo;
	}

	@Override
	public void setInterventoGestito(Intervento i) throws BusinessException {
		interventoGestito = i;

	}

	@Override
	public Intervento getInterventoGestito() throws BusinessException {
		return interventoGestito;
	}

	public void setGestito(Intervento intervento) throws BusinessException {
		try {
			FileData fileData = Utility.readAllRows(interventoFileName);
			try (PrintWriter writer = new PrintWriter(new File(interventoFileName))) {
				long contatore = fileData.getContatore();
				writer.println((contatore));
				for (String[] righe : fileData.getRighe()) {
					if (intervento.getId().toString().equals(righe[0])) {
						StringBuilder row = new StringBuilder();
						row.append(righe[0]);
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(LocalDate.now().atTime(10, 0).toString());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(righe[2]);
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(righe[3]);
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(righe[4]);
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(righe[5]);
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(righe[6]);
						row.append(Utility.SEPARATORE_COLONNA);
						row.append("true");
						writer.println(row.toString());
					} else
						writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}

	}

	public void setSostituito(Intervento intervento) throws BusinessException {
		try {
			FileData fileData = Utility.readAllRows(interventoFileName);
			try (PrintWriter writer = new PrintWriter(new File(interventoFileName))) {
				long contatore = fileData.getContatore();
				writer.println((contatore));
				for (String[] righe : fileData.getRighe()) {
					if (intervento.getId().toString().equals(righe[0])) {
						StringBuilder row = new StringBuilder();
						row.append(righe[0]);
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(LocalDate.now().atTime(10, 0).toString());
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(righe[2]);
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(righe[3]);
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(righe[4]);
						row.append(Utility.SEPARATORE_COLONNA);
						row.append(righe[5]);
						row.append(Utility.SEPARATORE_COLONNA);
						row.append("true");
						row.append(Utility.SEPARATORE_COLONNA);
						row.append("true");
						writer.println(row.toString());
					} else
						writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}

	}

	@Override
	public void gestisciIntervento(Intervento intervento, LocalDateTime dataIntervento) throws BusinessException {
		setGestito(intervento);
		setSostituito(intervento);

		if (veicolo != null) {
			setSostituito(intervento);
		}
	}

}
