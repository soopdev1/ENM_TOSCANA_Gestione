/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.forms.PdfAcroForm;
import static com.itextpdf.forms.PdfAcroForm.getAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import static com.itextpdf.kernel.colors.ColorConstants.BLACK;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.SignatureUtil;
import rc.so.db.Action;
import static rc.so.db.Action.insertTR;
import rc.so.db.Database;
import rc.so.db.Entity;
import rc.so.db.Registro_completo;
import rc.so.domain.Allievi;
import rc.so.domain.Comuni;
import rc.so.domain.Docenti;
import rc.so.domain.Lezioni_Modelli;
import rc.so.domain.MascheraM5;
import rc.so.domain.ModelliPrg;
import rc.so.domain.Nazioni_rc;
import rc.so.domain.ProgettiFormativi;
import rc.so.domain.SoggettiAttuatori;
import rc.so.domain.StaffModelli;
import rc.so.domain.TipoDoc;
import rc.so.domain.TipoDoc_Allievi;
import rc.so.domain.TitoliStudio;
import rc.so.entity.Item;
import rc.so.entity.MappaturaId;
import rc.so.entity.OreId;
import rc.so.entity.OutputId;
import static rc.so.util.Utility.checkPDF;
import static rc.so.util.Utility.convertToHours_R;
import static rc.so.util.Utility.createDir;
import static rc.so.util.Utility.estraiEccezione;
import static rc.so.util.Utility.estraiSessodaCF;
import static rc.so.util.Utility.getOnlyStrings;
import static rc.so.util.Utility.get_eta;
import static rc.so.util.Utility.patternITA;
import static rc.so.util.Utility.roundDoubleAndFormat;
import static rc.so.util.Utility.roundFloatAndFormat;
import static rc.so.util.Utility.sdfHHMM;
import static rc.so.util.Utility.sdfITA;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import static java.lang.Math.toRadians;
import static java.lang.System.setProperty;
import java.security.Principal;
import static java.security.Security.addProvider;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.Calendar;
import static java.util.Calendar.getInstance;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.io.FileUtils.readFileToByteArray;
import static org.apache.commons.io.FilenameUtils.getExtension;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import static org.apache.commons.lang3.StringUtils.replace;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDMarkInfo;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import static org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory.createFromImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.xmpbox.XMPMetadata;
import static org.apache.xmpbox.XMPMetadata.createXMPMetadata;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.xml.XmpSerializer;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.joda.time.DateTime;
import static rc.so.util.Utility.sd1;

/**
 *
 * @author rcosco
 */
public class Pdf_new {

