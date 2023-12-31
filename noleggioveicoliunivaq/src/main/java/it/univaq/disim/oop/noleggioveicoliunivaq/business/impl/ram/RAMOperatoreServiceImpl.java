package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.OperatoreEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.OperatoreNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.OperatoreService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Operatore;

public class RAMOperatoreServiceImpl implements OperatoreService {

	private static Set<Operatore> operatori = new HashSet<>();

	@Override
	public Set<Operatore> visualizzaOperatori() throws BusinessException {
		return operatori;
	}

	@Override
	public void aggiungiOperatore(Operatore operatore) throws BusinessException, OperatoreEsistenteException {
		for (Operatore op : operatori)
			if (op.getEmail().equals(operatore.getEmail()))
				throw new OperatoreEsistenteException();
		operatori.add(operatore);
	}

	@Override
	public void modificaOperatore(Operatore operatore) throws BusinessException {
		for (Iterator<Operatore> i = operatori.iterator(); i.hasNext();) {
			Operatore op = i.next();
			if (op.getEmail().equals(operatore.getEmail())) {
				op.setNome(operatore.getNome());
				op.setCognome(operatore.getCognome());
				op.setEmail(operatore.getEmail());
				op.setPassword(operatore.getPassword());
				op.setDataNascita(operatore.getDataNascita());
				op.setTelefono(operatore.getTelefono());
			}
		}
	}

	@Override
	public void eliminaOperatore(String email) throws BusinessException {
		for (Iterator<Operatore> i = operatori.iterator(); i.hasNext();) {
			Operatore op = i.next();
			if (op.getEmail().equals(email)) {
				i.remove();
			}
		}
	}

	@Override
	public Operatore getOperatore(String email) throws BusinessException, OperatoreNotFoundException {
		for (Operatore operatore : operatori)
			if (operatore.getEmail().equals(email))
				return operatore;
		throw new OperatoreNotFoundException();
	}

}
