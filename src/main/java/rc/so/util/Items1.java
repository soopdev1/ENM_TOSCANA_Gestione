/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rcosco
 */
public class Items1 {
    
    String cod;
    int codice;
    String descrizione;

    public Items1(String cod, String descrizione) {
        this.cod = cod;
        this.descrizione = descrizione;
    }
    
    public Items1(int codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }
    
    
    public static List<Items1> formatAction() {
        List<Items1> out = new ArrayList<>();
        out.add(new Items1("L1", "Login"));
        out.add(new Items1("L2", "Logout"));
        out.add(new Items1("L3", "Logout"));
        out.add(new Items1("L3", "Logout"));
        out.add(new Items1("L4", "Logout"));
        out.add(new Items1("L5", "Chiusura stanza"));
        out.add(new Items1("IN", "Info"));
        return out;
    }
    
    
    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }
    
    public int getCodice() {
        return codice;
    }

    public void setCodice(int codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

}
