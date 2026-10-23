package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rfx_estudos.domain.PlanejamentoGrupoAssuntoItem;

public interface PlanejamentoGrupoAssuntoItemRepository
        extends JpaRepository<PlanejamentoGrupoAssuntoItem, Long> {

    List<PlanejamentoGrupoAssuntoItem> findByPlanejamentoGrupoAssuntoIdOrderByOrdemAsc(
            Long planejamentoGrupoAssuntoId);

    Optional<PlanejamentoGrupoAssuntoItem> findByIdAndPlanejamentoGrupoAssuntoId(
            Long id,
            Long planejamentoGrupoAssuntoId);
}
