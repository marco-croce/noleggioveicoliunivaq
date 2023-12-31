package it.univaq.disim.oop.noleggioveicoliunivaq.business;

import java.time.LocalDateTime;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Intervento;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public interface InterventoService {

	List<Intervento> visualizzaInterventi() throws BusinessException;

	void richiediIntervento(Intervento intervento) throws BusinessException;

	public void setVeicoloSostitutivo(Veicolo v) throws BusinessException;

	public Veicolo getVeicoloSostitutivo() throws BusinessException;

	public void gestisciIntervento(Intervento intervento, LocalDateTime dataIntervento) throws BusinessException;

	public void setInterventoGestito(Intervento i) throws BusinessException;

	public Intervento getInterventoGestito() throws BusinessException;

}
