package rfx_estudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rfx_estudos.domain.Assunto;

import java.util.List;

@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Long> {
    
    // Opcional: Criamos esse método para quando o front-end quiser puxar
    // apenas os assuntos de uma matéria específica (Ex: carregar só assuntos de Java)
    List<Assunto> findByMateriaId(Long materiaId);
}
