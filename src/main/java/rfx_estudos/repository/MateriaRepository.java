package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.Materia;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    List<Materia> findAllByOrderByNomeAsc();

    List<Materia> findByAtivoTrueOrderByNomeAsc();

    Optional<Materia> findByIdAndAtivoTrue(Long id);
}
