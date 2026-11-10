package rfx_estudos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "ciclo_itens",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_ciclo_itens_materia", columnNames = {"ciclo_fk", "materia_fk"}),
            @UniqueConstraint(name = "uk_ciclo_itens_grupo", columnNames = {"ciclo_fk", "planejamento_grupo_materia_fk"}),
            @UniqueConstraint(name = "uk_ciclo_itens_ordem", columnNames = {"ciclo_fk", "ordem"})
        })
public class CicloItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "ciclo_fk", nullable = false)
    private Ciclo ciclo;

    @ManyToOne
    @JoinColumn(name = "materia_fk")
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "planejamento_grupo_materia_fk")
    private PlanejamentoGrupoMateria planejamentoGrupoMateria;

    @ManyToOne
    @JoinColumn(name = "registro_conclusao_fk")
    private Registro registroConclusao;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private Boolean ativo = true;

    public CicloItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Ciclo getCiclo() { return ciclo; }
    public void setCiclo(Ciclo ciclo) { this.ciclo = ciclo; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public PlanejamentoGrupoMateria getPlanejamentoGrupoMateria() { return planejamentoGrupoMateria; }
    public void setPlanejamentoGrupoMateria(PlanejamentoGrupoMateria planejamentoGrupoMateria) {
        this.planejamentoGrupoMateria = planejamentoGrupoMateria;
    }

    public Registro getRegistroConclusao() { return registroConclusao; }
    public void setRegistroConclusao(Registro registroConclusao) { this.registroConclusao = registroConclusao; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
