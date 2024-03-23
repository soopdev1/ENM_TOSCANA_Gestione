/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.db;

import rc.so.util.Utility;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;
import org.joda.time.DateTime;

/**
 *
 * @author rcosco
 */
public class Registro_completo {

    int id, idprogetti_formativi, idsoggetti_attuatori;
    String cip;
    DateTime data;
    String idriunione;
    int numpartecipanti;
    String orainizio, orafine;
    long durata;
    String nud, fase;
    int gruppofaseb;
    String ruolo, cognome, nome, email, orelogin, orelogout;
    long totaleore, totaleorerendicontabili;
    int idutente;

    //modello6
    //docenti
    String cf, fascia;
    long data1, data2, data3, data4, data5, data6, data7, data8, data9, data10, data11, data12;
    long data13, data14, data15, data16, data17, data18, data19, data20, data21, data22, data23, data24;

    //allieviB
    int gruppoB;

    //allieviA
    String datapattogg, domandaammissione, modello5;

    public Registro_completo() {
    }

    public long getData13() {
        return data13;
    }

    public void setData13(long data13) {
        this.data13 = data13;
    }

    public long getData14() {
        return data14;
    }

    public void setData14(long data14) {
        this.data14 = data14;
    }

    public long getData15() {
        return data15;
    }

    public void setData15(long data15) {
        this.data15 = data15;
    }

    public long getData16() {
        return data16;
    }

    public void setData16(long data16) {
        this.data16 = data16;
    }

    public long getData17() {
        return data17;
    }

    public void setData17(long data17) {
        this.data17 = data17;
    }

    public long getData18() {
        return data18;
    }

    public void setData18(long data18) {
        this.data18 = data18;
    }

    public long getData19() {
        return data19;
    }

    public void setData19(long data19) {
        this.data19 = data19;
    }

    public long getData20() {
        return data20;
    }

    public void setData20(long data20) {
        this.data20 = data20;
    }

    public long getData21() {
        return data21;
    }

    public void setData21(long data21) {
        this.data21 = data21;
    }

    public long getData22() {
        return data22;
    }

    public void setData22(long data22) {
        this.data22 = data22;
    }

    public long getData23() {
        return data23;
    }

    public void setData23(long data23) {
        this.data23 = data23;
    }

    public long getData24() {
        return data24;
    }

    public void setData24(long data24) {
        this.data24 = data24;
    }

    public String getDatapattogg() {
        return datapattogg;
    }

    public void setDatapattogg(String datapattogg) {
        this.datapattogg = datapattogg;
    }

    public String getDomandaammissione() {
        return domandaammissione;
    }

    public void setDomandaammissione(String domandaammissione) {
        this.domandaammissione = domandaammissione;
    }

    public String getModello5() {
        return modello5;
    }

    public void setModello5(String modello5) {
        this.modello5 = modello5;
    }

    public int getGruppoB() {
        return gruppoB;
    }

