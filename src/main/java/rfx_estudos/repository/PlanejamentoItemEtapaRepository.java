package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rfx_estudos.domain.PlanejamentoItemEtapa;

public interface PlanejamentoItemEtapaRepository
        extends JpaRepository<PlanejamentoItemEtapa, Long> {

    List<PlanejamentoItemEtapa> findByPlanejamentoGrupoAssuntoItemIdOrderByOrdemAsc(
            Long planejamentoGrupoAssuntoItemId);

    Optional<PlanejamentoItemEtapa> findByIdAndPlanejamentoGrupoAssuntoItemId(
            Long id,
            Long planejamentoGrupoAssuntoItemId);
}
