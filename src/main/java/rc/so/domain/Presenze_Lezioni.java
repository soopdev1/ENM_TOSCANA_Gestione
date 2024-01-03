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
    @NamedQuery(name = "presenzelezioni.progetto", query = "SELECT u FROM Presenze_Lezioni u WHERE u.progetto=:progetto ORDER BY u.datalezione,u.orainizio"),
    @NamedQuery(name = "presenzelezioni.lezioni", query = "SELECT u FROM Presenze_Lezioni u WHERE u.lezioneriferimento=:lezioneriferimento")
})
@Entity
@Table(name = "presenzelezioni")
public class Presenze_Lezioni implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "idpresenzelezioni")
    private Long idpresenzelezioni;
    
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "idprogetto")
    private ProgettiFormativi progetto;
    
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "idlezioneriferimento")
    private Lezioni_Modelli lezioneriferimento;
    
    @Column(name = "datalezione")
    @Temporal(TemporalType.DATE)
    private Date datalezione;
    
    @Column(name = "orainizio")
    private String orainizio;
    
    @Column(name = "orafine")
    private String orafine;
    
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "iddocente")
    private Docenti docente;
    
    @Column(name = "datainserimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datainserimento;
    
    @Column(name = "pathdocumento", columnDefinition = "LONGTEXT")
    private String pathdocumento;

    public Presenze_Lezioni() {
    }

    public String getPathdocumento() {
        return pathdocumento;
    }

    public void setPathdocumento(String pathdocumento) {
        this.pathdocumento = pathdocumento;
    }

    public Long getIdpresenzelezioni() {
        return idpresenzelezioni;
    }

    public void setIdpresenzelezioni(Long idpresenzelezioni) {
        this.idpresenzelezioni = idpresenzelezioni;
    }

    public ProgettiFormativi getProgetto() {
        return progetto;
    }

    public void setProgetto(ProgettiFormativi progetto) {
        this.progetto = progetto;
    }

    public Lezioni_Modelli getLezioneriferimento() {
        return lezioneriferimento;
    }

    public void setLezioneriferimento(Lezioni_Modelli lezioneriferimento) {
        this.lezioneriferimento = lezioneriferimento;
    }

    public Date getDatalezione() {
        return datalezione;
    }

    public void setDatalezione(Date datalezione) {
        this.datalezione = datalezione;
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

    public Docenti getDocente() {
        return docente;
    }

    public void setDocente(Docenti docente) {
        this.docente = docente;
    }

    public Date getDatainserimento() {
        return datainserimento;
    }

    public void setDatainserimento(Date datainserimento) {
        this.datainserimento = datainserimento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.idpresenzelezioni);
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
        final Presenze_Lezioni other = (Presenze_Lezioni) obj;
        return Objects.equals(this.idpresenzelezioni, other.idpresenzelezioni);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Presenze_Lezioni{");
        sb.append("idpresenzelezioni=").append(idpresenzelezioni);
        sb.append(", datalezione=").append(datalezione);
        sb.append(", orainizio=").append(orainizio);
        sb.append(", orafine=").append(orafine);
        sb.append(", docente=").append(docente);
        sb.append(", datainserimento=").append(datainserimento);
        sb.append(", pathdocumento=").append(pathdocumento);
        sb.append('}');
        return sb.toString();
    }

    
    
    
}
