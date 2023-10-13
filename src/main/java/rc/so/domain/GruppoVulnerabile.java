/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author dolivo
 */
@Entity
@Table(name = "gruppovulnerabile")
public class GruppoVulnerabile implements Serializable {

    @Id
    @Column(name = "idgruppovulnerabile")
    private int idgruppovulnerabile;
    @Column(name = "descrizione")
    private String descrizione;

    public int getId() {
        return idgruppovulnerabile;
    }

    public void setId(int idgruppovulnerabile) {
        this.idgruppovulnerabile = idgruppovulnerabile;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.idgruppovulnerabile);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GruppoVulnerabile other = (GruppoVulnerabile) obj;
        return Objects.equals(this.idgruppovulnerabile, other.idgruppovulnerabile);
    }

    @Override
    public String toString() {
        return "Condizione_Lavorativa{" + "id=" + idgruppovulnerabile + ", descrizione=" + descrizione + '}';
    }

}
