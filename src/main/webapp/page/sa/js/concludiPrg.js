/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var context = document.getElementById("concludiPrg").getAttribute("data-context");
var doc_docenti = new Map();

$('select[id^=regione_]').on('change', function (e) {
    let alr = this.id.split("_")[1];
    $("#provincia_" + alr).empty();
    $("#comune_" + alr).empty();
    $("#comune_" + alr).append('<option value="-">Seleziona Comune</option>');
    if ($('#regione_' + alr).val() !== '-') {
        startBlockUILoad("#provincia_" + alr + "_div");
        $("#provincia_" + alr).append('<option value="-">Seleziona Provincia</option>');
        $.get(context + '/Login?type=getProvincia&regione=' + $('#regione_' + alr).val(), function (resp) {
            var json = JSON.parse(resp);
            for (var i = 0; i < json.length; i++) {
                $("#provincia_" + alr).append('<option value="' + json[i].value + '">' + json[i].desc + '</option>');
            }
            stopBlockUI("#provincia_" + alr + "_div");
        });
    } else {
        $("#provincia_" + alr).append('<option value="-">Seleziona Provincia</option>');
    }
});

$('select[id^=provincia_]').on('change', function (e) {
    let alp = this.id.split("_")[1];
    $("#comune_" + alp).empty();
    if ($('#provincia_' + alp).val() !== '-') {
        startBlockUILoad("#comune_" + alp + "_div");
        $("#comune_" + alp).append('<option value="-">Seleziona Comune</option>');
        $.get(context + '/Login?type=getComune&provincia=' + $('#provincia_' + alp).val(), function (resp) {
            var json = JSON.parse(resp);
            for (var i = 0; i < json.length; i++) {
                $("#comune_" + alp).append('<option value="' + json[i].value + '">' + json[i].desc + '</option>');
            }
            stopBlockUI("#comune_" + alp + "_div");
        });
    } else {
        $("#comune_" + alp).append('<option value="-">Seleziona Comune</option>');
    }
});

function deleteModello(id) {
    swal.fire({
        title: '<h3 class="kt-font-io-n"><b>Conferma Eliminazione</b></h3><br>',
        html: "<h5 style='text-align:center;'>Sicuro di voler eliminare il modello 5 dell'alunno?</h5>",
        animation: false,
        showCancelButton: true,
        confirmButtonText: '&nbsp;<i class="la la-check"></i>',
        cancelButtonText: '&nbsp;<i class="la la-close"></i>',
        cancelButtonClass: "btn btn-io-n",
        confirmButtonClass: "btn btn-io",
        customClass: {
            popup: 'large-swal animated bounceInUp'
        }
    }).then((result) => {
        if (result.value) {
            deleteConfirmedM5(id);
        } else {
            swal.close();
        }
    });
}

function deleteConfirmedM5(id) {
    showLoad();
    $.ajax({
        type: "POST",
        url: context + '/OperazioniSA?type=deleteModello5Alunno&id=' + id,
        success: function (data) {
            closeSwal();
            var json = JSON.parse(data);
            if (json.result) {
                swalSuccessReload("Modello eliminato", "Modello eliminato con successo");
                $('#currentstep').val('');
            } else {
                swalError("Errore", json.message);
            }
        },
        error: function () {
            swalError("Errore", "Non è stato possibile eliminare il modello.");
        }
    });
}


$('.decimal_custom.ctrl').on('change', function () {
    let val;
    if (this.value.startsWith("9.")) {
        this.value = "9.0";
    }
    ;
    val = this.value;
    if (this.value === "") {
        val = 0;
    }
    let colA = "#A_" + this.id.split("_")[1] + "_" + this.id.split("_")[2];
    let colC = "#C_" + this.id.split("_")[1] + "_" + this.id.split("_")[2];

    let res = (Number($(colA).val()) * Number(val)).toFixed(1);
    $(colC).val(res);
    calcTotals(colC);
});

function setTotals() {
    let peso, voto, alunno;
    let totalB = 0;
    $('div.accordion[id^=al_]').each(function () {
        alunno = this.id.split("_")[1];
        $(this).find(".decimal_custom[id^=C_]").each(function () {
            peso = this.id.replace("C", "A");
            voto = this.id.replace("C", "B");
            $('#' + voto).val("0.00");
            this.value = Number($('#' + peso).val()) * Number($('#' + voto).val()).toFixed(1);
            totalB += Number(this.value);
        });
        totalB = Number(totalB).toFixed(1);
        $('#totalB_' + alunno).val(totalB);
        let final = 0;

        if (totalB > 0 && totalB <= 3) {
            final = 3;
        } else if (totalB > 3 && totalB <= 6) {
            final = 6;
        } else if (totalB > 6) {
            final = 9;
        } else {
            final = 0;
        }
        $('#final_' + alunno).val(final);
    });
}

