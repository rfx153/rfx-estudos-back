package rfx_estudos.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "planejamento_grupos_assuntos")
public class PlanejamentoGrupoAssunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "planejamento_materia_fk", nullable = false)
    private PlanejamentoMateria planejamentoMateria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PlanejamentoGrupoAssuntoTipo tipo;

    @Column(length = 150)
    private String nome;

    @Column(nullable = false)
    private Short peso = 3;

    private Integer ordem;

    @Column(name = "data_prevista")
    private LocalDate dataPrevista;

    @Column(name = "data_finalizada")
    private LocalDate dataFinalizada;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    public PlanejamentoGrupoAssunto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PlanejamentoMateria getPlanejamentoMateria() { return planejamentoMateria; }
    public void setPlanejamentoMateria(PlanejamentoMateria planejamentoMateria) {
        this.planejamentoMateria = planejamentoMateria;
    }

    public PlanejamentoGrupoAssuntoTipo getTipo() { return tipo; }
    public void setTipo(PlanejamentoGrupoAssuntoTipo tipo) { this.tipo = tipo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Short getPeso() { return peso; }
    public void setPeso(Short peso) { this.peso = peso; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public LocalDate getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }

    public LocalDate getDataFinalizada() { return dataFinalizada; }
    public void setDataFinalizada(LocalDate dataFinalizada) { this.dataFinalizada = dataFinalizada; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
