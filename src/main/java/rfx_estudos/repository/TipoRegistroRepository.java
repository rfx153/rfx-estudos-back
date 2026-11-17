package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.TipoRegistro;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoRegistroRepository extends JpaRepository<TipoRegistro, Long> {
    List<TipoRegistro> findAllByOrderByNomeAsc();

    List<TipoRegistro> findByAtivoTrueOrderByNomeAsc();

    Optional<TipoRegistro> findByIdAndAtivoTrue(Long id);
}