function calcTotals(col) {
    let idAl = col.split("_")[2];
    let table = col.split("_")[0].replace('#', '');
    let totalB = 0;
    let idTotal = "#totalB_" + idAl;
    let idFinal = "#final_" + idAl;
    $('input[id^="' + table + '"][id$="' + idAl + '"]').each(function () {
        totalB += Number(this.value);
    });
    totalB = totalB.toFixed(1);
    $(idTotal).val(totalB);
    let final = 0;
    if (totalB > 0 && totalB <= 3) {
        final = 3;
    } else if (totalB > 3 && totalB <= 6) {
        final = 6;
    } else if (totalB > 6) {
        final = 9;
    } else {
        final = 0;
    }

    $(idFinal).val(final);
}

jQuery(document).ready(function () {
    $(".decimal_custom").inputmask({
        'alias': 'decimal',
        'mask': "9[.9]",
        'rightAlign': true
    });
    resetForm();
    setTotals();
    setStep3();
});


function resetForm() {
    $('#kt_form').find('select').select2("val", "-");
    $("#kt_form").find('input[type=text]:not([id^=A_],.hidden), select, textarea').val('');
    $("#kt_form").find('input:radio, input:checkbox').removeAttr('checked').removeAttr('selected');
    $('#kt_form').find('input[type=file]').val("");
    $("input[name^='domanda_a_']").prop('checked', true);
}

function ctrlForm(id) {
    var err = false;
    if ($('#domanda_a_' + id).is(":checked")) {
        err = checkObblFields_Allievo(id) ? true : err;
        err = !checkRequiredFileAlunno(id) ? true : err;
    }
    err = !checkRequiredM7Alunno(id) ? true : err;
    return !err;
}

function ctrlFormStep3() {
    var err = false;
    if ($("input:radio[name=scelta_step3]:checked").val() === 2) {
        err = checkObblFieldsContent($('#step3')) ? true : err;
    }
    return !err;
}

function checkObblFields_Allievo(id) {
    var err = false;
    $('input.obbligatory[id$=' + id + ']').each(function () {
        if (!$(this).is(':disabled')) {
            if ($(this).val() === '') {
                err = true;
                $(this).removeClass("is-valid").addClass("is-invalid");
            } else {
                $(this).removeClass("is-invalid").addClass("is-valid");
                //Valori della tabella possono essere 0.0?
//                if ($(this).hasClass("ctrl") && $(this).val() == "0.0") {
//                    err = true;
//                    $(this).removeClass("is-valid").addClass("is-invalid");
//                }
                if ($(this).hasClass("currencymask") && cleanCurrency($(this).val()) === 0) {
                    err = true;
                    $(this).removeClass("is-valid").addClass("is-invalid");
                }
            }
        } else {
            $(this).removeClass("is-valid").removeClass("is-invalid");
        }
    });
    $('select.obbligatory[id$=' + id + ']').each(function () {
        if ($(this).val() === '' || $(this).val() === '-' || $(this).val() === null) {
            err = true;
            $('#' + this.id + '_div').removeClass("is-valid-select").addClass("is-invalid-select");
        } else {
            $('#' + this.id + '_div').removeClass("is-invalid-select").addClass("is-valid-select");
        }
    });
    //Check radio SI/NO
    $('div.radioGroup_' + id).each(function () {
        let ctrl = false;
        let label;
        $(this).find('input[type=radio][name$=' + id + ']').each(function () {
            label = 'label[name=' + $(this).attr("name") + ']';
            if ($(this).is(":checked") && !ctrl) {
                ctrl = true;
            }
        });
        if (ctrl) {
            $(label).removeClass("kt-radio-invalid").addClass("kt-radio-valid");
        } else {
            $(label).removeClass("kt-radio-valid").addClass("kt-radio-invalid");
            err = true;
        }
    });

    return err;
}

function checkRequiredFileAlunno(id) {
    var err = false;
    $('input:file[tipo=obbligatory][id=doc_' + id + ']').each(function () {
        if ($(this)[0].files.length === 0) {
            err = true;
            $(this).attr("class", "custom-file-input is-invalid");
        } else {
            $(this).attr("class", "custom-file-input is-valid");
        }
    });
    return !err;
}

