package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.Registro;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {
    
    // Busca todo o seu histórico ordenado pela data mais recente
    List<Registro> findAllByOrderByDataEstudoDesc();

    List<Registro> findByDataEstudoOrderByDataEstudoDesc(LocalDate dataEstudo);

    List<Registro> findByOrderByDataEstudoDesc(Pageable pageable);

    List<Registro> findByMateriaIdOrderByDataEstudoDesc(Long materiaId, Pageable pageable);

    @EntityGraph(attributePaths = {
            "materia",
            "assunto",
            "materialTipo",
            "planejamento",
            "tipoRegistro",
            "revisaoAssunto"
    })
    @Query("select r from Registro r")
    Page<Registro> buscarHistorico(Pageable pageable);

    @EntityGraph(attributePaths = {
            "materia",
            "assunto",
            "materialTipo",
            "planejamento",
            "tipoRegistro",
            "revisaoAssunto"
    })
    @Query("select r from Registro r where r.materia.id = :materiaId")
    Page<Registro> buscarHistoricoPorMateria(@Param("materiaId") Long materiaId, Pageable pageable);
}
