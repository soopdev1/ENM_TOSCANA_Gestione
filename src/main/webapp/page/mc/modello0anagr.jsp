<%-- 
    Document   : uploadDocumet
    Created on : 29-gen-2020, 12.39.45
    Author     : agodino
--%>

<%@page import="rc.so.domain.MotivazioneNO"%>
<%@page import="rc.so.domain.SoggettiAttuatori"%>
<%@page import="rc.so.domain.MaturazioneIdea"%>
<%@page import="rc.so.domain.Aspettative"%>
<%@page import="rc.so.domain.Motivazione"%>
<%@page import="rc.so.domain.Canale"%>
<%@page import="rc.so.domain.Documenti_Allievi"%>
<%@page import="rc.so.domain.TipoDoc_Allievi"%>
<%@page import="rc.so.domain.Allievi"%>
<%@page import="rc.so.domain.TipoDoc_Allievi_Pregresso"%>
<%@page import="rc.so.domain.Documenti_Allievi_Pregresso"%>
<%@page import="rc.so.domain.Allievi_Pregresso"%>
<%@page import="java.util.Date"%>
<%@page import="rc.so.util.Utility"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.ArrayList"%>
<%@page import="rc.so.domain.DocumentiPrg"%>
<%@page import="rc.so.domain.TipoDoc"%>
<%@page import="java.util.List"%>
<%@page import="rc.so.db.Entity"%>
<%@page import="rc.so.db.Action"%>
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
            Allievi a = e.getEm().find(Allievi.class, Long.parseLong(request.getParameter("id")));
            String cittadinanza = e.nazionenascita(a.getStato_nascita()).getNome();
            e.close();
%>
<html>
    <head>
        <meta charset="utf-8" />
        <title>YES I Start Up - Toscana - Assegna Operatore ENM</title>
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
        <link href="<%=src%>/assets/vendors/general/select2/dist/css/select2.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/owl.carousel/dist/assets/owl.carousel.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/owl.carousel/dist/assets/owl.theme.default.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/socicon/css/socicon.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/line-awesome/css/line-awesome.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/flaticon/flaticon.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/flaticon2/flaticon.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/custom/vendors/fontawesome5/css/all.min.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/base/style.bundle.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/resource/custom.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/skins/header/base/light.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/skins/header/menu/light.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/skins/brand/light.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/demo/default/skins/aside/light.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/resource/animate.css" rel="stylesheet" type="text/css"/>
        <link rel="shortcut icon" href="<%=src%>/assets/media/logos/favicon.ico" />
        <!--this page-->
        <link href="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
        <!--fancy-->
        <link href="<%=src%>/assets/soop/css/jquery.fancybox.min.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="<%=src%>/assets/soop/js/jquery-3.6.1.min.js"></script>
        <script type="text/javascript" src="<%=src%>/assets/soop/js/jquery.fancybox.min.js"></script>
        <script type="text/javascript" src="<%=src%>/assets/soop/js/fancy.js"></script>
    </head>
    <body class="kt-header--fixed kt-header-mobile--fixed kt-subheader--fixed kt-subheader--enabled kt-subheader--solid kt-aside--enabled kt-aside--fixed">
        <div class="kt-grid kt-grid--hor kt-grid--root">
            <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--ver kt-page">
                <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">
                    <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">
                        <div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
                            <div class="kt-portlet kt-portlet--mobile">


                                <div class="kt-portlet__head">
                                    <div class="kt-portlet__head-label">
                                        <h3 class="kt-portlet__head-title">
                                            Modello 0 - Pre-Iscrizione Allievo: <b><%=a.getCognome()%> <%=a.getNome()%> (<%=a.getCodicefiscale()%>)</b>
                                        </h3>
                                    </div>
                                </div>
                                <div class="kt-portlet__body">
                                    <div class="row col-md-12">
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Cognome</label>
                                            <label><%=a.getCognome()%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Nome</label>
                                            <label><%=a.getNome()%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Data di Nascita</label>
                                            <label><%=Utility.sdfITA.format(a.getDatanascita())%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Codice Fiscale</label>
                                            <label><%=a.getCodicefiscale()%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Sesso</label>
                                            <label><%=a.getSesso()%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Comune Nascita</label>
                                            <label><%=a.getComune_nascita().getNome()%> (<%=a.getComune_nascita().getProvincia()%>) - <%=a.getComune_nascita().getRegione()%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Cittadinanza</label>
                                            <label><%=cittadinanza%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Telefono</label>
                                            <label><%=a.getTelefono()%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Email</label>
                                            <label><%=a.getEmail()%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Data Iscrizione</label>
                                            <label><%=Utility.sdfITA.format(a.getIscrizionegg())%></label>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label class="kt-font-danger kt-font-boldest">Indirizzo Residenza</label>
                                            <label><%=a.getIndirizzoresidenza()%> - <%=a.getComune_residenza().getNome()%> (<%=a.getComune_residenza().getProvincia()%>) - <%=a.getComune_residenza().getRegione()%></label>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label class="kt-font-danger kt-font-boldest">Gruppo vulnerabilità</label>
                                            <label><%=a.getTos_gruppovulnerabile().getDescrizione()%></label>
                                        </div>
                                        <div class="form-group col-md-6">
                                            <label class="kt-font-danger kt-font-boldest">Titolo di studio</label>
                                            <label><%=a.getTitoloStudio().getDescrizione()%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">CPI</label>
                                            <label><%=a.getCpi().getDescrizione()%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Data Iscrizione CPI</label>
                                            <label><%=Utility.sdfITA.format(a.getDatacpi())%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Condizione professionale</label>
                                            <label><%=a.getCondizione_mercato().getDescrizione()%></label>
                                        </div>
                                        <div class="form-group col-md-3">
                                            <label class="kt-font-danger kt-font-boldest">Tipo Finanziamento (GOL/PATTO)</label>
                                            <label><%=a.getTos_tipofinanziamento()%></label>
                                        </div>
                                        
                                    </div>
                                    <div class="row col-md-12">
                                        <hr>
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
        <script src="<%=src%>/assets/vendors/general/perfect-scrollbar/dist/perfect-scrollbar.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/sticky-js/dist/sticky.min.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/demo/default/base/scripts.bundle.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/jquery-form/dist/jquery.form.min.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/jquery-validation/dist/jquery.validate.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/jquery-validation/dist/additional-methods.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/general/components/extended/blockui1.33.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/soop/js/utility.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/bundle/app.bundle.js" type="text/javascript"></script>
        <!--this page-->
        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/bootstrap-datepicker.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/js/bootstrap-datepicker.js" type="text/javascript"></script>
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
            function changesino() {

                try {
                    var sino = $('#tos_m0_volonta').val();
                    if (sino === "1") {
                        document.getElementById("div_volontasi").style.display = "";
                        document.getElementById("div_volontano").style.display = "none";
                    } else {
                        document.getElementById("div_volontasi").style.display = "none";
                        document.getElementById("div_volontano").style.display = "";

                    }
                } catch (e) {
                    console.error(e);
                }




            }
            function changealtro() {

                try {
                    var noper = $('#tos_m0_noperche').val();
                    if (noper === "7") {
                        document.getElementById("div_altrospec").style.display = "";
                    } else {
                        document.getElementById("div_altrospec").style.display = "none";
                    }
                } catch (e) {
                    console.error(e);
                }
            }

            jQuery(document).ready(function () {
                changesino();
                changealtro();
            });
        </script>
    </body>
</html>
<%}
    }%>