
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import rc.so.db.Database;
import rc.so.db.Entity;
import rc.so.domain.Comuni;
import static rc.so.util.Utility.cp_toUTF;

//import rc.so.db.Entity;
/**
 *
 * @author smo
 */
public class Example {

    public static boolean copyR(File source, File dest) {
        boolean es;
        try {
            long byteing = source.length();
            try (OutputStream out = new FileOutputStream(dest)) {
                long contenuto = FileUtils.copyFile(source, out);
                es = byteing == contenuto;
            }
        } catch (Exception e) {
            es = false;
        }
        return es;
    }

    public static ArrayList<daticomune> read1() {
        ArrayList<daticomune> comuni = new ArrayList<>();
        try {
            String path = "C:\\Users\\Administrator\\Desktop\\da caricare\\Codici-statistici-e-denominazioni-al-18_11_2023.xlsx";

            //Create Workbook instance holding reference to .xlsx file
            try (FileInputStream file = new FileInputStream(path)) {
                //Create Workbook instance holding reference to .xlsx file
                XSSFWorkbook wb = new XSSFWorkbook(file);
                //Get first/desired sheet from the workbook
                XSSFSheet ws = wb.getSheetAt(0);
                //Iterate through each rows one by one
                for (Row row : ws) {
                    Iterator<Cell> cellIterator = row.cellIterator();
                    daticomune dc = new daticomune();
                    while (cellIterator.hasNext()) {
                        String value = "";
                        Cell cell = cellIterator.next();
                        switch (cell.getCellType()) {
                            case NUMERIC:
                                value = String.valueOf(cell.getNumericCellValue());
                                break;
                            case STRING:
                                value = cell.getStringCellValue().trim();
                                break;
                            case BLANK:
                                break;
                            default:
                                break;
                        }

                        switch (cell.getColumnIndex()) {
                            case 0:
                                dc.setCodiceregione(value);
                                break;
                            case 1:
                                dc.setIdprovincia(value);
                                break;
                            case 2:
                                dc.setNomecomune(value);
                                break;
                            case 3:
                                dc.setNomeregione(value);
                                break;
                            case 4:
                                dc.setNomeprovincia(value);
                                break;
                            case 5:
                                dc.setCodiceprovincia(value);
                                break;
                            case 6:
                                dc.setIstat(value);
                                break;
                        }
                    }
                    comuni.add(dc);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return comuni;
    }

    public static ArrayList<datigpi> read2() {
        ArrayList<datigpi> comuni = new ArrayList<>();
        try {
            String path = "C:\\Users\\Administrator\\Desktop\\da caricare\\Tabella comuni e stati esteri 20231214.xlsx";

            //Create Workbook instance holding reference to .xlsx file
            try (FileInputStream file = new FileInputStream(path)) {
                //Create Workbook instance holding reference to .xlsx file
                XSSFWorkbook wb = new XSSFWorkbook(file);
                //Get first/desired sheet from the workbook
                XSSFSheet ws = wb.getSheetAt(0);
                //Iterate through each rows one by one
                for (Row row : ws) {
                    Iterator<Cell> cellIterator = row.cellIterator();
                    datigpi dc = new datigpi();
                    while (cellIterator.hasNext()) {
                        String value = "";
                        Cell cell = cellIterator.next();
                        switch (cell.getCellType()) {
                            case NUMERIC:
                                value = String.valueOf(cell.getNumericCellValue());
                                break;
                            case STRING:
                                value = cell.getStringCellValue().trim();
                                break;
                            case BLANK:
                                break;
                            default:
                                break;
                        }

                        switch (cell.getColumnIndex()) {
                            case 3:
                                dc.setNomecomune(value);
                                break;
                            case 2:
                                dc.setIstat(value);
                                break;
                            case 4:
                                dc.setIdprovincia(value);
                                break;
                        }
                    }
                    comuni.add(dc);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return comuni;
    }

    public static void main(String[] args) {

        try {

            ArrayList<String> presenti = new ArrayList<>();
            ArrayList<datinazioni> nazioni = new ArrayList<>();

            Database db1 = new Database(false);

            String sql1 = "SELECT istat FROM comuni";

            try (Statement st1 = db1.getC().createStatement(); ResultSet rs1 = st1.executeQuery(sql1)) {
                while (rs1.next()) {
                    presenti.add(rs1.getString(1).trim());
                }
            }

            String sql2 = "SELECT codicefiscale,ue,nome FROM nazioni_rc";

            try (Statement st1 = db1.getC().createStatement(); ResultSet rs1 = st1.executeQuery(sql2)) {
                while (rs1.next()) {
                    nazioni.add(new datinazioni(rs1.getString(1).trim(), rs1.getString(2).trim(), rs1.getString(2).trim()));
                }
            }

            db1.closeDB();

//            ArrayList<daticomune> listaistat = read1();
            ArrayList<datigpi> listagpi = read2();
            Entity en = new Entity();
            en.begin();
            for (datigpi c1 : listagpi) {

                if (!presenti.contains(c1.getIstat())) {
                    if (c1.getIdprovincia() != null) {
//                        daticomune d1 = listaistat.stream().filter(l1 -> l1.getIdprovincia() != null && l1.getIdprovincia().equals(c1.getIdprovincia())).findAny().orElse(null);
//                        if (d1 == null) {
//                            System.out.println("NON TROVATO: " + c1.toString());
//                        } else {
//                            Comuni c1_new = new Comuni();
//                            c1_new.setCittadinanza(0);
//                            c1_new.setCod_comune("0");
//                            c1_new.setCod_provincia(d1.getCodiceprovincia().toUpperCase());
//                            c1_new.setProvincia(d1.getCodiceprovincia().toUpperCase());
//                            c1_new.setCod_regione(d1.getCodiceregione());
//                            c1_new.setIstat(c1.getIstat().toUpperCase());
//                            c1_new.setNome(cp_toUTF(c1.getNomecomune().toUpperCase()));
//                            c1_new.setNome_provincia(cp_toUTF(d1.getNomeprovincia().toUpperCase()));
//                            c1_new.setRegione(cp_toUTF(d1.getNomeregione().toUpperCase()));
//                            en.persist(c1_new);
//                        }
                    } else {

                        datinazioni dn = nazioni.stream().filter(l1 -> l1.getIstat().equals(c1.getIstat())).findAny().orElse(null);
                        if (dn != null) {
//                            Comuni c1_new = new Comuni();
//                            c1_new.setCittadinanza(1);
//                            c1_new.setCod_comune("0");
//                            c1_new.setCod_provincia(dn.getUe().toUpperCase());
//                            c1_new.setProvincia(dn.getUe().toUpperCase());
//                            c1_new.setCod_regione("0");
//                            c1_new.setIstat(c1.getIstat().toUpperCase());
//                            c1_new.setNome(cp_toUTF(c1.getNomecomune().toUpperCase()));
//                            c1_new.setNome_provincia(cp_toUTF(c1.getNomecomune().toUpperCase()));
//                            c1_new.setRegione(cp_toUTF(c1.getNomecomune().toUpperCase()));
//                            en.persist(c1_new);
                        } else {
                            
                            Comuni c1_new = new Comuni();
                            c1_new.setCittadinanza(1);
                            c1_new.setCod_comune("0");
                            c1_new.setCod_provincia("NON UE");
                            c1_new.setProvincia("NON UE");
                            c1_new.setCod_regione("0");
                            c1_new.setIstat(c1.getIstat().toUpperCase());
                            c1_new.setNome(cp_toUTF(c1.getNomecomune().toUpperCase()));
                            c1_new.setNome_provincia(cp_toUTF(c1.getNomecomune().toUpperCase()));
                            c1_new.setRegione(cp_toUTF(c1.getNomecomune().toUpperCase()));
                            en.persist(c1_new);
                            
                            
//                            System.out.println(c1.toString());
                        }

                    }
                }

            }
            en.commit();
            en.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        Entity en = new Entity();
//        en.begin();
//        Database db1 = new Database(true);
//        SoggettiAttuatori sa = db1.estrai_SA_accettare(en, "500");
//        List<Docenti> docenti = db1.estrai_DOCENTI_SA(sa, en);
//        docenti.forEach(d1 -> {
//            en.persist(d1);
//        });
//        db1.closeDB();
//        en.commit();
//        en.close();
//        System.out.println(conversionText("PADOVA"));
//        
//        
//        System.out.println("rc.so.util.Utility.main() " + conversionText("ISTITUTO TECNICO\n"
//                + "COMMERCIALE E. VANONI,\n"
//                + "NARDÃ (LE)"));
//            File ing = new File("C:\\Users\\rcosco\\Downloads\\HistoricalStockPrice_2021 (1).pdf");
//            File out = new File("C:\\Users\\rcosco\\Downloads\\COPIED.pdf");
//
//            boolean es = copyR(ing, out);
//
//            System.out.println(es);
//        try {
//            //SALVARE TUTTO
//            //INVIA MAIL REGISTRAZIONE
//            Entity en = new Entity();
//            en.begin();
//            String mailsender = en.getPath("mailsender");
//            String linkweb = en.getPath("dominio");
//            String linknohttpweb = remove(linkweb, "https://");
//            linknohttpweb = removeEnd(linknohttpweb, "/");
//            Email email = (Email) en.getEmail("registration");
//            String email_txt = email.getTesto()
//                    .replace("@username", "testusername")
//                    .replace("@password", "testpassword!!!")
//                    .replace("@linkweb", linkweb)
//                    .replace("@linknohttpweb", linknohttpweb);
//
//            String[] dest_mail = {"MONICA.POMPILI@MICROCREDITO.GOV.IT"};
//            String[] dest_cc = {"raffaele.cosco@faultless.it"};
//
//            SendMailJet.sendMail(mailsender, dest_mail, dest_cc, email_txt, email.getOggetto());
//            en.close();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

//    public static void main(String[] args) {
//        System.out.println(roundFloatAndFormat(100000L, true));
//    }
//    public static void main(String[] args) {
//        try {
//            SendMailJet.sendMail("testing", new String[]{"raffaele.cosco@faultless.it"}, "TESTO", "testing oggetto");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//    public static void main(String[] args) {
//        Entity e = new Entity();
//        ProgettiFormativi prg = e.getEm().find(ProgettiFormativi.class, 3L);
////            prg.setImporto(Double.valueOf(prezzo.replaceAll("[._]", "").replace(",", ".").trim()));
////            e.merge(prg);
////            e.commit();
//
//        //14-10-2020 MODIFICA - TOGLIERE IMPORTO CHECKLIST
//        double ore_convalidate = 0;
//        for (DocumentiPrg d : prg.getDocumenti().stream().filter(p -> p.getGiorno() != null).collect(Collectors.toList())) {
//            ore_convalidate += d.getOre_convalidate();
//        }
//        for (Allievi a : prg.getAllievi()) {
//            for (Documenti_Allievi d : a.getDocumenti().stream().filter(p -> p.getGiorno() != null).collect(Collectors.toList())) {
//                try {
//                    ore_convalidate += d.getOrericonosciute() == null ? 0 : d.getOrericonosciute();
//                } catch (Exception ex) {
//                    System.out.println(d.getId() + " " + d.getAllievo().getCognome() + " " + d.getGiorno() + " " + d.getOrericonosciute());
//                }
//            }
//        }
//        e.close();
//
//        System.out.println(ore_convalidate);
//    }
}

class datinazioni {

    String istat, ue, nome;

    public datinazioni(String istat, String ue, String nome) {
        this.istat = istat;
        this.ue = ue;
        this.nome = nome;
    }

    public datinazioni() {
    }

    public String getIstat() {
        return istat;
    }

    public void setIstat(String istat) {
        this.istat = istat;
    }

    public String getUe() {
        return ue;
    }

    public void setUe(String ue) {
        this.ue = ue;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}

class datigpi {

    String istat, nomecomune, idprovincia;

    public datigpi() {
    }

    public String getIstat() {
        return istat;
    }

    public void setIstat(String istat) {
        this.istat = istat;
    }

    public String getNomecomune() {
        return nomecomune;
    }

    public void setNomecomune(String nomecomune) {
        this.nomecomune = nomecomune;
    }

    public String getIdprovincia() {
        return idprovincia;
    }

    public void setIdprovincia(String idprovincia) {
        this.idprovincia = idprovincia;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("datigpi{");
        sb.append("istat=").append(istat);
        sb.append(", nomecomune=").append(nomecomune);
        sb.append(", idprovincia=").append(idprovincia);
        sb.append('}');
        return sb.toString();
    }

}

class daticomune {

    String codiceregione, idprovincia, nomecomune, nomeregione, nomeprovincia, codiceprovincia, istat;

    public daticomune() {
    }

    public String getCodiceregione() {
        return codiceregione;
    }

    public void setCodiceregione(String codiceregione) {
        this.codiceregione = codiceregione;
    }

    public String getIdprovincia() {
        return idprovincia;
    }

    public void setIdprovincia(String idprovincia) {
        this.idprovincia = idprovincia;
    }

    public String getNomecomune() {
        return nomecomune;
    }

    public void setNomecomune(String nomecomune) {
        this.nomecomune = nomecomune;
    }

    public String getNomeregione() {
        return nomeregione;
    }

    public void setNomeregione(String nomeregione) {
        this.nomeregione = nomeregione;
    }

    public String getNomeprovincia() {
        return nomeprovincia;
    }

    public void setNomeprovincia(String nomeprovincia) {
        this.nomeprovincia = nomeprovincia;
    }

    public String getCodiceprovincia() {
        return codiceprovincia;
    }

    public void setCodiceprovincia(String codiceprovincia) {
        this.codiceprovincia = codiceprovincia;
    }

    public String getIstat() {
        return istat;
    }

    public void setIstat(String istat) {
        this.istat = istat;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("daticomune{");
        sb.append("codiceregione=").append(codiceregione);
        sb.append(", idprovincia=").append(idprovincia);
        sb.append(", nomecomune=").append(nomecomune);
        sb.append(", nomeregione=").append(nomeregione);
        sb.append(", nomeprovincia=").append(nomeprovincia);
        sb.append(", codiceprovincia=").append(codiceprovincia);
        sb.append(", istat=").append(istat);
        sb.append('}');
        return sb.toString();
    }

}
