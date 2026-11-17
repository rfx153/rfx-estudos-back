package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rfx_estudos.domain.MaterialGuia;

@Repository
public interface MaterialGuiaRepository extends JpaRepository<MaterialGuia, Long> {
    List<MaterialGuia> findAllByOrderByNomeAsc();

    List<MaterialGuia> findByAtivoTrueOrderByNomeAsc();

    Optional<MaterialGuia> findByIdAndAtivoTrue(Long id);
}
