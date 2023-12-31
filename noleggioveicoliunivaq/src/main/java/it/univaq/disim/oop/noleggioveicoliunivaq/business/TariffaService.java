package it.univaq.disim.oop.noleggioveicoliunivaq.business;

import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tariffa;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public interface TariffaService {

	List<Tariffa> visualizzaTariffe(Veicolo v) throws BusinessException;

	Object getIdCounter() throws BusinessException;

	void aggiungiTariffa(Tariffa t, Veicolo veicolo) throws BusinessException, CategoriaNonAggiungibileException;

	void eliminaTariffa(Tariffa t) throws BusinessException;

	void modificaTariffa(Tariffa t) throws BusinessException, CategoriaNonAggiungibileException;

}
