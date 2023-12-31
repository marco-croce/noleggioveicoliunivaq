package it.univaq.disim.oop.noleggioveicoliunivaq.business;

import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tipologia;

public interface TipologiaService {

	List<Tipologia> visualizzaTipologie() throws BusinessException;

	Integer getIdCounter();

	void aggiungiTipologia(Tipologia t) throws BusinessException, TipologiaEsistenteException;

	void eliminaTipologia(Tipologia t) throws BusinessException, TipologiaNonEliminabileException;

	void modificaTipologia(Tipologia t) throws BusinessException;

	public Tipologia getTipologiaById(Integer id) throws BusinessException;
}
