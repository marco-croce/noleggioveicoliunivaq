package it.univaq.disim.oop.noleggioveicoliunivaq.business;

import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Notifica;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;

public interface NotificaService {

	void inviaNotifica(Notifica notifica) throws BusinessException;

	Notifica visualizzaNotifica(Notifica notifica) throws BusinessException;

	List<Notifica> visualizzaNotifiche(Persona persona) throws BusinessException;

	List<Notifica> visualizzaNotifiche() throws BusinessException;

	void modificaNotifica(Notifica notifica) throws BusinessException;

}
