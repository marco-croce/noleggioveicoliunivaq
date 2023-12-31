package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Intervento;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Notifica;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Prenotazione;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Sesso;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Stato;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Utente;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class FileNoleggioServiceImpl implements NoleggioService {

    private String noleggioFileName;
    private String interventoFileName;
    private String prenotazioneFileName;
    private String utenteFileName;
    private String veicoloFileName;
    private VeicoloService veicoloService;
    private String notificaFileName;
    private UtenteService utenteService;

    public FileNoleggioServiceImpl(String noleggioFileName, String prenotazioneFileName, String utenteFileName,
            String interventoFileName, VeicoloService veicoloService, String notificaFileName, String veicoloFileName,
            UtenteService utenteService) {
        this.noleggioFileName = noleggioFileName;
        this.interventoFileName = interventoFileName;
        this.utenteFileName = utenteFileName;
        this.veicoloService = veicoloService;
        this.prenotazioneFileName = prenotazioneFileName;
        this.notificaFileName = notificaFileName;
        this.veicoloFileName = veicoloFileName;
        this.utenteService = utenteService;
    }

    @Override
    public void aggiungiNoleggio(Noleggio noleggio) throws BusinessException {
        try {
            FileData fileData = Utility.readAllRows(noleggioFileName);
            try (PrintWriter writer = new PrintWriter(new File(noleggioFileName))) {
                long contatore = fileData.getContatore();
                writer.println((contatore + 1));
                for (String[] righe : fileData.getRighe()) {
                    writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
                }
                StringBuilder row = new StringBuilder();
                // id,km,dataRiconsegna,Pagamentoeffettuato,prenotazione, costo, veicolo
                row.append(contatore);
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(noleggio.getChilometraggioPrevisto().toString());
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(noleggio.getDataRiconsegna().toString());
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(noleggio.isPagamentoEffettuato());
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(noleggio.getPrenotazione().getId());
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(noleggio.getCosto().toString());
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(noleggio.getVeicolo().getTarga());
                writer.println(row.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    @Override
    public List<Noleggio> visualizzaNoleggiInCorso() throws BusinessException {
        List<Noleggio> result = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(noleggioFileName);
            for (String[] colonne : fileData.getRighe()) {
                if (!(FileNoleggioVeicoliUnivaqBusinessFactoryImpl.getPrenotazioneServiceStatic()
                        .getPrenotazione(Integer.parseInt(colonne[4])).getDataRitiro().isAfter(LocalDate.now()))
                        && (!(LocalDate.parse(colonne[2]).isBefore(LocalDate.now())))) {
                    // id,km,dataRiconsegna,Pagamentoeffettuato,prenotazione, costo, veicolo
                    Noleggio noleggio = new Noleggio();
                    noleggio.setId(Integer.parseInt(colonne[0]));
                    noleggio.setChilometraggioPrevisto(Integer.parseInt(colonne[1]));
                    noleggio.setDataRiconsegna(LocalDate.parse(colonne[2]));
                    noleggio.setPagamentoEffettuato(Boolean.parseBoolean(colonne[3]));
                    Prenotazione p = new Prenotazione();
                    try {
                        FileData fileData1 = Utility.readAllRows(prenotazioneFileName);
                        for (String[] colonne1 : fileData1.getRighe()) {
                            if (colonne1[3].equals(colonne[0])) {
                                p.setId(Integer.parseInt(colonne1[0]));
                                p.setDataRitiro(LocalDate.parse(colonne1[1]));
                                // p.setUtente(utenteService.getUtente(colonne1[2]));
                                p.setGestito(Boolean.parseBoolean(colonne1[4]));
                                Utente utente = new Utente();
                                try {
                                    FileData fileData2 = Utility.readAllRows(utenteFileName);
                                    for (String[] colonne2 : fileData2.getRighe()) {
                                        if (colonne2[2].equals(colonne1[2])) {
                                            utente.setNome(colonne2[0]);
                                            utente.setCognome(colonne2[1]);
                                            utente.setEmail(colonne2[2]);
                                            utente.setPassword(colonne2[3]);
                                            utente.setCodiceFiscale(colonne2[4]);
                                            utente.setTelefono(colonne2[5]);
                                            utente.setDataNascita(LocalDate.parse(colonne2[6]));
                                            utente.setSesso(Sesso.valueOf(colonne2[7]));
                                            p.setUtente(utente);
                                            break;
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    throw new BusinessException(e);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new BusinessException(e);
                    }
                    noleggio.setCosto(Double.parseDouble(colonne[5]));
                    noleggio.setVeicolo(veicoloService.getVeicolo(colonne[6]));
                    p.setNoleggio(noleggio);
                    noleggio.setPrenotazione(p);
                    result.add(noleggio);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (PrenotazioneNotFoundException e) {
            e.printStackTrace();
        } catch (VeicoloNotFoundException e) {
            e.printStackTrace();
            throw new BusinessException(e);

        }

        return result;
    }

    @Override
    public List<Noleggio> visualizzaNoleggi() throws BusinessException {
        List<Noleggio> result = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(noleggioFileName);
            for (String[] colonne : fileData.getRighe()) {
                Noleggio noleggio = new Noleggio();
                noleggio.setId(Integer.parseInt(colonne[0]));
                noleggio.setChilometraggioPrevisto(Integer.parseInt(colonne[1]));
                noleggio.setDataRiconsegna(LocalDate.parse(colonne[2]));
                noleggio.setPagamentoEffettuato(Boolean.parseBoolean(colonne[3]));
                Prenotazione p = new Prenotazione();
                try {
                    FileData fileData1 = Utility.readAllRows(prenotazioneFileName);
                    for (String[] colonne1 : fileData1.getRighe()) {
                        if (colonne1[3].equals(colonne[0])) {
                            p.setId(Integer.parseInt(colonne1[0]));
                            p.setDataRitiro(LocalDate.parse(colonne1[1]));
                            // p.setUtente(utenteService.getUtente(colonne1[2]));
                            p.setGestito(Boolean.parseBoolean(colonne1[4]));
                            Utente utente = new Utente();
                            try {
                                FileData fileData2 = Utility.readAllRows(utenteFileName);
                                for (String[] colonne2 : fileData2.getRighe()) {
                                    if (colonne2[2].equals(colonne1[2])) {
                                        utente.setNome(colonne2[0]);
                                        utente.setCognome(colonne2[1]);
                                        utente.setEmail(colonne2[2]);
                                        utente.setPassword(colonne2[3]);
                                        utente.setCodiceFiscale(colonne2[4]);
                                        utente.setTelefono(colonne2[5]);
                                        utente.setDataNascita(LocalDate.parse(colonne2[6]));
                                        utente.setSesso(Sesso.valueOf(colonne2[7]));
                                        p.setUtente(utente);
                                        break;
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                throw new BusinessException(e);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BusinessException(e);
                }

                noleggio.setCosto(Double.parseDouble(colonne[5]));
                noleggio.setVeicolo(veicoloService.getVeicolo(colonne[6]));
                result.add(noleggio);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (VeicoloNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Noleggio> visualizzaNoleggiAssistiti() throws BusinessException {
        List<Noleggio> result = new ArrayList<>();
        Intervento intervento = new Intervento();
        try {
            FileData fileData = Utility.readAllRows(interventoFileName);
            for (String[] colonne : fileData.getRighe()) {
                // 1, 2001-3-1 10:00, 2021-2-2, ciao, 2, 4, false, false
                intervento.setId(Integer.parseInt(colonne[0]));
                if (!colonne[1].equals("-"))
                    intervento.setDataIntervento(LocalDateTime.parse(colonne[1]));
                intervento.setDataRichiestaIntervento(LocalDate.parse(colonne[2]));
                intervento.setMessaggio(colonne[3]);
                Utente utente = new Utente();
                try {
                    FileData fileData1 = Utility.readAllRows(utenteFileName);
                    for (String[] righe : fileData1.getRighe()) {
                        if (righe[2].equals(colonne[4])) {
                            utente.setNome(righe[0]);
                            utente.setCognome(righe[1]);
                            utente.setEmail(righe[2]);
                            utente.setPassword(righe[3]);
                            utente.setCodiceFiscale(righe[4]);
                            utente.setTelefono(righe[5]);
                            utente.setDataNascita(LocalDate.parse(righe[6]));
                            utente.setSesso(Sesso.valueOf(righe[7]));
                            intervento.setUtente(utente);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BusinessException(e);
                }
                intervento.setNoleggio(getNoleggio(Integer.parseInt(colonne[5])));
                intervento.setSostituito(Boolean.parseBoolean(colonne[6]));
                intervento.setGestito(Boolean.parseBoolean(colonne[7]));
                if (result.isEmpty())
                    result.add(intervento.getNoleggio());
                else
                    for (Noleggio n : result) {
                        if (n.getId() != intervento.getNoleggio().getId())
                            result.add(intervento.getNoleggio());
                    }

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (NoleggioNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Noleggio> visualizzaNoleggiInRiconsegna(LocalDate data) throws BusinessException {
        List<Noleggio> noleggiInRiconsegna = new ArrayList<>();
        Noleggio noleggio = new Noleggio();
        try {
            FileData fileData = Utility.readAllRows(noleggioFileName);
            for (String[] colonne : fileData.getRighe()) {
                if (data.equals(LocalDate.parse(colonne[2]))) {
                    // 1, 200, 2021-7-20, false, 3, 200.0, ch380
                    noleggio.setId(Integer.parseInt(colonne[0]));
                    noleggio.setChilometraggioPrevisto(Integer.parseInt(colonne[1]));
                    noleggio.setDataRiconsegna(LocalDate.parse(colonne[2]));
                    noleggio.setPagamentoEffettuato(Boolean.parseBoolean(colonne[3]));
                    // noleggio.setPrenotazione(FileNoleggioVeicoliUnivaqBusinessFactoryImpl.getPrenotazioneServiceStatic().getPrenotazione(Integer.parseInt(colonne[4])));
                    noleggio.setCosto(Double.parseDouble(colonne[5]));
                    noleggio.setVeicolo(veicoloService.getVeicolo(colonne[6]));
                    Prenotazione p = new Prenotazione();
                    try {
                        FileData fileData1 = Utility.readAllRows(prenotazioneFileName);
                        for (String[] colonne1 : fileData1.getRighe()) {
                            if (colonne1[3].equals(colonne[0])) {
                                p.setId(Integer.parseInt(colonne1[0]));
                                p.setDataRitiro(LocalDate.parse(colonne1[1]));
                                // p.setUtente(utenteService.getUtente(colonne1[2]));
                                // p.setNoleggio(noleggio);
                                p.setGestito(Boolean.parseBoolean(colonne1[4]));
                                Utente utente = new Utente();
                                try {
                                    FileData fileData2 = Utility.readAllRows(utenteFileName);
                                    for (String[] colonne2 : fileData2.getRighe()) {
                                        if (colonne2[2].equals(colonne1[2])) {
                                            utente.setNome(colonne2[0]);
                                            utente.setCognome(colonne2[1]);
                                            utente.setEmail(colonne2[2]);
                                            utente.setPassword(colonne2[3]);
                                            utente.setCodiceFiscale(colonne2[4]);
                                            utente.setTelefono(colonne2[5]);
                                            utente.setDataNascita(LocalDate.parse(colonne2[6]));
                                            utente.setSesso(Sesso.valueOf(colonne2[7]));
                                            p.setUtente(utente);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    throw new BusinessException(e);
                                }
                                noleggio.setPrenotazione(p);
                                p.setNoleggio(noleggio);

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new BusinessException(e);
                    }
                    noleggiInRiconsegna.add(noleggio);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (VeicoloNotFoundException e1) {
            e1.printStackTrace();
            throw new BusinessException(e1);
        }

        return noleggiInRiconsegna;
    }

    @Override
    public Noleggio getNoleggio(Integer id) throws BusinessException, NoleggioNotFoundException {
        Noleggio noleggio = new Noleggio();
        try {
            FileData fileData = Utility.readAllRows(noleggioFileName);
            for (String[] colonne : fileData.getRighe()) {
                if (Long.parseLong(colonne[0]) == id) {
                    // 1, 200, 2021-7-20, false, 3, 200.0, ch380
                    noleggio.setId(Integer.parseInt(colonne[0]));
                    noleggio.setChilometraggioPrevisto(Integer.parseInt(colonne[1]));
                    noleggio.setDataRiconsegna(LocalDate.parse(colonne[2]));
                    noleggio.setPagamentoEffettuato(Boolean.parseBoolean(colonne[3]));
                    // noleggio.setPrenotazione(FileNoleggioVeicoliUnivaqBusinessFactoryImpl.getPrenotazioneServiceStatic().getPrenotazione(Integer.parseInt(colonne[4])));
                    noleggio.setCosto(Double.parseDouble(colonne[5]));
                    noleggio.setVeicolo(veicoloService.getVeicolo(colonne[6]));
                    Prenotazione p = new Prenotazione();
                    try {
                        FileData fileData1 = Utility.readAllRows(prenotazioneFileName);
                        for (String[] colonne1 : fileData1.getRighe()) {
                            if (colonne1[3].equals(colonne[0])) {
                                p.setId(Integer.parseInt(colonne1[0]));
                                p.setDataRitiro(LocalDate.parse(colonne1[1]));
                                // p.setUtente(utenteService.getUtente(colonne1[2]));
                                // p.setNoleggio(noleggio);
                                p.setGestito(Boolean.parseBoolean(colonne1[4]));
                                Utente utente = new Utente();
                                try {
                                    FileData fileData2 = Utility.readAllRows(utenteFileName);
                                    for (String[] colonne2 : fileData2.getRighe()) {
                                        if (colonne2[2].equals(colonne1[2])) {
                                            utente.setNome(colonne2[0]);
                                            utente.setCognome(colonne2[1]);
                                            utente.setEmail(colonne2[2]);
                                            utente.setPassword(colonne2[3]);
                                            utente.setCodiceFiscale(colonne2[4]);
                                            utente.setTelefono(colonne2[5]);
                                            utente.setDataNascita(LocalDate.parse(colonne2[6]));
                                            utente.setSesso(Sesso.valueOf(colonne2[7]));
                                            p.setUtente(utente);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    throw new BusinessException(e);
                                }
                                noleggio.setPrenotazione(p);
                                p.setNoleggio(noleggio);

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new BusinessException(e);
                    }
                    return noleggio;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (VeicoloNotFoundException e) {
            e.printStackTrace();
        }
        throw new NoleggioNotFoundException();
    }

    @Override
    public void gestioneRiconsegna(Noleggio noleggio, Integer km, Double costo) throws BusinessException {
        noleggio.setPagamentoEffettuato(true);
        noleggio.setCosto(costo);
        modificaNoleggio(noleggio);
        Veicolo v = noleggio.getVeicolo();
        v.setStato(Stato.DISPONIBILE);
        v.setChilometriPercorsi(km);
        modificaVeicolo(v);

        inviaNotifica(new Notifica(noleggio.getDataRiconsegna(), "Lascia un feedback sul veicolo!",
                noleggio.getPrenotazione().getUtente()));

    }

    //Metodo preso da FileVeicoloServiceImpl
    private void modificaVeicolo(Veicolo v) throws BusinessException {
        try {
            FileData fileData = Utility.readAllRows(veicoloFileName);
            try (PrintWriter writer = new PrintWriter(new File(veicoloFileName))) {
                writer.println(fileData.getContatore());
                for (String[] righe : fileData.getRighe()) {
                    if (righe[0].equals(v.getTarga())) {
                        StringBuilder row = new StringBuilder();
                        row.append(v.getTarga());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getMarca());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getModello());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getColore());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getPotenza().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getCilindrata().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getChilometriPercorsi().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getNumeroPorte().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getNumeroPosti().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getConsumo().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getStato().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getAlimentazione().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getCambio().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(v.getTipologia().getId().toString());
                        writer.println(row.toString());
                    } else {
                        writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }
    //Metodo creato per la gestione della riconsegna di un veicolo
    //Serve principalmente per impostare il costo e il pagamento del noleggio
    private void modificaNoleggio(Noleggio noleggio) throws BusinessException {
        try {
            FileData fileData = Utility.readAllRows(noleggioFileName);
            try (PrintWriter writer = new PrintWriter(new File(noleggioFileName))) {
                writer.println(fileData.getContatore());
                for (String[] righe : fileData.getRighe()) {
                    if (righe[0].equals(noleggio.getId().toString())) {
                        StringBuilder row = new StringBuilder();
                        row.append(noleggio.getId().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(noleggio.getChilometraggioPrevisto().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(noleggio.getDataRiconsegna().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(noleggio.isPagamentoEffettuato());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(noleggio.getPrenotazione().getId());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(noleggio.getCosto().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(noleggio.getVeicolo().getTarga());
                        writer.println(row.toString());
                    } else {
                        writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    //Metodo preso da FilePrenotazioneServiceImpl
    private List<Prenotazione> visualizzaPrenotazioni() throws BusinessException {
        List<Prenotazione> prenotazioni = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(prenotazioneFileName);
            for (String[] colonne : fileData.getRighe()) {
                // 1, 2021-7-12, 1, 3, false
                Prenotazione prenotazione = new Prenotazione();
                prenotazione.setId(Integer.parseInt(colonne[0]));
                prenotazione.setDataRitiro(LocalDate.parse(colonne[1]));
                prenotazione.setUtente(utenteService.getUtente(colonne[2]));
                prenotazione.setNoleggio(getNoleggio(Integer.parseInt(colonne[3])));
                prenotazione.setGestito(Boolean.parseBoolean(colonne[4]));
                prenotazioni.add(prenotazione);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (NoleggioNotFoundException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return prenotazioni;
    }

    private void setGestito(Prenotazione prenotazione) throws BusinessException {
        try {
            FileData fileData = Utility.readAllRows(prenotazioneFileName);
            try (PrintWriter writer = new PrintWriter(new File(prenotazioneFileName))) {
                long contatore = fileData.getContatore();
                writer.println((contatore));
                for (String[] righe : fileData.getRighe()) {
                    if (prenotazione.getId().toString().equals(righe[0])) {
                        StringBuilder row = new StringBuilder();
                        row.append(righe[0]);
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(righe[1]);
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(righe[2]);
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(righe[3]);
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append("true");
                        writer.println(row.toString());
                    } else
                        writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }
    //Metodo preso da FileNotificaServiceImpl
    private void inviaNotifica(Notifica notifica) throws BusinessException {
        try {
            FileData fileData = Utility.readAllRows(notificaFileName);
            try (PrintWriter writer = new PrintWriter(new File(notificaFileName))) {
                long contatore = fileData.getContatore();
                writer.println((contatore + 1));
                for (Prenotazione p : visualizzaPrenotazioni()) {
                    if (p.getUtente().getEmail().equals(notifica.getUtente().getEmail())) {
                        if (!(p.getDataRitiro().isBefore(LocalDate.now()))) {
                            setGestito(p);
                        }
                    }
                }

                for (String[] righe : fileData.getRighe()) {
                    writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
                }
                StringBuilder row = new StringBuilder();
                row.append(contatore);
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(notifica.getDataCreazione().toString());
                row.append(Utility.SEPARATORE_COLONNA);
                if (notifica.getData() != null) {
                    row.append(notifica.getData().toString());
                    row.append(Utility.SEPARATORE_COLONNA);
                }
                row.append(notifica.getMessaggio());
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(notifica.getUtente().getEmail());
                writer.println(row.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }
}
