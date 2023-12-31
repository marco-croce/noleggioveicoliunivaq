package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNonEliminabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Alimentazione;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Stato;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tipologia;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class RAMVeicoloServiceImpl implements VeicoloService {

	private static Set<Veicolo> veicoli = new HashSet<>();

	private TipologiaService tipologie;
	private NoleggioService noleggioService;

	public RAMVeicoloServiceImpl() {
		tipologie = new RAMTipologiaServiceImpl();
		noleggioService = new RAMNoleggioServiceImpl();
	}

	@Override
	public List<Veicolo> visualizzaVeicoli() throws BusinessException {
		return new ArrayList<Veicolo>(veicoli);
	}

	public List<String> visualizzaNomiTipologie() throws BusinessException {
		Set<String> nomiTipologie = new HashSet<>();
		for (Tipologia t : tipologie.visualizzaTipologie()) {
			nomiTipologie.add(t.getNome());
		}
		return new ArrayList<String>(nomiTipologie);
	}

	@Override
	public List<Veicolo> visualizzaVeicoli(Stato stato) throws BusinessException {
		List<Veicolo> listaVeicoli = new ArrayList<>();
		for (Veicolo v : veicoli) {
			if (v.getStato().equals(stato))
				listaVeicoli.add(v);
		}
		return listaVeicoli;
	}

	@Override
	public List<Veicolo> visualizzaVeicoli(Alimentazione alimentazione) throws BusinessException {
		List<Veicolo> listaVeicoli = new ArrayList<>();
		for (Veicolo v : veicoli) {
			if (v.getAlimentazione().equals(alimentazione))
				listaVeicoli.add(v);
		}
		return listaVeicoli;
	}

	@Override
	public List<Veicolo> visualizzaVeicoli(Tipologia tipologia) throws BusinessException {
		List<Veicolo> listaVeicoli = new ArrayList<>();
		for (Veicolo v : veicoli) {
			if (v.getTipologia().equals(tipologia))
				listaVeicoli.add(v);
		}
		return listaVeicoli;
	}

	@Override
	public List<Veicolo> visualizzaVeicoli(LocalDate dal, LocalDate al) throws BusinessException {
		List<Noleggio> listaNoleggi = new ArrayList<>(noleggioService.visualizzaNoleggi());
		Set<Veicolo> listaVeicoli = new HashSet<>(veicoli);
		for (Noleggio n : listaNoleggi) {
			if (n.getPrenotazione().getDataRitiro().isBefore(al) || n.getDataRiconsegna().isAfter(dal)) {
				listaVeicoli.remove(n.getVeicolo());
			}

		}
		return new ArrayList<Veicolo>(listaVeicoli);
	}

	@Override
	public void aggiungiVeicolo(Veicolo v) throws BusinessException, VeicoloEsistenteException {
		for (Veicolo veicolo : veicoli)
			if (veicolo.getTarga().equals(v.getTarga()))
				throw new VeicoloEsistenteException();
		veicoli.add(v);
		v.getTipologia().addVeicolo(v);
	}

	@Override
	public void eliminaVeicolo(Veicolo v) throws BusinessException, VeicoloNonEliminabileException {
		if (!(v.getStato().equals(Stato.DISPONIBILE)))
			throw new VeicoloNonEliminabileException();
		v.getTipologia().getVeicoli().remove(v);
		veicoli.remove(v);
	}

	@Override
	public void modificaVeicolo(Veicolo v) throws BusinessException {
		for (Iterator<Veicolo> i = veicoli.iterator(); i.hasNext();) {
			Veicolo veicolo = i.next();
			if (veicolo.getTarga().equals(v.getTarga())) {
				veicolo.setMarca(v.getMarca());
				veicolo.setModello(v.getModello());
				veicolo.setColore(v.getColore());
				veicolo.setChilometriPercorsi(v.getChilometriPercorsi());
				veicolo.setNumeroPorte(v.getNumeroPorte());
				veicolo.setNumeroPosti(v.getNumeroPosti());
				veicolo.setConsumo(v.getConsumo());
				veicolo.setStato(v.getStato());
				veicolo.setAlimentazione(v.getAlimentazione());
				veicolo.setPotenza(v.getPotenza());
				veicolo.setCambio(v.getCambio());
				veicolo.setTipologia(v.getTipologia());
			}
		}
	}

	@Override
	public List<Veicolo> visualizzaVeicoli(double min, double max) throws BusinessException {
		List<Veicolo> listaVeicoli = new ArrayList<>();
		for (Veicolo v : veicoli) {
			if (v.getConsumo() >= min && v.getConsumo() <= max)
				listaVeicoli.add(v);
		}
		return listaVeicoli;
	}

	@Override
	public Veicolo getVeicolo(String targa) throws BusinessException, VeicoloNotFoundException {
		for (Veicolo v : veicoli) {
			if (v.getTarga().equals(targa))
				return v;
		}
		throw new VeicoloNotFoundException();
	}

	@Override
	public Integer getNumeroVeicoli(Tipologia t) throws BusinessException {
		int cont = 0;
		for (Veicolo v : visualizzaVeicoli()) {
			if (v.getTipologia().getId().toString().equals(t.getId().toString()))
				cont++;
		}
		return cont;
	}

	@Override
	public List<Veicolo> visualizzaVeicoli(Stato stato, Stato stato1) throws BusinessException {
		List<Veicolo> listaVeicoli = new ArrayList<>();
		for (Veicolo v : veicoli) {
			if (v.getStato().equals(stato) || v.getStato().equals(stato1))
				listaVeicoli.add(v);
		}
		return listaVeicoli;
	}

}