package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rfx_estudos.domain.PlanejamentoGrupoAssunto;
import rfx_estudos.dto.PlanejamentoEstruturaLinhaDTO;

public interface PlanejamentoGrupoAssuntoRepository
        extends JpaRepository<PlanejamentoGrupoAssunto, Long> {

    List<PlanejamentoGrupoAssunto> findByPlanejamentoMateriaIdOrderByOrdemAsc(
            Long planejamentoMateriaId);

    Optional<PlanejamentoGrupoAssunto> findByIdAndPlanejamentoMateriaId(
            Long id,
            Long planejamentoMateriaId);

    @Query(value = """
            SELECT
                grupo.id AS "grupoId",
                grupo.planejamento_materia_fk AS "planejamentoMateriaId",
                grupo.tipo AS "grupoTipo",
                grupo.nome AS "grupoNome",
                grupo.peso AS "grupoPeso",
                grupo.ordem AS "grupoOrdem",
                item.id AS "itemId",
                item.assunto_fk AS "assuntoId",
                assunto.nome AS "assuntoNome",
                item.peso AS "itemPeso",
                item.dificuldade AS "itemDificuldade",
                item.ordem AS "itemOrdem",
                etapa.id AS "etapaId",
                etapa.tipo_registro_fk AS "tipoRegistroId",
                tipo.nome AS "tipoRegistroNome",
                etapa.ordem AS "etapaOrdem",
                etapa.concluida AS "etapaConcluida",
                etapa.data_concluida AS "etapaDataConcluida"
            FROM planejamento_grupos_assuntos grupo
            JOIN planejamento_materias materia_planejamento
              ON materia_planejamento.id = grupo.planejamento_materia_fk
            LEFT JOIN planejamento_grupo_assunto_itens item
              ON item.planejamento_grupo_assunto_fk = grupo.id
            LEFT JOIN assuntos assunto
              ON assunto.id = item.assunto_fk
            LEFT JOIN planejamento_item_etapas etapa
              ON etapa.planejamento_grupo_assunto_item_fk = item.id
            LEFT JOIN tipos_registro tipo
              ON tipo.id = etapa.tipo_registro_fk
            WHERE materia_planejamento.planejamento_fk = :planejamentoId
            ORDER BY materia_planejamento.id, grupo.ordem, grupo.id,
                     item.ordem, item.id, etapa.ordem, etapa.id
            """, nativeQuery = true)
    List<PlanejamentoEstruturaLinhaDTO> buscarEstruturaPorPlanejamentoId(
            @Param("planejamentoId") Long planejamentoId);
}
