package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NotificaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Notifica;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Stato;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class RAMNoleggioServiceImpl implements NoleggioService {

	private static List<Noleggio> noleggi = new ArrayList<>();
	private NotificaService notificaService = new RAMNotificaServiceImpl();

	@Override
	public List<Noleggio> visualizzaNoleggiInCorso() throws BusinessException {
		List<Noleggio> noleggiInCorso = new ArrayList<>();
		for (Noleggio n : noleggi) {
			if (!(n.getPrenotazione().getDataRitiro().isAfter(LocalDate.now()))
					&& (!n.getDataRiconsegna().isBefore(LocalDate.now())))
				noleggiInCorso.add(n);
		}
		return noleggiInCorso;
	}

	@Override
	public List<Noleggio> visualizzaNoleggi() throws BusinessException {
		return noleggi;
	}

	@Override
	public List<Noleggio> visualizzaNoleggiAssistiti() throws BusinessException {
		Set<Noleggio> noleggiAssistiti = new HashSet<>();
		for (Noleggio n : noleggi) {
			if (!(n.getInterventi().isEmpty()))
				noleggiAssistiti.add(n);
		}
		return new ArrayList<>(noleggiAssistiti);
	}

	@Override
	public List<Noleggio> visualizzaNoleggiInRiconsegna(LocalDate data) throws BusinessException {
		List<Noleggio> noleggiInRiconsegna = new ArrayList<>();
		for (Noleggio n : noleggi) {
			if (n.getDataRiconsegna().isEqual(data))
				noleggiInRiconsegna.add(n);
		}
		return noleggiInRiconsegna;

	}

	@Override
	public void aggiungiNoleggio(Noleggio noleggio) throws BusinessException {
		noleggi.add(noleggio);
	}

	@Override
	public Noleggio getNoleggio(Integer id) throws BusinessException, NoleggioNotFoundException {
		for (Noleggio noleggio : noleggi)
			if (noleggio.getId() == id)
				return noleggio;
		throw new NoleggioNotFoundException();
	}

	@Override
	public void gestioneRiconsegna(Noleggio noleggio, Integer km, Double costo) throws BusinessException {
		if (noleggio.isPagamentoEffettuato() == false) {
			noleggio.setPagamentoEffettuato(true);
			noleggio.setCosto(costo);
			noleggio.getVeicolo().setStato(Stato.DISPONIBILE);
			noleggio.getVeicolo().setChilometriPercorsi(km);
			Notifica notificaFeedbackUtente = new Notifica(noleggio.getDataRiconsegna(),
					"Lascia un feedback sul veicolo!", noleggio.getPrenotazione().getUtente());
			notificaService.visualizzaNotifiche().add(notificaFeedbackUtente);

		} else {
			noleggio.getVeicolo().setStato(Stato.NON_DISPONIBILE);
			Veicolo veicoloSostitutivo = noleggio.getVeicoliSostitutivi()
					.get(noleggio.getVeicoliSostitutivi().size() - 1);
			veicoloSostitutivo.setChilometriPercorsi(km);
			veicoloSostitutivo.setStato(Stato.DISPONIBILE);
			noleggio.getVeicoliSostitutivi().remove(noleggio.getVeicoliSostitutivi().size() - 1);
		}

	}

}