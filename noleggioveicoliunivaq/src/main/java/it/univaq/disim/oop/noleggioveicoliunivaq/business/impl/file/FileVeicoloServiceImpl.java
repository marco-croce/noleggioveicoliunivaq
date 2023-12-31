package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.NoleggioService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNonEliminabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Alimentazione;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Cambio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Noleggio;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Stato;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tipologia;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class FileVeicoloServiceImpl implements VeicoloService {

    private String veicoloFileName;
    private String tipologiaFileName;
    private TipologiaService tipologiaService;
    private NoleggioService noleggioService;

    public FileVeicoloServiceImpl(String veicoloFileName, String tipologiaFileName, TipologiaService tipologiaService,
            NoleggioService noleggioService) {
        this.veicoloFileName = veicoloFileName;
        this.tipologiaFileName = tipologiaFileName;
        this.tipologiaService = tipologiaService;
        this.noleggioService = noleggioService;
    }

    @Override
    public List<Veicolo> visualizzaVeicoli() throws BusinessException {
        List<Veicolo> result = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(veicoloFileName);
            for (String[] colonne : fileData.getRighe()) {
                Veicolo veicolo = new Veicolo();
                veicolo.setTarga(colonne[0]);
                veicolo.setMarca(colonne[1]);
                veicolo.setModello(colonne[2]);
                veicolo.setColore(colonne[3]);
                veicolo.setPotenza(Integer.parseInt(colonne[4]));
                veicolo.setCilindrata(Integer.parseInt(colonne[5]));
                veicolo.setChilometriPercorsi(Integer.parseInt(colonne[6]));
                veicolo.setNumeroPorte(Integer.parseInt(colonne[7]));
                veicolo.setNumeroPosti(Integer.parseInt(colonne[8]));
                veicolo.setConsumo(Double.parseDouble(colonne[9]));
                veicolo.setStato(Stato.valueOf(colonne[10]));
                veicolo.setAlimentazione(Alimentazione.valueOf(colonne[11]));
                veicolo.setCambio(Cambio.valueOf(colonne[12]));
                veicolo.setTipologia(tipologiaService.getTipologiaById(Integer.parseInt(colonne[13])));
                result.add(veicolo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

        return result;
    }

    @Override
    public Integer getNumeroVeicoli(Tipologia t) throws BusinessException {
        int cont = 0;
        try {
            for (Veicolo v : this.visualizzaVeicoli()) {
                if (v.getTipologia().getId().equals(t.getId()))
                    cont++;
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return cont;
    }

    @Override
    public List<String> visualizzaNomiTipologie() throws BusinessException {
        List<String> result = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(tipologiaFileName);
            for (String[] colonne : fileData.getRighe()) {
                result.add(colonne[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

        return result;
    }

    @Override
    public List<Veicolo> visualizzaVeicoli(Stato stato) throws BusinessException {
        List<Veicolo> result = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(veicoloFileName);
            for (String[] colonne : fileData.getRighe()) {
                if (colonne[10].equals(stato.toString())) {
                    // targa,marca,modello,colore,potenza,cilindrata,chilometri,numeroPorte,numeroPosti,consumo,stato,alimentazione,cambio,tipologia
                    Veicolo veicolo = new Veicolo();
                    veicolo.setTarga(colonne[0]);
                    veicolo.setMarca(colonne[1]);
                    veicolo.setModello(colonne[2]);
                    veicolo.setColore(colonne[3]);
                    veicolo.setPotenza(Integer.parseInt(colonne[4]));
                    veicolo.setCilindrata(Integer.parseInt(colonne[5]));
                    veicolo.setChilometriPercorsi(Integer.parseInt(colonne[6]));
                    veicolo.setNumeroPorte(Integer.parseInt(colonne[7]));
                    veicolo.setNumeroPosti(Integer.parseInt(colonne[8]));
                    veicolo.setConsumo(Double.parseDouble(colonne[9]));
                    veicolo.setStato(Stato.valueOf(colonne[10]));
                    veicolo.setAlimentazione(Alimentazione.valueOf(colonne[11]));
                    veicolo.setCambio(Cambio.valueOf(colonne[12]));
                    veicolo.setTipologia(tipologiaService.getTipologiaById(Integer.parseInt(colonne[13])));
                    result.add(veicolo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

        return result;
    }

    @Override
    public List<Veicolo> visualizzaVeicoli(Stato stato, Stato stato1) throws BusinessException {
        List<Veicolo> result = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(veicoloFileName);
            for (String[] colonne : fileData.getRighe()) {
                if (colonne[10].equals(stato.toString()) || colonne[10].equals(stato1.toString())) {
                    // targa,marca,modello,colore,potenza,cilindrata,chilometri,numeroPorte,numeroPosti,consumo,stato,alimentazione,cambio,tipologia
                    Veicolo veicolo = new Veicolo();
                    veicolo.setTarga(colonne[0]);
                    veicolo.setMarca(colonne[1]);
                    veicolo.setModello(colonne[2]);
                    veicolo.setColore(colonne[3]);
                    veicolo.setPotenza(Integer.parseInt(colonne[4]));
                    veicolo.setCilindrata(Integer.parseInt(colonne[5]));
                    veicolo.setChilometriPercorsi(Integer.parseInt(colonne[6]));
                    veicolo.setNumeroPorte(Integer.parseInt(colonne[7]));
                    veicolo.setNumeroPosti(Integer.parseInt(colonne[8]));
                    veicolo.setConsumo(Double.parseDouble(colonne[9]));
                    veicolo.setStato(Stato.valueOf(colonne[10]));
                    veicolo.setAlimentazione(Alimentazione.valueOf(colonne[11]));
                    veicolo.setCambio(Cambio.valueOf(colonne[12]));
                    veicolo.setTipologia(tipologiaService.getTipologiaById(Integer.parseInt(colonne[13])));
                    result.add(veicolo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

        return result;
    }

    @Override
    public List<Veicolo> visualizzaVeicoli(Alimentazione alimentazione) throws BusinessException {
        List<Veicolo> result = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(veicoloFileName);
            for (String[] colonne : fileData.getRighe()) {
                if (colonne[11].equals(alimentazione.toString())) {
                    // targa,marca,modello,colore,potenza,cilindrata,chilometri,numeroPorte,numeroPosti,consumo,stato,alimentazione,cambio,tipologia
                    Veicolo veicolo = new Veicolo();
                    veicolo.setTarga(colonne[0]);
                    veicolo.setMarca(colonne[1]);
                    veicolo.setModello(colonne[2]);
                    veicolo.setColore(colonne[3]);
                    veicolo.setPotenza(Integer.parseInt(colonne[4]));
                    veicolo.setCilindrata(Integer.parseInt(colonne[5]));
                    veicolo.setChilometriPercorsi(Integer.parseInt(colonne[6]));
                    veicolo.setNumeroPorte(Integer.parseInt(colonne[7]));
                    veicolo.setNumeroPosti(Integer.parseInt(colonne[8]));
                    veicolo.setConsumo(Double.parseDouble(colonne[9]));
                    veicolo.setStato(Stato.valueOf(colonne[10]));
                    veicolo.setAlimentazione(Alimentazione.valueOf(colonne[11]));
                    veicolo.setCambio(Cambio.valueOf(colonne[12]));
                    veicolo.setTipologia(tipologiaService.getTipologiaById(Integer.parseInt(colonne[13])));
                    result.add(veicolo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return result;
    }

    @Override
    public List<Veicolo> visualizzaVeicoli(Tipologia tipologia) throws BusinessException {
        List<Veicolo> veicoli = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(veicoloFileName);
            for (String[] righe : fileData.getRighe()) {
                if (righe[13].equals(tipologia.getId().toString())) {
                    Veicolo veicolo = new Veicolo();
                    veicolo.setTarga(righe[0]);
                    veicolo.setMarca(righe[1]);
                    veicolo.setModello(righe[2]);
                    veicolo.setColore(righe[3]);
                    veicolo.setPotenza(Integer.parseInt(righe[4]));
                    veicolo.setCilindrata(Integer.parseInt(righe[5]));
                    veicolo.setChilometriPercorsi(Integer.parseInt(righe[6]));
                    veicolo.setNumeroPorte(Integer.parseInt(righe[7]));
                    veicolo.setNumeroPosti(Integer.parseInt(righe[8]));
                    veicolo.setConsumo(Double.parseDouble(righe[9]));
                    veicolo.setStato(Stato.valueOf(righe[10]));
                    veicolo.setAlimentazione(Alimentazione.valueOf(righe[11]));
                    veicolo.setCambio(Cambio.valueOf(righe[12]));
                    veicolo.setTipologia(tipologiaService.getTipologiaById(Integer.parseInt(righe[13])));
                    veicoli.add(veicolo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return veicoli;
    }

    @Override
    public List<Veicolo> visualizzaVeicoli(LocalDate dataInizio, LocalDate dataFine) throws BusinessException {
        List<Noleggio> listaNoleggi = new ArrayList<>(noleggioService.visualizzaNoleggi());
        Set<Veicolo> listaVeicoli = new HashSet<>(visualizzaVeicoli());
        for (Noleggio n : listaNoleggi) {
            if (n.getPrenotazione().getDataRitiro().isBefore(dataFine) || n.getDataRiconsegna().isAfter(dataInizio)) {
                listaVeicoli.remove(n.getVeicolo());
            }

        }
        return new ArrayList<Veicolo>(listaVeicoli);
    }

    @Override
    public void aggiungiVeicolo(Veicolo v) throws BusinessException, VeicoloEsistenteException {
        try {
            FileData fileData = Utility.readAllRows(veicoloFileName);
            try (PrintWriter writer = new PrintWriter(new File(veicoloFileName))) {
                long contatore = fileData.getContatore();
                writer.println((contatore + 1));
                for (String[] righe : fileData.getRighe()) {
                    if (righe[0].equals(v.getTarga()))
                        throw new VeicoloEsistenteException();
                    writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
                }

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
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    @Override
    public void eliminaVeicolo(Veicolo v) throws BusinessException, VeicoloNonEliminabileException {
        if (!(v.getStato().equals(Stato.DISPONIBILE)))
            throw new VeicoloNonEliminabileException();

        v.getTipologia().getVeicoli().remove(v);

        try {
            FileData fileData = Utility.readAllRows(veicoloFileName);
            try (PrintWriter writer = new PrintWriter(new File(veicoloFileName))) {
                writer.println(fileData.getContatore());
                for (String[] righe : fileData.getRighe()) {
                    if (!(righe[0].equals(v.getTarga())))
                        writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

    @Override
    public void modificaVeicolo(Veicolo v) throws BusinessException {
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

    @Override
    public List<Veicolo> visualizzaVeicoli(double min, double max) throws BusinessException {
        List<Veicolo> veicoli = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(veicoloFileName);
            for (String[] righe : fileData.getRighe()) {
                if (Double.parseDouble(righe[9]) >= min && Double.parseDouble(righe[9]) <= max) {
                    Veicolo veicolo = new Veicolo();
                    veicolo.setTarga(righe[0]);
                    veicolo.setMarca(righe[1]);
                    veicolo.setModello(righe[2]);
                    veicolo.setColore(righe[3]);
                    veicolo.setPotenza(Integer.parseInt(righe[4]));
                    veicolo.setCilindrata(Integer.parseInt(righe[5]));
                    veicolo.setChilometriPercorsi(Integer.parseInt(righe[6]));
                    veicolo.setNumeroPorte(Integer.parseInt(righe[7]));
                    veicolo.setNumeroPosti(Integer.parseInt(righe[8]));
                    veicolo.setConsumo(Double.parseDouble(righe[9]));
                    veicolo.setStato(Stato.valueOf(righe[10]));
                    veicolo.setAlimentazione(Alimentazione.valueOf(righe[11]));
                    veicolo.setCambio(Cambio.valueOf(righe[12]));
                    veicolo.setTipologia(tipologiaService.getTipologiaById(Integer.parseInt(righe[13])));
                    veicoli.add(veicolo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

        return veicoli;
    }

    @Override
    public Veicolo getVeicolo(String targa) throws BusinessException, VeicoloNotFoundException {
        Veicolo veicolo = new Veicolo();
        try {
            FileData fileData = Utility.readAllRows(veicoloFileName);
            for (String[] righe : fileData.getRighe()) {
                if (righe[0].equals(targa))
                    veicolo.setTarga(righe[0]);
                veicolo.setMarca(righe[1]);
                veicolo.setModello(righe[2]);
                veicolo.setColore(righe[3]);
                veicolo.setPotenza(Integer.parseInt(righe[4]));
                veicolo.setCilindrata(Integer.parseInt(righe[5]));
                veicolo.setChilometriPercorsi(Integer.parseInt(righe[6]));
                veicolo.setNumeroPorte(Integer.parseInt(righe[7]));
                veicolo.setNumeroPosti(Integer.parseInt(righe[8]));
                veicolo.setConsumo(Double.parseDouble(righe[9]));
                veicolo.setStato(Stato.valueOf(righe[10]));
                veicolo.setAlimentazione(Alimentazione.valueOf(righe[11]));
                veicolo.setCambio(Cambio.valueOf(righe[12]));
                veicolo.setTipologia(tipologiaService.getTipologiaById(Integer.parseInt(righe[13])));
                return veicolo;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        throw new VeicoloNotFoundException();
    }
}
