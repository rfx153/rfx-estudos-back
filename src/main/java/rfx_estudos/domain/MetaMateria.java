package rfx_estudos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "metas_materias")
public class MetaMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "materia_fk", nullable = false, unique = true)
    private Materia materia;

    @Column(name = "meta_semanal_minutos", nullable = false)
    private Integer metaSemanalMinutos;

    public MetaMateria() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public Integer getMetaSemanalMinutos() { return metaSemanalMinutos; }
    public void setMetaSemanalMinutos(Integer metaSemanalMinutos) {
        this.metaSemanalMinutos = metaSemanalMinutos;
    }
}