    public void setGruppoB(int gruppoB) {
        this.gruppoB = gruppoB;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getFascia() {
        return fascia;
    }

    public void setFascia(String fascia) {
        this.fascia = fascia;
    }

    public long getData1() {
        return data1;
    }

    public void setData1(long data1) {
        this.data1 = data1;
    }

    public long getData2() {
        return data2;
    }

    public void setData2(long data2) {
        this.data2 = data2;
    }

    public long getData3() {
        return data3;
    }

    public void setData3(long data3) {
        this.data3 = data3;
    }

    public long getData4() {
        return data4;
    }

    public void setData4(long data4) {
        this.data4 = data4;
    }

    public long getData5() {
        return data5;
    }

    public void setData5(long data5) {
        this.data5 = data5;
    }

    public long getData6() {
        return data6;
    }

    public void setData6(long data6) {
        this.data6 = data6;
    }

    public long getData7() {
        return data7;
    }

    public void setData7(long data7) {
        this.data7 = data7;
    }

    public long getData8() {
        return data8;
    }

    public void setData8(long data8) {
        this.data8 = data8;
    }

    public long getData9() {
        return data9;
    }

    public void setData9(long data9) {
        this.data9 = data9;
    }

    public long getData10() {
        return data10;
    }

    public void setData10(long data10) {
        this.data10 = data10;
    }

    public long getData11() {
        return data11;
    }

    public void setData11(long data11) {
        this.data11 = data11;
    }

    public long getData12() {
        return data12;
    }

    public void setData12(long data12) {
        this.data12 = data12;
    }

    public Registro_completo(int id, int idprogetti_formativi, int idsoggetti_attuatori, String cip, DateTime data, String idriunione, int numpartecipanti, String orainizio, String orafine, long durata, String nud, String fase, int gruppofaseb, String ruolo, String cognome, String nome, String email, String orelogin, String orelogout, long totaleore, long totaleorerendicontabili, int idutente) {
        this.id = id;
        this.idprogetti_formativi = idprogetti_formativi;
        this.idsoggetti_attuatori = idsoggetti_attuatori;
        this.cip = cip;
        this.data = data;
        this.idriunione = idriunione;
        this.numpartecipanti = numpartecipanti;
        this.orainizio = orainizio;
        this.orafine = orafine;
        this.durata = durata;
        this.nud = nud;
        this.fase = fase;
        this.gruppofaseb = gruppofaseb;
        this.ruolo = ruolo;
        this.cognome = cognome;
        this.nome = nome;
        this.email = email;
        this.orelogin = orelogin;
        this.orelogout = orelogout;
        this.totaleore = totaleore;
        if (Utility.demoversion && totaleorerendicontabili > 18000000L) {
            this.totaleorerendicontabili = 18000000L;
        } else {
            this.totaleorerendicontabili = totaleorerendicontabili;
        }

        this.idutente = idutente;
    }

    public int getIdutente() {
        return idutente;
    }

    public void setIdutente(int idutente) {
        this.idutente = idutente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdprogetti_formativi() {
        return idprogetti_formativi;
    }

    public void setIdprogetti_formativi(int idprogetti_formativi) {
        this.idprogetti_formativi = idprogetti_formativi;
    }

    public int getIdsoggetti_attuatori() {
        return idsoggetti_attuatori;
    }

    public void setIdsoggetti_attuatori(int idsoggetti_attuatori) {
        this.idsoggetti_attuatori = idsoggetti_attuatori;
    }

    public String getCip() {
        return cip;
    }

    public void setCip(String cip) {
        this.cip = cip;
    }

    public DateTime getData() {
        return data;
    }

    public void setData(DateTime data) {
        this.data = data;
    }

    public String getIdriunione() {
        return idriunione;
    }

    public void setIdriunione(String idriunione) {
        this.idriunione = idriunione;
    }

    public int getNumpartecipanti() {
        return numpartecipanti;
    }

    public void setNumpartecipanti(int numpartecipanti) {
        this.numpartecipanti = numpartecipanti;
    }

    public String getOrainizio() {
        return orainizio;
    }

    public void setORainizio(String rainizio) {
        this.orainizio = rainizio;
    }

    public String getOrafine() {
        return orafine;
    }

    public void setOrafine(String orafine) {
        this.orafine = orafine;
    }

    public long getDurata() {
        return durata;
    }

    public void setDurata(long durata) {
        this.durata = durata;
    }

    public String getNud() {
        return nud;
    }

    public void setNud(String nud) {
        this.nud = nud;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public int getGruppofaseb() {
        return gruppofaseb;
    }

    public void setGruppofaseb(int gruppofaseb) {
        this.gruppofaseb = gruppofaseb;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrelogin() {
        return orelogin;
    }

    public void setOrelogin(String orelogin) {
        this.orelogin = orelogin;
    }

    public String getOrelogout() {
        return orelogout;
    }

    public void setOrelogout(String orelogout) {
        this.orelogout = orelogout;
    }

    public long getTotaleore() {
        return totaleore;
    }

    public void setTotaleore(long totaleore) {
        this.totaleore = totaleore;
    }

    public long getTotaleorerendicontabili() {
        return totaleorerendicontabili;
    }

    public void setTotaleorerendicontabili(long totaleorerendicontabili) {
        this.totaleorerendicontabili = totaleorerendicontabili;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, JSON_STYLE);
    }
}
