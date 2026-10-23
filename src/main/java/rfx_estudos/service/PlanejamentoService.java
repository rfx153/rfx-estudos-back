package rfx_estudos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rfx_estudos.domain.Materia;
import rfx_estudos.domain.Assunto;
import rfx_estudos.domain.Planejamento;
import rfx_estudos.domain.PlanejamentoGrupoAssunto;
import rfx_estudos.domain.PlanejamentoGrupoAssuntoItem;
import rfx_estudos.domain.PlanejamentoGrupoAssuntoTipo;
import rfx_estudos.domain.PlanejamentoItemEtapa;
import rfx_estudos.domain.PlanejamentoMateria;
import rfx_estudos.domain.TipoRegistro;
import rfx_estudos.dto.PlanejamentoGrupoAssuntoDTO;
import rfx_estudos.dto.PlanejamentoGrupoAssuntoItemDTO;
import rfx_estudos.dto.PlanejamentoItemEtapaDTO;
import rfx_estudos.dto.PlanejamentoMateriaDTO;
import rfx_estudos.repository.AssuntoRepository;
import rfx_estudos.repository.MateriaRepository;
import rfx_estudos.repository.PlanejamentoGrupoAssuntoItemRepository;
import rfx_estudos.repository.PlanejamentoGrupoAssuntoRepository;
import rfx_estudos.repository.PlanejamentoItemEtapaRepository;
import rfx_estudos.repository.PlanejamentoMateriaRepository;
import rfx_estudos.repository.PlanejamentoRepository;
import rfx_estudos.repository.TipoRegistroRepository;

@Service
public class PlanejamentoService {

    private final PlanejamentoRepository planejamentoRepository;
    private final PlanejamentoMateriaRepository planejamentoMateriaRepository;
    private final PlanejamentoGrupoAssuntoRepository planejamentoGrupoAssuntoRepository;
    private final PlanejamentoGrupoAssuntoItemRepository planejamentoGrupoAssuntoItemRepository;
    private final PlanejamentoItemEtapaRepository planejamentoItemEtapaRepository;
    private final MateriaRepository materiaRepository;
    private final AssuntoRepository assuntoRepository;
    private final TipoRegistroRepository tipoRegistroRepository;

