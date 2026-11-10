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
        name = "metas_semanais",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_metas_semanais_inicio_semana",
                columnNames = "inicio_semana"))
public class MetaSemanal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inicio_semana", nullable = false)
    private LocalDate inicioSemana;

    @Column(name = "meta_minutos", nullable = false)
    private Integer metaMinutos;

    public MetaSemanal() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getInicioSemana() { return inicioSemana; }
    public void setInicioSemana(LocalDate inicioSemana) { this.inicioSemana = inicioSemana; }

    public Integer getMetaMinutos() { return metaMinutos; }
    public void setMetaMinutos(Integer metaMinutos) { this.metaMinutos = metaMinutos; }
}
