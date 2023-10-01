/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.util;

import static rc.so.db.Action.insertTR;
import rc.so.db.Database;
import static rc.so.util.Utility.estraiEccezione;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rcosco
 */
public class FaseB {

    public String host;

    public FaseB(boolean test, boolean neet) {
        if (neet) {
            this.host = "clustermicrocredito.cluster-c6m6yfqeypv3.eu-south-1.rds.amazonaws.com:3306/enm_gestione_neet_prod";
            if (test) {
                this.host = "clustermicrocredito.cluster-c6m6yfqeypv3.eu-south-1.rds.amazonaws.com:3306/enm_gestione_neet";
            }
        } else {
            this.host = "clustermicrocredito.cluster-c6m6yfqeypv3.eu-south-1.rds.amazonaws.com:3306/enm_gestione_dd_prod";
            if (test) {
                this.host = "clustermicrocredito.cluster-c6m6yfqeypv3.eu-south-1.rds.amazonaws.com:3306/enm_gestione_dd";
            }
        }
    }

    public List<Lezione> calcolaegeneraregistrofaseb(int idpr, String host, boolean printing, boolean save, boolean today) {
        List<Lezione> calendar = new ArrayList<>();
        try {
            List<Lezione> calendartemp = new ArrayList<>();

            Database db1 = new Database(false);

            String sql1 = "SELECT lc.lezione,lm.giorno,lm.orario_start,lm.orario_end,lm.id_docente,ud.codice,lc.ore,lm.gruppo_faseB FROM lezioni_modelli lm, modelli_progetti mp, lezione_calendario lc, unita_didattiche ud "
                    + " WHERE mp.id_modello=lm.id_modelli_progetto AND lc.id_lezionecalendario=lm.id_lezionecalendario AND ud.codice=lc.codice_ud "
                    + " AND mp.id_progettoformativo=" + idpr + " AND ud.fase = 'Fase B' ORDER BY lm.gruppo_faseB,lc.lezione,lm.orario_start";
            
            
            try (Statement st1 = db1.getC().createStatement(); ResultSet rs1 = st1.executeQuery(sql1)) {

                while (rs1.next()) {
                    calendartemp.add(new Lezione(rs1.getInt("lc.lezione"),
                            rs1.getInt("lm.id_docente"),
                            rs1.getString("lm.giorno"), rs1.getString("lm.orario_start"), rs1.getString("lm.orario_end"),
                            rs1.getString("ud.codice"), rs1.getString("lc.ore"), rs1.getInt("lm.gruppo_faseB"), ""));
                }

            }
            db1.closeDB();

            for (int i = 0; i < calendartemp.size(); i++) {
                Lezione cal = calendartemp.get(i);
                Lezione cal2;
                try {
                    cal2 = calendartemp.get(i + 1);
                } catch (Exception e) {
                    cal2 = null;
                }
                Lezione cal3;
                try {
                    cal3 = calendartemp.get(i - 1);
                } catch (Exception e) {
                    cal3 = null;
                }

                boolean hasnext = cal2 != null;
                if (hasnext) {
                    if (cal.getGiorno().equals(cal2.getGiorno()) && cal.getGruppo() == cal2.getGruppo()) {
                        List<Integer> doc1 = new ArrayList<>();
                        doc1.addAll(cal.getDocente());
                        cal2.getDocente().forEach(d1 -> {
                            if (!doc1.contains(d1)) {
                                doc1.add(d1);
                            }
                        });
                        double ore = Double.parseDouble(cal.getOre()) + Double.parseDouble(cal2.getOre());
                        calendar.add(new Lezione(cal.getId(), doc1,
                                cal.getGiorno(), cal.getStart(), cal2.getEnd(),
                                cal.getCodiceud() + "_" + cal2.getCodiceud(),
                                Utility.doubleformat.format(ore), cal.getGruppo(), cal.getNomestanza()));

                    } else {
                        if (cal3 == null || !cal3.getGiorno().equals(cal.getGiorno())) {
                            calendar.add(cal);
                        }
                    }
                } else {
                    if (i == 0) {
                        calendar.add(cal);
                    } else {
                        if (cal.getGiorno().equals(cal3.getGiorno()) && cal.getGruppo() == cal3.getGruppo()) {

                        } else {
                            calendar.add(cal);
                        }
                    }
                }

            }

        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
        return calendar;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
