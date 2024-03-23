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
            Documenti_Allievi presentemod0 = e.getModello0Allievo(a);
            List<Canale> canalecon = e.findAll(Canale.class);
            List<Motivazione> motiv = e.findAll(Motivazione.class);
            List<Aspettative> aspettat = e.findAll(Aspettative.class);
            List<MaturazioneIdea> matidea = e.findAll(MaturazioneIdea.class);
            List<MotivazioneNO> motivno = e.findAll(MotivazioneNO.class);
            List<SoggettiAttuatori> salist = e.getSoggettiAttuatori("", "", "", "", "", "", "");

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
                                <%if (presentemod0 == null) {%>
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
                                    </div>
                                    <div class="row col-md-12">
                                        <hr>
                                    </div>
                                    <div class="row col-md-12">
                                        <h4>COLLOQUIO-INTERVISTA</h4>
                                    </div>

                                    <form method="POST" action="<%=request.getContextPath()%>/OperazioniMicro">
                                        <input type="hidden" name="type" value="salvamodello0" />
                                        <input type="hidden" name="idallievo" value="<%=a.getId()%>" />

                                        <div class="row col-md-12">
                                            <div class="form-group col-md-3">
                                                <label class="kt-font-danger kt-font-boldest">DATA COLLOQUIO</label>
                                                <label><%=Utility.sdfITA.format(new Date())%></label>
                                            </div>
                                            <div class="form-group col-md-3">
                                                <label class="kt-font-danger kt-font-boldest">SIGLA OPERATORE ENM</label>
                                                <label><%=us.getSiglaenm()%></label>
                                            </div>
                                        </div> 
                                        <div class="row col-md-12">
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">Modalità di svolgimento del colloquio: </label>
                                                <select class="form-control kt-select2-general" id="tos_m0_modalitacolloquio" name="tos_m0_modalitacolloquio"  style="width: 100%" required>
                                                    <option value="">Seleziona Risposta</option>
                                                    <option value="1">IN PRESENZA</option>
                                                    <option value="2">TELEFONICO</option>
                                                </select>
                                            </div>
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">Come definirebbe il suo grado di conoscenza delle finalità e dei contenuti del percorso formativo YISU?</label>
                                                <select class="form-control kt-select2-general" id="tos_m0_gradoconoscenza" name="tos_m0_gradoconoscenza"  style="width: 100%" required>
                                                    <option value="">Seleziona Risposta</option>
                                                    <option value="1">ALTO</option>
                                                    <option value="2">MEDIO</option>
                                                    <option value="3">SCARSO</option>
                                                    <option value="4">NULLO</option>
                                                </select>
                                            </div>
                                        </div> 
                                        <div class="row col-md-12">
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">Attraverso quale principale canale è venuto a conoscenza del progetto YISU Toscana?</label>
                                                <select class="form-control kt-select2-general" id="tos_m0_canaleconoscenza" name="tos_m0_canaleconoscenza"  style="width: 100%" required>
                                                    <option value="">Seleziona Risposta</option>
                                                    <%for (Canale c1 : canalecon) {%>
                                                    <option value="<%=c1.getId()%>"><%=c1.getDescrizione()%></option>
                                                    <%}%>
                                                </select>
                                            </div>
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">Per quale principale motivazione ha scelto di frequentare YISU-Toscana?</label>
                                                <select class="form-control kt-select2-general" id="tos_m0_motivazione" name="tos_m0_motivazione"  style="width: 100%"required>
                                                    <option value="">Seleziona Risposta</option>
                                                    <%for (Motivazione c1 : motiv) {%>
                                                    <option value="<%=c1.getId()%>"><%=c1.getDescrizione()%></option>
                                                    <%}%>
                                                </select>
                                            </div>
                                        </div> 
                                        <div class="row col-md-12">
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">Quanto ritiene possa essere utile il percorso YISU per il lavoro che vorrebbe svolgere?</label>
                                                <select class="form-control kt-select2-general" id="tos_m0_utilita" name="tos_m0_utilita"  style="width: 100%" required>
                                                    <option value="">Seleziona Risposta</option>
                                                    <option value="1">PER NULLA UTILE</option>
                                                    <option value="2">POCO UTILE</option>
                                                    <option value="3">UTILE</option>
                                                    <option value="4">ABBASTANZA UTILE</option>
                                                    <option value="5">MOLTO UTILE</option>
                                                </select>
                                            </div>
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">Cosa si aspetta soprattutto frequentando YES I START UP?</label>
                                                <select class="form-control kt-select2-general" id="tos_m0_aspettative" name="tos_m0_aspettative"  style="width: 100%" required>
                                                    <option value="">Seleziona Risposta</option>
                                                    <%for (Aspettative c1 : aspettat) {%>
                                                    <option value="<%=c1.getId()%>"><%=c1.getDescrizione()%></option>
                                                    <%}%>
                                                </select>
                                            </div>
                                        </div> 
                                        <div class="row col-md-12">
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">Quanto è matura l’idea di impresa/attività che potrà realizzare a seguito del percorso formativo?</label>
                                                <select class="form-control kt-select2-general" id="tos_m0_maturazione" name="tos_m0_maturazione"  style="width: 100%" required>
                                                    <option value="">Seleziona Risposta</option>
                                                    <%for (MaturazioneIdea c1 : matidea) {%>
                                                    <option value="<%=c1.getId()%>"><%=c1.getDescrizione()%></option>
                                                    <%}%>
                                                </select>
                                            </div>
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">Indirizzo email (da confermare)</label>
                                                <input class="form-control" name="tos_mail" id="tos_mail" value="<%=a.getEmail()%>" />
                                            </div>
                                        </div>
                                        <hr>
                                        <div class="row col-md-12">
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">Conferma la sua volontà di frequentare il percorso YISU?</label>
                                                <select class="form-control kt-select2-general" id="tos_m0_volonta" name="tos_m0_volonta"  style="width: 100%" onchange="return changesino();" required>
                                                    <option value="">Seleziona Risposta</option>
                                                    <option value="1">SI</option>
                                                    <option value="0">NO</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="row col-md-12" id="div_volontasi">
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">Con quale Soggetto Esecutore vorrebbe realizzare il percorso?</label>
                                                <select class="form-control kt-select2-general" id="soggetto" name="soggetto"  style="width: 100%">
                                                    <%for (SoggettiAttuatori c1 : salist) {%>
                                                    <option value="<%=c1.getId()%>"><%=c1.getRagionesociale()%> <%=e.getProvinceSediFormazione(c1)%></option>
                                                    <%}%>
                                                </select>
                                            </div>
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">È consapevole che la mancata partecipazione alle giornate formative (1 o 2 gg di assenza) senza giustificato motivo può determinare la perdita e/o la decurtazione delle indennità eventualmente percepite?</label>
                                                <select class="form-control kt-select2-general" id="tos_m0_consapevole" name="tos_m0_consapevole"  style="width: 100%">
                                                    <option value="0">Inapplicabile, non percepisco alcuna indennità</option>
                                                    <option value="1">SI</option>
                                                    <option value="2">NO</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="row col-md-12" id="div_volontano">
                                            <div class="form-group col-md-6">
                                                <label class="kt-font-info kt-font-boldest">Se NO, Perché?</label>
                                                <select class="form-control kt-select2-general" id="tos_m0_noperche" name="tos_m0_noperche"  style="width: 100%" onchange="return changealtro();">
                                                    <%for (MotivazioneNO c1 : motivno) {%>
                                                    <option value="<%=c1.getId()%>"><%=c1.getDescrizione()%></option>
                                                    <%}%>
                                                </select>
                                            </div>
                                            <div class="form-group col-md-6" id="div_altrospec">
                                                <label class="kt-font-info kt-font-boldest">Specificare Altro:</label>
                                                <input class="form-control" name="tos_m0_noperchealtro" id="tos_m0_noperchealtro" autocomplete="off">
                                            </div>
                                        </div>
                                        <div class="row col-md-12">
                                            <label class="kt-font-info kt-font-boldest">Note ENM</label>
                                            <textarea class="form-control" name="tos_m0_note" id="tos_m0_note" autocomplete="off"></textarea>
                                        </div>



                                        <div class="kt-portlet__foot" style="padding-left: 10px;">
                                            <div class="kt-form__actions">
                                                <div class="form-group col-xl-3 col-lg-6">
                                                    <button type="submit" class="btn btn-io" 
                                                            style="font-family: Poppins"><i class="fa fa-save"></i> SALVA DATI</button>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <%} else {%>
                                <div class="kt-portlet__foot" style="padding-left: 10px;">
                                    <div class="kt-form__actions row col-md-12">
                                        <div class="form-group col-md-3">
                                            <form action="<%=request.getContextPath()%>/OperazioniGeneral" method="POST" target="_blank">

                                                <input type="hidden" name="type" value="onlyDownload" />
                                                <input type="hidden" name="path" value="<%=presentemod0.getPath()%>" />
                                                <button type="submit" class="btn btn-success" 
                                                        style="font-family: Poppins"><i class="fa fa-file-pdf"></i> SCARICA MODELLO 0</button>
                                            </form>
                                        </div>
                                    </div>
                                    <div class="kt-form__actions row col-md-12">
                                        <div class="form-group col-md-3">
                                            <form action="<%=request.getContextPath()%>/OperazioniGeneral" method="POST">
                                                <input type="hidden" name="type" value="sendmailModello0" />
                                                <input type="hidden" name="idallievo" value="<%=a.getId()%>" />
                                                <input type="hidden" name="maildest" value="<%=a.getEmail()%>" />
                                                <input type="hidden" name="path" value="<%=presentemod0.getPath()%>" />
                                                <button type="submit" class="btn btn-io" 
                                                        style="font-family: Poppins"><i class="fa fa-file-pdf"></i> INVIA MODELLO 0 TRAMITE MAIL AL DISCENTE</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <%}%>
                                <%if (Utility.getRequestValue(request, "esito").equals("OK")) {%>
                                <div class="row col-md-12 alert alert-success">
                                    OPERAZIONE COMPLETATA CON SUCCESSO!
                                </div>
                                <%}%>
                                <%if (Utility.getRequestValue(request, "esito").equals("KO")) {%>
                                <div class="row col-md-12 alert alert-danger">
                                    ERRORE DURANTE L'OPERAZIONE! RIPROVARE.
                                </div>
                                <%}%>
                            </div>

                        </div>
                    </div>	
                </div>
            </div>
        </div>
        <%e.close();%>
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
                                                            if (sino === "") {
                                                                document.getElementById("div_volontasi").style.display = "none";
                                                                document.getElementById("div_volontano").style.display = "none";

                                                            } else if (sino === "1") {
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