function checkRequiredM7Alunno(id) {
    var err = false;
    $('input:file[tipo=obbligatory][id=m7_' + id + ']').each(function () {
        if ($(this)[0].files.length === 0) {
            err = true;
            $(this).attr("class", "custom-file-input is-invalid");
        } else {
            $(this).attr("class", "custom-file-input is-valid");
        }
    });
    return !err;
}

function cleanCurrency(v) {
    v = v.substring(v.lastIndexOf("_") + 1);
    v = v.replaceAll(".", "");
    v = v.replaceAll(",", ".");
    return Number(v);
}


//Rendiconto Allievo
$('a[id^=rendiconta_]').on('click', function () {
    let idal = this.id.split("_")[1];
    if (ctrlForm(idal)) {

        swal.fire({
            title: '<h3 class="kt-font-io-n"><b>Completa Modello 5 Allievo</b></h3><br>',
            html: "<h5>Attenzione, dopo la conferma non sarà più possibile modificare il Modello 5 creato.<br>" +
                    "In caso di errore sarà necessario eliminare il modello in questione e ricompilare l'intera sezione.</h5>",
            animation: false,
            showCancelButton: true,
            confirmButtonText: '&nbsp;<i class="la la-check"></i>',
            cancelButtonText: '&nbsp;<i class="la la-close"></i>',
            cancelButtonClass: "btn btn-io-n",
            confirmButtonClass: "btn btn-io",
            customClass: {
                popup: 'animated bounceInUp'
            }
        }).then((result) => {
            if (result.value) {

                let grado_completezza = $('#gcbp_' + idal).val();
                let probabilita = $('#prob_' + idal).val();
                let forma_giuridica = $('#fg_' + idal).val();
                let ateco = $('#ateco_' + idal).val();
                let sede = $('input[type=radio][name=check_sede_' + idal + ']:checked').val();
                let regione = $('#regione_' + idal).val();
                let provincia = $('#provincia_' + idal).val();
                let comune = $('#comune_' + idal).val();
                let totale_fabbisogno = cleanCurrency($('#tff_' + idal).val());
                let misura_individuata = $('input[type=radio][name=check_misura_' + idal + ']:checked').val();
                let misura_no_motivazione = $('#no_mot_misura_' + idal).val();
                let misura_si_nome = $('#den_misura_si_' + idal).val();
                let misura_si_tipo = $('#tipo_misura_' + idal).val();
                let misura_si_motivazione = $('#si_mot_misura_' + idal).val();

                showLoad();
                let fdata = new FormData();

                fdata.append("id_allievo", idal);
                fdata.append("grado_completezza", grado_completezza);
                fdata.append("probabilita", probabilita);
                fdata.append("forma_giuridica", forma_giuridica);
                fdata.append("ateco", ateco);
                fdata.append("sede", sede);
                fdata.append("regione", regione);
                fdata.append("provincia", provincia);
                fdata.append("comune", comune);
                fdata.append("totale_fabbisogno", totale_fabbisogno);
                fdata.append("misura_individuata", misura_individuata);
                fdata.append("misura_no_motivazione", misura_no_motivazione);
                fdata.append("misura_si_nome", misura_si_nome);
                fdata.append("misura_si_tipo", misura_si_tipo);
                fdata.append("misura_si_motivazione", misura_si_motivazione);
                fdata.append("doc", $('#doc_' + idal)[0].files[0]);
                $.ajax({
                    type: "POST",
                    url: context + '/OperazioniSA?type=salvamodello5',
                    data: fdata,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        closeSwal();
                        var json = JSON.parse(data);
                        if (json.result) {
                            swalSuccessReload("Completato Modello 5 Allievo", "Operazione effettuata con successo");
                        } else {
                            swalError("Errore", json.message);
                        }
                    },
                    error: function () {
                        swalError("Errore", "Non è stato possibile salvare il modello 5 dell'allievo");
                    }
                });
            } else {
                swal.close();
            }
        });
    }
});



