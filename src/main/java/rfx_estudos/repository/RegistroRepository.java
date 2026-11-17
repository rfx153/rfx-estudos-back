package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.Registro;
import rfx_estudos.dto.PlanejamentoAproveitamentoDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    
    // Busca todo o seu histórico ordenado pela data mais recente
    List<Registro> findAllByOrderByDataEstudoDesc();

    List<Registro> findByDataEstudoOrderByDataEstudoDesc(LocalDate dataEstudo);

    List<Registro> findByOrderByDataEstudoDesc(Pageable pageable);

    List<Registro> findByMateriaIdOrderByDataEstudoDesc(Long materiaId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "materia",
            "assunto",
            "materialGuia",
            "planejamento",
            "tipoRegistro",
            "planejamentoEtapa"
    })
    @Query("select r from Registro r")
    Page<Registro> buscarHistorico(Pageable pageable);

    @EntityGraph(attributePaths = {
            "materia",
            "assunto",
            "materialGuia",
            "planejamento",
            "tipoRegistro",
            "planejamentoEtapa"
    })
    @Query("select r from Registro r where r.materia.id = :materiaId")
    Page<Registro> buscarHistoricoPorMateria(@Param("materiaId") Long materiaId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "materia",
            "assunto",
            "materialGuia",
            "planejamento",
            "tipoRegistro",
            "planejamentoEtapa"
    })
    @Query("""
            select r
            from Registro r
            where (:materiaId is null or r.materia.id = :materiaId)
              and (:assuntoId is null or r.assunto.id = :assuntoId)
              and (:materialGuiaId is null or r.materialGuia.id = :materialGuiaId)
              and (:tipoRegistroId is null or r.tipoRegistro.id = :tipoRegistroId)
            """)
    Page<Registro> buscarHistoricoFiltrado(
            @Param("materiaId") Long materiaId,
            @Param("assuntoId") Long assuntoId,
            @Param("materialGuiaId") Long materialGuiaId,
            @Param("tipoRegistroId") Long tipoRegistroId,
            Pageable pageable);

    Optional<Registro> findFirstByPlanejamentoEtapaIdAndConcluidoTrueOrderByDataEstudoDescIdDesc(
            Long planejamentoEtapaId);

    boolean existsByPlanejamentoEtapaId(Long planejamentoEtapaId);

    @Query("""
            select distinct r.planejamentoEtapa.id
            from Registro r
            where r.planejamentoEtapa.planejamentoGrupoAssuntoItem.planejamentoGrupoAssunto
                    .planejamentoMateria.planejamento.id = :planejamentoId
            """)
    List<Long> buscarIdsEtapasIniciadas(
            @Param("planejamentoId") Long planejamentoId);

    @Query("""
            select new rfx_estudos.dto.PlanejamentoAproveitamentoDTO(
                    r.assunto.id,
                    coalesce(sum(r.questoesFeitas), 0),
                    coalesce(sum(r.questoesAcertadas), 0))
            from Registro r
            where r.planejamento.id = :planejamentoId
            group by r.assunto.id
            """)
    List<PlanejamentoAproveitamentoDTO> somarQuestoesPorPlanejamento(
            @Param("planejamentoId") Long planejamentoId);
}
