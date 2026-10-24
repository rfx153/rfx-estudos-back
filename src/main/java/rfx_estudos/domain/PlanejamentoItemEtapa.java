package rfx_estudos.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "planejamento_item_etapas")
public class PlanejamentoItemEtapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "planejamento_grupo_assunto_item_fk", nullable = false)
    private PlanejamentoGrupoAssuntoItem planejamentoGrupoAssuntoItem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_registro_fk", nullable = false)
    private TipoRegistro tipoRegistro;

    @Column(nullable = false)
    private Boolean concluida = false;

    @Column(name = "concluida_manualmente", nullable = false)
    private Boolean concluidaManualmente = false;

    private Integer ordem;

    @Column(name = "data_prevista")
    private LocalDate dataPrevista;

    @Column(name = "data_concluida")
    private LocalDate dataConcluida;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Transient
    private Boolean iniciada = false;

    public PlanejamentoItemEtapa() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PlanejamentoGrupoAssuntoItem getPlanejamentoGrupoAssuntoItem() {
        return planejamentoGrupoAssuntoItem;
    }

    public void setPlanejamentoGrupoAssuntoItem(
            PlanejamentoGrupoAssuntoItem planejamentoGrupoAssuntoItem) {
        this.planejamentoGrupoAssuntoItem = planejamentoGrupoAssuntoItem;
    }

    public TipoRegistro getTipoRegistro() { return tipoRegistro; }
    public void setTipoRegistro(TipoRegistro tipoRegistro) { this.tipoRegistro = tipoRegistro; }

    public Boolean getConcluida() { return concluida; }
    public void setConcluida(Boolean concluida) { this.concluida = concluida; }

    public Boolean getConcluidaManualmente() { return concluidaManualmente; }
    public void setConcluidaManualmente(Boolean concluidaManualmente) {
        this.concluidaManualmente = concluidaManualmente;
    }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public LocalDate getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }

    public LocalDate getDataConcluida() { return dataConcluida; }
    public void setDataConcluida(LocalDate dataConcluida) { this.dataConcluida = dataConcluida; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Boolean getIniciada() { return iniciada; }
    public void setIniciada(Boolean iniciada) { this.iniciada = iniciada; }
}
