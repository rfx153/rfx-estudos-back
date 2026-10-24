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
@Table(name = "planejamento_materias")
public class PlanejamentoMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "planejamento_fk", nullable = false)
    private Planejamento planejamento;

    @ManyToOne
    @JoinColumn(name = "planejamento_grupo_materia_fk")
    private PlanejamentoGrupoMateria planejamentoGrupoMateria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "materia_fk", nullable = false)
    private Materia materia;

    @Column(nullable = false)
    private Short peso = 3;

    private Integer ordem;

    @Column(name = "data_finalizar_prevista")
    private LocalDate dataFinalizarPrevista;

    @Column(name = "data_finalizado")
    private LocalDate dataFinalizado;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    public PlanejamentoMateria() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Planejamento getPlanejamento() { return planejamento; }
    public void setPlanejamento(Planejamento planejamento) { this.planejamento = planejamento; }

    public PlanejamentoGrupoMateria getPlanejamentoGrupoMateria() {
        return planejamentoGrupoMateria;
    }
    public void setPlanejamentoGrupoMateria(PlanejamentoGrupoMateria planejamentoGrupoMateria) {
        this.planejamentoGrupoMateria = planejamentoGrupoMateria;
    }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public Short getPeso() { return peso; }
    public void setPeso(Short peso) { this.peso = peso; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public LocalDate getDataFinalizarPrevista() { return dataFinalizarPrevista; }
    public void setDataFinalizarPrevista(LocalDate dataFinalizarPrevista) {
        this.dataFinalizarPrevista = dataFinalizarPrevista;
    }

    public LocalDate getDataFinalizado() { return dataFinalizado; }
    public void setDataFinalizado(LocalDate dataFinalizado) { this.dataFinalizado = dataFinalizado; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
