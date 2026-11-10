package rfx_estudos.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "metas_diarias",
        uniqueConstraints = @UniqueConstraint(name = "uk_metas_diarias_data", columnNames = "data"))
public class MetaDiaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "meta_minutos", nullable = false)
    private Integer metaMinutos;

    public MetaDiaria() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Integer getMetaMinutos() { return metaMinutos; }
    public void setMetaMinutos(Integer metaMinutos) { this.metaMinutos = metaMinutos; }
}
