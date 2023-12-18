/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import static com.google.common.base.Splitter.on;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.constants.StandardFonts;
import static com.itextpdf.kernel.colors.ColorConstants.BLACK;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import rc.so.db.Action;
import static rc.so.db.Action.insertTR;
import rc.so.db.Database;
import rc.so.db.FileDownload;
import rc.so.domain.Allievi;
import rc.so.domain.Docenti;
import rc.so.domain.DocumentiPrg;
import rc.so.domain.Documenti_Allievi;
import rc.so.domain.Documenti_Allievi_Pregresso;
import rc.so.domain.Documenti_UnitaDidattiche;
import rc.so.domain.LezioneCalendario;
import rc.so.domain.Lezioni_Modelli;
import rc.so.domain.MascheraM5;
import rc.so.domain.ModelliPrg;
import rc.so.domain.ProgettiFormativi;
import rc.so.domain.SoggettiAttuatori;
import rc.so.domain.StaffModelli;
import rc.so.domain.StatiPrg;
import rc.so.domain.TipoDoc;
import rc.so.domain.TipoDoc_Allievi;
import rc.so.entity.Item;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import static java.lang.Math.toRadians;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Paths.get;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import static java.util.Locale.ITALY;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.tika.parser.txt.CharsetDetector;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import static org.joda.time.format.DateTimeFormat.forPattern;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author smo
 */
public class Utility {

    public static boolean demoversion = false;

    
    public static final ResourceBundle conf = ResourceBundle.getBundle("conf.conf");

    
    // TEST //
    public static boolean test = conf.getString("test").equals("SI");
    //////////

    //ADD RAF
    public static final int maxQueryResult = 5000;
    public static final String patternComplete = "yyMMddHHmmssSSS";
    public static final String patternSql = "yyyy-MM-dd";
    public static final String patternITA = "dd/MM/yyyy";
    public static final String patternITACOMPLETE = "dd/MM/yyyy HH:mm:ss";
    public static final String patternFile = "yyyyMMdd";
    public static final String patternHHMM = "HH:mm";
    public static final SimpleDateFormat sdfSQL = new SimpleDateFormat(patternSql);
    public static final SimpleDateFormat sdfITA = new SimpleDateFormat(patternITA);
    public static final SimpleDateFormat sdfITAC1 = new SimpleDateFormat(patternITACOMPLETE);
    public static final SimpleDateFormat sdfHHMM = new SimpleDateFormat(patternHHMM);
    public static final NumberFormat numITA = NumberFormat.getCurrencyInstance(Locale.ITALY);
    public static boolean pregresso = false;
    public static final DateTimeZone dtz_italy = DateTimeZone.forID("Europe/Rome");

    public static DecimalFormat doubleformat = new DecimalFormat("#.##");
    public static final String patternH = "HH:mm:ss";
    public static final String patternHmin = "HH:mm";
    public static final String patternid = "yyyyMMdd";
    public static final String timestamp = "yyyyMMddHHmmssSSS";
    public static final String timestampFAD = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String timestampSQLZONE = "yyyy-MM-dd HH:mm:ss Z";
    public static final String timestampSQL = "yyyy-MM-dd HH:mm:ss";
    public static final String timestampITA = "dd/MM/yyyy HH:mm:ss";    
    public static final String timestampITAcomplete = "dd/MM/yyyy HH:mm:ss.SSS";
    public static final SimpleDateFormat sd0 = new SimpleDateFormat(timestampSQL);
    public static final DateTimeFormatter dtf = DateTimeFormat.forPattern(patternSql);
    public static final DateTimeFormatter dtfad = DateTimeFormat.forPattern(timestampFAD);
    public static final DateTimeFormatter dtfh = DateTimeFormat.forPattern(patternHmin);
    public static final DateTimeFormatter dtfsql = DateTimeFormat.forPattern(timestampSQL);

    public static final String APP = "ENM_NEET";
    public static final Logger LOGAPP = Logger.getLogger(APP);

