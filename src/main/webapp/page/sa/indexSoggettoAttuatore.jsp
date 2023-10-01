
<%@page import="rc.so.domain.Allievi"%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="rc.so.util.Utility"%>
<%@page import="java.time.temporal.ChronoUnit"%>
<%@page import="java.time.ZoneId"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="rc.so.domain.DocumentiPrg"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.function.Predicate"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.List"%>
<%@page import="rc.so.domain.StatiPrg"%>
<%@page import="rc.so.domain.ProgettiFormativi"%>
<%@page import="rc.so.db.Entity"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="rc.so.domain.User"%>
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String src = session.getAttribute("src").toString();
            Date today = new Date();
            Date todaynotime = Date.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).atZone(ZoneId.systemDefault()).toInstant());
            List<ProgettiFormativi> FA = new ArrayList();
            List<DocumentiPrg> docs = new ArrayList();
            List<DocumentiPrg> filtered = new ArrayList();
            Entity e = new Entity();
            String messaggio = e.getPath("messageToSA");
            double ore_filter = 0;

            for (ProgettiFormativi p : e.ProgettiSA_Fa(us.getSoggettoAttuatore())) {
                docs = p.getDocumenti();
                filtered = new ArrayList<>();
                ore_filter = 0;
                for (DocumentiPrg o : docs) {
                    if (o.getGiorno() != null && o.getGiorno().equals(todaynotime)) {
                        filtered.add(o);
                        ore_filter += o.getOre();
                    }
                }
                if (filtered.size() < 2 && ore_filter < 5) {
                    FA.add(p);
                }
            }

            e.close();
