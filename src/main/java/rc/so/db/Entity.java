/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.db;

import com.google.common.base.Splitter;
import rc.so.domain.Allievi;
import rc.so.domain.Allievi_Pregresso;
import rc.so.domain.Ateco;
import rc.so.domain.Attivita;
import rc.so.domain.CPI;
import rc.so.domain.Cad;
import rc.so.domain.Comuni;
import rc.so.domain.Docenti;
import rc.so.domain.DocumentiPrg;
import rc.so.domain.Documenti_Allievi;
import rc.so.domain.Documenti_Allievi_Pregresso;
import rc.so.domain.Email;
import rc.so.domain.Estrazioni;
import rc.so.domain.FadMicro;
import rc.so.domain.Faq;
import rc.so.domain.TipoFaq;
import rc.so.domain.FasceDocenti;
import rc.so.domain.LezioneCalendario;
import rc.so.domain.Lezioni_Modelli;
import rc.so.domain.MascheraM5;
import rc.so.domain.ModelliPrg;
import rc.so.domain.Nazioni_rc;
import rc.so.domain.Pagina;
import rc.so.domain.Path;
import rc.so.domain.ProgettiFormativi;
import rc.so.domain.SediFormazione;
import rc.so.domain.Selfiemployment_Prestiti;
import rc.so.domain.SoggettiAttuatori;
import rc.so.domain.StatiPrg;
import rc.so.domain.StatoPartecipazione;
import rc.so.domain.Storico_ModificheInfo;
import rc.so.domain.Storico_Prg;
import rc.so.domain.TipoDoc;
import rc.so.domain.TipoDoc_Allievi;
import rc.so.domain.TitoliStudio;
import rc.so.domain.Tracking;
import rc.so.domain.UnitaDidattiche;
import rc.so.domain.User;
import rc.so.entity.Item;
import rc.so.domain.Cloud;
import rc.so.domain.checklist_finale;
import rc.so.util.Utility;
import static rc.so.util.Utility.convMd5;
import static rc.so.util.Utility.dtz_italy;
import static rc.so.util.Utility.maxQueryResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import static org.apache.commons.io.FilenameUtils.separatorsToSystem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.joda.time.DateTime;
import rc.so.domain.Presenze_Lezioni;
import rc.so.domain.Presenze_Lezioni_Allievi;

/**
 *
 * @author rcosco
 */
public class Entity {

    EntityManagerFactory emf;
    EntityManager em;

    public Entity() {
        this.emf = Persistence.createEntityManagerFactory("microcredito");
        this.em = this.emf.createEntityManager();
        this.em.clear();
    }

    public EntityManager getEm() {
        return em;
    }

    public void begin() {
        this.em.getTransaction().begin();
    }

    public void persist(Object o) {
        this.em.persist(o);
    }

    public void merge(Object o) {
        this.em.merge(o);
    }

    public void commit() {
        this.em.getTransaction().commit();
    }

    public void rollBack() {
        if (this.em.getTransaction().isActive()) {
            this.em.getTransaction().rollback();
        }
    }

    public void remove(Object o) {
        this.em.remove(o);
    }

    public void flush() {
        this.em.flush();
    }

    public void close() {
        this.em.close();
        this.emf.close();
    }

    public List findAll(Class c) {
        return this.em.createQuery("Select a FROM " + c.getSimpleName() + " a", c).getResultList();
    }

    public boolean isVisible(String gruppo, String page) {
        Pagina p = this.em.find(Pagina.class, page);
        StringTokenizer st = new StringTokenizer(p == null ? "" : p.getPermessi(), "-");
        while (st.hasMoreTokens()) {
            if (gruppo.equals(st.nextToken())) {
                return true;
            }
        }
        return false;
    }

    public void insertTracking(String id_user, String action) {
        if (!this.em.getTransaction().isActive()) {
            this.begin();
        }

        if (id_user == null || id_user.equals("")) {
            id_user = "Microcredito";
        }
        this.persist(new Tracking(id_user, action));
        this.commit();
    }

    public TipoDoc_Allievi getTipoDoc_Allievi(String id) {
        return this.em.find(TipoDoc_Allievi.class, Long.valueOf(id));
    }

    public String getPath(String id) {
        Path out = this.em.find(Path.class, id);
        if (out != null) {
            if (out.getUrl() != null) {
                if (SystemUtils.OS_NAME.contains("Windows") && out.getUrl().startsWith("/")) {
                    return separatorsToSystem("C:" + out.getUrl());
                } else {
                    return separatorsToSystem(out.getUrl());

                }
            }
        }
        return "";
    }

    public List<Item> listaRegioni() {
        List<String> regioni = this.em.createNamedQuery("comuni.Regione", String.class).getResultList();
        List<Item> out = new ArrayList<>();
        for (String r : regioni) {
            out.add(new Item(r, r, ""));
        }
        return out;

    }

    public List<Item> listaComuni_totale() {
        List<String> cm = this.em.createNamedQuery("comuni.ComuniTotale", String.class).getResultList();
        List<Item> out = new ArrayList<>();
        for (String c : cm) {
            out.add(new Item(c, c, ""));
        }
        return out;
    }

    public List<Item> listaNazioni_totale() {
        List<String> cm = this.em.createNamedQuery("nazioni_rc.NazioniTotale", String.class).getResultList();
        List<Item> out = new ArrayList<>();
        for (String c : cm) {
            out.add(new Item(c.toUpperCase(), c.toUpperCase() + " (STATO ESTERO)", ""));
        }
        return out;
    }

    public List<Nazioni_rc> listaNazioni_rc() {
        TypedQuery<Nazioni_rc> q = this.em.createNamedQuery("nazioni_rc.cf", Nazioni_rc.class);
        return (List<Nazioni_rc>) q.getResultList();
    }

