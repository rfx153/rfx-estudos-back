package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rfx_estudos.domain.PlanejamentoGrupoAssunto;

public interface PlanejamentoGrupoAssuntoRepository
        extends JpaRepository<PlanejamentoGrupoAssunto, Long> {

    List<PlanejamentoGrupoAssunto> findByPlanejamentoMateriaIdOrderByOrdemAsc(
            Long planejamentoMateriaId);

    Optional<PlanejamentoGrupoAssunto> findByIdAndPlanejamentoMateriaId(
            Long id,
            Long planejamentoMateriaId);
}
