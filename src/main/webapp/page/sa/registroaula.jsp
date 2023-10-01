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
            String idpr = Utility.getRequestValue(request, "id");
            String src = session.getAttribute("src").toString();
            Entity e = new Entity();
            ProgettiFormativi p = e.getEm().find(ProgettiFormativi.class, Long.parseLong(idpr));
            List<LezioneCalendario> lezioniCalendarioFASEA = e.getLezioniByModello(3);
            List<LezioneCalendario> lezioniCalendarioFASEB = e.getLezioniByModello(4);
            ModelliPrg m3 = Utility.filterModello3(p.getModelli());
            ModelliPrg m4 = Utility.filterModello4(p.getModelli());

            List<Lezioni_Modelli> lezioniA = m3.getLezioni();
            List<LezioneCalendario> grouppedByLezioneA = Utility.grouppedByLezione(lezioniCalendarioFASEA);

            int gruppi = Utility.numberGroupsModello4(p);
            List<Lezioni_Modelli> lezioniB = m4.getLezioni();
            List<LezioneCalendario> grouppedByLezioneB = Utility.grouppedByLezione(lezioniCalendarioFASEB);
            DateTime oggi = new DateTime().withMillisOfDay(0);
            List<Registro_completo> rc = Action.registro_modello6(idpr);
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
    <body class="kt-header--fixed kt-header-mobile--fixed kt-subheader--fixed kt-subheader--enabled kt-subheader--solid kt-aside--enabled kt-aside--fixed">
        <!-- begin:: Page -->
        <div class="kt-grid kt-grid--hor kt-grid--root">
            <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--ver kt-page">
                <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">
                    <div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
                        <div class="kt-portlet kt-portlet--mobile">
                            <div class="kt-portlet__body">
                                <div class="kt-section kt-section--space-md">
                                    <div class="form-group form-group-sm row">
                                        <div class="col-12">
                                            <div class="row">
                                                <h5>Registro Lezioni - Modalità svolgimento <u>IN PRESENZA</u>: Lezioni programmate</h5>
                                            </div>
                                            <table class="table table-bordered" style="width: 100%;" >
                                                <thead>
                                                <th>FASE</th>
                                                <th>#</th>
                                                <th>GIORNO</th>
                                                <th>ORA INIZIO</th>
                                                <th>ORA FINE</th>
                                                <th>GRUPPO</th>
                                                <th>AZIONI</th>
                                                </thead>
                                                <%
                                                    for (LezioneCalendario lez : grouppedByLezioneA) {
                                                        Lezioni_Modelli tempA = Utility.lezioneFiltered(lezioniA, lez.getId());
                                                        if (tempA != null) {

                                                            Registro_completo rc1 = rc.stream().filter(rr1 -> rr1.getData().equals(new DateTime(tempA.getGiorno()))).findAny().orElse(null);

                                                            String operazioni = rc1 != null
                                                                    ? "<a href='registroaula_edit.jsp?idpr=" + idpr + "&data=" + new DateTime(tempA.getGiorno()).toString("yyyyMMdd") + "&giorno=" + lez.getLezione() + "&fase=A' "
                                                                    + "class='btn btn-primary btn-icon btn-circle btn-sm' data-container='body' data-html='true' data-toggle='kt-tooltip'"
                                                                    + "title='Visualizza/Modifica Registro'><i class='fa fa-edit'></i></a>"
                                                                    : "<a href='registroaula_edit.jsp?idpr=" + idpr + "&data=" + new DateTime(tempA.getGiorno()).toString("yyyyMMdd") + "&giorno=" + lez.getLezione() + "&fase=A' "
                                                                    + "class='btn btn-danger btn-icon btn-circle btn-sm' data-container='body' data-html='true' data-toggle='kt-tooltip'"
                                                                    + "title='Carica Registro'><i class='fa fa-upload'></i></a>";

                                                            boolean passato = new DateTime(tempA.getGiorno()).isBefore(oggi);
                                                            String azioni = passato
                                                                    ? operazioni
                                                                    : "-";
                                                %>
                                                <tr>
                                                    <td>A</td>
                                                    <td><%=lez.getLezione()%></td>
                                                    <td><%=new DateTime(tempA.getGiorno()).toString("dd/MM/yyyy")%></td>
                                                    <td><%=tempA.getOrainizio()%></td>
                                                    <td><%=tempA.getOrafine()%> </td>
                                                    <td><%=tempA.getGruppo_faseB() + 1%> </td>
                                                    <td><%=azioni%></td>
                                                </tr>
                                                <%}
                                                    }%>

                                                <%
                                                    for (int i = 1; i <= gruppi; i++) {
                                                        for (LezioneCalendario lez : grouppedByLezioneB) {
                                                            Lezioni_Modelli tempB = Utility.lezioneFilteredByGroup(lezioniB, lez.getId(), i);
                                                            if (tempB != null) {

                                                                Registro_completo rc1 = rc.stream().filter(rr1 -> rr1.getData()
                                                                        .equals(new DateTime(tempB.getGiorno()))).findAny().orElse(null);

                                                                String operazioni = rc1 != null
                                                                        ? "<a href='registroaula_edit.jsp?idpr=" + idpr + "&data=" + new DateTime(tempB.getGiorno()).toString("yyyyMMdd") + "&gruppo=" + i + "&giorno=" + lez.getLezione() + "&fase=B' "
                                                                        + "class='btn btn-primary btn-icon btn-circle btn-sm' data-container='body' data-html='true' data-toggle='kt-tooltip'"
                                                                        + "title='Visualizza/Modifica Registro'><i class='fa fa-edit'></i></a>"
                                                                        : "<a href='registroaula_edit.jsp?idpr=" + idpr + "&data=" + new DateTime(tempB.getGiorno()).toString("yyyyMMdd") + "&gruppo=" + i + "&giorno=" + lez.getLezione() + "&fase=B' ' "
                                                                        + "class='btn btn-danger btn-icon btn-circle btn-sm' data-container='body' data-html='true' data-toggle='kt-tooltip'"
                                                                        + "title='Carica Registro'><i class='fa fa-upload'></i></a>";

                                                                boolean passato = new DateTime(tempB.getGiorno()).isBefore(oggi);
                                                                String azioni = passato
                                                                        ? operazioni
                                                                        : "-";
                                                %>
                                                <tr>
                                                    <td>B</td>
                                                    <td><%=lez.getLezione()%></td>
                                                    <td><%=new DateTime(tempB.getGiorno()).toString("dd/MM/yyyy")%></td>
                                                    <td><%=tempB.getOrainizio()%></td>
                                                    <td><%=tempB.getOrafine()%> </td>
                                                    <td><%=tempB.getGruppo_faseB()%> </td>
                                                    <td><%=azioni%></td>
                                                </tr>
                                                <%}
                                                        }
                                                    }%>
                                            </table>
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
<script src="<%=src%>/page/mc/js/control.js<%=new Date().getTime()%>" type="text/javascript"></script>
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
</script>
</body>
</html>
<%}
    }%>
