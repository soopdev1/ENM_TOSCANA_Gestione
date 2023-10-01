
import rc.so.db.Entity;
import rc.so.domain.ModelliPrg;
import rc.so.domain.ProgettiFormativi;
//import rc.so.domain.ProgettiFormativi;
import rc.so.util.Pdf_new;
import static rc.so.util.Pdf_new.checkFirmaQRpdfA;
import static rc.so.util.Utility.filterModello6;
import java.io.File;
import org.joda.time.DateTime;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rcosco
 */
public class Pdf {

    public static void main(String[] args) {
        
//        File downloadFile = null;
//        try {
//            Entity e = new Entity();
//            ProgettiFormativi pf = e.getEm().find(ProgettiFormativi.class,
//                    110L);
//            
//            System.out.println("Pdf.main() "+pf.getModelli());
//            
//            ModelliPrg m6 = filterModello6(pf.getModelli());
//            if (m6 != null) {
//                downloadFile = Pdf_new.MODELLO6(e,
//                        "AMMINISTRAZIONE",
//                        pf.getSoggetto(),
//                        pf, m6, new DateTime(), true);
//                System.out.println("Pdf.main() "+downloadFile.getPath());
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        
        
//        Entity e = new Entity();
//
//        ProgettiFormativi pf = e.getEm().find(ProgettiFormativi.class, Long.parseLong("53"));

//            List<OreId> list = Arrays.asList(new ObjectMapper().readValue(pf.getChecklist_finale().getTab_neet_fa(), OreId[].class));
//            Map<String, String> map = new ObjectMapper().readValue(pf.getChecklist_finale().getTab_neet_fa(), Map.class);
        //          System.out.println(list);
//        File out1 = Pdf_new.ESITOVALUTAZIONE_BASE(e, "R", pf.getSoggetto(), pf, new DateTime(), true);
//        System.out.println(out1.getPath());

    }

//    public static void main(String[] args) {
//
//        Entity e = new Entity();
//
//        ProgettiFormativi p = e.getEm().find(ProgettiFormativi.class, Long.parseLong("82"));
//        List<StaffModelli> staff = p.getStaff_modelli().stream().filter(m -> m.getAttivo() == 1).collect(Collectors.toList());
//        ModelliPrg m4 = Utility.filterModello4(p.getModelli());
//        List<Lezioni_Modelli> lezioni = m4.getLezioni();
//
//        File f1 = MODELLO4_BASE(e,
//                "ASSEIMPRENDITORI", p.getSoggetto(),
//                p,
//                p.getAllievi().stream().filter(al1 -> al1.getGruppo_faseB() > 0).collect(Collectors.toList()),
//                p.getDocenti(),
//                lezioni,
//                staff,
//                new DateTime(), true);
//
//        System.out.println(f1.getPath());
//
//    }
//    public static void main(String[] args) {
//        
//        String o = checkFirmaQRpdfA("MODELLO2", "", new File("C:\\Users\\raf\\Desktop\\SEGRETERIASISTEMAIMPRESATA121020210858515.M2_pdfA.pdf.p7m"), "", "20;0;60;60");
//        System.out.println(o);
//    }
//                    
}
