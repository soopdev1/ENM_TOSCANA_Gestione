
<%@page import="rc.so.domain.CPI"%>
<%@page import="rc.so.db.Entity"%>
<%@page import="java.util.List"%>
<%@page import="rc.so.domain.SoggettiAttuatori"%>
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
            Long idsa = 0L;
            if (request.getParameter("idsa") != null) {
                idsa = Long.parseLong(request.getParameter("idsa"));
            }
            Entity e = new Entity();
            List<CPI> cpi = e.listaCPI();
            List<SoggettiAttuatori> sa_list = e.findAll(SoggettiAttuatori.class);
            e.close();
            String src = session.getAttribute("src").toString();
%>
<html>
    <head>
        <meta charset="utf-8" />
        <title>YES I Start Up - Toscana - Allievi</title>
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
        <link href="<%=src%>/assets/vendors/general/bootstrap-select/dist/css/bootstrap-select.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/select2/dist/css/select2.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/resource/datatbles.bundle.css" rel="stylesheet" type="text/css"/>
        <!-- - -->
        <link href="<%=src%>/assets/vendors/general/perfect-scrollbar/css/perfect-scrollbar.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.css" rel="stylesheet" type="text/css" />
        <link href="<%=src%>/assets/vendors/general/socicon/css/socicon.css" rel="stylesheet" type="text/css" />
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
                                    <h3 class="kt-subheader__title">Allievi</h3>
                                    <span class="kt-subheader__separator kt-subheader__separator--v"></span>
                                    <a class="kt-subheader__breadcrumbs-link">Cerca</a>
                                </div>
                            </div>
                        </div>
                        <div class="kt-content  kt-grid__item kt-grid__item--fluid" id="kt_content">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="kt-portlet" id="kt_portlet" data-ktportlet="true"><!--io-background-->
                                        <div class="kt-portlet__head">
                                            <div class="kt-portlet__head-label">
                                                <h3 class="kt-portlet__head-title" >
                                                    Cerca :
                                                </h3>
                                            </div>
                                            <div class="kt-portlet__head-toolbar">
                                                <div class="kt-portlet__head-group">
                                                    <a href="#" data-ktportlet-tool="toggle" class="btn btn-sm btn-icon btn-clean btn-icon-md"><i class="la la-angle-down" id="toggle_search"></i></a>
                                                </div>
                                            </div>
                                        </div>
                                        <form action="" class="kt-form kt-form--label-right" accept-charset="ISO-8859-1" method="post">
                                            <div class="kt-portlet__body paddig_0_t paddig_0_b">
                                                <div class="kt-section kt-section--first">
                                                    <div class="kt-section__body"><br>
                                                        <div class="form-group row">
                                                            <div class="col-lg-3">
                                                                <label>Soggetto Esecutore</label>
                                                                <div class="dropdown bootstrap-select form-control kt-" id="soggettoattuatore_div" style="padding: 0;height: 35px;">
                                                                    <select class="form-control kt-select2-general" id="soggettoattuatore" name="soggettoattuatore"  style="width: 100%">
                                                                        <option value="-">Seleziona Soggetto Esecutore</option>
                                                                        <%for (SoggettiAttuatori i : sa_list) {%>
                                                                        <%if (i.getId() == idsa) {%>
                                                                        <option selected value="<%=i.getId()%>"><%=i.getRagionesociale()%></option>
                                                                        <%} else {%>
                                                                        <option value="<%=i.getId()%>"><%=i.getRagionesociale()%></option>
                                                                        <%}
                                                                            }%>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="col-lg-3">
                                                                <label>Nome</label>
                                                                <input class="form-control" name="nome" id="nome" autocomplete="off">
                                                            </div>
                                                            <div class="col-lg-3">
                                                                <label>Cognome</label>
                                                                <input class="form-control" name="cognome" id="cognome" autocomplete="off">
                                                            </div>
                                                            <div class="col-lg-3">
                                                                <label>Codice Fiscale</label>
                                                                <input class="form-control" name="cf" id="cf" autocomplete="off">
                                                            </div>
                                                        </div>
                                                        <input type="hidden" name="cpi" value="-" />

                                                    </div>
                                                    <div class="kt-portlet__foot">
                                                        <div class="kt-form__actions">
                                                            <div class="row">
                                                                <div class="offset-lg-6 col-lg-6 kt-align-right">
                                                                    <a onclick="refresh();" href="javascript:void(0);" class="btn btn-io"><font color='white'>Cerca</font></a>
                                                                    <a href="<%=pageName_%>" class="btn btn-io-n"><font color='white'>Reset</font></a>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <div class="row" id="offsetresult">
                                <div class="col-lg-12">
                                    <div class="kt-portlet" id="kt_portlet" data-ktportlet="true">
                                        <div class="kt-portlet__head">
                                            <div class="kt-portlet__head-label col-lg-8">
                                                <div class="col-lg-4">
                                                    <h3 class="kt-portlet__head-title text" >
                                                        Risultati :
                                                    </h3>
                                                </div>
                                            </div>
                                            <div class="kt-portlet__head-toolbar">
                                                <div class="kt-portlet__head-group">
                                                    <a href="#" data-ktportlet-tool="toggle" class="btn btn-sm btn-icon btn-clean btn-icon-md"><i class="la la-angle-down" id="toggle_search"></i></a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="kt-portlet__body kt-scroll-x">
                                            <table class="table table-striped table-bordered " cellspacing="0" id="kt_table_1"style="width:100%;border-collapse: collapse;"> 
                                                <thead>
                                                    <tr>
                                                        <th class="text-uppercase text-center">Azioni</th>
                                                        <th class="text-uppercase text-center">Nome</th>
                                                        <th class="text-uppercase text-center">Cognome</th>
                                                        <th class="text-uppercase text-center">Codice Fiscale</th>
                                                        <th class="text-uppercase text-center">Data Nascita</th>
                                                        <th class="text-uppercase text-center">Residenza</th>
                                                        <th class="text-uppercase text-center">Soggetto Esecutore</th>
                                                        <th class="text-uppercase text-center">Assegnato a Operatore ENM</th>
                                                        <th class="text-uppercase text-center">Stato di partecipazione</th>
                                                    </tr>
                                                </thead>
                                            </table>  
                                        </div>
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
        <!--start:Modal-->
        <div class="modal fade" id="doc_modal" tabindex="-1" role="dialog" aria-labelledby="Documenti Allievo" aria-hidden="true">
            <div class="modal-dialog modal-xl modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Documenti Allievo</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        </button>
                    </div>
                    <div class="modal-body">
                        <div style="text-align: center;">
                            <div class="row col-12" id="prg_docs"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--end:Modal-->
        <script src="<%=src%>/assets/soop/js/jquery-3.6.1.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/popper.js/dist/umd/popper.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/bootstrap/dist/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/js-cookie/src/js.cookie.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/soop/js/moment.min.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/tooltip.js/dist/umd/tooltip.min.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/perfect-scrollbar/dist/perfect-scrollbar.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/sticky-js/dist/sticky.min.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/demo/default/base/scripts.bundle.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/sweetalert2/dist/sweetalert2.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/soop/js/utility.js" type="text/javascript"></script>
        <!-- this page -->
        <script src="<%=src%>/assets/vendors/custom/datatables/datatables.bundle.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/soop/js/loadTable.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/select2/dist/js/select2.full.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/select2.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/custom/general/crud/forms/widgets/bootstrap-datepicker.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/vendors/general/bootstrap-datepicker/dist/js/bootstrap-datepicker.js" type="text/javascript"></script>
        <script src="<%=src%>/assets/app/bundle/app.bundle.js" type="text/javascript"></script>
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
        <script>

            var prg = new Map();
            var context = '<%=request.getContextPath()%>';
            $.getScript(context + '/page/partialView/partialView.js', function () {});

            var KTDatatablesDataSourceAjaxServer = function () {
                let stato;
                var initTable1 = function () {
                    var table = $('#kt_table_1');
                    table.DataTable({
                        dom: `<'row'<'col-sm-12'ftr>><'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7 dataTables_pager'lp>>`,
                        lengthMenu: [5, 10, 25, 50],
                        language: {
                            'lengthMenu': 'Mostra _MENU_',
                            "infoEmpty": "Mostrati 0 di 0 per 0",
                            "loadingRecords": "Caricamento...",
                            "search": "Cerca:",
                            "zeroRecords": "Nessun risultato trovato",
                            "info": "Mostrati _START_ di _TOTAL_ ",
                            "emptyTable": "Nessun risultato",
                            "sInfoFiltered": "(filtrato su _MAX_ risultati totali)"
                        },
                        ScrollX: "100%",
                        sScrollXInner: "110%",
                        searchDelay: 500,
                        processing: true,
                        pageLength: 10,
                        ajax: context + '/QueryMicro?type=searchAllievo&soggettoattuatore=' + $('#soggettoattuatore').val() + '&cf=' + $('#cf').val()
                                + '&nome=' + $('#nome').val() + '&cognome=' + $('#cognome').val() + '&cpi=' + $('#cpi').val() + '&pregresso=0',
                        order: [],
                        columns: [
                            {defaultContent: ''},
                            {data: 'nome', className: 'text-center text-uppercase'},
                            {data: 'cognome', className: 'text-center text-uppercase'},
                            {data: 'codicefiscale', className: 'text-center text-uppercase'},
                            {data: 'datanascita'},
                            {data: 'indirizzoresidenza'},
                            {defaultContent: ''},
                            {data: 'tos_operatore', className: 'text-center'},
                            {data: 'statopartecipazione.descrizione', className: 'text-center'}
                        ],
                        drawCallback: function () {
                            $('[data-toggle="kt-tooltip"]').tooltip();
                        }
                        ,
                        rowCallback: function (row, data) {
                            $(row).attr("id", "row_" + data.id);

                        }
                        ,
                        columnDefs: [
                            {
                                targets: 0,
                                className: 'text-center',
                                orderable: false,
                                render: function (data, type, row, meta) {
                                    var option = '<div class="dropdown dropdown-inline">'
                                            + '<button type="button" class="btn btn-icon btn-sm btn-icon-md btn-circle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
                                            + '   <i class="flaticon-more-1"></i>'
                                            + '</button>'
                                            + '<div class="dropdown-menu dropdown-menu-left">';


                                    option += '<a class="dropdown-item " href="javascript:void(0);" onclick="swalSigma(' + row.id + ',\'' + row.statopartecipazione.id +
                                            '\')"><i class="fa fa-user-check" data-container="body" data-html="true" data-toggle="kt-tooltip" title="Stato '
                                            + row.statopartecipazione.descrizione + '"></i>Cambia stato di partecipazione</a>';


                                    if (row.progetto !== null) {
                                        prg.set(row.progetto.id, row.progetto);
                                        option += '<a class="dropdown-item" href="javascript:void(0);" onclick="showPrg(' + row.progetto.id + ')"><i class="flaticon-presentation-1"></i> Visualizza Progetto Formativo</a>';
                                    }
                                    option += '<a class="dropdown-item" href="javascript:void(0);" onclick="swalDocumentAllievo(' + row.id + ')"><i class="fa fa-file-alt"></i> Visualizza Documenti</a>';
//                                    option += '<a class="dropdown-item" href="#"><i class="fa fa-list"></i> Visualizza Registro</a>';

                                    if (row.tos_operatore === null || row.tos_operatore === 'null') {
                                        option += '<a class="fancyBoxFullReload dropdown-item" href="assegnaENM.jsp?id=' +
                                                row.id + '"><i class="fa fa-user"></i> Assegna ad Operatore</a>';
                                    } else {
                                        option += '<a class="fancyBoxFullReload dropdown-item" href="assegnaENM.jsp?id=' +
                                                row.id + '"><i class="fa fa-edit"></i> Modifica assegnazione Operatore</a>';

                                    }

                                    option += '<a class="dropdown-item" href="javascript:void(0);" onclick="swalMail(' + row.id + ',\'' + row.email + '\')"><i class="fa fa-envelope"></i> Modifica Email</a>';
                                    option += '<a class="fancyBoxFullReload dropdown-item" href="modello0.jsp?id=' +
                                            row.id + '"><i class="fa fa-file"></i> Modello 0</a>';

                                    option += '<a class="fancyBoxFullReload dropdown-item" href="modello0anagr.jsp?id=' +
                                            row.id + '"><i class="fa fa-user"></i> Anagrafica Allievo</a>';

                                    option += '<a class="dropdown-item" href="javascript:void(0);" onclick="uploadDoc(' + row.id + ')"><i class="fa fa-upload"></i> Carica Documentazione Integrativa</a>';

                                    option += '</div></div>';
                                    return option;
                                }

                            }, {
                                targets: 4,
                                className: 'text-center',
                                type: 'date-it',
                                render: function (data, type, row, meta) {
                                    return formattedDate(new Date(row.datanascita));
                                }
                            }, {
                                targets: 5,
                                className: 'text-center',
                                render: function (data, type, row, meta) {
                                    var comune = (row.comune_residenza.nome === null ? "N.I." : row.comune_residenza.nome)
                                            + " (" + (row.comune_residenza.provincia === null ? "N.I." : row.comune_residenza.provincia) + ")";
                                    return comune + ",<br> " + row.indirizzoresidenza;
                                }
                            }, {
                                targets: 6,
                                className: 'text-center',
                                render: function (data, type, row, meta) {
                                    if (row.soggetto === null) {
                                        return "";
                                    } else {
                                        return row.soggetto.ragionesociale;
                                    }
                                }
                            }
                        ]
                    }
                    ).columns.adjust();
                };
                return {
                    init: function () {
                        initTable1();
                    }
                };
            }();
            jQuery(document).ready(function () {
                KTDatatablesDataSourceAjaxServer.init();
                $('.kt-scroll-x').each(function () {
                    const ps = new PerfectScrollbar($(this)[0], {suppressScrollY: true});
                });
            });

            function swalSigma(id, idsp) {
                swal.fire({
                    title: 'Stato di partecipazione',
                    html: '<div id="swalModificaStato">'
                            + '<div id="warning_sp" class="form-group kt-font-io-n row col" style="margin-left: 0px;margin-right: 0px; display: none;" ><div class="col-1"><i class="fa fa-exclamation-triangle" style="position: absolute;top: 50%;left: 50%;transform: translate(-50%,-50%);font-size:20px;" ></i></div><div id="warningmsg" class="col-10" ></div><div class="col-1"><i class="fa fa-exclamation-triangle" style="position: absolute;top: 50%;left: 50%;transform: translate(-50%,-50%);font-size:20px;"></i></div></div>'
                            + '<div class="select-div" id="sigma_div">'
                            + '<select class="form-control kt-select2-general obbligatory" id="sigma" name="sigma"  style="width: 100%" >'
                            + '<option value="-">Seleziona stato di partecipazione</option>'
                            + '</select></div><br>'
                            + '</div>',
                    animation: false,
                    showCancelButton: true,
                    confirmButtonText: '&nbsp;<i class="la la-check"></i>',
                    cancelButtonText: '&nbsp;<i class="la la-close"></i>',
                    customClass: {
                        popup: 'animated bounceInUp',
                        cancelButton: "btn btn-io-n",
                        confirmButton: "btn btn-io"
                    },
                    onOpen: function () {
                        $('#sigma').select2({
                            dropdownCssClass: "select2-on-top",
                            minimumResultsForSearch: -1
                        });
                        $.get(context + "/QueryMicro?type=getSIGMA", function (resp) {
                            var json = JSON.parse(resp);
                            for (var i = 0; i < json.length; i++) {
                                if (json[i].id === idsp) {
                                    $("#sigma").append('<option selected value="' + json[i].id + '">' + json[i].descrizione + '</option>');
                                } else {
                                    $("#sigma").append('<option value="' + json[i].id + '">' + json[i].descrizione + '</option>');
                                }
                            }
                        });
                    },
                    preConfirm: function () {
                        var err = false;
                        err = checkObblFieldsContent($('#swalModificaStato')) ? true : err;
                        if (!err) {
                            return new Promise(function (resolve) {
                                resolve({
                                    "sigma": $('#sigma').val()
                                });
                            });
                        } else {
                            return false;
                        }
                    }
                }).then((result) => {
                    if (result.value) {
                        setValueStato(id, result.value.sigma);
                    } else {
                        swal.close();
                    }
                }
                );
            }

            function setValueStato(id, sigma) {
                showLoad();
                $.ajax({
                    type: "POST",
                    url: context + '/OperazioniMicro?type=setSIGMA&id=' + id + '&sigma=' + sigma,
                    success: function (data) {
                        closeSwal();
                        console.log(data);
                        var json = JSON.parse(data);
                        if (json.result) {
                            swalSuccess("Modifica Stato Allievo", "Stato di partecipazione impostato correttamente");
                            reload_table($('#kt_table_1'));
                        } else {
                            swalError("Errore", json.message);
                        }
                    },
                    error: function () {
                        swalError("Errore", "Non è stato possibile impostare lo stato di partecipazione");
                    }
                });
            }



            function refresh() {
                $('html, body').animate({scrollTop: $('#offsetresult').offset().top}, 500);
                load_table($('#kt_table_1'), context + '/QueryMicro?type=searchAllievo&soggettoattuatore=' + $('#soggettoattuatore').val()
                        + '&cf=' + $('#cf').val() + '&nome=' + $('#nome').val() + '&cognome=' + $('#cognome').val()
                        + '&cpi=' + $('#cpi').val() + '&pregresso=0', );
            }

            function reload() {
                $('html, body').animate({scrollTop: $('#offsetresult').offset().top}, 500);
                reload_table($('#kt_table_1'));
            }

            function uploadDoc(idallievo) {
                var htmldoc = getHtml("uploadDoc", context).replace("@func", "checkFileExtAndDim('pdf');").replace("@mime", "application/pdf");
                swal.fire({
                    title: 'Carica Documento',
                    html: htmldoc,
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
                        $('#file').change(function (e) {
                            if (e.target.files.length !== 0)
                                //$('#label_doc').html(e.target.files[0].name);
                                if (e.target.files[0].name.length > 30)
                                    $('#label_doc').html(e.target.files[0].name.substring(0, 30) + "...");
                                else
                                    $('#label_doc').html(e.target.files[0].name);
                            else
                                $('#label_doc').html("Seleziona File");
                        });
                    },
                    preConfirm: function () {
                        var err = false;
                        err = !checkRequiredFileContent($('#uploadDoc')) ? true : err;
                        if (!err) {
                            return new Promise(function (resolve) {
                                resolve({
                                    "file": $('#file')[0].files[0]
                                });
                            });
                        } else {
                            return false;
                        }
                    }
                }).then((result) => {
                    if (result.value) {
                        showLoad();
                        var fdata = new FormData();
                        fdata.append("file", result.value.file);
                        upDoc(idallievo, "32", fdata);
                    } else {
                        swal.close();
                    }
                });
            }

            function upDoc(id, id_tipoDoc, fdata) {
                $.ajax({
                    type: "POST",
                    url: context + '/OperazioniMicro?type=uploadDocAllievo&idallievo=' + id + "&id_tipo=" + id_tipoDoc,
                    data: fdata,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        var json = JSON.parse(data);
                        if (json.result) {
                            swalSuccessReload("Documento Caricato", (json.message = !"" ? json.message : ""));
                        } else {
                            swalError("Errore", json.message);
                        }
                    },
                    error: function () {
                        swalError("Errore", "Non è stato possibile caricare il documento");
                    }
                });
            }

            function showPrg(id) {
                var progetto = prg.get(id);

                var html = "<div class='col-12' style='text-align:left;'>"
                        + "<dl class='row'>"
                        + "<dt class='col-sm-6'><h4><label class='font-weight-bold'>Nome:</label></h4></dt><dd class='col-sm-6'><h4>" + progetto.nome.descrizione + "</h4></dd>"
                        + "<dt class='col-sm-6'><h4><label class='font-weight-bold'>CIP:</label></h4></dt><dd class='col-sm-6'><h4>" + checknullField(progetto.cip) + "</h4></dd>"
                        + "<dt class='col-sm-6'><h4><label class='font-weight-bold'>Ore:</label></h4></dt><dd class='col-sm-6'><h4>" + progetto.ore + "</h4></dd>"
                        + "<dt class='col-sm-6'><h4><label class='font-weight-bold'>Inizio:</label></h4></dt><dd class='col-sm-6'><h4>" + formattedDate(new Date(progetto.start)) + "</h4></dd>"
                        + "<dt class='col-sm-6'><h4><label class='font-weight-bold'>Fine:</label></h4></dt><dd class='col-sm-6'><h4>" + formattedDate(new Date(progetto.end)) + "</h4></dd>"
                        + "<dt class='col-sm-6'><h4><label class='font-weight-bold'>Sog. Esecutore:</label></h4></dt><dd class='col-sm-6'><h4>" + progetto.soggetto.ragionesociale + "</h4></dd>"
                        + "<dt class='col-sm-6'><h4><label class='font-weight-bold'>Stato:</label></h4></dt><dd class='col-sm-6'><h4>" + progetto.stato.descrizione + "</h4></dd>"
                        + "</dl>";
                +"</div>";

                fastSwalShow(html, "bounceInUp");
            }

            function swalMail(idallievo, mailoriginal) {
                var html = "<div class='form-group' id='swal_mail'>"
                        + "<label>Indirizzo email:</label>"
                        + "<input class='form-control obbligatory' id='new_mail_" + idallievo + "' name='new_mail' value='" + mailoriginal + "' />"
                        + "</div>";

                swal.fire({
                    title: '<h2 class="kt-font-io-n"><b>Modifica Email NEET</b></h2><br>',
                    html: html,
                    animation: false,
                    showCancelButton: true,
                    confirmButtonText: '&nbsp;<i class="la la-check"></i>',
                    cancelButtonText: '&nbsp;<i class="la la-close"></i>',
                    cancelButtonClass: "btn btn-io-n",
                    confirmButtonClass: "btn btn-io",
                    width: '750px',
                    customClass: {
                        popup: 'animated bounceInUp'
                    },
                    preConfirm: function () {
                        var err = false;
                        if (checkEmail($('#new_mail_' + idallievo))) {
                            err = true;
                        }
                        err = checkObblFieldsContent($('#new_mail_' + idallievo)) ? true : err;
                        if (!err) {
                            return new Promise(function (resolve) {
                                resolve({
                                    "new_mail": $('#new_mail_' + idallievo).val()
                                });
                            });
                        } else {
                            return false;
                        }
                    }
                }).then((result) => {
                    if (result.value) {
                        changemail(idallievo, result.value);
                    } else {
                        swal.close();
                    }
                });

            }

            function changemail(idallievo, result) {
                showLoad();
                $.ajax({
                    type: "POST",
                    url: context + '/OperazioniGeneral?type=editMailNeet&idallievo=' + idallievo,
                    data: result,
                    success: function (data) {
                        closeSwal();
                        var json = JSON.parse(data);
                        if (json.result) {
                            reload();
                            swalSuccess("Email Modificata", "Indirizzo email modificato con successo");
                        } else {
                            swalError("Errore", json.message);
                        }
                    },
                    error: function () {
                        swalError("Errore", "Non è stato possibile validare il progetto formativo");
                    }
                });
            }

            var registri = new Map();
            var registri_aula = new Map();
            function swalDocumentAllievo(idallievo) {
                $("#prg_docs").empty();
                var giorno;
                //var doc_registro_aula = getHtml("documento_registro", context);
                var doc_prg = getHtml("documento_prg", context);
                $.get(context + "/QueryMicro?type=getDocAllievo&idallievo=" + idallievo, function (resp) {
                    var json = JSON.parse(resp);
                    for (var i = 0; i < json.length; i++) {
                        giorno = json[i].giorno !== null ? " del " + formattedDate(new Date(json[i].giorno)) : "";
                        var ext = json[i].tipo.estensione;
                        if (ext === null || ext === undefined || typeof ext === 'undefined' || ext === "p7m" || ext.includes("pdf")) {
                            ext = "pdf";
                        }
                        $("#prg_docs").append(doc_prg.replace("@href", context + "/OperazioniGeneral?type=showDoc&path=" + json[i].path)
                                .replace("#ex", ext)
                                .replace("@nome", json[i].tipo.descrizione + giorno));
                    }
                    $('#doc_modal').modal('show');
                });
            }



        </script>
    </body>
</html>
<%
        }
    }
%>