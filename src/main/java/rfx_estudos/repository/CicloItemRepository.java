package rfx_estudos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rfx_estudos.domain.CicloItem;

public interface CicloItemRepository extends JpaRepository<CicloItem, Long> {

    @EntityGraph(attributePaths = {"materia", "planejamentoGrupoMateria"})
    List<CicloItem> findByCicloIdOrderByOrdemAsc(Long cicloId);

    Optional<CicloItem> findByIdAndCicloId(Long id, Long cicloId);

    boolean existsByCicloIdAndMateriaId(Long cicloId, Long materiaId);

    boolean existsByCicloIdAndPlanejamentoGrupoMateriaId(Long cicloId, Long grupoId);

    @Modifying
    @Query(value = "UPDATE ciclo_itens SET ordem = -ordem WHERE ciclo_fk = :cicloId", nativeQuery = true)
    void tornarOrdensTemporarias(@Param("cicloId") Long cicloId);

    @Modifying
    @Query(value = "UPDATE ciclo_itens SET ordem = :ordem WHERE id = :id AND ciclo_fk = :cicloId", nativeQuery = true)
    int atualizarOrdem(
            @Param("cicloId") Long cicloId,
            @Param("id") Long id,
            @Param("ordem") Integer ordem);
}
