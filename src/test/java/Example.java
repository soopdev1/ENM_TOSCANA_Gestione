
import static rc.so.util.Utility.conversionText;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;

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

    public static void main(String[] args) {
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
