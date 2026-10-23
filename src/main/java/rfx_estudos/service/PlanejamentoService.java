package rfx_estudos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rfx_estudos.domain.Materia;
import rfx_estudos.domain.Planejamento;
import rfx_estudos.domain.PlanejamentoGrupoAssunto;
import rfx_estudos.domain.PlanejamentoMateria;
import rfx_estudos.dto.PlanejamentoGrupoAssuntoDTO;
import rfx_estudos.dto.PlanejamentoMateriaDTO;
import rfx_estudos.repository.MateriaRepository;
import rfx_estudos.repository.PlanejamentoGrupoAssuntoRepository;
import rfx_estudos.repository.PlanejamentoMateriaRepository;
import rfx_estudos.repository.PlanejamentoRepository;

@Service
public class PlanejamentoService {

    private final PlanejamentoRepository planejamentoRepository;
    private final PlanejamentoMateriaRepository planejamentoMateriaRepository;
    private final PlanejamentoGrupoAssuntoRepository planejamentoGrupoAssuntoRepository;
    private final MateriaRepository materiaRepository;

    public PlanejamentoService(
            PlanejamentoRepository planejamentoRepository,
            PlanejamentoMateriaRepository planejamentoMateriaRepository,
            PlanejamentoGrupoAssuntoRepository planejamentoGrupoAssuntoRepository,
            MateriaRepository materiaRepository) {
        this.planejamentoRepository = planejamentoRepository;
        this.planejamentoMateriaRepository = planejamentoMateriaRepository;
        this.planejamentoGrupoAssuntoRepository = planejamentoGrupoAssuntoRepository;
        this.materiaRepository = materiaRepository;
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

    @Transactional(readOnly = true)
    public Optional<List<PlanejamentoMateria>> listarMaterias(Long planejamentoId) {
        if (!planejamentoRepository.existsById(planejamentoId)) {
            return Optional.empty();
        }

        return Optional.of(
                planejamentoMateriaRepository.findByPlanejamentoIdOrderByOrdemAsc(planejamentoId));
    }

    @Transactional(readOnly = true)
    public Optional<PlanejamentoMateria> buscarMateria(Long planejamentoId, Long id) {
        return planejamentoMateriaRepository.findByIdAndPlanejamentoId(id, planejamentoId);
    }

    @Transactional
    public Optional<PlanejamentoMateria> adicionarMateria(
            Long planejamentoId,
            PlanejamentoMateriaDTO dados) {
        Optional<Planejamento> planejamento = planejamentoRepository.findById(planejamentoId);
        Optional<Materia> materia = buscarMateriaPorId(dados.getMateriaId());

        if (planejamento.isEmpty() || materia.isEmpty()) {
            return Optional.empty();
        }

        PlanejamentoMateria planejamentoMateria = new PlanejamentoMateria();
        planejamentoMateria.setPlanejamento(planejamento.get());
        planejamentoMateria.setMateria(materia.get());
        atualizarCampos(planejamentoMateria, dados);

        return Optional.of(planejamentoMateriaRepository.save(planejamentoMateria));
    }

    @Transactional
    public Optional<PlanejamentoMateria> atualizarMateria(
            Long planejamentoId,
            Long id,
            PlanejamentoMateriaDTO dados) {
        Optional<PlanejamentoMateria> planejamentoMateria =
                planejamentoMateriaRepository.findByIdAndPlanejamentoId(id, planejamentoId);
        Optional<Materia> materia = buscarMateriaPorId(dados.getMateriaId());

        if (planejamentoMateria.isEmpty() || materia.isEmpty()) {
            return Optional.empty();
        }

        PlanejamentoMateria existente = planejamentoMateria.get();
        existente.setMateria(materia.get());
        atualizarCampos(existente, dados);

        return Optional.of(planejamentoMateriaRepository.save(existente));
    }

    @Transactional
    public boolean excluirMateria(Long planejamentoId, Long id) {
        return planejamentoMateriaRepository.findByIdAndPlanejamentoId(id, planejamentoId)
                .map(planejamentoMateria -> {
                    planejamentoMateriaRepository.delete(planejamentoMateria);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Optional<List<PlanejamentoGrupoAssunto>> listarGrupos(
            Long planejamentoId,
            Long planejamentoMateriaId) {
        Optional<PlanejamentoMateria> planejamentoMateria = buscarPlanejamentoMateria(
                planejamentoId,
                planejamentoMateriaId);

        if (planejamentoMateria.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                planejamentoGrupoAssuntoRepository
                        .findByPlanejamentoMateriaIdOrderByOrdemAsc(planejamentoMateriaId));
    }

    @Transactional(readOnly = true)
    public Optional<PlanejamentoGrupoAssunto> buscarGrupo(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long id) {
        if (buscarPlanejamentoMateria(planejamentoId, planejamentoMateriaId).isEmpty()) {
            return Optional.empty();
        }

        return planejamentoGrupoAssuntoRepository.findByIdAndPlanejamentoMateriaId(
                id,
                planejamentoMateriaId);
    }

    @Transactional
    public Optional<PlanejamentoGrupoAssunto> adicionarGrupo(
            Long planejamentoId,
            Long planejamentoMateriaId,
            PlanejamentoGrupoAssuntoDTO dados) {
        Optional<PlanejamentoMateria> planejamentoMateria = buscarPlanejamentoMateria(
                planejamentoId,
                planejamentoMateriaId);

        if (planejamentoMateria.isEmpty()) {
            return Optional.empty();
        }

        PlanejamentoGrupoAssunto grupo = new PlanejamentoGrupoAssunto();
        grupo.setPlanejamentoMateria(planejamentoMateria.get());
        atualizarCampos(grupo, dados);

        return Optional.of(planejamentoGrupoAssuntoRepository.save(grupo));
    }

    @Transactional
    public Optional<PlanejamentoGrupoAssunto> atualizarGrupo(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long id,
            PlanejamentoGrupoAssuntoDTO dados) {
        if (buscarPlanejamentoMateria(planejamentoId, planejamentoMateriaId).isEmpty()) {
            return Optional.empty();
        }

        return planejamentoGrupoAssuntoRepository
                .findByIdAndPlanejamentoMateriaId(id, planejamentoMateriaId)
                .map(grupo -> {
                    atualizarCampos(grupo, dados);
                    return planejamentoGrupoAssuntoRepository.save(grupo);
                });
    }

    @Transactional
    public boolean excluirGrupo(Long planejamentoId, Long planejamentoMateriaId, Long id) {
        if (buscarPlanejamentoMateria(planejamentoId, planejamentoMateriaId).isEmpty()) {
            return false;
        }

        return planejamentoGrupoAssuntoRepository
                .findByIdAndPlanejamentoMateriaId(id, planejamentoMateriaId)
                .map(grupo -> {
                    planejamentoGrupoAssuntoRepository.delete(grupo);
                    return true;
                })
                .orElse(false);
    }

    private void atualizarCampos(
            PlanejamentoMateria planejamentoMateria,
            PlanejamentoMateriaDTO dados) {
        planejamentoMateria.setPeso(dados.getPeso() != null ? dados.getPeso() : (short) 3);
        planejamentoMateria.setOrdem(dados.getOrdem());
        planejamentoMateria.setDataFinalizarPrevista(dados.getDataFinalizarPrevista());
        planejamentoMateria.setDataFinalizado(dados.getDataFinalizado());
        planejamentoMateria.setObservacoes(dados.getObservacoes());
    }

    private void atualizarCampos(
            PlanejamentoGrupoAssunto grupo,
            PlanejamentoGrupoAssuntoDTO dados) {
        grupo.setTipo(dados.getTipo());
        grupo.setNome(dados.getNome());
        grupo.setPeso(dados.getPeso() != null ? dados.getPeso() : (short) 3);
        grupo.setOrdem(dados.getOrdem());
        grupo.setDataPrevista(dados.getDataPrevista());
        grupo.setDataFinalizada(dados.getDataFinalizada());
        grupo.setObservacoes(dados.getObservacoes());
    }

    private Optional<PlanejamentoMateria> buscarPlanejamentoMateria(
            Long planejamentoId,
            Long planejamentoMateriaId) {
        return planejamentoMateriaRepository.findByIdAndPlanejamentoId(
                planejamentoMateriaId,
                planejamentoId);
    }

    private Optional<Materia> buscarMateriaPorId(Long materiaId) {
        if (materiaId == null) {
            return Optional.empty();
        }

        return materiaRepository.findById(materiaId);
    }
}
