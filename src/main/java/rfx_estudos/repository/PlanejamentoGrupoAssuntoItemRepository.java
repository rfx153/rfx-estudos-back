package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rfx_estudos.domain.PlanejamentoGrupoAssuntoItem;

public interface PlanejamentoGrupoAssuntoItemRepository
        extends JpaRepository<PlanejamentoGrupoAssuntoItem, Long> {

    List<PlanejamentoGrupoAssuntoItem> findByPlanejamentoGrupoAssuntoIdOrderByOrdemAsc(
            Long planejamentoGrupoAssuntoId);

    Optional<PlanejamentoGrupoAssuntoItem> findByIdAndPlanejamentoGrupoAssuntoId(
            Long id,
            Long planejamentoGrupoAssuntoId);

    boolean existsByPlanejamentoGrupoAssuntoId(Long planejamentoGrupoAssuntoId);

    boolean existsByPlanejamentoGrupoAssuntoIdAndIdNot(
            Long planejamentoGrupoAssuntoId,
            Long id);

    boolean existsByPlanejamentoGrupoAssuntoIdAndAssuntoId(
            Long planejamentoGrupoAssuntoId,
            Long assuntoId);

    boolean existsByPlanejamentoGrupoAssuntoIdAndAssuntoIdAndIdNot(
            Long planejamentoGrupoAssuntoId,
            Long assuntoId,
            Long id);

    long countByPlanejamentoGrupoAssuntoId(Long planejamentoGrupoAssuntoId);

    @Query("""
            select item
            from PlanejamentoGrupoAssuntoItem item
            join item.planejamentoGrupoAssunto grupo
            join grupo.planejamentoMateria planejamentoMateria
            where planejamentoMateria.planejamento.id = :planejamentoId
              and item.assunto.id = :assuntoId
            """)
    List<PlanejamentoGrupoAssuntoItem> findByPlanejamentoIdAndAssuntoId(
            @Param("planejamentoId") Long planejamentoId,
            @Param("assuntoId") Long assuntoId);
}
