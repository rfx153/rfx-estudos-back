package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rfx_estudos.domain.PlanejamentoGrupoMateria;

public interface PlanejamentoGrupoMateriaRepository
        extends JpaRepository<PlanejamentoGrupoMateria, Long> {

    List<PlanejamentoGrupoMateria> findByPlanejamentoIdOrderByOrdemAsc(Long planejamentoId);

    Optional<PlanejamentoGrupoMateria> findByIdAndPlanejamentoId(Long id, Long planejamentoId);
}
