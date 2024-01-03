
import rc.so.db.Action;
import rc.so.db.Entity;
import rc.so.domain.MascheraM5;
import rc.so.domain.ProgettiFormativi;
import rc.so.domain.TipoDoc_Allievi;
import rc.so.util.Pdf_new;
import rc.so.util.Utility;
import java.io.File;
import java.util.Map;
import org.joda.time.DateTime;
import rc.so.domain.Allievi;
import rc.so.domain.Lezioni_Modelli;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rcosco
 */
public class GeneraDoc {

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger(
        "org.apache").setLevel(java.util.logging.Level.SEVERE);

//        String idpr = "614";
//        String idall = "2469";
//        String usernameSA = "FLCOMMERCIALISTI";

        Entity e = new Entity();
        e.begin();
//        ProgettiFormativi prg = e.getEm().find(ProgettiFormativi.class,
//                Long.valueOf(idpr));
//        Allievi al = e.getEm().find(Allievi.class, 
//                Long.valueOf(idall));
    
            Lezioni_Modelli lm = e.getEm().find(Lezioni_Modelli.class, 2L);
            File f1 = Pdf_new.REGISTROCARTACEO(e, "rcc", lm, new DateTime());
//        ModelliPrg m3 = Utility.filterModello3(prg.getModelli());
//        ModelliPrg m4 = Utility.filterModello4(prg.getModelli());
//        File f1 = Pdf_new.MODELLO0(e, "30", al);
        System.out.println(f1.getPath());
//        File f2 = Pdf_new.MODELLO2(e,
//                            "1",
//                            usernameSA, prg.getSoggetto(),
//                            prg,
//                            prg.getAllievi().stream().filter(a1-> a1.getStatopartecipazione().getId().equals("01")).collect(Collectors.toList()) , new DateTime(), true);
//        
//        System.out.println(f2.getPath());

//        File f3 = Pdf_new.MODELLO3(e,
//                            usernameSA,
//                            prg.getSoggetto(),
//                            prg,
//                            prg.getAllievi().stream().filter(p1 -> p1.getStatopartecipazione().getId().equals("01")).collect(Collectors.toList()),
//                            prg.getDocenti(), m3.getLezioni(), prg.getStaff_modelli().stream().filter(m -> m.getAttivo() == 1).collect(Collectors.toList()),
//                            new DateTime(), true);        
//        System.out.println(f3.getPath());
//        Collections.sort(m4.getLezioni(), (emp1, emp2) -> emp1.getGiorno().compareTo(emp2.getGiorno()));
//        File f4 = Pdf_new.MODELLO4(e, usernameSA, prg.getSoggetto(), prg, prg.getAllievi().stream().filter(p1
//                -> p1.getStatopartecipazione().getId().equals("01")).collect(Collectors.toList()),
//                prg.getDocenti(),
//                m4.getLezioni(),
//                prg.getStaff_modelli().stream().filter(m
//                -> m.getAttivo() == 1).collect(Collectors.toList()), new DateTime(), true);
//
//        System.out.println(f4.getPath());
//        Map<Long, Long> allievi_m5 = Utility.allieviM5_loaded(e.getM5Loaded_byPF(prg));
//        prg.getAllievi().stream().filter(al1 -> al1.getStatopartecipazione().getId().equals("01")).forEach(al -> {
//
//            MascheraM5 m5 = e.getEm().find(MascheraM5.class, allievi_m5.get(al.getId()));
//
//            if (m5 != null) {
//                TipoDoc_Allievi tipodoc_m5;
//                if (m5.isTabella_premialita()) {
//                    tipodoc_m5 = e.getEm().find(TipoDoc_Allievi.class, 21L);
//                } else {
//                    tipodoc_m5 = e.getEm().find(TipoDoc_Allievi.class, 20L);
//                }
//
//                String[] datifrequenza = Action.dati_modello5_neet(
//                        String.valueOf(al.getId()),
//                        String.valueOf(prg.getSoggetto().getId()),
//                        String.valueOf(m5.getProgetto_formativo().getId()));
//
//                File f5 = Pdf_new.MODELLO5(e,
//                        tipodoc_m5.getModello(),
//                        usernameSA,
//                        prg.getSoggetto(),
//                        al,
//                        datifrequenza,
//                        m5,
//                        new DateTime(), true);
//                System.out.println(f5.getPath());
//
//            }
//
//        });
//        ModelliPrg m6 = Utility.filterModello6(prg.getModelli());
//        if (m6 != null) {
//            File f6 = Pdf_new.MODELLO6(e,
//                    usernameSA,
//                    prg.getSoggetto(),
//                    prg, m6, new DateTime(), true);
//
//            System.out.println(f6.getPath());
//        }
//        Map<Long, Long> oreRendicontabili = Action.OreRendicontabiliAlunni((int) (long) prg.getId());
//        File f7 = Pdf_new.MODELLO7(e, usernameSA, al, Utility.roundFloatAndFormat(oreRendicontabili.get(al.getId()), true),
//                new DateTime(), true);
//        System.out.println(f7.getPath());
        e.close();
//        
//        String o = Pdf_new.checkFirmaQRpdfA("MODELLO1", "", new File("C:\\Users\\Administrator\\Desktop\\da caricare\\INFO05_MOISE_CLAUDIASILVIA_041120211144476.M1_pdfA.pdf"), "", "20;0;60;60");
//        System.out.println(o);
    }

}
