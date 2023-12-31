package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaNonEliminabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tipologia;

public class RAMTipologiaServiceImpl implements TipologiaService {

	private static Set<Tipologia> tipologie = new HashSet<>();
	private static int idCounter = 1;

	public Integer getIdCounter() {
		return idCounter;
	}

	@Override
	public List<Tipologia> visualizzaTipologie() throws BusinessException {

		return new ArrayList<>(tipologie);
	}

	@Override
	public void aggiungiTipologia(Tipologia t) throws BusinessException, TipologiaEsistenteException {
		for (Tipologia tipologia : tipologie)
			if (tipologia.getNome().equals(t.getNome()) || tipologia.getId().equals(t.getId()))
				throw new TipologiaEsistenteException();
		tipologie.add(t);
		idCounter++;
	}

	@Override
	public void eliminaTipologia(Tipologia t) throws BusinessException, TipologiaNonEliminabileException {
		if (!t.getVeicoli().isEmpty()) {
			throw new TipologiaNonEliminabileException();
		}
		tipologie.remove(t);
	}

	@Override
	public void modificaTipologia(Tipologia t) throws BusinessException {
		for (Iterator<Tipologia> i = tipologie.iterator(); i.hasNext();) {
			Tipologia tipologia = i.next();
			if (tipologia.getNome().equals(t.getNome()) && tipologia.getId().equals(t.getId())) {
				tipologia.setNome(t.getNome());
			}
		}
	}

	@Override
	public Tipologia getTipologiaById(Integer id) throws BusinessException {
		return null;
	}

}
