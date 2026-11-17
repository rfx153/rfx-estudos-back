package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.Assunto;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Long> {
    List<Assunto> findAllByOrderByNomeAsc();

    List<Assunto> findByAtivoTrueAndMateriaAtivoTrueOrderByNomeAsc();

    List<Assunto> findByMateriaIdAndAtivoTrueAndMateriaAtivoTrueOrderByNomeAsc(Long materiaId);

    Optional<Assunto> findByIdAndAtivoTrue(Long id);
}
