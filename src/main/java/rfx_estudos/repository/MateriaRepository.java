package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.Materia;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    List<Materia> findAllByOrderByNomeAsc();

    List<Materia> findByAtivoTrueOrderByNomeAsc();

    Optional<Materia> findByIdAndAtivoTrue(Long id);

    @Query("""
            select count(materia)
            from Materia materia
            where lower(trim(materia.nome)) = lower(trim(:nome))
              and (:idIgnorado is null or materia.id <> :idIgnorado)
            """)
    long contarPorNomeNormalizado(
            @Param("nome") String nome,
            @Param("idIgnorado") Long idIgnorado);
}
