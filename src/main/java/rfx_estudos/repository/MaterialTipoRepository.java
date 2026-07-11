package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.MaterialTipo;

@Repository
public interface MaterialTipoRepository extends JpaRepository<MaterialTipo, Long> {
}