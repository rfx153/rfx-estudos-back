package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.Ciclo;

import java.util.List;

@Repository
public interface CicloRepository extends JpaRepository<Ciclo, Long> {
    List<Ciclo> findAllByOrderByNomeAsc();
}
