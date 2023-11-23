/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var context = document.getElementById("newAllievo").getAttribute("data-context");


function ctrlForm() {
    var err = false;
    err = checkObblFields() ? true : err;
    err = !checkRequiredFile() ? true : err;
    err = !checkFileExtAndDim(['pdf']) ? true : err;
    return !err;
}
function ctrlFormNOFILE() {
    var err = false;
    err = checkObblFields() ? true : err;
    return !err;
}

$('#submit_change').on('click', function () {
    if (ctrlForm()) {
        showLoad();
        document.getElementById('save').value = '0';
        $('#kt_form').prop("target", "");
        $('#kt_form').ajaxSubmit({
            error: function () {
                closeSwal();
                swal.fire({
                    "title": 'Errore',
                    "text": "Riprovare, se l'errore persiste contattare il servizio clienti",
                    "type": "error",
                    cancelButtonClass: "btn btn-io-n"
                });
            },
            success: function (resp) {
                var json = JSON.parse(resp);
                closeSwal();
                if (json.result) {
                    resetInput();
                    swalSuccessReload("Allievo aggiunto!", "Operazione effettuata con successo.");
//                                swal.fire({
//                                    "title": '<h2 class="kt-font-io"><b>Allievo aggiunto!</b></h2><br>',
//                                    "html": "<h4>Operazione effettuata con successo.</h4>",
//                                    "type": "success",
//                                    "width": '45%',
//                                    "confirmButtonClass": "btn btn-io",
//                                });
                } else {
                    swal.fire({
                        "title": '<h2 class="kt-font-io-n"><b>Errore!</b></h2><br>',
                        "html": "<h4>" + json.message + "</h4>",
                        "type": "error",
                        cancelButtonClass: "btn btn-io-n"
                    });
                }
            }
        });
    }
});

$('#email').on("change", function () {
    if (checkEmail($('#email'))) {
        EmailPresente();
    }
});

function EmailPresente() {
    var err;
    $.ajax({
        type: "GET",
        async: false,
        url: context + '/OperazioniSA?type=checkEmail&email=' + $('#email').val(),
        success: function (data) {
            if (data !== null && data !== 'null') {
                swal.fire({
                    "title": 'Errore',
                    "html": "<h3>Email già presente</h3>",
                    "type": "error",
                    cancelButtonClass: "btn btn-io-n"
                });
                fastSwal("Attenzione!", "Email già presente", "wobble");
                $('#email').attr("class", "form-control is-invalid");
                err = true;
            } else {
                $('#email').attr("class", "form-control is-valid");
                err = false;
            }
        }
    });
    return err;
}


$('#codicefiscale').on("change", function () {
    if (checkCF($('#codicefiscale'))) {
        CFPresent();
    }
});

