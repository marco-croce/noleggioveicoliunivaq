package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneNonEffettuabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNonPrenotabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Categoria;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Prenotazione;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Stato;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tariffa;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class RAMPrenotazioneServiceImpl implements PrenotazioneService {

	private static List<Prenotazione> prenotazioni = new ArrayList<>();
	private static Veicolo veicolo;
	private static int idCounter = 1;
	private VeicoloService veicoloService;
	private NoleggioService noleggioService;

	public RAMPrenotazioneServiceImpl() {
		noleggioService = new RAMNoleggioServiceImpl();
		veicoloService = new RAMVeicoloServiceImpl();
	}

	@Override
	public List<Prenotazione> visualizzaPrenotazioni() throws BusinessException {
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

		veicolo.setStato(Stato.IN_NOLEGGIO);
		prenotazione.setId(idCounter++);
		n.setId(prenotazione.getId());
		n.setCosto(costo);
		prenotazione.setNoleggio(n);
		noleggioService.aggiungiNoleggio(n);
		prenotazioni.add(prenotazione);
		veicolo = null;
	}

	/*
	 * Effettua il calcolo del costo del noleggio cercando di utilizzare la tariffa
	 * più consona al periodo del noleggio che l'utente sta cercando di prenotare
	 */
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
		Set<Tariffa> tariffeVeicolo = veicolo.getTariffe();
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

	private boolean verificaPrenotazioniVeicolo(Prenotazione prenotazione) {
		List<Prenotazione> prenotazioniVeicolo = new ArrayList<>();
		// Aggiunta nella lista delle prenotazioni già effettuate per il veicolo scelto
		for (Prenotazione p : prenotazioni) {
			Veicolo v = prenotazione.getNoleggio().getVeicolo();
			if (p.getNoleggio().getVeicolo().getTarga().equals(v.getTarga()))
				prenotazioniVeicolo.add(p);
		}

		return verificaDatePrenotazioni(prenotazione, prenotazioniVeicolo);
	}

	private boolean verificaPrenotazioniUtente(Prenotazione prenotazione) {

		List<Prenotazione> prenotazioniUtente = new ArrayList<>();
		// Aggiunta nella lista delle prenotazioni già effettuate dall'utente che sta
		// cercando di prenotare
		for (Prenotazione p : prenotazioni) {
			if (p.getUtente().getEmail().equals(prenotazione.getUtente().getEmail()))
				prenotazioniUtente.add(p);
		}

		return verificaDatePrenotazioni(prenotazione, prenotazioniUtente);

	}

	private boolean verificaDatePrenotazioni(Prenotazione prenotazione, List<Prenotazione> listaPrenotazioni) {
		for (Prenotazione p : listaPrenotazioni) {
			if (prenotazione.getDataRitiro().isBefore(p.getDataRitiro())
					&& (!prenotazione.getNoleggio().getDataRiconsegna().isBefore(p.getDataRitiro())))
				return false;
			if (prenotazione.getDataRitiro().isAfter(p.getDataRitiro())
					&& (!prenotazione.getNoleggio().getDataRiconsegna().isAfter(p.getNoleggio().getDataRiconsegna())))
				return false;
			if (prenotazione.getDataRitiro().isEqual(p.getDataRitiro())
					|| (prenotazione.getNoleggio().getDataRiconsegna().isEqual(p.getNoleggio().getDataRiconsegna())))
				return false;
		}
		return true;
	}

	@Override
	public void setVeicoloScelto(String targa) throws BusinessException {
		for (Veicolo v : veicoloService.visualizzaVeicoli())
			if (v.getTarga().equals(targa)) {
				veicolo = v;
				return;
			}
	}

	@Override
	public Veicolo getVeicoloScelto() throws BusinessException {
		return veicolo;
	}

	@Override
	public void aggiungiPrenotazione(Prenotazione p) throws BusinessException {
		prenotazioni.add(p);
	}

	@Override
	public Prenotazione getPrenotazione(Integer id) throws BusinessException, PrenotazioneNotFoundException {
		for (Prenotazione p : prenotazioni) {
			if (p.getId() == id)
				return p;
		}
		throw new PrenotazioneNotFoundException();
	}

}
