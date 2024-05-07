<%@page import="rc.so.domain.TipoDoc"%>
<%@page import="rc.so.util.Utility"%>
<%@page import="rc.so.db.Database"%>
<%@page import="rc.so.domain.TitoliStudio"%>
<%@page import="rc.so.entity.Item"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="rc.so.domain.User"%>
<%@page import="rc.so.db.Entity"%>
<%@page import="rc.so.db.Action"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    User us = (User) session.getAttribute("user");
    if (us == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    } else {
        String uri_ = request.getRequestURI();
        String pageName_ = uri_.substring(uri_.lastIndexOf("/") + 1);
        if (!Action.isVisibile(String.valueOf(us.getTipo()), pageName_)) {
            response.sendRedirect(request.getContextPath() + "/page_403.jsp");
        } else {
            String src = session.getAttribute("src").toString();
            Entity e = new Entity();
            List<Item> regioni = e.listaRegioni();
            List<Item> comuni = e.listaComuni_totale();
            comuni.addAll(e.listaNazioni_totale());
            TipoDoc richiesta = e.getEm().find(TipoDoc.class, 38L);
            TipoDoc allegatoF = e.getEm().find(TipoDoc.class, 12L);
            e.close();
            Database db = new Database(true);
            List<Item> dispo = db.query_disponibilita_rc();
            db.closeDB();
%>
<html>
    <head>
        <meta charset="utf-8" />
        <title>YES I Start Up - Toscana - Aule</title>
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
        <style type="text/css">
            .kt-section__title {
                font-size: 1.2rem!important;
            }

            .form-group {
                margin-bottom: 1rem;
            }

            .custom-file-label::after {
                color:#fff;
                background-color: #eaa21c;
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
        </style>
    </head>

    <body class="kt-header--fixed kt-header-mobile--fixed kt-subheader--fixed kt-subheader--enabled kt-subheader--solid kt-aside--enabled kt-aside--fixed">
        <%@ include file="menu/head1.jsp"%>
        <div class="kt-grid kt-grid--hor kt-grid--root">
            <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--ver kt-page">
                <%@ include file="menu/menu.jsp"%>
                <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor kt-wrapper" id="kt_wrapper">
                    <%@ include file="menu/head.jsp"%>
                    <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">
                        <div class="kt-subheader   kt-grid__item" id="kt_subheader">
                            <div class="kt-subheader   kt-grid__item" id="kt_subheader">
                                <div class="kt-subheader__main">
                                    <h3 class="kt-subheader__title">Sedi di Formazione</h3>
                                    <span class="kt-subheader__separator kt-subheader__separator--v"></span>
                                    <a class="kt-subheader__breadcrumbs-link">Aggiungi</a>
                                </div>
                            </div>
                        </div>
                        <div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="kt-portlet" id="kt_portlet" data-ktportlet="true">
                                        <form id="kt_form" action="<%=request.getContextPath()%>/OperazioniSA?type=addAula" 
                                              class="kt-form kt-form--label-right" method="post" enctype="multipart/form-data">
                                            <input type="hidden" name="save" id="save" value="0" />
                                            <div class="kt-portlet__body">
                                                <h5>DATI</h5>
                                                <div class="kt-separator kt-separator--border kt-separator--space-xs"></div>
                                                <div class="kt-section kt-section--first">
                                                    <div class="kt-section__body">

                                                        <div class="form-group row">
                                                            <div class="form-group col-4">
                                                                <label for="regione">Regione</label><label class="kt-font-danger kt-font-boldest valobbl">*</label>
                                                                <div class="dropdown bootstrap-select form-control kt-" id="regione_div" style="padding: 0;">
                                                                    <select class="form-control kt-select2-general obbligatory" id="regione" name="regione"  style="width: 100%">
                                                                        <option selected value="-">Seleziona Regione</option>
                                                                        <%for (Item i : regioni) {%>
                                                                        <option value="<%=i.getValue()%>"><%=i.getDesc()%></option>
                                                                        <%}%>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group col-4">
                                                                <label for="provincia">Provincia</label><label class="kt-font-danger kt-font-boldest valobbl">*</label>
                                                                <div class="dropdown bootstrap-select form-control kt-" id="provincia_div" style="padding: 0;">
                                                                    <select class="form-control kt-select2-general obbligatory" id="provincia" name="provincia"  style="width: 100%;">
                                                                        <option value="-">Seleziona Provincia</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="form-group col-4">
                                                                <label for="comune">Comune</label><label class="kt-font-danger kt-font-boldest valobbl">*</label>
                                                                <div class="dropdown bootstrap-select form-control kt-" id="comune_div" style="padding: 0;">
                                                                    <select class="form-control kt-select2-general obbligatory" id="comune" name="comune"  style="width: 100%;">
                                                                        <option value="-">Seleziona Comune</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div> 


                                                        <div class="form-group row ">
                                                            <div class="col-lg-4">
                                                                <label for="indirizzo">Indirizzo </label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                <input class="form-control obbligatory" name="indirizzo" id="indirizzo" />
                                                            </div>
                                                            <div class="col-lg-3">
                                                                <label for="titolo">Titolo di disponibilita' </label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                <div class="dropdown bootstrap-select form-control kt-" 
                                                                     id="titolo_div" style="padding: 0;">
                                                                    <select class="form-control kt-select2-general obbligatory" 
                                                                            id="titolo" name="titolo"  style="width: 100%">
                                                                        <option value="-">Seleziona</option>
                                                                        <%for (Item t : dispo) {%>
                                                                        <option value="<%=t.getCodice()%>"><%=t.getDescrizione()%></option>
                                                                        <%}%>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="col-lg-2">
                                                                <label for="mq">Mq aula </label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                <input class="form-control obbligatory decimalvalue" name="mq" id="mq" />
                                                            </div>   
                                                            <div class="col-lg-3" >
                                                                <label for="accr">Accreditamento Regionale </label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                <div class="dropdown bootstrap-select form-control kt-" 
                                                                     id="accr_div" style="padding: 0;">
                                                                    <select class="form-control kt-select2-general obbligatory" 
                                                                            id="accr" name="accr" style="width: 100%">
                                                                        <option value="-">Seleziona</option>
                                                                        <option value="SI">SI</option>
                                                                        <option value="NO">NO</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="form-group row ">

                                                            <div class="col-lg-4">
                                                                <label for="responsabile">Nominativo Responsabile </label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                <input class="form-control obbligatory" name="responsabile" id="responsabile" />
                                                            </div>                                                            
                                                            <div class="col-lg-4">
                                                                <label for="mailresponsabile">Mail Responsabile </label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                <input type="text" class="form-control obbligatory" name="mailresponsabile" id="mailresponsabile" maxlength="100"/>
                                                            </div>                                                       
                                                            <div class="col-lg-4">
                                                                <label for="telresponsabile">Telefono Responsabile </label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                <input type="text" class="form-control obbligatory" name="telresponsabile" id="telresponsabile" maxlength="100"/>
                                                            </div>  
                                                        </div>
                                                        <div class="form-group row ">

                                                            <div class="col-lg-4">
                                                                <label for="responsabileAmm">Nominativo Referente Amministrativo </label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                <input class="form-control obbligatory" name="responsabileAmm" id="responsabileAmm" />
                                                            </div>                                                            
                                                            <div class="col-lg-4">
                                                                <label for="mailresponsabileAmm">Mail Referente Amministrativo </label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                <input type="text" class="form-control obbligatory" name="mailresponsabileAmm" id="mailresponsabileAmm" maxlength="100"/>
                                                            </div>
                                                            <div class="col-lg-4">
                                                                <label for="telresponsabileAmm">Telefono Referente Amministrativo </label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                <input type="text" class="form-control obbligatory" name="telresponsabileAmm" id="telresponsabileAmm" maxlength="100"/>
                                                            </div> 
                                                        </div>

                                                    </div>
                                                </div>  
                                                <h5>DOCUMENTAZIONE</h5>
                                                <div class="kt-separator kt-separator--border kt-separator--space-xs"></div>
                                                <div class="kt-section kt-section--first">
                                                    <div class="kt-section__body">              
                                                        <div class="form-group row" id="div_allegatoF" style="display:none;">
                                                            <div class="col-lg-12">
                                                                <div class="form-group row">
                                                                    <div class="form-group col-lg-12">
                                                                        <h6><%=allegatoF.getDescrizione()%></h6>
                                                                        <small><label>Caricare il documento firmato digitalmente (.p7m CAdES, .pdf PAdES) nel campo seguente:</label>
                                                                        </small>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group row">
                                                                    <div class="form-group col-lg-6">
                                                                        <div class="custom-file">
                                                                            <input type="file" tipo="obbligatory" class="custom-file-input" 
                                                                                   accept="<%=allegatoF.getMimetype()%>" 
                                                                                   name="doc_<%=allegatoF.getId()%>" id="doc_<%=allegatoF.getId()%>" 
                                                                                   onchange="return checkFileExtAndDim('<%=allegatoF.getEstensione()%>');" />
                                                                            <label style="text-align: left;" class="custom-file-label selected" id="label_file">Scegli File</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>   <hr>                                                     
                                                        <div class="form-group row">
                                                            <div class="col-lg-12">
                                                                <div class="form-group row">
                                                                    <div class="form-group col-lg-12">
                                                                        <h6><%=richiesta.getDescrizione()%></h6>
                                                                        <small><label>Scaricare il modello con i dati inseriti per poi caricarlo firmato digitalmente (.p7m CAdES, .pdf PAdES) nel campo seguente.</label>
                                                                        </small>
                                                                    </div>
                                                                </div>
                                                                <div class="form-group row">
                                                                    <div class="form-group col-xl-6 col-lg-6">
                                                                        <button class="btn btn-io btn-md btn-tall btn-wide kt-font-bold kt-font-transform-u" 
                                                                                type="button" 
                                                                                onclick="return model_funct('<%=richiesta.getId()%>');">
                                                                            Scarica
                                                                        </button>
                                                                    </div>
                                                                    <div class="form-group col-xl-4 col-lg-6">
                                                                        <div class="custom-file">
                                                                            <input type="file" tipo="obbligatory" class="custom-file-input" 
                                                                                   accept="<%=richiesta.getMimetype()%>" 
                                                                                   name="doc_<%=richiesta.getId()%>" id="doc_<%=richiesta.getId()%>" 
                                                                                   onchange="return checkFileExtAndDim('<%=richiesta.getEstensione()%>');" />
                                                                            <label style="text-align: left;" 
                                                                                   class="custom-file-label selected" id="label_file">Scegli File</label>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>                                                        
                                                    </div>
                                                </div>

                                                <div class="kt-portlet__foot">
                                                    <div class="kt-form__actions">
                                                        <div class="row">
                                                            <div class="offset-lg-6 col-lg-6 kt-align-right">
                                                                <a id="submit" href="javascript:void(0);" class="btn btn-io"><font color='white'>Salva</font></a>
                                                                <a href="<%=pageName_%>" class="btn btn-io-n"><font color='white'>Reset</font></a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%@ include file="menu/footer.jsp"%>
                </div>
            </div>
        </div>
        <div id="kt_scrolltop" class="kt-scrolltop">
            <i class="fa fa-arrow-up"></i>
        </div>
        <!--begin:: Global Mandatory Vendors -->
        <script src="<%=src%>/assets/soop/js/jquery-3.6.1.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/popper.js/dist/umd/popper.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/bootstrap/dist/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/js-cookie/src/js.cookie.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/soop/js/moment.min.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/tooltip.js/dist/umd/tooltip.min.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/perfect-scrollbar/dist/perfect-scrollbar.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/sticky-js/dist/sticky.min.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/jquery-form/dist/jquery.form.min.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/demo/default/base/scripts.bundle.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/bundle/app.bundle.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/general/components/extended/blockui1.33.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/soop/js/utility.js" type="text/javascript"></script>
        <!-- this page -->
        <script src="<%=src%>/assets/vendors/general/select2/dist/js/select2.full.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/select2.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/bootstrap-select/dist/js/bootstrap-select.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/bootstrap-datepicker.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/js/bootstrap-datepicker.js" type="text/javascript"></script>
        <script id="newAula" src="<%=src%>/page/sa/js/newAula.js<%="?dummy=" + String.valueOf(new Date().getTime())%>" data-context="<%=request.getContextPath()%>" type="text/javascript"></script> 
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
<%
        }
    }
%>