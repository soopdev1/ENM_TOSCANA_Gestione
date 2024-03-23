/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rc.so.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author dolivo
 */
@Entity
@Table(name = "maschera_m5")
@NamedQueries(value = {
    @NamedQuery(name = "m5.byPF", query = "SELECT m FROM MascheraM5 m WHERE m.progetto_formativo=:progetto_formativo"),
    @NamedQuery(name = "m5.byAllievo", query = "SELECT m FROM MascheraM5 m WHERE m.allievo=:allievo"),
    @NamedQuery(name = "m5.byAllievoPFnotNULL", query = "SELECT m FROM MascheraM5 m WHERE m.allievo=:allievo and m.progetto_formativo <> null")
})
public class MascheraM5 implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "progetto_formativo")
    private ProgettiFormativi progetto_formativo;

    @ManyToOne
    @JoinColumn(name = "allievo")
    private Allievi allievo;

    @Column(name = "m5_grado_completezza")
    private String grado_completezza;
    
    @Column(name = "m5_probabilita")
    private String probabilita;

    @ManyToOne
    @JoinColumn(name = "m5_forma_giuridica")
    private Formagiuridica forma_giuridica;

    @ManyToOne
    @JoinColumn(name = "m5_ateco")
    private Ateco ateco;

    @Column(name = "m5_sede", columnDefinition = "TINYINT", length = 1)
    private boolean sede;

    @ManyToOne
    @JoinColumn(name = "m5_comune_localizzazione")
    private Comuni comune_localizzazione;

    @Column(name = "m5_totale_fabbisogno")
    private double totale_fabbisogno;
    
    @Column(name = "m5_misura_individuata", columnDefinition = "TINYINT", length = 1)
    private boolean misura_individuata;
    
    @Column(name = "m5_misura_no_motivazione")
    private String misura_no_motivazione;
    
    @Column(name = "m5_misura_si_nome")
    private String misura_si_nome;
    
    @Column(name = "m5_misura_si_tipo")
    private String misura_si_tipo;
    
    @Column(name = "m5_misura_si_motivazione")
    private String misura_si_motivazione;

    @Column(name = "businessplan_path")
    private String businessplan_path;
    @Column(name = "businessplan_presente")
    private boolean businessplan_presente;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", name = "timestamp", insertable = false)
    private Date timestamp;

    public MascheraM5() {
    }

    public String getBusinessplan_path() {
        return businessplan_path;
    }

    public void setBusinessplan_path(String businessplan_path) {
        this.businessplan_path = businessplan_path;
    }

    public boolean isBusinessplan_presente() {
        return businessplan_presente;
    }

    public void setBusinessplan_presente(boolean businessplan_presente) {
        this.businessplan_presente = businessplan_presente;
    }

    public ProgettiFormativi getProgetto_formativo() {
        return progetto_formativo;
    }

    public void setProgetto_formativo(ProgettiFormativi progetto_formativo) {
        this.progetto_formativo = progetto_formativo;
    }

    public String getGrado_completezza() {
        return grado_completezza;
    }

    public void setGrado_completezza(String grado_completezza) {
        this.grado_completezza = grado_completezza;
    }

    public String getProbabilita() {
        return probabilita;
    }

    public void setProbabilita(String probabilita) {
        this.probabilita = probabilita;
    }

    public Formagiuridica getForma_giuridica() {
        return forma_giuridica;
    }

    public void setForma_giuridica(Formagiuridica forma_giuridica) {
        this.forma_giuridica = forma_giuridica;
    }

    public Ateco getAteco() {
        return ateco;
    }

    public void setAteco(Ateco ateco) {
        this.ateco = ateco;
    }

    public boolean isSede() {
        return sede;
    }

    public void setSede(boolean sede) {
        this.sede = sede;
    }

    public double getTotale_fabbisogno() {
        return totale_fabbisogno;
    }

    public void setTotale_fabbisogno(double totale_fabbisogno) {
        this.totale_fabbisogno = totale_fabbisogno;
    }

    public boolean isMisura_individuata() {
        return misura_individuata;
    }

    public void setMisura_individuata(boolean misura_individuata) {
        this.misura_individuata = misura_individuata;
    }

    public String getMisura_no_motivazione() {
        return misura_no_motivazione;
    }

    public void setMisura_no_motivazione(String misura_no_motivazione) {
        this.misura_no_motivazione = misura_no_motivazione;
    }

    public String getMisura_si_nome() {
        return misura_si_nome;
    }

    public void setMisura_si_nome(String misura_si_nome) {
        this.misura_si_nome = misura_si_nome;
    }

    public String getMisura_si_tipo() {
        return misura_si_tipo;
    }

    public void setMisura_si_tipo(String misura_si_tipo) {
        this.misura_si_tipo = misura_si_tipo;
    }

    public String getMisura_si_motivazione() {
        return misura_si_motivazione;
    }

    public void setMisura_si_motivazione(String misura_si_motivazione) {
        this.misura_si_motivazione = misura_si_motivazione;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Allievi getAllievo() {
        return allievo;
    }

    public void setAllievo(Allievi allievo) {
        this.allievo = allievo;
    }

    public Comuni getComune_localizzazione() {
        return comune_localizzazione;
    }

    public void setComune_localizzazione(Comuni comune_localizzazione) {
        this.comune_localizzazione = comune_localizzazione;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final MascheraM5 other = (MascheraM5) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
