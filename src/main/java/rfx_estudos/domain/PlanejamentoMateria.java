package rfx_estudos.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "planejamento_materias",
    uniqueConstraints = @UniqueConstraint(columnNames = {"planejamento_fk", "materia_fk"})
)
public class PlanejamentoMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "planejamento_fk", nullable = false)
    private Planejamento planejamento;

    @ManyToOne
    @JoinColumn(name = "materia_fk", nullable = false)
    private Materia materia;

    @Column(length = 50)
    private String prioridade = "Media";

    @Column(name = "data_prevista")
    private LocalDate dataPrevista;

    @Column(length = 50)
    private String status = "Pendente";

    @Column(name = "data_finalizacao")
    private LocalDate dataFinalizacao;

    @Column(name = "ordem")
    private Integer ordem;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    public PlanejamentoMateria() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Planejamento getPlanejamento() { return planejamento; }
    public void setPlanejamento(Planejamento planejamento) { this.planejamento = planejamento; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }

    public LocalDate getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDataFinalizacao() { return dataFinalizacao; }
    public void setDataFinalizacao(LocalDate dataFinalizacao) { this.dataFinalizacao = dataFinalizacao; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
