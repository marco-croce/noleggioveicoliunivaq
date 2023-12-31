package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import it.univaq.disim.oop.noleggioveicoliunivaq.business.BusinessException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaEsistenteException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaNonEliminabileException;
import it.univaq.disim.oop.noleggioveicoliunivaq.business.TipologiaService;
import it.univaq.disim.oop.noleggioveicoliunivaq.domain.Tipologia;

public class FileTipologiaServiceImpl implements TipologiaService {

    private String tipologiaFileName;
    private String veicoloFileName;

    public FileTipologiaServiceImpl(String tipologiaFileName, String veicoloFileName) {
        this.tipologiaFileName = tipologiaFileName;
        this.veicoloFileName = veicoloFileName;
    }

    @Override
    public List<Tipologia> visualizzaTipologie() throws BusinessException {
        List<Tipologia> result = new ArrayList<>();
        try {
            FileData fileData = Utility.readAllRows(tipologiaFileName);
            for (String[] colonne : fileData.getRighe()) {
                // id, nome
                Tipologia tipologia = new Tipologia();
                tipologia.setId(Integer.parseInt(colonne[0]));
                tipologia.setNome(colonne[1]);
                result.add(tipologia);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

        return result;
    }

    public Tipologia getTipologiaById(Integer id) throws BusinessException {
        Tipologia result = new Tipologia();
        try {
            FileData fileData = Utility.readAllRows(tipologiaFileName);
            for (String[] colonne : fileData.getRighe()) {
                if (id.toString().equals(colonne[0])) {
                    Tipologia tipologia = new Tipologia();
                    tipologia.setId(Integer.parseInt(colonne[0]));
                    tipologia.setNome(colonne[1]);
                    result = tipologia;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

        return result;
    }

    @Override
    public void aggiungiTipologia(Tipologia t) throws BusinessException, TipologiaEsistenteException {
        try {
            FileData fileData = Utility.readAllRows(tipologiaFileName);
            for (String[] righe : fileData.getRighe()) {
                if (righe[1].equals(t.getNome()))
                    throw new TipologiaEsistenteException();
            }
            try (PrintWriter writer = new PrintWriter(new File(tipologiaFileName))) {
                long contatore = fileData.getContatore();
                writer.println((contatore + 1));
                for (String[] righe : fileData.getRighe()) {
                    writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
                }
                StringBuilder row = new StringBuilder();
                row.append(contatore);
                row.append(Utility.SEPARATORE_COLONNA);
                row.append(t.getNome());
                writer.println(row.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    @Override
    public void eliminaTipologia(Tipologia t) throws BusinessException, TipologiaNonEliminabileException {
        try {
            FileData fileData = Utility.readAllRows(tipologiaFileName);
            try (PrintWriter writer = new PrintWriter(new File(tipologiaFileName))) {
                long contatore = fileData.getContatore();
                writer.println((contatore));
                FileData fileDataVeicolo = Utility.readAllRows(veicoloFileName);
                for (String[] colonneVeicolo : fileDataVeicolo.getRighe()) {
                    if (colonneVeicolo[13].equals(t.getId().toString()))
                        throw new TipologiaNonEliminabileException();
                    else {
                        for (String[] righe : fileData.getRighe()) {
                            writer.println(String.join(Utility.SEPARATORE_COLONNA, righe));
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }

    }

    @Override
    public void modificaTipologia(Tipologia t) throws BusinessException {
        try {
            FileData fileData = Utility.readAllRows(tipologiaFileName);
            try (PrintWriter writer = new PrintWriter(new File(tipologiaFileName))) {
                writer.println(fileData.getContatore());
                for (String[] righe : fileData.getRighe()) {
                    if (Long.parseLong(righe[0]) == t.getId()) {
                        StringBuilder row = new StringBuilder();
                        row.append(t.getId().toString());
                        row.append(Utility.SEPARATORE_COLONNA);
                        row.append(t.getNome());
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
    public Integer getIdCounter() {
        FileData fileData;
        try {
            fileData = Utility.readAllRows(tipologiaFileName);
            return (int) fileData.getContatore();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
