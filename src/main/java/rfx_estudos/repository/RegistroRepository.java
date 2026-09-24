package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