    public ArrayList<Item> listaProvince(String regione) {
        TypedQuery<String> q = this.em.createNamedQuery("comuni.Provincia", String.class);
        q.setParameter("regione", regione);
        List<String> provincie = q.getResultList();
        ArrayList<Item> out = new ArrayList<>();
        for (String p : provincie) {
            out.add(new Item(p, p, ""));
        }
        return out;

    }

    public ArrayList<Item> listaComuni(String provincia) {
        TypedQuery<Comuni> q = this.em.createNamedQuery("comuni.Comune", Comuni.class);
        q.setParameter("provincia", provincia);
        List<Comuni> comuni = q.getResultList();
        ArrayList<Item> out = new ArrayList<>();
        for (Comuni c : comuni) {
            out.add(new Item(String.valueOf(c.getId()), c.getNome(), ""));
        }
        return out;
    }

    public List<Comuni> listaComunibyRegione(String regione) {
        TypedQuery<Comuni> q = this.em.createNamedQuery("comuni.byRegione", Comuni.class);
        q.setParameter("regione", regione);
        return (List<Comuni>) q.getResultList();
    }

    public List<Comuni> listaComunibyProvincia(String provincia) {
        TypedQuery<Comuni> q = this.em.createNamedQuery("comuni.Comune", Comuni.class);
        q.setParameter("provincia", provincia);
        return (List<Comuni>) q.getResultList();
    }

    public Comuni getComunibyIstat(Nazioni_rc naz) {
        Comuni c1 = new Comuni();
        c1.setId(0L);
        c1.setCittadinanza(1);
        c1.setCod_comune("0");
        c1.setCod_provincia("0");
        c1.setCod_regione("0");
        c1.setCodicicatastali_altri(null);
        c1.setIstat(naz.getCodicefiscale());
        c1.setNome(naz.getNome());
        c1.setNome_provincia(naz.getNome());
        c1.setProvincia(naz.getUe());
        c1.setRegione(naz.getNome());
        return c1;
    }

    public List<TitoliStudio> listaTitoliStudio() {
        TypedQuery<TitoliStudio> q = this.em.createNamedQuery("titoli_studio.Elenco", TitoliStudio.class);
        return q.getResultList();
    }

    public List<CPI> listaCPI() {
        TypedQuery<CPI> q = this.em.createNamedQuery("cpi.Elenco", CPI.class);
        return q.getResultList();
    }

