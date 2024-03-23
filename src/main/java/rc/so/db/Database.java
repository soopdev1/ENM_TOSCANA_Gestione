/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.db;

import com.google.common.base.Splitter;
import rc.so.domain.Comuni;
import rc.so.domain.Docenti;
import rc.so.domain.FasceDocenti;
import rc.so.domain.Lezioni_Modelli;
import rc.so.domain.ProgettiFormativi;
import rc.so.domain.SediFormazione;
import rc.so.domain.SoggettiAttuatori;
import rc.so.entity.FadCalendar;
import rc.so.entity.Item;
import rc.so.util.Fadroom;
import rc.so.util.Utenti;
import rc.so.util.Utility;
import static rc.so.util.Utility.LOGAPP;
import static rc.so.util.Utility.calcoladurata;
import static rc.so.util.Utility.createDir;
import static rc.so.util.Utility.estraiEccezione;
import static rc.so.util.Utility.formatStringtoStringDate;
import static rc.so.util.Utility.getUtilDate;
import static rc.so.util.Utility.patternFile;
import static rc.so.util.Utility.patternITA;
import static rc.so.util.Utility.patternSql;
import static rc.so.util.Utility.preparefilefordownload;
import static rc.so.util.Utility.sdfSQL;
import static rc.so.util.Utility.test;
import static rc.so.util.Utility.timestampSQL;
import java.io.File;
import static java.lang.Class.forName;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringUtils.right;
import static org.apache.commons.lang3.StringUtils.stripAccents;
import org.joda.time.DateTime;
import rc.so.domain.Presenze_Lezioni_Allievi;
import static rc.so.util.Utility.calcolaintervallomillis;
import static rc.so.util.Utility.conf;

/**
 *
 * @author rcosco
 */
public class Database {

    public Connection c = null;

    public Database(boolean bando) {
        String driver = "com.mysql.cj.jdbc.Driver";
        String user = conf.getString("db.user");
        String password = conf.getString("db.pass");
        String host;
        if (bando) {
            if (test) {
                host = conf.getString("db.host") + ":3306/enm_toscana";
            } else {
                host = conf.getString("db.host") + ":3306/enm_toscana_prod";
            }
        } else {
            if (test) {
                host = conf.getString("db.host") + ":3306/enm_gestione_toscana";
            } else {
                host = conf.getString("db.host") + ":3306/enm_gestione_toscana_prod";
            }
        }

        try {
            forName(driver);
            Properties p = new Properties();
            p.put("user", user);
            p.put("password", password);
            p.put("characterEncoding", "UTF-8");
            p.put("passwordCharacterEncoding", "UTF-8");
            p.put("useSSL", "false");
            p.put("connectTimeout", "1000");
            p.put("useUnicode", "true");
            this.c = DriverManager.getConnection("jdbc:mysql://" + host, p);
//            boolean res1 = this.c != null && !this.c.isClosed();
//            LOGAPP.log(Level.INFO, "HOST: {0} - CONNESSO {1} - ISDBTEST: {2}", new Object[]{host, res1, test});

        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
            if (this.c != null) {
                try {
                    this.c.close();
                } catch (Exception ex1) {
                    LOGAPP.log(Level.SEVERE, estraiEccezione(ex1));
                }
            }
            this.c = null;
        }
    }

    public void closeDB() {
        try {
            if (c != null) {
                this.c.close();
            }
        } catch (SQLException ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
    }

    public Connection getC() {
        return c;
    }

    public void setC(Connection c) {
        this.c = c;
    }

    public String getNow() {
        try {
            String sql = "SELECT now()";
            PreparedStatement ps = this.c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception ex) {
        }
        return new DateTime().toString(timestampSQL);
    }

    public void insertTR(String type, String user, String descr) {
        try {
            PreparedStatement ps = this.c.prepareStatement("INSERT INTO tracking (azione,iduser,timestamp) VALUES (?,?,?)");
            ps.setString(1, descr);
            ps.setString(2, user);
            ps.setString(3, getNow());
            ps.execute();
        } catch (SQLException ex) {
        }
    }

    public int countPregresso() {
        int count = 0;
        String sql = "SELECT COUNT(idallievi_pregresso) FROM allievi_pregresso";
        try {
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            count = 0;
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return count;
    }

    public List<Fadroom> listStanzeOGGI() {
        List<Fadroom> out = new ArrayList<>();
        try {
            String sql = "SELECT room,idprogetti_formativi FROM fad_access WHERE DATA LIKE CONCAT(CURDATE(),'%') GROUP BY idprogetti_formativi,room";
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    out.add(new Fadroom(rs.getString(1), String.valueOf(rs.getInt(2)),
                            right(rs.getString(1), 1),
                            "0"));
                }
            }
        } catch (SQLException ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public List<Fadroom> listStanzeOGGISA(Long idsa) {
        List<Fadroom> out = new ArrayList<>();
        try {
            String sql = "SELECT room,idprogetti_formativi FROM fad_access "
                    + "WHERE data LIKE CONCAT(CURDATE(),'%') AND idprogetti_formativi IN "
                    + "(SELECT idprogetti_formativi FROM progetti_formativi WHERE (stato = 'ATA' OR stato = 'ATB') AND idsoggetti_attuatori = " + idsa + ")"
                    + " GROUP BY idprogetti_formativi,room";
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    out.add(new Fadroom(rs.getString(1), String.valueOf(rs.getInt(2)),
                            right(rs.getString(1), 1),
                            "0"));
                }
            }
        } catch (SQLException ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public List<Fadroom> listStanze() {
        List<Fadroom> out = new ArrayList<>();
        try {
            String sql = "SELECT * FROM fad_multi a WHERE stato='0'";
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    out.add(new Fadroom(rs.getString(1), String.valueOf(rs.getInt(2)), rs.getString(3), rs.getString(5)));
                }
            }
        } catch (SQLException ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public List<Item> listReport() {
        List<Item> out = new ArrayList<>();
        try {
            String sql = "SELECT * FROM fad_report";
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    out.add(new Item(rs.getString(1), rs.getString(2), ""));
                }
            }
        } catch (SQLException ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }

        return out;
    }

