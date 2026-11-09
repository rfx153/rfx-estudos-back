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
        name = "ciclo_materias",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_ciclo_materias_materia", columnNames = {"ciclo_fk", "materia_fk"}),
            @UniqueConstraint(name = "uk_ciclo_materias_ordem", columnNames = {"ciclo_fk", "ordem"})
        })
public class CicloMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "ciclo_fk", nullable = false)
    private Ciclo ciclo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "materia_fk", nullable = false)
    private Materia materia;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private Boolean ativo = true;

    public CicloMateria() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Ciclo getCiclo() { return ciclo; }
    public void setCiclo(Ciclo ciclo) { this.ciclo = ciclo; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
