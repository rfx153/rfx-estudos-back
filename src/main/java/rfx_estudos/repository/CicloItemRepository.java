package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rfx_estudos.domain.CicloItem;

public interface CicloItemRepository extends JpaRepository<CicloItem, Long> {

    @EntityGraph(attributePaths = {"materia", "planejamentoGrupoMateria", "registroConclusao"})
    List<CicloItem> findByCicloIdOrderByOrdemAsc(Long cicloId);

    Optional<CicloItem> findByIdAndCicloId(Long id, Long cicloId);

    boolean existsByCicloIdAndMateriaId(Long cicloId, Long materiaId);

    boolean existsByCicloIdAndPlanejamentoGrupoMateriaId(Long cicloId, Long grupoId);

    List<CicloItem> findByRegistroConclusaoId(Long registroId);

    @Query(value = """
            SELECT DISTINCT ci.*
              FROM ciclo_itens ci
              JOIN ciclos c ON c.id = ci.ciclo_fk
              LEFT JOIN planejamento_grupos_materias pg
                ON pg.id = ci.planejamento_grupo_materia_fk
              LEFT JOIN planejamento_materias pm
                ON pm.planejamento_grupo_materia_fk = pg.id
             WHERE c.ativo = TRUE
               AND ci.ativo = TRUE
               AND ci.registro_conclusao_fk IS NULL
               AND (
                    ci.materia_fk = :materiaId
                    OR (
                        :planejamentoId IS NOT NULL
                        AND pg.planejamento_fk = :planejamentoId
                        AND pm.materia_fk = :materiaId
                    )
               )
            """, nativeQuery = true)
    List<CicloItem> buscarPendentesDoRegistro(
            @Param("materiaId") Long materiaId,
            @Param("planejamentoId") Long planejamentoId);

    @Modifying
    @Query(value = "UPDATE ciclo_itens SET ordem = -ordem WHERE ciclo_fk = :cicloId", nativeQuery = true)
    void tornarOrdensTemporarias(@Param("cicloId") Long cicloId);

    @Modifying
    @Query(value = "UPDATE ciclo_itens SET ordem = :ordem WHERE id = :id AND ciclo_fk = :cicloId", nativeQuery = true)
    int atualizarOrdem(
            @Param("cicloId") Long cicloId,
            @Param("id") Long id,
            @Param("ordem") Integer ordem);
}
