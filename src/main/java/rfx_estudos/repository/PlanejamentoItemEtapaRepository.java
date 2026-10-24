package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rfx_estudos.domain.PlanejamentoItemEtapa;

public interface PlanejamentoItemEtapaRepository
        extends JpaRepository<PlanejamentoItemEtapa, Long> {

    List<PlanejamentoItemEtapa> findByPlanejamentoGrupoAssuntoItemIdOrderByOrdemAsc(
            Long planejamentoGrupoAssuntoItemId);

    Optional<PlanejamentoItemEtapa> findByIdAndPlanejamentoGrupoAssuntoItemId(
            Long id,
            Long planejamentoGrupoAssuntoItemId);

    @Query("""
            select etapa
            from PlanejamentoItemEtapa etapa
            join etapa.planejamentoGrupoAssuntoItem item
            join item.planejamentoGrupoAssunto grupo
            join grupo.planejamentoMateria planejamentoMateria
            where planejamentoMateria.planejamento.id = :planejamentoId
              and item.assunto.id = :assuntoId
              and etapa.tipoRegistro.id = :tipoRegistroId
            """)
    List<PlanejamentoItemEtapa> findByPlanejamentoAssuntoETipoRegistro(
            @Param("planejamentoId") Long planejamentoId,
            @Param("assuntoId") Long assuntoId,
            @Param("tipoRegistroId") Long tipoRegistroId);
}
