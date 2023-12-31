package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.InterventoService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Intervento;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Stato;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class RAMInterventoServiceImpl implements InterventoService {

	private static List<Intervento> interventiRichiesti = new ArrayList<>();
	private static Veicolo veicolo;
	private static Intervento interventoGestito;
	private static Integer idCounter = 1;

	public RAMInterventoServiceImpl() {

	}

	@Override
	public List<Intervento> visualizzaInterventi() throws BusinessException {
		return interventiRichiesti;
	}

	@Override
	public void richiediIntervento(Intervento intervento) throws BusinessException {
		intervento.setId(idCounter++);
		interventiRichiesti.add(intervento);
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

	@Override
	public void gestisciIntervento(Intervento intervento, LocalDateTime dataIntervento) throws BusinessException {
		intervento.setGestito(true);
		intervento.setDataIntervento(dataIntervento);

		/*
		 * Verifica se l'operatore ha optato per una semplice assistenza oppure per una
		 * sostituzione del veicolo durante la gestione dell'intervento di assistenza
		 */
		if (veicolo != null) {
			intervento.setSostituito(true);
			intervento.getNoleggio().getVeicoliSostitutivi().add(veicolo);
			veicolo.setStato(Stato.IN_NOLEGGIO);
			intervento.getNoleggio().getVeicolo().setStato(Stato.NON_DISPONIBILE);
		} else
			intervento.setSostituito(false);
	}

}
