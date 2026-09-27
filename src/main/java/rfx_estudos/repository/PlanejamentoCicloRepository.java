package rfx_estudos.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.PlanejamentoCiclo;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanejamentoCicloRepository extends JpaRepository<PlanejamentoCiclo, Long> {

    @EntityGraph(attributePaths = {"planejamento", "ciclo"})
    List<PlanejamentoCiclo> findByPlanejamentoIdOrderByCicloNomeAsc(Long planejamentoId);

    Optional<PlanejamentoCiclo> findByPlanejamentoIdAndCicloId(Long planejamentoId, Long cicloId);
}
