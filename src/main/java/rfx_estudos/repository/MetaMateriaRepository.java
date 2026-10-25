package rfx_estudos.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rfx_estudos.domain.MetaMateria;

public interface MetaMateriaRepository extends JpaRepository<MetaMateria, Long> {

    @EntityGraph(attributePaths = "materia")
    List<MetaMateria> findAllByOrderByMateriaNomeAsc();

    @EntityGraph(attributePaths = "materia")
    Optional<MetaMateria> findByMateriaId(Long materiaId);

    @Query(value = """
            SELECT mm.id AS id,
                   m.id AS "materiaId",
                   m.nome AS "materiaNome",
                   mm.meta_semanal_minutos AS "metaSemanalMinutos",
                   COALESCE(SUM(EXTRACT(EPOCH FROM r.tempo_estudado) / 60), 0)::BIGINT
                       AS "tempoRealizadoMinutos"
              FROM metas_materias mm
              JOIN materias m ON m.id = mm.materia_fk
              LEFT JOIN registros r ON r.materia_fk = m.id
                   AND r.data_estudo BETWEEN :inicio AND :fim
             GROUP BY mm.id, m.id, m.nome, mm.meta_semanal_minutos
             ORDER BY m.nome
            """, nativeQuery = true)
    List<ResumoMetaMateriaProjection> resumirPorPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);

    interface ResumoMetaMateriaProjection {
        Long getId();
        Long getMateriaId();
        String getMateriaNome();
        Integer getMetaSemanalMinutos();
        Long getTempoRealizadoMinutos();
    }
}
