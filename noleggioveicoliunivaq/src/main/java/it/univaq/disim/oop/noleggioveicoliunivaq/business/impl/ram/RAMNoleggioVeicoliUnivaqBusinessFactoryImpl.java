package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.FeedbackService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.InterventoService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioVeicoliUnivaqBusinessFactory;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NotificaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.OperatoreService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PersonaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TariffaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;

public class RAMNoleggioVeicoliUnivaqBusinessFactoryImpl extends NoleggioVeicoliUnivaqBusinessFactory {

    private FeedbackService feedbackService;
    private InterventoService interventoService;
    private NoleggioService noleggioService;
    private NotificaService notificaService;
    private OperatoreService operatoreService;
    private PersonaService personaService;
    private PrenotazioneService prenotazioneService;
    private TariffaService tariffaService;
    private TipologiaService tipologiaService;
    private UtenteService utenteService;
    private VeicoloService veicoloService;

    public RAMNoleggioVeicoliUnivaqBusinessFactoryImpl() {
        feedbackService = new RAMFeedbackServiceImpl();
        notificaService = new RAMNotificaServiceImpl();
        operatoreService = new RAMOperatoreServiceImpl();
        personaService = new RAMPersonaServiceImpl();
        prenotazioneService = new RAMPrenotazioneServiceImpl();
        tariffaService = new RAMTariffaServiceImpl();
        tipologiaService = new RAMTipologiaServiceImpl();
        utenteService = new RAMUtenteServiceImpl();
        veicoloService = new RAMVeicoloServiceImpl();
        interventoService = new RAMInterventoServiceImpl();
        noleggioService = new RAMNoleggioServiceImpl();
    }

    @Override
    public FeedbackService getFeedbackService() {
        return feedbackService;
    }

    @Override
    public NotificaService getNotificaService() {
        return notificaService;
    }

    @Override
    public OperatoreService getOperatoreService() {
        return operatoreService;
    }

    @Override
    public PersonaService getPersonaService() {
        return personaService;
    }

    @Override
    public PrenotazioneService getPrenotazioneService() {
        return prenotazioneService;
    }

    @Override
    public TariffaService getTariffaService() {
        return tariffaService;
    }

    @Override
    public TipologiaService getTipologiaService() {
        return tipologiaService;
    }

    @Override
    public UtenteService getUtenteService() {
        return utenteService;
    }

    @Override
    public VeicoloService getVeicoloService() {
        return veicoloService;
    }

    @Override
    public NoleggioService getNoleggioService() {
        return noleggioService;
    }

    @Override
    public InterventoService getInterventoService() {
        return interventoService;
    }

}