%>
<html>
    <head>
        <meta charset="utf-8" />
        <title>YES I Start Up - Toscana - Home Page</title>
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
        <link href="<%=src%>/assets/vendors/general/perfect-scrollbar/css/perfect-scrollbar.css" rel="stylesheet" type="text/css" />
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
        <link href="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/css/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
        <link rel="shortcut icon" href="<%=src%>/assets/media/logos/favicon.ico" />
        <style>
            #containerCanvas {
                position: inherit;
                padding-top: 0;
            }
            .kt-portlet .kt-iconbox .kt-iconbox--animate-slow {
                height: 90%;
            }
            .kt-widget27__title{
                font-size: 7vh!important;
            }
            .kt-notification__item {
                border-radius: 5px;
                margin-bottom:1.5rem;
            }
            .kt-notification__item:after{
                color: #a7abc300 !important;
            }
            .kt-iconbox{
                border-radius: 5px;
            }

            .custom-redbox:before{
                font-family: 'Flaticon';
                content: "\f1af";
            }
            .custom-redbox.message:before{
                font-family: 'Flaticon2';
                content: "";
            }
            .custom-greenbox:before{
                font-family: 'Flaticon';
                content: "\f1b7";
            }
            .custom-yellowbox:before{
                font-family: 'Flaticon';
                content: "\f19f";
            }
            .custom-bluebox:before{
                font-family: 'Flaticon2';
                content: "\f126";
            }
            .custom-greenbox.sa:before{
                font-family: 'Flaticon2';
                content: "\f126";
            }
        </style>
    </head>
    <body class="kt-header--fixed kt-header-mobile--fixed kt-subheader--fixed kt-subheader--enabled kt-subheader--solid kt-aside--enabled kt-aside--fixed">
        <!-- begin:: Page -->
        <%@ include file="menu/head1.jsp"%>
        <div class="kt-grid kt-grid--hor kt-grid--root">
            <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--ver kt-page">
                <%@ include file="menu/menu.jsp"%>
                <!-- end:: Aside -->
                <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor kt-wrapper" id="kt_wrapper">
                    <%@ include file="menu/head.jsp"%>
                    <!-- begin:: Footer -->
                    <div class="kt-grid__item kt-grid__item--fluid kt-grid kt-grid--hor" style="background-image: url(<%=src%>/resource/bg.png); background-size: cover;background-position: center; background-color: #fff;">
                        <!-- begin:: Content Head -->
                        <div class="kt-subheader   kt-grid__item" id="kt_subheader">
                            <div class="kt-subheader   kt-grid__item" id="kt_subheader">
                                <div class="kt-subheader__main">
                                    <h3 class="kt-subheader__title">Home</h3>
                                    <span class="kt-subheader__separator kt-subheader__separator--v"></span>
                                    <a class="kt-subheader__breadcrumbs-link">Soggetto Attuatore</a>
                                </div>
                                <div class="kt-portlet__head-toolbar kt-align-right">
                                    <ul class="nav nav-pills nav-pills-lg nav-pills-label nav-pills-bold" style="padding-top: 0.5rem;" role="tablist">
                                        <li class="nav-item">
                                            <a class="nav-link " data-toggle="tab" id="tab2" href="#kt_widget5_tab2_content"  role="tab">
                                                Riepilogo
                                            </a>
                                        </li>
                                        <%if (us.getTipo() == 1) {%>
                                        <li class="nav-item">
                                            <a class="nav-link"  data-toggle="tab" id="tab1" href="#kt_widget5_tab1_content" role="tab">
                                                Dashboard
                                            </a>
                                        </li>
                                        <%}%>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <br>
                        <div class="tab-content" style="margin-right: 10px;">
                            <div class="tab-pane" id="kt_widget5_tab1_content" aria-expanded="true">
                                <div class="row">
                                    <div class="col-md-12" style="padding-right: 0px;">
                                        <%if (!messaggio.equals("")) {%>
                                        <div class="row col">
                                            <div class="col-12 paddig_0_r" style="padding-bottom: 1.5rem;">
                                                <div class="custom-redbox message paddig_0_t"><br><label style="font-size: 1.5rem; font-weight: 200;"><%=messaggio%></label></div>
                                            </div>
                                        </div>
                                        <%}%>
                                        <div class="row flex col-lg-12"  style="margin-right: 0px; padding-right: 0px;">

                                            <%
                                                String[] contatori = Action.contatoriHomeSA(us);
                                            %>

                                            <div class="col-xl-3 col-lg-12 col-md-6" style="padding-bottom: 1.5rem;">
                                                <div class="one-half custom-redbox">Allievi totali<br><label style="font-size: 3rem; font-weight: 800;">
                                                        <%=contatori[0]%></label></div>
                                            </div>
                                            <div class="col-xl-3 col-lg-12 col-md-6" style="padding-bottom: 1.5rem;">
                                                <div class="one-half custom-greenbox">Allievi formati<br><label style="font-size: 3rem; font-weight: 800;">
                                                        <%=contatori[1]%></label></div>
                                            </div>
                                            <div class="col-xl-3 col-lg-12 col-md-6" style="padding-bottom: 1.5rem;">
                                                <div class="one-half custom-yellowbox">Progetti totali<br><label style="font-size: 3rem; font-weight: 800;">
                                                        <%=contatori[2]%></label></div>
                                            </div>
                                            <div class="col-xl-3 col-lg-12 col-md-6" style="padding-bottom: 1.5rem;">
                                                <div class="one-half custom-bluebox">Progetti conclusi<br><label style="font-size: 3rem; font-weight: 800;">
                                                        <%=contatori[3]%></label></div>
                                            </div>
                                        </div>
                                        <%if (Utility.demoversion) {%>
                                        <div class="row flex col-lg-12"  style="margin-right: 0px; padding-right: 0px;">
                                            <div class="col-xl-4 col-lg-12 col-md-6">
                                                <a href="<%=request.getContextPath()%>/OperazioniSA?type=resetdatidemo" 
                                                   class="btn btn-dark kt-font-bold"><i class="fa fa-times"></i> RESET DATI DEMO</a>
                                            </div>
                                        </div>
                                        <%}%>
                                        <div class="row flex col-lg-12"  style="margin-right: 0px; padding-right: 0px;">
                                            <%if (today.after(us.getSoggettoAttuatore().getScadenza())) {%>
                                            <div class="col-xl-4 col-lg-12 col-md-6">
                                                <div class="kt-portlet kt-iconbox kt-iconbox--warning kt-iconbox--animate-slow">
                                                    <div class="kt-portlet__body">
                                                        <div class="row">
                                                            <div class="col-lg-3">
                                                                <h4 class="kt-widget27__title kt-font-io-n" style="font-size: 5vh!important;">
                                                                    <i class="fa fa-exclamation-triangle"></i>
                                                                </h4>
                                                            </div>
                                                            <div class="col-lg-9">
                                                                <div class="kt-iconbox__desc">
                                                                    <h3 class="kt-iconbox__title">
                                                                        <a href="javascript:void(0);" onclick="rinnovoCartaID();" class="kt-link kt-notification__item">Documento Scaduto</a>
                                                                    </h3>
                                                                    <div class="kt-iconbox__content">
                                                                        Carica nuovo documento AD/AU 
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <%}%>


                                        </div>
                                    </div>

                                </div>
                            </div>
                            <br>
                            <div class="tab-pane" id="kt_widget5_tab2_content" aria-expanded="true">
                                <div class="row col-lg-12">
                                    <div class="col-lg-8">
                                        <div class="row">
                                            <div class="col-xl-6 col-lg-12"><br><br>
                                                <h5 class="kt-font-io-n"><b>Soggetto Attuatore</b></h5><br>
                                                <h6><b>Ragione Sociale:</b> <%=us.getSoggettoAttuatore().getRagionesociale()%></h6><br>
                                                <%if (us.getSoggettoAttuatore().getPiva() != null && !us.getSoggettoAttuatore().getPiva().equalsIgnoreCase("")) {%>
                                                <h6><b>Partita IVA:</b> <%=us.getSoggettoAttuatore().getPiva()%></h6><br>
                                                <%}
                                                    if (us.getSoggettoAttuatore().getCodicefiscale() != null && !us.getSoggettoAttuatore().getCodicefiscale().equalsIgnoreCase("")) {%>
                                                <h6><b>Codice Fiscale:</b> <%=us.getSoggettoAttuatore().getCodicefiscale()%></h6><br>
                                                <%}%>
                                                <h6><b>Email:</b> <%=us.getSoggettoAttuatore().getEmail()%></h6><br>
                                                <h6><b>PEC:</b> <%=us.getSoggettoAttuatore().getPec()%></h6><br>
                                                <h6><b>Telefono:</b> <%=us.getSoggettoAttuatore().getTelefono_sa()%></h6><br>
                                                <h6><b>Cellulare:</b> <%=us.getSoggettoAttuatore().getCell_sa()%></h6><br>
                                                <h6><b>Indirizzo:</b> <%=us.getSoggettoAttuatore().getIndirizzo()%></h6><br>
                                                <h6><b>Comune:</b> <%=us.getSoggettoAttuatore().getComune().getNome()%> (<%=us.getSoggettoAttuatore().getCap()%>, <%=us.getSoggettoAttuatore().getComune().getNome_provincia()%>)</h6><br><br><br>
                                            </div>
                                            <div class="col-xl-6 col-lg-12"><br><br>
                                                <h5 class="kt-font-io-n"><b>Amministratore Delegato / Unico</b></h5><br>
                                                <h6><b>Nome:</b> <%=us.getSoggettoAttuatore().getNome()%></h6><br>
                                                <h6><b>Cognome:</b> <%=us.getSoggettoAttuatore().getCognome()%></h6><br>
                                                <h6><b>Data Nascita:</b> <%=sdf.format(us.getSoggettoAttuatore().getDatanascita())%></h6><br>
                                                <h6><b>Numero Documento:</b> <%=us.getSoggettoAttuatore().getNro_documento()%></h6><br>
                                                <h6><b>Scadenza Documento:</b> <%=sdf.format(us.getSoggettoAttuatore().getScadenza())%></h6><br><br><br>
                                                <h5 class="kt-font-io-n"><b>Referente</b></h5><br>
                                                <h6><b>Nome:</b> <%=us.getSoggettoAttuatore().getNome_refente()%></h6><br>
                                                <h6><b>Cognome:</b> <%=us.getSoggettoAttuatore().getCognome_referente()%></h6><br>
                                                <h6><b>Telefono:</b> <%=us.getSoggettoAttuatore().getTelefono_referente()%></h6><br>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-4">
                                        <div class="form-group form-group-sm row" id="div_preview"></div>
                                    </div>
                                </div>
                            </div>
                        </div>      
                        <!-- end:: Content Head -->
                        <a id="chgPwd" href="<%=src%>/page/personal/chgPwd.jsp" class="btn btn-outline-brand btn-sm fancyProfileNoClose" style="display:none;"></a>
                    </div>
                    <%@ include file="menu/footer.jsp"%>
                    <!-- end:: Footer -->
                    <!-- end:: Content -->
                </div>
                <div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true">
                    <button id="showmod1" type="button" class="btn btn-outline-brand btn-sm" data-toggle="modal" data-target="#kt_modal_6">Launch Modal</button>
                </div>
                <div class="modal fade" id="kt_modal_6" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered modal-xl" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="text_modal_title"></h5>
                                <button type="button" id='close_kt_modal_6' class="close" data-target="#kt_modal_6" data-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body" id="text_modal_html"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- end:: Page -->

        <!-- begin::Quick Panel -->


        <!-- end::Quick Panel -->

        <!-- begin::Scrolltop -->
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
        <script src="<%=src%>/assets/demo/default/base/scripts.bundle.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/bundle/app.bundle.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/soop/js/utility.js" type="text/javascript"></script>

        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/bootstrap-datepicker.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/js/bootstrap-datepicker.js" type="text/javascript"></script>
        <!--end::Global Theme Bundle -->
        <!--begin::Page Vendors(used by this page) -->


        <!--end::Global Theme Bundle -->
        <!--begin::Page Vendors(used by this page) -->
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
                                                                            $('[data-toggle="popover-hover"]').popover({
                                                                                html: true,
                                                                                trigger: 'hover',
                                                                                placement: 'right'
                                                                            });
        </script>
        <script>
            $('.kt-scroll').each(function () {
                const ps = new PerfectScrollbar($(this)[0]);
            });

            function rinnovoCartaID() {
                swal.fire({
                    title: 'Nuovo documento d\'identità AD/AU',
                    html: "<div id='formCartaId'>" +
                            "<div class='custom-file'>" +
                            "<input type='file' tipo='obbligatory' class='custom-file-input' accept='application/pdf' name='cartaid' id='cartaid' onchange='return checkFileExtAndDim([&quot;pdf&quot;])'>" +
                            "<label class='custom-file-label selected' id='label_file'>Seleziona File</label>" +
                            "<div><br><input class='form-control obbligatory' id='numerodoc' name='numerodoc' placeholder='Numero documento'></div>" +
                            "<div><br><input class='form-control dp obbligatory' id='datascadenza' name='datascadenza' placeholder='Data scadenza'></div>" +
                            "<br></div></div>",
                    animation: false,
                    showCancelButton: true,
                    confirmButtonText: '&nbsp;<i class="la la-check"></i>',
                    cancelButtonText: '&nbsp;<i class="la la-close"></i>',
                    cancelButtonClass: "btn btn-io-n",
                    confirmButtonClass: "btn btn-io",
                    customClass: {
                        popup: 'animated bounceInUp'
                    },
                    onOpen: function () {

                        $('#cartaid').on('change', function () {
                            ctrlPdf($('#cartaid'));
                        })
                        var arrows = {
                            leftArrow: '<i class="la la-angle-left"></i>',
                            rightArrow: '<i class="la la-angle-right"></i>'
                        }
                        $('input.dp').datepicker({
                            templates: arrows,
                            orientation: "bottom left",
                            todayHighlight: true,
                            autoclose: true,
                            format: 'dd/mm/yyyy',
                            startDate: new Date(),
                        });
                    },
                    preConfirm: function () {
                        var err = false;
                        err = !checkRequiredFileContent($('#formCartaId')) ? true : err;
                        err = checkObblFieldsContent($('#formCartaId')) ? true : err;
                        if (!err) {
                            return new Promise(function (resolve) {
                                //                                formCartaId = $('#formCartaId');
                                resolve({
                                    "numerodoc": $('#numerodoc').val(),
                                    "datascadenza": $('#datascadenza').val(),
                                    "cartaid": $('#cartaid')[0].files[0]
                                });
                            });
                        } else {
                            return false;
                        }
                    },
                }).then((result) => {
                    if (result.value) {
                        showLoad();
                        var fdata = new FormData();
                        fdata.append("cartaid", result.value.cartaid);
                        fdata.append("numerodoc", result.value.numerodoc);
                        fdata.append("datascadenza", result.value.datascadenza);
                        upDoc(fdata);
                    } else {
                        swal.close();
                    }
                }
                );
            }

            function upDoc(fdata) {
                $.ajax({
                    type: "POST",
                    url: "<%=request.getContextPath()%>/OperazioniSA?type=updtCartaIDAD",
                    data: fdata,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        console.log(data);
                        var json = JSON.parse(data);
                        if (json.result) {
                            swalSuccessReload("Documento d'identità AD/AU", json.message);
                        } else {
                            swalError("Errore", json.message);
                        }
                    },
                    error: function () {
                        swalError("Errore", "Non è stato possibile caricare il documento d'identità");
                    }
                });
            }

            $(document).ready(function () {
                $.get("<%=request.getContextPath()%>/OperazioniGeneral?type=pdfTob64", function (data) {
                    $("<iframe />", {
                        "src": "data:application/pdf;base64," + data,
                        "width": '100%',
                        "height": '800px',
                        "type": "application/pdf",
                        css: {
                            "margin": 5,
                            "border": 0,
                            "border-radius": 10,
                        }
                    }).appendTo($('#div_preview'));
                });
            }); //div_preview


            function fancyBoxClose() {
                $('div.fancybox-overlay.fancybox-overlay-fixed').css('display', 'none');
            }

            jQuery(document).ready(function () {
            <%if (us.getStato() == 2) {%>
                $('#chgPwd')[0].click();
            <%}%>
            <%if (us.getTipo() == 3) {%>
                $('#tab2').trigger("click");
                $('#infoko').css("display", "");
            <%} else {%>
                $('#tab1').trigger("click");
                $('#infook').css("display", "");
            <%}%>
            });

            <%if (request.getParameter("fileNotFound") != null) {%>
            swalError("<h2>File Non Trovato<h2>", "<h4>Il file richiesto non esiste.</h4>");
            <%}%>

        </script>
    </body>
</html>
<%
        }
    }
%>
