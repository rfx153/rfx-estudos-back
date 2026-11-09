package rfx_estudos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rfx_estudos.domain.Ciclo;

public interface CicloRepository extends JpaRepository<Ciclo, Long> {
    List<Ciclo> findAllByOrderByNomeAsc();
}
