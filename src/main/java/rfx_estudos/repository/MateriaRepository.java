package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.Materia;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    // O Spring Data JPA vai gerar automaticamente todos os métodos de CRUD (findAll, save, etc.)
}
