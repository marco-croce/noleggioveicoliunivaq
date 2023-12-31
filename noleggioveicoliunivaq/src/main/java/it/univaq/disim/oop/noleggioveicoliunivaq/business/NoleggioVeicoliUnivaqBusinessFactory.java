package it.univaq.disim.oop.noleggioveicoliunivaq.business;

//import it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file.FileNoleggioVeicoliUnivaqBusinessFactoryImpl;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.ram.RAMNoleggioVeicoliUnivaqBusinessFactoryImpl;

public abstract class NoleggioVeicoliUnivaqBusinessFactory {

    private static NoleggioVeicoliUnivaqBusinessFactory factory = new RAMNoleggioVeicoliUnivaqBusinessFactoryImpl();
    //private static NoleggioVeicoliUnivaqBusinessFactory factory = new FileNoleggioVeicoliUnivaqBusinessFactoryImpl();

    public static NoleggioVeicoliUnivaqBusinessFactory getInstance() {
        return factory;
    }

    public abstract FeedbackService getFeedbackService();

    public abstract NotificaService getNotificaService();

    public abstract OperatoreService getOperatoreService();

    public abstract PersonaService getPersonaService();

    public abstract PrenotazioneService getPrenotazioneService();

    public abstract TariffaService getTariffaService();

    public abstract TipologiaService getTipologiaService();

    public abstract UtenteService getUtenteService();

    public abstract VeicoloService getVeicoloService();

    public abstract NoleggioService getNoleggioService();

    public abstract InterventoService getInterventoService();

}
