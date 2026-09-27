package rfx_estudos.domain;

import jakarta.persistence.*;

@Entity
@Table(
    name = "planejamento_ciclos",
    uniqueConstraints = @UniqueConstraint(columnNames = {"planejamento_fk", "ciclo_fk"})
)
public class PlanejamentoCiclo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "planejamento_fk", nullable = false)
    private Planejamento planejamento;

    @ManyToOne
    @JoinColumn(name = "ciclo_fk", nullable = false)
    private Ciclo ciclo;

    public PlanejamentoCiclo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Planejamento getPlanejamento() { return planejamento; }
    public void setPlanejamento(Planejamento planejamento) { this.planejamento = planejamento; }

    public Ciclo getCiclo() { return ciclo; }
    public void setCiclo(Ciclo ciclo) { this.ciclo = ciclo; }
}
