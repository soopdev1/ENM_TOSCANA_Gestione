/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import static rc.so.db.Action.createFile_R;
import rc.so.db.Database;
import rc.so.db.Entity;
import rc.so.domain.Allievi;
import rc.so.domain.Attivita;
import rc.so.domain.User;
import rc.so.util.Utility;
import static rc.so.util.Utility.getRequestValue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.String.format;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.EmailValidator;
import static rc.so.db.Action.insertTR;
import rc.so.domain.Email;
import rc.so.util.SendMailJet;
import static rc.so.util.Utility.estraiEccezione;
import static rc.so.util.Utility.redirect;

/**
 *
 * @author dolivo
 */
public class OperazioniGeneral extends HttpServlet {

    protected void sendmailModello0(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String maildest = getRequestValue(request, "maildest");
        String path = getRequestValue(request, "path");
        try {
            File f1 = new File(path);
            if (EmailValidator.getInstance().isValid(maildest)) {
                Entity e = new Entity();
                Email email_txt = (Email) e.getEmail("invio_modello0");
                SendMailJet.sendMail(e.getPath("mailsender"), new String[]{maildest}, null,
                        email_txt.getTesto()
                                .replace("@email_tec", e.getPath("emailtecnico"))
                                .replace("@email_am", e.getPath("emailamministrativo")),
                        email_txt.getOggetto(), f1);
                redirect(request, response, "page/mc/modello0.jsp?id=" + getRequestValue(request, "idallievo"));
            } else {
                redirect(request, response, "page/mc/modello0.jsp?esito=KO&id=" + getRequestValue(request, "idallievo"));
            }
        } catch (Exception ex) {
            insertTR("E", String.valueOf(((User) request.getSession().getAttribute("user")).getId()), estraiEccezione(ex));
            redirect(request, response, "page/mc/modello0.jsp?esito=KO&id=" + getRequestValue(request, "idallievo"));
        }

    }