    public User getUser(String user, String pwd) {
        TypedQuery<User> q = em.createNamedQuery("user.UsernamePwd", User.class);
        q.setParameter("username", user);
        q.setParameter("password", convMd5(pwd));
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public SoggettiAttuatori getUserPiva(String piva) {
        TypedQuery<SoggettiAttuatori> q = em.createNamedQuery("sa.byPiva", SoggettiAttuatori.class);
        q.setParameter("piva", piva);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public SoggettiAttuatori getUserEmail(String email) {
        TypedQuery<SoggettiAttuatori> q = em.createNamedQuery("sa.byEmail", SoggettiAttuatori.class);
        q.setParameter("email", email);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public SoggettiAttuatori getUserCF(String cf) {
        TypedQuery<SoggettiAttuatori> q = em.createNamedQuery("sa.byCF", SoggettiAttuatori.class);
        q.setParameter("codicefiscale", cf);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public String getLAST_CIP() {
        TypedQuery<ProgettiFormativi> q = em.createNamedQuery("progetti.cip", ProgettiFormativi.class);
        q.setMaxResults(1);
        String cipbase = q.getResultList().isEmpty() ? null : q.getSingleResult().getCip();
        if (cipbase == null) {
            return "2023ENM0001";
        } else {
            String id = cipbase.replaceAll("2023ENM", "").trim();
            try {
                int id_1 = Utility.parseInt(id) + 1;
                return "2023ENM" + StringUtils.leftPad(String.valueOf(id_1), 4, "0");
            } catch (Exception e) {
                return "2023ENM0001";
            }
        }
    }

    public User getUserbyUsername(String username) {
        TypedQuery<User> q = em.createNamedQuery("user.byUsername", User.class);
        q.setParameter("username", username);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public User getUserbyUsernameToken(String username, String token) {
        TypedQuery<User> q = em.createNamedQuery("user.byUsernameToken", User.class);
        q.setParameter("username", username);
        q.setParameter("token", token);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }

    }

    public User getUserbyEmail(String email) {
        TypedQuery<User> q = em.createNamedQuery("user.byEmail", User.class);
        q.setParameter("email", email);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public User getUserbySA(SoggettiAttuatori sa) {
        TypedQuery<User> q = em.createNamedQuery("user.bySA", User.class);
        q.setParameter("sa", sa);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public Email getEmail(String chiave) {
        return this.em.find(Email.class, chiave);
    }

    public List<SoggettiAttuatori> getSoggettiAttuatori(String ragionesociale,
            String protocollo, String piva, String cf, String protocollare, String nome,
            String cognome) {
        HashMap<String, Object> param = new HashMap<>();
        String sql = "SELECT sa FROM SoggettiAttuatori sa ";

        if (!ragionesociale.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "sa.ragionesociale LIKE :ragionesociale";
            param.put("ragionesociale", ragionesociale + "%");
        }
        if (!piva.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "sa.piva=:piva";
            param.put("piva", piva);
        }
        if (!cf.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "sa.codicefiscale=:codicefiscale";
            param.put("codicefiscale", cf);
        }
        if (!protocollo.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "sa.protocollo=:protocollo";
            param.put("protocollo", protocollo);
        }

//        if (!protocollare.equals("") && protocollo.equals("")) {
//            if (!protocollare.equals("")) {
//                sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
//                sql += "sa.protocollo ";
//                if (protocollare.equals("1")) {
//                    sql += "IS NULL";
//                } else if (protocollare.equals("0")) {
//                    sql += "IS NOT NULL";
//                }
//            }
//        }
        if (!nome.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "sa.nome LIKE :nome";
            param.put("nome", nome + "%");
        }
        if (!cognome.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "sa.cognome LIKE :cognome";
            param.put("cognome", cognome + "%");
        }

        TypedQuery<SoggettiAttuatori> q = this.em.createQuery(sql, SoggettiAttuatori.class);

        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }

        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }

        return q.getResultList();
    }

    public Allievi getAllievoCF(String cf) {
        TypedQuery<Allievi> q = this.em.createNamedQuery("a.byCF", Allievi.class);
        q.setParameter("codicefiscale", cf);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public boolean updateuserTipo(int tipo, SoggettiAttuatori sa) {
        return this.em.createNamedQuery("user.updateTipo")
                .setParameter("tipo", tipo)
                .setParameter("soggettoattuatore", sa).executeUpdate() > 0;
    }

    public List<Allievi> getAllieviSoggetto(SoggettiAttuatori sa) {
        TypedQuery<Allievi> q = em.createNamedQuery("a.bySoggettoAttuatore", Allievi.class)
                .setParameter("soggetto", sa);
        return q.getResultList();
    }

    public List<Allievi> getAllieviSoggettoNoPrgAttivi(SoggettiAttuatori sa) {
        TypedQuery<Allievi> q = em.createNamedQuery("allievi.assegnatisoggetto", Allievi.class)
                .setParameter("soggetto", sa);
        return q.getResultList();
    }

    public List<Allievi> getAllieviNuovoModello1(SoggettiAttuatori sa) {
        TypedQuery<Allievi> q = em.createNamedQuery("allievi.nuovomodello1", Allievi.class)
                .setParameter("soggetto", sa);
        return q.getResultList();
    }

    public List<Allievi> getAllieviSoggettoModello1(SoggettiAttuatori sa) {
        TypedQuery<Allievi> q = em.createNamedQuery("allievi.modello1", Allievi.class)
                .setParameter("soggetto", sa);
        return q.getResultList();
    }

    public List<Allievi> getAllieviSA(String nome, String cognome, String cf, String stato, CPI cpi, SoggettiAttuatori sa) {
        HashMap<String, Object> param = new HashMap<>();

        String sql = "SELECT a FROM Allievi a ";

        if (!nome.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.nome LIKE :nome";
            param.put("nome", nome + "%");
        }
        if (!cognome.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.cognome LIKE :cognome";
            param.put("cognome", cognome + "%");
        }
        if (!cf.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.codicefiscale=:codicefiscale";
            param.put("codicefiscale", cf);
        }
        if (!stato.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.stato=:stato";
            param.put("stato", stato);
        }
        if (cpi != null) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.cpi=:cpi";
            param.put("cpi", cpi);
        }
        if (sa != null) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.soggetto=:soggetto";
            param.put("soggetto", sa);
        }

        TypedQuery<Allievi> q = this.em.createQuery(sql, Allievi.class);

        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }

        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }

        return q.getResultList();
    }

    public List<Docenti> getDocenti(String nome, String cognome, String cf, SoggettiAttuatori sa) {
        HashMap<String, Object> param = new HashMap<>();

        String sql = "SELECT a FROM Docenti a WHERE a.soggetto=:soggetto ";
        param.put("soggetto", sa);

        if (!nome.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.nome LIKE :nome";
            param.put("nome", nome + "%");
        }
        if (!cognome.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.cognome LIKE :cognome";
            param.put("cognome", cognome + "%");
        }
        if (!cf.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.codicefiscale=:codicefiscale";
            param.put("codicefiscale", cf);
        }

        TypedQuery<Docenti> q = this.em.createQuery(sql, Docenti.class);

        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }

        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }
        return q.getResultList();

    }

    public List<Docenti> getDocenti(String nome, String cognome, String cf) {
        HashMap<String, Object> param = new HashMap<>();

        String sql = "SELECT a FROM Docenti a ";

        if (!nome.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.nome LIKE :nome";
            param.put("nome", nome + "%");
        }
        if (!cognome.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.cognome LIKE :cognome";
            param.put("cognome", cognome + "%");
        }
        if (!cf.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.codicefiscale=:codicefiscale";
            param.put("codicefiscale", cf);
        }

        TypedQuery<Docenti> q = this.em.createQuery(sql, Docenti.class);

        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }

        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }
        return q.getResultList();
    }

    public List<Allievi> getAllievidaAssegnare() {
        TypedQuery<Allievi> q = this.em.createNamedQuery("allievi.daassegnare", Allievi.class);
        return q.getResultList();
    }

    public List<String> elencoOperatori() {
        return Splitter.on(",").splitToList(getPath("elenco.operatori"));
    }

    public List<Allievi> getAllievi(SoggettiAttuatori sa, CPI cpi, String nome, String cognome, String cf) {
        HashMap<String, Object> param = new HashMap<>();

        String sql = "SELECT a FROM Allievi a WHERE a.statopartecipazione.id<>'10' AND a.statopartecipazione.id<>'00' ";

        if (sa != null) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.soggetto = :soggetto";
            param.put("soggetto", sa);
        }
        if (cpi != null) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.cpi=:cpi";
            param.put("cpi", cpi);
        }
        if (!nome.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.nome LIKE :nome";
            param.put("nome", nome + "%");
        }
        if (!cognome.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.cognome LIKE :cognome";
            param.put("cognome", cognome + "%");
        }
        if (!cf.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.codicefiscale = :codicefiscale";
            param.put("codicefiscale", cf);
        }

        TypedQuery<Allievi> q = this.em.createQuery(sql, Allievi.class);

        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }

        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }

        return q.getResultList();
    }

    public List<ProgettiFormativi> getProgettiFormativi(SoggettiAttuatori sa, List<StatiPrg> stati, String cip) {
        return getProgettiFormativi(sa, stati, cip, "");
    }

    public List<ProgettiFormativi> getProgettiFormativi(SoggettiAttuatori sa, List<StatiPrg> stati, String cip, String rendicontato) {
        HashMap<String, Object> param = new HashMap<>();

        String sql = "SELECT a FROM ProgettiFormativi a ";

        if (sa != null) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.soggetto = :soggetto";
            param.put("soggetto", sa);
        }
        if (stati != null && !stati.isEmpty()) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.stato IN :stati";
            param.put("stati", stati);
        }

        if (!cip.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.cip LIKE :cip";
            param.put("cip", cip + "%");
        }

        if (!rendicontato.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.rendicontato = :rendicontato";
            param.put("rendicontato", Integer.valueOf(rendicontato));
        }

        sql += " ORDER BY a.timestamp DESC";//ordina per ultima modifica al progetto
        TypedQuery<ProgettiFormativi> q = this.em.createQuery(sql, ProgettiFormativi.class);
        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }

        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }
        return q.getResultList();
    }

    public List<ProgettiFormativi> getProgettiFormativiDocenti(Docenti d) {
        TypedQuery<ProgettiFormativi> q = em.createNamedQuery("progetti.ProgettiDocente", ProgettiFormativi.class);
        q.setParameter("docente", d);
        return q.getResultList();
    }

    public List<Allievi> getAllieviProgettiFormativi(ProgettiFormativi p) {
        TypedQuery<Allievi> q = em.createNamedQuery("a.byProgetto", Allievi.class);
        q.setParameter("progetto", p);
        return q.getResultList();
    }

    public List<Allievi> getAllieviProgettiFormativiAll(ProgettiFormativi p) {
        TypedQuery<Allievi> q = em.createNamedQuery("a.byProgettoAll", Allievi.class);
        q.setParameter("progetto", p);
        return q.getResultList();
    }

    public List<StatiPrg> getStatiPrg() {
        return (List<StatiPrg>) this.em.createNamedQuery("statiPrg.Tipo").getResultList();
    }

    public List<StatiPrg> getStatiPrg_R() {
        return (List<StatiPrg>) this.em.createNamedQuery("statiPrg.TipoR").getResultList();
    }

    public List<StatiPrg> getStatibyTipo(String tipo) {
        return this.em.createNamedQuery("statiPrg.ByTipo",
                StatiPrg.class)
                .setParameter("tipo", tipo)
                .getResultList();
    }

    public List<StatiPrg> getStatibyDescrizione(String descrizione) {
        return this.em.createNamedQuery("statiPrg.ByDescrizione", StatiPrg.class)
                .setParameter("descrizione", descrizione)
                .getResultList();
    }

    public String getProvinceSediFormazione(SoggettiAttuatori soggetto) {

        HashMap<String, Object> param = new HashMap<>();
        String sql = "SELECT a FROM SediFormazione a WHERE a.soggetto=:soggetto AND (a.stato='A' OR a.stato = 'A1')";
        param.put("soggetto", soggetto);
        TypedQuery<SediFormazione> q = this.em.createQuery(sql, SediFormazione.class);
        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }
        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }

        List<String> province = new ArrayList<>();
        for (SediFormazione sf : q.getResultList()) {
            province.add(sf.getComune().getNome_provincia().trim());
        }
        return province.stream().distinct().collect(Collectors.toList()).toString();
    }

    public List<SediFormazione> getSediFormazione(HttpSession session) {

        User us = (User) session.getAttribute("user");
        HashMap<String, Object> param = new HashMap<>();
        String sql = "SELECT a FROM SediFormazione a WHERE a.soggetto=:soggetto AND (a.stato='A' OR a.stato = 'A1')";
        param.put("soggetto", us.getSoggettoAttuatore());
        TypedQuery<SediFormazione> q = this.em.createQuery(sql, SediFormazione.class);
        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }
        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }
        return q.getResultList();
    }

    public List<SediFormazione> getSediFormazione(SoggettiAttuatori soggetto) {

        HashMap<String, Object> param = new HashMap<>();
        String sql = "SELECT a FROM SediFormazione a WHERE a.soggetto=:soggetto AND (a.stato='A' OR a.stato = 'A1')";
        param.put("soggetto", soggetto);
        TypedQuery<SediFormazione> q = this.em.createQuery(sql, SediFormazione.class);
        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }

        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }
        return q.getResultList();
    }

    public List<SediFormazione> getSediFormazione(String referente, List<Comuni> comuni) {
        HashMap<String, Object> param = new HashMap<>();

        String sql = "SELECT a FROM SediFormazione a ";

        if (!comuni.isEmpty()) {
            if (comuni.size() == 1) {
                sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
                sql += "a.comune =:comune";
                param.put("comune", comuni.get(0));
            } else {
                sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
                sql += "a.comune IN :comune";
                param.put("comune", comuni);
            }
        }

        if (!referente.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.referente LIKE :referente";
            param.put("referente", "%" + referente + "%");
        }

        TypedQuery<SediFormazione> q = this.em.createQuery(sql, SediFormazione.class);

        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }

        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }
        return q.getResultList();
    }

    public List<Docenti> getActiveDocenti() {
        TypedQuery<Docenti> q = this.em.createNamedQuery("d.Active", Docenti.class);
        return q.getResultList();
    }

    public List<Docenti> getActiveDocenti(HttpSession session) {
        User us = (User) session.getAttribute("user");
        TypedQuery<Docenti> q = this.em.createNamedQuery("d.bySA", Docenti.class)
                .setParameter("soggetto", us.getSoggettoAttuatore());
        return q.getResultList();
    }

    public List<Docenti> getActiveDocenti(SoggettiAttuatori sa) {
        TypedQuery<Docenti> q = this.em.createNamedQuery("d.bySA", Docenti.class)
                .setParameter("soggetto", sa);
        return q.getResultList();
    }

    public List<Docenti> getAllocenti() {
        TypedQuery<Docenti> q = this.em.createNamedQuery("d.All", Docenti.class);
        return q.getResultList();
    }

    public List<TipoDoc> getTipoDoc(StatiPrg stato) {
        TypedQuery<TipoDoc> q = this.em.createNamedQuery("t.byStato", TipoDoc.class)
                .setParameter("stato", stato);
        return q.getResultList();
    }

    public List<TipoDoc> getTipoDocObbl(StatiPrg stato) {
        TypedQuery<TipoDoc> q = this.em.createNamedQuery("t.byStatoObbligatori", TipoDoc.class)
                .setParameter("stato", stato);
        return q.getResultList();
    }

    public List<DocumentiPrg> getDocPrg(ProgettiFormativi progetto) {
        TypedQuery<DocumentiPrg> q = this.em.createNamedQuery("d.byPrg", DocumentiPrg.class)
                .setParameter("progetto", progetto);
        return q.getResultList();
    }

    public List<DocumentiPrg> getregisterPrg(ProgettiFormativi progetto) {
        TypedQuery<DocumentiPrg> q = this.em.createNamedQuery("d.registriByPrg", DocumentiPrg.class)
                .setParameter("progetto", progetto);
        return q.getResultList();
    }

    public List<DocumentiPrg> getregisterPrgDay(ProgettiFormativi progetto, Date giorno) {
        TypedQuery<DocumentiPrg> q = this.em.createNamedQuery("d.registriByPrgAndDay", DocumentiPrg.class)
                .setParameter("progetto", progetto)
                .setParameter("giorno", giorno);
        return q.getResultList();
    }

    public List<DocumentiPrg> getDocIdModifiableDocente(SoggettiAttuatori soggetto, Docenti docente) {
        TypedQuery<DocumentiPrg> q = this.em.createNamedQuery("d.getDocIdModifiableDocente", DocumentiPrg.class)
                .setParameter("soggetto", soggetto)
                .setParameter("docente", docente);
        return q.getResultList();
    }

    public List<Docenti> getDocentiPrg(ProgettiFormativi p) {
        TypedQuery<Docenti> q = this.em.createNamedQuery("d.byProgetto", Docenti.class);
        q.setParameter("progetto", p);
        return q.getResultList();
    }

    public boolean deleteDocPrg(ProgettiFormativi p, Docenti d) {
        return this.em.createNamedQuery("d.deleteDocDocente")
                .setParameter("progetto", p)
                .setParameter("docente", d).executeUpdate() > 0;
    }

    public List<Storico_Prg> getStoryPrg(ProgettiFormativi p) {
        TypedQuery<Storico_Prg> q = this.em.createNamedQuery("storico.byPrg", Storico_Prg.class);
        q.setParameter("progetto", p);
        return q.getResultList();
    }

    public StatiPrg getStatiByOrdineProcesso(int ordine) {
        TypedQuery<StatiPrg> q = this.em.createNamedQuery("statiPrg.ByOrdinePocesso", StatiPrg.class);
        q.setParameter("ordine", ordine);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public List<TipoDoc_Allievi> getTipoDocAllievi(StatiPrg stato) {
        TypedQuery<TipoDoc_Allievi> q = this.em.createNamedQuery("tipo_doc_a.byStato", TipoDoc_Allievi.class)
                .setParameter("stato", stato);
        return q.getResultList();
    }

    public List<TipoDoc_Allievi> getTipoDocAllievi_ALL(StatiPrg stato) {
        TypedQuery q = this.em.createNamedQuery("tipo_doc_a.byStatoALL", TipoDoc_Allievi.class)
                .setParameter("stato", stato);
        return q.getResultList();
    }

    public List<Documenti_Allievi> getDocAllievo(Allievi a) {
        TypedQuery<Documenti_Allievi> q = this.em.createNamedQuery("da.byAllievo", Documenti_Allievi.class)
                .setParameter("allievo", a);
        return q.getResultList();
    }

    public List<Documenti_Allievi> getDocAllievoAgg(Allievi a) {
        TypedQuery<Documenti_Allievi> q = this.em.createNamedQuery("da.byAllievo", Documenti_Allievi.class)
                .setParameter("allievo", a);

        return q.getResultList();
    }

    public Documenti_Allievi getModello0Allievo(Allievi a) {
        TypedQuery<Documenti_Allievi> q = this.em.createNamedQuery("da.modello0", Documenti_Allievi.class)
                .setParameter("allievo", a);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public List<TipoDoc_Allievi> getTipoDocAllieviObbl(StatiPrg stato) {
        TypedQuery<TipoDoc_Allievi> q = this.em.createNamedQuery("tipo_doc_a.byStatoObbligatori", TipoDoc_Allievi.class)
                .setParameter("stato", stato);
        return q.getResultList();
    }

    public List<DocumentiPrg> getRegistriDay(ProgettiFormativi p, Date d) {
        TypedQuery<DocumentiPrg> q = this.em.createNamedQuery("d.getRegisterToday", DocumentiPrg.class)
                .setParameter("progetto", p)
                .setParameter("date", d);
        return q.getResultList();
    }

    public List<Selfiemployment_Prestiti> listaSE_P() {
        TypedQuery<Selfiemployment_Prestiti> q = this.em.createNamedQuery("se_p.Elenco", Selfiemployment_Prestiti.class);
        return q.getResultList();
    }

    public List<StatoPartecipazione> lista_StatoPartecipazione() {
        TypedQuery<StatoPartecipazione> q = this.em.createNamedQuery("sp.Elenco", StatoPartecipazione.class);
        return q.getResultList();
    }

    public List<StatoPartecipazione> lista_StatoPartecipazioneMOD() {
        TypedQuery<StatoPartecipazione> q = this.em.createNamedQuery("sp.modificaincorso", StatoPartecipazione.class);
        return q.getResultList();
    }

    public Allievi getAllievoEmail(String email) {
        TypedQuery<Allievi> q = this.em.createNamedQuery("a.byEmail", Allievi.class);
        q.setParameter("email", email);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public List<ProgettiFormativi> ProgettiSAOrdered(SoggettiAttuatori soggetto) {
        TypedQuery<ProgettiFormativi> q = this.em.createNamedQuery("progetti.ProgettiSAOrdered", ProgettiFormativi.class)
                .setParameter("soggetto", soggetto);
        return q.getResultList();
    }

    public List<ProgettiFormativi> ProgettiSA_Fa(SoggettiAttuatori soggetto) {
        TypedQuery<ProgettiFormativi> q = this.em.createNamedQuery("progetti.ProgettiSA_Fa", ProgettiFormativi.class)
                .setParameter("soggetto", soggetto);
        return q.getResultList();
    }

    public List<Documenti_Allievi> getRegistriAllievi(List<Allievi> allievi) {
        TypedQuery<Documenti_Allievi> q = this.em.createNamedQuery("da.registriByAllievi", Documenti_Allievi.class)
                .setParameter("allievi", allievi);
        return q.getResultList();
    }

    public List<SoggettiAttuatori> getSoggettiAttuatori() {
        TypedQuery<SoggettiAttuatori> q = this.em.createNamedQuery("sa.listaSA", SoggettiAttuatori.class);
        return q.getResultList();
    }

    public List<ProgettiFormativi> getProgettiDaRendicontare() {
        TypedQuery<ProgettiFormativi> q = this.em.createNamedQuery("progetti.toRend", ProgettiFormativi.class);
        return q.getResultList();
    }

    public List<Presenze_Lezioni_Allievi> getPresenzeLezioniAllievi_PR(Allievi allievo) {
        TypedQuery<Presenze_Lezioni_Allievi> q = this.em.createNamedQuery("presenzelezioni.allievo", Presenze_Lezioni_Allievi.class);
        q.setParameter("allievo", allievo);
        return q.getResultList();
    }

    public List<Presenze_Lezioni_Allievi> getPresenzeLezioniAllievi(Allievi allievo) {
        TypedQuery<Presenze_Lezioni_Allievi> q = this.em.createNamedQuery("presenzelezioni.allievototal", Presenze_Lezioni_Allievi.class);
        q.setParameter("allievo", allievo);
        return q.getResultList();
    }

    public List<Estrazioni> getRendicontazioni() {
        TypedQuery<Estrazioni> q = this.em.createNamedQuery("estrazioni.rendicontazione", Estrazioni.class).setMaxResults(maxQueryResult);
        return q.getResultList();
    }

    public List<Estrazioni> getEstazioniDesc() {
        TypedQuery<Estrazioni> q = this.em.createNamedQuery("estrazioni.timestampDesc", Estrazioni.class).setMaxResults(maxQueryResult);
        List<Estrazioni> result = q.getResultList();
        result.forEach(r1 -> {
            r1.setVisualTime(new DateTime(r1.getTimestamp().getTime()).toDateTime(dtz_italy).toString(Utility.patternITACOMPLETE));
        });
        return result;
    }

    public Docenti getDocenteByCf(String cf) {
        TypedQuery<Docenti> q = this.em.createNamedQuery("d.byCf", Docenti.class);
        q.setParameter("cf", cf);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public HashMap<String, FasceDocenti> getFasceMap() {
        List<FasceDocenti> fasce = this.findAll(FasceDocenti.class);
        HashMap<String, FasceDocenti> out = new HashMap<>();
        fasce.forEach(f -> {
            out.put(f.getId(), f);
        });
        return out;
    }

    public HashMap<String, String> getUsersSA() {
        List<User> users = this.findAll(User.class);
        HashMap<String, String> out = new HashMap();
        users.forEach(f -> {
            out.put(f.getUsername(), f.getUsername());
        });
        return out;
    }

    public String getCodiceCatastaleComune(Long idcomune) {
        String code = this.em.find(Comuni.class, idcomune) == null ? "-" : this.em.find(Comuni.class, idcomune).getIstat();
        return code;
    }

    public Comuni getComune(Long idcomune) {
        return this.em.find(Comuni.class, idcomune);
    }

    public TipoDoc getsingleTipoDoc(Long iddoc) {
        return this.em.find(TipoDoc.class, iddoc);
    }

    public List<Storico_ModificheInfo> getPec(SoggettiAttuatori sa) {
        TypedQuery<Storico_ModificheInfo> q = em.createNamedQuery("storicomodifiche.bySA", Storico_ModificheInfo.class);
        q.setParameter("soggetto", sa);
        return q.getResultList();
    }

    public List<DocumentiPrg> getRegisterProgetto_by_Day(Date date, ProgettiFormativi p) {
        TypedQuery<DocumentiPrg> q = em.createNamedQuery("d.getRegisterByDayAndSA", DocumentiPrg.class)
                .setParameter("progetto", p)
                .setParameter("date", date);
        return q.getResultList();
    }

    public Documenti_Allievi registriByAllievoAndDay(Allievi a, Date giorno) {
        TypedQuery<Documenti_Allievi> q = this.em.createNamedQuery("da.registriByAllievoAndDay", Documenti_Allievi.class)
                .setParameter("allievo", a)
                .setParameter("giorno", giorno);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public Long countProgettiConclusi() {
        TypedQuery<Long> q = this.em.createNamedQuery("progetti.countConclusi", Long.class);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public List<Documenti_Allievi_Pregresso> getDoc_Pregresso(Allievi_Pregresso a) {
        TypedQuery<Documenti_Allievi_Pregresso> q = em.createNamedQuery("docP.byAllievo", Documenti_Allievi_Pregresso.class)
                .setParameter("allievo", a);
        return q.getResultList();
    }

    public List<Faq> getFaqSoggetto(SoggettiAttuatori s) {
        TypedQuery<Faq> q = em.createNamedQuery("faq.BySoggetto", Faq.class)
                .setParameter("soggetto", s)
                .setMaxResults(maxQueryResult);
        return q.getResultList();
    }

    public List<Faq> getFaqSoggetto(SoggettiAttuatori s, int offset, int limit) {
        TypedQuery<Faq> q = em.createNamedQuery("faq.BySoggetto", Faq.class)
                .setParameter("soggetto", s)
                .setMaxResults(limit)//limit
                .setFirstResult(offset);//offset
        return q.getResultList();
    }

    public List<Faq> getFaqSoggetti() {
        TypedQuery<Faq> q = em.createNamedQuery("faq.SA", Faq.class);//offset
        return q.getResultList();
    }

    public List<Faq> getFaqPublic() {
        TypedQuery<Faq> q = em.createNamedQuery("faq.Public", Faq.class);//offset
        return q.getResultList();
    }

    public List<Faq> faqNoAnswer() {
        TypedQuery<Faq> q = em.createNamedQuery("faq.NoAnswer", Faq.class);//offset
        return q.getResultList();
    }

    public Faq getLastFaqSoggetto(SoggettiAttuatori s) {
        TypedQuery<Faq> q = em.createNamedQuery("faq.LastBySoggetto", Faq.class)
                .setParameter("soggetto", s);//offset
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public List<Faq> faqAnswer(SoggettiAttuatori s, TipoFaq tipo) {

        HashMap<String, Object> param = new HashMap<>();

        String sql = "SELECT f FROM Faq f WHERE f.risposta IS NOT NULL ";

        if (s != null) {
            sql += "AND f.soggetto =:soggetto ";
            param.put("soggetto", s);
        }

        if (tipo != null) {
            sql += "AND f.tipo=:tipo ";
            param.put("tipo", tipo);
        }

        sql += "ORDER BY f.timestamp DESC";

        TypedQuery<Faq> q = this.em.createQuery(sql, Faq.class);

        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }

        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }

        return q.getResultList();
    }

    public List<FadMicro> getMyConference(User u, String nomestanza, String stato) {

        HashMap<String, Object> param = new HashMap<>();

        String sql = "SELECT f FROM FadMicro f WHERE f.user=:user AND f.stato<>2 ";
        if (!stato.trim().isEmpty()) {
            sql = "SELECT f FROM FadMicro f WHERE f.user=:user AND f.stato=:stato ";
            param.put("stato", Integer.valueOf(stato));
        }
        param.put("user", u);

        if (nomestanza != null) {
            sql += "AND f.nomestanza LIKE :nomestanza ";
            param.put("nomestanza", "%" + nomestanza + "%");
        }

        sql += "ORDER BY f.datacreazione DESC";

        TypedQuery<FadMicro> q = this.em.createQuery(sql, FadMicro.class);

        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }

        return q.getResultList();
    }

    public Long countFAQ() {
        TypedQuery<Long> q = this.em.createNamedQuery("faq.count", Long.class);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public List<Attivita> getAttivitaValide() {
        TypedQuery<Attivita> q = em.createNamedQuery("attivita.Valide", Attivita.class);//offset
        return q.getResultList();
    }

    public List<Attivita> getAttivita(String nome, List<Comuni> comuni) {
        HashMap<String, Object> param = new HashMap<>();

        String sql = "SELECT a FROM Attivita a ";

        if (!comuni.isEmpty()) {
            if (comuni.size() == 1) {
                sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
                sql += "a.comune =:comune";
                param.put("comune", comuni.get(0));
            } else {
                sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
                sql += "a.comune IN :comune";
                param.put("comune", comuni);
            }
        }

        if (!nome.equals("")) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "a.name LIKE :nome";
            param.put("nome", "%" + nome + "%");
        }

        TypedQuery<Attivita> q = this.em.createQuery(sql, Attivita.class);

        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }

        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }
        return q.getResultList();
    }

    public List<Cad> getCad(Date giorno, User u) {
        HashMap<String, Object> param = new HashMap<>();

        String sql = "SELECT c FROM Cad c WHERE c.stato<>2";

        if (giorno != null) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "c.giorno=:giorno";
            param.put("giorno", giorno);
        }
        if (u != null) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "c.user=:user";
            param.put("user", u);
        }

        TypedQuery<Cad> q = this.em.createQuery(sql, Cad.class);

        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }
        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }
        return q.getResultList();
    }

    public List<Cad> getCadFromDate(Date giorno, User u) {
        HashMap<String, Object> param = new HashMap<>();

        String sql = "SELECT c FROM Cad c WHERE c.stato<>2";//stato 0 Aperto 1 Chiusa 2 Eliminata

        if (giorno != null) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "c.giorno>=:giorno";
            param.put("giorno", giorno);
        }
        if (u != null) {
            sql += !sql.toUpperCase().contains("WHERE") ? "WHERE " : " AND ";
            sql += "c.user=:user";
            param.put("user", u);
        }

        TypedQuery<Cad> q = this.em.createQuery(sql, Cad.class);

        if (param.isEmpty()) {
            q.setMaxResults(maxQueryResult);
        }
        for (HashMap.Entry<String, Object> m : param.entrySet()) {
            q.setParameter(m.getKey(), m.getValue());
        }
        return q.getResultList();
    }

    public List<UnitaDidattiche> getUD() {
        TypedQuery<UnitaDidattiche> q = em.createNamedQuery("unita_didattiche.Elenco", UnitaDidattiche.class);
        return q.getResultList();
    }

    public List<String> getFasi_UD() {
        TypedQuery<String> q = em.createNamedQuery("unita_didattiche.Fasi", String.class);
        return q.getResultList();
    }

    public List<LezioneCalendario> getLezioniByModello(int modello) {
        TypedQuery<LezioneCalendario> q = em.createNamedQuery(("lezioni.byModello"), LezioneCalendario.class)
                .setParameter("modello", modello);
        return q.getResultList();
    }

    public List<Lezioni_Modelli> getLezioniByProgetto(ModelliPrg m) {
        TypedQuery<Lezioni_Modelli> q = em.createNamedQuery("lezmod.ByModello", Lezioni_Modelli.class)
                .setParameter("modello", m);
        return q.getResultList();
    }

    public Docenti getDocenteByEmail(String email, SoggettiAttuatori s) {
        TypedQuery<Docenti> q = this.em.createNamedQuery("d.byEmail", Docenti.class);
        q.setParameter("email", email).setParameter("soggetto", s);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            re.printStackTrace();
            return null;
        }
    }

    public Docenti getDocenteByCF_SA(String cf, SoggettiAttuatori s) {
        TypedQuery<Docenti> q = this.em.createNamedQuery("d.byCf_SA", Docenti.class);
        q.setParameter("cf", cf).setParameter("soggetto", s);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public List<Docenti> getActiveDocenti_bySA(SoggettiAttuatori s) {
        TypedQuery<Docenti> q = this.em.createNamedQuery("d.bySA_Active", Docenti.class).setParameter("soggetto", s);
        return q.getResultList();
    }

    public List<MascheraM5> getM5Loaded_byPF(ProgettiFormativi pf) {
        TypedQuery<MascheraM5> q = this.em.createNamedQuery("m5.byPF", MascheraM5.class).setParameter("progetto_formativo", pf);
        return q.getResultList();
    }

    public MascheraM5 getM5_byAllievo(Allievi a1) {
        TypedQuery<MascheraM5> q = this.em.createNamedQuery("m5.byAllievo", MascheraM5.class).setParameter("allievo", a1);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public List<Ateco> list_CodiciAteco() {
        TypedQuery<Ateco> q = this.em.createNamedQuery("ate.Elenco", Ateco.class);
        return q.getResultList().isEmpty() ? new ArrayList() : (List<Ateco>) q.getResultList();
    }

    public List<Cloud> getCloudFilesByTipo(int tipo) {
        TypedQuery<Cloud> q = em.createNamedQuery(("cloud.byTipo"), Cloud.class)
                .setParameter("tipo", tipo);
        return q.getResultList();
    }

    public checklist_finale getCheckListByPf(ProgettiFormativi p) {
        TypedQuery<checklist_finale> q = this.em.createNamedQuery("cl.byPF", checklist_finale.class).setParameter("progetto_formativo", p);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public Nazioni_rc byCodiceFiscale(String cf) {
        TypedQuery<Nazioni_rc> q = this.em.createNamedQuery("nazioni_rc.byCodiceFiscale", Nazioni_rc.class);
        q.setParameter("codicefiscale", cf);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public Nazioni_rc nazionenascita(String statonascita) {
        TypedQuery<Nazioni_rc> q = this.em.createNamedQuery("nazioni_rc.byIstat", Nazioni_rc.class);
        q.setParameter("istat", statonascita);
        if (q.getResultList().isEmpty()) {
            q = this.em.createNamedQuery("nazioni_rc.byCodiceFiscale", Nazioni_rc.class);
            q.setParameter("codicefiscale", statonascita);
        }
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public Comuni byIstatEstero(String istat) {
        TypedQuery<Comuni> q = this.em.createNamedQuery("comuni.byIstatEstero", Comuni.class);
        q.setParameter("istat", istat);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public Presenze_Lezioni getPresenzeLezione(Lezioni_Modelli lezioneriferimento) {
        TypedQuery<Presenze_Lezioni> q = this.em.createNamedQuery("presenzelezioni.lezioni", Presenze_Lezioni.class);
        q.setParameter("lezioneriferimento", lezioneriferimento);
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public Presenze_Lezioni getPresenzeLezione(Long idcalendar) {
        TypedQuery<Presenze_Lezioni> q = this.em.createNamedQuery("presenzelezioni.lezioni", Presenze_Lezioni.class);
        q.setParameter("lezioneriferimento", this.em.find(Lezioni_Modelli.class, idcalendar));
        q.setMaxResults(1);
        try {
            return q.getResultList().isEmpty() ? null : q.getSingleResult();
        } catch (NoResultException re) {
            return null;
        }
    }

    public List<Presenze_Lezioni_Allievi> getpresenzelezioniGiornata(Presenze_Lezioni pl) {
        TypedQuery<Presenze_Lezioni_Allievi> q = this.em.createNamedQuery("presenzelezioni.giornata", Presenze_Lezioni_Allievi.class);
        q.setParameter("presenzelezioni", pl);
        return q.getResultList();
    }

}