    //END RAF
    public static void redirect(HttpServletRequest request, HttpServletResponse response, String destination) throws ServletException, IOException {
        if (response.isCommitted()) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(destination);
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect(destination);
        }
    }

    public static void printRequest(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            for (String paramValue : paramValues) {
                System.out.println(paramName + " : " + new String(paramValue.getBytes(Charsets.ISO_8859_1), Charsets.UTF_8));
            }
        }
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            try {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List items = upload.parseRequest((RequestContext) request);
                Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    if (item.isFormField()) {
                        String fieldName = item.getFieldName();
                        String value = new String(item.getString().getBytes(Charsets.ISO_8859_1), Charsets.UTF_8);
                        System.out.println("MULTIPART FIELD - " + fieldName + " : " + value);
                    } else {
                        String fieldName = item.getFieldName();
                        String fieldValue = item.getName();
                        System.out.println("MULTIPART FILE - " + fieldName + " : " + fieldValue);
                    }
                }
            } catch (Exception ex) {
                insertTR("E", "SERVICE", estraiEccezione(ex));
            }
        }
    }

    public static String formatStringtoStringDate(String dat, String pattern1, String pattern2) {
        try {
            return new SimpleDateFormat(pattern2).format(new SimpleDateFormat(pattern1).parse(dat));
        } catch (ParseException e) {
        }
        return "No correct date";
    }

    public static List<Date> getListDates(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar) || calendar.equals(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }

    public static String convMd5(String psw) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(psw.getBytes());
            byte byteData[] = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString().trim();
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
            return "-";
        }
    }

    public static String getRandomString(int length) {
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public static String correctName(String ing) {
        ing = correggiusername(ing);
        return ing.replaceAll("-", "");
    }

    public static String[] stylePF(Map<String, String[]> map, ProgettiFormativi pf) {
        String[] values = map.get(pf.getStato().getTipo()) == null ? (new String[]{"<b style='color: #363a90;'>Stato progetto</b> : @stato", "color: #363a90;", "background-color: rgba(93, 120, 255, 0.07) !important;"})
                : map.get(pf.getStato().getTipo());
        return values;
    }

    public static Map<String, String[]> mapStyles() {
        Map<String, String[]> ms = new HashMap();
        ms.put("chiuso", new String[]{"<b style='color: #363a90;'>Stato progetto</b> : @stato<br>Procedere con l'invio della documentazione di chiusura<br><b>@giorni</b>", "color: #0abb87;", "background-color: rgba(10, 187, 135, 0.07) !important;"});
        ms.put("errore", new String[]{"<b style='color: #363a90;'>Stato progetto</b> : @stato<br>Progetto formativo in stato di errore", "color: #c30041;", "background-color: rgba(253, 57, 122, 0.07) !important;"});
        ms.put("controllare", new String[]{"<b style='color: #363a90;'>Stato progetto</b> : @stato<br>In attesa di verifica da parte del Microcredito", "color: #eaa21c;", "background-color: rgba(255, 184, 34, 0.07) !important;"});
        return ms;
    }

    public static String[] styleMicro(StatiPrg sp) {
        Map<String, String> ms = new HashMap();
        /*fa-times-circle fa-exclamation-circle fa-check-circle fa-clock*/
        ms.put("chiuso", "color: #0abb87;");
        ms.put("errore", "color: #c30041;");
        ms.put("controllare", "color: #eaa21c;");
        return new String[]{ms.get(sp.getTipo()) == null ? "color: #363a90;" : ms.get(sp.getTipo()), sp.getDescrizione().equalsIgnoreCase(sp.getDe_tipo()) ? sp.getDescrizione() : sp.getDescrizione() + " - " + sp.getDe_tipo()};
    }

    public static String correggiusername(String ing) {
        if (ing != null) {
            ing = ing.replaceAll("\\\\", "");
            ing = ing.replaceAll("\n", "");
            ing = ing.replaceAll("\r", "");
            ing = ing.replaceAll("\t", "");
            ing = ing.replaceAll("'", "");
            ing = ing.replaceAll("“", "");
            ing = ing.replaceAll("‘", "");
            ing = ing.replaceAll("”", "");
            ing = ing.replaceAll("\"", "");
            ing = ing.replaceAll(" ", "_");
            return ing.replaceAll("\"", "");
        } else {
            return "-";
        }
    }

    public static String correggi(String ing) {
        if (ing != null) {
            ing = ing.replaceAll("\\\\", "/");
            ing = ing.replaceAll("\n", "");
            ing = ing.replaceAll("\r", "");
            ing = ing.replaceAll("\t", "");
            ing = ing.replaceAll("'", "\'");
            ing = ing.replaceAll("“", "\'");
            ing = ing.replaceAll("‘", "\'");
            ing = ing.replaceAll("”", "\'");
            ing = ing.replaceAll("\"", "/");
            return ing.replaceAll("\"", "\'");
        } else {
            return "-";
        }
    }

    public static String CaratteriSpeciali(String ing) {
        if (ing != null) {
            ing = StringUtils.replace(ing, "Ã©", "è");
            ing = StringUtils.replace(ing, "Ã¨", "é");
            ing = StringUtils.replace(ing, "Ã¬", "ì");
            ing = StringUtils.replace(ing, "Ã²", "ò");
            ing = StringUtils.replace(ing, "Ã¹", "ù");
            ing = StringUtils.replace(ing, "Ã", "à");
            return ing;
        } else {
            return "-";
        }
    }

    public static String ctrlCheckbox(String check) {
        return check == null ? "NO" : "SI";
    }

    public static String UniqueUser(Map<String, String> usernames, String username) {
        Random rand = new Random();
        boolean ok = usernames.get(username) == null;
        while (!ok) {
            username += String.valueOf(rand.nextInt(10));
            ok = usernames.get(username) == null;
        }
        return username;
    }

    public static Map<StatiPrg, Long> GroupByAndCount(SoggettiAttuatori s) {
        return s.getProgettiformativi().stream().collect(Collectors.groupingBy(ProgettiFormativi::getStato, Collectors.counting()));
    }

    public static void sortDoc(List<DocumentiPrg> documeti) {
        documeti.sort((p1, p2) -> p1.getTipo().getId().compareTo(p2.getTipo().getId()));
    }

    public static void sortDoc_Pregresso(List<Documenti_Allievi_Pregresso> documeti) {
        documeti.sort((p1, p2) -> p1.getTipo().getId().compareTo(p2.getTipo().getId()));
    }

    /**
     * CONVERTE LA DATA IN FORMATO UTILIZZABILE IN JAVA UTIL A PARTIRE DALLA
     * DATA IN INGRESSO
     *
     * @param dat - DATA IN INGRESSO
     * @param pattern1 - PATTERN DATA
     * @return Date
     */
    public static Date getUtilDate(String dat, String pattern1) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern1);
            return formatter.parse(dat);
        } catch (ParseException ex) {
            System.err.println("METHOD: " + new Object() {
            }
                    .getClass()
                    .getEnclosingMethod()
                    .getName());
            System.err.println("ERROR: " + ExceptionUtils.getStackTrace(ex));
        }
        return null;
    }

    public static void writeJsonResponseR(HttpServletResponse response, Object list) {
        try {
            JsonObject jMembers = new JsonObject();
            StringWriter sw = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(sw, list);
            String json_s = sw.toString();
            JsonParser parser = new JsonParser();
            JsonElement tradeElement = parser.parse(json_s);
            jMembers.add("aaData", tradeElement.getAsJsonArray());
            response.setContentType("application/json");
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(jMembers.toString());
            response.getWriter().close();
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
    }

    public static void setOreLezioni(Allievi a) {
        if (a.getProgetto() != null) {
            //ore Fase A
            for (DocumentiPrg d : a.getProgetto().getDocumenti().stream().filter(doc -> doc.getGiorno() != null && doc.getDeleted() == 0).collect(Collectors.toList())) {
                d.getPresenti_list().stream().filter((p) -> (a.getId() == p.getId())).forEachOrdered((p) -> {
                    a.setOre_fa(a.getOre_fa() + p.getOre_riconosciute());
                });
            }
            //ore Fase B
            for (Documenti_Allievi d : a.getDocumenti().stream().filter(doc -> doc.getGiorno() != null && doc.getDeleted() == 0).collect(Collectors.toList())) {
                a.setOre_fb(a.getOre_fb() + (d.getOrericonosciute() == null ? 0 : d.getOrericonosciute()));
            }
        }
    }

    public static String formatStringtoStringDate(String dat, String pattern1, String pattern2, boolean timestamp) {
        try {
            if (timestamp) {
                dat = dat.substring(0, dat.length() - 2);
            }
            if (dat.length() == pattern1.length()) {
                DateTimeFormatter fmt = forPattern(pattern1);
                DateTime dtout = fmt.parseDateTime(dat);
                return dtout.toString(pattern2, ITALY);
            }
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
        return "DATA ERRATA";
    }

    public static String estraiEccezione(Exception ec1) {
        try {
            String stack_nam = ec1.getStackTrace()[0].getMethodName();
            String stack_msg = ExceptionUtils.getStackTrace(ec1);
            return stack_nam + " - " + stack_msg;
        } catch (Exception e) {
        }
        return ec1.getMessage();

    }

    public static String getRequestValue(HttpServletRequest request, String fieldname) {
        String out = request.getParameter(fieldname);
        if (out == null || out.trim().equals("null")) {
            out = "";
        } else {
            out = out.trim();
        }
        return out;
    }

    public static String getRequestCheckbox(HttpServletRequest request, String fieldname) {
        String out = request.getParameter(fieldname);
        if (out == null) {
            return "NO";
        }
        return "SI";
    }

    public static boolean checkPDF(File pdffile) {
        if (pdffile.exists()) {
            try {
                int pag;
                try ( InputStream is = new FileInputStream(pdffile);  PdfReader pdfReader = new PdfReader(is)) {
                    PdfDocument pd = new PdfDocument(pdfReader);
                    pag = pd.getNumberOfPages();
                    pd.close();
                }
                return pag > 0;
            } catch (Exception ex) {
                insertTR("E", "SERVICE", estraiEccezione(ex));
            }
        }
        return false;
    }

    public static FileDownload preparefilefordownload(String path) {
        List<String> spl = on("###").splitToList(path);
        if (spl.size() == 3) {
            return new FileDownload(spl.get(0), spl.get(1), spl.get(2));
        }
        return null;
    }

    public static void createDir(String path) {
        try {
            createDirectories(get(path));
        } catch (Exception e) {
        }
    }

    public static List<Documenti_UnitaDidattiche> UDOrderByDate(List<Documenti_UnitaDidattiche> s) {
        s = s.stream().filter(o -> o.getDeleted() == 0).collect(Collectors.toList());
        s.sort(Comparator.comparing(o -> o.getData_modifica()));
        return s;
    }

    public static boolean[] LinksDocs_UD(List<Documenti_UnitaDidattiche> s, int maxfiles, int maxlinks) {
        s = s.stream().filter(o -> o.getDeleted() == 0).collect(Collectors.toList());
        int pdfs = maxfiles - (int) s.stream().filter(d -> d.getTipo().equalsIgnoreCase("PDF")).count();
        int links = maxlinks - (int) s.stream().filter(d -> d.getTipo().equalsIgnoreCase("LINK")).count();
        boolean verify[] = {pdfs == 0, links == 0};
        return verify;
    }

    public static String cp_toUTF(String ing) {
        try {
            String t = new String(ing.getBytes("Windows-1252"), "UTF-8");
            return t.trim();
        } catch (Exception ex) {

        }
        return ing;
    }

    public static int get_eta(Date nascita) {
        try {
            Period period = new Period(new DateTime(nascita.getTime()).withMillisOfDay(0), new DateTime().withMillisOfDay(0));
            return period.getYears();
        } catch (Exception e) {
        }
        return 0;
    }

    public static Map<Integer, List<LezioneCalendario>> groupByLezione(List<LezioneCalendario> l) {
        Map<Integer, List<LezioneCalendario>> byLezione = l.stream().collect(Collectors.groupingBy(t -> t.getLezione()));
        return byLezione;
    }

    public static List<LezioneCalendario> grouppedByLezione(List<LezioneCalendario> l) {
        Map<Integer, List<LezioneCalendario>> byLezione = l.stream().collect(Collectors.groupingBy(t -> t.getLezione()));

        List<LezioneCalendario> grouppedByLezione = new ArrayList();
        LezioneCalendario cl;
        for (Map.Entry<Integer, List<LezioneCalendario>> lez : byLezione.entrySet()) {
            cl = new LezioneCalendario();
            cl.setId(lez.getValue().get(0).getId());
            cl.setLezione(lez.getKey());
            cl.setModello(lez.getValue().get(0).getModello());
            if (lez.getValue().size() > 1) {
                cl.setDoppia(true);
                cl.setId_cal1(lez.getValue().get(0).getId());
                cl.setId_cal2(lez.getValue().get(1).getId());
                cl.setOre1(lez.getValue().get(0).getOre());
                cl.setOre2(lez.getValue().get(1).getOre());
                cl.setUd1(lez.getValue().get(0).getUnitadidattica().getCodice());
                cl.setUd2(lez.getValue().get(1).getUnitadidattica().getCodice());
            } else {
                cl.setDoppia(false);
                cl.setOre1(lez.getValue().get(0).getOre());
                cl.setUd1(lez.getValue().get(0).getUnitadidattica().getCodice());
            }
            grouppedByLezione.add(cl);
        }
        return grouppedByLezione;
    }

    public static String createJson(Object list) {
        try {
            JsonObject jMembers = new JsonObject();
            StringWriter sw = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(sw, list);
            String json_s = sw.toString();
            JsonParser parser = new JsonParser();
            JsonElement tradeElement = parser.parse(json_s);
            jMembers.add("aaData", tradeElement.getAsJsonArray());
            return jMembers.toString();
        } catch (IOException ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
            return "";
        }
    }

    public static ModelliPrg filterModello3(List<ModelliPrg> m) {
        ModelliPrg m3 = m.stream().filter(s -> s.getModello() == 3).findFirst().orElse(null);
        return m3;
    }

    public static ModelliPrg filterModello4(List<ModelliPrg> m) {
        ModelliPrg m3 = m.stream().filter(s -> s.getModello() == 4).findFirst().orElse(null);
        return m3;
    }

    public static ModelliPrg filterModello6(List<ModelliPrg> m) {
        ModelliPrg m3 = m.stream().filter(s -> s.getModello() == 6).findFirst().orElse(null);
        return m3;
    }

    public static boolean lessonPresent(List<Lezioni_Modelli> l, Long id) {
        return l.stream().filter(o -> o.getLezione_calendario().getId() == id).findFirst().isPresent();
    }

    public static Lezioni_Modelli lezioneFiltered(List<Lezioni_Modelli> l, Long id) {
        return l.stream().filter(o -> o.getLezione_calendario().getId() == id).findFirst().orElse(null);
    }

    public static int numberGroupsModello4(ProgettiFormativi ps) {
        Set<Integer> statesAsSet = new HashSet<Integer>();
        for (Allievi p : ps.getAllievi().stream().filter(a -> a.getGruppo_faseB() > 0).collect(Collectors.toList())) {
            statesAsSet.add(p.getGruppo_faseB());
        }
        return statesAsSet.size();
    }

    public static int maxGroupsCreation(ProgettiFormativi ps) {
        Long cnt = ps.getAllievi().stream().filter(a -> !a.getStatopartecipazione().getId().equalsIgnoreCase("03") && a.getGruppo_faseB() != -1).count();
        return cnt.intValue();
    }

    public static Lezioni_Modelli lezioneFilteredByGroup(List<Lezioni_Modelli> l, Long id, int gruppo) {
        return l.stream().filter(o -> o.getLezione_calendario().getId() == id && o.getGruppo_faseB() == gruppo).findFirst().orElse(null);
    }

    public static boolean isPresent_LessonGroup(List<Lezioni_Modelli> l, Long id, int gruppo) {
        return l.stream().anyMatch(lc -> lc.getLezione_calendario().getId() == id && lc.getGruppo_faseB() == gruppo);
    }

    public static DocumentiPrg filterM2(ProgettiFormativi p) {
        return p.getDocumenti().stream().filter(d -> d.getTipo().getId() == 1L).findFirst().orElse(null);
    }

    public static String getStartPath(String path) {
        if (test && SystemUtils.IS_OS_WINDOWS && !path.startsWith("E:")) {
            return "E:\\" + path;
        }
        return path;
    }

    public static long membriAttivi(List<StaffModelli> s) {
        return s.stream().filter(a -> a.getAttivo() == 1).count();
    }

    public static boolean iswindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }

    public static boolean invioEmailComunicazione(String stato1, String stato2) {
        if (Utility.demoversion) {
            return false;
        }
        String key = stato1 + "_" + stato2;
        Set s = statiEmail();
        return s.contains(key);
    }

    private static Set statiEmail() {
        Set s = new HashSet<String>();
        s.add("DV_P"); //Accettazione Modello 2
        s.add("DV_DVE"); //Rifiuto Modello 2
        s.add("DC_ATA"); //Accettazione Modello 3
        s.add("DC_DCE"); //Rifiuto Modello 3
        s.add("DVA_ATB"); //Accettato Modello 4
        s.add("DVA_DVAE"); //Rifiutato Modello 

        return s;
    }

    public static Map<Integer, String> bando_SE() {
        Map s = new HashMap();
        s.put(1, "Microcredito");
        s.put(2, "Microcredito esteso");
        s.put(3, "Piccoli prestiti");
        return s;
    }

    public static Map<Integer, String> bando_SUD() {
        Map s = new HashMap();
        s.put(1, "Finanziamento più consistente");
        s.put(2, "Procedure più semplici");
        s.put(3, "Criteri di selezione meno stringenti");
        s.put(4, "Tempi di istruttoria più veloci");
        s.put(5, "Piano di ammortamento e restituzione più conveniente");
        s.put(6, "Presenza di quota a fondo perduto");
        return s;
    }

    public static Map<Integer, String> no_agenvolazione() {
        Map s = new HashMap();
        s.put(1, "L'investimento iniziale non raggiunge l'investimento minimo per accedere alle agevolazioni di legge");
        s.put(2, "La copertura è assicurata interamente con fondi propri");
        s.put(3, "Ricorso al credito ordinario - non ci sono bandi attivi o l'iniziativa non rientra tra le iniziative ammissibili a finanziamento dei bandi attivi");
        return s;
    }

    public static Map<Long, Long> allieviM5_loaded(List<MascheraM5> m5) {
        Map<Long, Long> ids = new HashMap();

        for (MascheraM5 m : m5) {
            ids.put(m.getAllievo().getId(), m.getId());
        }
        return ids;
    }

    public static Map<Long, Boolean> allieviM5_premialita(List<MascheraM5> m5, int idpf) {
        Long hh64 = Long.valueOf(230400000);
        Map<Long, Long> oreRendicontabili = Action.OreRendicontabiliAlunni(idpf);
        Map<Long, Boolean> ids = new HashMap();
        int i = 1;
        for (MascheraM5 m : m5) {
            if (oreRendicontabili.get(m.getAllievo().getId()) != null && oreRendicontabili.get(m.getAllievo().getId()).compareTo(hh64) > 0) {
                if (m.isTabella_premialita() && m.getTabella_premialita_punteggio() > 0) {
                    ids.put(m.getAllievo().getId(), true);
                }
            }
            i++;
        }
        return ids;

    }

    public static TipoDoc filterDocById(List<TipoDoc> docs, Long id) {
        return docs.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    public static Map<Allievi, Documenti_Allievi> Modello5Allievi(List<MascheraM5> m5) {
        Map<Allievi, Documenti_Allievi> ret = new HashMap();
        Documenti_Allievi d = null;
        for (Allievi a : listAllieviM5_loaded(m5)) {
            d = a.getDocumenti().stream().filter(doc -> doc.getDeleted() == 0 && doc.getTipo().getId() == 20L).findFirst().orElse(null);
            if (d != null) {
                ret.put(a, d);
            }
        }
        return ret;
    }

    public static Map<Allievi, Documenti_Allievi> Modello7Allievi(List<MascheraM5> m5) {
        Map<Allievi, Documenti_Allievi> ret = new HashMap();
        Documenti_Allievi d = null;
        for (Allievi a : listAllieviM5_loaded(m5)) {
            d = a.getDocumenti().stream().filter(doc -> doc.getDeleted() == 0 && doc.getTipo().getId() == 22L).findFirst().orElse(null);
            if (d != null) {
                ret.put(a, d);
            }
        }
        return ret;
    }

    public static List<Allievi> listAllieviM5_loaded(List<MascheraM5> m5) {
        List<Allievi> al = new ArrayList();
        for (MascheraM5 m : m5) {
            al.add(m.getAllievo());
        }
        return al;
    }

    public static String calcoladurata(long millis) {
        if (millis == 0 || millis < 0) {
            return "0h 0min 0sec";
        }
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        StringBuilder sb = new StringBuilder(64);
        sb.append(hours);
        sb.append("h ");
        sb.append(minutes);
        sb.append("min ");
        sb.append(seconds);
        sb.append("sec");
        return sb.toString();
    }

    public static String convertbooleantostring(boolean ing) {
        try {
            if (ing) {
                return "SI";
            }
        } catch (Exception e) {
        }
        return "NO";
    }

    public static DocumentiPrg filterByTipo(ProgettiFormativi p, TipoDoc t) {
        return p.getDocumenti().stream().filter(d -> d.getTipo().getId() == t.getId()).findFirst().orElse(null);
    }

    public static TipoDoc_Allievi filterDocAllievoById(List<TipoDoc_Allievi> docs, Long id) {
        return docs.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    public static String roundFloatAndFormat(float f, boolean converttoHours) {
        try {
            if (converttoHours) {
                double hours = f / 1000.0 / 60.0 / 60.0;
                BigDecimal bigDecimal = new BigDecimal(hours);
                bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
                String out = numITA.format(bigDecimal).replaceAll("[^0123456789.,()-]", "").trim();
                return out;
            } else {
                BigDecimal bigDecimal = new BigDecimal(Float.toString(f));
                bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
                return numITA.format(bigDecimal).replaceAll("[^0123456789.,()-]", "").trim();
            }
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
        return "0";

    }

    public static String roundDoubleAndFormat(double f) {
        try {
            BigDecimal bigDecimal = new BigDecimal(Double.toString(f));
            bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
            return numITA.format(bigDecimal).replaceAll("[^0123456789.,()-]", "").trim();
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
        return "0";

    }

    //cscrfl86e19c3520
    public static String estraiSessodaCF(String cf) {
        try {
            if (cf.trim().length() == 16) {
                String data = StringUtils.substring(cf, 9, 11);
                if (Integer.parseInt(data) > 31) {
                    return "F";
                } else {
                    return "M";
                }
            }
        } catch (Exception e) {
        }
        return "M";
    }

    public static List<Item> unitamisura() {
        List<Item> al = new ArrayList<>();
        al.add(new Item("gg", "GIORNI"));
        al.add(new Item("mm", "MESI"));
        al.add(new Item("aa", "ANNI"));
        return al;
    }

    public static Date getUltimoGiornoLezioneM4(ProgettiFormativi p) {
        ModelliPrg m4 = filterModello4(p.getModelli());
        Lezioni_Modelli ultima_lezione = m4.getLezioni().stream().max(Comparator.comparing(d -> d.getGiorno())).orElse(null);
        return ultima_lezione.getGiorno();
    }

    public static Date getPrimoGiornoLezioneM3(ProgettiFormativi pf) {
        ModelliPrg m3 = filterModello3(pf.getModelli());
        Lezioni_Modelli prima_lezione = m3.getLezioni().stream().min(Comparator.comparing(d -> d.getGiorno())).orElse(null);
        return prima_lezione.getGiorno();
    }

    public static boolean isEditableModel(List<Lezioni_Modelli> list) throws ParseException {
        Date today = new Date();
//solo per test        today = new SimpleDateFormat("yyyy-MM-dd").parse("2021-07-18");
        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();
        for (Lezioni_Modelli l : list) {
            if (dateTimeComparator.compare(l.getGiorno(), today) > -1) {
                return true;
            }
        }
        return false;
    }

    public static int allieviOK(long idp, List<Allievi> l) {
        Long hh36 = Long.valueOf(129600000);
//        Long hh64 = new Long(230400000);
//        Map<Long, Long> oreRendicontabili = Action.OreRendicontabiliAlunni((int) (long) idp);
        Map<Long, Long> oreRendicontabili_faseA = Action.OreRendicontabiliAlunni_faseA((int) (long) idp);
        int count = l.size();
        for (Allievi a : l) {
            if (oreRendicontabili_faseA.get(a.getId()) != null && oreRendicontabili_faseA.get(a.getId()).compareTo(hh36) < 0) {
                count--;
            }
        }
        return count;
    }

    public static List<Allievi> allievi_fa(long idp, List<Allievi> l) {
        Long hh36 = Long.valueOf(129600000);
        Map<Long, Long> oreRendicontabili_faseA = Action.OreRendicontabiliAlunni_faseA((int) (long) idp);
        return l.stream().filter(a -> oreRendicontabili_faseA.get(a.getId()) != null && oreRendicontabili_faseA.get(a.getId()).compareTo(hh36) >= 0).collect(Collectors.toList());
    }

    public static List<Allievi> allievi_fb(long idp, List<Allievi> l) {

        return l.stream().filter(a -> a.getGruppo_faseB() > 0).collect(Collectors.toList());
//        
//        Long hh64 = new Long(230400000);
//        Map<Long, Long> oreRendicontabili_faseB = Action.OreRendicontabiliAlunni((int) (long) idp);
//        return l.stream().filter(a -> oreRendicontabili_faseB.get(a.getId()) != null && oreRendicontabili_faseB.get(a.getId()).compareTo(hh64) > 0).collect(Collectors.toList());
    }

    public static List<Docenti> docenti_ore(long idp, List<Docenti> l) {
        Map<Long, Long> oreRendicontabili_docenti = Action.OreRendicontabiliDocenti((int) (long) idp);
        return l.stream().filter(a -> oreRendicontabili_docenti.get(a.getId()) != null).collect(Collectors.toList());
    }

    public static List<Docenti> docenti_ore_A(long idp, List<Docenti> l) {
        Map<Long, Long> oreRendicontabili_docenti = Action.OreRendicontabiliDocentiFASEA((int) (long) idp);
        return l.stream().filter(a -> oreRendicontabili_docenti.get(a.getId()) != null).collect(Collectors.toList());
    }

    public static String convertToHours_R(long value1) {
        try {
            double hours = value1 / 1000.0 / 60.0 / 60.0;
            BigDecimal bigDecimal = new BigDecimal(hours);
            bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
            return bigDecimal.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.00";
    }

    public static String roundTwoDigits(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        String ret = String.valueOf(bd.doubleValue());
        if (ret.split("\\.")[1].length() == 1) {
            ret += "0";
        }
        return ret;
    }

    public static Map<String, String> mapCoeffDocenti(String fasciaA, String fasciaB) {
        Map<String, String> m = new HashMap();
        m.put("FA", fasciaA);
        m.put("FB", fasciaB);
        return m;
    }

    public static String createJsonCL(String message) {
        JSONArray array = new JSONArray();
        JSONObject item;
        String[] alunni = message.split(";");
        for (String a : alunni) {
            item = new JSONObject();
            item.put("id", a.split("=")[0]);
            item.put("ore", a.split("=")[1]);
            item.put("totale", a.split("=")[2]);
            array.put(item);
        }
        return array.toString();
    }

    public static String createJsonCL_Mappatura(String message) {
        JSONArray array = new JSONArray();
        JSONObject item;
        String[] alunni = message.split(";");
        for (String a : alunni) {
            item = new JSONObject();
            item.put("id", a.split("=")[0]);
            item.put("mappato", a.split("=")[1]);
            array.put(item);
        }
        return array.toString();
    }

    public static String createJsonCL_Output(String message) {
        JSONArray array = new JSONArray();
        JSONObject item;
        String[] alunni = message.split(";");
        for (String a : alunni) {
            item = new JSONObject();
            item.put("id", a.split("=")[0]);
            item.put("output", a.split("=")[1]);
            array.put(item);
        }
        return array.toString();
    }

    public static double parseDouble(String f) {
        try {
            BigDecimal bigDecimal = new BigDecimal(f);
            bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
            return bigDecimal.doubleValue();
        } catch (Exception e) {
        }
        return 0.0;

    }
    public static long parseLong(String f) {
        try {
            
            return Long.valueOf(f);
        } catch (Exception e) {
        }
        return 0L;
    }
    public static int parseInt(String f) {
        try {
            return Integer.parseInt(f);
        } catch (Exception e) {
        }
        return 0;

    }

    public static boolean copyR(File source, File dest) {
        boolean es;
        try {
            long byteing = source.length();
            try ( OutputStream out = new FileOutputStream(dest)) {
                long contenuto = FileUtils.copyFile(source, out);
                es = byteing == contenuto;
            }
        } catch (Exception e) {
            es = false;
        }
        return es;
    }

    public static String conversionText(String ing) {
        try {
            CharsetDetector detector = new CharsetDetector();
            detector.setText(ing.getBytes());
            return new String(ing.getBytes(detector.detect().getName()), Charset.forName("utf-8"));
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
        return ing;
    }

    public static String getstatoannullato(String stato_prec) {
        switch (stato_prec) {
            case "ATA":
            case "ATB":
                return stato_prec + "E";
            case "SOA":
                return "ATAE";
            case "SOB":
                return "ATBE";
            default:
                return "DVBE";
        }
    }

    public static DateTime format(String ing, String pattern) {
        try {
            if (ing.contains(".")) {
                ing = ing.split("\\.")[0];
            }
            return DateTimeFormat.forPattern(pattern).parseDateTime(ing);
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
        return null;
    }

    private static final long MAX = 18000000;

    public static long convertHours(String ore) {
        try {
            double d1 = Double.parseDouble(ore);
            long tot = Math.round(d1) * 3600000;
            return tot;
        } catch (Exception e) {

        }
        return MAX;
    }

    public static String formatStringtoStringDateSQL(String dat) {
        return formatStringtoStringDate(dat, patternSql, patternITA, false);
    }

    public static void printbarcode(BarcodeQRCode barcode, PdfDocument pdfDoc, boolean page, String add) {
        try {
            Rectangle rect = barcode.getBarcodeSize();
            PdfFormXObject formXObject = new PdfFormXObject(new Rectangle(rect.getWidth(), rect.getHeight() + 10));
            PdfCanvas pdfCanvas = new PdfCanvas(formXObject, pdfDoc);
            barcode.placeBarcode(pdfCanvas, BLACK);
            Image bCodeImage = new Image(formXObject);
            bCodeImage.setRotationAngle(toRadians(90));
            bCodeImage.setFixedPosition(25, 5);

            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                new Canvas(pdfDoc.getPage(i), pdfDoc.getDefaultPageSize()).add(bCodeImage);
                if (page) {
                    Canvas canvas = new Canvas(pdfDoc.getPage(i), pdfDoc.getDefaultPageSize());

                    canvas.showTextAligned((("Pag. " + i + " di " + pdfDoc.getNumberOfPages())),
                            pdfDoc.getPage(i).getPageSize().getWidth() - 100,
                            5, TextAlignment.CENTER)
                            .close();

                    if (add != null) {
                        Text text = new Text(add);
                        PdfFont fontnormal = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);
                        text.setFont(fontnormal);
                        text.setFontSize(8);
                        Paragraph p1 = new Paragraph();
                        p1.add(text);
                        canvas.showTextAligned(p1,
                                5,
                                pdfDoc.getPage(i).getPageSize().getHeight() - 15,
                                TextAlignment.LEFT).close();
                    }

                }
            }
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
    }

    public static void gestisciorerendicontabili(LinkedList<Presenti1> report, long ore) {

        try {
            DateTimeFormatter fmt = forPattern(timestampSQL);
            Presenti1 docente = report.stream().filter(pr1 -> pr1.getRuolo().equalsIgnoreCase("DOCENTE")).findAny().orElse(null);
            List<Presenti1> allievi = report.stream().filter(pr1 -> !pr1.getRuolo().equalsIgnoreCase("DOCENTE")).collect(Collectors.toList());

            if (docente != null && !allievi.isEmpty()) {
                List<Interval> accessi_docente = new ArrayList<>();
                List<Interval> accessi_complessivi = new ArrayList<>();
                List<String> login_docente = Splitter.on("\n").splitToList(docente.getOradilogin());
                List<String> logout_docente = Splitter.on("\n").splitToList(docente.getOradilogout());
                for (int x = 0; x < login_docente.size(); x++) {
                    DateTime start1 = fmt.parseDateTime("2021-01-01 " + login_docente.get(x));
                    DateTime end1 = fmt.parseDateTime("2021-01-01 " + logout_docente.get(x));
                    if (end1.isAfter(start1)) {
                        accessi_docente.add(new Interval(start1, end1));
                    }
                }

                AtomicLong millis_rendicontabili_DOCENTE = new AtomicLong(0L);

                allievi.forEach(cnsmr -> {
                    AtomicLong millis_rendicontabili = new AtomicLong(0L);
                    List<Interval> accessi = new ArrayList<>();
                    List<String> login = Splitter.on("\n").splitToList(cnsmr.getOradilogin());
                    List<String> logout = Splitter.on("\n").splitToList(cnsmr.getOradilogout());

                    for (int x = 0; x < login.size(); x++) {
                        DateTime start2 = fmt.parseDateTime("2021-01-01 " + login.get(x));
                        DateTime end2 = fmt.parseDateTime("2021-01-01 " + logout.get(x));
                        if (end2.isAfter(start2)) {
                            accessi.add(new Interval(start2, end2));
                            accessi_complessivi.add(new Interval(start2, end2));
                        }
                    }
                    accessi.forEach(intervallo2 -> {
                        accessi_docente.forEach(intervallo1 -> {
                            if (intervallo2.overlaps(intervallo1)) {
                                millis_rendicontabili.addAndGet(intervallo2.overlap(intervallo1).toDurationMillis());
                            }
                        });

                    });

                    if (millis_rendicontabili.get() >= ore) {
                        cnsmr.setTotaleorerendicontabili(calcoladurata(ore));
                        cnsmr.setMillistotaleorerendicontabili(ore);
                    } else if (millis_rendicontabili.get() >= cnsmr.getMillistotaleore()) {
                        cnsmr.setTotaleorerendicontabili(cnsmr.getTotaleore());
                        cnsmr.setMillistotaleorerendicontabili(cnsmr.getMillistotaleore());
                    } else {
                        cnsmr.setTotaleorerendicontabili(calcoladurata(millis_rendicontabili.get()));
                        cnsmr.setMillistotaleorerendicontabili(millis_rendicontabili.get());
                    }

                });

                accessi_docente.forEach(intervallo1 -> {
                    DateTime start = intervallo1.getStart();
                    while (start.isBefore(intervallo1.getEnd())) {
                        for (int i = 0; i < accessi_complessivi.size(); i++) {
                            Interval ac1 = accessi_complessivi.get(i);

                            if (ac1.getStart().isBefore(start) || ac1.getStart().isEqual(start)) {
                                if (ac1.getEnd().isAfter(start) || ac1.getEnd().isEqual(start)) {
                                    millis_rendicontabili_DOCENTE.addAndGet(1000);
                                    break;
                                }
                            }
                        }
                        start = start.plusSeconds(1);
                    }
                });

                if (millis_rendicontabili_DOCENTE.get() >= ore) {
                    docente.setTotaleorerendicontabili(calcoladurata(ore));
                    docente.setMillistotaleorerendicontabili(ore);
                } else if (millis_rendicontabili_DOCENTE.get() >= docente.getMillistotaleore()) {
                    docente.setTotaleorerendicontabili(docente.getTotaleore());
                    docente.setMillistotaleorerendicontabili(docente.getMillistotaleore());
                } else {
                    docente.setTotaleorerendicontabili(calcoladurata(millis_rendicontabili_DOCENTE.get()));
                    docente.setMillistotaleorerendicontabili(millis_rendicontabili_DOCENTE.get());
                }
            }
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
    }

    public static int getIdUser(Database db, String nome, String cognome, int idpr, int idsa, String ruolo) {
        if (ruolo.equalsIgnoreCase("DOCENTE")) {
            return getIdDocente(db, nome, cognome, idsa);
        } else if (ruolo.equalsIgnoreCase("ALLIEVO NEET")) {
            return getIdAllievo(db, nome, cognome, idpr);
        }
        return 0;
    }

    private static int getIdAllievo(Database db, String nome, String cognome, int idpr) {
        try {
            String sql = "SELECT idallievi FROM allievi WHERE nome = ? AND cognome = ? AND idprogetti_formativi = ? AND id_statopartecipazione = ? ORDER BY idallievi DESC LIMIT 1";
            try ( PreparedStatement ps = db.getC().prepareStatement(sql)) {
                ps.setString(1, nome);
                ps.setString(2, cognome);
                ps.setInt(3, idpr);
                ps.setString(4, "01");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
        return 0;
    }

    private static int getIdDocente(Database db, String nome, String cognome, int idsa) {
        try {
            String sql = "SELECT iddocenti FROM docenti WHERE nome = ? AND cognome = ? AND idsoggetti_attuatori = ? AND stato = ? ORDER BY iddocenti DESC LIMIT 1";
            try ( PreparedStatement ps = db.getC().prepareStatement(sql)) {
                ps.setString(1, nome);
                ps.setString(2, cognome);
                ps.setInt(3, idsa);
                ps.setString(4, "A");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
        return 0;
    }

    public static String convertTS_Italy(String ts1) {
//        TimeZone tz1 = TimeZone.getTimeZone("Europe/Rome");
//        TimeZone tz2 = TimeZone.getTimeZone("GMT");
//        long timeDifference = tz1.getRawOffset() - tz2.getRawOffset() + tz1.getDSTSavings() - tz2.getDSTSavings();
        String dt1 = StringUtils.substring(ts1, 0, 26);
        try {
            if (dt1.length() != timestampFAD.length()) {
                if (dt1.length() == 19) {
                    dt1 += ".000000";
                }
            }
        } catch (Exception e) {
        }
        DateTime start = new DateTime(dtfad.parseDateTime(dt1));
        DateTime dateTimeIT = start.plus(getTimeDiff());
        return dateTimeIT.toString(timestampSQL);
    }

    public static long getTimeDiff() {
        try {
            TimeZone tz1 = TimeZone.getTimeZone("Europe/Rome");
            TimeZone tz2 = TimeZone.getTimeZone("GMT");
            TimeZone tz3 = TimeZone.getTimeZone("GMT+1");
            ZoneId arrivingZone = ZoneId.of("Europe/Rome");
            ZonedDateTime arrival = Instant.now().atZone(arrivingZone);
            if (arrivingZone.getRules().isDaylightSavings(arrival.toInstant())) {
                return tz1.getRawOffset() - tz2.getRawOffset() + tz1.getDSTSavings() - tz2.getDSTSavings();
            } else {
                return tz1.getRawOffset() - tz3.getRawOffset() + tz1.getDSTSavings() - tz3.getDSTSavings();
            }
        } catch (Exception e) {
        }
        return 0L;
    }

    public static String getOnlyStrings(String s) {
        Pattern pattern = Pattern.compile("[^a-z A-Z]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }

    public static String convertSvolgimento(String ing) {
        if (ing == null) {
            return "In FAD";
        } else {
            switch (ing) {
                case "":
                case "F":
                    return "In FAD";
                case "P":
                    return "In Presenza";
                default:
                    return "In FAD";
            }
        }
    }

    public static long calcolaintervallomillis(String orastart, String oraend) {
        try {
            DateTime st_data1 = new DateTime(2000, 1, 1, Integer.parseInt(orastart.split(":")[0]), Integer.parseInt(orastart.split(":")[1]));
            DateTime st_data2 = new DateTime(2000, 1, 1, Integer.parseInt(oraend.split(":")[0]), Integer.parseInt(oraend.split(":")[1]));
            Period p = new Period(st_data1, st_data2, PeriodType.millis());
            return p.getValue(0);
        } catch (Exception e) {
            return 0L;
        }

    }

}