    protected void onlyDownloadnew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        try {
            Entity e = new Entity();
            String manuale = e.getPath(path);
            e.close();
            if (manuale == null || manuale.equals("")) {
                response.sendRedirect("login.jsp");
            } else {
                File downloadFile = createFile_R(manuale);
                if (downloadFile != null
                        && downloadFile.exists()) {
                    String mimeType = Files.probeContentType(downloadFile.toPath());
                    if (mimeType == null) {
                        mimeType = "application/pdf";
                    }
                    response.setContentType(mimeType);
                    String headerKey = "Content-Disposition";
                    String headerValue = format("attach; filename=\"%s\"", downloadFile.getName());
                    response.setHeader(headerKey, headerValue);
                    response.setContentLength(-1);
                    try (OutputStream outStream = response.getOutputStream()) {
                        outStream.write(FileUtils.readFileToByteArray(downloadFile));
                    }
                } else {
                    response.sendRedirect("login.jsp");
                }
            }
        } catch (Exception ex1) {
            response.sendRedirect("login.jsp");
        }
    }

    protected void onlyDownload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        File downloadFile = createFile_R(path);
        if (downloadFile != null
                && downloadFile.exists()) {
            String mimeType = Files.probeContentType(downloadFile.toPath());
            if (mimeType == null) {
                mimeType = "application/pdf";
            }
            response.setContentType(mimeType);
            String headerKey = "Content-Disposition";
            String headerValue = format("attach; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);
            response.setContentLength(-1);
            try (OutputStream outStream = response.getOutputStream()) {
                outStream.write(FileUtils.readFileToByteArray(downloadFile));
            }
        } else {
            response.sendRedirect("404.jsp");
        }
    }

    protected void showDoc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getParameter("path");
        File downloadFile = createFile_R(path);
        if (downloadFile != null && downloadFile.exists()) {
            try (FileInputStream inStream = new FileInputStream(downloadFile)) {
                String mimeType = Files.probeContentType(downloadFile.toPath());
                if (mimeType == null) {
                    mimeType = "application/pdf";
                }
                response.setContentType(mimeType);
                String headerKey = "Content-Disposition";
                String headerValue = String.format("inline; filename=\"%s\"", downloadFile.getName());
                response.setHeader(headerKey, headerValue);
                try (OutputStream outStream = response.getOutputStream()) {
                    byte[] buffer = new byte[4096 * 4096];
                    int bytesRead;
                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                }
            }

        } else {
            User us = (User) request.getSession().getAttribute("user");
            String page = "page/";
            switch (us.getTipo()) {
                case 1:
                    page += "sa/indexSoggettoAttuatore.jsp";
                    break;
                case 4:
                    page += "ci/indexCi.jsp";
                    break;
                default:
                    page += "sa/indexMicrocredito.jsp";
                    break;
            }
            response.sendRedirect("redirect.jsp?page=" + page + "&fileNotFound=true");
        }
    }

    protected void downloadDoc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("path");
        File downloadFile = createFile_R(path);

        if (downloadFile.exists()) {
            try (FileInputStream inStream = new FileInputStream(downloadFile)) {
                String mimeType = Files.probeContentType(downloadFile.toPath());
                if (mimeType == null) {
                    mimeType = "application/pdf";
                }
                response.setContentType(mimeType);
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attach; filename=\"%s\"", downloadFile.getName());
                response.setHeader(headerKey, headerValue);
                try (OutputStream outStream = response.getOutputStream()) {
                    byte[] buffer = new byte[4096 * 4096];
                    int bytesRead;
                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, bytesRead);
                    }
                }
            }

        } else {
            User us = (User) request.getSession().getAttribute("user");
            String page = "page/";
            switch (us.getTipo()) {
                case 1:
                    page += "sa/indexSoggettoAttuatore.jsp";
                    break;
                case 4:
                    page += "ci/indexCi.jsp";
                    break;
                default:
                    page += "sa/indexMicrocredito.jsp";
                    break;
            }
            response.sendRedirect("redirect.jsp?page=" + page + "&fileNotFound=true");
        }
    }

    protected void excelfad(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idpr = request.getParameter("idpr");
        Database db1 = new Database(false);
        String base64 = db1.getBase64Report(Integer.parseInt(idpr));
        db1.closeDB();

//        String base64 = new ExcelFAD().generatereportFAD_multistanza(idpr);
        if (base64 != null) {
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", new Object[]{"Progetto_" + idpr + "_report_FAD.xlsx"});
            response.setHeader(headerKey, headerValue);
            try (OutputStream outStream = response.getOutputStream()) {
                outStream.write(Base64.getDecoder().decode(base64.getBytes()));
            }
        } else {
            User us = (User) request.getSession().getAttribute("user");
            String page = "page/";
            switch (us.getTipo()) {
                case 1:
                    page += "sa/indexSoggettoAttuatore.jsp";
                    break;
                case 4:
                    page += "ci/indexCi.jsp";
                    break;
                default:
                    page += "sa/indexMicrocredito.jsp";
                    break;
            }
            response.sendRedirect("redirect.jsp?page=" + page + "&noFileFound=true");
        }

    }

    protected void pdfTob64(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String path = getRequestValue(request, "path");
            byte[] input_file;
            if (path.equals("")) {
                input_file = Files.readAllBytes(Paths.get(((User) request.getSession().getAttribute("user")).getSoggettoAttuatore().getCartaid()));
            } else {
                input_file = Files.readAllBytes(Paths.get(path));
            }

            byte[] encodedBytes = Base64.getEncoder().encode(input_file);
            String encodedString = new String(encodedBytes);
            response.getWriter().write(encodedString);
            response.getWriter().flush();
            response.getWriter().close();

        } catch (Exception e) {
            response.getWriter().write("");
            response.getWriter().flush();
            response.getWriter().close();
        }

    }

    private void ctrlSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        Entity e = new Entity();
        boolean esito = request.getSession().getAttribute("user") == null || e.getPath("mantenance").equals("Y");
        e.close();
        response.getWriter().write(String.valueOf(esito));
        response.getWriter().flush();
        response.getWriter().close();
    }

    private void editMailNeet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Entity e = new Entity();
        JsonObject resp = new JsonObject();
        try {
            String idallievo = getRequestValue(request, "idallievo");
            String new_mail = getRequestValue(request, "new_mail");
            e.begin();
            Allievi a = e.getEm().find(Allievi.class, Long.valueOf(idallievo));
            a.setEmail(new_mail);
            e.merge(a);
            e.flush();
            e.commit();
            e.close();
            resp.addProperty("result", true);
        } catch (Exception ex) {
            resp.addProperty("result", false);
            resp.addProperty("message", "Errore Mail: " + Utility.estraiEccezione(ex));
        }

        response.getWriter().write(resp.toString());
        response.getWriter().flush();
        response.getWriter().close();

    }

    private void getAttivita(HttpServletResponse response) throws ServletException, IOException {
        Entity e = new Entity();
        List<Attivita> list = e.getAttivitaValide();
        e.close();

        Map<String, List<Attivita>> out = list.stream().collect(Collectors.groupingBy(a -> a.getComune().getProvincia()));

        response.setContentType("application/json");
        response.setHeader("Content-Type", "application/json");
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(out));
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        if ((User) request.getSession().getAttribute("user") == null) {
            String type = request.getParameter("type");
            if (type != null) {
                switch (type) {
                    case "ctrlSession":
                        ctrlSession(request, response);
                        break;
                    case "showDoc":
                        showDoc(request, response);
                        break;
                    case "onlyDownloadnew":
                        onlyDownloadnew(request, response);
                        break;
                    default:
                        break;
                }
            } else {
                response.sendRedirect("login.jsp");
            }
        } else {
            String type = request.getParameter("type");
            switch (type) {
                case "onlyDownload":
                    onlyDownload(request, response);
                    break;
                case "showDoc":
                    showDoc(request, response);
                    break;
                case "downloadDoc":
                    downloadDoc(request, response);
                    break;
                case "pdfTob64":
                    pdfTob64(request, response);
                    break;
                case "excelfad":
                    excelfad(request, response);
                    break;
                case "ctrlSession":
                    ctrlSession(request, response);
                    break;
                case "getAttivita":
                    getAttivita(response);
                    break;
                case "editMailNeet":
                    editMailNeet(request, response);
                    break;
                case "sendmailModello0":
                    sendmailModello0(request, response);
                    break;
                default:
                    break;
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