    public PlanejamentoService(
            PlanejamentoRepository planejamentoRepository,
            PlanejamentoMateriaRepository planejamentoMateriaRepository,
            PlanejamentoGrupoAssuntoRepository planejamentoGrupoAssuntoRepository,
            PlanejamentoGrupoAssuntoItemRepository planejamentoGrupoAssuntoItemRepository,
            PlanejamentoItemEtapaRepository planejamentoItemEtapaRepository,
            MateriaRepository materiaRepository,
            AssuntoRepository assuntoRepository,
            TipoRegistroRepository tipoRegistroRepository) {
        this.planejamentoRepository = planejamentoRepository;
        this.planejamentoMateriaRepository = planejamentoMateriaRepository;
        this.planejamentoGrupoAssuntoRepository = planejamentoGrupoAssuntoRepository;
        this.planejamentoGrupoAssuntoItemRepository = planejamentoGrupoAssuntoItemRepository;
        this.planejamentoItemEtapaRepository = planejamentoItemEtapaRepository;
        this.materiaRepository = materiaRepository;
        this.assuntoRepository = assuntoRepository;
        this.tipoRegistroRepository = tipoRegistroRepository;
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
                    validarAlteracaoDoTipoDoGrupo(grupo, dados);
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

    @Transactional(readOnly = true)
    public Optional<List<PlanejamentoGrupoAssuntoItem>> listarItens(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId) {
        if (buscarPlanejamentoGrupoAssunto(
                planejamentoId,
                planejamentoMateriaId,
                grupoId).isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                planejamentoGrupoAssuntoItemRepository
                        .findByPlanejamentoGrupoAssuntoIdOrderByOrdemAsc(grupoId));
    }

    @Transactional(readOnly = true)
    public Optional<PlanejamentoGrupoAssuntoItem> buscarItem(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId,
            Long id) {
        if (buscarPlanejamentoGrupoAssunto(
                planejamentoId,
                planejamentoMateriaId,
                grupoId).isEmpty()) {
            return Optional.empty();
        }

        return planejamentoGrupoAssuntoItemRepository
                .findByIdAndPlanejamentoGrupoAssuntoId(id, grupoId);
    }

    @Transactional
    public Optional<PlanejamentoGrupoAssuntoItem> adicionarItem(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId,
            PlanejamentoGrupoAssuntoItemDTO dados) {
        Optional<PlanejamentoGrupoAssunto> grupo = buscarPlanejamentoGrupoAssunto(
                planejamentoId,
                planejamentoMateriaId,
                grupoId);
        Optional<Assunto> assunto = buscarAssuntoPorId(dados.getAssuntoId());

        if (grupo.isEmpty()
                || assunto.isEmpty()
                || !assuntoPertenceAoGrupo(assunto.get(), grupo.get())) {
            return Optional.empty();
        }

        validarInclusaoDoItem(grupo.get(), assunto.get());

        PlanejamentoGrupoAssuntoItem item = new PlanejamentoGrupoAssuntoItem();
        item.setPlanejamentoGrupoAssunto(grupo.get());
        item.setAssunto(assunto.get());
        atualizarCampos(item, dados);

        return Optional.of(planejamentoGrupoAssuntoItemRepository.save(item));
    }

    @Transactional
    public Optional<PlanejamentoGrupoAssuntoItem> atualizarItem(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId,
            Long id,
            PlanejamentoGrupoAssuntoItemDTO dados) {
        Optional<PlanejamentoGrupoAssunto> grupo = buscarPlanejamentoGrupoAssunto(
                planejamentoId,
                planejamentoMateriaId,
                grupoId);
        Optional<PlanejamentoGrupoAssuntoItem> item =
                planejamentoGrupoAssuntoItemRepository
                        .findByIdAndPlanejamentoGrupoAssuntoId(id, grupoId);
        Optional<Assunto> assunto = buscarAssuntoPorId(dados.getAssuntoId());

        if (grupo.isEmpty()
                || item.isEmpty()
                || assunto.isEmpty()
                || !assuntoPertenceAoGrupo(assunto.get(), grupo.get())) {
            return Optional.empty();
        }

        validarAtualizacaoDoItem(grupo.get(), assunto.get(), id);

        PlanejamentoGrupoAssuntoItem existente = item.get();
        existente.setAssunto(assunto.get());
        atualizarCampos(existente, dados);

        return Optional.of(planejamentoGrupoAssuntoItemRepository.save(existente));
    }

    @Transactional
    public boolean excluirItem(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId,
            Long id) {
        if (buscarPlanejamentoGrupoAssunto(
                planejamentoId,
                planejamentoMateriaId,
                grupoId).isEmpty()) {
            return false;
        }

        return planejamentoGrupoAssuntoItemRepository
                .findByIdAndPlanejamentoGrupoAssuntoId(id, grupoId)
                .map(item -> {
                    planejamentoGrupoAssuntoItemRepository.delete(item);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Optional<List<PlanejamentoItemEtapa>> listarEtapas(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId,
            Long itemId) {
        if (buscarPlanejamentoGrupoAssuntoItem(
                planejamentoId,
                planejamentoMateriaId,
                grupoId,
                itemId).isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                planejamentoItemEtapaRepository
                        .findByPlanejamentoGrupoAssuntoItemIdOrderByOrdemAsc(itemId));
    }

    @Transactional(readOnly = true)
    public Optional<PlanejamentoItemEtapa> buscarEtapa(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId,
            Long itemId,
            Long id) {
        if (buscarPlanejamentoGrupoAssuntoItem(
                planejamentoId,
                planejamentoMateriaId,
                grupoId,
                itemId).isEmpty()) {
            return Optional.empty();
        }

        return planejamentoItemEtapaRepository
                .findByIdAndPlanejamentoGrupoAssuntoItemId(id, itemId);
    }

    @Transactional
    public Optional<PlanejamentoItemEtapa> adicionarEtapa(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId,
            Long itemId,
            PlanejamentoItemEtapaDTO dados) {
        Optional<PlanejamentoGrupoAssuntoItem> item = buscarPlanejamentoGrupoAssuntoItem(
                planejamentoId,
                planejamentoMateriaId,
                grupoId,
                itemId);
        Optional<TipoRegistro> tipoRegistro = buscarTipoRegistroPorId(
                dados.getTipoRegistroId());

        if (item.isEmpty() || tipoRegistro.isEmpty()) {
            return Optional.empty();
        }

        PlanejamentoItemEtapa etapa = new PlanejamentoItemEtapa();
        etapa.setPlanejamentoGrupoAssuntoItem(item.get());
        etapa.setTipoRegistro(tipoRegistro.get());
        atualizarCampos(etapa, dados);

        return Optional.of(planejamentoItemEtapaRepository.save(etapa));
    }

    @Transactional
    public Optional<PlanejamentoItemEtapa> atualizarEtapa(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId,
            Long itemId,
            Long id,
            PlanejamentoItemEtapaDTO dados) {
        if (buscarPlanejamentoGrupoAssuntoItem(
                planejamentoId,
                planejamentoMateriaId,
                grupoId,
                itemId).isEmpty()) {
            return Optional.empty();
        }

        Optional<PlanejamentoItemEtapa> etapa = planejamentoItemEtapaRepository
                .findByIdAndPlanejamentoGrupoAssuntoItemId(id, itemId);
        Optional<TipoRegistro> tipoRegistro = buscarTipoRegistroPorId(
                dados.getTipoRegistroId());

        if (etapa.isEmpty() || tipoRegistro.isEmpty()) {
            return Optional.empty();
        }

        PlanejamentoItemEtapa existente = etapa.get();
        existente.setTipoRegistro(tipoRegistro.get());
        atualizarCampos(existente, dados);

        return Optional.of(planejamentoItemEtapaRepository.save(existente));
    }

    @Transactional
    public boolean excluirEtapa(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId,
            Long itemId,
            Long id) {
        if (buscarPlanejamentoGrupoAssuntoItem(
                planejamentoId,
                planejamentoMateriaId,
                grupoId,
                itemId).isEmpty()) {
            return false;
        }

        return planejamentoItemEtapaRepository
                .findByIdAndPlanejamentoGrupoAssuntoItemId(id, itemId)
                .map(etapa -> {
                    planejamentoItemEtapaRepository.delete(etapa);
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

    private void atualizarCampos(
            PlanejamentoGrupoAssuntoItem item,
            PlanejamentoGrupoAssuntoItemDTO dados) {
        item.setPeso(dados.getPeso() != null ? dados.getPeso() : (short) 3);
        item.setOrdem(dados.getOrdem());
        item.setDataPrevista(dados.getDataPrevista());
        item.setObservacoes(dados.getObservacoes());
    }

    private void atualizarCampos(
            PlanejamentoItemEtapa etapa,
            PlanejamentoItemEtapaDTO dados) {
        etapa.setConcluida(Boolean.TRUE.equals(dados.getConcluida()));
        etapa.setConcluidaManualmente(Boolean.TRUE.equals(dados.getConcluidaManualmente()));
        etapa.setOrdem(dados.getOrdem());
        etapa.setDataPrevista(dados.getDataPrevista());
        etapa.setDataConcluida(dados.getDataConcluida());
        etapa.setObservacoes(dados.getObservacoes());
    }

    private Optional<PlanejamentoMateria> buscarPlanejamentoMateria(
            Long planejamentoId,
            Long planejamentoMateriaId) {
        return planejamentoMateriaRepository.findByIdAndPlanejamentoId(
                planejamentoMateriaId,
                planejamentoId);
    }

    private Optional<PlanejamentoGrupoAssunto> buscarPlanejamentoGrupoAssunto(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId) {
        if (buscarPlanejamentoMateria(planejamentoId, planejamentoMateriaId).isEmpty()) {
            return Optional.empty();
        }

        return planejamentoGrupoAssuntoRepository.findByIdAndPlanejamentoMateriaId(
                grupoId,
                planejamentoMateriaId);
    }

    private Optional<PlanejamentoGrupoAssuntoItem> buscarPlanejamentoGrupoAssuntoItem(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId,
            Long itemId) {
        if (buscarPlanejamentoGrupoAssunto(
                planejamentoId,
                planejamentoMateriaId,
                grupoId).isEmpty()) {
            return Optional.empty();
        }

        return planejamentoGrupoAssuntoItemRepository
                .findByIdAndPlanejamentoGrupoAssuntoId(itemId, grupoId);
    }

    private Optional<Materia> buscarMateriaPorId(Long materiaId) {
        if (materiaId == null) {
            return Optional.empty();
        }

        return materiaRepository.findById(materiaId);
    }

    private Optional<Assunto> buscarAssuntoPorId(Long assuntoId) {
        if (assuntoId == null) {
            return Optional.empty();
        }

        return assuntoRepository.findById(assuntoId);
    }

    private Optional<TipoRegistro> buscarTipoRegistroPorId(Long tipoRegistroId) {
        if (tipoRegistroId == null) {
            return Optional.empty();
        }

        return tipoRegistroRepository.findById(tipoRegistroId);
    }

    private boolean assuntoPertenceAoGrupo(
            Assunto assunto,
            PlanejamentoGrupoAssunto grupo) {
        return assunto.getMateria().getId().equals(
                grupo.getPlanejamentoMateria().getMateria().getId());
    }

    private void validarInclusaoDoItem(
            PlanejamentoGrupoAssunto grupo,
            Assunto assunto) {
        if (grupo.getTipo() == PlanejamentoGrupoAssuntoTipo.ASSUNTO
                && planejamentoGrupoAssuntoItemRepository
                        .existsByPlanejamentoGrupoAssuntoId(grupo.getId())) {
            throw new PlanejamentoConflitoException(
                    "Um grupo do tipo ASSUNTO pode possuir somente um item");
        }

        if (planejamentoGrupoAssuntoItemRepository
                .existsByPlanejamentoGrupoAssuntoIdAndAssuntoId(
                        grupo.getId(),
                        assunto.getId())) {
            throw new PlanejamentoConflitoException(
                    "O assunto já está cadastrado neste grupo");
        }
    }

    private void validarAtualizacaoDoItem(
            PlanejamentoGrupoAssunto grupo,
            Assunto assunto,
            Long itemId) {
        if (grupo.getTipo() == PlanejamentoGrupoAssuntoTipo.ASSUNTO
                && planejamentoGrupoAssuntoItemRepository
                        .existsByPlanejamentoGrupoAssuntoIdAndIdNot(
                                grupo.getId(),
                                itemId)) {
            throw new PlanejamentoConflitoException(
                    "Um grupo do tipo ASSUNTO pode possuir somente um item");
        }

        if (planejamentoGrupoAssuntoItemRepository
                .existsByPlanejamentoGrupoAssuntoIdAndAssuntoIdAndIdNot(
                        grupo.getId(),
                        assunto.getId(),
                        itemId)) {
            throw new PlanejamentoConflitoException(
                    "O assunto já está cadastrado neste grupo");
        }
    }

    private void validarAlteracaoDoTipoDoGrupo(
            PlanejamentoGrupoAssunto grupo,
            PlanejamentoGrupoAssuntoDTO dados) {
        if (dados.getTipo() != PlanejamentoGrupoAssuntoTipo.ASSUNTO) {
            return;
        }

        long quantidadeDeItens = planejamentoGrupoAssuntoItemRepository
                .countByPlanejamentoGrupoAssuntoId(grupo.getId());

        if (quantidadeDeItens > 1) {
            throw new PlanejamentoConflitoException(
                    "Um grupo do tipo ASSUNTO pode possuir somente um item");
        }
    }
}