function checkinfoCF() {
    var cf = $('#codicefiscale');
    var nome = $('#nome');
    var cognome = $('#cognome');
    var data = $('#datanascita');
    var stato = $('#stato');
    var checkdata = false;
    var err = false;
    var msg = "<b style='padding:10px;color: #fd397a!important;'>Attenzione i seguenti dati anagrafici non sono conformi con il codice fiscale inserito (";
    //CONTROLLO NOME ---> 1,3,4 CONSONANTI SE CE NE SONO ALMENO 4, ALTRIMENTI AGGIUNGO VOCALI NEL LORO ORDINE 
    if (!check_nome_CF(nome.val().replace(/[^a-zA-Z ]/g, "").toUpperCase(), cf.val().substring(3, 6).toUpperCase())) {
        msg += "Nome";
        $('#nome').removeClass("is-valid").addClass("is-invalid");
        err = true;
    } else {
        $('#nome').removeClass("is-invalid").addClass("is-valid");
    }
    //CONTROLLO COGNOME ---> 1,2,3 CONSONANTI SE CE NE SONO ALMENO 3, ALTRIMENTI AGGIUNGO VOCALI NEL LORO ORDINE 
    if (!check_cognome_CF(cognome.val().replace(/[^a-zA-Z ]/g, "").toUpperCase(), cf.val().substring(0, 3).toUpperCase())) {
        msg += err ? ", Cognome" : "Cognome";
        $('#cognome').removeClass("is-valid").addClass("is-invalid");
        err = true;
    } else {
        $('#cognome').removeClass("is-invalid").addClass("is-valid");
    }
    //CONTROLLO MESE - GIORNO - ANNO
    if (!check_mese_CF(data.val().substring(3, 5), cf.val().substring(8, 9).toUpperCase())) {
        msg += err ? ", Mese di nascita" : "Mese di nascita";
        checkdata = true;
    }
    if (!check_giorno_CF(Number(data.val().substring(0, 2)), Number(cf.val().substring(9, 11)))) {
        msg += err ? ", Giorno di nascita" : "Giorno di nascita";
        checkdata = true;
    }
    if (data.val().substring(8) !== cf.val().substring(6, 8)) {
        msg += err ? ", Anno di nascita" : "Anno di nascita";
        checkdata = true;
    }
    if (checkdata) {
        err = true;
        $('#datanascita').removeClass("is-valid").addClass("is-invalid");
    } else {
        $('#datanascita').removeClass("is-invalid").addClass("is-valid");
    }
    //CONTROLLO COMUNE O, SE ESTERO, STATO
    $('#regionenascita_div').removeClass("is-valid-select").removeClass("is-invalid-select");
    $('#provincianascita_div').removeClass("is-valid-select").removeClass("is-invalid-select");
    $('#comunenascita_div').removeClass("is-valid-select").removeClass("is-invalid-select");
    if (stato.val() === "100") {
        if ($('#comunenascita').val() !== null && $('#comunenascita').val() !== "-") {
            $.ajax({
                type: "GET",
                async: false,
                url: context + "/OperazioniSA?type=getCodiceCatastaleComune&idcomune=" + $('#comunenascita').val(),
                success: function (resp) {
                    $('#stato_div').removeClass("is-invalid-select").addClass("is-valid-select");
                    if (check_comune_CF(resp, cf.val().substring(11, 15).toUpperCase())) {
                        msg += err ? ", Comune di nascita" : "Comune di nascita";
                        $('#comunenascita_div').removeClass("is-valid-select").addClass("is-invalid-select");
                        err = true;
                    } else {
                        $('#comunenascita_div').removeClass("is-invalid-select").addClass("is-valid-select");
                    }
                }
            });
        } else {
            msg += err ? ", Comune di nascita" : " Comune di nascita";
            $('#comunenascita_div').removeClass("is-valid-select").addClass("is-invalid-select");
            err = true;
        }
    } else {
        if ((stato.val()) !== cf.val().substring(11, 15).toUpperCase()) {
            msg += err ? ", Stato di nascita" : "Stato di nascita";
            $('#stato_div').removeClass("is-valid-select").addClass("is-invalid-select");
            err = true;
        } else {
            $('#stato_div').removeClass("is-invalid-select").addClass("is-valid-select");
        }
    }

    if (err) {
        msg += ").</b>";
        $("#msg_cf").html(msg);
        $("#msgrow").css("display", "");
    } else {
        $("#msg_cf").html("<b style='padding:10px;color: #0abb87!important;'>Dati anagrafici coerenti con il codice fiscale inserito.</b>");
        $("#msgrow").css("display", "");
    }
    return err;
}

function CFPresent() {
    var err;
    $.ajax({
        type: "GET",
        async: false,
        url: context + '/OperazioniSA?type=checkCF&cf=' + $('#codicefiscale').val(),
        success: function (data) {
            if (data !== null && data !== 'null') {
                swal.fire({
                    "title": 'Errore',
                    "html": "<h3>Codice fiscale già presente</h3>",
                    "type": "error",
                    cancelButtonClass: "btn btn-io-n"
                });
                fastSwal("Attenzione!", "Codice fiscale già presente", "wobble");
                $('#codicefiscale').attr("class", "form-control is-invalid");
                err = true;
            } else {
                $('#codicefiscale').attr("class", "form-control is-valid");
                err = false;
            }
        }
    });
    return err;
}
$('#capres').keydown(function (e) {
    if (this.value.length > 4)
        if (!(e.which === '46' || e.which === '8' || e.which === '13')) // backspace/enter/del
            e.preventDefault();
});
$('#capdom').keydown(function (e) {
    if (this.value.length > 4)
        if (!(e.which === '46' || e.which === '8' || e.which === '13')) // backspace/enter/del
            e.preventDefault();
});

