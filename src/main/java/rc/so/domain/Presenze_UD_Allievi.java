/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rc.so.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrator
 */
@NamedQueries(value = {
    @NamedQuery(name = "presenzeud.allievo",
            query = "SELECT u FROM Presenze_UD_Allievi u WHERE u.allievo=:allievo")
})
@Entity
@Table(name = "presenzeudallievi")
public class Presenze_UD_Allievi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "idpresenzeudallievi")
    private Long idpresenzeudallievi;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "idallievi")
    private Allievi allievo;

    @Column(name = "ud")
    private String ud;
    
    @Column(name = "fase")
    private String fase;

    @Column(name = "orepresenze")
    private double orepresenze;
    
    @Column(name = "oretotali")
    private double oretotali;
    
    @Column(name = "completa", columnDefinition = "tinyint(1) default 0")
    private boolean completa;

    @Column(name = "datainizio")
    @Temporal(TemporalType.DATE)
    private Date datainizio;
    
    @Column(name = "datafine")
    @Temporal(TemporalType.DATE)
    private Date datafine;

    @Column(name = "datainserimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datainserimento;
    
    
    public Presenze_UD_Allievi() {
    }

    public Long getIdpresenzeudallievi() {
        return idpresenzeudallievi;
    }

    public void setIdpresenzeudallievi(Long idpresenzeudallievi) {
        this.idpresenzeudallievi = idpresenzeudallievi;
    }

    public Allievi getAllievo() {
        return allievo;
    }

    public void setAllievo(Allievi allievo) {
        this.allievo = allievo;
    }

    public String getUd() {
        return ud;
    }

    public void setUd(String ud) {
        this.ud = ud;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public double getOrepresenze() {
        return orepresenze;
    }

    public void setOrepresenze(double orepresenze) {
        this.orepresenze = orepresenze;
    }

    public double getOretotali() {
        return oretotali;
    }

    public void setOretotali(double oretotali) {
        this.oretotali = oretotali;
    }

    public boolean isCompleta() {
        return completa;
    }

    public void setCompleta(boolean completa) {
        this.completa = completa;
    }

    public Date getDatainizio() {
        return datainizio;
    }

    public void setDatainizio(Date datainizio) {
        this.datainizio = datainizio;
    }

    public Date getDatafine() {
        return datafine;
    }

    public void setDatafine(Date datafine) {
        this.datafine = datafine;
    }

    public Date getDatainserimento() {
        return datainserimento;
    }

    public void setDatainserimento(Date datainserimento) {
        this.datainserimento = datainserimento;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.idpresenzeudallievi);
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
        final Presenze_UD_Allievi other = (Presenze_UD_Allievi) obj;
        return Objects.equals(this.idpresenzeudallievi, other.idpresenzeudallievi);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Presenze_UD_Allievi{");
        sb.append("idpresenzeudallievi=").append(idpresenzeudallievi);
        sb.append(", allievo=").append(allievo);
        sb.append(", ud=").append(ud);
        sb.append(", fase=").append(fase);
        sb.append(", orepresenze=").append(orepresenze);
        sb.append(", oretotali=").append(oretotali);
        sb.append(", completa=").append(completa);
        sb.append(", datainizio=").append(datainizio);
        sb.append(", datafine=").append(datafine);
        sb.append(", datainserimento=").append(datainserimento);
        sb.append('}');
        return sb.toString();
    }
    
    

}
