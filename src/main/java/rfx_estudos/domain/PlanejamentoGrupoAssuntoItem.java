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

@Entity
@Table(name = "planejamento_grupo_assunto_itens")
public class PlanejamentoGrupoAssuntoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "planejamento_grupo_assunto_fk", nullable = false)
    private PlanejamentoGrupoAssunto planejamentoGrupoAssunto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assunto_fk", nullable = false)
    private Assunto assunto;

    @Column(nullable = false)
    private Short peso = 3;

    private Integer ordem;

    @Column(name = "data_prevista")
    private LocalDate dataPrevista;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    public PlanejamentoGrupoAssuntoItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PlanejamentoGrupoAssunto getPlanejamentoGrupoAssunto() {
        return planejamentoGrupoAssunto;
    }

    public void setPlanejamentoGrupoAssunto(
            PlanejamentoGrupoAssunto planejamentoGrupoAssunto) {
        this.planejamentoGrupoAssunto = planejamentoGrupoAssunto;
    }

    public Assunto getAssunto() { return assunto; }
    public void setAssunto(Assunto assunto) { this.assunto = assunto; }

    public Short getPeso() { return peso; }
    public void setPeso(Short peso) { this.peso = peso; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public LocalDate getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
