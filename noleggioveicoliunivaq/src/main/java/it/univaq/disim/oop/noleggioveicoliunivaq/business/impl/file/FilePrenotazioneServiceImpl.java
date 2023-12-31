package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneNonEffettuabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TariffaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNonPrenotabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Categoria;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Prenotazione;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Stato;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tariffa;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class FilePrenotazioneServiceImpl implements PrenotazioneService {

	private String prenotazioneFileName;
	private VeicoloService veicoloService;
	private NoleggioService noleggioService;
	private UtenteService utenteService;
	private TariffaService tariffaService;

	private static Veicolo veicolo;

	public FilePrenotazioneServiceImpl(String prenotazioneFileName, NoleggioService noleggioService,
			VeicoloService veicoloService, UtenteService utenteService, TariffaService tariffaService) {
		this.prenotazioneFileName = prenotazioneFileName;
		this.noleggioService = noleggioService;
		this.veicoloService = veicoloService;
		this.utenteService = utenteService;
		this.tariffaService = tariffaService;
	}

	@Override
	public List<Prenotazione> visualizzaPrenotazioni() throws BusinessException {
		List<Prenotazione> prenotazioni = new ArrayList<>();
		try {
			FileData fileData = Utility.readAllRows(prenotazioneFileName);
			for (String[] colonne : fileData.getRighe()) {
				// 1, 2021-7-12, 1, 3, false
				Prenotazione prenotazione = new Prenotazione();
				prenotazione.setId(Integer.parseInt(colonne[0]));
				prenotazione.setDataRitiro(LocalDate.parse(colonne[1]));
				prenotazione.setUtente(utenteService.getUtente(colonne[2]));
				prenotazione.setNoleggio(noleggioService.getNoleggio(Integer.parseInt(colonne[3])));
				prenotazione.setGestito(Boolean.parseBoolean(colonne[4]));
				prenotazioni.add(prenotazione);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		} catch (NoleggioNotFoundException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
		return prenotazioni;
	}

	@Override
	public void prenota(Prenotazione prenotazione, Integer chilometraggio, LocalDate dataRiconsegna, Double costo)
			throws BusinessException, PrenotazioneNonEffettuabileException, VeicoloNonPrenotabileException {

		Noleggio n = new Noleggio(chilometraggio, dataRiconsegna, false, prenotazione, veicolo);
		prenotazione.setNoleggio(n);

		if (!(verificaPrenotazioniUtente(prenotazione)))
			throw new PrenotazioneNonEffettuabileException();

		if (!(verificaPrenotazioniVeicolo(prenotazione)))
			throw new VeicoloNonPrenotabileException();

		FileData fileData;
		try {
			fileData = Utility.readAllRows(prenotazioneFileName);
			veicolo.setStato(Stato.IN_NOLEGGIO);
			veicoloService.modificaVeicolo(veicolo);
			int contatore = (int) fileData.getContatore();
			prenotazione.setId(contatore);
			n.setId(prenotazione.getId());
			n.setCosto(costo);
			prenotazione.setNoleggio(n);
			n.setPrenotazione(prenotazione);
			noleggioService.aggiungiNoleggio(n);
			aggiungiPrenotazione(prenotazione);
			veicolo = null;
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	private boolean verificaPrenotazioniUtente(Prenotazione prenotazione) throws BusinessException {

		List<Prenotazione> prenotazioniUtente = new ArrayList<>();
		// Aggiunta nella lista delle prenotazioni già effettuate dall'utente che sta
		// cercando di prenotare
		try {
			FileData fileData = Utility.readAllRows(prenotazioneFileName);
			for (String[] colonne : fileData.getRighe()) {
				// 1, 2021-7-12, 1, 3, false
				if (colonne[2].equals(prenotazione.getUtente().getEmail())) {
					Prenotazione prenotazioneprov = new Prenotazione();
					prenotazioneprov.setId(Integer.parseInt(colonne[0]));
					prenotazioneprov.setDataRitiro(LocalDate.parse(colonne[1]));
					prenotazioneprov.setUtente(utenteService.getUtente(colonne[2]));
					prenotazioneprov.setNoleggio(noleggioService.getNoleggio(Integer.parseInt(colonne[3])));
					prenotazioneprov.setGestito(Boolean.parseBoolean(colonne[4]));
					prenotazioniUtente.add(prenotazioneprov);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		} catch (NoleggioNotFoundException e) {
			e.printStackTrace();
		}

		return verificaDatePrenotazioni(prenotazione, prenotazioniUtente);
	}

	private boolean verificaDatePrenotazioni(Prenotazione prenotazione, List<Prenotazione> listaPrenotazioni) {
		for (Prenotazione p : listaPrenotazioni) {
				if(prenotazione.getDataRitiro().isBefore(p.getDataRitiro())&&(!prenotazione.getNoleggio().getDataRiconsegna().isBefore(p.getDataRitiro())))
				return false;
				if(prenotazione.getDataRitiro().isAfter(p.getDataRitiro())&&(!prenotazione.getNoleggio().getDataRiconsegna().isAfter(p.getNoleggio().getDataRiconsegna())))
				return false;
				if(prenotazione.getDataRitiro().isEqual(p.getDataRitiro())||(prenotazione.getNoleggio().getDataRiconsegna().isEqual(p.getNoleggio().getDataRiconsegna())))
				return false;
		}

		
		return true;
	}

	@Override
	public Double calcolaCosto(Tariffa tariffa, int periodo) {
		double costo = 0;
		switch (tariffa.getCategoria().ordinal()) {
			case 0:
				costo = tariffa.getPrezzo() * periodo;
				break;
			case 1:
				if (periodo % 7 != 0)
					periodo = (periodo / 7) + 1;
				else
					periodo = periodo / 7;
				costo = tariffa.getPrezzo() * periodo;
				break;
			case 2:
				if (periodo % 28 != 0)
					periodo = (periodo / 28) + 1;
				else
					periodo = periodo / 28;
				costo = tariffa.getPrezzo() * periodo;
				break;
		}
		return costo;
	}

	@Override
	public Tariffa scegliTariffa(int periodo) throws BusinessException {
		List<Tariffa> tariffeVeicolo = tariffaService.visualizzaTariffe(veicolo);
		if (periodo < 7) {
			for (Tariffa tariffa : tariffeVeicolo) {
				if (tariffa.getCategoria() == Categoria.GIORNALIERO)
					return tariffa;
				if (tariffa.getCategoria() == Categoria.SETTIMANALE)
					return tariffa;
				if (tariffa.getCategoria() == Categoria.MENSILE)
					return tariffa;
			}
		}
		if (periodo > 7 && periodo < 28) {
			for (Tariffa tariffa : tariffeVeicolo) {
				if (tariffa.getCategoria() == Categoria.SETTIMANALE)
					return tariffa;
				if (tariffa.getCategoria() == Categoria.MENSILE)
					return tariffa;
				if (tariffa.getCategoria() == Categoria.GIORNALIERO)
					return tariffa;
			}
		}
		if (periodo > 28) {
			for (Tariffa tariffa : tariffeVeicolo) {
				if (tariffa.getCategoria() == Categoria.MENSILE)
					return tariffa;
				if (tariffa.getCategoria() == Categoria.SETTIMANALE)
					return tariffa;
				if (tariffa.getCategoria() == Categoria.GIORNALIERO)
					return tariffa;
			}
		}
		return null;
	}

	private boolean verificaPrenotazioniVeicolo(Prenotazione prenotazione) throws BusinessException {
		List<Prenotazione> prenotazioniVeicolo = new ArrayList<>();
		// Aggiunta nella lista delle prenotazioni già effettuate per il veicolo scelto
		try {
			FileData fileData = Utility.readAllRows(prenotazioneFileName);
			for (String[] colonne : fileData.getRighe()) {
				// 1, 2021-7-12, 1, 3, false
				if (colonne[3].equals(prenotazione.getNoleggio().getVeicolo().getTarga())) {
					Prenotazione prenotazioneprov = new Prenotazione();
					prenotazioneprov.setId(Integer.parseInt(colonne[0]));
					prenotazioneprov.setDataRitiro(LocalDate.parse(colonne[1]));
					prenotazioneprov.setUtente(utenteService.getUtente(colonne[2]));
					prenotazioneprov.setNoleggio(noleggioService.getNoleggio(Integer.parseInt(colonne[3])));
					prenotazioneprov.setGestito(Boolean.parseBoolean(colonne[4]));
					prenotazioniVeicolo.add(prenotazioneprov);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		} catch (NoleggioNotFoundException e) {
			e.printStackTrace();
		}

		return verificaDatePrenotazioni(prenotazione, prenotazioniVeicolo);
	}

	@Override
	public void setVeicoloScelto(String targa) throws BusinessException {
		try {
			veicolo = veicoloService.getVeicolo(targa);
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (VeicoloNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Veicolo getVeicoloScelto() throws BusinessException {
		return veicolo;
	}

	@Override
	public void aggiungiPrenotazione(Prenotazione p) throws BusinessException {
		try {
			FileData fileData = Utility.readAllRows(prenotazioneFileName);
			try (PrintWriter writer = new PrintWriter(new File(prenotazioneFileName))) {
				long contatore = fileData.getContatore();
				writer.println((contatore + 1));
				for (String[] righe : fileData.getRighe()) {
					writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
				}
				// 1, 2021-7-12, 1, 3, false
				StringBuilder row = new StringBuilder();
				row.append(contatore);
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(p.getDataRitiro().toString());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(p.getUtente().getEmail());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(p.getNoleggio().getId().toString());
				row.append(Utility.SEPARATORE_COLONNA);
				row.append(p.isGestito());
				writer.println(row.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	@Override
	public Prenotazione getPrenotazione(Integer id) throws BusinessException, PrenotazioneNotFoundException {
		Prenotazione p = new Prenotazione();
		try {
			FileData fileData = Utility.readAllRows(prenotazioneFileName);
			for (String[] colonne : fileData.getRighe()) {
				if (Integer.parseInt(colonne[0]) == id) {
					p.setId(Integer.parseInt(colonne[0]));
					p.setDataRitiro(LocalDate.parse(colonne[1]));
					p.setUtente(utenteService.getUtente(colonne[2]));
					p.setNoleggio(noleggioService.getNoleggio(Integer.parseInt(colonne[3])));
					p.setGestito(Boolean.parseBoolean(colonne[4]));
					return p;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(e);
		} catch (NoleggioNotFoundException e) {
			e.printStackTrace();
		}
		throw new PrenotazioneNotFoundException();
	}

	
}
