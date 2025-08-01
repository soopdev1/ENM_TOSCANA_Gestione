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

            Documenti_Allievi attestatofirmato = null;
            for (Documenti_Allievi da : a.getDocumenti()) {
                if (da.getTipo().getId().equals(25L)) {
                    attestatofirmato = da;
                    break;
                }
            }

%>
<html>
    <head>
        <meta charset="utf-8" />
        <title>YES I Start Up - Toscana - Attestati Partecipazione</title>
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
                                            Attestati di partecipazione: <b><%=a.getCognome()%> <%=a.getNome()%> (<%=a.getCodicefiscale()%>)</b>
                                        </h3>
                                    </div>
                                </div>
                                <%if (attestatofirmato != null) {%>
                                <div class="kt-portlet__body">
                                    <div class="row col-md-12">
                                        <div class="kt-form__actions row col-md-12">
                                            <div class="form-group col-md-6">
                                                <form action="<%=request.getContextPath()%>/OperazioniMicro" method="POST" target="_blank">
                                                    <input type="hidden" name="type" value="SCARICAATTESTATOENM" />
                                                    <input type="hidden" name="idallievo" value="<%=a.getId()%>" />
                                                    <button type="submit" class="btn btn-success" 
                                                            style="font-family: Poppins"><i class="fa fa-file-pdf"></i> SCARICA ATTESTATO DI FREQUENZA FIRMATO DA ENM</button>
                                                </form>
                                            </div>
                                        <div class="form-group col-md-6">
                                            <form action="<%=request.getContextPath()%>/OperazioniGeneral" method="POST">
                                                <input type="hidden" name="type" value="sendmailAttestato" />
                                                <input type="hidden" name="idallievo" value="<%=a.getId()%>" />
                                                <input type="hidden" name="maildest" value="<%=a.getEmail()%>" />
                                                <input type="hidden" name="path" value="<%=attestatofirmato.getPath()%>" />
                                                <button type="submit" class="btn btn-io" 
                                                        style="font-family: Poppins"><i class="fa fa-file-pdf"></i> INVIA ATTESTATO DI FREQUENZA TRAMITE MAIL AL DISCENTE</button>
                                            </form>
                                    </div>
                                        </div>                                    
                                    </div>
                                </div>
                                <%} else {%>


                                <div class="kt-portlet__foot" style="padding-left: 10px;">
                                    <div class="row col-md-12">
                                        <div class="form-group col-md-6">
                                            <label class="kt-font-danger kt-font-boldest">ATTESTATI DISPONIBILI:</label>
                                        </div>
                                        <%for (Documenti_Allievi da : a.getDocumenti()) {
                                                if (da.getTipo().getId().equals(22L)
                                                        || da.getTipo().getId().equals(23L)
                                                        || da.getTipo().getId().equals(24L)) {
                                        %>
                                        <div class="form-group col-md-12">
                                            <label class="kt-font-dark kt-font-boldest">
                                                <%=da.getTipo().getDescrizione()%>
                                            </label>
                                        </div>
                                        <%}
                                            }%>
                                    </div> 
                                </div>
                                <div class="kt-portlet__foot" style="padding-left: 10px;">
                                    <div class="kt-form__actions row col-md-12">
                                        <div class="form-group col-md-3">
                                            <form action="<%=request.getContextPath()%>/OperazioniMicro" method="POST" target="_blank">
                                                <input type="hidden" name="type" value="SCARICAATTESTATI" />
                                                <input type="hidden" name="idallievo" value="<%=a.getId()%>" />
                                                <button type="submit" class="btn btn-success" 
                                                        style="font-family: Poppins"><i class="fa fa-file-pdf"></i> SCARICA</button>
                                            </form>
                                        </div>
                                        <div class="form-group col-md-9">
                                            <a href='javascript:void(0);' onclick="uploadDoc(<%=a.getId()%>, 25, 'pdf,p7m', 'application/pkcs7-mime,application/pdf');" 
                                               class="btn btn-primary" 
                                               style="font-family: Poppins"><i class="fa fa-upload"></i> UPLOAD ATTESTATO DI FREQUENZA FIRMATO DA ENM</a>
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

            var context = '<%=src%>';
            $.getScript(context + '/page/partialView/partialView.js', function () {});

            function uploadDoc(idallievo, id_tipoDoc, estensione, mime_type) {
                var ext = estensione.split('"').join("&quot;");
                var swalDoc = getHtml("swalDoc", context).replace("@func", "checkFileExtAndDim(" + ext + ");").replace("@mime", mime_type);
                swal.fire({
                    title: 'Carica Documento',
                    html: swalDoc,
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
                                //$('#label_file').html(e.target.files[0].name);
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
                        err = !checkRequiredFileContent($('#swalDoc')) ? true : err;
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
                        $.ajax({
                            type: "POST",
                            url: context + '/OperazioniMicro?type=uploadDocAllievo&idallievo=' + idallievo + "&id_tipo=" + id_tipoDoc,
                            data: fdata,
                            processData: false,
                            contentType: false,
                            success: function (data) {
                                closeSwal();
                                var json = JSON.parse(data);
                                if (json.result) {
                                    swalSuccessReload("Documento Caricato", "Documento caricato con successo.");
                                } else {
                                    swalError("Errore", json.message);
                                }
                            },
                            error: function () {
                                swalError("Errore", "Non è stato possibile caricare il documento");
                            }
                        });
                    } else {
                        swal.close();
                    }
                }
                );
            }

        </script>
    </body>
</html>
<%}
    }%>