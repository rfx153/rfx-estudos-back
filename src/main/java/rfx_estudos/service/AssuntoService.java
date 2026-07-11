package rfx_estudos.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rfx_estudos.domain.Assunto;
import rfx_estudos.domain.Materia;
import rfx_estudos.dto.AssuntoNovoDTO;
import rfx_estudos.repository.AssuntoRepository;
import rfx_estudos.repository.MateriaRepository;

@Service
public class AssuntoService {

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    /**
     * Regra de negócio para salvar um assunto atrelando-o a uma matéria existente.
     */
    @Transactional // Garante a segurança da transação no banco Neon
    public Assunto salvarComMateria(AssuntoNovoDTO dto) {
        // 1. Valida e busca a matéria
        Materia materia = materiaRepository.findById(dto.getMateriaId())
                .orElseThrow(() -> new RuntimeException("Matéria não encontrada com o ID: " + dto.getMateriaId()));

        // 2. Monta a entidade
        Assunto novoAssunto = new Assunto();
        novoAssunto.setNome(dto.getNome());
        novoAssunto.setMateria(materia); // Seta a FK (materia_fk)

        // 3. Salva no banco via Hibernate
        return assuntoRepository.save(novoAssunto);
    }
}