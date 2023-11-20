/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.util;

import rc.so.domain.ProgettiFormativi;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 *
 * @author smo
 */
public class MakeTarGz {

    public static void createTarArchive(List<ProgettiFormativi> p_list, String file_path_name) throws IOException, ParseException {
//        File f = new File(file_path_name);
//        FileOutputStream fos = new FileOutputStream(f);
//        GZIPOutputStream gos = new GZIPOutputStream(new BufferedOutputStream(fos));
//        final TarArchiveOutputStream tarOs = new TarArchiveOutputStream(gos);
//
//        List<Allievi> allievi = new ArrayList<>();
//
//        p_list.forEach(p -> {
//            p.getDocumenti().stream()
//                    .filter(d -> d.getTipo().getEstrazione() == 1 && d.getDeleted() == 0)
//                    .forEach(d -> {
//                        try {
//                            addFilesToTarGZ(d.getPath(), p.getCip() + "/" + (d.getDocente() != null ? "Docenti/" + d.getDocente().getCognome() + "/" : ""), tarOs);
//                        } catch (IOException ex) {
//                            ex.printStackTrace();
//                        }
//                    });
//            p.getAllievi().stream()
//                    .filter(a -> a.getStatopartecipazione().getId().equals("01")).forEach(a -> {
//                allievi.add(a);//aggiungo allievi per estrazione excel
//                a.getDocumenti().stream()
//                        .filter(d -> d.getTipo().getEstrazione() == 1 && d.getDeleted() == 0)
//                        .forEach(d -> {
//                            try {
//                                addFilesToTarGZ(d.getPath(), p.getCip() + "/Allievi/" + a.getCodicefiscale() + "/", tarOs);
//                            } catch (IOException ex) {
//                                ex.printStackTrace();
//                            }
//                        });
//            });
//        });
//
//        String excel = ExportExcel.createExcelAllievi(allievi);
//        addFilesToTarGZ(excel, "", tarOs);
//        new File(excel).deleteOnExit();
//        tarOs.close();
//        gos.close();
//        fos.flush();
//        fos.close();
//        new File(excel).delete();
////        return f;
    }

    public static ByteArrayOutputStream createTarArchive(List<ProgettiFormativi> p_list) {
//        try {
//            ByteArrayOutputStream fos = new ByteArrayOutputStream();
//            GZIPOutputStream gos = new GZIPOutputStream(new BufferedOutputStream(fos));
//            final TarArchiveOutputStream tarOs = new TarArchiveOutputStream(gos);
//
//            List<Allievi> allievi = new ArrayList<>();
//
//            p_list.forEach(p -> {
//                p.getDocumenti().stream()
//                        .filter(d -> d.getTipo().getEstrazione() == 1 && d.getDeleted() == 0)
//                        .forEach(d -> {
//                            try {
//                                addFilesToTarGZ(d.getPath(), p.getCip() + "/" + (d.getDocente() != null ? "Docenti/" + d.getDocente().getCognome() + "/" : ""), tarOs);
//                            } catch (IOException ex) {
//                                ex.printStackTrace();
//                            }
//                        });
//                p.getAllievi().stream()
//                        .filter(a -> a.getStatopartecipazione().getId().equals("01")).forEach(a -> {
//                    allievi.add(a);//aggiungo allievi per estrazione excel
//                    a.getDocumenti().stream()
//                            .filter(d -> d.getTipo().getEstrazione() == 1 && d.getDeleted() == 0)
//                            .forEach(d -> {
//                                try {
//                                    addFilesToTarGZ(d.getPath(), p.getCip() + "/Allievi/" + a.getCodicefiscale() + "/", tarOs);
//                                } catch (IOException ex) {
//                                    ex.printStackTrace();
//                                }
//                            });
//                });
//            });
//
//            String excel = ExportExcel.createExcelAllievi(allievi);
//            addFilesToTarGZ(excel, "", tarOs);
//            new File(excel).deleteOnExit();
//            tarOs.close();
//            gos.close();
//            new File(excel).delete();
//            return fos;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return null;
    }

}
