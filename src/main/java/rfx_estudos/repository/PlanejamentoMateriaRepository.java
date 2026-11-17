package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import rfx_estudos.domain.PlanejamentoMateria;

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

    boolean existsByPlanejamentoGrupoMateriaId(Long planejamentoGrupoMateriaId);
}
