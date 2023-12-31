package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.CategoriaNonAggiungibileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TariffaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tariffa;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class RAMTariffaServiceImpl implements TariffaService {

    private static Set<Tariffa> tariffe = new HashSet<>();
    private static Integer idCounter = 1;
    private VeicoloService veicoloService = new RAMVeicoloServiceImpl();

    @Override
    public List<Tariffa> visualizzaTariffe(Veicolo v) throws BusinessException {
        Set<Tariffa> lista = new HashSet<>();
        for (Veicolo veicolo : veicoloService.visualizzaVeicoli()) {
            if (v.getTarga().equals(veicolo.getTarga())) {
                lista.addAll(v.getTariffe());
            }
        }
        return new ArrayList<>(lista);
    }

    public Integer getIdCounter() {
        return idCounter;
    }

    @Override
    public void aggiungiTariffa(Tariffa tariffa, Veicolo veicolo)
            throws BusinessException, CategoriaNonAggiungibileException {
        for (Tariffa t : tariffe) {
            if (t.getVeicolo().getTarga().equals(veicolo.getTarga())) {
                if (tariffa.getCategoria().equals(t.getCategoria()))
                    throw new CategoriaNonAggiungibileException();
            }
        }
        tariffa.setVeicolo(veicolo);
        veicolo.addTariffa(tariffa);
        tariffe.add(tariffa);
        idCounter++;
    }

    @Override
    public void eliminaTariffa(Tariffa t) throws BusinessException {
        tariffe.remove(t);
    }

    @Override
    public void modificaTariffa(Tariffa tariffa) throws BusinessException, CategoriaNonAggiungibileException {
        for (Tariffa t : tariffe) {
            if (tariffa.getCategoria().equals(t.getCategoria())) {
                throw new CategoriaNonAggiungibileException();
            }
            if (tariffa.getId().equals(t.getId())) {
                t.setCategoria(tariffa.getCategoria());
                t.setPrezzo(tariffa.getPrezzo());
            }
        }
    }

}
