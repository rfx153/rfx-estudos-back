package rfx_estudos.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rfx_estudos.domain.MetaSemanal;

public interface MetaSemanalRepository extends JpaRepository<MetaSemanal, Long> {

    Optional<MetaSemanal> findByInicioSemana(LocalDate inicioSemana);

    @Query(value = """
            SELECT ms.id AS id,
                   ms.inicio_semana AS "inicioSemana",
                   ms.meta_minutos AS "metaMinutos",
                   COALESCE(SUM(EXTRACT(EPOCH FROM r.tempo_estudado) / 60), 0)::BIGINT
                       AS "tempoRealizadoMinutos"
              FROM metas_semanais ms
              LEFT JOIN registros r
                ON r.data_estudo BETWEEN ms.inicio_semana AND (ms.inicio_semana + 6)
             WHERE ms.inicio_semana = :inicioSemana
             GROUP BY ms.id, ms.inicio_semana, ms.meta_minutos
            """, nativeQuery = true)
    List<ResumoMetaSemanalProjection> resumir(
            @Param("inicioSemana") LocalDate inicioSemana);

    interface ResumoMetaSemanalProjection {
        Long getId();
        LocalDate getInicioSemana();
        Integer getMetaMinutos();
        Long getTempoRealizadoMinutos();
    }
}
