<!DOCTYPE HTML>
<%@page import="rc.so.domain.Revisori"%>
<%@page import="rc.so.domain.Docenti"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="rc.so.domain.checklist_finale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="rc.so.domain.SoggettiAttuatori"%>
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Entity e = new Entity();
            String msg = e.getPath("compilaCL.html").replaceAll("\\\\", "/");
            ProgettiFormativi p = e.getEm().find(ProgettiFormativi.class, Long.parseLong(request.getParameter("id")));
            int allievi_Patto = 0;
            int allievi_Gol = 0;
            List<Allievi> allievi_totali = e.getAllieviProgettiFormativiAll(p);
            List<Allievi> allievi_OK = e.getAllieviProgettiFormativi(p);
            List<Docenti> docenti_tab = Utility.docenti_A(e, p);
            int soglia = Utility.parseIntR(e.getPath("soglia.allegato7"));
            String coeff_fa = e.getPath("coeff.allievo.fasea");
            String coeff_fb = e.getPath("coeff.allievo.faseb");
            List<Revisori> controllori = e.findAll(Revisori.class);
            Map<String, String> fasceDocenti = Utility.mapCoeffDocenti(e.getPath("coeff.docente.a"), e.getPath("coeff.docente.b"));
            e.close();

            int allieviOK = allievi_OK.size();
            int allieviOKATT = Utility.allieviOKattestato(allievi_OK, soglia);

            boolean fancy = request.getParameter("fb") != null && request.getParameter("fb").equals("1") ? false : true;%>
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

            .form-control:disabled, .form-control[readonly] {
                background-color: #e8f0ff;
                opacity: 1;
            }

            a.disablelink {
                opacity: 0.65;
            }

            .kt-switch.kt-switch--outline.kt-switch--info input:checked ~ span::after {
                background-color: #4279ea;
                opacity: 1;
            }

            .kt-switch.kt-switch--outline.kt-switch--info input:empty ~ span::after {
                color: #10034f;
                font-weight: bold;
            }

            .kt-switch.kt-switch--outline.kt-switch--info input:empty ~ span::before {
                border: 2px solid #2857b7;
                background-color: #e8ebf1;
            }

            .kt-switch input[disabled] ~ span::after, .kt-switch input[disabled] ~ span::before {
                cursor: not-allowed;
                border: 2px solid #e8ebf1;
                opacity: 0.5;
            }

            .info-input{
                background-color: #e8f0ff00!important;
                border: 1px solid #fff0;
                border-radius: 0px;
                border-bottom: 1px solid #363a90;
                padding-left: 0px!important;
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
                                    <a class="kt-subheader__breadcrumbs-link">Rendicontazione</a>
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
                                        <div class="row">
                                            <div class="alert alert-info col-12" style="text-align: center; display: flow-root; background: #363a90; border: 1px solid #363a90;">
                                                <%=msg%>
                                            </div>
                                        </div>
                                        <div class="row" style="height: 100%;">
                                            <div class="kt-grid  kt-wizard-v1 kt-wizard-v1--white" id="kt_wizard_v1" data-ktwizard-state="step-first" style="width: 100%;">
                                                <div class="kt-grid__item" style="">
                                                    <!--begin: Form Wizard Nav -->
                                                    <div class="kt-wizard-v1__nav">
                                                        <div class="kt-wizard-v1__nav-items" style="background: #fff; border-top-left-radius: 10px; border-top-right-radius: 10px;">
                                                            <a class="kt-wizard-v1__nav-item" href="#" data-ktwizard-type="step" data-ktwizard-state="current">
                                                                <div class="kt-wizard-v1__nav-body " >
                                                                    <div class="kt-wizard-v1__nav-icon">
                                                                        <i class="fa fa-address-card" ></i>
                                                                    </div>
                                                                    <div class="kt-wizard-v1__nav-label text-uppercase">
                                                                        1 - Anagrafica corso
                                                                    </div>
                                                                </div>
                                                            </a>
                                                            <a class="kt-wizard-v1__nav-item" href="#" data-ktwizard-type="step">
                                                                <div class="kt-wizard-v1__nav-body">
                                                                    <div class="kt-wizard-v1__nav-icon">
                                                                        <i class="fa fa-calculator"></i>
                                                                    </div>
                                                                    <div class="kt-wizard-v1__nav-label text-uppercase">
                                                                        2 - Calcolo contributo
                                                                    </div>
                                                                </div>
                                                            </a>
                                                            <a class="kt-wizard-v1__nav-item" href="#" data-ktwizard-type="step">
                                                                <div class="kt-wizard-v1__nav-body">
                                                                    <div class="kt-wizard-v1__nav-icon">
                                                                        <i class="fa fa-pen-alt"></i>
                                                                    </div>
                                                                    <div class="kt-wizard-v1__nav-label  text-uppercase">
                                                                        3 - Conformità e completezza output
                                                                    </div>
                                                                </div>
                                                            </a>
                                                            <a class="kt-wizard-v1__nav-item" href="#" data-ktwizard-type="step">
                                                                <div class="kt-wizard-v1__nav-body">
                                                                    <div class="kt-wizard-v1__nav-icon">
                                                                        <i class="fa fa-flag-checkered"></i>
                                                                    </div>
                                                                    <div class="kt-wizard-v1__nav-label  text-uppercase">
                                                                        4 - Riepilogo
                                                                    </div>
                                                                </div>
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>
                                                <!--end: Form Wizard Nav -->
                                                <div class="kt-grid__item kt-grid__item--fluid kt-wizard-v1__wrapper">
                                                    <form id="kt_form" action="<%=request.getContextPath()%>/OperazioniMicro?type=checklistFinale" 
                                                          style="padding: 0;" class="kt-form kt-form--label-right"
                                                          accept-charset="ISO-8859-1" method="post" enctype="multipart/form-data">
                                                        <input type="hidden" class="hidden" name="pf" id="pf" value="<%=p.getId()%>" />
                                                        <input type="hidden" class="hidden" name="cip" id="cip" value="<%=p.getCip()%>" />
                                                        <input type="hidden" class="hidden" name="currentstep" id="currentstep"/>
                                                        <!--step: 1-->
                                                        <div class="kt-wizard-v1__content" id="step1" data-ktwizard-type="step-content" data-ktwizard-state="current">
                                                            <div class="kt-form__section kt-form__section--first">
                                                                <div class="kt-wizard-v1__form kt-font-io" style="min-height: 40vh">
                                                                    <div class="row">
                                                                        <div class="form-group col-md-4 col-lg-4 col-sm-12">
                                                                            <label>Soggetto Esecutore sottoposto a controllo</label>
                                                                            <div class="input-group input-group-md">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text">
                                                                                        <i class="fa fa-user kt-font-io" aria-hidden="true"></i>
                                                                                    </span>
                                                                                </div>
                                                                                <input type="text" disabled class="form-control kt-font-io" style="font-weight: 500;" value="<%=p.getSoggetto().getRagionesociale()%>">
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group col-md-4 col-lg-4 col-sm-12">
                                                                            <label>Protocollo Soggetto Esecutore</label>
                                                                            <div class="input-group input-group-md">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text">
                                                                                        <i class="fa fa-info kt-font-io" aria-hidden="true"></i>
                                                                                    </span>
                                                                                </div>
                                                                                <input type="text" disabled class="form-control kt-font-io" style="font-weight: 500;" value="<%=p.getSoggetto().getProtocollo()%>">
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group col-md-4 col-lg-4 col-sm-12">
                                                                            <label>Codice Identificativo Percorso</label>
                                                                            <div class="input-group input-group-md">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text">
                                                                                        <i class="fa fa-info kt-font-io" aria-hidden="true"></i>
                                                                                    </span>
                                                                                </div>
                                                                                <input type="text" disabled class="form-control kt-font-io" style="font-weight: 500;" value="<%=p.getCip()%>">
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">        
                                                                        <div class="form-group col-md-12 col-lg-12 col-sm-12">
                                                                            <label>Codice Fiscale / Partita IVA Soggetto Esecutore</label>
                                                                            <div class="input-group input-group-md">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text">
                                                                                        <i class="fa fa-info kt-font-io" aria-hidden="true"></i>
                                                                                    </span>
                                                                                </div>
                                                                                <input type="text" disabled class="form-control kt-font-io" style="font-weight: 500;" value="<%=p.getSoggetto().getPiva()%>">
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">        
                                                                        <div class="form-group col-md-6 col-lg-6 col-sm-12">
                                                                            <label>Email Soggetto Esecutore</label>
                                                                            <div class="input-group input-group-md">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text">
                                                                                        <i class="fa fa-mail-bulk kt-font-io" aria-hidden="true"></i>
                                                                                    </span>
                                                                                </div>
                                                                                <input type="text" disabled class="form-control kt-font-io" style="font-weight: 500;" value="<%=p.getSoggetto().getEmail()%>">
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group col-md-6 col-lg-6 col-sm-12">
                                                                            <label>PEC Soggetto Esecutore</label>
                                                                            <div class="input-group input-group-md">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text">
                                                                                        <i class="fa fa-mail-bulk kt-font-io" aria-hidden="true"></i>
                                                                                    </span>
                                                                                </div>
                                                                                <input type="text" disabled class="form-control kt-font-io" style="font-weight: 500;" value="<%=p.getSoggetto().getPec()%>">
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">        
                                                                        <div class="form-group col-md-6 col-lg-6 col-sm-12">
                                                                            <label>Data avvio effettiva</label>
                                                                            <div class="input-group input-group-md">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text">
                                                                                        <i class="fa fa-calendar-check kt-font-io" aria-hidden="true"></i>
                                                                                    </span>
                                                                                </div>
                                                                                <input type="text" disabled class="form-control kt-font-io" style="font-weight: 500;" value="<%=sdf.format(p.getStart())%>">
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group col-md-6 col-lg-6 col-sm-12">
                                                                            <label>Data conclusione effettiva</label>
                                                                            <div class="input-group input-group-md">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text">
                                                                                        <i class="fa fa-calendar-check kt-font-io" aria-hidden="true"></i>
                                                                                    </span>
                                                                                </div>
                                                                                <input type="text" disabled class="form-control kt-font-io" style="font-weight: 500;" value="<%=sdf.format(p.getEnd())%>">
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">        
                                                                        <div class="form-group col-md-4 col-lg-4 col-sm-12">
                                                                            <label>Numero allievi iscritti</label>
                                                                            <div class="input-group input-group-md">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text">
                                                                                        <i class="fa fa-users kt-font-io" aria-hidden="true"></i>
                                                                                    </span>
                                                                                </div>
                                                                                <input type="text" disabled class="form-control kt-font-io" style="font-weight: 500;" value="<%=allievi_totali.size()%>">
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group col-md-4 col-lg-4 col-sm-12">
                                                                            <label>Numero allievi che hanno terminato il percorso</label>
                                                                            <div class="input-group input-group-md">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text">
                                                                                        <i class="fa fa-users kt-font-io" aria-hidden="true"></i>
                                                                                    </span>
                                                                                </div>
                                                                                <input type="text" disabled class="form-control kt-font-io" style="font-weight: 500;" value="<%=allieviOK%>">
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group col-md-4 col-lg-4 col-sm-12">
                                                                            <label>Numero allievi che hanno ricevuto l'Attestato Finale</label>
                                                                            <div class="input-group input-group-md">
                                                                                <div class="input-group-prepend">
                                                                                    <span class="input-group-text">
                                                                                        <i class="fa fa-users kt-font-io" aria-hidden="true"></i>
                                                                                    </span>
                                                                                </div>
                                                                                <input type="text" disabled class="form-control kt-font-io" 
                                                                                       style="font-weight: 500;" value="<%=allieviOKATT%>">
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="kt-separator kt-separator--border kt-separator--space-xs" style="margin-top: 2rem; margin-bottom: 2rem;"></div>
                                                                    <div class="row">
                                                                        <div class="form-group col-12 form-group-marginless">
                                                                            <div class="row">
                                                                                <div class="col-12 row">
                                                                                    <div class="col-lg-5 col-md-10 col-sm-8">
                                                                                        <label class="kt-option">
                                                                                            <span class="kt-option__control">
                                                                                                <span class="kt-radio kt-radio--bold kt-radio--brand kt-radio--check-bold">
                                                                                                    <input type="radio" name="step1_switch" value="DURC">
                                                                                                    <span></span>
                                                                                                </span>
                                                                                            </span>
                                                                                            <span class="kt-option__label">
                                                                                                <span class="kt-option__head">
                                                                                                    <span class="kt-option__title">
                                                                                                        Regolarità DURC
                                                                                                    </span>
                                                                                                </span>
                                                                                                <span class="kt-option__body">
                                                                                                    Caricare il file in formato .pdf, .p7m
                                                                                                </span>
                                                                                            </span>
                                                                                        </label>
                                                                                    </div>
                                                                                    <div class="col-lg-1 col-md-2 col-sm-4 kt-align-left">
                                                                                        <a target="_blank" class="btn btn-success btn-icon kt-font-bold" id="show_file_DURC" style="display: none; float:left" data-container="body" data-html="true" data-toggle="kt-tooltip" data-placement="top" title="<h5>Visualizza file caricato</h5>"><i class="fa fa-file-pdf"></i></a>
                                                                                    </div>    
                                                                                    <div class="col-lg-2 col-md-12 col-sm-12">
                                                                                        &nbsp;
                                                                                    </div>
                                                                                    <div class="col-lg-4 col-md-12 col-sm-12">
                                                                                        <div class="custom-file">
                                                                                            <input type="file" class="custom-file-input" accept="application/pdf" tipo="obbligatory" id="DURC_file" name="DURC_file" onchange="return checkFileExtAndDim('pdf');">
                                                                                            <label class="custom-file-label selected" style="text-align: left!important;" name="label_DURC_file">Scegli File</label>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                                <div class="col-lg-12 row">
                                                                                    <div class="col-lg-5 col-md-10 col-sm-8">
                                                                                        <label class="kt-option">
                                                                                            <span class="kt-option__control">
                                                                                                <span class="kt-radio kt-radio--bold kt-radio--brand">
                                                                                                    <input type="radio" name="step1_switch" value="ASSENZA">
                                                                                                    <span></span>
                                                                                                </span>
                                                                                            </span>
                                                                                            <span class="kt-option__label">
                                                                                                <span class="kt-option__head">
                                                                                                    <span class="kt-option__title">
                                                                                                        Assenza posizione INPS-INAIL
                                                                                                    </span>
                                                                                                </span>
                                                                                                <span class="kt-option__body">
                                                                                                    Scaricare il file generato dalla piattaforma, firmarlo digitalmente e ricaricarlo.
                                                                                                </span>
                                                                                            </span>
                                                                                        </label>
                                                                                    </div>
                                                                                    <div class="col-lg-1 col-md-2 col-sm-4 kt-align-left">
                                                                                        <a target="_blank" class="btn btn-success btn-icon kt-font-bold" id="show_file_ASSENZA" style="display: none; float:left" data-container="body" data-html="true" data-toggle="kt-tooltip" data-placement="top" title="<h5>Visualizza file caricato</h5>"><i class="fa fa-file-pdf"></i></a>
                                                                                    </div>    
                                                                                    <div class="col-lg-2 col-md-12 col-sm-12 kt-align-right" style="float: right;">
                                                                                        <a href="<%=request.getContextPath()%>/OperazioniMicro?type=scaricaFileAssenza&idpr=<%=request.getParameter("id")%>" id="scarica_ASSENZA_file" 
                                                                                           target="_blank" class="btn btn-io btn-icon kt-font-bold disablelink" data-container="body" data-html="true" 
                                                                                           data-toggle="kt-tooltip" data-placement="top" title="<h5>Scarica Autocertificazione generata dalla piattaforma</h5>">
                                                                                            <i class="fa fa-download"></i>
                                                                                        </a>
                                                                                    </div>
                                                                                    <div class="col-lg-4 col-md-12 col-sm-12">
                                                                                        <div class="custom-file">
                                                                                            <input type="file" class="custom-file-input" accept="application/pdf" tipo="obbligatory" id="ASSENZA_file" name="ASSENZA_file" onchange="return checkFileExtAndDim('pdf');">
                                                                                            <label class="custom-file-label selected" style="text-align: left!important;" name="label_ASSENZA_file">Scegli File</label>
                                                                                        </div>
                                                                                    </div>
                                                                                </div>    
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="kt-section__content kt-section__content--solid text-center" style="margin-top:3rem;">
                                                                        <a id="save_step1" href="javascript:void(0);" class="btn btn-success" style="font-family: Poppins;"><i class="fa fa-check"></i> Salva</a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <label class="kt-font-success kt-font-bold" id="step1_ok" style="display: none; padding-top: 1rem;"><font size="2"><i class="fa fa-check kt-font-success"></i>&nbsp;Caricamento dati Step 1 completato</font></label>
                                                            <label class="kt-font-danger kt-font-bold" id="step1_ko" style="display: none; padding-top: 1rem;"><font size="2">Procedere con il caricamento dei dati per poter accedere allo Step 2</font></label>
                                                        </div>
                                                        <!--step: 2-->
                                                        <div class="kt-wizard-v1__content" id="step2" data-ktwizard-type="step-content">
                                                            <div class="kt-form__section kt-form__section--first">
                                                                <div class="kt-wizard-v1__form" style="color: #6c7293;">
                                                                    <label class="text-uppercase" style="color:#b9003d; font-weight: 800;">Calcolo importi da liquidare FASE A</label>
                                                                    <div class="form-group" id="allievi_faseA">
                                                                        <div class="row">
                                                                            <div class="col-2"><b>Allievo</b></div>
                                                                            <div class="col-2"><b>Patto del Lavoro / GOL</b></div>
                                                                            <div class="col-2"><b>Ore Presenza Allievi</b></div>
                                                                            <div class="col-2"><b>Controllo Ore Presenze Allievo</b></div>
                                                                            <div class="col-2"><b>Importo Orario Riconosciuto</b></div>
                                                                            <div class="col-2"><b>Totale</b></div>
                                                                        </div>
                                                                        <%for (Allievi al : allievi_totali) {
                                                                                if (al.getTos_tipofinanziamento().startsWith("PA")) {
                                                                                    allievi_Patto++;
                                                                                } else {
                                                                                    allievi_Gol++;
                                                                                }
                                                                        %>
                                                                        <div class="row" id="farow_<%=al.getId()%>">
                                                                            <div class="col-2"><%=al.getNome()%> <%=al.getCognome()%></div>
                                                                            <div class="col-2"><%=al.getTos_tipofinanziamento()%></div>
                                                                            <div class="col-2"><input style="background-color: rgb(237, 243, 255)!important;" class="form-control decimal_custom" 
                                                                                                      disabled="disabled" type="text" 
                                                                                                      value="<%=al.getOrec_fasea()%>" name="fa_ore_<%=al.getId()%>" 
                                                                                                      id="fa_ore_<%=al.getId()%>" /></div>
                                                                            <div class="col-2"><input class="form-control decimal_custom obbligatory kt-font-bold kt-font-danger ctrl" type="text" 
                                                                                                      value="<%=al.getOrec_fasea()%>" name="fa_controllo_ore_<%=al.getId()%>" id="fa_controllo_ore_<%=al.getId()%>" /></div>
                                                                            <div class="col-2"><input style="background-color: rgb(237, 243, 255)!important;" class="form-control decimal_custom"
                                                                                                      disabled="disabled" type="text" value="<%=coeff_fa%>" name="fa_coeff_<%=al.getId()%>" id="fa_coeff_<%=al.getId()%>" /></div>
                                                                            <div class="col-2"><input style="background-color: rgb(237, 243, 255)!important;" class="form-control decimal_custom tot_splitter"
                                                                                                      disabled="disabled" type="text" name="fa_tot_<%=al.getId()%>" id="fa_tot_<%=al.getId()%>" 
                                                                                                      data-tipo="<%=al.getTos_tipofinanziamento()%>" /></div>
                                                                        </div>
                                                                        <%}%>
                                                                        <div class="row">
                                                                            <div class="col-10 kt-align-right"><b>Totale contributo Fase A</b></div>
                                                                            <div class="col-2"><input style="background-color: rgb(219, 230, 253)!important;font-weight: 600;" 
                                                                                                      class="form-control decimal_custom totalref kt-font-io" disabled="disabled" type="text" 
                                                                                                      id="fa_total" name="fa_total" /></div>
                                                                        </div>
                                                                        <div class="row">
                                                                            <div class="col-10 kt-align-right"><b>Totale contributo Fase A - GOL</b></div>
                                                                            <div class="col-2"><input style="background-color: rgb(219, 230, 253)!important;font-weight: 600;" 
                                                                                                      class="form-control decimal_custom totalref kt-font-io" disabled="disabled" type="text" 
                                                                                                      id="fa_total_G" name="fa_total_G" /></div>
                                                                        </div>
                                                                        <div class="row">
                                                                            <div class="col-10 kt-align-right"><b>Totale contributo Fase A - PATTO</b></div>
                                                                            <div class="col-2"><input style="background-color: rgb(219, 230, 253)!important;font-weight: 600;" 
                                                                                                      class="form-control decimal_custom totalref kt-font-io" disabled="disabled" type="text" 
                                                                                                      id="fa_total_P" name="fa_total_P" /></div>
                                                                        </div>
                                                                    </div> 
                                                                    <label class="text-uppercase" style="color:#b9003d; font-weight: 800;">Calcolo importi da liquidare FASE B</label>
                                                                    <div class="form-group" id="allievi_faseB">
                                                                        <div class="row">
                                                                            <div class="col-2"><b>Allievo</b></div>
                                                                            <div class="col-2"><b>Patto del Lavoro / GOL</b></div>
                                                                            <div class="col-2"><b>Ore Presenza Allievi</b></div>
                                                                            <div class="col-2"><b>Controllo Ore Presenze Allievo</b></div>
                                                                            <div class="col-2"><b>Importo Orario Riconosciuto</b></div>
                                                                            <div class="col-2"><b>Totale</b></div>
                                                                        </div>
                                                                        <%for (Allievi al : allievi_OK) {%>
                                                                        <div class="row" id="fbrow_<%=al.getId()%>">
                                                                            <div class="col-2"><%=al.getNome()%> <%=al.getCognome()%></div>
                                                                            <div class="col-2"><%=al.getTos_tipofinanziamento()%></div>
                                                                            <div class="col-2"><input style="background-color: rgb(237, 243, 255)!important;" class="form-control decimal_custom" 
                                                                                                      disabled="disabled" type="text" 
                                                                                                      value="<%=al.getOrec_faseb()%>" name="fb_ore_<%=al.getId()%>" id="fb_ore_<%=al.getId()%>" /></div>
                                                                            <div class="col-2"><input class="form-control decimal_custom obbligatory kt-font-bold kt-font-danger ctrl" type="text" 
                                                                                                      value="<%=al.getOrec_faseb()%>" name="fb_controllo_ore_<%=al.getId()%>" id="fb_controllo_ore_<%=al.getId()%>" /></div>
                                                                            <div class="col-2"><input style="background-color: rgb(237, 243, 255)!important;" 
                                                                                                      class="form-control decimal_custom"  disabled="disabled" type="text" value="<%=coeff_fb%>" name="fb_coeff_<%=al.getId()%>" id="fb_coeff_<%=al.getId()%>" /></div>
                                                                            <div class="col-2"><input style="background-color: rgb(237, 243, 255)!important;" 
                                                                                                      class="form-control decimal_custom tot_splitter" disabled="disabled" type="text" name="fb_tot_<%=al.getId()%>" id="fb_tot_<%=al.getId()%>" data-tipo="<%=al.getTos_tipofinanziamento()%>" /></div>
                                                                        </div>
                                                                        <%}%>
                                                                        <div class="row">
                                                                            <div class="col-10 kt-align-right"><b>Totale contributo FASE B</b></div>
                                                                            <div class="col-2"><input style="background-color: rgb(219, 230, 253)!important;font-weight: 600;" 
                                                                                                      class="form-control decimal_custom totalref kt-font-io" disabled="disabled" 
                                                                                                      type="text" id="fb_total" name="fb_total" /></div>
                                                                        </div>
                                                                        <div class="row">
                                                                            <div class="col-10 kt-align-right"><b>Totale contributo FASE B - GOL</b></div>
                                                                            <div class="col-2"><input style="background-color: rgb(219, 230, 253)!important;font-weight: 600;" 
                                                                                                      class="form-control decimal_custom totalref kt-font-io" 
                                                                                                      disabled="disabled" type="text" id="fb_total_G" name="fb_total_G" /></div>
                                                                        </div>
                                                                        <div class="row">
                                                                            <div class="col-10 kt-align-right"><b>Totale contributo FASE B - PATTO</b></div>
                                                                            <div class="col-2"><input style="background-color: rgb(219, 230, 253)!important;font-weight: 600;" 
                                                                                                      class="form-control decimal_custom totalref kt-font-io" 
                                                                                                      disabled="disabled" type="text" id="fb_total_P" name="fb_total_P" /></div>
                                                                        </div>
                                                                    </div>   
                                                                    <div class="form-group">
                                                                        <div class="row">
                                                                            <div class="col-6"><b>TOTALE ALLIEVI GOL</b></div>
                                                                            <div class="col-6"><b>TOTALE ALLIEVI PATTO</b></div>
                                                                        </div>
                                                                        <div class="row">
                                                                            <div class="col-6"><b><%=allievi_Gol%></b></div>
                                                                            <div class="col-6"><b><%=allievi_Patto%></b></div>
                                                                        </div>
                                                                        <input type="hidden" id="allievi_gol" name="allievi_gol" value="<%=allievi_Gol%>" />
                                                                        <input type="hidden" id="allievi_pat" name="allievi_pat" value="<%=allievi_Patto%>" />
                                                                    </div>
                                                                    <label class="text-uppercase" style="color:#b9003d; font-weight: 800;">Calcolo importi da liquidare DOCENTI</label>
                                                                    <div class="form-group" id="allievi_faseA">
                                                                        <div class="row">
                                                                            <div class="col-4"><b>Docenti</b></div>
                                                                            <div class="col-2"><b>Ore Presenze Docente</b></div>
                                                                            <div class="col-2"><b>Controllo Ore Presenze Docente</b></div>
                                                                            <div class="col-2"><b>Importo Orario Riconosciuto</b></div>
                                                                            <div class="col-2"><b>Totale</b></div>
                                                                        </div>
                                                                        <%for (Docenti al : docenti_tab) {
                                                                        %>
                                                                        <div class="row" id="dcrow_<%=al.getId()%>">
                                                                            <div class="col-4"><%=al.getNome()%> <%=al.getCognome()%></div>
                                                                            <div class="col-2"><input class="form-control decimal_custom" disabled="disabled" type="text" 
                                                                                                      value="<%=al.getOrec_faseA()%>" name="dc_ore_<%=al.getId()%>" id="dc_ore_<%=al.getId()%>" /></div>
                                                                            <div class="col-2"><input class="form-control decimal_custom obbligatory kt-font-bold kt-font-danger ctrl" type="text" 
                                                                                                      value="<%=al.getOrec_faseA()%>" 
                                                                                                      name="dc_controllo_ore_<%=al.getId()%>" 
                                                                                                      id="dc_controllo_ore_<%=al.getId()%>" /></div>                          
                                                                            <div class="col-2"><input style="background-color: rgb(237, 243, 255)!important;" 
                                                                                                      class="form-control decimal_custom"  disabled="disabled" type="text" 
                                                                                                      value="<%=fasceDocenti.get(al.getFascia().getId())%>" 
                                                                                                      name="dc_coeff_<%=al.getId()%>" id="dc_coeff_<%=al.getId()%>" /></div>
                                                                            <div class="col-2"><input style="background-color: rgb(237, 243, 255)!important;" 
                                                                                                      class="form-control decimal_custom" disabled="disabled" type="text" 
                                                                                                      name="dc_tot_<%=al.getId()%>" id="dc_tot_<%=al.getId()%>" /></div>
                                                                        </div>
                                                                        <%}%>
                                                                        <div class="row">
                                                                            <div class="col-10 kt-align-right"><b>Totale docenza FASE A</b></div>
                                                                            <div class="col-2"><input style="background-color: rgb(219, 230, 253)!important;font-weight: 600;" 
                                                                                                      class="form-control decimal_custom kt-font-io" disabled="disabled" 
                                                                                                      type="text" id="dc_total" name="dc_total" /></div>
                                                                        </div>
                                                                        
                                                                        <div class="row">
                                                                            <div class="col-10 kt-align-right"><b>Totale docenza FASE A - GOL</b></div>
                                                                            <div class="col-2"><input style="background-color: rgb(219, 230, 253)!important;font-weight: 600;" 
                                                                                                      class="form-control decimal_custom kt-font-io" disabled="disabled" 
                                                                                                      type="text" id="dc_total_G" name="dc_total_G" /></div>
                                                                        </div>
                                                                        <div class="row">
                                                                            <div class="col-10 kt-align-right"><b>Totale docenza FASE A - PATTO</b></div>
                                                                            <div class="col-2"><input style="background-color: rgb(219, 230, 253)!important;font-weight: 600;" 
                                                                                                      class="form-control decimal_custom kt-font-io" disabled="disabled" 
                                                                                                      type="text" id="dc_total_P" name="dc_total_P" /></div>
                                                                        </div>
                                                                    </div> 

                                                                    <div class="kt-section__content kt-section__content--solid text-center" style="margin-top:3rem;">
                                                                        <a id="save_step2" href="javascript:void(0);" class="btn btn-success" style="font-family: Poppins;"><i class="fa fa-check"></i> Salva</a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <label class="kt-font-success kt-font-bold" id="step2_ok" style="display: none; padding-top: 1rem;"><font size="2"><i class="fa fa-check kt-font-success"></i>&nbsp;Caricamento dati Step 2 completato</font></label>
                                                            <label class="kt-font-danger kt-font-bold" id="step2_ko" style="display: none; padding-top: 1rem;"><font size="2">Procedere con il caricamento dei dati per poter accedere allo Step 3</font></label>

                                                        </div>
                                                        <!--step: 3-->
                                                        <div class="kt-wizard-v1__content" id="step3" data-ktwizard-type="step-content">
                                                            <div class="kt-form__section kt-form__section--first">
                                                                <div class="kt-wizard-v1__form" style="color: #6c7293;">
                                                                    
                                                                    <label class="text-uppercase" style="color:#b9003d; font-weight: 800;">Conformità/Completezza output allievo e mappatura</label>
                                                                    <div class="row step3class">
                                                                        <div class="form-group col-lg-8 col-md-12 col-sm-12" id="mappatura_allievi">
                                                                            <div class="row">
                                                                                <div class="col-4 kt-align-center"><b>Allievo</b></div>
                                                                                <div class="col-4 kt-align-center"><b>Business Plan</b></div>
                                                                                <div class="col-4 kt-align-center"><b>Output Conforme</b></div>
                                                                            </div>
                                                                            <%for (Allievi al : allievi_OK) {
                                                                            %>
                                                                            <div class="row" id="mappaturarow_<%=al.getId()%>">
                                                                                <div class="col-4 kt-align-center"><%=al.getNome()%> <%=al.getCognome()%></div>
                                                                                <div class="col-4 kt-align-center">    
                                                                                    <a target="_blank" class="btn btn-md kt-font-bold" id="ammissione_<%=al.getId()%>" 
                                                                                       style="color: white; width:100%!important;" data-container="body" data-html="true" 
                                                                                       data-toggle="kt-tooltip" data-placement="top" title=""></a>
                                                                                </div>
                                                                                <div class="col-4 kt-align-center">
                                                                                    <span class="kt-switch kt-switch--outline kt-switch--icon kt-switch--info">
                                                                                        <label>
                                                                                            <input type="checkbox" name="output_<%=al.getId()%>" id="output_<%=al.getId()%>" data-container="body" data-html="true" data-toggle="kt-tooltip" data-placement="top" title="">
                                                                                            <span></span>
                                                                                        </label>
                                                                                    </span>
                                                                                </div>
                                                                            </div>
                                                                            <%}%>
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-md-12 col-sm-12">
                                                                            <label for="nota_controllore"><b>Nota Controllore</b></label>
                                                                            <textarea class="form-control tinyta" maxlength="500" id="nota_controllore" name="nota_controllore" placeholder="Inserire eventuali note" style="white-space: pre-wrap;"></textarea>
                                                                        </div>
                                                                    </div>
                                                                    <br>
                                                                    <div class="form-group">
                                                                        <div class="row" id="totalsrow">
                                                                            <div class="col-lg-6 col-sm-12 col-md-6"><b>Numero Allievi con output conforme</b></div>
                                                                            <div class="col-lg-6 col-sm-12 col-md-6">
                                                                                <input style="background-color: #e8f0ff!important; font-weight: 600;" 
                                                                                       class="form-control kt-font-io" disabled="disabled" 
                                                                                       type="text" id="allievi_output_ok" name="allievi_output_ok" /></div>
                                                                        </div>
                                                                    </div> 
                                                                    <br>
                                                                    <div class="form-group">
                                                                        <div class="row">
                                                                            <div class="col-4"><b>IMPORTO AMMESSO GOL</b></div>
                                                                            <div class="col-4"><b>IMPORTO AMMESSO PATTO DEL LAVORO</b></div>
                                                                            <div class="col-4"><b>IMPORTO AMMESSO TOTALE</b></div>
                                                                        </div>
                                                                        <div class="row" id="totalsrow">
                                                                            <div class="col-4"><input style="background-color: #0abb87!important; font-weight: 600;" class="form-control decimal_custom kt-font-white" 
                                                                                                      readonly type="text" id="tot_gol" name="tot_gol" /></div>
                                                                            <div class="col-4"><input style="background-color: #0abb87!important; font-weight: 600;" class="form-control decimal_custom kt-font-white" 
                                                                                                      readonly type="text" id="tot_pat" name="tot_pat" /></div>
                                                                            <div class="col-4"><input style="background-color: #0abb87!important; font-weight: 600;" class="form-control decimal_custom kt-font-white" 
                                                                                                      readonly type="text" id="tot_tot" name="tot_tot" /></div>
                                                                        </div>
                                                                    </div> 
                                                                    <br>
                                                                    
                                                                    
                                                                    <div class="kt-section__content kt-section__content--solid text-center" style="margin-top:3rem;">
                                                                        <a id="save_step3" href="javascript:void(0);" class="btn btn-success" style="font-family: Poppins;"><i class="fa fa-check"></i> Salva</a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <label class="kt-font-success kt-font-bold" id="step3_ok" style="display: none; padding-top: 1rem;"><font size="2"><i class="fa fa-check kt-font-success"></i>&nbsp;Caricamento dati Step 3 completato</font></label>
                                                            <label class="kt-font-danger kt-font-bold" id="step3_ko" style="display: none; padding-top: 1rem;"><font size="2">Procedere con il caricamento dei dati per poter accedere allo Step 4</font></label>
                                                        </div>
                                                        <!--step: 4-->
                                                        <div class="kt-wizard-v1__content" id="step4" data-ktwizard-type="step-content">
                                                            <div class="kt-form__section kt-form__section--first">
                                                                <div class="kt-wizard-v1__form" style="color: #6c7293;">
                                                                    <div class="row">
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Soggetto Esecutore sottoposto a controllo</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" style="font-weight: 500; color: #e8f0ff00" 
                                                                                   value="<%=p.getSoggetto().getRagionesociale()%>">
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Protocollo Soggetto Esecutore</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" style="font-weight: 500;" value="<%=p.getSoggetto().getProtocollo()%>">
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Codice Identificativo Percorso</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" style="font-weight: 500;" value="<%=p.getCip()%>">
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">        
                                                                        <div class="form-group col-lg-6 col-sm-12 col-md-6">
                                                                            <label>Codice Fiscale / Partita IVA Soggetto Esecutore</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" style="font-weight: 500;" value="<%=p.getSoggetto().getPiva()%>">
                                                                        </div>
                                                                        <div class="form-group col-lg-3 col-sm-12 col-md-6">
                                                                            <label>Email Soggetto Esecutore</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" style="font-weight: 500;" value="<%=p.getSoggetto().getEmail()%>">
                                                                        </div>
                                                                        <div class="form-group col-lg-3 col-sm-12 col-md-6">
                                                                            <label>PEC Soggetto Esecutore</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" style="font-weight: 500;" value="<%=p.getSoggetto().getPec()%>">
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">        
                                                                        <div class="form-group col-lg-6 col-sm-12 col-md-6">
                                                                            <label>Data avvio effettiva</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" style="font-weight: 500;" value="<%=sdf.format(p.getStart())%>">
                                                                        </div>
                                                                        <div class="form-group col-lg-6 col-sm-12 col-md-6">
                                                                            <label>Data conclusione effettiva</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" style="font-weight: 500;" value="<%=sdf.format(p.getEnd())%>">
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">        
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Numero allievi iscritti</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" style="font-weight: 500;" value="<%=allievi_totali.size()%>">
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Numero allievi che hanno terminato il percorso</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" style="font-weight: 500;" value="<%=allieviOK%>">
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Numero allievi che hanno ricevuto l'Attestato Finale</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" style="font-weight: 500;" value="<%=allieviOKATT%>">
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="form-group col-12">
                                                                            <div class="row">
                                                                                <div class="col-5"><label>DOCENTI</label></div>
                                                                                <div class="col-2"><label>Totale € Docenza FASE A</label></div>
                                                                            </div>
                                                                            <%for (Docenti al : docenti_tab) {%>
                                                                            <div class="row">
                                                                                <div class="col-5"><input class="form-control info-input kt-font-io" style="font-weight: 500;" 
                                                                                                          disabled="disabled" type="text" value="<%=al.getNome()%> <%=al.getCognome()%>" /></div>
                                                                                <div class="col-2"><input class="form-control info-input kt-font-io" style="font-weight: 500;" 
                                                                                                          disabled="disabled" type="text" value="-" name="recapdc_<%=al.getId()%>" id="recapdc_<%=al.getId()%>" /></div>
                                                                            </div>
                                                                            <%}%>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">
                                                                        <div class="form-group col-12">
                                                                            <div class="row">
                                                                                <div class="col-3"><label>ALLIEVI</label></div>
                                                                                <div class="col-3"><label>Totale € FASE A</label></div>
                                                                                <div class="col-3"><label>Totale € FASE B</label></div>
                                                                                <div class="col-3"><label>Output Conforme</label></div>
                                                                            </div>
                                                                            <%for (Allievi al : allievi_OK) {%>
                                                                            <div class="row">
                                                                                <div class="col-3"><input class="form-control info-input kt-font-io" style="font-weight: 500;" 
                                                                                                          disabled="disabled" type="text" value="<%=al.getNome()%> <%=al.getCognome()%> (<%=al.getTos_tipofinanziamento()%>)" /></div>
                                                                                <div class="col-3"><input class="form-control info-input kt-font-io" style="font-weight: 500;" 
                                                                                                          disabled="disabled" type="text" value="-" name="recapfa_<%=al.getId()%>" id="recapfa_<%=al.getId()%>" /></div>
                                                                                <div class="col-3"><input class="form-control info-input kt-font-io" style="font-weight: 500;" disabled="disabled" type="text" value="-" name="recapfb_<%=al.getId()%>" id="recapfb_<%=al.getId()%>" /></div>
                                                                                <div class="col-3"><input class="form-control info-input kt-font-io" style="font-weight: 500;" disabled="disabled" type="text" value="NO" name="recapmap_<%=al.getId()%>" id="recapmap_<%=al.getId()%>" /></div>
                                                                            </div>
                                                                            <%}%>
                                                                        </div>
                                                                    </div>
                                                                    <div class="row">        
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Totale Contributo FASE A ALLIEVI PATTO</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" id="recap_totfa_allievi_P" style="font-weight: 500;" />
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Totale Contributo FASE A ALLIEVI GOL</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" id="recap_totfa_allievi_G" style="font-weight: 500;" />
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Totale Contributo FASE A ALLIEVI</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" id="recap_totfa_allievi" style="font-weight: 500;" />
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Totale Contributo FASE B ALLIEVI PATTO</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" id="recap_totfb_allievi_P" style="font-weight: 500;" />
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Totale Contributo FASE B ALLIEVI GOL</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" id="recap_totfb_allievi_G" style="font-weight: 500;" />
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Totale Contributo FASE B ALLIEVI</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" id="recap_totfb_allievi" style="font-weight: 500;" />
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Totale Contributo Fase A DOCENZA PATTO</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" id="recap_totfa_docenti_P" style="font-weight: 500;" />
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Totale Contributo Fase A DOCENZA GOL</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" id="recap_totfa_docenti_G" style="font-weight: 500;" />
                                                                        </div>
                                                                        <div class="form-group col-lg-4 col-sm-12 col-md-4">
                                                                            <label>Totale Contributo Fase A DOCENZA</label>
                                                                            <input type="text" disabled class="form-control info-input kt-font-io" id="recap_totfa_docenti" style="font-weight: 500;" />
                                                                        </div>
                                                                    </div>
                                                                    <div class="kt-separator kt-separator--border kt-separator--space-xs" style="margin-top: 2rem; margin-bottom: 2rem;"></div>

                                                                    <div class="form-group row">
                                                                        <div class="col-lg-4 col-sm-1 col-md-3"></div>
                                                                        <div class="form-group col-lg-4 col-sm-10 col-md-6">
                                                                            <label for="controllore"><b>Controllore</b></label><label class="kt-font-danger kt-font-boldest">*</label>
                                                                            <div class="dropdown bootstrap-select form-control kt-" id="controllore_div" style="padding: 0;">
                                                                                <select class="form-control kt-select2-general obbligatory" id="controllore" name="controllore"  style="width: 100%">
                                                                                    <option selected value="-">Seleziona Controllore</option>
                                                                                    <%for (Revisori i : controllori) {%>
                                                                                    <option value="<%=i.getCodice()%>"><%=i.getDescrizione()%></option>
                                                                                    <%}%>
                                                                                </select>
                                                                            </div>
                                                                        </div>
                                                                        <div class="col-lg-4 col-sm-1 col-md-3"></div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <label class="kt-font-success kt-font-bold" id="step4_ok" style="display: none; padding-top: 1rem;"><font size="2"><i class="fa fa-check kt-font-success"></i>&nbsp;Caricamento checklist finale completato</font></label>
                                                            <label class="kt-font-danger kt-font-bold" id="step4_ko" style="display: none; padding-top: 1rem;"><font size="2">Procedere con la selezione del controllore per poter completare la checklist finale</font></label>

                                                        </div>                    
                                                        <!--step: final-->
                                                        <hr>
                                                        <div class="kt-form__actions row col-md-12">
                                                            <div class="btn btn-io-n btn-md btn-tall btn-wide kt-font-bold kt-font-transform-u" data-ktwizard-type="action-prev">
                                                                Indietro
                                                            </div>
                                                            <div class="btn btn-success btn-md btn-tall btn-wide kt-font-bold kt-font-transform-u" 
                                                                 data-ktwizard-type="action-submit"><i class="fa fa-check"></i> Concludi
                                                            </div>
                                                            <div id="go_next" class="btn btn-io btn-md btn-tall btn-wide kt-font-bold kt-font-transform-u" data-ktwizard-type="action-next">
                                                                Avanti
                                                            </div>
                                                        </div>
                                                        <hr><!-- comment -->
                                                    </form>
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
        <script src="<%=src%>/assets/app/custom/wizard/wizard-compileCL.js<%="?dummy=" + String.valueOf(new Date().getTime())%>" type="text/javascript" id="wizCl" data-context="<%=request.getContextPath()%>"></script>
        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/bootstrap-datepicker.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/js/bootstrap-datepicker.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/input-mask.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/inputmask/dist/inputmask/inputmask.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/inputmask/dist/inputmask/jquery.inputmask.js" type="text/javascript"></script>
        <script src="<%=src%>/resource/jquery.maskMoney.js" type="text/javascript"></script>

        <script src="https://cdn.tiny.cloud/1/<%=Action.getTinyMCE()%>/tinymce/6/tinymce.min.js" referrerpolicy="origin"></script>
        <script id="compileCL" src="<%=src%>/page/mc/js/compileCL.js<%="?dummy=" + String.valueOf(new Date().getTime())%>" data-context="<%=request.getContextPath()%>" type="text/javascript"></script>
        <script type="text/javascript">

                                                                                                $(function () {
                                                                                                    tinymce.init({
                                                                                                        selector: '.tinyta',
                                                                                                        height: 200,
                                                                                                        menubar: false,
                                                                                                        statusbar: false,
                                                                                                        schema: 'html5',
                                                                                                        toolbar1: 'undo redo',
                                                                                                        language: 'it'
                                                                                                    });
                                                                                                });

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
