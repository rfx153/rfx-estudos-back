package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rfx_estudos.domain.Planejamento;

import java.util.List;
import java.util.Optional;

public interface PlanejamentoRepository extends JpaRepository<Planejamento, Long> {

    List<Planejamento> findAllByOrderByNomeAsc();

    List<Planejamento> findByAtivoTrueOrderByNomeAsc();

    Optional<Planejamento> findByIdAndAtivoTrue(Long id);

    boolean existsByIdAndAtivoTrue(Long id);
}
