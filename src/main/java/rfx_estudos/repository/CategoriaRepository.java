package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
