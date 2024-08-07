<!DOCTYPE HTML>
<%@page import="rc.so.domain.MascheraM5"%>
<%@page import="rc.so.domain.ModelliPrg"%>
<%@page import="rc.so.domain.DocumentiPrg"%>
<%@page import="rc.so.domain.Ateco"%>
<%@page import="rc.so.domain.Documenti_Allievi"%>
<%@page import="java.util.ArrayList"%>
<%@page import="rc.so.entity.Item"%>
<%@page import="rc.so.domain.Formagiuridica"%>
<%@page import="rc.so.domain.ValutazioneFinaleM5"%>
<%@page import="rc.so.domain.TipoDoc"%>
<%@page import="rc.so.domain.TipoDoc_Allievi"%>
<%@page import="java.util.Map"%>
<%@page import="rc.so.domain.Allievi"%>
<%@page import="rc.so.domain.StatiPrg"%>
<%@page import="rc.so.util.Utility"%>
<%@page import="java.util.List"%>
<%@page import="rc.so.db.Action"%>
<%@page import="rc.so.domain.User"%>
<%@page import="rc.so.domain.ProgettiFormativi"%>
<%@page import="rc.so.db.Entity"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            ProgettiFormativi p = e.getEm().find(ProgettiFormativi.class, Long.parseLong(request.getParameter("id")));
            List<Allievi> al = e.getAllieviProgettiFormativi(p);
            List<MascheraM5> rendicontati = e.getM5Loaded_byPF(p);
            Map<Long, Long> allievi_m5 = Utility.allieviM5_loaded(rendicontati);
            Map<Allievi, Documenti_Allievi> modelli5 = Utility.Modello5Allievi(rendicontati);
            List<Ateco> list_codes = e.list_CodiciAteco();
            List<TipoDoc_Allievi> docs_a = e.getTipoDocAllievi(e.getEm().find(StatiPrg.class, "ATB"));
            //Domanda di ammissione
            TipoDoc_Allievi bus_pl = Utility.filterDocAllievoById(docs_a, 19L);
            //Modelli fase ATB: Domanda Complessiva e Modello 6
            List<TipoDoc> tipo_doc = e.getTipoDoc(e.getEm().find(StatiPrg.class, "ATB"));
            TipoDoc complessivo = Utility.filterDocById(tipo_doc, 30L);
            TipoDoc complessivo_temp = Utility.filterDocById(tipo_doc, 33L);
            DocumentiPrg registro_complessivo_temp = Utility.filterByTipo(p, complessivo_temp);
            DocumentiPrg registro_complessivo = Utility.filterByTipo(p, complessivo);
            TipoDoc m6 = Utility.filterDocById(tipo_doc, 31L);
            //Liste e mappe per gli input della maschera M5
            boolean m6_loaded = Utility.filterModello6(p.getModelli()) != null ? true : false;
            List<Formagiuridica> list_fg = e.findAll(Formagiuridica.class);
            List<Item> regioni = e.listaRegioni();
            e.close();
            List<Allievi> m5_files = new ArrayList();
            boolean fancy = request.getParameter("fb") != null && request.getParameter("fb").equals("1") ? false : true;
            int cntA = al.size();
