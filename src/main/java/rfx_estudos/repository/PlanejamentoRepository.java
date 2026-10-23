package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rfx_estudos.domain.Planejamento;

import java.util.List;

public interface PlanejamentoRepository extends JpaRepository<Planejamento, Long> {

    List<Planejamento> findAllByOrderByNomeAsc();
}
