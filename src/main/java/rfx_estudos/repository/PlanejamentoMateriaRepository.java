package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rfx_estudos.domain.PlanejamentoMateria;

public interface PlanejamentoMateriaRepository extends JpaRepository<PlanejamentoMateria, Long> {

    List<PlanejamentoMateria> findByPlanejamentoIdOrderByOrdemAsc(Long planejamentoId);

    Optional<PlanejamentoMateria> findByIdAndPlanejamentoId(Long id, Long planejamentoId);

    Optional<PlanejamentoMateria> findByPlanejamentoIdAndMateriaId(
            Long planejamentoId,
            Long materiaId);

    boolean existsByPlanejamentoGrupoMateriaId(Long planejamentoGrupoMateriaId);
}
