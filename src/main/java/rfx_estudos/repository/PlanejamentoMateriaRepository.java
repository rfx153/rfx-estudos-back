package rfx_estudos.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.PlanejamentoMateria;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanejamentoMateriaRepository extends JpaRepository<PlanejamentoMateria, Long> {

    @EntityGraph(attributePaths = {"planejamento", "materia"})
    List<PlanejamentoMateria> findByPlanejamentoIdOrderByOrdemAscMateriaNomeAsc(Long planejamentoId);

    Optional<PlanejamentoMateria> findByPlanejamentoIdAndMateriaId(Long planejamentoId, Long materiaId);
}
