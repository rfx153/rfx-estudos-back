package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rfx_estudos.domain.CicloMateria;

public interface CicloMateriaRepository extends JpaRepository<CicloMateria, Long> {

    List<CicloMateria> findByCicloIdOrderByOrdemAsc(Long cicloId);

    Optional<CicloMateria> findByIdAndCicloId(Long id, Long cicloId);

    boolean existsByCicloIdAndMateriaId(Long cicloId, Long materiaId);

    @Modifying
    @Query(value = "UPDATE ciclo_materias SET ordem = -ordem WHERE ciclo_fk = :cicloId", nativeQuery = true)
    void tornarOrdensTemporarias(@Param("cicloId") Long cicloId);

    @Modifying
    @Query(value = "UPDATE ciclo_materias SET ordem = :ordem WHERE id = :id AND ciclo_fk = :cicloId", nativeQuery = true)
    int atualizarOrdem(
            @Param("cicloId") Long cicloId,
            @Param("id") Long id,
            @Param("ordem") Integer ordem);
}
