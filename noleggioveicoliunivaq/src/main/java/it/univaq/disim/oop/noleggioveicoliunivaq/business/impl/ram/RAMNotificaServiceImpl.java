package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NotificaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Notifica;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;

public class RAMNotificaServiceImpl implements NotificaService {

	private static List<Notifica> notifiche = new ArrayList<>();

	@Override
	public void inviaNotifica(Notifica notifica) throws BusinessException {
		notifiche.add(notifica);
	}

	@Override
	public Notifica visualizzaNotifica(Notifica notifica) throws BusinessException {
		return null;
	}

	@Override
	public List<Notifica> visualizzaNotifiche(Persona persona) throws BusinessException {
		List<Notifica> lista = new ArrayList<>();
		for (Notifica n : notifiche) {
			if (n.getUtente().getEmail().equals(persona.getEmail()))
				lista.add(n);
		}

		return lista;
	}

	@Override
	public List<Notifica> visualizzaNotifiche() throws BusinessException {
		return notifiche;
	}

	@Override
	public void modificaNotifica(Notifica notifica) throws BusinessException {
		for (Notifica n : notifiche)
			if (n.equals(notifica))
				n.setMessaggio("Grazie per aver lasciato il feedback sul veicolo!");
	}

}