function uploadModello5(idallievo, title) {
    swal.fire({
        title: title,
        html: '<div id="swalM5' + idallievo + '">'
                + '<div class="custom-file">'
                + '<input type="file" tipo="obbligatory" class="custom-file-input" accept="application/pkcs7-mime,application/pdf" name="m5_' + idallievo + '" id="m5_' + idallievo
                + '" onchange="return checkFileExtAndDim([&quot;pdf,p7m&quot;]);">'
                + '<label class="custom-file-label selected" id="label_' + idallievo + '" style="text-align: left;">Seleziona File</label>'
                + '</div>'
                + '</div>',
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
            $('#m5_' + idallievo).change(function (e) {
                if (e.target.files.length !== 0)
                    //$('#label_file').html(e.target.files[0].name);
                    if (e.target.files[0].name.length > 30)
                        $('#label_' + idallievo).html(e.target.files[0].name.substring(0, 30) + "...");
                    else
                        $('#label_' + idallievo).html(e.target.files[0].name);
                else
                    $('#label_' + idallievo).html("Seleziona File");
            });
        },
        preConfirm: function () {
            var err = false;
            err = !checkRequiredFileContent($('#swalM5' + idallievo)) ? true : err;
            if (!err) {
                return new Promise(function (resolve) {
                    resolve({
                        "file": $('#m5_' + idallievo)[0].files[0]
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
                url: context + '/OperazioniSA?type=uploadM5Alunno&id=' + idallievo,
                data: fdata,
                processData: false,
                contentType: false,
                success: function (data) {
                    closeSwal();
                    var json = JSON.parse(data);
                    if (json.result) {
                        swalSuccessReload("Modello 5", "Modello 5 caricato con successo");
                        $('#currentstep').val('');
                    } else {
                        swalError("Errore", json.message);
                    }
                },
                error: function () {
                    swalError("Errore", "Non è stato possibile caricare il modello 5");
                }
            });
        } else {
            swal.close();
        }
    });
}

$('button[id^=loadM5_]').click(function () {
    let idalunno = this.id.split("_")[1];
    uploadModello5(idalunno, $(this).attr('data-original-title'));
});


function setStep3() {
    let m6 = loadInformazioniStep3();
    if (m6 === null || m6 === "") {
        $("input:radio[name=scelta_step3]:first").prop('checked', 'checked');
        $('[name=regione_step3]').val('-');
        $('[name=provincia_step3]').val('-');
        $('[name=comune_step3]').val('-');
        $('#info_step3').hide();
    } else {
        if (m6.scelta_modello6 === 1) {
            $("input:radio[name=scelta_step3]:first").prop('checked', 'checked');
            $('#info_step3').hide();
            $('[name=regione_step3]').val('-');
            $('[name=provincia_step3]').val('-');
            $('[name=comune_step3]').val('-');
        } else {
            $("input:radio[name=scelta_step3]:last").prop('checked', 'checked');
            $('#info_step3').show();
            $('[name=regione_step3]').val(m6.comune_modello6.regione).trigger('change');
            setProvinciaStep3(m6.comune_modello6.regione, m6.comune_modello6.nome_provincia);
            setComuneStep3(m6.comune_modello6.nome_provincia, m6.comune_modello6.id);
            $('#indirizzo_step3').val(m6.indirizzo_modello6);
            $('#civico_step3').val(m6.civico_modello6);
        }
    }
}

$("input:radio[name=scelta_step3]").change(function () {
    if (this.value === 2 || this.value === "2") {
        $('#info_step3').show();
    } else {
        $('#info_step3').hide();
    }
});


function uploadRegistroComplessivo(pf) {
    swal.fire({
        title: 'Carica Registro complessivo presenze',
        html: '<div id="swal_regC">'
                + '<div class="custom-file">'
                + '<input type="file" tipo="obbligatory" class="custom-file-input" accept="application/pkcs7-mime,application/pdf" name="rcp_'
                + pf + '" id="rcp_' + pf + '" onchange="return checkFileExtAndDim([&quot;pdf,p7m&quot;]);">'
                + '<label class="custom-file-label selected" id="label_rcp_' + pf + '" style="text-align: left;">Seleziona File</label>'
                + '</div>'
                + '</div>',
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
            $('#rcp_' + pf).change(function (e) {
                if (e.target.files.length !== 0)
                    //$('#label_file').html(e.target.files[0].name);
                    if (e.target.files[0].name.length > 30)
                        $('#label_rcp_' + pf).html(e.target.files[0].name.substring(0, 30) + "...");
                    else
                        $('#label_rcp_' + pf).html(e.target.files[0].name);
                else
                    $('#label_rcp_' + pf).html("Seleziona File");
            });
        },
        preConfirm: function () {
            var err = false;
            err = !checkRequiredFileContent($('#swal_regC')) ? true : err;
            if (!err) {
                return new Promise(function (resolve) {
                    resolve({
                        "file": $('#rcp_' + pf)[0].files[0]
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
                url: context + '/OperazioniSA?type=uploadRegistroComplessivo&id=' + pf,
                data: fdata,
                processData: false,
                contentType: false,
                success: function (data) {
                    closeSwal();
                    var json = JSON.parse(data);
                    if (json.result) {
                        $('#currentstep').val('2');
                        swalSuccessReload("Registro Complessivo Presenze", "Registro caricato con successo");
                    } else {
                        swalError("Errore", json.message);
                    }
                },
                error: function () {
                    swalError("Errore", "Non è stato possibile caricare il registro.");
                }
            });
        } else {
            swal.close();
        }
    });
}

$('button[id^=registro_]').click(function () {
    uploadRegistroComplessivo(this.id.split("_")[1]);
});


function loadInformazioniStep3() {
    let temp = "";
    $.ajax({
        type: "GET",
        async: false,
        url: context + "/QuerySA?type=loadInfoM6&id=" + $('#pf').val(),
        success: function (resp) {
            if (resp !== null)
                temp = JSON.parse(resp);
        }
    });
    return temp;
}

function setComuneStep3(provincia, comune) {
    $("#comune_step3").empty();
    startBlockUILoad("#comune_step3_div");
    $("#comune_step3").append('<option value="-">Seleziona Comune</option>');
    $.get(context + '/Login?type=getComune&provincia=' + provincia, function (resp) {
        var json = JSON.parse(resp);
        for (var i = 0; i < json.length; i++) {
            if (comune === json[i].value) {
                $("#comune_step3").append('<option selected value="' + json[i].value + '">' + json[i].desc + '</option>');
            } else {
                $("#comune_step3").append('<option value="' + json[i].value + '">' + json[i].desc + '</option>');
            }
        }
        stopBlockUI("#comune_step3_div");
    });
}

function setProvinciaStep3(regione, provincia) {
    $("#provincia_step3").empty();
    startBlockUILoad("#provincia_step3_div");
    $("#provincia_step3").append('<option value="-">Seleziona Provincia</option>');
    $.get(context + '/Login?type=getProvincia&regione=' + regione, function (resp) {
        var json = JSON.parse(resp);
        for (var i = 0; i < json.length; i++) {
            if (provincia.toLowerCase() === json[i].value.toLowerCase()) {
                $("#provincia_step3").append('<option selected value="' + json[i].value + '">' + json[i].desc + '</option>');
            } else {
                $("#provincia_step3").append('<option value="' + json[i].value + '">' + json[i].desc + '</option>');
            }
        }
        stopBlockUI("#provincia_step3_div");
    });
}

$('button[id^=dichiarazione_]').click(function () {
    if (ctrlFormStep3()) {
        let scelta_step3 = $('input[type=radio][name=scelta_step3]:checked').val();
        let indirizzo_step3 = $('#indirizzo_step3').val();
        let civico_step3 = $('#indirizzo_step3').val();
        let comune_step3 = $('#comune_step3').val();

        showLoad();
        let fdata = new FormData();
        fdata.append("pf", this.id.split("_")[1]);
        fdata.append("scelta_step3", scelta_step3);
        fdata.append("indirizzo_step3", indirizzo_step3);
        fdata.append("civico_step3", civico_step3);
        fdata.append("comune_step3", comune_step3);

        $.ajax({
            type: "POST",
            url: context + '/OperazioniSA?type=uploadDichiarazioneM6',
            data: fdata,
            processData: false,
            contentType: false,
            success: function (data) {
                closeSwal();
                var json = JSON.parse(data);
                if (json.result) {
                    swalSuccessReload("Dichiarazione di chiusura percorso", "Dati salvati con successo");
                    $('#currentstep').val('3');
                } else {
                    swalError("Errore", "Non è stato possibile procedere con il salvataggio dei dati");
                }
            },
            error: function () {
                swalError("Errore", "Non è stato possibile procedere con il salvataggio dei dati");
            }
        });

    }
});


function misuraindividuata(idal) {
    var misura = $('input[type=radio][name=check_misura_' + idal + ']:checked').val();
    alert(misura);

    if (misura === "NO") {
        $('#MISURANO_' + idal).toggle(true);
        $('#MISURASI_' + idal).toggle(false);
    } else {
        $('#MISURASI_' + idal).toggle(true);
        $('#MISURANO_' + idal).toggle(false);

    }
}