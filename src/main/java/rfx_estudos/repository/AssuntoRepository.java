package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
            select count(assunto)
            from Assunto assunto
            where assunto.materia.id = :materiaId
              and lower(trim(assunto.nome)) = lower(trim(:nome))
              and (:idIgnorado is null or assunto.id <> :idIgnorado)
            """)
    long contarPorMateriaENomeNormalizado(
            @Param("materiaId") Long materiaId,
            @Param("nome") String nome,
            @Param("idIgnorado") Long idIgnorado);
}
