package rfx_estudos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rfx_estudos.domain.MaterialGuia;

@Repository
public interface MaterialGuiaRepository extends JpaRepository<MaterialGuia, Long> {
    List<MaterialGuia> findAllByOrderByNomeAsc();
}