    //RICHIAMI
    public static File MODELLO0(Entity e, String idmodello, Allievi al) {
        File out1 = MODELLO0_BASE(e, idmodello, al);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "MODELLO0", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File MODELLO1(Entity e, String idmodello, String username,
            SoggettiAttuatori sa, Allievi al, DateTime dataconsegna, boolean flatten) {
        File out1 = MODELLO1_BASE(e, idmodello, username, sa, al, dataconsegna, flatten);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "MODELLO1", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File MODELLO2(Entity e, String idmodello, String username,
            SoggettiAttuatori sa, ProgettiFormativi pf, List<Allievi> allievi,
            DateTime dataconsegna, boolean flatten) {
        File out1 = MODELLO2_BASE(e, idmodello, username, sa, pf, allievi, dataconsegna, flatten);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "MODELLO2", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File MODELLO3(Entity e,
            String username,
            SoggettiAttuatori sa, ProgettiFormativi pf, List<Allievi> al, List<Docenti> docenti, List<Lezioni_Modelli> lezioni,
            List<StaffModelli> staff,
            DateTime dataconsegna,
            boolean flatten) {
        File out1 = MODELLO3_BASE(e, username, sa, pf, al, docenti, lezioni, staff, dataconsegna, flatten);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "MODELLO3", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File REGISTROCARTACEO(Entity e,
            String username,
            Lezioni_Modelli lm,
            DateTime dataconsegna) {
        File out1 = REGISTROCARTACEO_BASE(null, e, username, lm, dataconsegna);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "REGISTROCARTACEO", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File MODELLO4(Entity e,
            String username,
            SoggettiAttuatori sa, ProgettiFormativi pf, List<Allievi> al, List<Docenti> docenti, List<Lezioni_Modelli> lezioni,
            List<StaffModelli> staff,
            DateTime dataconsegna,
            boolean flatten) {
        File out1 = MODELLO4_BASE(e, username, sa, pf, al, docenti, lezioni, staff, dataconsegna, flatten);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "MODELLO4", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File MODELLO5(
            Entity e,
            String contentb64,
            String username,
            SoggettiAttuatori sa,
            Allievi al,
            String[] datifrequenza,
            MascheraM5 m5,
            DateTime dataconsegna,
            boolean flatten) {
        File out1 = MODELLO5_BASE(e, contentb64, username, sa, al, datifrequenza, m5, dataconsegna, flatten);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "MODELLO5", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File MODELLO6(
            Entity e,
            String username,
            SoggettiAttuatori sa,
            ProgettiFormativi pf,
            ModelliPrg m6,
            DateTime dataconsegna,
            boolean flatten) {
        File out1 = MODELLO6_BASE(e, username, sa, pf, m6, dataconsegna, flatten);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "MODELLO6", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File MODELLO7(
            Entity e,
            String username,
            Allievi al,
            String orerendicontabili,
            DateTime dataconsegna,
            boolean flatten) {
        File out1 = MODELLO7_BASE(e, username, al, orerendicontabili, dataconsegna, flatten);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "MODELLO7", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File ALLEGATOB1(
            String pathdest,
            Entity e,
            String username,
            Docenti d,
            DateTime dataconsegna) {
        File out1 = ALLEGATOB1_BASE(pathdest, e, username, d, dataconsegna);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "ALLEGATOB1", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File CERTIFICAZIONEASSENZA(
            Entity e,
            String username,
            SoggettiAttuatori sa,
            DateTime dataconsegna,
            boolean flatten) {
        File out1 = CERTIFICAZIONEASSENZA_BASE(e, username, sa, dataconsegna, flatten);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "ASSENZA POSIZIONE", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File CHECKLIST(
            String startpath,
            Entity e,
            String username,
            SoggettiAttuatori sa,
            ProgettiFormativi pf,
            DateTime dataconsegna,
            boolean flatten) {
        File out1 = CHECKLIST_BASE(startpath, e, username, sa, pf, dataconsegna, flatten);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "CHECKLIST FINALE", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    public static File ESITOVALUTAZIONE(
            String startpath,
            Entity e,
            String username,
            SoggettiAttuatori sa,
            ProgettiFormativi pf,
            DateTime dataconsegna,
            boolean flatten) {
        File out1 = ESITOVALUTAZIONE_BASE(startpath, e, username, sa, pf, dataconsegna, flatten);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "ESITO VALUTAZIONE", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }

    //MODELLI
    private static File REGISTROCARTACEO_BASE(
            String pathdest,
            Entity e,
            String username,
            Lezioni_Modelli lm,
            DateTime dataconsegna) {

        try {

            TipoDoc p = e.getEm().find(TipoDoc.class, 4L);
            String contentb64 = p.getModello();

            File pdfOut;
            if (pathdest == null) {
                String pathtemp = e.getPath("pathtemp");
                createDir(pathtemp);
                pdfOut = new File(pathtemp
                        + username + "_"
                        + StringUtils.deleteWhitespace(
                                "REGISTROCARTACEO_BASE_"
                                + sd1.format(lm.getGiorno()) + "_" + lm.getModello().getProgetto().getId() + "_"
                                + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".R0.pdf"));
            } else {
                pdfOut = new File(pathdest);
            }

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);
                Map<String, PdfFormField> fields = form.getAllFormFields();

                //PAG.1
                setFieldsValue(form, fields, "NOMESA", lm.getModello().getProgetto().getSoggetto().getRagionesociale().toUpperCase());
                setFieldsValue(form, fields, "DD", lm.getModello().getProgetto().getSoggetto().getDd());
                setFieldsValue(form, fields, "COGNOME", lm.getModello().getProgetto().getSoggetto().getCognome().toUpperCase());
                setFieldsValue(form, fields, "NOME", lm.getModello().getProgetto().getSoggetto().getNome().toUpperCase());
                setFieldsValue(form, fields, "CARICA", lm.getModello().getProgetto().getSoggetto().getCarica().toUpperCase());
                setFieldsValue(form, fields, "CIP", lm.getModello().getProgetto().getCip());
                setFieldsValue(form, fields, "DATAINIZIO", sdfITA.format(lm.getModello().getProgetto().getStart()));
                setFieldsValue(form, fields, "DATAFINE", sdfITA.format(lm.getModello().getProgetto().getEnd()));

                if (lm.getModello().getProgetto().getSedefisica() != null) {
                    Comuni c_s = lm.getModello().getProgetto().getSedefisica().getComune();
                    setFieldsValue(form, fields, "REGIONESEDE", c_s.getRegione());
                    setFieldsValue(form, fields, "COMUNESEDE", c_s.getNome());
                    setFieldsValue(form, fields, "PROVINCIASEDE", c_s.getCod_provincia());
//                    setFieldsValue(form, fields, "CAPSEDE", c_s.getC());
                    setFieldsValue(form, fields, "INDIRIZZOSEDE", lm.getModello().getProgetto().getSede().getIndirizzo());

                }

                setFieldsValue(form, fields, "DATALEZ", sdfITA.format(lm.getGiorno()));
                setFieldsValue(form, fields, "UDLEZ", lm.getLezione_calendario().getUnitadidattica().getDescrizione());
                setFieldsValue(form, fields, "MODLEZ", lm.getLezione_calendario().getUnitadidattica().getCodice());
                setFieldsValue(form, fields, "ORELEZ", roundDoubleAndFormat(lm.getLezione_calendario().getUnitadidattica().getOre()));

                setFieldsValue(form, fields, "DOC_COGN", lm.getDocente().getCognome().toUpperCase());
                setFieldsValue(form, fields, "DOC_NOM", lm.getDocente().getNome().toUpperCase());
                setFieldsValue(form, fields, "DOC_CF", lm.getDocente().getCodicefiscale().toUpperCase());
                setFieldsValue(form, fields, "DOC_RUOLO", "DOCENTE");

                List<Allievi> allievi = lm.getModello().getProgetto().getAllievi().stream().filter(al -> al.getStatopartecipazione().getId()
                        .equalsIgnoreCase("13") || al.getStatopartecipazione().getId()
                        .equalsIgnoreCase("14") || al.getStatopartecipazione().getId()
                        .equalsIgnoreCase("15")
                ).collect(Collectors.toList());

                AtomicInteger in = new AtomicInteger(1);
                allievi.forEach(a1 -> {
                    setFieldsValue(form, fields, "ALL_COGN" + in.get(), a1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "ALL_NOM" + in.get(), a1.getNome().toUpperCase());
                    setFieldsValue(form, fields, "ALL_CF" + in.get(), a1.getCodicefiscale().toUpperCase());
                    setFieldsValue(form, fields, "ALL_RUOLO" + in.get(), "ALLIEVO/A");
                    in.addAndGet(1);
                });

                fields.forEach((KEY, VALUE) -> {
                    form.partialFormFlattening(KEY);
                });

                form.flattenFields();
                form.flush();

                BarcodeQRCode barcode = new BarcodeQRCode(username + " / REGCART / "
                        + StringUtils.deleteWhitespace(sd1.format(lm.getGiorno()) + "_" + lm.getModello().getProgetto().getId())
                        + " / " + dataconsegna.toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);
            }
            if (checkPDF(pdfOut)) {
                return pdfOut;
            }
        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;
    }

    private static File ALLEGATOB1_BASE(
            String pathdest,
            Entity e,
            String username,
            Docenti d,
            DateTime dataconsegna) {
        try {

            TipoDoc p = e.getEm().find(TipoDoc.class, 34L);
            String contentb64 = p.getModello();

            File pdfOut;
            if (pathdest == null) {
                String pathtemp = e.getPath("pathtemp");
                createDir(pathtemp);
                pdfOut = new File(pathtemp
                        + username + "_"
                        + StringUtils.deleteWhitespace(d.getCognome() + "_"
                                + d.getNome()) + "_" + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".B1.pdf");
            } else {
                pdfOut = new File(pathdest);
            }

            List<TitoliStudio> ts = e.listaTitoliStudio();

            Database db = new Database(true);
            List<Item> aq = db.area_qualificazione();
            List<Item> inq = db.inquadramento();
            List<Item> att = db.attivita_docenti();
            List<Item> fon = db.fontifin();
            db.closeDB();
            List<Item> um = Utility.unitamisura();

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);
                Map<String, PdfFormField> fields = form.getAllFormFields();

                //PAG.1
                setFieldsValue(form, fields, "NOMESA", d.getSoggetto().getRagionesociale().toUpperCase());
                setFieldsValue(form, fields, "DD", d.getSoggetto().getDd());
                setFieldsValue(form, fields, "COGNOME", d.getSoggetto().getCognome().toUpperCase());
                setFieldsValue(form, fields, "NOME", d.getSoggetto().getNome().toUpperCase());
                setFieldsValue(form, fields, "CARICA", d.getSoggetto().getCarica().toUpperCase());

                setFieldsValue(form, fields, "nome", d.getNome().toUpperCase());
                setFieldsValue(form, fields, "cognome", d.getCognome().toUpperCase());
                setFieldsValue(form, fields, "cf", d.getCodicefiscale().toUpperCase());
                setFieldsValue(form, fields, "comune", d.getComune_di_nascita().toUpperCase());
                setFieldsValue(form, fields, "datanascita", sdfITA.format(d.getDatanascita()));
                setFieldsValue(form, fields, "sesso", estraiSessodaCF(d.getCodicefiscale().toUpperCase()));
                setFieldsValue(form, fields, "regione", d.getRegione_di_residenza().toUpperCase());
                setFieldsValue(form, fields, "pec", d.getPec().toLowerCase());
                setFieldsValue(form, fields, "mail", d.getEmail().toLowerCase());
                setFieldsValue(form, fields, "tel", d.getCellulare());

                TitoliStudio t0 = ts.stream().filter(t1 -> t1.getCodice().endsWith(String.valueOf(d.getTitolo_di_studio()))).findFirst().orElse(null);
                if (t0 != null) {
                    setFieldsValue(form, fields, "titolistudio", t0.getDescrizione().toUpperCase());
                }

                Item q0 = aq.stream().filter(t1 -> t1.getCodice() == d.getArea_prevalente_di_qualificazione()).findFirst().orElse(null);
                if (q0 != null) {
                    setFieldsValue(form, fields, "qualifiche", q0.getDescrizione().toUpperCase());
                }

                Item i0 = inq.stream().filter(t1 -> t1.getCodice() == d.getInquadramento()).findFirst().orElse(null);
                if (i0 != null) {
                    setFieldsValue(form, fields, "inquadramento", i0.getDescrizione().toUpperCase());
                }
                setFieldsValue(form, fields, "fascia", d.getFascia().getDescrizione());

                AtomicInteger indice = new AtomicInteger(1);
                d.getAttivita().forEach(at1 -> {

                    Item at0 = att.stream().filter(t1 -> t1.getCodice() == at1.getTipologia_di_attivita()).findFirst().orElse(null);
                    if (at0 != null) {
                        setFieldsValue(form, fields, "attivita" + indice.get(), at0.getDescrizione().toUpperCase());
                    }

                    setFieldsValue(form, fields, "committente" + indice.get(), at1.getCommittente().toUpperCase());

                    setFieldsValue(form, fields, "periodo" + indice.get(),
                            sdfITA.format(at1.getData_inizio_periodo_di_riferimento()) + " "
                            + sdfITA.format(at1.getData_fine_periodo_di_riferimento()));

                    Item um0 = um.stream().filter(t1 -> t1.getCod().equals(at1.getUnita_di_misura())).findFirst().orElse(null);
                    if (um0 != null) {
                        setFieldsValue(form, fields, "um" + indice.get(), um0.getDescrizione().toUpperCase());
                    }
                    setFieldsValue(form, fields, "du" + indice.get(), String.valueOf(at1.getDurata()).toUpperCase());

                    Item in0 = inq.stream().filter(t1 -> t1.getCodice() == at1.getTipologia_di_incarico()).findFirst().orElse(null);
                    if (in0 != null) {
                        setFieldsValue(form, fields, "incarico" + indice.get(), in0.getDescrizione().toUpperCase());
                    }

                    Item fi0 = fon.stream().filter(t1 -> t1.getCodice() == at1.getFonte_di_finanziamento()).findFirst().orElse(null);
                    if (fi0 != null) {
                        setFieldsValue(form, fields, "finanziamento" + indice.get(), fi0.getDescrizione().toUpperCase());
                    }
                    setFieldsValue(form, fields, "rif" + indice.get(), String.valueOf(at1.getRiferimento()));

                    indice.addAndGet(1);
                });

                fields.forEach((KEY, VALUE) -> {
                    form.partialFormFlattening(KEY);
                });

                form.flattenFields();
                form.flush();

                BarcodeQRCode barcode = new BarcodeQRCode(username + " / ALLEGATOB1 / "
                        + StringUtils.deleteWhitespace(d.getCognome() + "_" + d.getNome())
                        + " / " + dataconsegna.toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);
            }
            if (checkPDF(pdfOut)) {
                return pdfOut;
            }
        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;

    }

    private static File ESITOVALUTAZIONE_BASE(
            String startpath,
            Entity e,
            String username,
            SoggettiAttuatori sa,
            ProgettiFormativi pf,
            DateTime dataconsegna,
            boolean flatten) {

        try {

            TipoDoc p = e.getEm().find(TipoDoc.class, 36L);
            String contentb64 = p.getModello();

            List<Allievi> allievi_faseA = Utility.allievi_fa(pf.getId(), e.getAllieviProgettiFormativi(pf));
            List<Docenti> docenti_tab = Utility.docenti_ore_A(pf.getId(), pf.getDocenti());
//            Map<Long, Long> oreRendicontabili_faseA = Action.OreRendicontabiliAlunni_faseA((int) (long) pf.getId());
            Map<Long, Long> oreRendicontabili_faseB = Action.OreRendicontabiliAlunni_faseB((int) (long) pf.getId());
            Map<Long, Long> oreRendicontabili_docenti = Action.OreRendicontabiliDocentiFASEA((int) (long) pf.getId());

            File pdfOut = new File(startpath + username + "_"
                    + getOnlyStrings(sa.getRagionesociale()) + "_"
                    + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".EV.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {

                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);

                Map<String, PdfFormField> fields = form.getAllFormFields();

                //PAG 1
                setFieldsValue(form, fields, "NOMESA", sa.getRagionesociale().toUpperCase());
                setFieldsValue(form, fields, "CIP", pf.getCip());
                setFieldsValue(form, fields, "PROT", sa.getProtocollo());
                setFieldsValue(form, fields, "DATA", dataconsegna.toString(patternITA));
                setFieldsValue(form, fields, "DATAINIZIO", sdfITA.format(pf.getStart()));
                setFieldsValue(form, fields, "DATAFINE", sdfITA.format(pf.getEnd()));
                setFieldsValue(form, fields, "TOTALE", roundDoubleAndFormat(pf.getChecklist_finale().getTot_contributo_ammesso()) + " €");

                List<OreId> list_orecontrollatefaseA = Arrays.asList(new ObjectMapper().readValue(pf.getChecklist_finale().getTab_neet_fa(), OreId[].class));
                List<OreId> list_orecontrollatefaseB = Arrays.asList(new ObjectMapper().readValue(pf.getChecklist_finale().getTab_neet_fb(), OreId[].class));
                List<OutputId> list_completi = Arrays.asList(new ObjectMapper().readValue(pf.getChecklist_finale().getTab_completezza_output_neet(), OutputId[].class));
//                List<OutputId> list_outputfaseA = Arrays.asList(new ObjectMapper().readValue(pf.getChecklist_finale().getTab_completezza_output_neet(), OutputId[].class));

                AtomicInteger indice1 = new AtomicInteger(1);
                allievi_faseA.forEach(al1 -> {
                    setFieldsValue(form, fields, "Cognome" + indice1.get(), al1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "Nome" + indice1.get(), al1.getNome().toUpperCase());
                    setFieldsValue(form, fields, "CF" + indice1.get(), al1.getCodicefiscale().toUpperCase());

                    OreId orea = list_orecontrollatefaseA.stream().filter(al2 -> al2.getId().equals(String.valueOf(al1.getId()))).findAny().orElse(null);
                    if (orea != null) {
                        setFieldsValue(form, fields, "TOTALEA" + indice1.get(),
                                Utility.roundFloatAndFormat(Float.parseFloat(orea.getOre()), false));
                    }
                    if (oreRendicontabili_faseB.get(al1.getId()) != null) {
                        OreId oreb = list_orecontrollatefaseB.stream().filter(al2 -> al2.getId().equals(String.valueOf(al1.getId()))).findAny().orElse(null);
                        if (oreb != null) {
                            setFieldsValue(form, fields, "TOTALEB" + indice1.get(), roundFloatAndFormat(Float.parseFloat(oreb.getOre()), false));
                        }
                    }

                    indice1.addAndGet(1);
                });

                AtomicInteger indice2 = new AtomicInteger(1);

                docenti_tab.forEach(d1 -> {

                    setFieldsValue(form, fields, "CognomeD_A" + indice2.get(),
                            d1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "NomeD_A" + indice2.get(),
                            d1.getNome().toUpperCase());
                    setFieldsValue(form, fields, "FASCIAD_A" + indice2.get(),
                            d1.getFascia().getDescrizione());
                    setFieldsValue(form, fields, "TOTALED_B" + indice2.get(),
                            roundFloatAndFormat(oreRendicontabili_docenti.get(d1.getId()), true));
                    indice2.addAndGet(1);
                });

                //PAG 2
                AtomicInteger indice3 = new AtomicInteger(1);
                list_completi.stream().filter(m1 -> m1.getOutput().equals("0")).collect(Collectors.toList()).forEach(m2 -> {

                    Allievi ako = allievi_faseA.stream().filter(al1 -> String.valueOf(al1.getId()).equals(m2.getId())).findFirst().orElse(null);

                    if (ako != null) {
                        setFieldsValue(form, fields, "ERR_Cognome" + indice3.get(), ako.getCognome().toUpperCase());
                        setFieldsValue(form, fields, "ERR_Nome" + indice3.get(), ako.getNome().toUpperCase());
                        setFieldsValue(form, fields, "ERR_CF" + indice3.get(), ako.getCodicefiscale().toUpperCase());
                        indice3.addAndGet(1);
                    }

                });

                if (flatten) {
                    form.flattenFields();
                    form.flush();
                }

                BarcodeQRCode barcode = new BarcodeQRCode(username
                        + " / ESITO VALUTAZIONE / "
                        + StringUtils.deleteWhitespace(sa.getRagionesociale())
                        + " / " + dataconsegna.toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);
            }
            if (checkPDF(pdfOut)) {
                return pdfOut;
            }

        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;

    }

    private static File CHECKLIST_BASE(
            String startpath,
            Entity e,
            String username,
            SoggettiAttuatori sa,
            ProgettiFormativi pf,
            DateTime dataconsegna,
            boolean flatten) {

        try {

            TipoDoc p = e.getEm().find(TipoDoc.class, 37L);
            String contentb64 = p.getModello();

            List<Allievi> allievi_totali = e.getAllieviProgettiFormativi(pf);
            int allieviOK = Utility.allieviOK(pf.getId(), allievi_totali);

            List<Allievi> allievi_faseA = Utility.allievi_fa(pf.getId(), e.getAllieviProgettiFormativi(pf));
            List<Docenti> docenti_tab = Utility.docenti_ore_A(pf.getId(), pf.getDocenti());
            Map<Long, Long> oreRendicontabili_faseA = Action.OreRendicontabiliAlunni_faseA((int) (long) pf.getId());
            Map<Long, Long> oreRendicontabili_faseB = Action.OreRendicontabiliAlunni_faseB((int) (long) pf.getId());
            Map<Long, Long> oreRendicontabili_docenti = Action.OreRendicontabiliDocentiFASEA((int) (long) pf.getId());
            String coeff_fa = e.getPath("coeff.allievo.fasea");
            String coeff_fb = e.getPath("coeff.allievo.faseb");

            String coeff_fasciaA = e.getPath("coeff.docente.a");
            String coeff_fasciaB = e.getPath("coeff.docente.b");

//            List<Revisori> controllori = e.findAll(Revisori.class);
            Map<String, String> fasceDocenti = Utility.mapCoeffDocenti(coeff_fasciaA, coeff_fasciaB);

            File pdfOut = new File(startpath + username + "_"
                    + getOnlyStrings(sa.getRagionesociale()) + "_"
                    + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".CL_FIN.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {

                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);

                Map<String, PdfFormField> fields = form.getAllFormFields();

                //PAG 1
                setFieldsValue(form, fields, "NOMESA", sa.getRagionesociale().toUpperCase());
                setFieldsValue(form, fields, "CIP", pf.getCip());
                setFieldsValue(form, fields, "PROT", sa.getProtocollo());
                setFieldsValue(form, fields, "CFSA", sa.getCodicefiscale().toUpperCase());
                setFieldsValue(form, fields, "PIVASA", sa.getPiva().toUpperCase());
                setFieldsValue(form, fields, "DATAINIZIO", sdfITA.format(pf.getStart()));
                setFieldsValue(form, fields, "DATAFINE", sdfITA.format(pf.getEnd()));
                setFieldsValue(form, fields, "ISCRITTI", String.valueOf(allievi_totali.size()));
                setFieldsValue(form, fields, "TERMINATI", String.valueOf(allieviOK));

                if (pf.getChecklist_finale().getTipo().equals("ASSENZA")) {
                    setFieldsValue(form, fields, "INPS", "SI");
                    setFieldsValue(form, fields, "DURC", "NO");
                } else {
                    setFieldsValue(form, fields, "DURC", "SI");
                    setFieldsValue(form, fields, "INPS", "NO");
                }
                //PAG 2

                List<OreId> list_orecontrollatefaseA = Arrays.asList(new ObjectMapper().readValue(pf.getChecklist_finale().getTab_neet_fa(), OreId[].class));
                List<OreId> list_orecontrollatefaseB = Arrays.asList(new ObjectMapper().readValue(pf.getChecklist_finale().getTab_neet_fb(), OreId[].class));

                List<MappaturaId> list_mappatifaseA = Arrays.asList(new ObjectMapper().readValue(pf.getChecklist_finale().getTab_mappatura_neet(), MappaturaId[].class));
                List<OutputId> list_outputfaseA = Arrays.asList(new ObjectMapper().readValue(pf.getChecklist_finale().getTab_completezza_output_neet(), OutputId[].class));

                AtomicInteger indice1 = new AtomicInteger(1);

                setFieldsValue(form, fields, "IMPORTONEETA", roundDoubleAndFormat(Double.parseDouble(coeff_fa)) + " €");
                setFieldsValue(form, fields, "IMPORTO ORARIO RICONOSCIUTOCONTROLL O ORE PRESENZE ALLIEVI  FASE B", roundDoubleAndFormat(Double.parseDouble(coeff_fb)) + " €");

                allievi_faseA.forEach(al1 -> {
                    setFieldsValue(form, fields, "COGNOMERow" + indice1.get(), al1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "NOMERow" + indice1.get(), al1.getNome().toUpperCase());
                    setFieldsValue(form, fields, "ORENEETARow" + indice1.get(), roundFloatAndFormat(oreRendicontabili_faseA.get(al1.getId()), true));

                    OreId orea = list_orecontrollatefaseA.stream().filter(al2 -> al2.getId().equals(String.valueOf(al1.getId()))).findAny().orElse(null);
                    if (orea != null) {
                        setFieldsValue(form, fields, "C_ORENEETARow" + indice1.get(),
                                Utility.roundFloatAndFormat(Float.parseFloat(orea.getOre()), false));

                        setFieldsValue(form, fields, "IMPORTONEETARow" + indice1.get(), roundDoubleAndFormat(Double.parseDouble(coeff_fa)));

//                        float tota = Float.parseFloat(orea.getOre()) * Float.parseFloat(coeff_fa);
//                        totalefasea.addAndGet(Double.parseDouble(orea.getOre()) * Double.parseDouble(coeff_fa));
                        setFieldsValue(form, fields, "TOTALE FASE ARow" + indice1.get(), roundFloatAndFormat(Float.parseFloat(orea.getTotale()), false));

                    }

                    if (oreRendicontabili_faseB.get(al1.getId()) != null) {

                        OreId oreb = list_orecontrollatefaseB.stream().filter(al2 -> al2.getId().equals(String.valueOf(al1.getId()))).findAny().orElse(null);

                        if (oreb != null) {

                            setFieldsValue(form, fields, "ORE PRESENZE ALLIEVI  FASE BRow" + indice1.get(),
                                    roundFloatAndFormat(oreRendicontabili_faseB.get(al1.getId()), true));

                            setFieldsValue(form, fields, "CONTROLL O ORE PRESENZE ALLIEVI  FASE BRow" + indice1.get(),
                                    Utility.roundFloatAndFormat(Float.parseFloat(oreb.getOre()), false));

                            setFieldsValue(form, fields, "IMPORTO ORARIO RICONOSCIUTORow" + (indice1.get() + 1) + "_2", roundDoubleAndFormat(Double.parseDouble(coeff_fb)));

//                            float totb = Float.parseFloat(oreb.getOre()) * Float.parseFloat(coeff_fb);
//                            totalefaseb.addAndGet(Double.parseDouble(oreb.getOre()) * Double.parseDouble(coeff_fb));
                            setFieldsValue(form, fields, "TOTALE FASE BRow" + indice1.get(), roundFloatAndFormat(Float.parseFloat(oreb.getTotale()), false));

                        }

                    }

                    MappaturaId map1 = list_mappatifaseA.stream().filter(al2 -> al2.getId().equals(String.valueOf(al1.getId()))).findAny().orElse(null);
                    if (map1 != null) {
                        String map = map1.getMappato().equalsIgnoreCase("1") ? "SI" : "NO";
                        setFieldsValue(form, fields, "MAPPATURA IN CHIUSURARow" + indice1.get(), map);
                    }

                    OutputId out1 = list_outputfaseA.stream().filter(al2 -> al2.getId().equals(String.valueOf(al1.getId()))).findAny().orElse(null);
                    if (out1 != null) {
                        String conf = out1.getOutput().equalsIgnoreCase("1") ? "SI" : "NO";
                        setFieldsValue(form, fields, "OUTPUT CONFORMERow" + indice1.get(), conf);
                    }

                    indice1.addAndGet(1);
                });

//                setFieldsValue(form, fields, "TOTALE FASE ATOTALE CONTRIBUTO INDENNITA DI FREQUENZA  FASE A", roundDoubleAndFormat(totalefasea.get()) + " €");
//                setFieldsValue(form, fields, "TOTALE FASE BTOTALE CONTRIBUTO FASE B", roundDoubleAndFormat(totalefaseb.get()) + " €");
                setFieldsValue(form, fields, "TOTALE FASE ATOTALE CONTRIBUTO INDENNITA DI FREQUENZA  FASE A", roundDoubleAndFormat(pf.getChecklist_finale().getTot_contributo_indennita_frequenza_fa()) + " €");
                setFieldsValue(form, fields, "TOTALE FASE BTOTALE CONTRIBUTO FASE B", roundDoubleAndFormat(pf.getChecklist_finale().getTot_contributo_fb()) + " €");

                AtomicInteger indice2 = new AtomicInteger(1);

                setFieldsValue(form, fields, "IMPORTO ORARIO RICONOSCIUTOFASCIA DI APPARTENENZA RICONOSCIUTA",
                        "A: " + roundDoubleAndFormat(Double.parseDouble(coeff_fasciaA)) + " € B: " + roundDoubleAndFormat(Double.parseDouble(coeff_fasciaB))
                );

                docenti_tab.forEach(d1 -> {

                    setFieldsValue(form, fields, "COGNOMERow" + indice2.get() + "_2", d1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "NOMERow" + indice2.get() + "_2", d1.getNome().toUpperCase());

                    setFieldsValue(form, fields, "CONTROLLO ORE PRESENZE DOCENTE  FASE ARow" + indice2.get(),
                            roundFloatAndFormat(oreRendicontabili_docenti.get(d1.getId()), true));

                    setFieldsValue(form, fields, "FASCIA DI APPARTENENZA RICONOSCIUTARow" + indice2.get(),
                            d1.getFascia().getDescrizione());

                    setFieldsValue(form, fields, "IMPORTO ORARIO RICONOSCIUTORow" + (indice2.get() + 1) + "_3",
                            roundDoubleAndFormat(Double.parseDouble(fasceDocenti.get(d1.getFascia().getId()))));

                    float tota = Float.parseFloat(convertToHours_R(oreRendicontabili_docenti.get(d1.getId())))
                            * Float.parseFloat(fasceDocenti.get(d1.getFascia().getId()));

                    setFieldsValue(form, fields, "TOTALE FASE ARow" + indice2.get() + "_2", roundFloatAndFormat(tota, false));

                    indice2.addAndGet(1);
                });

                setFieldsValue(form, fields, "TOTALE FASE ATOTALE DOCENZA  FASE A", roundDoubleAndFormat(pf.getChecklist_finale().getTot_docenza_fa()) + " €");

                //PAG 3
                setFieldsValue(form, fields, "TOTALE OUTPUT CONFORMI", String.valueOf(pf.getChecklist_finale().getTot_output_conformi()));
                setFieldsValue(form, fields, "N ALLIEVI CON OUTPUT AMMISSIBILE C", String.valueOf(pf.getChecklist_finale().getTot_output_conformi()));
                setFieldsValue(form, fields, "NOTE CONTROLLORERow1", pf.getChecklist_finale().getNota_controllore());

                setFieldsValue(form, fields, "A TOTALE MASSIMO AMMISSIBILERow1", roundDoubleAndFormat(pf.getChecklist_finale().getTot_massimo_ammissibile()));
                setFieldsValue(form, fields, "30 A CONDIZIONALITARow1", roundDoubleAndFormat(pf.getChecklist_finale().getCondizionalita_30perc()));
                setFieldsValue(form, fields, "70 VCR A PROCESSORow1", roundDoubleAndFormat(pf.getChecklist_finale().getVcr_70perc()));
                setFieldsValue(form, fields, "B VALORE UNITARIO CONDIZIONALITARow1", roundDoubleAndFormat(pf.getChecklist_finale().getValore_unitario_condizionalita()));
                setFieldsValue(form, fields, "TOTALE CONTRIBUTO AMMESSO BC", roundDoubleAndFormat(pf.getChecklist_finale().getTot_contributo_ammesso()) + " €");

                setFieldsValue(form, fields, "DATA CONTROLLO", dataconsegna.toString(patternITA));
                setFieldsValue(form, fields, "SIGLA CONTROLLORE", pf.getChecklist_finale().getRevisore().getDescrizione());

                if (flatten) {
                    form.flattenFields();
                    form.flush();
                }

                BarcodeQRCode barcode = new BarcodeQRCode(username
                        + " / CHECKLIST FINALE / "
                        + StringUtils.deleteWhitespace(sa.getRagionesociale())
                        + " / " + dataconsegna.toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);

            }

            if (checkPDF(pdfOut)) {
                return pdfOut;
            }

        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;

    }

    private static File CERTIFICAZIONEASSENZA_BASE(
            Entity e,
            String username,
            SoggettiAttuatori sa,
            DateTime dataconsegna,
            boolean flatten) {
        try {

            TipoDoc p = e.getEm().find(TipoDoc.class, 35L);
            String contentb64 = p.getModello();

            String pathtemp = e.getPath("pathtemp");
            createDir(pathtemp);

            File pdfOut = new File(pathtemp + username + "_"
                    + getOnlyStrings(sa.getRagionesociale()) + "_"
                    + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".AssenzaINPS.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is)) {
                PdfWriter writer = new PdfWriter(pdfOut);
                PdfDocument pdfDoc = new PdfDocument(reader, writer);
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);

                Map<String, PdfFormField> fields = form.getAllFormFields();

                setFieldsValue(form, fields, "COGNOME", sa.getCognome().toUpperCase());
                setFieldsValue(form, fields, "NOME", sa.getNome().toUpperCase());
                setFieldsValue(form, fields, "NOMESA", sa.getRagionesociale().toUpperCase());
                setFieldsValue(form, fields, "INDIRIZZOSEDE", sa.getIndirizzo().toUpperCase() + " - " + sa.getComune().getNome());

//                setFieldsValue(form, fields, "CFSA", sa.getCodicefiscale().toUpperCase());
                setFieldsValue(form, fields, "PIVASA", sa.getPiva().toUpperCase());

                setFieldsValue(form, fields, "DATA", dataconsegna.toString(patternITA));

                if (flatten) {
                    form.flattenFields();
                    form.flush();
                }

//                BarcodeQRCode barcode = new BarcodeQRCode(username
//                        + " / ASSENZA POSIZIONE / "
//                        + StringUtils.deleteWhitespace(sa.getRagionesociale())
//                        + " / " + dataconsegna.toString("ddMMyyyyHHmmSSS"));
//                printbarcode(barcode, pdfDoc);
                pdfDoc.close();
                writer.close();

            }

            if (checkPDF(pdfOut)) {
                return pdfOut;
            }

        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;
    }

    private static File MODELLO7_BASE(
            Entity e,
            String username,
            Allievi al,
            String orerendicontabili,
            DateTime dataconsegna,
            boolean flatten) {

        try {

            TipoDoc_Allievi p = e.getEm().find(TipoDoc_Allievi.class, 22L);
            String contentb64 = p.getModello();

            String pathtemp = e.getPath("pathtemp");
            createDir(pathtemp);
            File pdfOut = new File(pathtemp + username + "_" + StringUtils.deleteWhitespace(al.getCognome() + "_" + al.getNome()) + "_" + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".M7.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is)) {
                PdfWriter writer = new PdfWriter(pdfOut);
                PdfDocument pdfDoc = new PdfDocument(reader, writer);
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);
                Map<String, PdfFormField> fields = form.getAllFormFields();

                setFieldsValue(form, fields, "COGNOME", al.getCognome().toUpperCase());
                setFieldsValue(form, fields, "NOME", al.getNome().toUpperCase());
                setFieldsValue(form, fields, "CF", al.getCodicefiscale().toUpperCase());
                setFieldsValue(form, fields, "COMUNENASCITA", al.getComune_nascita().getNome().toUpperCase());
                setFieldsValue(form, fields, "PROVINCIANASCITA", al.getComune_nascita().getCod_provincia().toUpperCase());
                setFieldsValue(form, fields, "DATANASCITA", sdfITA.format(al.getDatanascita()));
                setFieldsValue(form, fields, "ORE", orerendicontabili);
                setFieldsValue(form, fields, "CIP", al.getProgetto().getCip());
                setFieldsValue(form, fields, "DATAINIZIO", sdfITA.format(al.getProgetto().getStart()));
                setFieldsValue(form, fields, "DATAFINE", sdfITA.format(al.getProgetto().getEnd()));
                setFieldsValue(form, fields, "NOMESA", al.getSoggetto().getRagionesociale().toUpperCase());
                setFieldsValue(form, fields, "LUOGODATA", al.getSoggetto().getComune().getNome().toUpperCase() + "; " + dataconsegna.toString(patternITA));

                fields.forEach((KEY, VALUE) -> {
                    form.partialFormFlattening(KEY);
                });
                if (flatten) {

                    form.flattenFields();
                    form.flush();
                }

                BarcodeQRCode barcode = new BarcodeQRCode(username + " / MODELLO7 / "
                        + StringUtils.deleteWhitespace(al.getCognome() + "_" + al.getNome())
                        + " / " + dataconsegna.toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);
                pdfDoc.close();
                writer.close();
            }
            if (checkPDF(pdfOut)) {
                return pdfOut;
            }
        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;
    }

    private static File MODELLO6_BASE(
            Entity e,
            String username,
            SoggettiAttuatori sa,
            ProgettiFormativi pf,
            ModelliPrg m6,
            DateTime dataconsegna,
            boolean flatten) {

        try {

            Database d1 = new Database(false);
            List<Registro_completo> out = d1.registro_modello6(String.valueOf(pf.getId()));
            d1.closeDB();

            TipoDoc p = e.getEm().find(TipoDoc.class, 31L);
            String contentb64 = p.getModello();

            String pathtemp = e.getPath("pathtemp");
            createDir(pathtemp);

            File pdfOut = new File(pathtemp + username + "_"
                    + getOnlyStrings(sa.getRagionesociale()) + "_"
                    + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".M6.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut)) {
                PdfDocument pdfDoc = new PdfDocument(reader, writer);
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);

                Map<String, PdfFormField> fields = form.getAllFormFields();

                //PAG.1
                setFieldsValue(form, fields, "NOMESA", sa.getRagionesociale().toUpperCase());
                setFieldsValue(form, fields, "DD", sa.getDd());
                setFieldsValue(form, fields, "CIP", pf.getCip());
                setFieldsValue(form, fields, "COGNOME", sa.getCognome().toUpperCase());
                setFieldsValue(form, fields, "NOME", sa.getNome().toUpperCase());
                setFieldsValue(form, fields, "CF", sa.getCodicefiscale().toUpperCase());
                setFieldsValue(form, fields, "CARICA", sa.getCarica().toUpperCase());

                setFieldsValue(form, fields, "DATAINIZIO", sdfITA.format(pf.getStart()));
                setFieldsValue(form, fields, "DATAFINE", sdfITA.format(pf.getEnd()));

                if (m6.getScelta_modello6() == 1) {
                    setFieldsValue(form, fields, "SEDE1", "On");
                    setFieldsValue(form, fields, "COMUNESEDE", "");
                    setFieldsValue(form, fields, "PROVINCIASEDE", "");
                    setFieldsValue(form, fields, "INDIRIZZOSEDE", "");
                    setFieldsValue(form, fields, "CIVICOSEDE", "");
                } else if (m6.getScelta_modello6() == 2) {
                    setFieldsValue(form, fields, "SEDE2", "On");
                    setFieldsValue(form, fields, "COMUNESEDE", m6.getComune_modello6().getNome().toUpperCase());
                    setFieldsValue(form, fields, "PROVINCIASEDE", m6.getComune_modello6().getProvincia().toUpperCase());
                    setFieldsValue(form, fields, "INDIRIZZOSEDE", m6.getIndirizzo_modello6().toUpperCase());
                    setFieldsValue(form, fields, "CIVICOSEDE", m6.getCivico_modello6());
                }

                //PAG.2
                setFieldsValue(form, fields, "DATA", dataconsegna.toString(patternITA));
                setFieldsValue(form, fields, "COGNOMENOME", sa.getCognome().toUpperCase() + " " + sa.getNome().toUpperCase());

                //PAGINA 3
                ModelliPrg m3 = Utility.filterModello3(pf.getModelli());

                LinkedList<String> lezioniA = m3.getLezioni().stream()
                        .map(l1 -> new DateTime(l1.getGiorno())
                        .toString("dd/MM/yyyy")).distinct()
                        .collect(Collectors.toCollection(LinkedList::new));

                if (!lezioniA.isEmpty()) {
                    String DATAINIZIOFASEA = lezioniA.getFirst();
                    String DATAFINEFASEA = lezioniA.getLast();

//                    System.out.println("A) " + DATAINIZIOFASEA + " -- " + DATAFINEFASEA);
                    List<Registro_completo> faseA = out.stream().filter(r1
                            -> r1.getRuolo().contains("NEET")
                            && r1.getFase().equalsIgnoreCase("A"))
                            .collect(Collectors.toList());

                    List<Registro_completo> allieviFaseA = new ArrayList<>();
                    List<Integer> neetID = faseA.stream().map(r1 -> r1.getIdutente()).distinct().collect(Collectors.toList());
                    AtomicInteger index_allieviA = new AtomicInteger(1);

                    neetID.forEach(n1 -> {
                        Allievi n2 = e.getEm().find(Allievi.class, Long.valueOf(String.valueOf(n1)));
                        MascheraM5 datiM5 = e.getM5_byAllievo(n2);

                        Registro_completo allievo_A = new Registro_completo();
                        allievo_A.setId(index_allieviA.get());
                        allievo_A.setCognome(n2.getCognome().toUpperCase());
                        allievo_A.setNome(n2.getNome().toUpperCase());
                        allievo_A.setCf(n2.getCodicefiscale().toUpperCase());
                        allievo_A.setDatapattogg(sdfITA.format(n2.getIscrizionegg()));

                        if (datiM5 != null) {
                            allievo_A.setDomandaammissione(Utility.convertbooleantostring(datiM5.isDomanda_ammissione_presente()));
                            allievo_A.setModello5("SI");
                        } else {
                            allievo_A.setDomandaammissione("NO");
                            allievo_A.setModello5("NO");
                        }

                        Map<Integer, Long> orario = new HashMap<>();

                        AtomicLong totaleA = new AtomicLong(0L);

                        AtomicInteger indicigiorni = new AtomicInteger(1);
                        lezioniA.forEach(giorno1 -> {

                            List<Registro_completo> n3 = faseA.stream().filter(
                                    r3
                                    -> r3.getIdutente() == n1
                                    && r3.getData().toString("dd/MM/yyyy").equals(giorno1)
                            ).collect(Collectors.toList());

                            if (n3.isEmpty()) {

                                orario.put(indicigiorni.get(), 0L);

                            } else {

                                n3.forEach(r3 -> {
                                    long ADD = r3.getTotaleorerendicontabili();
                                    if (Utility.demoversion && ADD > 18000000L) {
                                        ADD = 18000000L;
                                    }

                                    totaleA.addAndGet(ADD);
                                    long valore = orario.getOrDefault(indicigiorni.get(), 0L);
                                    if (valore == 0L) {
                                        orario.put(indicigiorni.get(), ADD);
                                    } else {
                                        orario.remove(indicigiorni.get());
                                        orario.put(indicigiorni.get(), valore + ADD);
                                    }
                                });
                            }

                            indicigiorni.addAndGet(1);
                        });

                        allievo_A.setData1(orario.get(1));
                        allievo_A.setData2(orario.get(2));
                        allievo_A.setData3(orario.get(3));
                        allievo_A.setData4(orario.get(4));
                        allievo_A.setData5(orario.get(5));
                        allievo_A.setData6(orario.get(6));
                        allievo_A.setData7(orario.get(7));
                        allievo_A.setData8(orario.get(8));
                        allievo_A.setData9(orario.get(9));
                        allievo_A.setData10(orario.get(10));
                        allievo_A.setData11(orario.get(11));
                        allievo_A.setData12(orario.get(12));

                        allievo_A.setTotaleore(totaleA.get());
                        allieviFaseA.add(allievo_A);
                        index_allieviA.addAndGet(1);
                    });

                    setFieldsValue(form, fields, "DATAINIZIOFASEA", DATAINIZIOFASEA);
                    setFieldsValue(form, fields, "DATAFINEFASEA", DATAFINEFASEA);

                    AtomicInteger indicigiorniA = new AtomicInteger(1);
                    lezioniA.forEach(lezione -> {
                        setFieldsValue(form, fields, "DATA" + indicigiorniA.get() + "FASEA", lezione);
                        setFieldsValue(form, fields, "DATAD" + indicigiorniA.get() + "A", lezione);
                        indicigiorniA.addAndGet(1);
                    });

                    allieviFaseA.forEach(al1 -> {
                        int indice = al1.getId();
                        setFieldsValue(form, fields, "Cognome" + indice, al1.getCognome().toUpperCase());
                        setFieldsValue(form, fields, "Nome" + indice, al1.getNome().toUpperCase());
                        setFieldsValue(form, fields, "CF" + indice, al1.getCf().toUpperCase());
                        setFieldsValue(form, fields, "Data Patto GG" + indice, al1.getDatapattogg());
                        setFieldsValue(form, fields, "Domanda di ammissione" + indice, al1.getDomandaammissione());
                        setFieldsValue(form, fields, "Doc di accompag  Modello 5" + indice, al1.getModello5());

                        setFieldsValue(form, fields, "OREA1_" + indice, roundFloatAndFormat(al1.getData1(), true));
                        setFieldsValue(form, fields, "OREA2_" + indice, roundFloatAndFormat(al1.getData2(), true));
                        setFieldsValue(form, fields, "OREA3_" + indice, roundFloatAndFormat(al1.getData3(), true));
                        setFieldsValue(form, fields, "OREA4_" + indice, roundFloatAndFormat(al1.getData4(), true));
                        setFieldsValue(form, fields, "OREA5_" + indice, roundFloatAndFormat(al1.getData5(), true));
                        setFieldsValue(form, fields, "OREA6_" + indice, roundFloatAndFormat(al1.getData6(), true));
                        setFieldsValue(form, fields, "OREA7_" + indice, roundFloatAndFormat(al1.getData7(), true));
                        setFieldsValue(form, fields, "OREA8_" + indice, roundFloatAndFormat(al1.getData8(), true));
                        setFieldsValue(form, fields, "OREA9_" + indice, roundFloatAndFormat(al1.getData9(), true));
                        setFieldsValue(form, fields, "OREA10_" + indice, roundFloatAndFormat(al1.getData10(), true));
                        setFieldsValue(form, fields, "OREA11_" + indice, roundFloatAndFormat(al1.getData11(), true));
                        setFieldsValue(form, fields, "OREA12_" + indice, roundFloatAndFormat(al1.getData12(), true));

                        setFieldsValue(form, fields, "TOTALEA" + indice, roundFloatAndFormat(al1.getTotaleore(), true));

                    });

                    // PAGINA 4 - 7
                    ModelliPrg m4 = Utility.filterModello4(pf.getModelli());
                    List<Registro_completo> faseB = out.stream().filter(r1
                            -> r1.getRuolo().contains("NEET")
                            && r1.getFase().equalsIgnoreCase("B"))
                            .collect(Collectors.toList());

                    List<Registro_completo> allieviFaseB = new ArrayList<>();
                    for (int i = 1; i < 13; i++) {

                        AtomicInteger indiceb = new AtomicInteger(i);

                        LinkedList<String> lezioniB = m4.getLezioni().stream().filter(l1 -> l1.getGruppo_faseB() == indiceb.get())
                                .map(l1 -> new DateTime(l1.getGiorno())
                                .toString("dd/MM/yyyy")).distinct()
                                .collect(Collectors.toCollection(LinkedList::new));
                        if (lezioniB.isEmpty()) {
//                            System.out.println("B) GRUPPO " + i + " VUOTO");
                        } else {
                            String DATAINIZIOFASEB = lezioniB.getFirst();
                            String DATAFINEFASEB = lezioniB.getLast();

                            List<Registro_completo> grupposingolo = faseB.stream().filter(r2 -> r2.getGruppofaseb()
                                    == indiceb.get()).collect(Collectors.toList());

                            neetID = grupposingolo.stream().map(r1 -> r1.getIdutente()).distinct().collect(Collectors.toList());
                            AtomicInteger index_allieviB = new AtomicInteger(1);
                            neetID.forEach(n1 -> {
                                Allievi n2 = e.getEm().find(Allievi.class, Long.parseLong(String.valueOf(n1)));

                                Registro_completo allievo_B = new Registro_completo();
                                allievo_B.setGruppoB(indiceb.get());
                                allievo_B.setId(index_allieviB.get());
                                allievo_B.setCognome(n2.getCognome().toUpperCase());
                                allievo_B.setNome(n2.getNome().toUpperCase());
                                allievo_B.setCf(n2.getCodicefiscale().toUpperCase());

                                Map<Integer, Long> orarioB = new HashMap<>();

                                AtomicLong totaleB = new AtomicLong(0L);
                                AtomicInteger indicigiorni = new AtomicInteger(1);
                                lezioniB.forEach(giorno1 -> {
                                    List<Registro_completo> n3 = grupposingolo.stream().filter(
                                            r3
                                            -> r3.getIdutente() == n1
                                            && r3.getData().toString("dd/MM/yyyy")
                                                    .equals(giorno1))
                                            .collect(Collectors.toList());

                                    if (n3.isEmpty()) {
                                        orarioB.put(indicigiorni.get(), 0L);
                                    } else {
                                        n3.forEach(r3 -> {

                                            long ADD = r3.getTotaleorerendicontabili();
                                            if (Utility.demoversion && ADD > 18000000L) {
                                                ADD = 18000000L;
                                            }

                                            totaleB.addAndGet(ADD);
                                            long valore = orarioB.getOrDefault(indicigiorni.get(), 0L);
                                            if (valore == 0L) {
                                                orarioB.put(indicigiorni.get(), ADD);
                                            } else {
                                                orarioB.remove(indicigiorni.get());
                                                orarioB.put(indicigiorni.get(), valore + ADD);
                                            }
                                        });
                                    }
                                    indicigiorni.addAndGet(1);
                                });
                                try {
                                    allievo_B.setData1(orarioB.get(1));
                                    allievo_B.setData2(orarioB.get(2));
                                    allievo_B.setData3(orarioB.get(3));
                                    allievo_B.setData4(orarioB.get(4));
                                    allievo_B.setTotaleore(totaleB.get());
                                    allieviFaseB.add(allievo_B);
                                } catch (Exception exx) {
                                }

//                                System.out.println(allievo_B.toString());
                                index_allieviB.addAndGet(1);
                            });

                            setFieldsValue(form, fields, "DATAINIZIOB" + indiceb.get(), DATAINIZIOFASEB);
                            setFieldsValue(form, fields, "DATAFINEB" + indiceb.get(), DATAFINEFASEB);

                            AtomicInteger indicigiorniB = new AtomicInteger(1);
                            lezioniB.forEach(lezione -> {
                                setFieldsValue(form, fields, "DATAB" + indiceb.get() + "_" + indicigiorniB.get(), lezione);
                                indicigiorniB.addAndGet(1);
                            });

                            allieviFaseB.forEach(al1 -> {
                                int indice = al1.getId();

                                setFieldsValue(form, fields, "CognomeB" + al1.getGruppoB() + "_" + indice, al1.getCognome().toUpperCase());
                                setFieldsValue(form, fields, "NomeB" + al1.getGruppoB() + "_" + indice, al1.getNome().toUpperCase());
                                setFieldsValue(form, fields, "CFB" + al1.getGruppoB() + "_" + indice, al1.getCf().toUpperCase());

                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_1", roundFloatAndFormat(al1.getData1(), true));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_2", roundFloatAndFormat(al1.getData2(), true));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_3", roundFloatAndFormat(al1.getData3(), true));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_4", roundFloatAndFormat(al1.getData4(), true));

                                setFieldsValue(form, fields, "TOTALEB" + al1.getGruppoB() + "_" + indice, roundFloatAndFormat(al1.getTotaleore(), true));

                            });

                        }

                    }

                    //PAGINA 8 
                    List<Registro_completo> docentifaseA = out.stream().filter(r1
                            -> r1.getRuolo().equalsIgnoreCase("DOCENTE")
                            && r1.getFase().equalsIgnoreCase("A")).collect(Collectors.toList());

                    List<Integer> docentiid = docentifaseA.stream().map(r1
                            -> r1.getIdutente()).distinct().collect(Collectors.toList());

                    List<Registro_completo> docenti = new ArrayList<>();

                    AtomicLong totaleD_A = new AtomicLong(0L);

                    AtomicInteger index_docenti = new AtomicInteger(1);
                    docentiid.forEach(r1 -> {
                        Docenti docente = e.getEm().find(Docenti.class, Long.parseLong(String.valueOf(r1)));
                        if (docente != null) {
                            Registro_completo doc1 = new Registro_completo();
                            doc1.setId(index_docenti.get());
                            index_docenti.addAndGet(1);

                            doc1.setCognome(docente.getCognome().toUpperCase());
                            doc1.setNome(docente.getNome().toUpperCase());
                            doc1.setCf(docente.getCodicefiscale().toUpperCase());
                            doc1.setFascia(docente.getFascia().getDescrizione().split(" ")[1]);

                            Map<Integer, Long> orarioD = new HashMap<>();
                            AtomicLong totaleA = new AtomicLong(0L);

                            AtomicInteger indicigiorni = new AtomicInteger(1);
                            lezioniA.forEach(giorno1 -> {
                                List<Registro_completo> doc3 = docentifaseA.stream().filter(
                                        r3 -> r3.getIdutente() == r1
                                        && r3.getData().toString("dd/MM/yyyy").equals(giorno1))
                                        .collect(Collectors.toList());

                                if (doc3.isEmpty()) {
                                    orarioD.put(indicigiorni.get(), 0L);
                                } else {

                                    doc3.forEach(r3 -> {
                                        totaleA.addAndGet(r3.getTotaleorerendicontabili());
                                        totaleD_A.addAndGet(r3.getTotaleorerendicontabili());

                                        long valore = orarioD.getOrDefault(indicigiorni.get(), 0L);
                                        if (valore == 0L) {
                                            orarioD.put(indicigiorni.get(), r3.getTotaleorerendicontabili());
                                        } else {
                                            orarioD.remove(indicigiorni.get());
                                            orarioD.put(indicigiorni.get(), valore + r3.getTotaleorerendicontabili());
                                        }
                                    });
                                }
                                indicigiorni.addAndGet(1);

                            });

                            doc1.setData1(orarioD.get(1));
                            doc1.setData2(orarioD.get(2));
                            doc1.setData3(orarioD.get(3));
                            doc1.setData4(orarioD.get(4));
                            doc1.setData5(orarioD.get(5));
                            doc1.setData6(orarioD.get(6));
                            doc1.setData7(orarioD.get(7));
                            doc1.setData8(orarioD.get(8));
                            doc1.setData9(orarioD.get(9));
                            doc1.setData10(orarioD.get(10));
                            doc1.setData11(orarioD.get(11));
                            doc1.setData12(orarioD.get(12));
                            doc1.setTotaleore(totaleA.get());
                            docenti.add(doc1);
                        }
                    });

                    docenti.forEach(doc1 -> {
                        int indice = doc1.getId();

                        setFieldsValue(form, fields, "CognomeD_A" + indice, doc1.getCognome().toUpperCase());
                        setFieldsValue(form, fields, "NomeD_A" + indice, doc1.getNome().toUpperCase());
                        setFieldsValue(form, fields, "CFD_A" + indice, doc1.getCf().toUpperCase());
                        setFieldsValue(form, fields, "FASCIAD_A" + indice, doc1.getFascia());

                        setFieldsValue(form, fields, "ORED_A_" + indice + "_1", roundFloatAndFormat(doc1.getData1(), true));
                        setFieldsValue(form, fields, "ORED_A_" + indice + "_2", roundFloatAndFormat(doc1.getData2(), true));
                        setFieldsValue(form, fields, "ORED_A_" + indice + "_3", roundFloatAndFormat(doc1.getData3(), true));
                        setFieldsValue(form, fields, "ORED_A_" + indice + "_4", roundFloatAndFormat(doc1.getData4(), true));
                        setFieldsValue(form, fields, "ORED_A_" + indice + "_5", roundFloatAndFormat(doc1.getData5(), true));
                        setFieldsValue(form, fields, "ORED_A_" + indice + "_6", roundFloatAndFormat(doc1.getData6(), true));
                        setFieldsValue(form, fields, "ORED_A_" + indice + "_7", roundFloatAndFormat(doc1.getData7(), true));
                        setFieldsValue(form, fields, "ORED_A_" + indice + "_8", roundFloatAndFormat(doc1.getData8(), true));
                        setFieldsValue(form, fields, "ORED_A_" + indice + "_9", roundFloatAndFormat(doc1.getData9(), true));
                        setFieldsValue(form, fields, "ORED_A_" + indice + "_10", roundFloatAndFormat(doc1.getData10(), true));
                        setFieldsValue(form, fields, "ORED_A_" + indice + "_11", roundFloatAndFormat(doc1.getData11(), true));
                        setFieldsValue(form, fields, "ORED_A_" + indice + "_12", roundFloatAndFormat(doc1.getData12(), true));
                        setFieldsValue(form, fields, "TOTALED_B" + indice, roundFloatAndFormat(doc1.getTotaleore(), true));

                    });
                    setFieldsValue(form, fields, "TOTALED_B", roundFloatAndFormat(totaleD_A.get(), true));
                }

                if (flatten) {
                    form.flattenFields();
                    form.flush();
                }

                BarcodeQRCode barcode = new BarcodeQRCode(username
                        + " / MODELLO6 / "
                        + StringUtils.deleteWhitespace(sa.getRagionesociale())
                        + " / " + dataconsegna.toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);
                pdfDoc.close();
            }
            if (checkPDF(pdfOut)) {
                return pdfOut;
            }

        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;
    }

    private static File MODELLO5_BASE(
            Entity e,
            String contentb64,
            String username,
            SoggettiAttuatori sa,
            Allievi al,
            String[] datifrequenza,
            MascheraM5 m5,
            DateTime dataconsegna,
            boolean flatten) {
        try {

            String pathtemp = e.getPath("pathtemp");
            createDir(pathtemp);
            File pdfOut = new File(pathtemp + username + "_" + StringUtils.deleteWhitespace(al.getCognome() + "_" + al.getNome()) + "_" + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".M5.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);
                Map<String, PdfFormField> fields = form.getAllFormFields();

                String NOMESA = sa.getRagionesociale();
                String DD = sa.getDd();
                String PRESENZA = "NO";
                String FAD = "SI";
                String REGIONESEDE = "";
                String COMUNESEDE = "";
                String PROVINCIASEDE = "";
                String INDIRIZZOSEDE = "";
                String CIP = m5.getProgetto_formativo().getCip();

                setFieldsValue(form, fields, "NOMESA", NOMESA);
                setFieldsValue(form, fields, "DD", DD);
                setFieldsValue(form, fields, "REGIONESEDE", REGIONESEDE);
                setFieldsValue(form, fields, "COMUNESEDE", COMUNESEDE);
                setFieldsValue(form, fields, "PROVINCIASEDE", PROVINCIASEDE);
                setFieldsValue(form, fields, "INDIRIZZOSEDE", INDIRIZZOSEDE);

                setFieldsValue(form, fields, "cognome", al.getCognome().toUpperCase());
                setFieldsValue(form, fields, "nome", al.getNome().toUpperCase());
                setFieldsValue(form, fields, "datanascita", sdfITA.format(al.getDatanascita()));
                setFieldsValue(form, fields, "comune_nascita", al.getComune_nascita().getNome().toUpperCase());
                setFieldsValue(form, fields, "codicefiscale", al.getCodicefiscale().toUpperCase());
                setFieldsValue(form, fields, "telefono", al.getTelefono());
                setFieldsValue(form, fields, "email", al.getEmail().toLowerCase());
                setFieldsValue(form, fields, "indirizzoresidenza", al.getIndirizzoresidenza().toUpperCase() + " " + al.getCivicoresidenza().toUpperCase());
                setFieldsValue(form, fields, "comune_residenza", al.getComune_residenza().getNome().toUpperCase());
                setFieldsValue(form, fields, "cap_residenza", al.getCapresidenza());
                setFieldsValue(form, fields, "provincia_residenza", al.getComune_residenza().getCod_provincia().toUpperCase());
                setFieldsValue(form, fields, "Ore di frequenza", sdfITA.format(al.getIscrizionegg())); //GG
                setFieldsValue(form, fields, "CIP", CIP);

                setFieldsValue(form, fields, "datafinepercorso", datifrequenza[0]);

                if (Utility.demoversion) {
                    setFieldsValue(form, fields, "orefrequenza", "80h 0min 0sec");
                } else {
                    setFieldsValue(form, fields, "orefrequenza", datifrequenza[1]);
                }

                StringBuilder listadocenti = new StringBuilder("");
                m5.getProgetto_formativo().getDocenti().forEach(doc -> {
                    listadocenti.append(doc.getCognome().toUpperCase()).append(" ").append(doc.getNome().toUpperCase()).append("; ");
                });
                setFieldsValue(form, fields, "ELENCODOCENTI", listadocenti.toString());
                setFieldsValue(form, fields, "RAGIONESOCIALE", m5.getRagione_sociale().toUpperCase());
                if (m5.getForma_giuridica() != null) {
                    setFieldsValue(form, fields, "FORMAGIURIDICA", m5.getForma_giuridica().getDescrizione().toUpperCase());
                }
                setFieldsValue(form, fields, "SEDEINDIVIDUATA", Utility.convertbooleantostring(m5.isSede()));
                setFieldsValue(form, fields, "COLLOQUIO", Utility.convertbooleantostring(m5.isColloquio()));

                setFieldsValue(form, fields, "IDEAIMPRESA", m5.getIdea_impresa().toUpperCase());
                if (m5.getAteco() != null) {
                    setFieldsValue(form, fields, "ATECO", m5.getAteco().getId());
                }

                if (m5.getComune_localizzazione() != null) {
                    setFieldsValue(form, fields, "COMUNE_NUOVO", m5.getComune_localizzazione().getNome().toUpperCase());
                    setFieldsValue(form, fields, "REGIONE_NUOVO", m5.getComune_localizzazione().getRegione().toUpperCase());
                }

                setFieldsValue(form, fields, "MOTIVAZIONEATTIVITA", m5.getMotivazione().toUpperCase());
                setFieldsValue(form, fields, "FABBISOGNO", Utility.numITA.format(m5.getFabbisogno_finanziario()));
                setFieldsValue(form, fields, "RICHIESTO", Utility.numITA.format(m5.getFinanziamento_richiesto_agevolazione()));

                if (m5.isBando_se()) {
                    setFieldsValue(form, fields, "AGEVOLAZIONE1", "On");

                    if (m5.getBando_se_opzione() != null) {
                        for (int i = 1; i < 4; i++) {
                            if (m5.getBando_se_opzione().contains(String.valueOf(i))) {
                                setFieldsValue(form, fields, "AGEVOLAZIONE1_" + i, "On");
                            } else {
                                form.partialFormFlattening("AGEVOLAZIONE1_" + i);
                            }
                        }
                    } else {
                        form.partialFormFlattening("AGEVOLAZIONE1_1");
                        form.partialFormFlattening("AGEVOLAZIONE1_2");
                        form.partialFormFlattening("AGEVOLAZIONE1_3");
                    }
                } else {
                    form.partialFormFlattening("AGEVOLAZIONE1");
                    form.partialFormFlattening("AGEVOLAZIONE1_1");
                    form.partialFormFlattening("AGEVOLAZIONE1_2");
                    form.partialFormFlattening("AGEVOLAZIONE1_3");
                }

                if (m5.isBando_sud()) {
                    setFieldsValue(form, fields, "AGEVOLAZIONE2", "On");
                    if (m5.getBando_sud_opzione() != null) {
                        for (int i = 1; i < 7; i++) {
                            if (m5.getBando_sud_opzione().contains(String.valueOf(i))) {
                                setFieldsValue(form, fields, "AGEVOLAZIONE2_" + i, "On");
                            } else {
                                form.partialFormFlattening("AGEVOLAZIONE2_" + i);
                            }
                        }
                    } else {
                        form.partialFormFlattening("AGEVOLAZIONE2_1");
                        form.partialFormFlattening("AGEVOLAZIONE2_2");
                        form.partialFormFlattening("AGEVOLAZIONE2_3");
                        form.partialFormFlattening("AGEVOLAZIONE2_4");
                        form.partialFormFlattening("AGEVOLAZIONE2_5");
                        form.partialFormFlattening("AGEVOLAZIONE2_6");
                    }
                } else {
                    form.partialFormFlattening("AGEVOLAZIONE2");
                    form.partialFormFlattening("AGEVOLAZIONE2_1");
                    form.partialFormFlattening("AGEVOLAZIONE2_2");
                    form.partialFormFlattening("AGEVOLAZIONE2_3");
                    form.partialFormFlattening("AGEVOLAZIONE2_4");
                    form.partialFormFlattening("AGEVOLAZIONE2_5");
                    form.partialFormFlattening("AGEVOLAZIONE2_6");
                }

                if (m5.isBando_reg()) {
                    setFieldsValue(form, fields, "AGEVOLAZIONE3", "On");
                } else {
                    form.partialFormFlattening("AGEVOLAZIONE3");
                }

                if (m5.isNo_agevolazione()) {
                    setFieldsValue(form, fields, "AGEVOLAZIONE4", "On");
                    if (m5.getNo_agevolazione_opzione() != null) {
                        for (int i = 1; i < 4; i++) {
                            if (m5.getNo_agevolazione_opzione().contains(String.valueOf(i))) {
                                setFieldsValue(form, fields, "AGEVOLAZIONE4_" + i, "On");
                            } else {
                                form.partialFormFlattening("AGEVOLAZIONE4_" + i);
                            }
                        }
                    } else {
                        form.partialFormFlattening("AGEVOLAZIONE4_1");
                        form.partialFormFlattening("AGEVOLAZIONE4_2");
                        form.partialFormFlattening("AGEVOLAZIONE4_3");
                    }
                } else {
                    form.partialFormFlattening("AGEVOLAZIONE4");
                    form.partialFormFlattening("AGEVOLAZIONE4_1");
                    form.partialFormFlattening("AGEVOLAZIONE4_2");
                    form.partialFormFlattening("AGEVOLAZIONE4_3");
                }

                //TABELLA 1
                if (m5.isTabella_premialita()) {
                    //  PAGINA PREMIALITA   //
                    // 1=0;2=2;3=3.2;4=4.4; //
                    if (m5.getTabella_premialita_val() != null) {
                        List<String> elencovalori = Splitter.on(";").splitToList(m5.getTabella_premialita_val());
                        elencovalori.forEach(val1 -> {
                            List<String> content = Splitter.on("=").splitToList(val1);
                            if (content.size() == 3) {
                                setFieldsValue(form, fields, "PREMIALITAB" + content.get(0), content.get(1));
                                setFieldsValue(form, fields, "PREMIALITAC" + content.get(0), content.get(2));
                            }
                        });
                        setFieldsValue(form, fields, "PREMIALITAPUNTEGGIO", String.valueOf(m5.getTabella_premialita_punteggio()));
                        setFieldsValue(form, fields, "PREMIALITA", String.valueOf(m5.getTabella_premialita_totale()));
                    } else {
                        form.partialFormFlattening("PREMIALITAB1");
                        form.partialFormFlattening("PREMIALITAC1");
                        form.partialFormFlattening("PREMIALITAB2");
                        form.partialFormFlattening("PREMIALITAC2");
                        form.partialFormFlattening("PREMIALITAB3");
                        form.partialFormFlattening("PREMIALITAC3");
                        form.partialFormFlattening("PREMIALITAB4");
                        form.partialFormFlattening("PREMIALITAC4");
                        form.partialFormFlattening("PREMIALITAPUNTEGGIO");
                        form.partialFormFlattening("PREMIALITA");
                    }
                } else {
                    //  PAGINA VALUTAZIONE  //
                    if (m5.getTabella_valutazionefinale_val() != null) {
                        List<String> elencovalori = Splitter.on(";").splitToList(m5.getTabella_valutazionefinale_val());
                        elencovalori.forEach(val1 -> {
                            List<String> content = Splitter.on("=").splitToList(val1);
                            if (content.size() == 3) {
                                setFieldsValue(form, fields, "VALUTAZIONEFINALEB" + content.get(0), content.get(1));
                                setFieldsValue(form, fields, "VALUTAZIONEFINALEC" + content.get(0), content.get(2));
                            }
                        });
                        setFieldsValue(form, fields, "VALUTAZIONEFINALEPUNTEGGIO", String.valueOf(m5.getTabella_valutazionefinale_punteggio()));
                        setFieldsValue(form, fields, "VALUTAZIONEFINALE", String.valueOf(m5.getTabella_valutazionefinale_totale()));
                    } else {
                        form.partialFormFlattening("VALUTAZIONEFINALEB1");
                        form.partialFormFlattening("VALUTAZIONEFINALEC1");
                        form.partialFormFlattening("VALUTAZIONEFINALEB2");
                        form.partialFormFlattening("VALUTAZIONEFINALEC2");
                        form.partialFormFlattening("VALUTAZIONEFINALEB3");
                        form.partialFormFlattening("VALUTAZIONEFINALEC3");
                        form.partialFormFlattening("VALUTAZIONEFINALEB4");
                        form.partialFormFlattening("VALUTAZIONEFINALEC4");
                        form.partialFormFlattening("VALUTAZIONEFINALEPUNTEGGIO");
                        form.partialFormFlattening("VALUTAZIONEFINALE");
                    }
                }
                fields.forEach((KEY, VALUE) -> {
                    boolean checkbox = asList(VALUE.getAppearanceStates()).size() > 0;
                    if (checkbox) {
                        if (KEY.equalsIgnoreCase("PRESENZA")) {
                            if (PRESENZA.equals("SI")) {
                                setFieldsValue(form, fields, KEY, "On");
                            } else {
                                form.partialFormFlattening(KEY);
                            }
                        } else if (KEY.equalsIgnoreCase("FAD")) {
                            if (FAD.equals("SI")) {
                                setFieldsValue(form, fields, KEY, "On");
                            } else {
                                form.partialFormFlattening(KEY);
                            }
                        } else {
                            form.partialFormFlattening(KEY);
                        }
                    } else {

                        form.partialFormFlattening(KEY);

                    }
                });
                if (flatten) {

                    form.flattenFields();
                    form.flush();
                }

                BarcodeQRCode barcode = new BarcodeQRCode(username + " / MODELLO5 / "
                        + StringUtils.deleteWhitespace(al.getCognome() + "_" + al.getNome())
                        + " / " + dataconsegna.toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);
            }
            if (checkPDF(pdfOut)) {
                return pdfOut;
            }
        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;
    }

    public static File MODELLO4_BASE(
            Entity e,
            String username,
            SoggettiAttuatori sa,
            ProgettiFormativi pf,
            List<Allievi> allievi,
            List<Docenti> docenti,
            List<Lezioni_Modelli> lezioni,
            List<StaffModelli> staff,
            DateTime dataconsegna,
            boolean flatten) {

        try {

            TipoDoc p = e.getEm().find(TipoDoc.class, 6L);
            String contentb64 = p.getModello();

            String pathtemp = e.getPath("pathtemp");
            createDir(pathtemp);

            File pdfOut = new File(pathtemp + username + "_" + getOnlyStrings(sa.getRagionesociale()) + "_" + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".M4.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);
                Map<String, PdfFormField> fields = form.getAllFormFields();

                String NOMESA = sa.getRagionesociale();
                String DD = sa.getDd();
                String COGNOME = sa.getCognome_referente();
                String NOME = sa.getNome_refente();
                String CARICA = sa.getCarica();

                String CIP = pf.getCip();
                String DATAINIZIO = sdfITA.format(lezioni.get(0).getGiorno());
                String DATAFINE = sdfITA.format(lezioni.get(lezioni.size() - 1).getGiorno());

                setFieldsValue(form, fields, "NOMESA", NOMESA);
                setFieldsValue(form, fields, "DD", DD);
                setFieldsValue(form, fields, "COGNOME", COGNOME);
                setFieldsValue(form, fields, "NOME", NOME);
                setFieldsValue(form, fields, "CARICA", CARICA);
                setFieldsValue(form, fields, "CIP", CIP);
                setFieldsValue(form, fields, "DATAINIZIO", DATAINIZIO);
                setFieldsValue(form, fields, "DATAFINE", DATAFINE);
                setFieldsValue(form, fields, "data", dataconsegna.toString(patternITA));

                AtomicInteger in = new AtomicInteger(1);
                allievi.forEach(a1 -> {
                    setFieldsValue(form, fields, "NomeRow" + in.get(), a1.getNome().toUpperCase());
                    setFieldsValue(form, fields, "CognomeRow" + in.get(), a1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "CodiceFiscaleRow" + in.get(), a1.getCodicefiscale().toUpperCase());
                    setFieldsValue(form, fields, "GruppoRow" + in.get(), String.valueOf(a1.getGruppo_faseB()));
                    setFieldsValue(form, fields, "EmailRow" + in.get(), a1.getEmail().toLowerCase());
                    setFieldsValue(form, fields, "CellRow" + in.get(), a1.getTelefono());
                    in.addAndGet(1);
                });

                AtomicInteger in2 = new AtomicInteger(1);
                docenti.forEach(a1 -> {
                    setFieldsValue(form, fields, "DC_NomeRow" + in2.get(), a1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "DC_CognomeRow" + in2.get(), a1.getNome().toUpperCase());
                    setFieldsValue(form, fields, "DC_CodiceFiscaleRow" + in2.get(), a1.getCodicefiscale().toUpperCase());
                    if (a1.getDatawebinair() == null) {
                        setFieldsValue(form, fields, "DC_DataWebRow" + in2.get(), "");
                    } else {
                        setFieldsValue(form, fields, "DC_DataWebRow" + in2.get(), sdfITA.format(a1.getDatawebinair()));
                    }
                    in2.addAndGet(1);
                });

                AtomicInteger in3 = new AtomicInteger(1);
                staff.forEach(a1 -> {
                    setFieldsValue(form, fields, "sa_CognomeRow" + in3.get(), a1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "sa_NomeRow" + in3.get(), a1.getNome().toUpperCase());
                    setFieldsValue(form, fields, "sa_EMAILRow" + in3.get(), a1.getEmail().toLowerCase());
                    setFieldsValue(form, fields, "sa_CELLULARERow" + in3.get(), a1.getTelefono());
                    setFieldsValue(form, fields, "sa_RUOLO" + in3.get(), a1.getRuolo().toUpperCase());
                    in3.addAndGet(1);
                });

                lezioni.forEach(a1 -> {

                    setFieldsValue(form, fields, "G" + a1.getGruppo_faseB() + "_ggmmaa_" + a1.getLezione_calendario().getId(), sdfITA.format(a1.getGiorno()));
                    setFieldsValue(form, fields, "G" + a1.getGruppo_faseB() + "_dalle_" + a1.getLezione_calendario().getId(), sdfHHMM.format(a1.getOrario_start()));
                    setFieldsValue(form, fields, "G" + a1.getGruppo_faseB() + "_alle_" + a1.getLezione_calendario().getId(), sdfHHMM.format(a1.getOrario_end()));
                    setFieldsValue(form, fields, "G" + a1.getGruppo_faseB() + "_docente_" + a1.getLezione_calendario().getId(),
                            a1.getDocente().getCognome().toUpperCase() + " " + a1.getDocente().getNome().toUpperCase());

                    setFieldsValue(form, fields, "G" + a1.getGruppo_faseB() + "_tipolez_" + a1.getLezione_calendario().getId(),
                            a1.getTipolez().equals("F") ? "IN FAD" : "IN PRESENZA");

                });

                if (pf.getSedefisica() != null) {
                    Comuni c_s = pf.getSedefisica().getComune();
                    setFieldsValue(form, fields, "REGIONESEDE", c_s.getRegione());
                    setFieldsValue(form, fields, "COMUNESEDE", c_s.getNome());
                    setFieldsValue(form, fields, "PROVINCIASEDE", c_s.getCod_provincia());
                    setFieldsValue(form, fields, "INDIRIZZOSEDE", pf.getSede().getIndirizzo());
                }

                fields.forEach((KEY, VALUE) -> {
                    form.partialFormFlattening(KEY);
                });

                if (flatten) {
                    form.flattenFields();
                    form.flush();
                }

                BarcodeQRCode barcode = new BarcodeQRCode(username
                        + " / MODELLO4 / "
                        + StringUtils.deleteWhitespace(sa.getRagionesociale())
                        + " / " + dataconsegna.toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);

            }

            if (checkPDF(pdfOut)) {
                return pdfOut;
            }
        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;

    }

    private static File MODELLO3_BASE(
            Entity e,
            String username,
            SoggettiAttuatori sa,
            ProgettiFormativi pf, List<Allievi> allievi,
            List<Docenti> docenti,
            List<Lezioni_Modelli> lezioni,
            List<StaffModelli> staff,
            DateTime dataconsegna,
            boolean flatten) {
        try {

            TipoDoc p = e.getEm().find(TipoDoc.class, 2L);
            String contentb64 = p.getModello();

            String pathtemp = e.getPath("pathtemp");
            createDir(pathtemp);

            File pdfOut = new File(pathtemp + username + "_"
                    + getOnlyStrings(sa.getRagionesociale()) + "_" + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".M3.pdf");
            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);
                Map<String, PdfFormField> fields = form.getAllFormFields();

                String NOMESA = sa.getRagionesociale();
                String DD = sa.getDd();

                String COGNOME = sa.getCognome_referente();
                String NOME = sa.getNome_refente();
                String CARICA = sa.getCarica();
                String DATAINIZIO = sdfITA.format(pf.getStart());
                String DATAFINE = sdfITA.format(pf.getEnd());

                setFieldsValue(form, fields, "NOMESA", NOMESA);
                setFieldsValue(form, fields, "DD", DD);
                setFieldsValue(form, fields, "COGNOME", COGNOME);
                setFieldsValue(form, fields, "NOME", NOME);
                setFieldsValue(form, fields, "CARICA", CARICA);
                setFieldsValue(form, fields, "DATAINIZIO", DATAINIZIO);
                setFieldsValue(form, fields, "DATAFINE", DATAFINE);
                setFieldsValue(form, fields, "data", dataconsegna.toString(patternITA));

                AtomicInteger in = new AtomicInteger(1);
                allievi.forEach(a1 -> {
                    setFieldsValue(form, fields, "NomeRow" + in.get(), a1.getCognome().toUpperCase()); //INVERTITI NEL MODELLO
                    setFieldsValue(form, fields, "CognomeRow" + in.get(), a1.getNome().toUpperCase()); //INVERTITI NEL MODELLO
                    setFieldsValue(form, fields, "CodiceFiscaleRow" + in.get(), a1.getCodicefiscale().toUpperCase());
                    setFieldsValue(form, fields, "EmailRow" + in.get(), a1.getEmail().toLowerCase());
                    setFieldsValue(form, fields, "CellRow" + in.get(), a1.getTelefono());
                    in.addAndGet(1);
                });
                AtomicInteger in2 = new AtomicInteger(1);
                docenti.forEach(a1 -> {
                    setFieldsValue(form, fields, "DC_CognomeRow" + in2.get(), a1.getCognome().toUpperCase());//INVERTITI NEL MODELLO
                    setFieldsValue(form, fields, "DC_NomeRow" + in2.get(), a1.getNome().toUpperCase());//INVERTITI NEL MODELLO
                    setFieldsValue(form, fields, "DC_CodiceFiscaleRow" + in2.get(), a1.getCodicefiscale().toUpperCase());
                    if (a1.getDatawebinair() == null) {
                        setFieldsValue(form, fields, "DC_DataWebRow" + in2.get(), "");
                    } else {
                        setFieldsValue(form, fields, "DC_DataWebRow" + in2.get(), sdfITA.format(a1.getDatawebinair()));
                    }
                    in2.addAndGet(1);
                });

                AtomicInteger in3 = new AtomicInteger(1);
                staff.forEach(a1 -> {
                    setFieldsValue(form, fields, "sa_CognomeRow" + in3.get(), a1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "sa_NomeRow" + in3.get(), a1.getNome().toUpperCase());
                    setFieldsValue(form, fields, "sa_EMAILRow" + in3.get(), a1.getEmail().toLowerCase());
                    setFieldsValue(form, fields, "sa_CELLULARERow" + in3.get(), a1.getTelefono());
                    setFieldsValue(form, fields, "sa_RUOLO" + in3.get(), a1.getRuolo().toUpperCase());
                    in3.addAndGet(1);
                });

                lezioni.forEach(a1 -> {
                    int numlez = a1.getLezione_calendario().getLezione();
                    if (numlez >= 1 && numlez <= 24) {
                        setFieldsValue(form, fields, "ggmmaa" + numlez, sdfITA.format(a1.getGiorno()));
                        setFieldsValue(form, fields, "dallehhmm" + numlez, sdfHHMM.format(a1.getOrario_start()));
                        setFieldsValue(form, fields, "allehhmm" + numlez, sdfHHMM.format(a1.getOrario_end()));
                        setFieldsValue(form, fields, "docentelez" + numlez, a1.getDocente().getNome().toUpperCase() + " " + a1.getDocente().getCognome().toUpperCase());
                        setFieldsValue(form, fields, "modal" + numlez, (a1.getTipolez().equals("P")) ? "IN PRESENZA" : "FAD");
                    }
                });

                if (pf.getSedefisica() != null) {
                    Comuni c_s = pf.getSedefisica().getComune();
                    setFieldsValue(form, fields, "COMUNESEDE", c_s.getNome());
                    setFieldsValue(form, fields, "PROVINCIASEDE", c_s.getCod_provincia());
                    setFieldsValue(form, fields, "INDIRIZZOSEDE", pf.getSede().getIndirizzo());

                }

                fields.forEach((KEY, VALUE) -> {
                    form.partialFormFlattening(KEY);
                });

                if (flatten) {
                    form.flattenFields();
                    form.flush();
                }

                BarcodeQRCode barcode = new BarcodeQRCode(username
                        + " / MODELLO3 / "
                        + StringUtils.deleteWhitespace(sa.getRagionesociale())
                        + " / " + dataconsegna.toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);

            }

            if (checkPDF(pdfOut)) {
                return pdfOut;
            }
        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;
    }

    private static File MODELLO0_BASE(Entity e, String idmodello, Allievi al) {

        try {

            TipoDoc_Allievi p = e.getEm().find(TipoDoc_Allievi.class, Long.valueOf(idmodello));
            String contentb64 = p.getModello();
            String pathtemp = e.getPath("pathtemp");
            createDir(pathtemp);

            File pdfOut = new File(pathtemp + RandomStringUtils.randomAlphabetic(5)
                    + "_" + StringUtils.deleteWhitespace(al.getCognome() + "_" + al.getNome()) + "_" + new DateTime().toString("ddMMyyyyHHmmSSS") + ".M0.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);
                Map<String, PdfFormField> fields = form.getAllFormFields();

                setFieldsValue(form, fields, "cognome", al.getCognome().toUpperCase());
                setFieldsValue(form, fields, "nome", al.getNome().toUpperCase());
                setFieldsValue(form, fields, "datanascita", sdfITA.format(al.getDatanascita()));
                setFieldsValue(form, fields, "eta", String.valueOf(get_eta(al.getDatanascita())));
                setFieldsValue(form, fields, "codicefiscale", al.getCodicefiscale().toUpperCase());
                setFieldsValue(form, fields, "cellulare", al.getTelefono());
                setFieldsValue(form, fields, "email", al.getEmail().toLowerCase());

                setFieldsValue(form, fields, "res_regione", al.getComune_residenza().getRegione().toUpperCase());
                setFieldsValue(form, fields, "res_indirizzo", al.getIndirizzoresidenza().toUpperCase());
                setFieldsValue(form, fields, "res_comune", al.getComune_residenza().getNome().toUpperCase());
                setFieldsValue(form, fields, "res_cap", al.getCapresidenza().toUpperCase());
                setFieldsValue(form, fields, "res_prov", al.getComune_residenza().getProvincia().toUpperCase());

                if (false) {
                    setFieldsValue(form, fields, "dom_regione", al.getComune_domicilio().getRegione().toUpperCase());
                    setFieldsValue(form, fields, "dom_indirizzo", al.getIndirizzodomicilio().toUpperCase());
                    setFieldsValue(form, fields, "dom_comune", al.getComune_domicilio().getNome().toUpperCase());
                    setFieldsValue(form, fields, "dom_cap", al.getCapdomicilio().toUpperCase());
                    setFieldsValue(form, fields, "dom_prov", al.getComune_domicilio().getProvincia().toUpperCase());
                }

                setFieldsValue(form, fields, "cpi", al.getCpi().getDescrizione());
                setFieldsValue(form, fields, "datacpi", sdfITA.format(al.getDatacpi()));
                setFieldsValue(form, fields, "golpatto", al.getTos_tipofinanziamento());
                setFieldsValue(form, fields, "titolostudio", al.getTitoloStudio().getDescrizione());
                setFieldsValue(form, fields, "vulnerab", al.getTos_gruppovulnerabile().getDescrizione());
                setFieldsValue(form, fields, "condizioneprof", al.getCondizione_mercato().getDescrizione());
                setFieldsValue(form, fields, "indennita", al.getTos_dirittoindennita());
                setFieldsValue(form, fields, "dataiscrizione", sdfITA.format(al.getData_up()));

                setFieldsValue(form, fields, "datacolloquio", sdfITA.format(al.getTos_m0_datacolloquio()));
                setFieldsValue(form, fields, "siglaenm", al.getTos_m0_siglaoperatore());

                setFieldsValue(form, fields, "svolgimento" + al.getTos_m0_modalitacolloquio(), "Sì");
                setFieldsValue(form, fields, "grado" + al.getTos_m0_gradoconoscenza(), "Sì");
                setFieldsValue(form, fields, "canale" + al.getTos_m0_canaleconoscenza().getId(), "Sì");
                setFieldsValue(form, fields, "motivazione" + al.getTos_m0_motivazione().getId(), "Sì");
                setFieldsValue(form, fields, "utilita" + al.getTos_m0_utilita(), "Sì");
                setFieldsValue(form, fields, "aspettative" + al.getTos_m0_aspettative(), "Sì");
                setFieldsValue(form, fields, "maturazione" + al.getTos_m0_maturazione().getId(), "Sì");
                setFieldsValue(form, fields, "volonta" + al.getTos_m0_volonta(), "Sì");

                if (al.getTos_m0_volonta() == 1) {
                    setFieldsValue(form, fields, "soggetto", al.getSoggetto().getRagionesociale());
                    setFieldsValue(form, fields, "consapevole" + al.getTos_m0_consapevole(), "Sì");
                } else {
                    setFieldsValue(form, fields, "noperche" + al.getTos_m0_noperche().getId(), "Sì");
                    if (al.getTos_m0_noperche().getId() == 7) { //ALTRO
                        setFieldsValue(form, fields, "altro", al.getTos_m0_noperchealtro().toUpperCase());
                    }
                }

                if (al.getEmail().equalsIgnoreCase(al.getTos_mailoriginale())) {
                    setFieldsValue(form, fields, "confermamail1", "Sì");
                } else {
                    setFieldsValue(form, fields, "confermamail0", "Sì");
                    setFieldsValue(form, fields, "mailadd", al.getEmail().toLowerCase());
                }

                form.flattenFields();
                form.flush();

                BarcodeQRCode barcode = new BarcodeQRCode("MODELLO0 / "
                        + StringUtils.deleteWhitespace(al.getCognome() + "_" + al.getNome())
                        + " / " + new DateTime().toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);

            }
            if (checkPDF(pdfOut)) {
                return pdfOut;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;
    }

    private static File MODELLO1_BASE(Entity e, String idmodello, String username,
            SoggettiAttuatori sa, Allievi al, DateTime dataconsegna, boolean flatten) {

        try {

            TipoDoc_Allievi p = e.getEm().find(TipoDoc_Allievi.class, Long.valueOf(idmodello));
            String contentb64 = p.getModello();
            String pathtemp = e.getPath("pathtemp");
            createDir(pathtemp);

            File pdfOut = new File(pathtemp + username + "_" + StringUtils.deleteWhitespace(al.getCognome() + "_" + al.getNome()) + "_" + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".M1.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);
                Map<String, PdfFormField> fields = form.getAllFormFields();

                String NOMESA = sa.getRagionesociale();
                String DD = sa.getDd();

                setFieldsValue(form, fields, "NOMESA", NOMESA);
                setFieldsValue(form, fields, "DD", DD);

                setFieldsValue(form, fields, "cognome", al.getCognome().toUpperCase());
                setFieldsValue(form, fields, "nome", al.getNome().toUpperCase());
                setFieldsValue(form, fields, "datanascita", sdfITA.format(al.getDatanascita()));
                setFieldsValue(form, fields, "eta", String.valueOf(get_eta(al.getDatanascita())));
                setFieldsValue(form, fields, "codicefiscale", al.getCodicefiscale().toUpperCase());
                setFieldsValue(form, fields, "telefono", al.getTelefono());
                setFieldsValue(form, fields, "email", al.getEmail().toLowerCase());
                setFieldsValue(form, fields, "res_regione", al.getComune_residenza().getNome_provincia().toUpperCase());
                setFieldsValue(form, fields, "res_indirizzo", al.getIndirizzoresidenza().toUpperCase());
                setFieldsValue(form, fields, "res_comune", al.getComune_residenza().getNome().toUpperCase());
                setFieldsValue(form, fields, "res_cap", al.getCapresidenza());
                setFieldsValue(form, fields, "res_prov", al.getComune_residenza().getCod_provincia().toUpperCase());
//                setFieldsValue(form, fields, "dom_regione", al.getComune_domicilio().getNome_provincia().toUpperCase());
//                setFieldsValue(form, fields, "dom_indirizzo", al.getIndirizzodomicilio().toUpperCase());
//                setFieldsValue(form, fields, "dom_comune", al.getComune_domicilio().getNome().toUpperCase());
//                setFieldsValue(form, fields, "dom_cap", al.getCapdomicilio());
//                setFieldsValue(form, fields, "dom_prov", al.getComune_domicilio().getCod_provincia().toUpperCase());

                setFieldsValue(form, fields, "SESSO" + al.getSesso(), "Sì");
                setFieldsValue(form, fields, "privacy1SI", "Sì");
                setFieldsValue(form, fields, "privacy2" + al.getPrivacy2(), "Sì");
                setFieldsValue(form, fields, "privacy3" + al.getPrivacy3(), "Sì");
                setFieldsValue(form, fields, "data", dataconsegna.toString(patternITA));

                if (flatten) {
                    form.flattenFields();
                    form.flush();
                }

                BarcodeQRCode barcode = new BarcodeQRCode(username + " / MODELLO1 / "
                        + StringUtils.deleteWhitespace(al.getCognome() + "_" + al.getNome())
                        + " / " + dataconsegna.toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);

            }
            if (checkPDF(pdfOut)) {
                return pdfOut;
            }

        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;
    }

    private static File MODELLO2_BASE(Entity e, String idmodello, String username,
            SoggettiAttuatori sa, ProgettiFormativi pf, List<Allievi> allievi,
            DateTime dataconsegna, boolean flatten
    ) {

        try {

            TipoDoc p = e.getEm().find(TipoDoc.class, Long.parseLong(idmodello));
            String contentb64 = p.getModello();
            String pathtemp = e.getPath("pathtemp");
            createDir(pathtemp);

            File pdfOut = new File(pathtemp + username + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".M2.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {

                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);
                Map<String, PdfFormField> fields = form.getAllFormFields();

                String NOMESA = sa.getRagionesociale();
                String DD = sa.getDd();

                String COGNOME = sa.getCognome_referente();
                String NOME = sa.getNome_refente();
                String CARICA = sa.getCarica();
                String NUMEROISCRITTI = String.valueOf(allievi.size());
                String DATAINIZIO = sdfITA.format(pf.getStart());
                String DATAFINE = sdfITA.format(pf.getEnd());

                setFieldsValue(form, fields, "NOMESA", NOMESA);
                setFieldsValue(form, fields, "DD", DD);
                setFieldsValue(form, fields, "COGNOME", COGNOME);
                setFieldsValue(form, fields, "NOME", NOME);
                setFieldsValue(form, fields, "CARICA", CARICA);
                setFieldsValue(form, fields, "NUMEROISCRITTI", NUMEROISCRITTI);
                setFieldsValue(form, fields, "DATAINIZIO", DATAINIZIO);
                setFieldsValue(form, fields, "DATAFINE", DATAFINE);

                AtomicInteger in = new AtomicInteger(1);
                allievi.forEach(a1 -> {
                    setFieldsValue(form, fields, "CognomeRow" + in.get(), a1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "NomeRow" + in.get(), a1.getNome().toUpperCase());
                    setFieldsValue(form, fields, "Codice FiscaleRow" + in.get(), a1.getCodicefiscale().toUpperCase());
                    setFieldsValue(form, fields, "Email_" + in.get(), a1.getEmail().toLowerCase());
                    setFieldsValue(form, fields, "Cell_" + in.get(), a1.getTelefono());
                    in.addAndGet(1);
                });
                if (flatten) {
                    form.flattenFields();
                }
                BarcodeQRCode barcode = new BarcodeQRCode(username + " / MODELLO2 / "
                        + dataconsegna.toString("ddMMyyyyHHmmSSS"));
                printbarcode(barcode, pdfDoc);
            }
            if (checkPDF(pdfOut)) {
                return pdfOut;
            }
        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;
    }

    //UTILITY
    private static File convertPDFA(File pdf_ing, String nomepdf, Entity e) {
        if (pdf_ing == null) {
            return null;
        }
        try {
            File pdfOutA = new File(replace(pdf_ing.getPath(), ".pdf", "_pdfA.pdf"));
            setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
            try (PDDocument doc = Loader.loadPDF(pdf_ing)) {
                int numPageTOT = 0;
                Iterator<PDPage> it1 = doc.getPages().iterator();
                while (it1.hasNext()) {
                    numPageTOT++;
                    it1.next();
                }
                PDPage page = new PDPage();
                doc.setVersion(1.7f);
                try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
                    PDDocument docSource = Loader.loadPDF(pdf_ing);
                    PDFRenderer pdfRenderer = new PDFRenderer(docSource);
                    for (int i = 0; i < numPageTOT; i++) {
                        BufferedImage imagePage = pdfRenderer.renderImageWithDPI(i, 200);
                        PDImageXObject pdfXOImage = createFromImage(doc, imagePage);
                        contents.drawImage(pdfXOImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
                    }
                }
                XMPMetadata xmp = createXMPMetadata();
                PDDocumentCatalog catalogue = doc.getDocumentCatalog();
                Calendar cal = getInstance();
                DublinCoreSchema dc = xmp.createAndAddDublinCoreSchema();
                dc.addCreator("YISU");
                dc.addDate(cal);
                PDFAIdentificationSchema id = xmp.createAndAddPDFAIdentificationSchema();
                id.setPart(3);  //value => 2|3
                id.setConformance("A"); // value => A|B|U
                XmpSerializer serializer = new XmpSerializer();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                serializer.serialize(xmp, baos, true);
                PDMetadata metadata = new PDMetadata(doc);
                metadata.importXMPMetadata(baos.toByteArray());
                catalogue.setMetadata(metadata);
                //            InputStream colorProfile = new FileInputStream("C:\\mnt\\mcn\\gestione_neet\\sRGB.icc");
                InputStream colorProfile = new Pdf_new().getClass().getResourceAsStream("/sRGB.icc");
                //            InputStream colorProfile = new ByteArrayInputStream(decodeBase64(e.getPath("pdf.icc")));
                PDOutputIntent intent = new PDOutputIntent(doc, colorProfile);
                intent.setInfo("sRGB IEC61966-2.1");
                intent.setOutputCondition("sRGB IEC61966-2.1");
                intent.setOutputConditionIdentifier("sRGB IEC61966-2.1");
                intent.setRegistryName("http://www.color.org");
                catalogue.addOutputIntent(intent);
                catalogue.setLanguage("it-IT");
                PDViewerPreferences pdViewer = new PDViewerPreferences(page.getCOSObject());
                pdViewer.setDisplayDocTitle(true);
                catalogue.setViewerPreferences(pdViewer);
                PDMarkInfo mark = new PDMarkInfo(); // new PDMarkInfo(page.getCOSObject());
                PDStructureTreeRoot treeRoot = new PDStructureTreeRoot();
                catalogue.setMarkInfo(mark);
                catalogue.setStructureTreeRoot(treeRoot);
                catalogue.getMarkInfo().setMarked(true);
                PDDocumentInformation info = doc.getDocumentInformation();
                info.setCreationDate(cal);
                info.setModificationDate(cal);
                info.setAuthor("YISU");
                info.setProducer("YISU");
                info.setCreator("YISU");
                info.setTitle(nomepdf);
                info.setSubject("PDF/A");
                doc.save(pdfOutA);
            }

            return pdfOutA;
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
        return null;
    }

    public static void printbarcode(BarcodeQRCode barcode, PdfDocument pdfDoc) {
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
            }
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
    }

    public static void setFieldsValue(
            PdfAcroForm form,
            Map<String, PdfFormField> fields_list,
            String field_name,
            String field_value) {
        try {
            if (fields_list.get(field_name) != null) {
                if (field_value == null) {
                    field_value = "";
                }
                fields_list.get(field_name).setValue(field_value);
                form.partialFormFlattening(field_name);
            }
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
    }

    public static SignedDoc extractSignatureInformation_P7M(byte[] p7m_bytes) {
        SignedDoc doc = new SignedDoc();
        CMSSignedData cms;
        try {
            cms = new CMSSignedData(p7m_bytes);
        } catch (Exception e) {
            doc.setErrore("ERRORE NEL FILE - " + e.getMessage());
            return doc;
        }
        if (cms.getSignedContent() == null) {
            doc.setErrore("ERRORE NEL FILE - CONTENUTO ERRATO");
            return doc;
        }
        try {
            Store<X509CertificateHolder> store = cms.getCertificates();
            Collection<X509CertificateHolder> allCerts = store.getMatches(null);
            if (!allCerts.isEmpty()) {
                X509CertificateHolder x509h = allCerts.iterator().next();
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                try (InputStream in = new ByteArrayInputStream(x509h.getEncoded())) {
                    X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
                    Principal principal = cert.getSubjectDN();
                    try {
                        cert.checkValidity();
                        doc.setValido(true);
                    } catch (Exception e) {
                        doc.setValido(false);
                        doc.setErrore(e.getMessage());
                    }
                    if (doc.isValido()) {
//                        String cf = substringBefore(substringAfter(principal.getName(), "SERIALNUMBER="), ", GIVENNAME");
                        doc.setCodicefiscale(principal.getName().toUpperCase());
                        doc.setContenuto((byte[]) cms.getSignedContent().getContent());
                    } else {
                        if (new DateTime(cert.getNotAfter().getTime()).isBeforeNow()) {
                            doc.setErrore("CERTIFICATO SCADUTO IN DATA " + new DateTime(cert.getNotAfter().getTime()).toString("dd/MM/yyyy HH:mm:ss"));
                        }
                    }
                }
            } else {
                doc.setErrore("ERRORE NEL FILE - CERTIFICATI NON TROVATI");
                return doc;
            }
        } catch (Exception ex) {
            doc.setValido(false);
            doc.setErrore("ERRORE NEL FILE - " + ex.getMessage());
        }
        return doc;
    }

    public static SignedDoc extractSignatureInformation_PDF(byte[] pdf_bytes) {
        SignedDoc doc = new SignedDoc();
        try {
            BouncyCastleProvider provider = new BouncyCastleProvider();
            addProvider(provider);
            try (InputStream is = new ByteArrayInputStream(pdf_bytes)) {
                PdfReader read = new PdfReader(is);
                PdfDocument pdfDoc = new PdfDocument(read);
                AtomicInteger error = new AtomicInteger(0);
                SignatureUtil signatureUtil = new SignatureUtil(pdfDoc);
                List<String> li = signatureUtil.getSignatureNames();
                if (li.isEmpty()) {
                    doc.setErrore("ERRORE NEL FILE - NESSUNA FIRMA");
                } else {
                    li.forEach(name -> {
                        if (error.get() == 0) {
                            PdfPKCS7 signature1 = signatureUtil.readSignatureData(name);
                            if (signature1 != null) {
                                X509Certificate cert = signature1.getSigningCertificate();
                                try {
                                    boolean es = signature1.verifySignatureIntegrityAndAuthenticity();
                                    if (es) {
                                        Principal principal = cert.getSubjectDN();
                                        doc.setCodicefiscale(principal.getName().toUpperCase());
                                        doc.setContenuto(pdf_bytes);
                                        doc.setValido(true);
                                    } else {
                                        doc.setErrore("ERRORE NEL FILE - CERTIFICATO NON VALIDO");
                                        error.addAndGet(1);
                                        doc.setValido(false);
                                    }
                                } catch (Exception e) {
                                    doc.setValido(false);
                                    doc.setErrore("ERRORE NEL FILE - " + e.getMessage());
                                    error.addAndGet(1);
                                }
                            } else {
                                doc.setValido(false);
                                doc.setErrore("ERRORE NEL FILE - FIRMA NON VALDA");
                                error.addAndGet(1);
                            }
                        }
                    });
                }
                pdfDoc.close();
                read.close();
            }
            return doc;
        } catch (Exception e) {
            doc.setErrore("ERRORE NEL FILE - " + e.getMessage());
            return doc;
        }
    }

    private static String verificaPDFA(byte[] content) {
        return "OK";
//        String out = "KO";
//        try {
//            setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
//            if (content != null) {
//                try (InputStream is1 = new ByteArrayInputStream(content); PDDocument doc = load(is1)) {
//                    PDDocumentInformation info = doc.getDocumentInformation();
//                    if (info.getSubject() != null) {
//                        if (info.getSubject().equals("PDF/A")) {
//                            out = "OK";
//                        } else {
//                            out = "ERRORE NEL FILE - NO PDF/A";
//                        }
//                    } else {
//                        out = "ERRORE NEL FILE - NO PDF/A";
//                    }
//                }
//            }
//        } catch (Exception e) {
//            out = "ERRORE NEL FILE - " + e.getMessage();
//        }
//
//        return out;

    }

    private static String estraiResult(PDDocument doc, String qrcrop, int pag) {
        try {
            PDPage page = doc.getPage(pag);
            if (qrcrop == null) {
                page.setCropBox(new PDRectangle(20, 0, 60, 60));
            } else {
                String[] cropdim = qrcrop.split(";");
                page.setCropBox(new PDRectangle(
                        Integer.parseInt(cropdim[0]),
                        Integer.parseInt(cropdim[1]),
                        Integer.parseInt(cropdim[2]),
                        Integer.parseInt(cropdim[3]))
                );
            }
            PDFRenderer pr = new PDFRenderer(doc);
            BufferedImage bi = pr.renderImageWithDPI(pag, 400);
            int[] pixels = bi.getRGB(0, 0,
                    bi.getWidth(), bi.getHeight(),
                    null, 0, bi.getWidth());
            RGBLuminanceSource source = new RGBLuminanceSource(bi.getWidth(),
                    bi.getHeight(),
                    pixels);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Map<DecodeHintType, Object> decodeHints = new HashMap<>();
            decodeHints.put(
                    DecodeHintType.TRY_HARDER,
                    Boolean.TRUE
            );

//
//            try {
//                ImageIO.write(bi, "jpg", new File("E:\\mnt\\qr" + pag + ".png"));
//            } catch (Exception e) {
//            }
            Result result = new QRCodeReader().decode(bitmap, decodeHints);
            doc.close();
            String qr = result.getText().toUpperCase();
            return qr;
        } catch (Exception ex) {
            if (ex.getMessage() == null) {
                return estraiResult(doc, qrcrop, pag + 1);
            }
            insertTR("E", "SERVICE", estraiEccezione(ex));
            return null;
        }
    }

    private static String verificaQR(String nomedoc, String username, byte[] content, String qrcrop) {
        String pdfa = "OK";
        if (nomedoc.equalsIgnoreCase("modello2")
                || nomedoc.equalsIgnoreCase("modello3")
                || nomedoc.equalsIgnoreCase("modello4")
                || nomedoc.equalsIgnoreCase("modello5")
                || nomedoc.equalsIgnoreCase("modello6")
                || nomedoc.equalsIgnoreCase("modello7")
                || nomedoc.equalsIgnoreCase("REGISTRO COMPLESSIVO")
                || nomedoc.equalsIgnoreCase("ASSENZA POSIZIONE")
                || nomedoc.equalsIgnoreCase("allegatob1")) {
            pdfa = verificaPDFA(content);
        }
        if (!pdfa.equals("OK")) {
            return pdfa;
        } else {

            if (nomedoc.equalsIgnoreCase("ASSENZA POSIZIONE")
                    || nomedoc.equalsIgnoreCase("REGISTRO GIORNALIERO")) {
                return "OK";
            }
            String out = "KO";
            if (content != null) {
                try {
                    PDDocument doc = Loader.loadPDF(content);
                    String qr = estraiResult(doc, qrcrop, 0);
                    if (qr == null) {
                        return "ERRORE - DOCUMENTO NON CORRISPONDE A QUANTO RICHIESTO";
                    } else {
                        if (qr.contains(username.toUpperCase())) {
                            if (qr.contains(nomedoc.toUpperCase())) {
                                out = "OK";
                            } else {
                                out = "ERRORE - DOCUMENTO NON CORRISPONDE A QUANTO RICHIESTO";
                            }
                        } else {
                            out = "ERRORE - USERNAME NON CORRISPONDE";
                        }
                    }
                } catch (Exception ex) {
                    if (ex.getMessage() == null) {
                        out = "ERRORE - QR CODE ILLEGGIBILE";
                    } else {
                        insertTR("E", "SERVICE", estraiEccezione(ex));
                        out = "ERRORE NEL FILE - " + ex.getMessage();
                    }
                }
            }
            return out;
        }

    }

    public static String formatStatoNascita(String stato_nascita, List<Nazioni_rc> nascitaconCF) {
        try {
            if (stato_nascita.equals("100")) {
                return "ITALIA";
            } else {
                Nazioni_rc nn = nascitaconCF.stream().filter(
                        n1
                        -> n1.getCodicefiscale().equalsIgnoreCase(stato_nascita)
                ).findAny().orElse(null);
                if (nn != null) {
                    return nn.getNome().toUpperCase();
                }
            }
        } catch (Exception e) {
        }
        return stato_nascita;
    }

    public static String checkFirmaQRpdfA(
            String nomedoc,
            String username,
            File file,
            String cfuser,
            String qrcrop
    ) {

        if (true) {
//        if (Utility.test || Utility.demoversion) {
            return "OK";
        }

        if (nomedoc.equalsIgnoreCase("modello1")) {
            //SOLO QR
            try {
                return verificaQR(nomedoc, username, readFileToByteArray(file), qrcrop);
            } catch (Exception e) {
                return "ERRORE NELL'UPLOAD - " + nomedoc + " - " + e.getMessage();
            }
        } else if (nomedoc.equalsIgnoreCase("modello2")
                || nomedoc.equalsIgnoreCase("modello3")
                || nomedoc.equalsIgnoreCase("modello4")
                || nomedoc.equalsIgnoreCase("modello5")
                || nomedoc.equalsIgnoreCase("modello6")
                || nomedoc.equalsIgnoreCase("modello7")
                || nomedoc.equalsIgnoreCase("REGISTRO COMPLESSIVO")
                || nomedoc.equalsIgnoreCase("REGISTRO GIORNALIERO")
                || nomedoc.equalsIgnoreCase("ASSENZA POSIZIONE")
                || nomedoc.equalsIgnoreCase("allegatob1")) { //  QR e FIRMA
            if (getExtension(file.getName()).endsWith("p7m")) {
                try {
                    SignedDoc dc = extractSignatureInformation_P7M(readFileToByteArray(file));
                    if (dc.isValido()) {
                        if (!dc.getCodicefiscale().toUpperCase().contains(cfuser.toUpperCase())) {
                            return "ERRORE NELL'UPLOAD - " + nomedoc + " - CF NON CONFORME";
                        } else {
                            byte[] content = dc.getContenuto();
                            if (content == null) {
                                return "ERRORE NELL'UPLOAD - " + nomedoc + " - CF NON CONFORME";
                            } else {
                                String esitoqr = verificaQR(nomedoc, username, content, qrcrop);
                                if (!esitoqr.equals("OK")) {
                                    return "ERRORE NELL'UPLOAD - " + nomedoc + " - " + esitoqr;
                                }
                            }
                        }
                    } else {
                        return "ERRORE NELL'UPLOAD - " + nomedoc + " - " + dc.getErrore();
                    }
                } catch (Exception e) {
                    return "ERRORE NELL'UPLOAD - " + nomedoc + " - " + e.getMessage();
                }
            } else {
                try {
                    SignedDoc dc = extractSignatureInformation_PDF(readFileToByteArray(file));
                    if (dc.isValido()) {
                        if (!dc.getCodicefiscale().toUpperCase().contains(cfuser.toUpperCase())) {
                            return "ERRORE NELL'UPLOAD - " + nomedoc + " - CF NON CONFORME";
                        } else {
                            byte[] content = dc.getContenuto();
                            if (content == null) {
                                return "ERRORE NELL'UPLOAD - " + nomedoc + " - CF NON CONFORME";
                            } else {
                                String esitoqr = verificaQR(nomedoc, username, content, qrcrop);
                                if (!esitoqr.equals("OK")) {
                                    return "ERRORE NELL'UPLOAD - " + nomedoc + " - " + esitoqr;
                                }
                            }
                        }
                    } else {
                        return "ERRORE NELL'UPLOAD - " + nomedoc + " - " + dc.getErrore();
                    }
                } catch (Exception e) {
                    return "ERRORE NELL'UPLOAD - " + nomedoc + " - " + e.getMessage();
                }
            }
        }
        return "OK";
    }

}
