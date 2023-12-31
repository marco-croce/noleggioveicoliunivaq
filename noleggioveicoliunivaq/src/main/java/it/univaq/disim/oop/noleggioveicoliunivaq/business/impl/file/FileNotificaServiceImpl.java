package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NotificaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.PrenotazioneService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.UtenteService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Notifica;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Persona;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Prenotazione;

public class FileNotificaServiceImpl implements NotificaService {

    private String notificaFileName;
    private UtenteService utenteService;
    private PrenotazioneService prenotazioneService;
    private String prenotazioneFileName;

    public FileNotificaServiceImpl(String notificaFileName, UtenteService utenteService,
            PrenotazioneService prenotazioneService, String prenotazioneFileName) {
        this.notificaFileName = notificaFileName;
        this.utenteService = utenteService;
        this.prenotazioneService = prenotazioneService;
        this.prenotazioneFileName = prenotazioneFileName;
    }

    @Override
    //Salva la notifica sul file
    public void inviaNotifica(Notifica notifica) throws BusinessException {
        try {
            FileData fileData = Utility.readAllRows(notificaFileName);
            try (PrintWriter writer = new PrintWriter(new File(notificaFileName))) {
                long contatore = fileData.getContatore();
                writer.println((contatore + 1));
                for (Prenotazione p : prenotazioneService.visualizzaPrenotazioni()) {
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

    @Override
    //Serve per vedere i dettagli di una notifica
    public Notifica visualizzaNotifica(Notifica notifica) throws BusinessException {
        Notifica n = new Notifica();
        try {
            FileData fileData = Utility.readAllRows(notificaFileName);
            for (String[] colonne : fileData.getRighe()) {
                // 1, 2021-7-12, 1, 3, false
                if (colonne[0].equals(notifica.getId().toString())) {
                    n.setId(Integer.parseInt(colonne[0]));
                    n.setDataCreazione(LocalDate.parse(colonne[1]));
                    n.setData(LocalDateTime.parse(colonne[2]));
                    n.setMessaggio(colonne[3]);
                    n.setUtente(utenteService.getUtente(colonne[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return n;
    }

    @Override
    //Permette a una persona le proprie notifiche
    public List<Notifica> visualizzaNotifiche(Persona persona) throws BusinessException {
        List<Notifica> notifiche = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(notificaFileName);
            for (String[] colonne : fileData.getRighe()) {
                // 1, 2021-7-12, 1, 3, false
                if (colonne.length == 5) {
                    if (colonne[4].equals(persona.getEmail())) {
                        Notifica n = new Notifica();
                        n.setId(Integer.parseInt(colonne[0]));
                        n.setDataCreazione(LocalDate.parse(colonne[1]));
                        n.setData(LocalDateTime.parse(colonne[2]));
                        n.setMessaggio(colonne[3]);
                        n.setUtente(utenteService.getUtente(colonne[4]));
                        notifiche.add(n);
                    }
                } else {
                    if (colonne[3].equals(persona.getEmail())) {
                        Notifica n = new Notifica();
                        n.setId(Integer.parseInt(colonne[0]));
                        n.setDataCreazione(LocalDate.parse(colonne[1]));
                        n.setMessaggio(colonne[2]);
                        n.setUtente(utenteService.getUtente(colonne[3]));
                        notifiche.add(n);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return notifiche;
    }

    @Override
    public List<Notifica> visualizzaNotifiche() throws BusinessException {
        List<Notifica> notifiche = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(notificaFileName);
            for (String[] colonne : fileData.getRighe()) {
                // 1, 2021-7-12, 1, 3, false
                Notifica n = new Notifica();
                n.setId(Integer.parseInt(colonne[0]));
                n.setDataCreazione(LocalDate.parse(colonne[1]));
                n.setData(LocalDateTime.parse(colonne[2]));
                n.setMessaggio(colonne[3]);
                n.setUtente(utenteService.getUtente(colonne[4]));
                notifiche.add(n);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return notifiche;
    }
    
    public void setGestito(Prenotazione prenotazione) throws BusinessException {
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

    @Override
    public void modificaNotifica(Notifica notifica) throws BusinessException {
        try {
            FileData fileData = Utility.readAllRows(notificaFileName);
            try (PrintWriter writer = new PrintWriter(new File(notificaFileName))) {
                writer.println(fileData.getContatore());
                for (String[] righe : fileData.getRighe()) {
                    if (righe[0].equals(notifica.getId().toString())) {
                        StringBuilder row = new StringBuilder();
                        row.append(notifica.getId().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(notifica.getDataCreazione().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        if (notifica.getData() != null) {
                            row.append(notifica.getData().toString());
                            row.append(Utility.SEPARATORE_COLONNA);
                        }
                        row.append("Grazie per aver lasciato il feedback sul veicolo!");
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(notifica.getUtente().getEmail());
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

}
