package rfx_estudos.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rfx_estudos.domain.MetaDiaria;

public interface MetaDiariaRepository extends JpaRepository<MetaDiaria, Long> {

    Optional<MetaDiaria> findByData(LocalDate data);

    @Query(value = """
            SELECT md.id AS id,
                   md.data AS data,
                   md.meta_minutos AS "metaMinutos",
                   COALESCE(SUM(EXTRACT(EPOCH FROM r.tempo_estudado) / 60), 0)::BIGINT
                       AS "tempoRealizadoMinutos"
              FROM metas_diarias md
              LEFT JOIN registros r ON r.data_estudo = md.data
             WHERE md.data BETWEEN :inicio AND :fim
             GROUP BY md.id, md.data, md.meta_minutos
             ORDER BY md.data
            """, nativeQuery = true)
    List<ResumoMetaDiariaProjection> resumirPorPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);

    interface ResumoMetaDiariaProjection {
        Long getId();
        LocalDate getData();
        Integer getMetaMinutos();
        Long getTempoRealizadoMinutos();
    }
}
