/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var context = document.getElementById("calendarjs").getAttribute("data-context");

$(document).ready(function () {

    populatedatafine(document.getElementById('orai'));
    checkorariomax();
});


function checkorariomax() {
    var orai = $('#orai').val();
    var oraf = $('#oraf').val();
    $('.sel-presenza').each(function (i, obj) {
        if (obj.id !== "") {
            $('#' + obj.id).empty();
        }
    });
    try {
        var date1 = new Date('1/1/2000 ' + orai + ':00');
        var date2 = new Date('1/1/2000 ' + oraf + ':00');


            console.log(date1);
            console.log(date2);


        $('.sel-presenza').each(function (i, obj) {
            var idoggetto = new String(obj.id).trim();
            if (idoggetto !== "") {

                $('#' + idoggetto).select2("enable", true);
                $('#' + idoggetto).prop('disabled', false);

                var idallievo = idoggetto.split('_')[1];
                //var attivo = $('#statusall_' + idallievo).val();
                var sino = $('#sino_' + idallievo).val();
                var startoi = $('#startoi_' + idallievo).val();
                var startof = $('#startof_' + idallievo).val();
                
//                alert(attivo);
                
                if (sino === "0") {
                    var no1 = new Option("--", "--", false, false);
                    $('#' + idoggetto).append(no1);
                    $('#' + idoggetto).select2("enable", false);
                    $('#' + idoggetto).prop('disabled', true);
                } else {
                    if (idoggetto.startsWith("oraf")) {
                        $('#' + idoggetto).val(null).trigger('change');
                    }
                    var selectedind = 1;
                    for (var i = 8; i < 22; i++) {
                        var v1 = new Date('1/1/2000 ' + i + ':00:00');
                        var v2 = new Date('1/1/2000 ' + i + ':30:00');
                        var newOption1 = null;
                        var newOption2 = null;
                        if (v1 >= date1 && v1 <= date2) {
                            var out1 = i + ":00";
                            if (i < 10) {
                                out1 = "0" + i + ":00";
                            }
                            newOption1 = out1;
                            selectedind = out1;
                        }
                        if (v2 >= date1 && v2 <= date2) {
                            var out1 = i + ":30";
                            if (i < 10) {
                                out1 = "0" + i + ":30";
                            }
                            newOption2 = out1;
                            selectedind = out1;
                        }
                        if (newOption1 !== null) {
//                        if (idoggetto.startsWith("oraf")) {
//                            var no1 = new Option(newOption1, newOption1, false, false);
//                            $('#' + idoggetto).append(no1).trigger('change');
//                        } else {
                            var no1 = new Option(newOption1, newOption1, false, false);
                            $('#' + idoggetto).append(no1);
//                        }
                        }
                        if (newOption2 !== null) {
//                        if (idoggetto.startsWith("oraf")) {
//                            var no2 = new Option(newOption2, newOption2, false, false);
//                            $('#' + idoggetto).append(no2).trigger('change');
//                        } else {
                            var no2 = new Option(newOption2, newOption2, false, false);
                            $('#' + idoggetto).append(no2);
//                        }
                        }
                    }

                    if (idoggetto.startsWith("oraf")) {
                        $('#' + idoggetto).val(selectedind).trigger('change');
                    }
                }
                
                

                if($('#modify_' + idallievo).val()){
                    
                    if (startoi !== "" && idoggetto.startsWith("orai")) {
                        $('#' + idoggetto).val(startoi).trigger('change');
                    }
                    if (startof !== "" && idoggetto.startsWith("oraf")) {
                        $('#' + idoggetto).val(startof).trigger('change');
                    }
                }


//                if (startoi !== "" && idoggetto.startsWith("orai")) {
//                    $('#' + idoggetto).val(startoi).trigger('change');
//                }
//                if (startof !== "" && idoggetto.startsWith("oraf")) {
//                    $('#' + idoggetto).val(startof).trigger('change');
//                }
            }
        });


    } catch (e) {
        console.log(e);
    }
}

function checkdatisalvati() {
    var tipolez = $('#tipolez').val();
    var orai = $('#orai').val();
    var oraf = $('#oraf').val();
    var residue = $('#modulo option:selected').attr('data-res');
    var residuefad = $('#modulo option:selected').attr('data-resfad');
    var date1 = new Date('1/1/2000 ' + orai + ':00');
    var date2 = new Date('1/1/2000 ' + oraf + ':00');

    var diff = (date2 - date1) / 3600000;
    var labelres = residue;
    var check = diff > residue;
    if (tipolez === "FAD") {
        check = diff > residuefad;
        labelres = residuefad;
    }

    if (check) {

        var d1 = Intl.NumberFormat('it-IT', {
            style: 'decimal'
        });

        $.alert({
            title: "Errore durante l'operazione!",
            content: "Per il modulo selezionato e per il tipo di lezione selezionato il numero di ore programmate <b>(" + d1.format(diff) + ")</b> supera il numero di ore residue: <b><u>" + d1.format(labelres) + "</u></b>. Controllare.",
            type: 'red',
            typeAnimated: true,
            theme: 'bootstrap',
            columnClass: 'col-md-9',
            buttons: {
                confirm: {
                    text: 'OK',
                    btnClass: 'btn-red'
                }
            }
        });
        return false;
    }
    return true;
}

function populatedatafine(component) {

    $('#oraf').val(null).trigger('change');
    $('#oraf').empty();

    var start = component.value;
    if (start !== "") {

        var hh = start.split(":")[0];
        var mm = start.split(":")[1];

        var hhdest = parseInt(hh, 10) + parseInt($('#orastandardlezione').val(), 10);

        if (hhdest > 20) {
            $.alert({
                title: "Errore durante l'operazione!",
                content: "Ora di inizio errata, in quanto non consentirebbe di completare la lezione nell'intervallo orario prestabilito (08 - 20). Controllare.",
                type: 'red',
                typeAnimated: true,
                theme: 'bootstrap',
                columnClass: 'col-md-9',
                buttons: {
                    confirm: {
                        text: 'OK',
                        btnClass: 'btn-red'
                    }
                }
            });
            return false;
        } else if (hhdest < 10) {
            hhdest = "0" + hhdest;
        }
        var out1 = hhdest + ":" + mm;
        var newOption = new Option(out1, out1, false, false);
        $('#oraf').append(newOption).trigger('change');
        $('#oraf').on("change", function (e) {
            checkorariomax();
        });
    }
}