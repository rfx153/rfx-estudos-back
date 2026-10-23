package rfx_estudos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rfx_estudos.domain.Planejamento;
import rfx_estudos.repository.PlanejamentoRepository;

@Service
public class PlanejamentoService {

    private final PlanejamentoRepository planejamentoRepository;

    public PlanejamentoService(PlanejamentoRepository planejamentoRepository) {
        this.planejamentoRepository = planejamentoRepository;
    }

    @Transactional(readOnly = true)
    public List<Planejamento> listarTodos() {
        return planejamentoRepository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Planejamento> buscarPorId(Long id) {
        return planejamentoRepository.findById(id);
    }

    @Transactional
    public Planejamento criar(Planejamento planejamento) {
        planejamento.setId(null);
        return planejamentoRepository.save(planejamento);
    }

    @Transactional
    public Optional<Planejamento> atualizar(Long id, Planejamento dados) {
        return planejamentoRepository.findById(id)
                .map(planejamento -> {
                    planejamento.setNome(dados.getNome());
                    planejamento.setDescricao(dados.getDescricao());
                    return planejamentoRepository.save(planejamento);
                });
    }

    @Transactional
    public boolean excluir(Long id) {
        return planejamentoRepository.findById(id)
                .map(planejamento -> {
                    planejamentoRepository.delete(planejamento);
                    return true;
                })
                .orElse(false);
    }
}