%>
<html>
    <head>
        <meta charset="utf-8" />
        <title>YES I Start Up - Toscana - Conclusione Progetto Formativo</title>
        <meta name="description" content="Updates and statistics">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <!--begin::Fonts -->
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
        <link href="<%=src%>/assets/app/custom/wizard/wizard-v1_io.css" rel="stylesheet" type="text/css"/>
        <link href="<%=src%>/assets/vendors/general/select2/dist/css/select2.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/bootstrap-select/dist/css/bootstrap-select.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />

        <!-- - -->
        <link href="<%=src%>/assets/vendors/general/perfect-scrollbar/css/perfect-scrollbar.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/bootstrap-touchspin/dist/jquery.bootstrap-touchspin.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.css" rel="stylesheet" type="text/css" />
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

        <style type="text/css">
            .form-group {
                margin-bottom: 1rem;
            }

            .custom-file-label::after {
                color:#fff;
                background-color: #eaa21c;
            }
            .offset-sm-1{
                margin-left: 5%;
            }

            .kt-radio > span::after {
                border: solid #363a90;
                border-color: #363a90;
            }

            .kt-radio-valid > span {
                border-color: #0abb87 !important;
            }

            .kt-radio-invalid > span {
                border-color: #fd397a !important;
            }

            .loaded::after{
                display: none;
            }

            .btn-danger {
                color: #fff;
                background-color: #d90f0f;
                border-color: #d90f0f;
            }
        </style>




    </head>
    <body class="kt-header--fixed kt-header-mobile--fixed kt-subheader--fixed kt-subheader--enabled kt-subheader--solid kt-aside--enabled kt-aside--fixed">
        <!-- begin:: Page -->
        <%if (fancy) {%>
        <%@ include file="menu/head1.jsp"%>
        <div class="kt-grid kt-grid--hor kt-grid--root">
            <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--ver kt-page">
                <%@ include file="menu/menu.jsp"%>
                <!-- end:: Aside -->
                <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor kt-wrapper" id="kt_wrapper">
                    <%@ include file="menu/head.jsp"%>
                    <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">
                        <div class="kt-subheader   kt-grid__item" id="kt_subheader">
                            <div class="kt-subheader   kt-grid__item" id="kt_subheader">
                                <div class="kt-subheader__main">
                                    <h3 class="kt-subheader__title">Progetto Formativo</h3>
                                    <span class="kt-subheader__separator kt-subheader__separator--v"></span>
                                    <a class="kt-subheader__breadcrumbs-link">Conclusione</a>
                                </div>
                            </div>
                        </div>
                        <%} else {%>

                        <div class="kt-grid kt-grid--hor kt-grid--root">
                            <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--ver kt-page">
                                <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor">
                                    <%}%>
                                    <!-- end:: Content Head -->
                                    <!-- begin:: Content -->

                                    <div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">

                                        <div class="row" style="height: 100%;">
                                            <div class="kt-grid  kt-wizard-v1 kt-wizard-v1--white" id="kt_wizard_v1" data-ktwizard-state="step-first" style="width: 100%;">
                                                <div class="kt-grid__item" style="">
                                                    <!--begin: Form Wizard Nav -->
                                                    <div class="kt-wizard-v1__nav">
                                                        <div class="kt-wizard-v1__nav-items" style="background: #fff; border-top-left-radius: 10px; border-top-right-radius: 10px;">
                                                            <a class="kt-wizard-v1__nav-item" href="#" data-ktwizard-type="step" data-ktwizard-state="current">
                                                                <div class="kt-wizard-v1__nav-body " >
                                                                    <div class="kt-wizard-v1__nav-icon">
                                                                        <i class="fa fa-users" ></i>
                                                                    </div>
                                                                    <div class="kt-wizard-v1__nav-label text-uppercase">
                                                                        1 - Modello 5 Allievi
                                                                    </div>
                                                                </div>
                                                            </a>
                                                            <a class="kt-wizard-v1__nav-item" href="#" data-ktwizard-type="step">
                                                                <div class="kt-wizard-v1__nav-body">
                                                                    <div class="kt-wizard-v1__nav-icon">
                                                                        <i class="fa fa-file-pdf"></i>
                                                                    </div>
                                                                    <div class="kt-wizard-v1__nav-label text-uppercase">
                                                                        2 - Stampa registri
                                                                    </div>
                                                                </div>
                                                            </a>
                                                            <a class="kt-wizard-v1__nav-item" href="#" data-ktwizard-type="step">
                                                                <div class="kt-wizard-v1__nav-body">
                                                                    <div class="kt-wizard-v1__nav-icon">
                                                                        <i class="fa fa-pen-nib"></i>
                                                                    </div>
                                                                    <div class="kt-wizard-v1__nav-label  text-uppercase">
                                                                        3 - Dichiarazione di chiusura percorso
                                                                    </div>
                                                                </div>
                                                            </a>
                                                            <a class="kt-wizard-v1__nav-item" href="#" data-ktwizard-type="step">
                                                                <div class="kt-wizard-v1__nav-body">
                                                                    <div class="kt-wizard-v1__nav-icon">
                                                                        <i class="fa fa-file-pdf"></i>
                                                                    </div>
                                                                    <div class="kt-wizard-v1__nav-label  text-uppercase">
                                                                        4 - Area richiesta contributo
                                                                    </div>
                                                                </div>
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!--end: Form Wizard Nav -->
                                                <div class="kt-grid__item kt-grid__item--fluid kt-wizard-v1__wrapper">
                                                    <form id="kt_form" action="<%=request.getContextPath()%>/OperazioniSA?type=concludiPrg" 
                                                          style="padding: 0;" class="kt-form kt-form--label-right"
                                                          accept-charset="ISO-8859-1" method="post" enctype="multipart/form-data">
                                                        <input type="hidden" class="hidden" name="pf" id="pf" value="<%=p.getId()%>" />
                                                        <input type="hidden" class="hidden" name="cip" id="cip" value="<%=p.getCip()%>" />
                                                        <input type="hidden" class="hidden" name="currentstep" id="currentstep"/>
                                                        <!--step: 1-->
                                                        <div class="kt-wizard-v1__content" id="step1" data-ktwizard-type="step-content" data-ktwizard-state="current">
                                                            <div class="kt-form__section kt-form__section--first">
                                                                <div class="kt-wizard-v1__form" style="color: #6c7293; min-height: 40vh">
                                                                    <%for (Allievi a : al) {

                                                                            if (allievi_m5.get(a.getId()) == null) {%>
                                                                    <div class="accordion  accordion-toggle-arrow countAtoL" id="al_<%=a.getId()%>">
                                                                        <div class="card" style="border-radius: 17px; margin-block: 20px; color: #363a90;">

                                                                            <div class="card-header" id="headingOne4_<%=a.getId()%>">
                                                                                <div class="card-title collapsed" data-toggle="collapse" data-target="#collapse_al_<%=a.getId()%>" aria-expanded="false" aria-controls="collapse_al_<%=a.getId()%>">
                                                                                    <i class="fa fa-user"></i> <%=a.getNome()%> <%=a.getCognome()%> (ORE SVOLTE <%=Utility.roundDoubleAndFormat(a.getImporto())%>)
                                                                                </div>
                                                                            </div>
                                                                            <div id="collapse_al_<%=a.getId()%>" class="collapse" aria-labelledby="headingOne" data-parent="#al_<%=a.getId()%>" style="">
                                                                                <div class="card-body">
                                                                                    <div id="cont_daok_<%=a.getId()%>">
                                                                                        <h5 style="color:#b9003d; font-weight: 800;">PROGETTO D'IMPRESA</h5>
                                                                                        <br>
                                                                                        <div class="form-group">
                                                                                            <label for="fg_<%=a.getId()%>"><b>Grado di completezza Business Plan elaborato</b></label>
                                                                                            <label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                            <div class="dropdown bootstrap-select form-control kt-" id="gcbp_<%=a.getId()%>_div" style="padding: 0;">
                                                                                                <select class="form-control kt-select2-general obbligatory" id="gcbp_<%=a.getId()%>" name="gcbp_<%=a.getId()%>"
                                                                                                        style="width: 100%" onchange="return BPPRESENTE('<%=a.getId()%>');">
                                                                                                    <option selected value="-">Seleziona</option>
                                                                                                    <option selected value="00">NON ELABORATO</option>
                                                                                                    <option selected value="01">INCOMPLETO</option>
                                                                                                    <option selected value="02">PARIALMENTE COMPLETO</option>
                                                                                                    <option selected value="03">ABBASTANZA COMPLETO</option>
                                                                                                    <option selected value="04">COMPLETO</option>
                                                                                                </select>
                                                                                            </div>
                                                                                        </div>
                                                                                        <div id="BPPRESENTE_<%=a.getId()%>">

                                                                                            <div class="form-group">
                                                                                                <label for="fg_<%=a.getId()%>"><b>PIANO D'IMPRESA ELABORATO (BUSINESS PLAN)</b></label>
                                                                                                <div class="col-9" id="file_daok_<%=a.getId()%>">
                                                                                                    <div class="custom-file">
                                                                                                        <input type="file" <%=bus_pl.getObbligatorio() == 1 ? "tipo='obbligatory'" : ""%> 
                                                                                                               class="custom-file-input" id="doc_<%=a.getId()%>"
                                                                                                               accept="<%=bus_pl.getMimetype()%>" name="doc_<%=a.getId()%>" 
                                                                                                               onchange="return checkFileExtAndDim('<%=bus_pl.getEstensione()%>');">
                                                                                                        <label class="custom-file-label selected" 
                                                                                                               style="color: #a7abc3; text-align: left;">Scegli File</label>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </div>
                                                                                            <div class="form-group">
                                                                                                <label for="fg_<%=a.getId()%>"><b>Probabilita' di realizzazione idea imprenditoriale</b></label>
                                                                                                <label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                                <div class="dropdown bootstrap-select form-control kt-" id="prob_<%=a.getId()%>_div" style="padding: 0;">
                                                                                                    <select class="form-control kt-select2-general obbligatory" id="prob_<%=a.getId()%>" name="prob_<%=a.getId()%>"
                                                                                                            style="width: 100%">
                                                                                                        <option selected value="-">Seleziona</option>
                                                                                                        <option selected value="01">NESSUNA PROBABILITA'</option>
                                                                                                        <option selected value="02">IMPROBABILE</option>
                                                                                                        <option selected value="03">POCO PROBABILE</option>
                                                                                                        <option selected value="04">PROBABILE</option>
                                                                                                        <option selected value="05">MOLTO PROBABILE</option>
                                                                                                        <option selected value="06">ALTAMENTE PROBABILE</option>
                                                                                                    </select>
                                                                                                </div>
                                                                                            </div>
                                                                                            <div class="form-group">
                                                                                                <label for="fg_<%=a.getId()%>"><b>Forma giuridica</b></label><label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                                <div class="dropdown bootstrap-select form-control kt-" id="fg_<%=a.getId()%>_div" style="padding: 0;">
                                                                                                    <select class="form-control kt-select2-general obbligatory" id="fg_<%=a.getId()%>" name="fg_<%=a.getId()%>"  style="width: 100%">
                                                                                                        <option selected value="-">Seleziona forma giuridica</option>
                                                                                                        <%for (Formagiuridica f : list_fg) {%>
                                                                                                        <option value="<%=f.getId()%>"><%=f.getDescrizione()%></option>
                                                                                                        <%}%>
                                                                                                    </select>
                                                                                                </div>
                                                                                            </div>
                                                                                            <div class="form-group row">
                                                                                                <div class="form-group col-9">
                                                                                                    <label for="ateco_<%=a.getId()%>"><b>Codice Ateco</b></label><label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                                    <div class="dropdown bootstrap-select form-control kt-" id="ateco_<%=a.getId()%>_div" style="padding: 0;">
                                                                                                        <select class="form-control kt-select2-general obbligatory" id="ateco_<%=a.getId()%>" name="ateco_<%=a.getId()%>"  style="width: 100%">
                                                                                                            <option selected value="-">Seleziona Codice Ateco</option>
                                                                                                            <%for (Ateco i : list_codes) {%>
                                                                                                            <option value="<%=i.getId()%>"><%=i.getId()%> - <%=i.getDescrizione()%></option>
                                                                                                            <%}%>
                                                                                                        </select>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="col-3">
                                                                                                    <label for="check_sede_<%=a.getId()%>"><b>Sede individuata?</b></label><label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                                    <div class="kt-radio-inline radioGroup_<%=a.getId()%>">
                                                                                                        <label class="kt-radio" name="check_sede_<%=a.getId()%>">
                                                                                                            <input type="radio" name="check_sede_<%=a.getId()%>" value="SI"> SI
                                                                                                            <span></span>
                                                                                                        </label>
                                                                                                        <label class="kt-radio" name="check_sede_<%=a.getId()%>">
                                                                                                            <input type="radio" name="check_sede_<%=a.getId()%>" value="NO"> NO
                                                                                                            <span></span>
                                                                                                        </label>
                                                                                                    </div>
                                                                                                </div>  
                                                                                            </div>      
                                                                                            <div class="form-group row">
                                                                                                <div class="form-group col-4">
                                                                                                    <label for="regione_<%=a.getId()%>"><b>Regione di insediamento</b></label><label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                                    <div class="dropdown bootstrap-select form-control kt-" id="regione_<%=a.getId()%>_div" style="padding: 0;">
                                                                                                        <select class="form-control kt-select2-general obbligatory" id="regione_<%=a.getId()%>" name="regione_<%=a.getId()%>"  style="width: 100%">
                                                                                                            <option selected value="-">Seleziona Regione</option>
                                                                                                            <%for (Item i : regioni) {%>
                                                                                                            <option value="<%=i.getValue()%>"><%=i.getDesc()%></option>
                                                                                                            <%}%>
                                                                                                        </select>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group col-4">
                                                                                                    <label for="provincia_<%=a.getId()%>"><b>Provincia di insediamento</b></label><label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                                    <div class="dropdown bootstrap-select form-control kt-" id="provincia_<%=a.getId()%>_div" style="padding: 0;">
                                                                                                        <select class="form-control kt-select2-general obbligatory" id="provincia_<%=a.getId()%>" name="provincia_<%=a.getId()%>"  style="width: 100%;">
                                                                                                            <option value="-">Seleziona Provincia</option>
                                                                                                        </select>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group col-4">
                                                                                                    <label for="comune_<%=a.getId()%>"><b>Comune di insediamento</b></label><label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                                    <div class="dropdown bootstrap-select form-control kt-" id="comune_<%=a.getId()%>_div" style="padding: 0;">
                                                                                                        <select class="form-control kt-select2-general obbligatory" id="comune_<%=a.getId()%>" name="comune_<%=a.getId()%>"  style="width: 100%;">
                                                                                                            <option value="-">Seleziona Comune</option>
                                                                                                        </select>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </div>    
                                                                                            <div class="form-group row">
                                                                                                <div class="col-6">
                                                                                                    <label><b>Totale fabbisogno finanziario</b></label><label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label> <i class="fa fa-info-circle kt-font-io-n" data-container="body" data-toggle="kt-popover" data-placement="top" data-original-title="Formato" data-content="€ ___.__1.234,56"></i>
                                                                                                    <div>
                                                                                                        <input class="form-control currencymask obbligatory" name="tff_<%=a.getId()%>" id="tff_<%=a.getId()%>" data-inputmask="'removeMaskOnSubmit': true" type="text"/>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="col-6">
                                                                                                    <label for="check_misura_<%=a.getId()%>" ><b>MISURA DI AGEVOLAZIONE INDIVIDUATA?</b></label>
                                                                                                    <label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                                    <div class="kt-radio-inline radioGroup_<%=a.getId()%>">
                                                                                                        <label class="kt-radio" name="check_misura_<%=a.getId()%>">
                                                                                                            <input type="radio" name="check_misura_<%=a.getId()%>" value="SI" onchange="return misuraindividuata(<%=a.getId()%>);"> SI
                                                                                                            <span></span>
                                                                                                        </label>
                                                                                                        <label class="kt-radio" name="check_misura_<%=a.getId()%>">
                                                                                                            <input type="radio" name="check_misura_<%=a.getId()%>" value="NO" onchange="return misuraindividuata(<%=a.getId()%>);"> NO
                                                                                                            <span></span>
                                                                                                        </label>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </div>
                                                                                            <div class="form-group row" id="MISURANO_<%=a.getId()%>" style="display:none;">
                                                                                                <div class="form-group col-md-12">
                                                                                                    <label for="no_mot_misura_<%=a.getId()%>">
                                                                                                        <b>Motivazione</b>
                                                                                                    </label>
                                                                                                    <label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                                    <div class="dropdown bootstrap-select form-control kt-" 
                                                                                                         id="no_mot_misura_<%=a.getId()%>_div" style="padding: 0;">
                                                                                                        <select class="form-control kt-select2-general obbligatory" 
                                                                                                                id="no_mot_misura_<%=a.getId()%>" name="no_mot_misura_<%=a.getId()%>"
                                                                                                                style="width: 100%">
                                                                                                            <option selected value="-">Seleziona</option>
                                                                                                            <option selected value="01">Poca convinzione dell'allievo sull'idea imprenditoriale</option>
                                                                                                            <option selected value="02">Poca convenienza e/o difficile realizzazione dell'idea imprenditoriale</option>
                                                                                                            <option selected value="03">Indecisione dell'allievo sulla scelta auto-imprenditoriale</option>
                                                                                                            <option selected value="04">Carenza di motivazione personale</option>
                                                                                                            <option selected value="05">Difficoltà a reperire finanziamenti</option>
                                                                                                            <option selected value="06">Difficoltà burocratiche legate a permessi, certificazioni, ecc.</option>
                                                                                                            <option selected value="07">Mancanza di strumenti finanziari di agevolazione</option>
                                                                                                            <option selected value="08">Altro</option>
                                                                                                        </select>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </div>
                                                                                            <div class="form-group row" id="MISURASI_<%=a.getId()%>" style="display:none;">
                                                                                                <div class="form-group col-12">
                                                                                                    <label><b>Denominazione Misura di agevolazione individuata</b></label>
                                                                                                    <div>
                                                                                                        <input class="form-control obbligatory"
                                                                                                               name="den_misura_si_<%=a.getId()%>" id="den_misura_si_<%=a.getId()%>" maxlength="100" max="100" type="text"/>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group col-md-6">
                                                                                                    <label for="tipo_misura_<%=a.getId()%>">
                                                                                                        <b>Tipologia di agevolazione individuata</b>
                                                                                                    </label>
                                                                                                    <label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                                    <div class="dropdown bootstrap-select form-control kt-" 
                                                                                                         id="tipo_misura_<%=a.getId()%>_div" style="padding: 0;">
                                                                                                        <select class="form-control kt-select2-general obbligatory" 
                                                                                                                id="tipo_misura_<%=a.getId()%>" name="tipo_misura_<%=a.getId()%>"
                                                                                                                style="width: 100%">
                                                                                                            <option selected value="-">Seleziona</option>
                                                                                                            <option selected value="01">NAZIONALE</option>
                                                                                                            <option selected value="02">CIRCOSCRIZIONALE</option>
                                                                                                            <option selected value="03">REGIONALE</option>
                                                                                                            <option selected value="04">CREDITO ORDINARIO</option>
                                                                                                            <option selected value="05">AUTOFINANZIAMENTO, FONDI PROPRI</option>
                                                                                                            <option selected value="06">MICROCREDITO TRAMITE ENM</option>
                                                                                                            <option selected value="07">ALTRO</option>
                                                                                                        </select>
                                                                                                    </div>
                                                                                                </div>
                                                                                                <div class="form-group col-md-6">
                                                                                                    <label for="si_mot_misura_<%=a.getId()%>">
                                                                                                        <b>Motivazione principale dell'agevolazione prescelta</b>
                                                                                                    </label>
                                                                                                    <label class="kt-font-danger kt-font-boldest valobbl_<%=a.getId()%>">*</label>
                                                                                                    <div class="dropdown bootstrap-select form-control kt-" 
                                                                                                         id="si_mot_misura_<%=a.getId()%>_div" style="padding: 0;">
                                                                                                        <select class="form-control kt-select2-general obbligatory" 
                                                                                                                id="si_mot_misura_<%=a.getId()%>" name="si_mot_misura_<%=a.getId()%>"
                                                                                                                style="width: 100%">
                                                                                                            <option selected value="-">Seleziona</option>
                                                                                                            <option selected value="01">Possibilità di finanziamento consistente</option>
                                                                                                            <option selected value="02">Procedure più semplici</option>
                                                                                                            <option selected value="03">Criteri di selezione meno stringenti</option>
                                                                                                            <option selected value="04">Tempi di istruttoria più veloci</option>
                                                                                                            <option selected value="05">Piano di ammortamento e restituzione più conveniente</option>
                                                                                                            <option selected value="06">Minori vincoli nella gestione dei fondi</option>
                                                                                                            <option selected value="07">Presenza di quota a fondo perduto</option>
                                                                                                            <option selected value="08">Assenza di alternative</option>
                                                                                                        </select>
                                                                                                    </div>
                                                                                                </div>
                                                                                            </div>
                                                                                        </div>

                                                                                    </div>
                                                                                    <div class="kt-section__content kt-section__content--solid text-center" style="margin-top:3rem;">
                                                                                        <a id="rendiconta_<%=a.getId()%>" href="javascript:void(0);" class="btn btn-io" style="font-family: Poppins;"><i class="fa fa-check"></i> Completa</a>
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <%} else {
                                                                        m5_files.add(a);%>
                                                                    <div class="accordion  accordion-toggle-arrow" id="al_<%=a.getId()%>">
                                                                        <div class="card" style="border-radius: 17px; margin-block: 20px; color: #363a90;">
                                                                            <div class="card-header">
                                                                                <div class="card-title loaded">
                                                                                    <i class="fa fa-check-circle kt-font-success"></i>&nbsp;<i class="fa fa-user kt-font-success"></i> <%=a.getNome()%> <%=a.getCognome()%> (ORE SVOLTE <%=Utility.roundDoubleAndFormat(a.getImporto())%> - Dati del Progetto d'Impresa caricati)
                                                                                    <a href="<%=request.getContextPath()%>/OperazioniSA?type=scaricaModello5&iduser=<%=a.getId()%>&idmodello=<%=allievi_m5.get(a.getId())%>" target="_blank" class="btn"><i class="fa fa-file-download kt-font-success" style='position: absolute; right: 3rem' data-container="body" data-html="true" data-toggle="kt-tooltip" data-placement="top" title="<h5>Scarica modello 5 precompilato relativo a <%=a.getNome()%> <%=a.getCognome()%></h5>"></i></a>
                                                                                    <a href="javascript:void(0);" onclick="return deleteModello('<%=allievi_m5.get(a.getId())%>');" class="btn">
                                                                                        <i style='position: absolute; right: 1rem' class="fa fa-times-circle kt-font-danger"
                                                                                           data-container="body" data-html="true" data-toggle="kt-tooltip" data-placement="top" 
                                                                                           title="<h5>Elimina modello 5 per <%=a.getNome()%> <%=a.getCognome()%></h5>"></i></a>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>            
                                                                    <%}

                                                                        }%>
                                                                    <br>
                                                                    <div class="kt-section__content kt-section__content--solid text-center">
                                                                        <%for (Allievi a : m5_files) {%>
                                                                        <div class="btn-group" role="group" aria-label="First group">
                                                                            <button type="button" disabled="disabled" class="btn" style="background-color: #363a90!important; opacity: 1!important; color:white;"><%=a.getNome()%> <%=a.getCognome()%></button>
                                                                            <%if (modelli5.get(a) == null) {%>
                                                                            <button id="loadM5_<%=a.getId()%>" type="button" load="toload" class="btn btn-io-n" data-container="body" data-html="true" data-toggle="kt-tooltip" data-placement="top" title="<h5>Carica modello 5 - <%=a.getNome()%> <%=a.getCognome()%></h5>" ><i class="fa fa-file-signature"></i></button>
                                                                                <%} else {%>
                                                                            <button id="loadM5_<%=a.getId()%>" type="button" class="btn btn-io" data-container="body" data-html="true" data-toggle="kt-tooltip" data-placement="top" title="<h5>Cambia modello 5 - <%=a.getNome()%> <%=a.getCognome()%></h5>" ><i class="fa fa-file-signature"></i></button>
                                                                            <a target="_blank" href="<%=request.getContextPath()%>/OperazioniGeneral?type=showDoc&path=<%=modelli5.get(a).getPath()%>" style="display: flex;" class="btn btn-io" data-container="body" data-html="true" data-toggle="kt-tooltip" data-placement="top" title="<h5>Visualizza modello 5 caricato</h5>" ><i class="fa fa-download"></i></a> 
                                                                                <%}%>
                                                                        </div>   
                                                                        <%}%>    
                                                                    </div>
                                                                    <br>
                                                                    <label class="kt-font-io kt-font-bold"><font size="2">Per ogni allievo scaricare il modello 5 (dal tasto <i class="fa fa-file-download kt-font-success"></i>), firmarlo digitalmente e ricaricarlo.</font></label>
                                                                </div>
                                                            </div>
                                                            <label class="kt-font-danger kt-font-bold"><font size="2" >Per poter proseguire è necessario completare ogni allievo presente in lista - <%=allievi_m5.size()%>/<%=cntA%></font></label>
                                                        </div>
                                                        <!--step: 2-->
                                                        <div class="kt-wizard-v1__content" id="step2" data-ktwizard-type="step-content">
                                                            <div class="kt-form__section kt-form__section--first">
                                                                <div class="kt-wizard-v1__form" style="color: #6c7293;">
                                                                    <div class="accordion  accordion-toggle-arrow">
                                                                        <%if (registro_complessivo_temp != null) {%>
                                                                        <div class="card" style="border-radius: 17px; margin-block: 20px; color: #363a90;">
                                                                            <div class="card-header">
                                                                                <div class="card-title loaded">
                                                                                    <i class="fa fa-file-pdf kt-font-success"></i>&nbsp;<i class="fa fa-users kt-font-success"></i> REGISTRO COMPLESSIVO PRESENZE
                                                                                    <a href="<%=request.getContextPath()%>/OperazioniGeneral?type=showDoc&path=<%=registro_complessivo_temp.getPath()%>" target="_blank" class="btn"><i class="fa fa-file-download kt-font-success" style='position: absolute; right: 1rem' data-container="body" data-html="true" data-toggle="kt-tooltip" data-placement="top" title="<h5>Scarica il registro complessivo delle presenze</h5>"></i></a>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                        <%}%>
                                                                    </div>
                                                                    <div class="kt-section__content kt-section__content--solid text-center" style="margin-top: 3rem; margin-bottom: 2rem;">
                                                                        <%if (registro_complessivo == null) {%>
                                                                        <button id="registro_<%=p.getId()%>" type="button" registro="toload" class="btn btn-io-n"><i class="fa fa-file-signature"></i>&nbsp;Carica <%=complessivo.getDescrizione()%></button>&nbsp;
                                                                        <label style="display: flex; margin-top:1.5rem;" class="kt-font-io kt-font-bold"><font size="2">Il presente registro complessivo delle presenze generato dal sistema, va scaricato (dal tasto <i class="fa fa-file-download kt-font-success"></i> ), firmato digitalmente (.p7m CAdES, .pdf PAdES) e ricaricato.</font></label>
                                                                        <label style="display: flex; margin-top:0.5rem;" class="kt-font-danger kt-font-bold"><font size="2" >Per poter proseguire è necessario caricare il registro complessivo delle presenze firmato digitalmente - 0/1</font></label>
                                                                            <%} else {%>
                                                                        <div class="btn-group" role="group" aria-label="First group">
                                                                            <button id="registro_<%=p.getId()%>" type="button" class="btn btn-success"><i class="fa fa-file-signature"></i>&nbsp;Cambia <%=complessivo.getDescrizione()%></button>
                                                                            <a target="_blank" href="<%=request.getContextPath()%>/OperazioniGeneral?type=showDoc&path=<%=registro_complessivo.getPath()%>" style="display: flex;" class="btn btn-success" data-container="body" data-html="true" data-toggle="kt-tooltip" data-placement="top" title="<h5>Visualizza registro complessivo presenze caricato</h5>" ><i class="fa fa-expand"></i></a> 
                                                                        </div>
                                                                        <label style="display: flex; margin-top:1.5rem;" class="kt-font-io kt-font-bold"><font size="2">Il presente registro complessivo delle presenze generato dal sistema, va scaricato (dal tasto <i class="fa fa-file-download kt-font-success"></i> ), firmato digitalmente (.p7m CAdES, .pdf PAdES) e ricaricato.</font></label>
                                                                        <label style="display: flex; margin-top:0.5rem;" class="kt-font-danger kt-font-bold"><font size="2" >Per poter proseguire è necessario caricare il registro complessivo delle presenze firmato digitalmente - 1/1</font></label>
                                                                            <%}%>
                                                                    </div>




                                                                </div>
                                                            </div>
                                                        </div>
                                                        <!--step: 3-->
                                                        <div class="kt-wizard-v1__content" id="step3" data-ktwizard-type="step-content">
                                                            <div class="kt-form__section kt-form__section--first">
                                                                <div class="kt-wizard-v1__form" style="color: #6c7293;">
                                                                    <!--<div class="form-group col-xl-12 col-lg-12">-->
                                                                    <!--<div class="row">-->
                                                                    <label style="text-align: left;"><b>SI DICHIARA</b> ai sensi del D.P.R. n.445/2000, sotto la propria personale responsabilità, consapevole delle pene stabilite per false mendaci dichiarazioni dall’art. 76 del predetto D.P.R., che:</label>
                                                                    <div class="form-group row" style="width: 100%;">
                                                                        <div class="kt-radio-list">
                                                                            <label class="kt-radio">
                                                                                <input type="radio" value="1" name="scelta_step3"> La sede di svolgimento del percorso anche sede legale/amministrativa  
                                                                                <span></span>
                                                                            </label>
                                                                            <label class="kt-radio">
                                                                                <input type="radio" value="2" name="scelta_step3"> La sede amministrativa/legale della scrivente sita in: 
                                                                                <span></span>
                                                                            </label>
                                                                        </div>
                                                                    </div>
                                                                    <div style="padding: 20px; margin: 10px 0px 30px; border: 4px solid rgb(239, 239, 239); width: 100%;" id="info_step3">
                                                                        <div class="row"  style="width: 100%;">
                                                                            <div class="form-group col-4">
                                                                                <label for="regione_step3">Regione</label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                                <div class="dropdown bootstrap-select form-control kt-" id="regione_step3_div" style="padding: 0;">
                                                                                    <select class="form-control kt-select2-general obbligatory" id="regione_step3" name="regione_step3"  style="width: 100%">
                                                                                        <option value="-">Seleziona Regione</option>
                                                                                        <%for (Item i : regioni) {%>
                                                                                        <option value="<%=i.getValue()%>"><%=i.getDesc()%></option>
                                                                                        <%}%>
                                                                                    </select>
                                                                                </div>
                                                                            </div>
                                                                            <div class="form-group col-4">
                                                                                <label for="provincia_step3">Provincia</label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                                <div class="dropdown bootstrap-select form-control kt-" id="provincia_step3_div" style="padding: 0;">
                                                                                    <select class="form-control kt-select2-general obbligatory" id="provincia_step3" name="provincia_step3"  style="width: 100%;">
                                                                                        <option value="-">Seleziona Provincia</option>
                                                                                    </select>
                                                                                </div>
                                                                            </div>
                                                                            <div class="form-group col-4">
                                                                                <label for="comune_step3">Comune</label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                                <div class="dropdown bootstrap-select form-control kt-" id="comune_step3_div" style="padding: 0;">
                                                                                    <select class="form-control kt-select2-general obbligatory" id="comune_step3" name="comune_step3"  style="width: 100%;">
                                                                                        <option value="-">Seleziona Comune</option>
                                                                                    </select>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                        <div class="row"  style="width: 100%;">
                                                                            <div class="form-group col-6">
                                                                                <label for="indirizzo_step3">Indirizzo</label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                                <input type="text" class="form-control obbligatory" name="indirizzo_step3" id="indirizzo_step3" placeholder="Indirizzo sede amministrativa/legale">
                                                                            </div>
                                                                            <div class="form-group col-6">
                                                                                <label for="civico_step3">Civico</label>
                                                                                <input type="text" class="form-control" name="civico_step3" id="civico_step3" placeholder="Civico indirizzo">
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="kt-section__content kt-section__content--solid text-center">
                                                                        <%if (m6_loaded) {%>
                                                                        <button id="dichiarazione_<%=p.getId()%>" type="button" declar="loaded" class="btn btn-success"><i class="fa fa-check-circle"></i>&nbsp;Cambia</button>&nbsp;
                                                                        <label style="display: flex; margin-top: 1.5rem" class="kt-font-danger kt-font-bold"><font size="2" >E' possibile proseguire avendo già salvato la dichiarazione</font></label>
                                                                            <%} else {%>
                                                                        <button id="dichiarazione_<%=p.getId()%>" declar="toload" type="button" class="btn btn-io-n"><i class="fa fa-check-circle"></i>&nbsp;Salva</button>&nbsp;
                                                                        <label style="display: flex; margin-top: 1.5rem" class="kt-font-danger kt-font-bold"><font size="2" >Per poter proseguire è necessario salvare la dichiarazione</font></label>
                                                                            <%}%>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <!--step: 4-->
                                                        <div class="kt-wizard-v1__content" id="step4" data-ktwizard-type="step-content">
                                                            <div class="kt-form__section kt-form__section--first">
                                                                <div class="kt-wizard-v1__form" style="color: #6c7293;">
                                                                    <div class="accordion  accordion-toggle-arrow">
                                                                        <div class="card" style="border-radius: 17px; margin-block: 20px; color: #363a90;">
                                                                            <div class="card-header">
                                                                                <div class="card-title loaded">
                                                                                    <i class="fa fa-file-pdf kt-font-success"></i>&nbsp;<i class="fa fa-users kt-font-success"></i> MODELLO 6
                                                                                    <a href="<%=request.getContextPath()%>/OperazioniSA?type=scaricamodello6temp&idpr=<%=p.getId()%>" target="_blank" class="btn"><i class="fa fa-file-download kt-font-success" style='position: absolute; right: 1rem' data-container="body" data-html="true" data-toggle="kt-tooltip" data-placement="top" title="<h5>Scarica il Modello 6</h5>"></i></a>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="kt-section__content kt-section__content--solid text-center">

                                                                        <label><%=m6.getDescrizione()%></label><%=m6.getObbligatorio() == 1 ? "<label class='kt-font-danger kt-font-boldest'>*</label>" : ""%>
                                                                        <input type="hidden" class="hidden" name="modello" id="modello_<%=m6.getId()%>" value="0"/>
                                                                        <div class="custom-file">
                                                                            <input type="file" <%=m6.getObbligatorio() == 1 ? "tipo='obbligatory'" : ""%> 
                                                                                   class="custom-file-input" 
                                                                                   accept="<%=m6.getMimetype()%>" name="doc_step4_<%=m6.getId()%>" id="doc_step4_<%=m6.getId()%>" 
                                                                                   onchange="return checkFileExtAndDim('<%=m6.getEstensione()%>');">
                                                                            <label class="custom-file-label selected" 
                                                                                   style="color: #a7abc3; text-align: left;">Scegli File</label>
                                                                        </div>

                                                                        <label style="display: flex; margin-top:1.5rem;" class="kt-font-io kt-font-bold"><font size="2">Il presente Modello 6 generato dal sistema, va scaricato (dal tasto <i class="fa fa-file-download kt-font-success"></i> ), firmato digitalmente (.p7m CAdES, .pdf PAdES) e ricaricato.</font></label>
                                                                        <label style="display: flex; margin-top:0.5rem;" class="kt-font-io kt-font-bold"><font size="2" >Una volta effettuate queste operazioni, si può procedere con la conclusione del Progetto formativo.</font></label>

                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>                    
                                                        <!--step: final-->
                                                        <div class="kt-form__actions">
                                                            <div class="btn btn-io-n btn-md btn-tall btn-wide kt-font-bold kt-font-transform-u" data-ktwizard-type="action-prev">
                                                                Indietro
                                                            </div>
                                                            <div class="btn btn-io btn-md btn-tall btn-wide kt-font-bold kt-font-transform-u" 
                                                                 data-ktwizard-type="action-submit">
                                                                Concludi Progetto Formativo
                                                            </div>
                                                            <div id="go_next" class="btn btn-io btn-md btn-tall btn-wide kt-font-bold kt-font-transform-u" data-ktwizard-type="action-next">
                                                                Avanti
                                                            </div>
                                                        </div>
                                                        <hr/>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>                        
                                    </div>
                                </div>
                                <%if (fancy) {%>
                                <%@ include file="menu/footer.jsp"%>
                                <%}%>
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
        <script src="<%=src%>/assets/app/bundle/app.bundle.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/bundle/app.bundle.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/general/components/extended/blockui1.33.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/soop/js/utility.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/jquery-form/dist/jquery.form.min.js" type="text/javascript"></script>
        <!--this page-->
        <script src="<%=src%>/assets/vendors/general/select2/dist/js/select2.full.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/select2.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/bootstrap-daterangepicker.js" type="text/javascript"></script><!--usa questo per modificare daterangepicker-->
        <script src="<%=src%>/assets/vendors/general/bootstrap-daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/wizard/wizard-concludiPrg.js" type="text/javascript" id="wizCp" data-context="<%=request.getContextPath()%>"></script>
        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/bootstrap-datepicker.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/js/bootstrap-datepicker.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/input-mask.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/inputmask/dist/inputmask/inputmask.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/inputmask/dist/inputmask/jquery.inputmask.js" type="text/javascript"></script>

        <script id="concludiPrg" src="<%=src%>/page/sa/js/concludiPrg.js<%="?dummy=" + String.valueOf(new Date().getTime())%>" data-context="<%=request.getContextPath()%>" type="text/javascript"></script>
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
