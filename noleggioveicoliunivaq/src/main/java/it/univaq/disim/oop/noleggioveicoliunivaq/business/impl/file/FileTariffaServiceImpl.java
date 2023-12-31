package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.CategoriaNonAggiungibileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TariffaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloNotFoundException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.VeicoloService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Categoria;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tariffa;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Veicolo;

public class FileTariffaServiceImpl implements TariffaService {

    private String tariffaFileName;
    private VeicoloService veicoloService;

    public FileTariffaServiceImpl(String tariffaFileName, VeicoloService veicoloService) {
        this.tariffaFileName = tariffaFileName;
        this.veicoloService = veicoloService;
    }

    @Override
    public List<Tariffa> visualizzaTariffe(Veicolo v) throws BusinessException {
        List<Tariffa> result = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(tariffaFileName);
            for (String[] colonne : fileData.getRighe()) {
                if (colonne[3].equals(v.getTarga())) {
                    // id, categoria, veicolo
                    Tariffa tariffa = new Tariffa();
                    tariffa.setId(Integer.parseInt(colonne[0]));
                    tariffa.setCategoria(Categoria.valueOf(colonne[1]));
                    tariffa.setPrezzo(Double.parseDouble(colonne[2]));
                    tariffa.setVeicolo(veicoloService.getVeicolo(colonne[3]));
                    result.add(tariffa);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (VeicoloNotFoundException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return result;
    }

    @Override
    public Object getIdCounter() throws BusinessException {
        FileData fileData;
        try {
            fileData = Utility.readAllRows(tariffaFileName);
            long contatore = fileData.getContatore();
            return contatore;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException();
        }

    }

    @Override
    public void aggiungiTariffa(Tariffa t, Veicolo veicolo)
            throws BusinessException, CategoriaNonAggiungibileException {
        try {
            FileData fileData = Utility.readAllRows(tariffaFileName);
            for (String[] righe : fileData.getRighe()) {
                if (righe[3].equals(veicolo.getTarga()))
                    if (righe[1].equals(t.getCategoria().toString()))
                        throw new CategoriaNonAggiungibileException();
            }
            try (PrintWriter writer = new PrintWriter(new File(tariffaFileName))) {
                long contatore = fileData.getContatore();
                writer.println((contatore + 1));
                // id, categoria,prezzo, veicolo
                for (String[] righe : fileData.getRighe()) {
                    writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
                }

                StringBuilder row = new StringBuilder();
                row.append(contatore);
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(t.getCategoria().toString());
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(t.getPrezzo().toString());
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(veicolo.getTarga());
                writer.println(row.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    @Override
    public void eliminaTariffa(Tariffa t) throws BusinessException {
        try {
            FileData fileData = Utility.readAllRows(tariffaFileName);
            try (PrintWriter writer = new PrintWriter(new File(tariffaFileName))) {
                writer.println(fileData.getContatore());
                for (String[] righe : fileData.getRighe()) {
                    if (!(righe[0].equals(t.getId().toString()))) {
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
    public void modificaTariffa(Tariffa t) throws BusinessException, CategoriaNonAggiungibileException {
        try {
            FileData fileData = Utility.readAllRows(tariffaFileName);
            try (PrintWriter writer = new PrintWriter(new File(tariffaFileName))) {
                writer.println(fileData.getContatore());
                long contatore = fileData.getContatore();
                for (String[] righe : fileData.getRighe()) {
                    if ((righe[0].equals(t.getId().toString()))) {
                        StringBuilder row = new StringBuilder();
                        row.append(contatore);
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(t.getCategoria().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(t.getPrezzo().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(t.getVeicolo().getTarga());
                        writer.println(row.toString());
                    }
                    writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
    }

}