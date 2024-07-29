/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.util;

import com.google.common.primitives.Ints;
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
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.SignatureUtil;
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
import rc.so.entity.OreId;
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
import java.util.Calendar;
import static java.util.Calendar.getInstance;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import org.json.JSONObject;
import rc.so.domain.SediFormazione;
import static rc.so.util.Utility.OM;
import static rc.so.util.Utility.checkPDF;
import static rc.so.util.Utility.createDir;
import static rc.so.util.Utility.estraiEccezione;
import static rc.so.util.Utility.parseDouble;
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
            String idgruppo,
            DateTime dataconsegna) {
        File out1 = REGISTROCARTACEO_BASE(null, e, username, lm, idgruppo, dataconsegna);
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
            MascheraM5 m5,
            DateTime dataconsegna,
            boolean flatten) {
        File out1 = MODELLO5_BASE(e, contentb64, username, sa, al, m5, dataconsegna, flatten);
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

    public static File MODELLO7_COMPLETO(
            Entity e,
            String username,
            Allievi al,
            String modalita,
            DateTime dataconsegna,
            boolean flatten) {
        File out1 = MODELLO7_OK(e, username, al, modalita, dataconsegna, flatten);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "MODELLO7", e);
            if (out2 != null) {
                return out2;
            }
        }
        return null;
    }
    
        public static File ALLEGATOA1(
            String pathdest,
            Entity e,
            String username,
            SediFormazione sf,
            DateTime dataconsegna) {
        File out1 = ALLEGATOA1_BASE(pathdest, e, username, sf, dataconsegna);
        if (out1 != null) {
            File out2 = convertPDFA(out1, "ALLEGATOA1", e);
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
    
    
    
    
    private static File ALLEGATOA1_BASE(
            String pathdest,
            Entity e,
            String username,
            SediFormazione sf,
            DateTime dataconsegna) {

        try {

            TipoDoc p = e.getEm().find(TipoDoc.class, 38L);
            String contentb64 = p.getModello();

            File pdfOut;
            if (pathdest == null) {
                String pathtemp = e.getPath("pathtemp");
                createDir(pathtemp);
                pdfOut = new File(pathtemp
                        + username + "_"
                        + StringUtils.deleteWhitespace(sf.getSoggetto().getRagionesociale()) + "_"
                        + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".A1.pdf");
            } else {
                pdfOut = new File(pathdest);
            }

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);
                Map<String, PdfFormField> fields = form.getAllFormFields();

                //PAG.1
                setFieldsValue(form, fields, "NOMESA", sf.getSoggetto().getRagionesociale().toUpperCase());
                setFieldsValue(form, fields, "DD", sf.getSoggetto().getDd());
                setFieldsValue(form, fields, "COGNOME", sf.getSoggetto().getCognome().toUpperCase());
                setFieldsValue(form, fields, "NOME", sf.getSoggetto().getNome().toUpperCase());
                setFieldsValue(form, fields, "CARICA", sf.getSoggetto().getCarica().toUpperCase());

                setFieldsValue(form, fields, "regioneaula1", sf.getComune().getRegione().toUpperCase());
                setFieldsValue(form, fields, "provincia1", sf.getComune().getNome_provincia().toUpperCase());
                setFieldsValue(form, fields, "citta1", sf.getComune().getNome().toUpperCase());
                setFieldsValue(form, fields, "indirizzo1", sf.getIndirizzo().toUpperCase());

                setFieldsValue(form, fields, "responsabile1", sf.getReferente().toUpperCase());
                setFieldsValue(form, fields, "mailresponsabile1", sf.getEmail().toLowerCase());
                setFieldsValue(form, fields, "telresponsabile1", sf.getTelefono());

                if (sf.getAltridati() != null) {
                    try {
                        JSONObject ad = new JSONObject(sf.getAltridati());
                        setFieldsValue(form, fields, "titolo1", ad.getString("titolo").toUpperCase());
                        setFieldsValue(form, fields, "estremi1", ad.getString("mq").toUpperCase());
                        setFieldsValue(form, fields, "accreditamento1", ad.getString("accreditamento").toUpperCase());
                        setFieldsValue(form, fields, "amministrativo1", ad.getString("amministrativo").toUpperCase());
                        setFieldsValue(form, fields, "mailamministrativo1", ad.getString("mailamministrativo").toLowerCase());
                        setFieldsValue(form, fields, "telamministrativo1", ad.getString("telamministrativo"));
                    } catch (Exception ex3) {
                        e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex3));
                    }
                }

                fields.forEach((KEY, VALUE) -> {
                    form.partialFormFlattening(KEY);
                });

                form.flattenFields();
                form.flush();

                BarcodeQRCode barcode = new BarcodeQRCode(username + " / ALLEGATOA1 / "
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

    //MODELLI
    private static File REGISTROCARTACEO_BASE(
            String pathdest,
            Entity e,
            String username,
            Lezioni_Modelli lm,
            String idgruppo,
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
                setFieldsValue(form, fields, "FASE", lm.getLezione_calendario().getUnitadidattica().getFase().toUpperCase());
                setFieldsValue(form, fields, "UDLEZ", lm.getLezione_calendario().getUnitadidattica().getDescrizione());
                setFieldsValue(form, fields, "MODLEZ", lm.getLezione_calendario().getUnitadidattica().getCodice());
                setFieldsValue(form, fields, "ORELEZ", roundDoubleAndFormat(lm.getLezione_calendario().getUnitadidattica().getOre()));

                if (lm.getLezione_calendario().getUnitadidattica().getFase().endsWith("A") && idgruppo.equals("")) {
                    setFieldsValue(form, fields, "DESCRFASE", lm.getLezione_calendario().getUnitadidattica().getFase().toUpperCase()
                            + ", UD N° " + lm.getLezione_calendario().getUnitadidattica().getDescrizione()
                            + ", MODULO " + lm.getLezione_calendario().getUnitadidattica().getCodice());
                } else {
                    setFieldsValue(form, fields, "DESCRFASE", lm.getLezione_calendario().getUnitadidattica().getFase().toUpperCase()
                            + ", UD N° " + lm.getLezione_calendario().getUnitadidattica().getDescrizione()
                            + ", MODULO " + lm.getLezione_calendario().getUnitadidattica().getCodice()
                            + ", GRUPPO " + idgruppo);
                }

                setFieldsValue(form, fields, "DOC_COGN", lm.getDocente().getCognome().toUpperCase());
                setFieldsValue(form, fields, "DOC_NOM", lm.getDocente().getNome().toUpperCase());
                setFieldsValue(form, fields, "DOC_CF", lm.getDocente().getCodicefiscale().toUpperCase());
                setFieldsValue(form, fields, "DOC_RUOLO", "DOCENTE");

                List<Allievi> allievi = lm.getModello().getProgetto().getAllievi().stream().filter(al -> al.getStatopartecipazione().getId()
                        .equalsIgnoreCase("13") || al.getStatopartecipazione().getId()
                        .equalsIgnoreCase("14") || al.getStatopartecipazione().getId()
                        .equalsIgnoreCase("15")
                ).collect(Collectors.toList());

                if (!idgruppo.equals("")) {
                    allievi = allievi.stream().filter(a1 -> a1.getGruppo_faseB() == Utility.parseIntR(idgruppo)).collect(Collectors.toList());
                }

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

            Database db = new Database(true);
            String[] datiSA = db.estrai_dati_permodello6(sa.getId());
            db.closeDB();

            List<Allievi> allievi_OK = e.getAllieviProgettiFormativi(pf);
            List<Docenti> docenti_tab = Utility.docenti_A(e, pf);
            int soglia = Utility.parseIntR(e.getPath("soglia.allegato7"));

            File pdfOut = new File(startpath + username + "_"
                    + getOnlyStrings(sa.getRagionesociale()) + "_"
                    + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".EV.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {

                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);

                Map<String, PdfFormField> fields = form.getAllFormFields();

                //PAG 1
                setFieldsValue(form, fields, "NOMESA", sa.getRagionesociale().toUpperCase());
                setFieldsValue(form, fields, "CIP", pf.getCip().toUpperCase());
                setFieldsValue(form, fields, "CRA", datiSA[0].toUpperCase());

                setFieldsValue(form, fields, "CF", sa.getCodicefiscale().toUpperCase());
                setFieldsValue(form, fields, "PIVA", sa.getPiva().toUpperCase());
                setFieldsValue(form, fields, "TIPO", datiSA[1].toUpperCase());
                setFieldsValue(form, fields, "DATAINIZIO", sdfITA.format(pf.getStart()));
                setFieldsValue(form, fields, "DATAFINE", sdfITA.format(pf.getEnd()));

                setFieldsValue(form, fields, "TOTALE", roundDoubleAndFormat(pf.getChecklist_finale().getTot_tot()) + " €");
                setFieldsValue(form, fields, "TOTALE_GOL", roundDoubleAndFormat(pf.getChecklist_finale().getTot_gol()) + " €");
                setFieldsValue(form, fields, "TOTALE_PATTO", roundDoubleAndFormat(pf.getChecklist_finale().getTot_pat()) + " €");

                setFieldsValue(form, fields, "DATA", dataconsegna.toString(patternITA));

                List<OreId> list_orecontrollatefaseA = Arrays.asList(OM.readValue(pf.getChecklist_finale().getTab_neet_fa(), OreId[].class));
                List<OreId> list_orecontrollatefaseB = Arrays.asList(OM.readValue(pf.getChecklist_finale().getTab_neet_fb(), OreId[].class));
                String ored = pf.getChecklist_finale().getTab_docenza_fa() == null ? "[]" : pf.getChecklist_finale().getTab_docenza_fa();
                List<OreId> list_orecontrollateDocenti = Arrays.asList(OM.readValue(ored, OreId[].class));
                
                 
                AtomicInteger indice1 = new AtomicInteger(1);
                allievi_OK.forEach(al1 -> {
                    setFieldsValue(form, fields, "Cognome" + indice1.get(), al1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "Nome" + indice1.get(), al1.getNome().toUpperCase());
                    setFieldsValue(form, fields, "CF" + indice1.get(), al1.getCodicefiscale().toUpperCase());

                    OreId orea = list_orecontrollatefaseA.stream().filter(al2 -> al2.getId().equals(String.valueOf(al1.getId()))).findAny().orElse(null);
                    if (orea != null) {
                        setFieldsValue(form, fields, "TOTALEA" + indice1.get(),
                                Utility.roundFloatAndFormat(Float.parseFloat(orea.getOre()), false, true));
                    }
                    OreId oreb = list_orecontrollatefaseB.stream().filter(al2 -> al2.getId().equals(String.valueOf(al1.getId()))).findAny().orElse(null);
                    if (oreb != null) {
                        setFieldsValue(form, fields, "TOTALEB" + indice1.get(), roundFloatAndFormat(Float.parseFloat(oreb.getOre()), false, true));
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
                            d1.getCodicefiscale().toUpperCase());
                    
                    OreId oredocente = list_orecontrollateDocenti.stream().filter(al2 -> al2.getId().equals(String.valueOf(d1.getId()))).findAny().orElse(null);
                    if (oredocente == null) {
                        setFieldsValue(form, fields, "TOTALED_B" + indice2.get(),
                            roundDoubleAndFormat(d1.getOrec_faseA()));
                    } else {
                        setFieldsValue(form, fields, "TOTALED_B" + indice2.get(),
                                roundDoubleAndFormat(parseDouble(oredocente.getOre())));
                    }
                    
                    indice2.addAndGet(1);
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

            List<Allievi> allievi_totali = e.getAllieviProgettiFormativiAll(pf);
            List<Allievi> allievi_OK = e.getAllieviProgettiFormativi(pf);
            List<Docenti> docenti_tab = Utility.docenti_A(e, pf);
            int soglia = Utility.parseIntR(e.getPath("soglia.allegato7"));
            String coeff_fa = e.getPath("coeff.allievo.fasea");
            String coeff_fb = e.getPath("coeff.allievo.faseb");
            String coeff_doc = e.getPath("coeff.docente.a");

            int allieviOK = allievi_OK.size();
            int allieviOKATT = Utility.allieviOKattestato(allievi_OK, soglia);

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
                setFieldsValue(form, fields, "ATTESTATI", String.valueOf(allieviOKATT));

                List<OreId> list_orecontrollatefaseA = Arrays.asList(OM.readValue(pf.getChecklist_finale().getTab_neet_fa(), OreId[].class));
                List<OreId> list_orecontrollatefaseB = Arrays.asList(OM.readValue(pf.getChecklist_finale().getTab_neet_fb(), OreId[].class));
                String ored = pf.getChecklist_finale().getTab_docenza_fa() == null ? "[]" : pf.getChecklist_finale().getTab_docenza_fa();
                List<OreId> list_orecontrollateDocenti = Arrays.asList(OM.readValue(ored, OreId[].class));
                
                AtomicInteger indice1 = new AtomicInteger(1);

                allievi_totali.forEach(al1 -> {

                    setFieldsValue(form, fields, "COGNOMERow" + indice1.get(), al1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "NOMERow" + indice1.get(), al1.getNome().toUpperCase());
                    setFieldsValue(form, fields, "TIPOARow" + indice1.get(), al1.getTos_tipofinanziamento());
                    setFieldsValue(form, fields, "ORENEETARow" + indice1.get(), roundDoubleAndFormat(al1.getOrec_fasea(), true));
                    setFieldsValue(form, fields, "ORE PRESENZE ALLIEVI  FASE BRow" + indice1.get(), roundDoubleAndFormat(al1.getOrec_faseb(), true));
                    OreId orea = list_orecontrollatefaseA.stream().filter(al2 -> al2.getId().equals(String.valueOf(al1.getId()))).findAny().orElse(null);
                    if (orea != null) {
                        setFieldsValue(form, fields, "C_ORENEETARow" + indice1.get(),
                                roundDoubleAndFormat(parseDouble(orea.getOre()), true));
                        setFieldsValue(form, fields, "IMPORTONEETARow" + indice1.get(),
                                roundDoubleAndFormat(parseDouble(coeff_fa), true));
                        setFieldsValue(form, fields, "TOTALE FASE ARow" + indice1.get(),
                                roundDoubleAndFormat(parseDouble(orea.getTotale()), true));
                    }
                    OreId oreb = list_orecontrollatefaseB.stream().filter(al2 -> al2.getId().equals(String.valueOf(al1.getId()))).findAny().orElse(null);
                    if (oreb != null) {
                        setFieldsValue(form, fields, "CONTROLL O ORE PRESENZE ALLIEVI  FASE BRow" + indice1.get(),
                                roundDoubleAndFormat(parseDouble(oreb.getOre()), true));
                        setFieldsValue(form, fields, "IMPORTO ORARIO RICONOSCIUTORow" + (indice1.get() + 1) + "_2",
                                roundDoubleAndFormat(parseDouble(coeff_fb), true));
                        setFieldsValue(form, fields, "TOTALE FASE BRow" + indice1.get(),
                                roundDoubleAndFormat(parseDouble(oreb.getTotale()), true));
                    }
                    indice1.addAndGet(1);
                });

                setFieldsValue(form, fields, "ALLIEVIPATTO", String.valueOf(pf.getChecklist_finale().getAllievi_pat()));
                setFieldsValue(form, fields, "ALLIEVIGOL", String.valueOf(pf.getChecklist_finale().getAllievi_gol()));
                setFieldsValue(form, fields, "TOTALE_FASEA_ALLIEVI", "€ " + roundDoubleAndFormat(pf.getChecklist_finale().getFa_total(), true));
                setFieldsValue(form, fields, "TOTALE_FASEB_ALLIEVI", "€ " + roundDoubleAndFormat(pf.getChecklist_finale().getFb_total(), true));
                setFieldsValue(form, fields, "TOT_ALLIEVI_GOL", "€ " + roundDoubleAndFormat((pf.getChecklist_finale().getFa_total_G() + pf.getChecklist_finale().getFb_total_G()), true));
                setFieldsValue(form, fields, "TOT_ALLIEVI_PDL", "€ " + roundDoubleAndFormat((pf.getChecklist_finale().getFa_total_P() + pf.getChecklist_finale().getFb_total_P()), true));
                setFieldsValue(form, fields, "TOT_DOCENTI_GOL", "€ " + roundDoubleAndFormat(pf.getChecklist_finale().getDc_total_G(), true));
                setFieldsValue(form, fields, "TOT_DOCENTI_PDL", "€ " + roundDoubleAndFormat(pf.getChecklist_finale().getDc_total_P(), true));
                setFieldsValue(form, fields, "TOT_DOCENTI_FASEA", "€ " + roundDoubleAndFormat(pf.getChecklist_finale().getDc_total(), true));
                setFieldsValue(form, fields, "TOTALE_PATTO", "€ " + roundDoubleAndFormat(pf.getChecklist_finale().getTot_pat(), true));
                setFieldsValue(form, fields, "TOTALE_GOL", "€ " + roundDoubleAndFormat(pf.getChecklist_finale().getTot_gol(), true));
                setFieldsValue(form, fields, "TOTALE", "€ " + roundDoubleAndFormat(pf.getChecklist_finale().getTot_tot(), true));
                AtomicInteger indice2 = new AtomicInteger(1);

                docenti_tab.forEach(d1 -> {
                    setFieldsValue(form, fields, "COGNOMERow" + indice2.get() + "_2", d1.getCognome().toUpperCase());
                    setFieldsValue(form, fields, "NOMERow" + indice2.get() + "_2", d1.getNome().toUpperCase());
                    
                    OreId oredocente = list_orecontrollateDocenti.stream().filter(al2 -> al2.getId().equals(String.valueOf(d1.getId()))).findAny().orElse(null);
                    double tota = d1.getOrec_faseA() * parseDouble(coeff_fb);
                    
                    if (oredocente == null) {
                        setFieldsValue(form, fields, "CONTROLLO ORE PRESENZE DOCENTE  FASE ARow" + indice2.get(),
                                roundDoubleAndFormat(d1.getOrec_faseA(), true));
                    } else {
                        setFieldsValue(form, fields, "CONTROLLO ORE PRESENZE DOCENTE  FASE ARow" + indice2.get(),
                                roundDoubleAndFormat(parseDouble(oredocente.getOre()), true));
                        tota = parseDouble(oredocente.getOre()) * parseDouble(coeff_fb);
                    }
                    
                    
                    setFieldsValue(form, fields, "IMPORTO ORARIO RICONOSCIUTORow" + (indice2.get() + 1) + "_3",
                            roundDoubleAndFormat(parseDouble(coeff_doc), true));
                    setFieldsValue(form, fields, "IMPORTO ORARIO RICONOSCIUTORow" + (indice1.get() + 1) + "_2",
                            roundDoubleAndFormat(parseDouble(coeff_fb), true));
                    
                    setFieldsValue(form, fields, "TOTALE FASE ARow" + indice2.get() + "_2",
                            roundDoubleAndFormat(tota, true));
                    indice2.addAndGet(1);
                });
                setFieldsValue(form, fields, "NOTE CONTROLLORERow1", pf.getChecklist_finale().getNota_controllore());
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
            ex.printStackTrace();
//            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
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

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {
                PdfAcroForm form = getAcroForm(pdfDoc, true);
                form.setGenerateAppearance(true);

                Map<String, PdfFormField> fields = form.getAllFormFields();

                setFieldsValue(form, fields, "COGNOME", sa.getCognome().toUpperCase());
                setFieldsValue(form, fields, "NOME", sa.getNome().toUpperCase());
                setFieldsValue(form, fields, "NOMESA", sa.getRagionesociale().toUpperCase());
                setFieldsValue(form, fields, "INDIRIZZOSEDE", sa.getIndirizzo().toUpperCase() + " - " + sa.getComune().getNome());
                setFieldsValue(form, fields, "PIVASA", sa.getPiva().toUpperCase());
                setFieldsValue(form, fields, "LUOGO", sa.getComune().getNome());
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

            }

            if (checkPDF(pdfOut)) {
                return pdfOut;
            }

        } catch (Exception ex) {
            e.insertTracking("ERROR SYSTEM ", estraiEccezione(ex));
        }
        return null;
    }

    private static File MODELLO7_OK(
            Entity e,
            String username,
            Allievi al,
            String modalita,
            DateTime dataconsegna,
            boolean flatten) {

        try {

            TipoDoc_Allievi p = e.getEm().find(TipoDoc_Allievi.class, 22L);
            String contentb64 = p.getModello();

            String pathtemp = e.getPath("pathtemp");
            createDir(pathtemp);
            File pdfOut = new File(pathtemp + username + "_" + StringUtils.deleteWhitespace(al.getCognome() + "_" + al.getNome()) + "_" + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".M7.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut)) {
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
                setFieldsValue(form, fields, "CIP", al.getProgetto().getCip());
                setFieldsValue(form, fields, "DATAINIZIO", sdfITA.format(al.getProgetto().getStart()));
                setFieldsValue(form, fields, "DATAFINE", sdfITA.format(al.getProgetto().getEnd()));
                setFieldsValue(form, fields, "NOMESA", al.getSoggetto().getRagionesociale().toUpperCase());
                setFieldsValue(form, fields, "OREFREQ", roundDoubleAndFormat(al.getImporto()));
                setFieldsValue(form, fields, "MODALITA", modalita);
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
            List<Registro_completo> out = d1.registro_modello6(Ints.checkedCast(pf.getId()));
            d1.closeDB();

            TipoDoc p = e.getEm().find(TipoDoc.class, 31L);
            String contentb64 = p.getModello();

            String pathtemp = e.getPath("pathtemp");
            createDir(pathtemp);

            File pdfOut = new File(pathtemp + username + "_"
                    + getOnlyStrings(sa.getRagionesociale()) + "_"
                    + dataconsegna.toString("ddMMyyyyHHmmSSS") + ".M6.pdf");

            try (InputStream is = new ByteArrayInputStream(decodeBase64(contentb64)); PdfReader reader = new PdfReader(is); PdfWriter writer = new PdfWriter(pdfOut); PdfDocument pdfDoc = new PdfDocument(reader, writer)) {
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

                //PAGINA 3
                ModelliPrg m3 = Utility.filterModello3(pf.getModelli());

                LinkedList<String> lezioniA = m3.getLezioni().stream()
                        .map(l1 -> new DateTime(l1.getGiorno())
                        .toString("dd/MM/yyyy")).distinct()
                        .collect(Collectors.toCollection(LinkedList::new));

                if (!lezioniA.isEmpty()) {

                    String DATAINIZIOFASEA = lezioniA.getFirst();
                    String DATAFINEFASEA = lezioniA.getLast();

                    List<Registro_completo> faseA = out.stream().filter(r1
                            -> r1.getRuolo().contains("ALLIEVO")
                            && r1.getFase().equalsIgnoreCase("A"))
                            .collect(Collectors.toList());

                    List<Registro_completo> allieviFaseA = new ArrayList<>();
                    List<Integer> allieviIDINT = faseA.stream().map(r1 -> r1.getIdutente()).distinct().collect(Collectors.toList());
                    AtomicInteger index_allieviA = new AtomicInteger(1);

                    allieviIDINT.forEach(n1 -> {
                        Allievi n2 = e.getEm().find(Allievi.class, Long.valueOf(String.valueOf(n1)));
                        Registro_completo allievo_A = new Registro_completo();
                        allievo_A.setId(index_allieviA.get());
                        allievo_A.setCognome(n2.getCognome().toUpperCase());
                        allievo_A.setNome(n2.getNome().toUpperCase());
                        allievo_A.setCf(n2.getCodicefiscale().toUpperCase());

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

                        for (int in = 1; in <= orario.size(); in++) {
                            switch (in) {
                                case 1 -> {
                                    allievo_A.setData1(orario.get(1));
                                }
                                case 2 -> {
                                    allievo_A.setData2(orario.get(2));
                                }
                                case 3 -> {
                                    allievo_A.setData3(orario.get(3));
                                }
                                case 4 -> {
                                    allievo_A.setData4(orario.get(4));
                                }
                                case 5 -> {
                                    allievo_A.setData5(orario.get(5));
                                }
                                case 6 -> {
                                    allievo_A.setData6(orario.get(6));
                                }
                                case 7 -> {
                                    allievo_A.setData7(orario.get(7));
                                }
                                case 8 -> {
                                    allievo_A.setData8(orario.get(8));
                                }
                                case 9 -> {
                                    allievo_A.setData9(orario.get(9));
                                }
                                case 10 -> {
                                    allievo_A.setData10(orario.get(10));
                                }
                                case 11 -> {
                                    allievo_A.setData11(orario.get(11));
                                }
                                case 12 -> {
                                    allievo_A.setData12(orario.get(12));
                                }
                                case 13 -> {
                                    allievo_A.setData13(orario.get(13));
                                }
                                case 14 -> {
                                    allievo_A.setData14(orario.get(14));
                                }
                                case 15 -> {
                                    allievo_A.setData15(orario.get(15));
                                }
                                case 16 -> {
                                    allievo_A.setData16(orario.get(16));
                                }
                                case 17 -> {
                                    allievo_A.setData17(orario.get(17));
                                }
                                case 18 -> {
                                    allievo_A.setData18(orario.get(18));
                                }
                                case 19 -> {
                                    allievo_A.setData19(orario.get(19));
                                }
                                case 20 -> {
                                    allievo_A.setData20(orario.get(20));
                                }
                                case 21 -> {
                                    allievo_A.setData21(orario.get(21));
                                }
                                case 22 -> {
                                    allievo_A.setData22(orario.get(22));
                                }
                                case 23 -> {
                                    allievo_A.setData23(orario.get(23));
                                }
                                case 24 -> {
                                    allievo_A.setData24(orario.get(24));
                                }
                            }
                        }
                        allievo_A.setTotaleore(totaleA.get());
                        allieviFaseA.add(allievo_A);
                        index_allieviA.addAndGet(1);
                    });

                    setFieldsValue(form, fields, "DATAINIZIOFASEA", DATAINIZIOFASEA);
                    setFieldsValue(form, fields, "DATAFINEFASEA", DATAFINEFASEA);

                    AtomicInteger indicigiorniA = new AtomicInteger(1);
                    lezioniA.forEach(lezione -> {
                        setFieldsValue(form, fields, "DATA" + indicigiorniA.get() + "FASEA", lezione);

                        Lezioni_Modelli lm1 = m3.getLezioni().stream().filter(l1 -> new DateTime(l1.getGiorno())
                                .toString("dd/MM/yyyy").equals(lezione)).findAny().orElse(null);
                        if (lm1 == null) {
                            setFieldsValue(form, fields, "TIPO" + indicigiorniA.get() + "FASEA", "-");
                        } else {
                            setFieldsValue(form, fields, "TIPO" + indicigiorniA.get() + "FASEA", lm1.getTipolez());
                        }
                        indicigiorniA.addAndGet(1);
                    });

                    allieviFaseA.forEach(al1 -> {
                        int indice = al1.getId();
                        setFieldsValue(form, fields, "Cognome" + indice, al1.getCognome().toUpperCase());
                        setFieldsValue(form, fields, "Nome" + indice, al1.getNome().toUpperCase());
                        setFieldsValue(form, fields, "CF" + indice, al1.getCf().toUpperCase());

                        setFieldsValue(form, fields, "OREA1_" + indice, roundFloatAndFormat(al1.getData1(), true, false));
                        setFieldsValue(form, fields, "OREA2_" + indice, roundFloatAndFormat(al1.getData2(), true, false));
                        setFieldsValue(form, fields, "OREA3_" + indice, roundFloatAndFormat(al1.getData3(), true, false));
                        setFieldsValue(form, fields, "OREA4_" + indice, roundFloatAndFormat(al1.getData4(), true, false));
                        setFieldsValue(form, fields, "OREA5_" + indice, roundFloatAndFormat(al1.getData5(), true, false));
                        setFieldsValue(form, fields, "OREA6_" + indice, roundFloatAndFormat(al1.getData6(), true, false));
                        setFieldsValue(form, fields, "OREA7_" + indice, roundFloatAndFormat(al1.getData7(), true, false));
                        setFieldsValue(form, fields, "OREA8_" + indice, roundFloatAndFormat(al1.getData8(), true, false));
                        setFieldsValue(form, fields, "OREA9_" + indice, roundFloatAndFormat(al1.getData9(), true, false));
                        setFieldsValue(form, fields, "OREA10_" + indice, roundFloatAndFormat(al1.getData10(), true, false));
                        setFieldsValue(form, fields, "OREA11_" + indice, roundFloatAndFormat(al1.getData11(), true, false));
                        setFieldsValue(form, fields, "OREA12_" + indice, roundFloatAndFormat(al1.getData12(), true, false));
                        setFieldsValue(form, fields, "OREA13_" + indice, roundFloatAndFormat(al1.getData13(), true, false));
                        setFieldsValue(form, fields, "OREA14_" + indice, roundFloatAndFormat(al1.getData14(), true, false));
                        setFieldsValue(form, fields, "OREA15_" + indice, roundFloatAndFormat(al1.getData15(), true, false));
                        setFieldsValue(form, fields, "OREA16_" + indice, roundFloatAndFormat(al1.getData16(), true, false));
                        setFieldsValue(form, fields, "OREA17_" + indice, roundFloatAndFormat(al1.getData17(), true, false));
                        setFieldsValue(form, fields, "OREA18_" + indice, roundFloatAndFormat(al1.getData18(), true, false));
                        setFieldsValue(form, fields, "OREA19_" + indice, roundFloatAndFormat(al1.getData19(), true, false));
                        setFieldsValue(form, fields, "OREA20_" + indice, roundFloatAndFormat(al1.getData20(), true, false));
                        setFieldsValue(form, fields, "OREA21_" + indice, roundFloatAndFormat(al1.getData21(), true, false));
                        setFieldsValue(form, fields, "OREA22_" + indice, roundFloatAndFormat(al1.getData22(), true, false));
                        setFieldsValue(form, fields, "OREA23_" + indice, roundFloatAndFormat(al1.getData23(), true, false));
                        setFieldsValue(form, fields, "OREA24_" + indice, roundFloatAndFormat(al1.getData24(), true, false));
                        setFieldsValue(form, fields, "TOTALEA" + indice, roundFloatAndFormat(al1.getTotaleore(), true, false));

                    });

                    // PAGINA 4 - 7
                    ModelliPrg m4 = Utility.filterModello4(pf.getModelli());

                    List<Registro_completo> faseB = out.stream().filter(r1
                            -> r1.getRuolo().contains("ALLIEVO")
                            && r1.getFase().equalsIgnoreCase("B"))
                            .collect(Collectors.toList());

                    List<Registro_completo> allieviFaseB = new ArrayList<>();
                    for (int i = 1; i < 13; i++) {

                        AtomicInteger indiceb = new AtomicInteger(i);

                        LinkedList<String> lezioniB = m4.getLezioni().stream().filter(l1 -> l1.getGruppo_faseB() == indiceb.get())
                                .map(l1 -> new DateTime(l1.getGiorno())
                                .toString("dd/MM/yyyy")).distinct()
                                .collect(Collectors.toCollection(LinkedList::new));

                        if (!lezioniB.isEmpty()) {

                            String DATAINIZIOFASEB = lezioniB.getFirst();
                            String DATAFINEFASEB = lezioniB.getLast();

                            List<Registro_completo> grupposingolo = faseB.stream().filter(r2 -> r2.getGruppofaseb()
                                    == indiceb.get()).collect(Collectors.toList());

                            allieviIDINT = grupposingolo.stream().map(r1 -> r1.getIdutente()).distinct().collect(Collectors.toList());
                            AtomicInteger index_allieviB = new AtomicInteger(1);
                            allieviIDINT.forEach(n1 -> {
                                Allievi n2 = e.getEm().find(Allievi.class, Long.valueOf(String.valueOf(n1)));

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

                                    for (int in = 1; in <= orarioB.size(); in++) {
                                        switch (in) {
                                            case 1 -> {
                                                allievo_B.setData1(orarioB.get(1));
                                            }
                                            case 2 -> {
                                                allievo_B.setData2(orarioB.get(2));
                                            }
                                            case 3 -> {
                                                allievo_B.setData3(orarioB.get(3));
                                            }
                                            case 4 -> {
                                                allievo_B.setData4(orarioB.get(4));
                                            }
                                            case 5 -> {
                                                allievo_B.setData5(orarioB.get(5));
                                            }
                                            case 6 -> {
                                                allievo_B.setData6(orarioB.get(6));
                                            }
                                            case 7 -> {
                                                allievo_B.setData7(orarioB.get(7));
                                            }
                                            case 8 -> {
                                                allievo_B.setData8(orarioB.get(8));
                                            }
                                            case 9 -> {
                                                allievo_B.setData9(orarioB.get(9));
                                            }
                                            case 10 -> {
                                                allievo_B.setData10(orarioB.get(10));
                                            }
                                            case 11 -> {
                                                allievo_B.setData11(orarioB.get(11));
                                            }
                                            case 12 -> {
                                                allievo_B.setData12(orarioB.get(12));
                                            }
                                            case 13 -> {
                                                allievo_B.setData13(orarioB.get(13));
                                            }
                                            case 14 -> {
                                                allievo_B.setData14(orarioB.get(14));
                                            }
                                        }
                                    }
                                    allievo_B.setTotaleore(totaleB.get());
                                    allieviFaseB.add(allievo_B);
                                } catch (Exception exx) {
                                }
                                index_allieviB.addAndGet(1);
                            });

                            setFieldsValue(form, fields, "DATAINIZIOB" + indiceb.get(), DATAINIZIOFASEB);
                            setFieldsValue(form, fields, "DATAFINEB" + indiceb.get(), DATAFINEFASEB);

                            AtomicInteger indicigiorniB = new AtomicInteger(1);
                            lezioniB.forEach(lezione -> {
                                setFieldsValue(form, fields, "DATAB" + indiceb.get() + "_" + indicigiorniB.get(), lezione);

                                Lezioni_Modelli lm1 = m4.getLezioni().stream().filter(l1 -> new DateTime(l1.getGiorno())
                                        .toString("dd/MM/yyyy").equals(lezione) && indiceb.get() == l1.getGruppo_faseB()).findAny().orElse(null);
                                if (lm1 == null) {
                                    setFieldsValue(form, fields, "TIPOB" + indiceb.get() + "_" + indicigiorniB.get(), "-");
                                } else {
                                    setFieldsValue(form, fields, "TIPOB" + indiceb.get() + "_" + indicigiorniB.get(), lm1.getTipolez());
                                }

                                indicigiorniB.addAndGet(1);
                            });

                            allieviFaseB.forEach(al1 -> {
                                int indice = al1.getId();
                                setFieldsValue(form, fields, "CognomeB" + al1.getGruppoB() + "_" + indice, al1.getCognome().toUpperCase());
                                setFieldsValue(form, fields, "NomeB" + al1.getGruppoB() + "_" + indice, al1.getNome().toUpperCase());
                                setFieldsValue(form, fields, "CFB" + al1.getGruppoB() + "_" + indice, al1.getCf().toUpperCase());
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_1", roundFloatAndFormat(al1.getData1(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_2", roundFloatAndFormat(al1.getData2(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_3", roundFloatAndFormat(al1.getData3(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_4", roundFloatAndFormat(al1.getData4(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_5", roundFloatAndFormat(al1.getData5(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_6", roundFloatAndFormat(al1.getData6(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_7", roundFloatAndFormat(al1.getData7(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_8", roundFloatAndFormat(al1.getData8(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_9", roundFloatAndFormat(al1.getData9(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_10", roundFloatAndFormat(al1.getData10(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_11", roundFloatAndFormat(al1.getData11(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_12", roundFloatAndFormat(al1.getData12(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_13", roundFloatAndFormat(al1.getData13(), true, false));
                                setFieldsValue(form, fields, "OREB" + al1.getGruppoB() + "_" + indice + "_14", roundFloatAndFormat(al1.getData14(), true, false));
                                setFieldsValue(form, fields, "TOTALEB" + al1.getGruppoB() + "_" + indice, roundFloatAndFormat(al1.getTotaleore(), true, false));
                            });

                        }

                    }

                    //PAGINA 8 
                    List<Registro_completo> docentifaseA = out.stream().filter(r1
                            -> r1.getRuolo().equalsIgnoreCase("DOCENTE")
                            && r1.getFase().equalsIgnoreCase("A")).collect(Collectors.toList());

                    List<Registro_completo> docentifaseB = out.stream().filter(r1
                            -> r1.getRuolo().equalsIgnoreCase("DOCENTE")
                            && r1.getFase().equalsIgnoreCase("B")).collect(Collectors.toList());

                    List<Integer> docentiid = docentifaseA.stream().map(r1
                            -> r1.getIdutente()).distinct().collect(Collectors.toList());

                    List<Integer> docentiid_B = docentifaseB.stream().map(r1
                            -> r1.getIdutente()).distinct().collect(Collectors.toList());

                    List<Registro_completo> docenti = new ArrayList<>();

                    AtomicLong totaleD_A = new AtomicLong(0L);

                    AtomicInteger index_docenti = new AtomicInteger(1);
                    AtomicInteger index_docenti_B = new AtomicInteger(1);

                    docentiid.forEach(r1 -> {
                        Docenti docente = e.getEm().find(Docenti.class, Long.valueOf(String.valueOf(r1)));
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

                            for (int in = 1; in <= orarioD.size(); in++) {
                                switch (in) {
                                    case 1 -> {
                                        doc1.setData1(orarioD.get(1));
                                    }
                                    case 2 -> {
                                        doc1.setData2(orarioD.get(2));
                                    }
                                    case 3 -> {
                                        doc1.setData3(orarioD.get(3));
                                    }
                                    case 4 -> {
                                        doc1.setData4(orarioD.get(4));
                                    }
                                    case 5 -> {
                                        doc1.setData5(orarioD.get(5));
                                    }
                                    case 6 -> {
                                        doc1.setData6(orarioD.get(6));
                                    }
                                    case 7 -> {
                                        doc1.setData7(orarioD.get(7));
                                    }
                                    case 8 -> {
                                        doc1.setData8(orarioD.get(8));
                                    }
                                    case 9 -> {
                                        doc1.setData9(orarioD.get(9));
                                    }
                                    case 10 -> {
                                        doc1.setData10(orarioD.get(10));
                                    }
                                    case 11 -> {
                                        doc1.setData11(orarioD.get(11));
                                    }
                                    case 12 -> {
                                        doc1.setData12(orarioD.get(12));
                                    }
                                    case 13 -> {
                                        doc1.setData13(orarioD.get(13));
                                    }
                                    case 14 -> {
                                        doc1.setData14(orarioD.get(14));
                                    }
                                    case 15 -> {
                                        doc1.setData15(orarioD.get(15));
                                    }
                                    case 16 -> {
                                        doc1.setData16(orarioD.get(16));
                                    }
                                    case 17 -> {
                                        doc1.setData17(orarioD.get(17));
                                    }
                                    case 18 -> {
                                        doc1.setData18(orarioD.get(18));
                                    }
                                    case 19 -> {
                                        doc1.setData19(orarioD.get(19));
                                    }
                                    case 20 -> {
                                        doc1.setData20(orarioD.get(20));
                                    }
                                    case 21 -> {
                                        doc1.setData21(orarioD.get(21));
                                    }
                                    case 22 -> {
                                        doc1.setData22(orarioD.get(22));
                                    }
                                    case 23 -> {
                                        doc1.setData23(orarioD.get(23));
                                    }
                                    case 24 -> {
                                        doc1.setData24(orarioD.get(24));
                                    }
                                }
                            }
                            doc1.setTotaleore(totaleA.get());
                            docenti.add(doc1);
                        }
                    });

                    docenti.forEach(doc1 -> {
                        int indice = doc1.getId();

                        setFieldsValue(form, fields, "CognomeD_A" + indice, doc1.getCognome().toUpperCase());
                        setFieldsValue(form, fields, "NomeD_A" + indice, doc1.getNome().toUpperCase());
                        setFieldsValue(form, fields, "CFD_A" + indice, doc1.getCf().toUpperCase());
//                        ORED_A1_1
                        setFieldsValue(form, fields, "ORED_A1_" + indice, roundFloatAndFormat(doc1.getData1(), true, false));
                        setFieldsValue(form, fields, "ORED_A2_" + indice, roundFloatAndFormat(doc1.getData2(), true, false));
                        setFieldsValue(form, fields, "ORED_A3_" + indice, roundFloatAndFormat(doc1.getData3(), true, false));
                        setFieldsValue(form, fields, "ORED_A4_" + indice, roundFloatAndFormat(doc1.getData4(), true, false));
                        setFieldsValue(form, fields, "ORED_A5_" + indice, roundFloatAndFormat(doc1.getData5(), true, false));
                        setFieldsValue(form, fields, "ORED_A6_" + indice, roundFloatAndFormat(doc1.getData6(), true, false));
                        setFieldsValue(form, fields, "ORED_A7_" + indice, roundFloatAndFormat(doc1.getData7(), true, false));
                        setFieldsValue(form, fields, "ORED_A8_" + indice, roundFloatAndFormat(doc1.getData8(), true, false));
                        setFieldsValue(form, fields, "ORED_A9_" + indice, roundFloatAndFormat(doc1.getData9(), true, false));
                        setFieldsValue(form, fields, "ORED_A10_" + indice, roundFloatAndFormat(doc1.getData10(), true, false));
                        setFieldsValue(form, fields, "ORED_A11_" + indice, roundFloatAndFormat(doc1.getData11(), true, false));
                        setFieldsValue(form, fields, "ORED_A12_" + indice, roundFloatAndFormat(doc1.getData12(), true, false));
                        setFieldsValue(form, fields, "ORED_A13_" + indice, roundFloatAndFormat(doc1.getData13(), true, false));
                        setFieldsValue(form, fields, "ORED_A14_" + indice, roundFloatAndFormat(doc1.getData14(), true, false));
                        setFieldsValue(form, fields, "ORED_A15_" + indice, roundFloatAndFormat(doc1.getData15(), true, false));
                        setFieldsValue(form, fields, "ORED_A16_" + indice, roundFloatAndFormat(doc1.getData16(), true, false));
                        setFieldsValue(form, fields, "ORED_A17_" + indice, roundFloatAndFormat(doc1.getData17(), true, false));
                        setFieldsValue(form, fields, "ORED_A18_" + indice, roundFloatAndFormat(doc1.getData18(), true, false));
                        setFieldsValue(form, fields, "ORED_A19_" + indice, roundFloatAndFormat(doc1.getData19(), true, false));
                        setFieldsValue(form, fields, "ORED_A20_" + indice, roundFloatAndFormat(doc1.getData20(), true, false));
                        setFieldsValue(form, fields, "ORED_A21_" + indice, roundFloatAndFormat(doc1.getData21(), true, false));
                        setFieldsValue(form, fields, "ORED_A22_" + indice, roundFloatAndFormat(doc1.getData22(), true, false));
                        setFieldsValue(form, fields, "ORED_A23_" + indice, roundFloatAndFormat(doc1.getData23(), true, false));
                        setFieldsValue(form, fields, "ORED_A24_" + indice, roundFloatAndFormat(doc1.getData24(), true, false));
                        setFieldsValue(form, fields, "TOTALED_A" + indice, roundFloatAndFormat(doc1.getTotaleore(), true, false));
                    });

                    docentiid_B.forEach(r1 -> {
                        Docenti docente = e.getEm().find(Docenti.class, Long.valueOf(String.valueOf(r1)));
                        if (docente != null) {
                            AtomicLong totaleB = new AtomicLong(0L);

                            docentifaseB.stream().filter(d11 -> d11.getIdutente() == r1).forEach(a1 -> {
                                totaleB.addAndGet(a1.getTotaleorerendicontabili());
                            });

                            setFieldsValue(form, fields, "CognomeD_B" + index_docenti_B.get(), docente.getCognome().toUpperCase());
                            setFieldsValue(form, fields, "NomeD_B" + index_docenti_B.get(), docente.getNome().toUpperCase());
                            setFieldsValue(form, fields, "CFD_B" + index_docenti_B.get(), docente.getCodicefiscale().toUpperCase());
                            setFieldsValue(form, fields, "TOTALEDB_" + index_docenti_B.get(), roundFloatAndFormat(totaleB.get(), true, false));
                            index_docenti_B.addAndGet(1);
                        }
                    });
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

                setFieldsValue(form, fields, "NOMESA", NOMESA);
                setFieldsValue(form, fields, "DD", DD);
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

                if (al.getComune_domicilio() != null) {
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

                String CIP = al.getProgetto().getCip();
                String DATAINIZIO = sdfITA.format(al.getProgetto().getStart());
                String DATAFINE = sdfITA.format(al.getProgetto().getEnd());

                setFieldsValue(form, fields, "CIP", CIP);
                setFieldsValue(form, fields, "DATAINIZIO", DATAINIZIO);
                setFieldsValue(form, fields, "DATAFINE", DATAFINE);

                setFieldsValue(form, fields, "STATOFINALE", al.getStatopartecipazione().getDescrizione());

                setFieldsValue(form, fields, "ORE_TOT", roundDoubleAndFormat(al.getImporto()));
                setFieldsValue(form, fields, "ORE_FA", roundDoubleAndFormat(al.getOrec_fasea()));
                setFieldsValue(form, fields, "ORE_FB", roundDoubleAndFormat(al.getOrec_faseb()));

                setFieldsValue(form, fields, "UD_FA", String.valueOf(al.getUd_ok_A()));
                setFieldsValue(form, fields, "UD_FB", String.valueOf(al.getUd_ok_B()));
                setFieldsValue(form, fields, "ASSENZEGIUST", String.valueOf(al.getAssenzeOK()));

                if (m5.getGrado_completezza() != null) {
                    setFieldsValue(form, fields, "grado_completezza" + m5.getGrado_completezza(), "Sì");
                }
                if (m5.getProbabilita() != null) {
                    setFieldsValue(form, fields, "probabilita" + m5.getProbabilita(), "Sì");
                }
                if (m5.getForma_giuridica() != null) {
                    setFieldsValue(form, fields, "forma_giuridica" + m5.getForma_giuridica().getId(), "Sì");
                }
                if (m5.getAteco() != null) {
                    setFieldsValue(form, fields, "ATECO", m5.getAteco().getId());
                }
                setFieldsValue(form, fields, "SEDEINDIVIDUATA", Utility.convertbooleantostring(m5.isSede()));

                if (m5.getComune_localizzazione() != null) {
                    setFieldsValue(form, fields, "COMUNE", m5.getComune_localizzazione().getNome().toUpperCase());
                    setFieldsValue(form, fields, "PROVINCIA", m5.getComune_localizzazione().getCod_provincia().toUpperCase());
                    setFieldsValue(form, fields, "REGIONE", m5.getComune_localizzazione().getRegione().toUpperCase());
                }
                setFieldsValue(form, fields, "TOT_FABB", Utility.numITA.format(m5.getTotale_fabbisogno()));

                setFieldsValue(form, fields, "MISURA_INDIVIDUATA", Utility.convertbooleantostring(m5.isMisura_individuata()));

                if (m5.isMisura_individuata()) {
                    setFieldsValue(form, fields, "MISURA_SI_NOME", m5.getMisura_si_nome().toUpperCase());
                    if (m5.getMisura_si_tipo() != null) {
                        setFieldsValue(form, fields, "misura_si_tipo" + m5.getMisura_si_tipo(), "Sì");
                    }
                    if (m5.getMisura_si_motivazione() != null) {
                        setFieldsValue(form, fields, "misura_si_motivazione" + m5.getMisura_si_motivazione(), "Sì");
                    }
                } else {
                    if (m5.getMisura_no_motivazione() != null) {
                        setFieldsValue(form, fields, "misura_no_motivazione" + m5.getMisura_no_motivazione(), "Sì");
                    }

                }

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

            String pathtemp = e.getPath("path.modello0");

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

                if (al.getComune_domicilio() != null) {
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
                if (al.getComune_domicilio() != null) {
                    setFieldsValue(form, fields, "dom_regione", al.getComune_domicilio().getRegione().toUpperCase());
                    setFieldsValue(form, fields, "dom_indirizzo", al.getIndirizzodomicilio().toUpperCase());
                    setFieldsValue(form, fields, "dom_comune", al.getComune_domicilio().getNome().toUpperCase());
                    setFieldsValue(form, fields, "dom_cap", al.getCapdomicilio().toUpperCase());
                    setFieldsValue(form, fields, "dom_prov", al.getComune_domicilio().getProvincia().toUpperCase());
                }
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

            TipoDoc p = e.getEm().find(TipoDoc.class, Long.valueOf(idmodello));
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

        if (Utility.test || Utility.demoversion) {
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

    public static File merge_PDF(List<String> elencopath, String dirtemp) {
        try {
            if (elencopath.isEmpty()) {
                return null;
            }
            File out = new File(dirtemp + UUID.randomUUID() + "_ATT.pdf");
            try (PdfDocument output = new PdfDocument(new PdfWriter(out))) {
                PdfMerger merger = new PdfMerger(output);
                for (String file : elencopath) {
                    try (PdfDocument pdfDocument2 = new PdfDocument(new PdfReader(file))) {
                        merger.merge(pdfDocument2, 1, pdfDocument2.getNumberOfPages());
                    }
                }
                merger.close();
            }
            return out;
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }
        return null;
    }

}
