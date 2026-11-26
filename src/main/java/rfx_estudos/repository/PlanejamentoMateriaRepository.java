package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rfx_estudos.domain.PlanejamentoMateria;
import rfx_estudos.dto.PlanejamentoDestinoRegistroLinhaDTO;

public interface PlanejamentoMateriaRepository extends JpaRepository<PlanejamentoMateria, Long> {

    @EntityGraph(attributePaths = {
            "planejamento",
            "planejamentoGrupoMateria",
            "planejamentoGrupoMateria.planejamento",
            "materia",
            "materia.categorias"
    })
    List<PlanejamentoMateria> findByPlanejamentoIdOrderByOrdemAsc(Long planejamentoId);

    Optional<PlanejamentoMateria> findByIdAndPlanejamentoId(Long id, Long planejamentoId);

    List<PlanejamentoMateria> findByPlanejamentoIdAndMateriaIdOrderByOrdemAsc(
            Long planejamentoId,
            Long materiaId);

    @Query(value = """
            SELECT
                planejamento_materia.id AS "planejamentoMateriaId",
                destino.etapa_id AS "etapaId",
                planejamento.nome AS "planejamentoNome",
                bloco_materia.nome AS "blocoMateriaNome",
                materia.nome AS "materiaNome",
                planejamento_materia.ordem AS "materiaOrdem",
                destino.grupo_assunto_nome AS "grupoAssuntoNome"
            FROM planejamento_materias planejamento_materia
            JOIN planejamentos planejamento
              ON planejamento.id = planejamento_materia.planejamento_fk
            JOIN materias materia
              ON materia.id = planejamento_materia.materia_fk
            LEFT JOIN planejamento_grupos_materias bloco_materia
              ON bloco_materia.id = planejamento_materia.planejamento_grupo_materia_fk
            LEFT JOIN LATERAL (
                SELECT
                    COALESCE(grupo.nome, assunto.nome) AS grupo_assunto_nome,
                    (
                        SELECT etapa.id
                        FROM planejamento_item_etapas etapa
                        WHERE etapa.planejamento_grupo_assunto_item_fk = item.id
                          AND etapa.tipo_registro_fk = :tipoRegistroId
                        ORDER BY etapa.ordem NULLS LAST, etapa.id
                        LIMIT 1
                    ) AS etapa_id
                FROM planejamento_grupos_assuntos grupo
                JOIN planejamento_grupo_assunto_itens item
                  ON item.planejamento_grupo_assunto_fk = grupo.id
                JOIN assuntos assunto
                  ON assunto.id = item.assunto_fk
                WHERE grupo.planejamento_materia_fk = planejamento_materia.id
                  AND item.assunto_fk = :assuntoId
                ORDER BY grupo.ordem NULLS LAST, grupo.id,
                         item.ordem NULLS LAST, item.id
                LIMIT 1
            ) destino ON TRUE
            WHERE planejamento_materia.planejamento_fk = :planejamentoId
              AND planejamento_materia.materia_fk = (
                  SELECT assunto.materia_fk
                  FROM assuntos assunto
                  WHERE assunto.id = :assuntoId
                    AND assunto.ativo = TRUE
              )
            ORDER BY planejamento_materia.ordem NULLS LAST, planejamento_materia.id
            """, nativeQuery = true)
    List<PlanejamentoDestinoRegistroLinhaDTO> buscarDestinosRegistro(
            @Param("planejamentoId") Long planejamentoId,
            @Param("assuntoId") Long assuntoId,
            @Param("tipoRegistroId") Long tipoRegistroId);

    boolean existsByPlanejamentoGrupoMateriaId(Long planejamentoGrupoMateriaId);
}
