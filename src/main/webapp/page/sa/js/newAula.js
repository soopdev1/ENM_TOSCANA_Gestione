var context = document.getElementById("newAula").getAttribute("data-context");

$('select[id^=regione]').on('change', function (e) {
    $("#provincia").empty();
    $("#comune").empty();
    $("#comune").append('<option value="-">Seleziona Comune</option>');
    if ($('#regione').val() !== '-') {
        startBlockUILoad("#provincia_div");
        $("#provincia").append('<option value="-">Seleziona Provincia</option>');
        $.get(context + '/Login?type=getProvincia&regione=' + $('#regione').val(), function (resp) {
            var json = JSON.parse(resp);
            for (var i = 0; i < json.length; i++) {
                $("#provincia").append('<option value="' + json[i].value + '">' + json[i].desc + '</option>');
            }
            stopBlockUI("#provincia_div");
        });
    } else {
        $("#provincia").append('<option value="-">Seleziona Provincia</option>');
    }
});

$('select[id^=provincia]').on('change', function (e) {
    $("#comune").empty();
    if ($('#provincia').val() !== '-') {
        startBlockUILoad("#comune_div");
        $("#comune").append('<option value="-">Seleziona Comune</option>');
        $.get(context + '/Login?type=getComune&provincia=' + $('#provincia').val(), function (resp) {
            var json = JSON.parse(resp);
            for (var i = 0; i < json.length; i++) {
                $("#comune").append('<option value="' + json[i].value + '">' + json[i].desc + '</option>');
            }
            stopBlockUI("#comune_div");
        });
    } else {
        $("#comune").append('<option value="-">Seleziona Comune</option>');
    }
});

$('.decimalvalue').keypress(function (event) {
    return isNumber(event, this);
});
$('.decimalvalue').keyup(function () {
    var val = $(this).val();
    var str = val.replace('.', ',');
    $(this).val(str);
});
$('.decimalvalue').change(function (event) {
    var val = $(this).val();
    if (isNaN(val)) {
        val = val.replace(/[^0-9\,]/g, '');
        if (val.split(',').length > 2)
            val = val.replace(/\,+$/, "");
    }
    $(this).val(val);
});

$('#accr').change(function (event) {
    var val = $(this).val();
    if (val === "SI") {
        document.getElementById("div_allegatoF").style.display = "none";
        document.getElementById("doc_12").removeAttribute("tipo");
    } else {
        document.getElementById("div_allegatoF").style.display = "";
        document.getElementById("doc_12").setAttribute("tipo", "obbligatory");
    }
});

function isNumber(evt, element) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if ((charCode !== 46) &&
            (charCode !== 44 || $(element).val().indexOf(',') !== -1) //VIRGOLA E SOLO 1
            && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function fieldNoEuroMail(fieldid) {

    var stringToReplace = document.getElementById(fieldid).value;
    var specialChars = "~`§!#$%^&*+€=[](){}|\"<>?£âãäëîïñôõö€ûüýÿÀÁÂÃÄÇçÈÉÊËÌÍÎÏÑÒÓÔÕÖÙÚÛÜÝ°";
    for (var i = 0; i < specialChars.length; i++) {
        stringToReplace = stringToReplace.replace(new RegExp("\\" + specialChars[i], 'gi'), '');
    }
    stringToReplace = stringToReplace.replace(/\\/g, "");

    //  var stringToReplace = document.getElementById(fieldid).value;
    //  alert(stringToReplace);                
    // stringToReplace = stringToReplace.replace("€", '');
    document.getElementById(fieldid).value = stringToReplace;

}

function checkObblFieldsContentVisible(content) {
    var err = false;
    content.find('input.obbligatory:visible').each(function () {
        if ($(this).val() === '') {
            err = true;
            $(this).removeClass("is-valid").addClass("is-invalid");
        } else {
            $(this).removeClass("is-invalid").addClass("is-valid");
        }
    });
    content.find('textarea.obbligatory:visible').each(function () {
        if ($(this).val() === '') {
            err = true;
            $(this).removeClass("is-valid").addClass("is-invalid");
        } else {
            $(this).removeClass("is-invalid").addClass("is-valid");
        }
    });
    content.find('select.obbligatory:visible').each(function () {
        if ($(this).val() === '' || $(this).val() === '-') {
            err = true;
            $('#' + this.id + '_div').removeClass("is-valid-select").addClass("is-invalid-select");
        } else {
            $('#' + this.id + '_div').removeClass("is-invalid-select").addClass("is-valid-select");
        }
    });
    return err;
}

function ctrlFormNF() {
    var err = false;
    err = checkObblFieldsContentVisible($('#kt_form')) ? true : err;
    return err ? false : true;
}

function ctrlForm() {
    var err = false;
    err = checkObblFieldsContentVisible($('#kt_form')) ? true : err;
    err = !checkRequiredFileContent($('#kt_form')) ? true : err;
    return err ? false : true;
}

function resetInput() {
    $('.form-control').val('');
    $('.form-control').removeClass('is-valid');
    $('.custom-file-input').val('');
    $('.custom-file-label').html('');
    $('.custom-file-input').removeClass('is-valid');
    $('select').val('-');
    $('select').trigger('change');
    $('div.dropdown.bootstrap-select.form-control.kt-').removeClass('is-valid is-valid-select');
}


function model_funct(codice) {
    if (ctrlFormNF()) {
        document.getElementById('save').value = "1";
        document.getElementById("kt_form").target = "_blank";
        document.getElementById("kt_form").submit();
    }
}

$('#submit').on('click', function () {
    if (ctrlForm()) {
        showLoad();
        document.getElementById('save').value = '0';
        $('#kt_form').prop("target", "");
        $('#kt_form').ajaxSubmit({
            error: function () {
                closeSwal();
                swalError("Errore", "Riprovare, se l'errore persiste contattare l'assistenza");
            },
            success: function (resp) {
                var json = JSON.parse(resp);
                closeSwal();
                if (json.result) {
                    resetInput();
                    swalSuccess("Aula aggiunta!", "Operazione effettuata con successo.");
                } else {
                    swalError("Errore!", json.message);
                }
            }
        });
    }
});

function fieldOnlyNumber(fieldid) {
    var stringToReplace = document.getElementById(fieldid).value;
    stringToReplace = stringToReplace.replace(/\D/g, '');
    document.getElementById(fieldid).value = stringToReplace;
}
