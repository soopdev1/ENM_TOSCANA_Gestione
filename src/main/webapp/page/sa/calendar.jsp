<%@page import="rc.so.domain.Presenze_Lezioni_Allievi"%>
<%@page import="java.util.ArrayList"%>
<%@page import="rc.so.domain.Presenze_Lezioni"%>
<%@page import="rc.so.domain.Allievi"%>
<%@page import="rc.so.domain.Docenti"%>
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
            String src = session.getAttribute("src").toString();
            Entity e = new Entity();
            Lezioni_Modelli lm = e.getEm().find(Lezioni_Modelli.class, Long.parseLong(request.getParameter("idcalendar")));

            ProgettiFormativi p = lm.getModello().getProgetto();

            //  String idsedefisica = p.getSedefisica() != null ? String.valueOf(p.getSedefisica().getId()) : "";
            Presenze_Lezioni pl1 = e.getPresenzeLezione(lm);
            List<Presenze_Lezioni_Allievi> pa1 = new ArrayList<>();
            boolean modify = (pl1 == null);

            if (pl1 != null) {
                pa1 = e.getpresenzelezioniGiornata(pl1);
            }

            e.close();
%>
<html>
    <head>
        <meta charset="utf-8" />
        <title>YES I Start Up - Toscana - Registro presenze</title>
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
        <link href="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/select2/dist/css/select2.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/bootstrap-select/dist/css/bootstrap-select.css" rel="stylesheet" type="text/css" />
        <!-- - -->
        <link href="<%=src%>/assets/vendors/general/perfect-scrollbar/css/perfect-scrollbar.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/owl.carousel/dist/assets/owl.carousel.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/owl.carousel/dist/assets/owl.theme.default.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/line-awesome/css/line-awesome.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/flaticon/flaticon.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/flaticon2/flaticon.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/fontawesome5/css/all.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/bootstrap-timepicker/css/bootstrap-timepicker.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/resource/animate.css" rel="stylesheet" type="text/css"/>
        <link href="<%=src%>/assets/demo/default/skins/header/base/light.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/skins/header/menu/light.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/skins/brand/light.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/skins/aside/light.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/base/style.bundle.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/raf/jquery-confirm.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/resource/custom.css" rel="stylesheet" type="text/css" />

        <link rel="shortcut icon" href="<%=src%>/assets/media/logos/favicon.ico" />
        <style type="text/css">
            .form-group {
                margin-bottom: 1rem;
            }

            .custom-file-label::after {
                color:#fff;
                background-color: #eaa21c;
            }

            .kt-radio > span::after {
                border: solid #363a90;
                background: #363a90;
            }

            .kt-radio > input:checked ~ span {
                border: 1px solid #363a90;
            }

            a.disablelink > i {
                color: #7c7fb7!important;
            }

            .datepicker table tr td.highlighted.disabled, .datepicker table tr td.highlighted.disabled:active {
                background: #d9edf7;
                color: #d08902 !important;
            }

            .datepicker tbody tr > td.day {
                background: #ebedf2;
                color: #1d32a6 !important;
            }

            .datepicker tbody tr > td.day.active, .datepicker tbody tr > td.day.active {
                background: #363a90;
                color: #ffffff !important;
            }

            .datepicker table tr td.disabled, .datepicker table tr td.disabled {
                background: none;
                color: #777;
                cursor: default;
                color: #756c6e !important;
            }
            .select2-container--open {
                z-index: 999999999;
            }
        </style>
    </head>
    <body class="kt-header--fixed kt-header-mobile--fixed kt-subheader--fixed kt-subheader--enabled kt-subheader--solid kt-aside--enabled kt-aside--fixed">
        <!-- begin:: Page -->
        <div class="kt-grid kt-grid--hor kt-grid--root">
            <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--ver kt-page">
                <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">
                    <div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
                        <div class="kt-portlet kt-portlet--mobile">
                            <div class="kt-portlet__head" style="display: none;">
                                <div class="kt-portlet__head-label">
                                    <h3 class="kt-portlet__head-title">
                                        Crea:
                                    </h3>
                                </div>
                                <input type="hidden" id="pId" name="pId" value="<%=p.getId()%>">
                            </div>

                            <div class="kt-portlet__body">
                                <div class="kt-section kt-section--space-md">
                                    <div class="form-group form-group-sm row">
                                        <div class="col-12">
                                            <div class="row">
                                                <div class="col-12">
                                                    <h5><a data-container="body" data-html="true" data-toggle="kt-tooltip" title="TORNA INDIETRO" 
                                                           href="modello3.jsp?id=<%=p.getId()%>" class='btn-icon kt-font-io'><i class="fa fa-arrow-left"></i></a> REGISTRO PRESENZE LEZIONE <%=Utility.sdfITA.format(lm.getGiorno())%> - <%=lm.getLezione_calendario().getUnitadidattica().getCodice()%> 
                                                        (<%=lm.getLezione_calendario().getUnitadidattica().getDescrizione()%>) </h5>
                                                        <%if (modify) {%>
                                                    <form action="<%=request.getContextPath()%>/OperazioniSA" method="POST" target="_blank">
                                                        <input type="hidden" name="type" value="SCARICAREGISTROCARTACEOBASE" />
                                                        <input type="hidden" name="idcalendar" value="<%=lm.getId()%>" />
                                                        <button type="submit" class='btn-icon kt-font-io'><small><i class="fa fa-file-download"></i> SCARICA REGISTRO CARTACEO DA COMPILARE</small></button>
                                                    </form>
                                                    <%}%>
                                                </div>                                               
                                            </div>
                                            <div class="kt-separator kt-separator--border kt-separator--space-xs"></div>
                                            <%
                                                String esito = (String) session.getAttribute("esito3");
                                                if (esito != null && !esito.trim().equals("") && !esito.trim().equals("OK")) {%>
                                            <div class="form-row">
                                                <div class="col-12">
                                                    <div class="alert alert-danger">
                                                        <%=esito%>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="kt-separator kt-separator--border kt-separator--space-xs"></div>
                                            <%}%>
                                            <form action="<%=request.getContextPath()%>/OperazioniSA" method="POST" enctype="multipart/form-data">
                                                <input type="hidden" name="type" value="SALVAPRESENZELEZIONE" />
                                                <input type="hidden" name="idpr" value="<%=p.getId()%>" />
                                                <input type="hidden" name="idcalendar" value="<%=lm.getId()%>" />
                                                <input type="hidden" id="orastandardlezione" value="<%=lm.getLezione_calendario().getOre()%>"/>
                                                <div class="form-row">
                                                    <label class="col-md-2 col-form-label fw-bold fs-6">
                                                        <span class="text-danger"><b>ORA INIZIO</b></span>
                                                    </label>
                                                    <div class="col-lg-4">
                                                        <%if (modify) {%>
                                                        <div class="dropdown bootstrap-select form-control kt-" id="orai_div" style="padding: 0;height: 35px;">
                                                            <select class="form-control kt-select2-general" id="orai" name="orai" 
                                                                    style="width: 100%" required onchange="return populatedatafine(this);">
                                                                <option value="<%=lm.getOrainizio()%>" selected><%=lm.getOrainizio()%></option>
                                                                <option value="08:00">08:00</option>
                                                                <option value="08:30">08:30</option>
                                                                <option value="09:00">09:00</option>
                                                                <option value="09:30">09:30</option>
                                                                <option value="10:00">10:00</option>
                                                                <option value="10:30">10:30</option>
                                                                <option value="11:00">11:00</option>
                                                                <option value="11:30">11:30</option>
                                                                <option value="12:00">12:00</option>
                                                                <option value="12:30">12:30</option>
                                                                <option value="13:00">13:00</option>
                                                                <option value="13:30">13:30</option>
                                                                <option value="14:00">14:00</option>
                                                                <option value="14:30">14:30</option>
                                                                <option value="15:00">15:00</option>
                                                                <option value="15:30">15:30</option>
                                                                <option value="16:00">16:00</option>
                                                                <option value="16:30">16:30</option>
                                                                <option value="17:00">17:00</option>
                                                                <option value="17:30">17:30</option>
                                                                <option value="18:00">18:00</option>
                                                                <option value="18:30">18:30</option>
                                                                <option value="19:00">19:00</option>
                                                                <option value="19:30">19:30</option>
                                                                <option value="20:00">20:00</option>
                                                            </select>
                                                        </div>    
                                                        <%} else {%>
                                                        <label class="col-md-4 col-form-label fw-bold fs-6">
                                                            <span><b><%=pl1.getOrainizio()%></b></span>
                                                        </label>
                                                        <%}%>
                                                    </div>    
                                                    <label class="col-md-2 col-form-label fw-bold fs-6">
                                                        <span class="text-danger"><b>ORA FINE</b></span>
                                                    </label>
                                                    <div class="col-lg-4">
                                                        <%if (modify) {%>
                                                        <div class="dropdown bootstrap-select form-control kt-" id="oraf_div" style="padding: 0;height: 35px;">
                                                            <select class="form-control kt-select2-general" id="oraf" name="oraf" data-placeholder="Ora Fine" 
                                                                    style="width: 100%" required >
                                                                <option value="<%=lm.getOrafine()%>" selected><%=lm.getOrafine()%></option>
                                                            </select>                                    
                                                        </div> 
                                                        <%} else {%>
                                                        <label class="col-md-4 col-form-label fw-bold fs-6">
                                                            <span><b><%=pl1.getOrafine()%></b></span>
                                                        </label>
                                                        <%}%>
                                                    </div> 


                                                </div>
                                                <div class="form-row"><hr></div>
                                                <div class="form-row">
                                                    <label class="col-md-2 col-form-label fw-bold fs-6">
                                                        <span class="text-danger"><b>DOCENTE</b></span>
                                                    </label>
                                                    <div class="col-lg-4">
                                                        <%if (modify) {%>
                                                        <div class="dropdown bootstrap-select form-control kt-" id="docente_div" style="padding: 0;height: 35px;">
                                                            <select class="form-control kt-select2-general" id="docente" name="docente" data-placeholder="Ora Fine" 
                                                                    style="width: 100%" required >
                                                                <option selected value="<%=lm.getDocente().getId()%>"><%=lm.getDocente().getCognome()%> <%=lm.getDocente().getNome()%> - <%=lm.getDocente().getCodicefiscale()%></option>
                                                            </select>                                    
                                                        </div> 
                                                        <%} else {%>
                                                        <label class="col-md-10 col-form-label fw-bold fs-6">
                                                            <span><b><%=pl1.getDocente().getCognome()%> <%=pl1.getDocente().getNome()%> - <%=pl1.getDocente().getCodicefiscale()%></b></span>
                                                        </label>
                                                        <%}%>
                                                    </div>
                                                </div>
                                                <div class="form-row"><hr></div>
                                                <div class="form-row">
                                                    <label class="col-md-2 col-form-label fw-bold fs-6">
                                                        <span class="text-danger"><b>ALLIEVI</b></span>
                                                    </label>
                                                    <div class="col-lg-10">
                                                        <table class="table table-hover table-row-bordered">
                                                            <thead>
                                                                <tr>
                                                                    <th>Cognome e Nome</th>
                                                                    <th>Codice Fiscale</th>
                                                                    <th>Presenza SI/NO</th>
                                                                    <th>Orario Ingresso/Uscita</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <%for (Allievi a1 : Utility.estraiAllieviOK(p)) {

                                                                        boolean modifyallievo = false;
                                                                        String allievo_presente = "0";
                                                                        String allievo_orai = "";
                                                                        String allievo_oraf = "";

                                                                        for (Presenze_Lezioni_Allievi pla : pa1) {
                                                                            if (pla.getAllievo().getId().equals(a1.getId())) {
                                                                                modifyallievo = true;
                                                                                allievo_presente = pla.getDurata() > 0 ? "1" : "0";
                                                                                allievo_orai = pla.getOrainizio();
                                                                                allievo_oraf = pla.getOrafine();
                                                                            }
                                                                        }


                                                                %>
                                                            <input type="hidden" id="modify_<%=a1.getId()%>" name="modify_<%=a1.getId()%>" value="<%=modifyallievo%>" />
                                                            <input type="hidden" id="startoi_<%=a1.getId()%>" value="<%=allievo_orai%>" />
                                                            <input type="hidden" id="startof_<%=a1.getId()%>" value="<%=allievo_oraf%>" />
                                                            <tr>
                                                                <td>
                                                                    <%=a1.getCognome()%> <%=a1.getNome()%>
                                                                </td>
                                                                <td>
                                                                    <%=a1.getCodicefiscale()%>
                                                                </td>
                                                                <td>
                                                                    <%if (modify) {
                                                                    %>
                                                                    <select class="form-control kt-select2-general" id="sino_<%=a1.getId()%>" name="sino_<%=a1.getId()%>"
                                                                            style="width: 100%" required onchange="return checkorariomax();">

                                                                        <%if (allievo_presente.equals("1")) {%>
                                                                        <option value="1" selected>SI</option>
                                                                        <option value="0">NO</option>
                                                                        <%} else {%>
                                                                        <option value="1">SI</option>
                                                                        <option value="0" selected>NO</option>
                                                                        <%}%>


                                                                    </select>
                                                                    <%} else {%>
                                                                    <span class="text-dark"><b><%=allievo_presente.equals("1") ? "SI" : "NO"%></b></span>
                                                                            <%}%>
                                                                </td>
                                                                <td>
                                                                    <div class="row col-md-12">
                                                                        <div class="col-md-6">
                                                                            <%if (modify) {%>
                                                                            <select class="form-control kt-select2-general sel-presenza" id="orai_<%=a1.getId()%>" name="orai_<%=a1.getId()%>"
                                                                                    style="width: 100%" required onchange="return checkorariomax();">
                                                                                <option value="<%=lm.getOrainizio()%>" selected><%=lm.getOrainizio()%></option>
                                                                            </select>
                                                                            <%} else if (allievo_presente.equals("1")) {%>
                                                                            <span class="text-dark"><b><%=allievo_orai%></b></span>
                                                                                    <%} else {%>
                                                                            <span class="text-dark"></span>
                                                                            <%}%>
                                                                        </div>
                                                                        <div class="col-md-6">
                                                                            <%if (modify) {%>
                                                                            <select class="form-control kt-select2-general sel-presenza" id="oraf_<%=a1.getId()%>" name="oraf_<%=a1.getId()%>"
                                                                                    style="width: 100%" required>
                                                                                <option value="<%=lm.getOrafine()%>" selected ><%=lm.getOrafine()%></option>                                        
                                                                            </select>
                                                                            <%} else if (allievo_presente.equals("1")) {%>
                                                                            <span class="text-dark"><b><%=allievo_oraf%></b></span>
                                                                                    <%} else {%>
                                                                            <span class="text-dark"></span>
                                                                            <%}%>
                                                                        </div>
                                                                </td>
                                                            </tr>
                                                            <%}%>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <%if (modify) {%>
                                                <div class="form-group col-xl-4 col-lg-6">
                                                    <label>REGISTRO PRESENZE FIRMATO DIGITALMENTE</label>
                                                    <div class="custom-file">
                                                        <input type="file" tipo='obbligatory'
                                                               class="custom-file-input" 
                                                               accept="application/pkcs7-mime,application/pdf" name="registrofirmato" id="registrofirmato"
                                                               onchange="return checkFileExtAndDim('pdf,p7m');" required>
                                                        <label class="custom-file-label selected" name="label_registrofirmato">Scegli File</label>
                                                    </div>
                                                </div>
                                                <button type="submit" class="btn btn-io"><i class="fa fa-save"></i> SALVA DATI</button>
                                            </form>
                                            <%} else {%>
                                            </form>

                                            <form action="<%=request.getContextPath()%>/OperazioniSA" method="POST" target="_blank">
                                                <input type="hidden" name="type" value="SCARICAREGISTROCARTACEO" />
                                                <input type="hidden" name="idpresenza" value="<%=pl1.getIdpresenzelezioni()%>" />
                                                <button type="submit" class='btn btn-success'><small><i class="fa fa-file-download"></i> SCARICA REGISTRO FIRMATO DIGITALMENTE</small></button>
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
</div>
</div>
<div id="kt_scrolltop" class="kt-scrolltop">
    <i class="fa fa-arrow-up"></i>
</div>
<script src="<%=src%>/assets/vendors/general/jquery/dist/jquery.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/popper.js/dist/umd/popper.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/tooltip.js/dist/umd/tooltip.min.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/sticky-js/dist/sticky.min.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/bootstrap/dist/js/bootstrap.min.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/js-cookie/src/js.cookie.js" type="text/javascript"></script>
<script src="<%=src%>/assets/soop/js/moment.min.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/perfect-scrollbar/dist/perfect-scrollbar.js" type="text/javascript"></script>
<script src="<%=src%>/assets/demo/default/base/scripts.bundle.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/jquery-form/dist/jquery.form.min.js" type="text/javascript"></script>
<script src="<%=src%>/assets/app/custom/general/components/extended/blockui1.33.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.js" type="text/javascript"></script>
<script src="<%=src%>/assets/soop/js/utility.js" type="text/javascript"></script>
<script src="<%=src%>/assets/app/bundle/app.bundle.js" type="text/javascript"></script>
<!--this page -->
<script src="<%=src%>/assets/vendors/general/select2/dist/js/select2.full.js" type="text/javascript"></script>
<script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/select2.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/bootstrap-select/dist/js/bootstrap-select.js" type="text/javascript"></script>
<script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/bootstrap-datepicker.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/js/bootstrap-datepicker.js" type="text/javascript"></script>
<script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/bootstrap-timepicker.js" type="text/javascript"></script>
<script src="<%=src%>/assets/vendors/general/bootstrap-timepicker/js/bootstrap-timepicker.js" type="text/javascript"></script>
<script src="<%=src%>/assets/raf/jquery-confirm.min.js" type="text/javascript"></script>
<input type="hidden" id="systemtype" value="<%=Utility.iswindows()%>" />
<script id="calendarjs" defer src="<%=src%>/page/sa/js/calendar.js" data-context="<%=request.getContextPath()%>" type="text/javascript"></script>
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