    public String getBase64Report(int idpr) {
        String out = null;
        try {
            String sql = "SELECT base64 FROM fad_report WHERE idprogetti_formativi = " + idpr;
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    out = rs.getString(1);
                }
            }
        } catch (SQLException ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
            out = null;
        }
        return out;
    }

    public List<Item> listStanza() {
        List<Item> out = new ArrayList<>();
        try {
            String sql = "SELECT idprogetti_formativi,nomestanza FROM fad a WHERE stato='0'";
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    out.add(new Item(String.valueOf(rs.getInt(1)), rs.getString(2), rs.getString(2)));
                }
            }
        } catch (SQLException ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public String getPathtemp(String id) {
        String p1 = "/mnt/mcn/test/temp/";
        try {
            String sql = "SELECT url FROM path WHERE id = ?";
            try (PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        p1 = rs.getString(1);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return p1;
    }

    public String getNomePR_F(String id) {
        String p1 = "Progetto Formativo";
        try {
            String sql = "SELECT descrizione FROM progetti_formativi WHERE idprogetti_formativi = ?";
            try (PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        p1 = rs.getString(1);
                    }
                }
            }
        } catch (SQLException ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return p1;
    }

    public List<FadCalendar> calendarioFAD(String id) {
        List<FadCalendar> out = new ArrayList<>();
        try {
            String sql = "SELECT * FROM fad_calendar f WHERE f.idprogetti_formativi = ? ORDER BY numerocorso,DATA";
            try (PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        if (rs.getString("orainizio").contains(";")) {
                            List<String> orainizio = Splitter.on(";").splitToList(rs.getString("orainizio"));
                            List<String> orafine = Splitter.on(";").splitToList(rs.getString("orafine"));
                            for (int x = 0; x < orainizio.size(); x++) {
                                out.add(new FadCalendar(
                                        id,
                                        rs.getString("numerocorso"),
                                        formatStringtoStringDate(rs.getString("data"), patternSql, patternITA, false),
                                        //                                rs.getString("data"),
                                        orainizio.get(x),
                                        orafine.get(x))
                                );
                            }
                        } else {
                            out.add(new FadCalendar(id, rs.getString("numerocorso"), formatStringtoStringDate(rs.getString("data"), patternSql, patternITA, false),
                                    rs.getString("orainizio"), rs.getString("orafine")));
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public boolean insertcalendarioFAD(String idpr, String corso, String data, String orainizio, String orafine) {
        boolean out;
        try {
            String sql = "SELECT * FROM fad_calendar WHERE idprogetti_formativi = ? AND numerocorso = ? AND data = ?";
            try (PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setString(1, idpr);
                ps.setString(2, corso);
                ps.setString(3, data);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String orainizio_old = rs.getString("orainizio");
                        String orafine_old = rs.getString("orafine");
                        String orainizio_new = orainizio_old + ";" + orainizio;
                        String orafine_new = orafine_old + ";" + orafine;
                        String update = "UPDATE fad_calendar SET orainizio = ?, orafine = ? "
                                + "WHERE idprogetti_formativi = ? AND numerocorso = ? AND data = ? AND orainizio = ? AND orafine = ?";
                        try (PreparedStatement ps1 = this.c.prepareStatement(update)) {
                            ps1.setString(1, orainizio_new);
                            ps1.setString(2, orafine_new);
                            ps1.setString(3, idpr);
                            ps1.setString(4, corso);
                            ps1.setString(5, data);
                            ps1.setString(6, orainizio_old);
                            ps1.setString(7, orafine_old);
                            ps1.executeUpdate();
                            out = true;
                        }

                    } else {
                        String del = "INSERT INTO fad_calendar VALUES (?,?,?,?,?)";
                        try (PreparedStatement ps1 = this.c.prepareStatement(del)) {
                            ps1.setString(1, idpr);
                            ps1.setString(2, corso);
                            ps1.setString(3, data);
                            ps1.setString(4, orainizio);
                            ps1.setString(5, orafine);
                            ps1.execute();
                            out = true;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
            out = false;
        }

        return out;
    }

    public boolean removecalendarioFAD(String idpr, String corso, String inizio, String data) {
        boolean out = false;
        try {
            String sql = "SELECT * FROM fad_calendar WHERE idprogetti_formativi = ? AND numerocorso = ? AND data = ? AND orainizio LIKE ?";
            try (PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setString(1, idpr);
                ps.setString(2, corso);
                ps.setString(3, data);
                ps.setString(4, "%" + inizio + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String orainizio = rs.getString("orainizio");
                        String orafine = rs.getString("orafine");
                        if (orainizio.contains(";")) {
                            LinkedList<String> orainizio_list = new LinkedList<>();
                            orainizio_list.addAll(Splitter.on(";").splitToList(orainizio));
                            LinkedList<String> orafine_list = new LinkedList<>();
                            orafine_list.addAll(Splitter.on(";").splitToList(orafine));
                            int indice = orainizio_list.indexOf(inizio);
                            if (indice >= 0) {
                                orainizio_list.remove(indice);
                                orafine_list.remove(indice);
                                String orainizio_new = String.join(";", orainizio_list);
                                String orafine_new = String.join(";", orafine_list);
                                String update = "UPDATE fad_calendar SET orainizio = ?, orafine = ? "
                                        + "WHERE idprogetti_formativi = ? AND numerocorso = ? AND data = ? AND orainizio = ? AND orafine = ?";
                                try (PreparedStatement ps1 = this.c.prepareStatement(update)) {
                                    ps1.setString(1, orainizio_new);
                                    ps1.setString(2, orafine_new);
                                    ps1.setString(3, idpr);
                                    ps1.setString(4, corso);
                                    ps1.setString(5, data);
                                    ps1.setString(6, orainizio);
                                    ps1.setString(7, orafine);
                                    ps1.executeUpdate();
                                    out = true;
                                }
                            }
                        } else {
                            String del = "DELETE FROM fad_calendar WHERE idprogetti_formativi = ? AND numerocorso = ? AND data = ? AND orainizio = ? AND orafine = ?";
                            try (PreparedStatement ps1 = this.c.prepareStatement(del)) {
                                ps1.setString(1, idpr);
                                ps1.setString(2, corso);
                                ps1.setString(3, data);
                                ps1.setString(4, orainizio);
                                ps1.setString(5, orafine);
                                ps1.execute();
                                out = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
            out = false;
        }
        return out;
    }

    public SoggettiAttuatori estrai_SA_accettare(Entity en, String id) {
        SoggettiAttuatori sa = null;
        try {
            String sql = "SELECT a.id,a.username,a.sedecap,a.cellulare,a.cf,a.cognome,a.data,a.datadecreto,a.mail,a.sedeindirizzo,a.nome,"
                    + "a.docric,a.pec,a.pivacf,a.protocollo,a.societa,a.scadenzadoc,a.cellulare,a.sedecomune,a.dataupconvenzionefinale,a.decreto,a.datadecreto,a.caricasoc"
                    + " FROM bando_toscana_mcn a WHERE id = " + id;
            try (PreparedStatement ps = this.c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sa = new SoggettiAttuatori();
                    sa.setId(rs.getLong("a.id"));
                    sa.setUsernameaccr(rs.getString("a.username"));
                    sa.setRagionesociale(rs.getString("a.societa"));
                    sa.setPiva(rs.getString("a.pivacf"));
                    sa.setCodicefiscale(rs.getString("a.cf"));
                    sa.setComune(en.getComune(Long.valueOf(rs.getString("a.sedecomune"))));
                    sa.setIndirizzo(rs.getString("a.sedeindirizzo"));
                    sa.setNome(rs.getString("a.nome"));
                    sa.setNome_refente(rs.getString("a.nome"));
                    sa.setCognome(rs.getString("a.cognome"));
                    sa.setCognome_referente(rs.getString("a.cognome"));
                    sa.setTelefono_sa(rs.getString("a.cellulare"));
                    sa.setTelefono_Ad(rs.getString("a.cellulare"));
                    sa.setTelefono_referente(rs.getString("a.cellulare"));
                    sa.setProtocollo(rs.getString("a.protocollo"));
                    sa.setDataprotocollo(getUtilDate(rs.getString("a.dataupconvenzionefinale"), patternITA));
                    sa.setVisual_dataprotocollo(rs.getString("a.datadecreto"));
                    //////////////////////////////////////////////////
                    sa.setEmail(rs.getString("a.mail"));
                    sa.setPec(rs.getString("a.pec"));
                    sa.setCell_sa(rs.getString("a.cellulare"));
                    sa.setCap(rs.getString("a.sedecap"));
                    sa.setDatanascita(getUtilDate(rs.getString("a.data"), patternITA));
                    sa.setScadenza(getUtilDate(rs.getString("a.scadenzadoc"), patternITA));
                    sa.setNro_documento(rs.getString("a.docric"));
                    sa.setDd(rs.getString("a.decreto") + " DEL " + rs.getString("a.datadecreto"));
                    sa.setCarica(rs.getString("a.caricasoc"));
                    return sa;
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return sa;
    }

    public List<SoggettiAttuatori> estrai_SA_accettare(Entity en) {
        List<SoggettiAttuatori> out = new LinkedList<>();

        try {
            String sql = "SELECT a.id,a.username,a.sedecap,a.cellulare,a.cf,a.cognome,a.`data`,a.datadecreto,a.mail,a.sedeindirizzo,a.nome,"
                    + "a.docric,a.pec,a.pivacf,a.protocollo,a.societa,a.scadenzadoc,a.cellulare,a.sedecomune"
                    + " FROM bando_toscana_mcn a WHERE stato_domanda='A' AND dataupconvenzionefinale<>'-'";
            try (PreparedStatement ps = this.c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SoggettiAttuatori sa = new SoggettiAttuatori();
                    sa.setId(rs.getLong("a.id"));
                    sa.setUsernameaccr(rs.getString("a.username"));
                    sa.setRagionesociale(rs.getString("a.societa"));
                    sa.setPiva(rs.getString("a.pivacf"));
                    sa.setCodicefiscale(rs.getString("a.cf"));
                    sa.setComune(en.getComune(Long.valueOf(rs.getString("a.sedecomune"))));
                    sa.setIndirizzo(rs.getString("a.sedeindirizzo"));
                    sa.setNome(rs.getString("a.nome"));
                    sa.setNome_refente(rs.getString("a.nome"));
                    sa.setCognome(rs.getString("a.cognome"));
                    sa.setCognome_referente(rs.getString("a.cognome"));
                    sa.setTelefono_sa(rs.getString("a.cellulare"));
                    sa.setTelefono_Ad(rs.getString("a.cellulare"));
                    sa.setTelefono_referente(rs.getString("a.cellulare"));
                    sa.setProtocollo(rs.getString("a.protocollo"));
                    sa.setDataprotocollo(getUtilDate(rs.getString("a.datadecreto"), patternITA));
                    sa.setVisual_dataprotocollo(rs.getString("a.datadecreto"));

                    out.add(sa);
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }

        return out;
    }

    public FileDownload getDocumentoIdentitaSA(String username) {
        FileDownload out = null;
        try {
            String sql = "SELECT path FROM docuserbandi WHERE username = '" + username + "' AND codicedoc = 'DOCR' AND stato='1' ORDER BY datacar DESC LIMIT 1";
            try (PreparedStatement ps = this.c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    out = preparefilefordownload(rs.getString(1));
                }
            }
        } catch (Exception ex) {
            out = null;
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;

    }

    public List<SediFormazione> estrai_SEDI_SA(SoggettiAttuatori sa, Entity en, String ragionesociale) {
        String username = sa.getUsernameaccr();
        List<SediFormazione> out = new LinkedList<>();
        try {

            String sql = "SELECT numaule,"
                    + "mailresponsabile1,indirizzo1,responsabile1,telresponsabile1,citta1,"
                    + "mailresponsabile2,indirizzo2,responsabile2,telresponsabile2,citta2,"
                    + "mailresponsabile3,indirizzo3,responsabile3,telresponsabile3,citta3,"
                    + "mailresponsabile4,indirizzo4,responsabile4,telresponsabile4,citta4,"
                    + "mailresponsabile5,indirizzo5,responsabile5,telresponsabile5,citta5 "
                    + "FROM allegato_a a WHERE username='" + username + "'";

            try (PreparedStatement ps = this.c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int numaule = Integer.parseInt(rs.getString("numaule"));
                    for (int i = 1; i <= numaule; i++) {
                        String denominazione = ragionesociale + " - SEDE " + i;
                        String via = rs.getString("indirizzo" + i);
                        String referente = rs.getString("responsabile" + i);
                        String telefono = rs.getString("telresponsabile" + i);
                        String cellulare = rs.getString("telresponsabile" + i);
                        String email = rs.getString("mailresponsabile" + i);
                        Comuni cm = en.getEm().find(Comuni.class, Long.valueOf(rs.getString("citta" + i)));
                        SediFormazione sf = new SediFormazione(denominazione, via, referente, telefono, cellulare, email, cm);
                        sf.setSoggetto(sa);
                        out.add(sf);
                    }
                }
            }
        } catch (Exception ex) {
            out = null;
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;

    }

    public List<Docenti> estrai_DOCENTI_SA(SoggettiAttuatori sa, Entity en) {
        List<Docenti> out = new LinkedList<>();
        try {
            String username = sa.getUsernameaccr();
            String date = new DateTime().toString(patternFile);

            String sql = "SELECT id,UPPER(nome),UPPER(cognome),UPPER(cf),datanascita,LOWER(mail) "
                    + "FROM allegato_b WHERE username = '" + username + "' GROUP BY cf ORDER BY id";

            try (PreparedStatement ps = this.c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    String nome = rs.getString(2);
                    String cognome = rs.getString(3);
                    String cf = rs.getString(4);
                    Date data_nascita = getUtilDate(rs.getString(5), patternITA);
                    String fascia = "FA";
                    String email = rs.getString(6);

                    Docenti d = new Docenti(nome, cognome, cf, data_nascita, email);
                    d.setSoggetto(sa);
                    d.setFascia(en.getEm().find(FasceDocenti.class, fascia));

                    String sql1 = "SELECT allegatocv,allegatodr FROM allegato_b1 WHERE username='" + username + "' AND idallegato_b1 = '" + id + "'";

                    try (PreparedStatement ps1 = this.c.prepareStatement(sql1); ResultSet rs1 = ps1.executeQuery()) {
                        if (rs1.next()) {
                            String path = en.getPath("pathDoc_Docenti").replace("@docente", d.getCodicefiscale());
                            createDir(path);

                            FileDownload allegatocv = preparefilefordownload(rs1.getString(1));
                            FileDownload allegatodr = preparefilefordownload(rs1.getString(2));

                            if (allegatocv != null && allegatodr != null) {
                                String ext1 = "." + FilenameUtils.getExtension(allegatocv.getName());
                                String ext2 = "." + FilenameUtils.getExtension(allegatodr.getName());

                                File dest1 = new File(path + "Curriculum_" + sa.getId() + "_" + date + "_" + d.getCodicefiscale() + ext1);
                                File dest2 = new File(path + "Doc_id_" + sa.getId() + "_" + date + "_" + d.getCodicefiscale() + ext2);

                                FileUtils.writeByteArrayToFile(dest1, decodeBase64(allegatocv.getContent()));
                                FileUtils.writeByteArrayToFile(dest2, decodeBase64(allegatodr.getContent()));

                                if (dest1.length() > 0 && dest2.length() > 0) {
                                    //DOCUMENTI
                                    d.setScadenza_doc(new SimpleDateFormat(patternITA).parse("31/12/2031"));
                                    d.setCurriculum(dest1.getPath());
                                    d.setDocId(dest2.getPath());
                                    d.setStato("DV");
                                    out.add(d);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;

    }

    //Totale Ore rendicontabili per Maschera Modello 5
    public Map<Long, Long> OreRendicontabiliDocentiFASEA(int pf) {
        Map result = new HashMap();
        try {

            if (Utility.demoversion) {
                String sql1 = "SELECT MAX(totaleorerendicontabili) as totOre,idutente "
                        + "FROM registro_completo WHERE fase = 'A' "
                        + "AND idutente IN (SELECT DISTINCT(idutente) "
                        + "FROM registro_completo WHERE fase = 'A' "
                        + "AND idprogetti_formativi = ? "
                        + "AND ruolo = 'DOCENTE') GROUP BY idutente,data";
                try (PreparedStatement ps = this.c.prepareStatement(sql1)) {
                    ps.setInt(1, pf);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            if (result.get(rs.getLong("idutente")) == null) {
                                result.put(rs.getLong("idutente"), rs.getLong("totOre"));
                            } else {
                                long pres = (long) result.get(rs.getLong("idutente"));
                                result.put(rs.getLong("idutente"), pres + rs.getLong("totOre"));
                            }

                        }
                    }
                }

            } else {

                String sql = "SELECT sum(totaleorerendicontabili) as totOre,idutente FROM registro_completo WHERE ruolo = 'DOCENTE' "
                        + "AND fase='A' AND idprogetti_formativi = ? GROUP BY idutente";
                try (PreparedStatement ps = this.c.prepareStatement(sql)) {
                    ps.setInt(1, pf);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            result.put(rs.getLong("idutente"), rs.getLong("totOre"));
                        }
                    }
                }
            }
            return result;
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return result;
    }

    public Map<Long, Long> OreRendicontabiliDocenti(int pf) {
        Map result = new HashMap();
        try {
            String sql = "SELECT sum(totaleorerendicontabili) as totOre,idutente FROM registro_completo WHERE ruolo = 'DOCENTE' AND idprogetti_formativi = ? GROUP BY idutente";
            try (PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setInt(1, pf);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.put(rs.getLong("idutente"), rs.getLong("totOre"));
                    }
                }
            }
            return result;
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return result;
    }

    //Totale Ore rendicontabili per Maschera Modello 5
    public Map<Long, Long> OreRendicontabiliAlunni(int pf) {
        Map<Long, Long> result = new HashMap<>();
        try {
            String sql = "SELECT sum(totaleorerendicontabili) as totOre,idutente FROM registro_completo WHERE idprogetti_formativi = ? AND ruolo like 'ALLIEVO%' GROUP BY idutente";
            try (PreparedStatement ps = this.c.prepareStatement(sql)) {
                ps.setInt(1, pf);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.put(rs.getLong("idutente"), rs.getLong("totOre"));
                    }
                }
            }
            return result;
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return result;
    }

    //Totale Ore rendicontabili FASE B
    public Map<Long, Long> OreRendicontabiliAlunni_faseB(int pf) {
        Map<Long, Long> result = new HashMap<>();
        try {

            if (Utility.demoversion) {
                String sql1 = "SELECT MAX(totaleorerendicontabili) as totOre,idutente "
                        + "FROM registro_completo WHERE fase = 'B' AND ruolo LIKE 'ALLIEVO%' "
                        + "AND idutente IN (SELECT DISTINCT(idutente) "
                        + "FROM registro_completo WHERE fase = 'B' "
                        + "AND idprogetti_formativi = ? "
                        + "AND ruolo LIKE 'ALLIEVO%') GROUP BY idutente,data";
                try (PreparedStatement ps = this.c.prepareStatement(sql1)) {
                    ps.setInt(1, pf);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            if (result.get(rs.getLong("idutente")) == null) {
                                result.put(rs.getLong("idutente"), rs.getLong("totOre"));
                            } else {
                                long pres = (long) result.get(rs.getLong("idutente"));
                                result.put(rs.getLong("idutente"), pres + rs.getLong("totOre"));
                            }

                        }
                    }
                }
            } else {
                String sql = "SELECT sum(totaleorerendicontabili) as totOre,idutente FROM registro_completo WHERE fase = 'B' AND ruolo LIKE 'ALLIEVO%' AND  idprogetti_formativi = ? GROUP BY idutente;";
                try (PreparedStatement ps = this.c.prepareStatement(sql)) {
                    ps.setInt(1, pf);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            result.put(rs.getLong("idutente"), rs.getLong("totOre"));
                        }
                    }
                }
            }
            return result;
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return result;
    }

    //Totale Ore rendicontabili FASE A per Maschera Modello 5
    public Map<Long, Long> OreRendicontabiliAlunni_faseA(int pf) {
        Map<Long, Long> result = new HashMap<>();
        try {

            if (Utility.demoversion) {
                String sql1 = "SELECT MAX(totaleorerendicontabili) as totOre,idutente "
                        + "FROM registro_completo WHERE fase = 'A' AND ruolo LIKE 'ALLIEVO%' "
                        + "AND idutente IN (SELECT DISTINCT(idutente) "
                        + "FROM registro_completo WHERE fase = 'A' "
                        + "AND idprogetti_formativi = ? "
                        + "AND ruolo LIKE 'ALLIEVO%') GROUP BY idutente,data";
                try (PreparedStatement ps = this.c.prepareStatement(sql1)) {
                    ps.setInt(1, pf);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            if (result.get(rs.getLong("idutente")) == null) {
                                result.put(rs.getLong("idutente"), rs.getLong("totOre"));
                            } else {
                                long pres = (long) result.get(rs.getLong("idutente"));
                                result.put(rs.getLong("idutente"), pres + rs.getLong("totOre"));
                            }

                        }
                    }
                }
            } else {
                String sql = "SELECT sum(totaleorerendicontabili) as totOre,idutente FROM registro_completo WHERE fase = 'A' AND ruolo LIKE 'ALLIEVO%' "
                        + "AND idprogetti_formativi = ? GROUP BY idutente";
                try (PreparedStatement ps = this.c.prepareStatement(sql)) {
                    ps.setInt(1, pf);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            result.put(rs.getLong("idutente"), rs.getLong("totOre"));
                        }
                    }
                }
            }
            return result;
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return result;
    }

    public String[] dati_modello5_neet(String idneet, String idsa, String pf) {
        String datafinepercorso = "";
        AtomicLong orefrequenza = new AtomicLong(0L);
        try {
            String sql = "SELECT * FROM registro_completo "
                    + "WHERE idutente = " + idneet + " AND idprogetti_formativi = " + pf + " AND idsoggetti_attuatori = " + idsa
                    + " AND ruolo = 'ALLIEVO NEET' ORDER BY data";
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    datafinepercorso = Utility.sdfITA.format(rs.getDate("data"));
                    orefrequenza.addAndGet(rs.getLong("totaleorerendicontabili"));
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return new String[]{datafinepercorso, calcoladurata(orefrequenza.get())};
    }

    public void popolaregistro_B(ProgettiFormativi p, Lezioni_Modelli lm) {
        try {
            String ins = "INSERT INTO registro_completo (idprogetti_formativi,idsoggetti_attuatori,cip,data,idriunione,numpartecipanti,orainizio,orafine,durata,nud,fase,gruppofaseb,ruolo,cognome,nome,email,orelogin,orelogout,totaleore,totaleorerendicontabili,idutente) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            long durata = new BigDecimal(lm.getLezione_calendario().getOre() * 3600000L).longValue();

            p.getAllievi().forEach(al1 -> {
                if (al1.getStatopartecipazione().getId().equals("01") && al1.getGruppo_faseB() == lm.getGruppo_faseB()) {
                    try {
                        try (PreparedStatement ps = this.c.prepareStatement(ins)) {
                            ps.setLong(1, p.getId());
                            ps.setLong(2, p.getSoggetto().getId());
                            ps.setString(3, p.getCip());
                            ps.setString(4, sdfSQL.format(lm.getGiorno()));
                            ps.setString(5, "TESTINGID_B" + lm.getGruppo_faseB() + "_" + lm.getLezione_calendario().getLezione());
                            ps.setInt(6, p.getAllievi_ok() + 1);
                            ps.setString(7, lm.getOrainizio());
                            ps.setString(8, lm.getOrafine());
                            ps.setLong(9, durata);
                            ps.setString(10, "GIORNO " + lm.getLezione_calendario().getLezione() + " - " + lm.getLezione_calendario().getUnitadidattica().getCodice());
                            ps.setString(11, "B");
                            ps.setInt(12, lm.getGruppo_faseB());
                            ps.setString(13, "ALLIEVO NEET");
                            ps.setString(14, al1.getCognome());
                            ps.setString(15, al1.getNome());
                            ps.setString(16, al1.getEmail());
                            ps.setString(17, lm.getOrainizio());
                            ps.setString(18, lm.getOrafine());
                            ps.setLong(19, durata);
                            ps.setLong(20, durata);
                            ps.setLong(21, al1.getId());
                            ps.execute();
                        }
                    } catch (Exception ex) {
                        LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
                    }
                }
            });

            try (PreparedStatement ps = this.c.prepareStatement(ins)) {
                ps.setLong(1, p.getId());
                ps.setLong(2, p.getSoggetto().getId());
                ps.setString(3, p.getCip());
                ps.setString(4, sdfSQL.format(lm.getGiorno()));
                ps.setString(5, "TESTINGID_B" + lm.getGruppo_faseB() + "_" + lm.getLezione_calendario().getLezione());
                ps.setInt(6, p.getAllievi_ok() + 1);
                ps.setString(7, lm.getOrainizio());
                ps.setString(8, lm.getOrafine());
                ps.setLong(9, durata);
                ps.setString(10, "GIORNO " + lm.getLezione_calendario().getLezione() + " - " + lm.getLezione_calendario().getUnitadidattica().getCodice());
                ps.setString(11, "B");
                ps.setInt(12, lm.getGruppo_faseB());
                ps.setString(13, "DOCENTE");
                ps.setString(14, lm.getDocente().getCognome());
                ps.setString(15, lm.getDocente().getNome());
                ps.setString(16, lm.getDocente().getEmail());
                ps.setString(17, lm.getOrainizio());
                ps.setString(18, lm.getOrafine());
                ps.setLong(19, durata);
                ps.setLong(20, durata);
                ps.setLong(21, lm.getDocente().getId());
                ps.execute();
            }

        } catch (Exception ex1) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex1));
        }
    }

    public void popolaregistro_A(ProgettiFormativi p, Lezioni_Modelli lm) {
        try {

            String ins = "INSERT INTO registro_completo (idprogetti_formativi,idsoggetti_attuatori,cip,data,idriunione,numpartecipanti,orainizio,orafine,durata,nud,fase,gruppofaseb,ruolo,cognome,nome,email,orelogin,orelogout,totaleore,totaleorerendicontabili,idutente) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            long durata = new BigDecimal(lm.getLezione_calendario().getOre() * 3600000L).longValue();
            p.getAllievi().forEach(al1 -> {
                if (al1.getStatopartecipazione().getId().equals("01")) {
                    try {
                        try (PreparedStatement ps = this.c.prepareStatement(ins)) {
                            ps.setLong(1, p.getId());
                            ps.setLong(2, p.getSoggetto().getId());
                            ps.setString(3, p.getCip());
                            ps.setString(4, sdfSQL.format(lm.getGiorno()));
                            ps.setString(5, "TESTINGID_" + lm.getLezione_calendario().getLezione());
                            ps.setInt(6, p.getAllievi_ok() + 1);
                            ps.setString(7, lm.getOrainizio());
                            ps.setString(8, lm.getOrafine());
                            ps.setLong(9, durata);
                            ps.setString(10, "GIORNO " + lm.getLezione_calendario().getLezione() + " - " + lm.getLezione_calendario().getUnitadidattica().getCodice());
                            ps.setString(11, "A");
                            ps.setInt(12, 1);
                            ps.setString(13, "ALLIEVO NEET");
                            ps.setString(14, al1.getCognome());
                            ps.setString(15, al1.getNome());
                            ps.setString(16, al1.getEmail());
                            ps.setString(17, lm.getOrainizio());
                            ps.setString(18, lm.getOrafine());
                            ps.setLong(19, durata);
                            ps.setLong(20, durata);
                            ps.setLong(21, al1.getId());
                            ps.execute();
                        }
                    } catch (Exception ex) {
                        LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
                    }
                }
            });

            try (PreparedStatement ps = this.c.prepareStatement(ins)) {
                ps.setLong(1, p.getId());
                ps.setLong(2, p.getSoggetto().getId());
                ps.setString(3, p.getCip());
                ps.setString(4, sdfSQL.format(lm.getGiorno()));
                ps.setString(5, "TESTINGID_" + lm.getLezione_calendario().getLezione());
                ps.setInt(6, p.getAllievi_ok() + 1);
                ps.setString(7, lm.getOrainizio());
                ps.setString(8, lm.getOrafine());
                ps.setLong(9, durata);
                ps.setString(10, "GIORNO " + lm.getLezione_calendario().getLezione() + " - " + lm.getLezione_calendario().getUnitadidattica().getCodice());
                ps.setString(11, "A");
                ps.setInt(12, 1);
                ps.setString(13, "DOCENTE");
                ps.setString(14, lm.getDocente().getCognome());
                ps.setString(15, lm.getDocente().getNome());
                ps.setString(16, lm.getDocente().getEmail());
                ps.setString(17, lm.getOrainizio());
                ps.setString(18, lm.getOrafine());
                ps.setLong(19, durata);
                ps.setLong(20, durata);
                ps.setLong(21, lm.getDocente().getId());
                ps.execute();
            }

        } catch (Exception ex1) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex1));
        }
    }

    public void svuotaregistroB(String idpr) {
        try {
            if (test) {
                String del = "DELETE FROM registro_completo WHERE fase = 'B' AND idprogetti_formativi = " + idpr;
                try (Statement st = this.c.createStatement()) {
                    st.execute(del);
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
    }

    public void svuotaregistro(String idpr) {
        try {
            if (test) {
                String del = "DELETE FROM registro_completo WHERE idprogetti_formativi = " + idpr;
                try (Statement st = this.c.createStatement()) {
                    st.execute(del);
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
    }

    public List<Registro_completo> registro_modello6(int idpr) {
        List<Registro_completo> registro = new ArrayList<>();
        try {
            //FAD
            String sql = "SELECT * FROM registro_completo WHERE idprogetti_formativi = " + idpr + " GROUP BY ruolo,idutente,data ORDER BY data";
            try (Statement st = this.c.createStatement(TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {

                    long orerend = rs.getLong(21);
                    Registro_completo rc = new Registro_completo(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getString(4),
                            new DateTime(rs.getDate(5).getTime()),
                            rs.getString(6),
                            rs.getInt(7),
                            rs.getString(8),
                            rs.getString(9),
                            rs.getLong(10),
                            rs.getString(11),
                            rs.getString(12),
                            rs.getInt(13),
                            rs.getString(14),
                            rs.getString(15),
                            rs.getString(16),
                            rs.getString(17),
                            rs.getString(18),
                            rs.getString(19),
                            rs.getLong(20),
                            orerend,
                            rs.getInt(23));
                    registro.add(rc);
                }
            }

            //PRESENZA
            String sql1 = "SELECT * FROM presenzelezioni p, progetti_formativi f, lezioni_modelli lm, lezione_calendario lc , docenti d "
                    + " WHERE d.iddocenti=p.iddocente AND lc.id_lezionecalendario=lm.id_lezionecalendario AND lm.id_lezionimodelli=p.idlezioneriferimento AND "
                    + " p.idprogetto=f.idprogetti_formativi AND p.idprogetto = " + idpr + " ORDER BY p.datalezione,p.orainizio;";

            try (Statement st1 = this.c.createStatement(TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY); ResultSet rs1 = st1.executeQuery(sql1)) {
                while (rs1.next()) {
                    String sql2 = "SELECT * FROM presenzelezioniallievi a, allievi l WHERE a.idallievi=l.idallievi AND a.idpresenzelezioni = "
                            + rs1.getInt("p.idpresenzelezioni")
                            + " AND a.convalidata = 1 GROUP BY l.idallievi";

                    try (Statement st2 = this.c.createStatement(TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY); ResultSet rs2 = st2.executeQuery(sql2)) {

                        int numpartecipanti = 0;
                        while (rs2.next()) {
                            numpartecipanti++;
                        }
                        rs2.beforeFirst();

                        long durata = calcolaintervallomillis(rs1.getString("p.orainizio"), rs1.getString("p.orafine"));

                        String fase = rs1.getString("lc.codice_ud").startsWith("A") ? "A" : "B";

                        int gruppofaseb = fase.equals("A") ? 0 : rs1.getInt("lm.gruppo_faseB");

                        Registro_completo docente = new Registro_completo(0,
                                idpr,
                                rs1.getInt("f.idsoggetti_attuatori"),
                                rs1.getString("f.cip"),
                                new DateTime(rs1.getDate("p.datalezione").getTime()),
                                rs1.getString("f.cip") + "_" + fase + "_" + rs1.getString("lc.codice_ud") + "_" + StringUtils.replace(rs1.getString("p.datalezione"), "-", ""),
                                numpartecipanti,
                                rs1.getString("p.orainizio"),
                                rs1.getString("p.orafine"),
                                durata,
                                rs1.getString("lc.codice_ud"),
                                fase,
                                gruppofaseb,
                                "DOCENTE",
                                rs1.getString("d.cognome"),
                                rs1.getString("d.nome"),
                                rs1.getString("d.email"),
                                rs1.getString("p.orainizio"),
                                rs1.getString("p.orafine"),
                                durata,
                                durata,
                                rs1.getInt("d.iddocenti"));
                        registro.add(docente);

                        while (rs2.next()) {

                            Registro_completo rc = new Registro_completo(0,
                                    idpr,
                                    rs1.getInt("f.idsoggetti_attuatori"),
                                    rs1.getString("f.cip"),
                                    new DateTime(rs1.getDate("p.datalezione").getTime()),
                                    rs1.getString("f.cip") + "_" + fase + "_" + rs1.getString("lc.codice_ud") + "_" + StringUtils.replace(rs1.getString("p.datalezione"), "-", ""),
                                    numpartecipanti,
                                    rs1.getString("p.orainizio"),
                                    rs1.getString("p.orafine"),
                                    calcolaintervallomillis(rs1.getString("p.orainizio"), rs1.getString("p.orafine")),
                                    rs1.getString("lc.codice_ud"),
                                    fase,
                                    gruppofaseb,
                                    "ALLIEVO",
                                    rs2.getString("l.cognome"),
                                    rs2.getString("l.nome"),
                                    rs2.getString("l.email"),
                                    rs2.getString("a.orainizio"),
                                    rs2.getString("a.orafine"),
                                    rs2.getLong("a.durata"),
                                    rs2.getLong("a.durataconvalidata"),
                                    rs2.getInt("l.idallievi"));
                            registro.add(rc);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return registro;
    }

    public List<Item> area_qualificazione() {
        List<Item> out = new ArrayList<>();
        try {
            String sql = "SELECT * FROM qualificazione_rc";
            try (PreparedStatement ps1 = this.c.prepareStatement(sql); ResultSet rs1 = ps1.executeQuery()) {
                while (rs1.next()) {
                    out.add(new Item(rs1.getInt(1), rs1.getString(2)));
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public List<Item> inquadramento() {
        List<Item> out = new ArrayList<>();
        try {
            String sql = "SELECT * FROM inquadramento_rc";
            try (PreparedStatement ps1 = this.c.prepareStatement(sql); ResultSet rs1 = ps1.executeQuery()) {
                while (rs1.next()) {
                    out.add(new Item(rs1.getInt(1), rs1.getString(2)));
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public ArrayList<Item> attivita_docenti() {
        ArrayList<Item> out = new ArrayList<>();
        try {
            String sql = "SELECT * FROM attivita_docenti_rc";
            try (PreparedStatement ps1 = this.c.prepareStatement(sql); ResultSet rs1 = ps1.executeQuery()) {
                while (rs1.next()) {
                    out.add(new Item(rs1.getInt(1), rs1.getString(2)));
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public ArrayList<Item> disponibilita() {
        ArrayList<Item> out = new ArrayList<>();
        try {
            String sql = "SELECT * FROM disponibilita_rc";
            try (PreparedStatement ps1 = this.c.prepareStatement(sql); ResultSet rs1 = ps1.executeQuery()) {
                while (rs1.next()) {
                    out.add(new Item(rs1.getInt(1), rs1.getString(2)));
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public boolean isVisible(String gruppo, String page) {

        try {
            String sql = "SELECT permessi FROM pagina WHERE nome='" + page + "' AND permessi LIKE'%" + gruppo + "%'";
            try (PreparedStatement ps1 = this.c.prepareStatement(sql); ResultSet rs1 = ps1.executeQuery()) {
                return rs1.next();
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return false;
    }

    public ArrayList<Item> fontifin() {
        ArrayList<Item> out = new ArrayList<>();
        try {
            String sql = "SELECT * FROM fontifin_rc";
            try (PreparedStatement ps1 = this.c.prepareStatement(sql); ResultSet rs1 = ps1.executeQuery()) {
                while (rs1.next()) {
                    out.add(new Item(rs1.getInt(1), rs1.getString(2)));
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public String[] sa_cip(int idpr) {
        try {

            String sql0 = "SELECT cip,idsoggetti_attuatori FROM progetti_formativi WHERE idprogetti_formativi = " + idpr;
            try (Statement st0 = this.c.createStatement(); ResultSet rs0 = st0.executeQuery(sql0)) {
                if (rs0.next()) {
                    String cip = rs0.getString(1);
                    String sql1 = "SELECT ragionesociale FROM soggetti_attuatori WHERE idsoggetti_attuatori = " + rs0.getInt(2);
                    try (Statement st1 = this.c.createStatement(); ResultSet rs1 = st1.executeQuery(sql1)) {
                        if (rs1.next()) {
                            String[] out = {rs1.getString(1).trim().toUpperCase(), cip, rs0.getString(2)};
                            return out;
                        }
                    }

                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return null;
    }

    public List<Utenti> list_Allievi_noAccento(int idpr) {
        List<Utenti> out = new ArrayList<>();
        try {
            String sql = "SELECT idallievi,nome,cognome,codicefiscale,email FROM allievi WHERE id_statopartecipazione='01' AND idprogetti_formativi = " + idpr;
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    Utenti u = new Utenti(rs.getInt("idallievi"),
                            stripAccents(rs.getString("cognome").toUpperCase().trim()),
                            stripAccents(rs.getString("nome").toUpperCase().trim()),
                            rs.getString("codicefiscale").toUpperCase(), "ALLIEVO NEET",
                            rs.getString("email").toLowerCase());
                    out.add(u);
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public List<Utenti> list_Allievi_noAccento(int idpr, int gruppo) {
        List<Utenti> out = new ArrayList<>();
        try {
            String sql = "SELECT idallievi,nome,cognome,codicefiscale,email FROM allievi WHERE id_statopartecipazione='01' AND idprogetti_formativi = " + idpr + " AND gruppo_faseB = " + gruppo;
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    Utenti u = new Utenti(rs.getInt("idallievi"),
                            stripAccents(rs.getString("cognome").toUpperCase().trim()),
                            stripAccents(rs.getString("nome").toUpperCase().trim()),
                            rs.getString("codicefiscale").toUpperCase(), "ALLIEVO NEET",
                            rs.getString("email").toLowerCase());
                    out.add(u);
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public List<Utenti> list_Allievi(int idpr) {
        List<Utenti> out = new ArrayList<>();
        try {
            String sql = "SELECT idallievi,nome,cognome,codicefiscale,email FROM allievi WHERE id_statopartecipazione='01' AND idprogetti_formativi = " + idpr;
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    Utenti u = new Utenti(rs.getInt("idallievi"),
                            (rs.getString("cognome").toUpperCase().trim()),
                            (rs.getString("nome").toUpperCase().trim()),
                            rs.getString("codicefiscale").toUpperCase(), "ALLIEVO NEET",
                            rs.getString("email").toLowerCase());
                    out.add(u);
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public List<Utenti> list_Docenti(int idpr) {
        List<Utenti> out = new ArrayList<>();
        try {
            String sql = "SELECT iddocenti,nome,cognome,codicefiscale,email FROM docenti WHERE iddocenti IN "
                    + "(SELECT iddocenti FROM progetti_docenti WHERE idprogetti_formativi = " + idpr + ")";
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    Utenti u = new Utenti(rs.getInt("iddocenti"),
                            (rs.getString("cognome").toUpperCase().trim()),
                            (rs.getString("nome").toUpperCase().trim()),
                            rs.getString("codicefiscale").toUpperCase(), "DOCENTE",
                            rs.getString("email").toLowerCase());
                    out.add(u);
                }
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public List<Utenti> list_Docenti_noAccento(int idpr) {
        List<Utenti> out = new ArrayList<>();
        try {
            String sql = "SELECT iddocenti,nome,cognome,codicefiscale,email FROM docenti WHERE iddocenti IN "
                    + "(SELECT iddocenti FROM progetti_docenti WHERE idprogetti_formativi = " + idpr + ")";
            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    Utenti u = new Utenti(rs.getInt("iddocenti"),
                            stripAccents(rs.getString("cognome").toUpperCase().trim()),
                            stripAccents(rs.getString("nome").toUpperCase().trim()),
                            rs.getString("codicefiscale").toUpperCase(), "DOCENTE",
                            rs.getString("email").toLowerCase());
                    out.add(u);
                }
            }
        } catch (SQLException ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return out;
    }

    public boolean insertRegistro(rc.so.util.Registro_completo rc) {
        try {
            String insert = "INSERT INTO registro_completo (idprogetti_formativi,idsoggetti_attuatori,cip,data,"
                    + "idriunione,numpartecipanti,orainizio,orafine,durata,nud,"
                    + "fase,gruppofaseb,ruolo,cognome,nome,email,orelogin,orelogout,"
                    + "totaleore,totaleorerendicontabili,idutente) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try (PreparedStatement ps = this.c.prepareStatement(insert)) {
                ps.setInt(1, rc.getIdprogetti_formativi());
                ps.setInt(2, rc.getIdsoggetti_attuatori());
                ps.setString(3, rc.getCip());
                ps.setString(4, rc.getData().toString(Utility.patternSql));

                ps.setString(5, rc.getIdriunione());
                ps.setInt(6, rc.getNumpartecipanti());
                ps.setString(7, rc.getOrainizio());
                ps.setString(8, rc.getOrafine());
                ps.setLong(9, rc.getDurata());
                ps.setString(10, rc.getNud());

                ps.setString(11, rc.getFase());
                ps.setInt(12, rc.getGruppofaseb());
                ps.setString(13, rc.getRuolo());
                ps.setString(14, rc.getCognome());
                ps.setString(15, rc.getNome());
                ps.setString(16, rc.getEmail());
                ps.setString(17, rc.getOrelogin());
                ps.setString(18, rc.getOrelogout());

                ps.setLong(19, rc.getTotaleore());
                ps.setLong(20, rc.getTotaleorerendicontabili());
                ps.setInt(21, rc.getIdutente());

                ps.execute();
                return true;
            }
        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return false;
    }

    public List<Presenze_Lezioni_Allievi> presenze_fad(Long idallievo) {
        List<Presenze_Lezioni_Allievi> pla = new ArrayList<>();
        try {

            String sql = "SELECT * FROM registro_completo r WHERE r.idutente='" + idallievo + "' AND r.ruolo='ALLIEVO'";

            try (Statement st = this.c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    Presenze_Lezioni_Allievi pl1 = new Presenze_Lezioni_Allievi();
                    pl1.setDatalezione(new java.util.Date(rs.getDate("data").getTime()));
                    pl1.setConvalidata(true);
                    pl1.setDurata(rs.getLong("totaleore"));
                    pl1.setDurataconvalidata(rs.getLong("totaleorerendicontabili"));
                    pl1.setOrainizio(rs.getString("orelogin"));
                    pl1.setOrafine(rs.getString("orelogout"));
                    pla.add(pl1);
                }
            }

        } catch (Exception ex) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex));
        }
        return pla;
    }

    public List<String> ore_convalidateAllievi(String idallievo) {
        List<String> report = new ArrayList<>();
        String sql1 = (idallievo == null) ? "SELECT a.idallievi FROM allievi a WHERE a.id_statopartecipazione IN ('15','16','17')"
                : "SELECT a.idallievi FROM allievi a WHERE a.idallievi=" + idallievo;

        try (Statement st1 = this.c.createStatement(); ResultSet rs1 = st1.executeQuery(sql1)) {
            while (rs1.next()) {
                int idallievi = rs1.getInt(1);
                String sql2 = "SELECT r.totaleorerendicontabili,r.fase FROM registro_completo r WHERE r.idutente='" + idallievi + "' AND r.ruolo='ALLIEVO'";
//                String sql3 = "SELECT a.durataconvalidata FROM presenzelezioniallievi a WHERE a.idallievi = '" + idallievi + "' AND a.convalidata=1";
                String sql3 = "SELECT p.durataconvalidata,z.codice_ud FROM presenzelezioniallievi p, presenzelezioni l, lezione_calendario z "
                        + "WHERE p.idallievi = '" + idallievi + "' AND p.convalidata=1 AND l.idpresenzelezioni=p.idpresenzelezioni "
                        + "AND l.idlezioneriferimento=z.id_lezionecalendario ";

                Long presenze = 0L;

                try (Statement st2 = this.c.createStatement(); ResultSet rs2 = st2.executeQuery(sql2)) {

                    while (rs2.next()) {
                        report.add(idallievi + ";" + rs2.getString(2) + ";" + rs2.getLong(1));
                        presenze += rs2.getLong(1);
                    }
                }

                try (Statement st3 = this.c.createStatement(); ResultSet rs3 = st3.executeQuery(sql3)) {
                    while (rs3.next()) {
                        Long conv = rs3.getString(1) == null ? 0L : rs3.getLong(1);
                        report.add(idallievi + ";" + StringUtils.substring(rs3.getString(2), 0, 1) + ";" + conv);
                        presenze += conv;
                    }
                }

                double res = presenze / 3600000.00;

                String upd4 = "UPDATE allievi SET importo = '" + res + "' WHERE idallievi='" + idallievi + "'";

                try (Statement st4 = this.c.createStatement()) {
                    st4.executeUpdate(upd4);
                }

            }

        } catch (Exception ex1) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex1));
        }

        return report;
    }

    public String[] estrai_dati_permodello6(Long idsoggetto) {
        String[] out = {"", ""};
        try {
            String sql1 = "SELECT p.accreditato,a.soggettosingolo,a.costituenda,a.costituita,a.rete,a.consorzio "
                    + "FROM enm_toscana_prod.bando_toscana_mcn p,enm_gestione_toscana_prod.soggetti_attuatori t, enm_toscana_prod.allegato_a a "
                    + "WHERE p.protocollo=t.protocollo AND p.username=a.username AND t.idsoggetti_attuatori=1;";
            try (Statement st1 = this.c.createStatement(); ResultSet rs1 = st1.executeQuery(sql1)) {
                if (rs1.next()) {
                    out[0] = rs1.getString(1).toUpperCase();
                    if (rs1.getString("a.soggettosingolo").equals("SI")) {
                        out[1] = "Soggetto Singolo";
                    } else if (rs1.getString("a.costituenda").equals("SI")) {
                        out[1] = "Costituenda ATI/ATS o costituenda rete di imprese";
                    } else if (rs1.getString("a.costituita").equals("SI")) {
                        out[1] = "Costituita ATI/ATS che preveda mandato di rappresentanza specifico al capofila";
                    } else if (rs1.getString("a.rete").equals("SI")) {
                        out[1] = "Costituita Rete contratto dotata di organo comune con potere di rappresentanza";
                    } else if (rs1.getString("a.consorzio").equals("SI")) {
                        out[1] = "Consorzio/Fondazione/Rete soggetto";
                    }
                }
            }
        } catch (Exception ex1) {
            LOGAPP.log(Level.SEVERE, estraiEccezione(ex1));
        }
        return out;
    }

}
