<%@page import="java.util.concurrent.atomic.AtomicInteger"%>
<%@page import="rc.so.domain.Allievi"%>
<%@page import="rc.so.domain.Docenti"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.Date"%>
<%@page import="rc.so.db.Registro_completo"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="rc.so.domain.TipoDoc"%>
<%@page import="rc.so.domain.Lezioni_Modelli"%>
<%@page import="rc.so.domain.ModelliPrg"%>
<%@page import="java.util.Map"%>
<%@page import="rc.so.util.Utility"%>
<%@page import="rc.so.domain.LezioneCalendario"%>
<%@page import="rc.so.domain.ProgettiFormativi"%>
<%@page import="rc.so.domain.SoggettiAttuatori"%>
<%@page import="rc.so.db.Action"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="rc.so.db.Entity"%>
<%@page import="rc.so.entity.Item"%>
<%@page import="rc.so.domain.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    User us = (User) session.getAttribute("user");
    if (us == null) {
    } else {
        String uri_ = request.getRequestURI();
        String pageName_ = uri_.substring(uri_.lastIndexOf("/") + 1);
        if (!Action.isVisibile(String.valueOf(us.getTipo()), pageName_)) {
            response.sendRedirect(request.getContextPath() + "/page_403.jsp");
        } else {
            boolean update = true;
            String giorno = Utility.getRequestValue(request, "giorno");
            String fase = Utility.getRequestValue(request, "fase");
            String idpr = Utility.getRequestValue(request, "idpr");
            AtomicInteger ind_gruppo = new AtomicInteger(1);
            String gruppo = Utility.getRequestValue(request, "gruppo");
            if (gruppo.equals("")) {
                gruppo = "1";
            } else {
                ind_gruppo.set(Integer.parseInt(gruppo));
            }
            DateTime data = Utility.format(Utility.getRequestValue(request, "data"), "yyyyMMdd");
            String src = session.getAttribute("src").toString();
            Entity e = new Entity();
            ProgettiFormativi p = e.getEm().find(ProgettiFormativi.class, Long.parseLong(idpr));
            List<LezioneCalendario> lezioniCalendarioFASEA = e.getLezioniByModello(3);
            List<LezioneCalendario> lezioniCalendarioFASEB = e.getLezioniByModello(4);

            ModelliPrg m3 = Utility.filterModello3(p.getModelli());
            ModelliPrg m4 = Utility.filterModello4(p.getModelli());
            List<Allievi> allievi_start = p.getAllievi().stream().filter(p1 -> p1.getStatopartecipazione().getId().equals("01")).collect(Collectors.toList());
            List<Lezioni_Modelli> lezioniA = m3.getLezioni();
            List<LezioneCalendario> grouppedByLezioneA = Utility.grouppedByLezione(lezioniCalendarioFASEA);
            List<Lezioni_Modelli> lezioniB = m4.getLezioni();
            List<LezioneCalendario> grouppedByLezioneB = Utility.grouppedByLezione(lezioniCalendarioFASEB);

            List<Registro_completo> rc1 = Action.registro_modello6(idpr).stream().filter(rr1 -> rr1.getData().equals(data))
                    .collect(Collectors.toList());
            List<String[]> select_ore = Action.ore_rendicontabili();

            Lezioni_Modelli temp = null;

            List<Lezioni_Modelli> lezionidelgiorno = new ArrayList<>();
            if (fase.equals("A")) {
                for (LezioneCalendario lez : grouppedByLezioneA) {
                    Lezioni_Modelli tempA = Utility.lezioneFiltered(lezioniA, lez.getId());
                    if (tempA != null) {
                        temp = tempA;
                        lezionidelgiorno = lezioniA.stream().filter(l1 -> new DateTime(l1.getGiorno()).equals(data)).collect(Collectors.toList());
                        allievi_start = p.getAllievi().stream().filter(p1 -> p1.getStatopartecipazione().getId().equals("01")).collect(Collectors.toList());
                        break;
                    }
                }
            } else if (fase.equals("B")) {
                for (LezioneCalendario lez : grouppedByLezioneB) {
                    Lezioni_Modelli tempB = Utility.lezioneFilteredByGroup(lezioniB, lez.getId(), ind_gruppo.get());
                    if (tempB != null) {
                        temp = tempB;
                        lezionidelgiorno = lezioniB.stream().filter(l1 -> new DateTime(l1.getGiorno()).equals(data)).collect(Collectors.toList());
                        allievi_start = p.getAllievi().stream().filter(p1 -> p1.getStatopartecipazione().getId().equals("01")
                                && p1.getGruppo_faseB() == ind_gruppo.get())
                                .collect(Collectors.toList());
                        break;
                    }
                }
            }

            if (fase.equals("A")) {
                if (p.getStato().getId().equals("ATA") || p.getStato().getId().equals("SOA")) {
                    update = true;
                } else {
                    update = false;
                }
            } else {
                if (p.getStato().getId().equals("ATB") || p.getStato().getId().equals("SOB") || p.getStato().getId().equals("F")) {
                    update = true;
                } else {
                    update = false;
                }
            }

            e.close();
%>
<html>
    <head>
        <meta charset="utf-8" />
        <title>YES I Start Up - Toscana - Registro Cartaceo</title>
        <meta name="description" content="Updates and statistics">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <script src="<%=src%>/resource/webfont.js"></script>
        <script>
            WebFont.load({
                google: {
                    "families": ["Poppins:300,400,500,600,700", "Roboto:300,400,500,600,700"]
                },
                active: function () {
                    sessionStorage.fonts = true;
                }
            });
        </script>
        <!-- this page -->
        <link href="<%=src%>/assets/vendors/general/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet" type="text/css"/>
        <link href="<%=src%>/resource/datatbles.bundle.css" rel="stylesheet" type="text/css"/>
        <link href="<%=src%>/assets/vendors/general/bootstrap-select/dist/css/bootstrap-select.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/select2/dist/css/select2.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/perfect-scrollbar/css/perfect-scrollbar.css" rel="stylesheet" type="text/css" />
        <!-- - -->
        <link href="<%=src%>/assets/vendors/general/owl.carousel/dist/assets/owl.carousel.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/owl.carousel/dist/assets/owl.theme.default.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/line-awesome/css/line-awesome.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/flaticon/flaticon.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/flaticon2/flaticon.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/fontawesome5/css/all.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/resource/animate.css" rel="stylesheet" type="text/css"/>
        <link href="<%=src%>/assets/demo/default/base/style.bundle.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/resource/custom.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/skins/header/base/light.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/skins/header/menu/light.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/skins/brand/light.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/skins/aside/light.css" rel="stylesheet" type="text/css" />
        <link rel="shortcut icon" href="<%=src%>/assets/media/logos/favicon.ico" />
    </head>
    <body class="kt-header--fixed kt-header-mobile--fixed kt-subheader--fixed kt-subheader--enabled">
        <!-- begin:: Page -->
        <div class="modal" tabindex="-1" role="dialog" id="modalerror">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">ERRORE!</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p id="modaltexterror">Modal body text goes here.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">OK</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="kt-content" id="kt_content">
            <div class="kt-portlet kt-portlet--mobile">
                <div class="kt-portlet__body">
                    <div class="kt-section">
                        <div class="row">
                            <div class="col-12">
                                <table class="table" style="width: 100%;" >
                                    <thead>
                                    <th colspan="1" style="width: 30px">
                                        <a href="registroaula.jsp?id=<%=idpr%>" 
                                           class="btn btn-icon btn-sm btn-circle btn-primary"
                                           data-container="body" 
                                           data-toggle="kt-popover" 
                                           data-html="true"
                                           data-placement="bottom"
                                           data-content="TORNA INDIETRO"><i class="fa fa-arrow-left"></i></a>
                                    </th>
                                    <th colspan="3">
                                        Registro Lezioni - Modalità svolgimento <u>IN PRESENZA</u>: | 
                                        Lezione del <%=data.toString("dd/MM/yyyy")%> |
                                        Ora Inizio: <%=temp.getOrainizio()%> | 
                                        Ora Fine: <%=temp.getOrafine()%> | 
                                        Fase/Gruppo: <%=fase%>/<%=gruppo%> | 
                                        <i class="fa fa-info-circle kt-font-danger" 
                                           data-container="body" 
                                           data-toggle="kt-popover" 
                                           data-html="true"
                                           data-placement="bottom"
                                           data-original-title="Calendario"
                                           data-content="Il Soggetto Attuatore si assume la responsabilità di quanto inserito in questo registro, a fine progetto verrà richiesto
                                           di caricare il registro cartaceo ORIGINALE per effettuare le verifiche incrociate."></i>
                                    </th>
                                    </thead>
                                </table>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-12">
                                <%if (rc1.isEmpty()) {
                                %>
                                <form method="POST" action="<%=src%>/OperazioniSA" id="form_add">
                                    <input type="hidden" name="type" value="addregistro" />
                                    <input type="hidden" name="idpr" value="<%=idpr%>" />
                                    <input type="hidden" name="idsa" value="<%=p.getSoggetto().getId()%>" />
                                    <input type="hidden" name="cip" value="<%=p.getCip()%>" />
                                    <input type="hidden" name="data" value="<%=data.toString(Utility.patternSql)%>" />
                                    <input type="hidden" name="giorno" value="<%=giorno%>" />
                                    <input type="hidden" name="fase" value="<%=fase%>" />
                                    <input type="hidden" name="gruppofaseb" value="<%=gruppo%>" />
                                    <input type="hidden" name="start" value="<%=temp.getOrainizio()%>" />
                                    <table class="table table-bordered" style="width: 100%;" >
                                        <%for (Lezioni_Modelli l1 : lezionidelgiorno) {

                                                Docenti do1 = l1.getDocente();
                                                long difference = Utility.calcolaintervallomillis(l1.getOrainizio(), l1.getOrafine());

                                        %>

                                        <input type="hidden" name="start_<%=l1.getId()%>" value="<%=l1.getOrainizio()%>" />
                                        <input type="hidden" name="ud_<%=l1.getId()%>" value="<%=l1.getLezione_calendario().getUnitadidattica().getCodice()%>" />
                                        <thead>
                                            <tr class="bg-primary text-white"><th colspan="4" 
                                                                                  style="text-align: center;">LEZIONE <%=l1.getOrainizio()%> - <%=l1.getOrafine()%></th>
                                            </tr>
                                            <tr>
                                                <th>RUOLO</th>
                                                <th>COGNOME</th>
                                                <th>NOME</th>
                                                <th>ORE RENDICONTABILI</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr class="bg-light">
                                                <td>DOCENTE</td>
                                                <td><%=do1.getCognome()%></td>
                                                <td><%=do1.getNome()%></td>
                                                <td>
                                                    <select class="form-control kt-select2-general" 
                                                            id="<%=do1.getId()%>_docereg_<%=l1.getId()%>" 
                                                            name="<%=do1.getId()%>_docereg_<%=l1.getId()%>" 
                                                            style="width: 100%" 
                                                            onchange="return setMaxhours(this.id, true);">
                                                        <%for (String[] s1 : select_ore) {
                                                                long ver = Long.parseLong(s1[0]);
                                                                if (ver <= difference) {
                                                        %>
                                                        <option value="<%=s1[0]%>"><%=s1[1]%></option>
                                                        <%}
                                                            }%>
                                                    </select>    
                                                </td>
                                            </tr>


                                            <%for (Allievi d1 : allievi_start) {%>
                                            <tr class="bg-light2">
                                                <td>ALLIEVO</td>
                                                <td><%=d1.getCognome()%></td>
                                                <td><%=d1.getNome()%></td>
                                                <td><select class="form-control kt-select2-general" 
                                                            id="<%=d1.getId()%>_allreg_<%=l1.getId()%>" 
                                                            name="<%=d1.getId()%>_allreg_<%=l1.getId()%>"  style="width: 100%" 
                                                            onchange="return getMaxhours(this.id, true)">
                                                        <%for (String[] s1 : select_ore) {
                                                                long ver = Long.parseLong(s1[0]);
                                                                if (ver <= difference) {
                                                        %>
                                                        <option value="<%=s1[0]%>"><%=s1[1]%></option>
                                                        <%}
                                                            }%>
                                                    </select></td>
                                            </tr>

                                            <%}%>



                                        </tbody>


                                        <%}%>
                                        <%if (update) {%>
                                        <tfoot>

                                        <th colspan="4" style="text-align: center;">
                                            <button type="submit" class="btn btn-block btn-success">
                                                <i class="fa fa-save"></i> SALVA REGISTRO
                                            </button>
                                        </th>
                                        </tfoot>
                                        <%}%>
                                    </table>
                                </form>

                                <%} else {

                                    List<Registro_completo> docenti = rc1.stream().filter(r1 -> r1.getRuolo().equals("DOCENTE"))
                                            .sorted(( object1,           object2) -> object1.getCognome().compareTo(object2.getCognome()))
                                            .collect(Collectors.toList());
                                    List<Registro_completo> allievi = rc1.stream().filter(r1 -> r1.getRuolo().contains("ALLIEVO"))
                                            .sorted(( object1,           object2) -> object1.getCognome().compareTo(object2.getCognome()))
                                            .collect(Collectors.toList());

                                %>
                                <form method="POST" action="<%=src%>/OperazioniSA" id="form_add">
                                    <input type="hidden" name="type" value="editregistro" />
                                    <input type="hidden" name="idpr" value="<%=idpr%>" />
                                    <input type="hidden" name="start" value="<%=temp.getOrainizio()%>" />
                                    <input type="hidden" name="data" value="<%=data.toString(Utility.patternSql)%>" />
                                    <input type="hidden" name="giorno" value="<%=giorno%>" />
                                    <input type="hidden" name="gruppofaseb" value="<%=gruppo%>" />
                                    <table class="table table-bordered" style="width: 100%;" >
                                        <thead>
                                        <th>RUOLO</th>
                                        <th>COGNOME</th>
                                        <th>NOME</th>
                                        <th>ORE RENDICONTABILI</th>
                                        </thead>
                                        <tr class="bg-light"><th colspan="4" 
                                                                 style="text-align: center;">DOCENTI</th>
                                        </tr>
                                        <%for (Registro_completo d1 : docenti) {%>
                                        <tr class="bg-light">
                                            <td>DOCENTE</td>
                                            <td><%=d1.getCognome()%></td>
                                            <td><%=d1.getNome()%></td>
                                            <td>
                                                <select class="form-control kt-select2-general" 
                                                        id="<%=d1.getId()%>_docereg" 
                                                        name="<%=d1.getId()%>_docereg"  style="width: 100%"
                                                        onchange="return setMaxhours(this.id, false);">
                                                    <%for (String[] s1 : select_ore) {
                                                            String sel = "";
                                                            if (s1[0].equals(String.valueOf(d1.getTotaleorerendicontabili()))) {
                                                                sel = "selected";
                                                            }
                                                    %>
                                                    <option <%=sel%> value="<%=s1[0]%>"><%=s1[1]%></option>
                                                    <%}%>
                                                </select>    
                                            </td>
                                        </tr>
                                        <%}%>
                                        <tr class="bg-light2">
                                            <th colspan="4" 
                                                style="text-align: center;">ALLIEVI</th>
                                        </tr>
                                        <%for (Registro_completo d1 : allievi) {%>
                                        <tr class="bg-light2">
                                            <td>ALLIEVO</td>
                                            <td><%=d1.getCognome()%></td>
                                            <td><%=d1.getNome()%></td>
                                            <td><select class="form-control kt-select2-general" 
                                                        id="<%=d1.getId()%>_allreg" 
                                                        name="<%=d1.getId()%>_allreg"  style="width: 100%"
                                                        onchange="return getMaxhours(this.id, false);">
                                                    <%for (String[] s1 : select_ore) {
                                                            String sel = "";
                                                            if (s1[0].equals(String.valueOf(d1.getTotaleorerendicontabili()))) {
                                                                sel = "selected";
                                                            }
                                                    %>
                                                    <option <%=sel%> value="<%=s1[0]%>"><%=s1[1]%></option>
                                                    <%}%>
                                                </select></td>
                                        </tr>

                                        <%}%>
                                        <tr class="bg-light2">
                                            <th colspan="4" style="text-align: center;"></th>
                                        </tr>
                                        <%if (update) {%>
                                        <tfoot>
                                        <th colspan="4" style="text-align: center;">
                                            <button class="btn btn-block btn-success">
                                                <i class="fa fa-save"></i> SALVA REGISTRO
                                            </button>
                                        </th>
                                        </tfoot>
                                        <%}%>
                                    </table>
                                </form>
                                <%}%>

                            </div>    
                        </div>    
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
</div>
</div>
<div id="kt_scrolltop" class="kt-scrolltop">
    <i class="fa fa-arrow-up"></i>
</div>
<script src="<%=src%>/assets/soop/js/jquery-3.6.1.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/popper.js/dist/umd/popper.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/bootstrap/dist/js/bootstrap.min.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/js-cookie/src/js.cookie.js" type="text/javascript"></script>
<script src="<%=src%>/assets/soop/js/moment.min.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/tooltip.js/dist/umd/tooltip.min.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/sticky-js/dist/sticky.min.js" type="text/javascript"></script>
<script src="<%=src%>/assets/demo/default/base/scripts.bundle.js" type="text/javascript"></script>
<script src="<%=src%>/assets/app/bundle/app.bundle.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.js" type="text/javascript"></script>
<script src="<%=src%>/assets/soop/js/utility.js" type="text/javascript"></script>
<link href="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
<!--this page -->
<script src="<%=src%>/assets/vendors/general/select2/dist/js/select2.full.js" type="text/javascript"></script>
<script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/select2.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/jquery-form/dist/jquery.form.min.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/custom/datatables/datatables.bundle.js" type="text/javascript"></script>
<script src="<%=src%>/assets/soop/js/loadTable.js" type="text/javascript"></script>
<script src="<%=src%>/resource/PerfectScroolbar/perfect-scrollbar.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/bootstrap-daterangepicker/daterangepicker.js" type="text/javascript"></script>
<script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/bootstrap-datepicker.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/js/bootstrap-datepicker.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/inputmask/dist/inputmask/inputmask.js" type="text/javascript"></script>
<link href="<%=src%>/assets/soop/css/jquery.fancybox.min.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=src%>/assets/soop/js/jquery.fancybox.min.js"></script>
<script type="text/javascript" src="<%=src%>/assets/soop/js/fancy.js"></script>
<input type="hidden" id="systemtype" value="<%=Utility.iswindows()%>" />
<script type="text/javascript">
                                                            var KTAppOptions = {
                                                                "colors": {
                                                                    "state": {
                                                                        "brand": "#5d78ff",
                                                                        "dark": "#282a3c",
                                                                        "light": "#ffffff",
                                                                        "primary": "#5867dd",
                                                                        "success": "#34bfa3",
                                                                        "info": "#36a3f7",
                                                                        "warning": "#ffb822"
                                                                    },
                                                                    "base": {
                                                                        "label": ["#c5cbe3", "#a1a8c3", "#3d4465", "#3e4466"],
                                                                        "shape": ["#f0f3ff", "#d9dffa", "#afb4d4", "#646c9a"]
                                                                    }
                                                                }
                                                            };
                                                            function getMaxhours(idselect, check) {
                                                                var selected_2 = $('#' + idselect).val();
                                                                $('#form_add select').each(
                                                                        function (index) {
                                                                            var input = $(this);
                                                                            var ew = '_docereg';
                                                                            if (check) {
                                                                                ew = '_docereg_' + idselect.split("_")[2];
                                                                            }
                                                                            if (input.attr('id').endsWith(ew)) {
                                                                                var selected = $('#' + input.attr('id')).val();
                                                                                if (BigInt(selected_2) > BigInt(selected)) {
                                                                                    $('#' + idselect).val(selected).trigger('change');
                                                                                    $('#modaltexterror').html("Le ore dell'allievo non possono superare quelle del docente della lezione (<b>"
                                                                                            + $("#" + input.attr('id') + " option:selected").text() + "</b>).");
                                                                                    $('#modalerror').modal('toggle');
                                                                                    return false;
                                                                                }
                                                                            }
                                                                        }
                                                                );
                                                            }
                                                            function setMaxhours(idselect, check) {
                                                                var selected = $('#' + idselect).val();
                                                                $('#form_add select').each(
                                                                        function (index) {
                                                                            var input = $(this);
                                                                            var ew = '_allreg';
                                                                            if (check) {
                                                                                ew = '_allreg_' + idselect.split("_")[2];
                                                                            }
                                                                            if (input.attr('id').endsWith(ew)) {
                                                                                var selected_2 = $('#' + input.attr('id')).val();
                                                                                if (BigInt(selected_2) > BigInt(selected)) {
                                                                                    $('#' + input.attr('id')).val(selected).trigger('change');
                                                                                }
                                                                            }
                                                                        }
                                                                );
                                                            }

</script>
</body>
</html>
<%}
    }%>
