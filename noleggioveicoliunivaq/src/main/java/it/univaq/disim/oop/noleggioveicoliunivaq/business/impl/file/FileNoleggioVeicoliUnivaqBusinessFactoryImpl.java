package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.File;
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

public class FileNoleggioVeicoliUnivaqBusinessFactoryImpl extends NoleggioVeicoliUnivaqBusinessFactory {

    private FeedbackService feedbackService;
    private InterventoService interventoService;
    private NoleggioService noleggioService;
    private NotificaService notificaService;
    private OperatoreService operatoreService;
    private PersonaService personaService;
    private PrenotazioneService prenotazioneService;
    private static PrenotazioneService prenotazioneService2;
    private TariffaService tariffaService;
    private TipologiaService tipologiaService;
    private UtenteService utenteService;
    private VeicoloService veicoloService;

    private static final String REPOSITORY_BASE = "src" + File.separator + "main" + File.separator + "resources"
            + File.separator + "dati";
    private static final String FEEDBACK_FILE_NAME = REPOSITORY_BASE + File.separator + "feedback.txt";
    private static final String NOTIFICA_FILE_NAME = REPOSITORY_BASE + File.separator + "notifiche.txt";
    private static final String OPERATORE_FILE_NAME = REPOSITORY_BASE + File.separator + "operatori.txt";
    private static final String PRENOTAZIONE_FILE_NAME = REPOSITORY_BASE + File.separator + "prenotazioni.txt";
    private static final String TARIFFA_FILE_NAME = REPOSITORY_BASE + File.separator + "tariffe.txt";
    private static final String TIPOLOGIA_FILE_NAME = REPOSITORY_BASE + File.separator + "tipologie.txt";
    private static final String UTENTE_FILE_NAME = REPOSITORY_BASE + File.separator + "utenti.txt";
    private static final String VEICOLO_FILE_NAME = REPOSITORY_BASE + File.separator + "veicoli.txt";
    private static final String INTERVENTO_FILE_NAME = REPOSITORY_BASE + File.separator + "interventi.txt";
    private static final String NOLEGGIO_FILE_NAME = REPOSITORY_BASE + File.separator + "noleggi.txt";

    public FileNoleggioVeicoliUnivaqBusinessFactoryImpl() {
        //Ordine delle variabili importante in quanto si usanto tra di loro
        operatoreService = new FileOperatoreServiceImpl(OPERATORE_FILE_NAME);
        utenteService = new FileUtenteServiceImpl(UTENTE_FILE_NAME);
        tipologiaService = new FileTipologiaServiceImpl(TIPOLOGIA_FILE_NAME, VEICOLO_FILE_NAME);
        personaService = new FilePersonaServiceImpl(operatoreService, utenteService);
        veicoloService = new FileVeicoloServiceImpl(VEICOLO_FILE_NAME, TIPOLOGIA_FILE_NAME, tipologiaService,
                noleggioService);
        feedbackService = new FileFeedbackServiceImpl(FEEDBACK_FILE_NAME, utenteService, veicoloService);
        tariffaService = new FileTariffaServiceImpl(TARIFFA_FILE_NAME, veicoloService);
        noleggioService = new FileNoleggioServiceImpl(NOLEGGIO_FILE_NAME, PRENOTAZIONE_FILE_NAME, UTENTE_FILE_NAME,
                INTERVENTO_FILE_NAME, veicoloService, NOTIFICA_FILE_NAME, VEICOLO_FILE_NAME, utenteService);
        prenotazioneService = new FilePrenotazioneServiceImpl(PRENOTAZIONE_FILE_NAME, noleggioService, veicoloService,
                utenteService, tariffaService);
        prenotazioneService2 = new FilePrenotazioneServiceImpl(PRENOTAZIONE_FILE_NAME, noleggioService, veicoloService,
                utenteService, tariffaService);
        interventoService = new FileInterventoServiceImpl(INTERVENTO_FILE_NAME, utenteService, noleggioService);
        notificaService = new FileNotificaServiceImpl(NOTIFICA_FILE_NAME, utenteService, prenotazioneService,
                PRENOTAZIONE_FILE_NAME);

    }

    @Override
    public FeedbackService getFeedbackService() {
        return feedbackService;
    }

    public static PrenotazioneService getPrenotazioneServiceStatic() {
        return prenotazioneService2;
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