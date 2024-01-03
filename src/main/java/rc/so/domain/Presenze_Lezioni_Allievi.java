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
    @NamedQuery(name = "presenzelezioni.allievo_corso",
            query = "SELECT u FROM Presenze_Lezioni_Allievi u WHERE u.presenzelezioni.progetto=:progetto "
                    + "ORDER BY u.presenzelezioni.datalezione,u.orainizio"),
    @NamedQuery(name = "presenzelezioni.giornata",
            query = "SELECT u FROM Presenze_Lezioni_Allievi u WHERE u.presenzelezioni=:presenzelezioni ORDER BY u.orainizio,u.allievo.cognome"),
    @NamedQuery(name = "presenzelezioni.allievo",
            query = "SELECT u FROM Presenze_Lezioni_Allievi u WHERE u.allievo=:allievo ORDER BY u.presenzelezioni.datalezione,u.orainizio"),
    @NamedQuery(name = "presenzelezioni.allievo_giornata",
            query = "SELECT u FROM Presenze_Lezioni_Allievi u WHERE u.presenzelezioni=:presenzelezioni AND u.allievo=:allievo"),})
@Entity
@Table(name = "presenzelezioniallievi")
public class Presenze_Lezioni_Allievi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "idpresenzelezioniallievi")
    private Long idpresenzelezioniallievi;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "idpresenzelezioni")
    private Presenze_Lezioni presenzelezioni;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "idallievi")
    private Allievi allievo;
    
    @Column(name = "presente", columnDefinition = "tinyint(1) default 1")
    private boolean presente;

    @Column(name = "orainizio")
    private String orainizio;

    @Column(name = "orafine")
    private String orafine;

    @Column(name = "durata")
    private Long durata;

    @Column(name = "datarealelezione")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datainserimento;

    public Presenze_Lezioni_Allievi() {
    }

    
    
    public Long getIdpresenzelezioniallievi() {
        return idpresenzelezioniallievi;
    }

    public void setIdpresenzelezioniallievi(Long idpresenzelezioniallievi) {
        this.idpresenzelezioniallievi = idpresenzelezioniallievi;
    }

    public Presenze_Lezioni getPresenzelezioni() {
        return presenzelezioni;
    }

    public void setPresenzelezioni(Presenze_Lezioni presenzelezioni) {
        this.presenzelezioni = presenzelezioni;
    }

    public Allievi getAllievo() {
        return allievo;
    }

    public void setAllievo(Allievi allievo) {
        this.allievo = allievo;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    public String getOrainizio() {
        return orainizio;
    }

    public void setOrainizio(String orainizio) {
        this.orainizio = orainizio;
    }

    public String getOrafine() {
        return orafine;
    }

    public void setOrafine(String orafine) {
        this.orafine = orafine;
    }

    public Long getDurata() {
        return durata;
    }

    public void setDurata(Long durata) {
        this.durata = durata;
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
        hash = 43 * hash + Objects.hashCode(this.idpresenzelezioniallievi);
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
        final Presenze_Lezioni_Allievi other = (Presenze_Lezioni_Allievi) obj;
        return Objects.equals(this.idpresenzelezioniallievi, other.idpresenzelezioniallievi);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Presenze_Lezioni_Allievi{");
        sb.append("idpresenzelezioniallievi=").append(idpresenzelezioniallievi);
        sb.append(", presenzelezioni=").append(presenzelezioni);
        sb.append(", allievo=").append(allievo);
        sb.append(", presente=").append(presente);
        sb.append(", orainizio=").append(orainizio);
        sb.append(", orafine=").append(orafine);
        sb.append(", durata=").append(durata);
        sb.append(", datainserimento=").append(datainserimento);
        sb.append('}');
        return sb.toString();
    }
    
    
    
    
}
