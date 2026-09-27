package rfx_estudos.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.PlanejamentoItem;

import java.util.List;

@Repository
public interface PlanejamentoItemRepository extends JpaRepository<PlanejamentoItem, Long> {

    @EntityGraph(attributePaths = {"planejamento", "materia", "assunto", "materialTipo"})
    List<PlanejamentoItem> findByPlanejamentoIdOrderByOrdemAscIdAsc(Long planejamentoId);
}
