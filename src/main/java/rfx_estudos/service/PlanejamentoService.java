package rfx_estudos.service;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rfx_estudos.domain.Materia;
import rfx_estudos.domain.Assunto;
import rfx_estudos.domain.Planejamento;
import rfx_estudos.domain.PlanejamentoGrupoMateria;
import rfx_estudos.domain.PlanejamentoGrupoAssunto;
import rfx_estudos.domain.PlanejamentoGrupoAssuntoItem;
import rfx_estudos.domain.PlanejamentoGrupoAssuntoTipo;
import rfx_estudos.domain.PlanejamentoItemEtapa;
import rfx_estudos.domain.PlanejamentoMateria;
import rfx_estudos.domain.TipoRegistro;
import rfx_estudos.dto.PlanejamentoGrupoAssuntoDTO;
import rfx_estudos.dto.PlanejamentoEtapaPorAssuntoDTO;
import rfx_estudos.dto.PlanejamentoDestinoRegistroDTO;
import rfx_estudos.dto.PlanejamentoAproveitamentoDTO;
import rfx_estudos.dto.PlanejamentoEstruturaDTO;
import rfx_estudos.dto.PlanejamentoEstruturaLinhaDTO;
import rfx_estudos.dto.PlanejamentoGrupoMateriaDTO;
import rfx_estudos.dto.PlanejamentoGrupoAssuntoItemDTO;
import rfx_estudos.dto.PlanejamentoItemEtapaDTO;
import rfx_estudos.dto.PlanejamentoMateriaDTO;
import rfx_estudos.dto.PlanejamentoReordenacaoDTO;
import rfx_estudos.repository.AssuntoRepository;
import rfx_estudos.repository.MateriaRepository;
import rfx_estudos.repository.PlanejamentoGrupoAssuntoItemRepository;
import rfx_estudos.repository.PlanejamentoGrupoAssuntoRepository;
import rfx_estudos.repository.PlanejamentoGrupoMateriaRepository;
import rfx_estudos.repository.PlanejamentoItemEtapaRepository;
import rfx_estudos.repository.PlanejamentoMateriaRepository;
import rfx_estudos.repository.PlanejamentoRepository;
import rfx_estudos.repository.RegistroRepository;
import rfx_estudos.repository.TipoRegistroRepository;

@Service
public class PlanejamentoService {

    private final PlanejamentoRepository planejamentoRepository;
    private final PlanejamentoMateriaRepository planejamentoMateriaRepository;
    private final PlanejamentoGrupoAssuntoRepository planejamentoGrupoAssuntoRepository;
    private final PlanejamentoGrupoMateriaRepository planejamentoGrupoMateriaRepository;
    private final PlanejamentoGrupoAssuntoItemRepository planejamentoGrupoAssuntoItemRepository;
    private final PlanejamentoItemEtapaRepository planejamentoItemEtapaRepository;
    private final MateriaRepository materiaRepository;
    private final AssuntoRepository assuntoRepository;
    private final TipoRegistroRepository tipoRegistroRepository;
    private final RegistroRepository registroRepository;

    public PlanejamentoService(
            PlanejamentoRepository planejamentoRepository,
            PlanejamentoMateriaRepository planejamentoMateriaRepository,
            PlanejamentoGrupoAssuntoRepository planejamentoGrupoAssuntoRepository,
            PlanejamentoGrupoMateriaRepository planejamentoGrupoMateriaRepository,
            PlanejamentoGrupoAssuntoItemRepository planejamentoGrupoAssuntoItemRepository,
            PlanejamentoItemEtapaRepository planejamentoItemEtapaRepository,
            MateriaRepository materiaRepository,
            AssuntoRepository assuntoRepository,
            TipoRegistroRepository tipoRegistroRepository,
            RegistroRepository registroRepository) {
        this.planejamentoRepository = planejamentoRepository;
        this.planejamentoMateriaRepository = planejamentoMateriaRepository;
        this.planejamentoGrupoAssuntoRepository = planejamentoGrupoAssuntoRepository;
        this.planejamentoGrupoMateriaRepository = planejamentoGrupoMateriaRepository;
        this.planejamentoGrupoAssuntoItemRepository = planejamentoGrupoAssuntoItemRepository;
        this.planejamentoItemEtapaRepository = planejamentoItemEtapaRepository;
        this.materiaRepository = materiaRepository;
        this.assuntoRepository = assuntoRepository;
        this.tipoRegistroRepository = tipoRegistroRepository;
        this.registroRepository = registroRepository;
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
                    planejamento.setDataFimPrevista(dados.getDataFimPrevista());
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
    public Optional<List<PlanejamentoGrupoMateria>> listarGruposMaterias(Long planejamentoId) {
        if (!planejamentoRepository.existsById(planejamentoId)) {
            return Optional.empty();
        }

        return Optional.of(
                planejamentoGrupoMateriaRepository.findByPlanejamentoIdOrderByOrdemAsc(planejamentoId));
    }

    @Transactional(readOnly = true)
    public Optional<PlanejamentoGrupoMateria> buscarGrupoMateria(Long planejamentoId, Long id) {
        return planejamentoGrupoMateriaRepository.findByIdAndPlanejamentoId(id, planejamentoId);
    }

    @Transactional
    public Optional<PlanejamentoGrupoMateria> adicionarGrupoMateria(
            Long planejamentoId,
            PlanejamentoGrupoMateriaDTO dados) {
        return planejamentoRepository.findById(planejamentoId)
                .map(planejamento -> {
                    PlanejamentoGrupoMateria grupo = new PlanejamentoGrupoMateria();
                    grupo.setPlanejamento(planejamento);
                    atualizarCampos(grupo, dados);
                    return planejamentoGrupoMateriaRepository.save(grupo);
                });
    }

    @Transactional
    public Optional<PlanejamentoGrupoMateria> atualizarGrupoMateria(
            Long planejamentoId,
            Long id,
            PlanejamentoGrupoMateriaDTO dados) {
        return planejamentoGrupoMateriaRepository.findByIdAndPlanejamentoId(id, planejamentoId)
                .map(grupo -> {
                    atualizarCampos(grupo, dados);
                    return planejamentoGrupoMateriaRepository.save(grupo);
                });
    }

    @Transactional
    public boolean reordenarGruposMaterias(
            Long planejamentoId,
            PlanejamentoReordenacaoDTO dados) {
        if (!planejamentoRepository.existsById(planejamentoId)) {
            return false;
        }

        List<PlanejamentoGrupoMateria> grupos = planejamentoGrupoMateriaRepository
                .findByPlanejamentoIdOrderByOrdemAsc(planejamentoId);
        Map<Long, PlanejamentoGrupoMateria> gruposPorId = new LinkedHashMap<>();
        grupos.forEach(grupo -> gruposPorId.put(grupo.getId(), grupo));

        if (!reordenacaoValida(dados, gruposPorId.keySet())) {
            return false;
        }

        dados.getItens().forEach(item ->
                gruposPorId.get(item.getId()).setOrdem(item.getOrdem()));
        planejamentoGrupoMateriaRepository.saveAll(gruposPorId.values());
        return true;
    }

    @Transactional
    public boolean excluirGrupoMateria(Long planejamentoId, Long id) {
        return planejamentoGrupoMateriaRepository.findByIdAndPlanejamentoId(id, planejamentoId)
                .map(grupo -> {
                    if (planejamentoMateriaRepository.existsByPlanejamentoGrupoMateriaId(grupo.getId())) {
                        throw new PlanejamentoConflitoException(
                                "Não é possível remover um bloco que ainda possui matérias");
                    }
                    planejamentoGrupoMateriaRepository.delete(grupo);
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
    public Optional<PlanejamentoEstruturaDTO> buscarEstrutura(Long planejamentoId) {
        if (!planejamentoRepository.existsById(planejamentoId)) {
            return Optional.empty();
        }

        List<PlanejamentoEstruturaLinhaDTO> linhas = planejamentoGrupoAssuntoRepository
                .buscarEstruturaPorPlanejamentoId(planejamentoId);
        Set<Long> etapasIniciadas = new HashSet<>(
                registroRepository.buscarIdsEtapasIniciadas(planejamentoId));

        Map<Long, PlanejamentoEstruturaDTO.Grupo> grupos = new LinkedHashMap<>();
        Map<Long, PlanejamentoEstruturaDTO.Item> itens = new LinkedHashMap<>();
        Map<Long, PlanejamentoEstruturaDTO.Etapa> etapas = new LinkedHashMap<>();

        for (PlanejamentoEstruturaLinhaDTO linha : linhas) {
            grupos.putIfAbsent(linha.getGrupoId(), new PlanejamentoEstruturaDTO.Grupo(
                    linha.getGrupoId(),
                    linha.getPlanejamentoMateriaId(),
                    PlanejamentoGrupoAssuntoTipo.valueOf(linha.getGrupoTipo()),
                    linha.getGrupoNome(),
                    linha.getGrupoPeso(),
                    linha.getGrupoOrdem()));

            if (linha.getItemId() != null) {
                itens.putIfAbsent(linha.getItemId(), new PlanejamentoEstruturaDTO.Item(
                        linha.getItemId(),
                        linha.getGrupoId(),
                        linha.getAssuntoId(),
                        linha.getAssuntoNome(),
                        linha.getItemPeso(),
                        linha.getItemDificuldade(),
                        linha.getItemOrdem()));
            }

            if (linha.getEtapaId() != null) {
                etapas.putIfAbsent(linha.getEtapaId(), new PlanejamentoEstruturaDTO.Etapa(
                        linha.getEtapaId(),
                        linha.getItemId(),
                        linha.getTipoRegistroId(),
                        linha.getTipoRegistroNome(),
                        linha.getEtapaOrdem(),
                        etapasIniciadas.contains(linha.getEtapaId()),
                        linha.getEtapaConcluida(),
                        linha.getEtapaDataConcluida()));
            }
        }

        return Optional.of(new PlanejamentoEstruturaDTO(
                List.copyOf(grupos.values()),
                List.copyOf(itens.values()),
                List.copyOf(etapas.values())));
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
    public boolean reordenarMaterias(
            Long planejamentoId,
            PlanejamentoReordenacaoDTO dados) {
        List<PlanejamentoMateria> materias =
                planejamentoMateriaRepository.findByPlanejamentoIdOrderByOrdemAsc(planejamentoId);
        Map<Long, PlanejamentoMateria> materiasPorId = new LinkedHashMap<>();
        materias.forEach(materia -> materiasPorId.put(materia.getId(), materia));

        if (!reordenacaoValida(dados, materiasPorId.keySet())) {
            return false;
        }

        dados.getItens().forEach(item ->
                materiasPorId.get(item.getId()).setOrdem(item.getOrdem()));
        planejamentoMateriaRepository.saveAll(materiasPorId.values());
        return true;
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
    public boolean reordenarGrupos(
            Long planejamentoId,
            Long planejamentoMateriaId,
            PlanejamentoReordenacaoDTO dados) {
        if (buscarPlanejamentoMateria(planejamentoId, planejamentoMateriaId).isEmpty()) {
            return false;
        }

        List<PlanejamentoGrupoAssunto> grupos = planejamentoGrupoAssuntoRepository
                .findByPlanejamentoMateriaIdOrderByOrdemAsc(planejamentoMateriaId);
        Map<Long, PlanejamentoGrupoAssunto> gruposPorId = new LinkedHashMap<>();
        grupos.forEach(grupo -> gruposPorId.put(grupo.getId(), grupo));

        if (!reordenacaoValida(dados, gruposPorId.keySet())) {
            return false;
        }

        dados.getItens().forEach(item ->
                gruposPorId.get(item.getId()).setOrdem(item.getOrdem()));
        planejamentoGrupoAssuntoRepository.saveAll(gruposPorId.values());
        return true;
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
    public boolean reordenarItens(
            Long planejamentoId,
            Long planejamentoMateriaId,
            Long grupoId,
            PlanejamentoReordenacaoDTO dados) {
        if (buscarPlanejamentoGrupoAssunto(
                planejamentoId,
                planejamentoMateriaId,
                grupoId).isEmpty()) {
            return false;
        }

        List<PlanejamentoGrupoAssuntoItem> itens = planejamentoGrupoAssuntoItemRepository
                .findByPlanejamentoGrupoAssuntoIdOrderByOrdemAsc(grupoId);
        Map<Long, PlanejamentoGrupoAssuntoItem> itensPorId = new LinkedHashMap<>();
        itens.forEach(item -> itensPorId.put(item.getId(), item));

        if (!reordenacaoValida(dados, itensPorId.keySet())) {
            return false;
        }

        dados.getItens().forEach(item ->
                itensPorId.get(item.getId()).setOrdem(item.getOrdem()));
        planejamentoGrupoAssuntoItemRepository.saveAll(itensPorId.values());
        return true;
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

        List<PlanejamentoItemEtapa> etapas = planejamentoItemEtapaRepository
                .findByPlanejamentoGrupoAssuntoItemIdOrderByOrdemAsc(itemId);
        etapas.forEach(etapa -> etapa.setIniciada(
                registroRepository.existsByPlanejamentoEtapaId(etapa.getId())));
        return Optional.of(etapas);
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

    @Transactional(readOnly = true)
    public Optional<PlanejamentoItemEtapa> buscarEtapaPorAssunto(
            Long planejamentoId,
            Long assuntoId,
            Long tipoRegistroId) {
        List<PlanejamentoItemEtapa> etapas = planejamentoItemEtapaRepository
                .findByPlanejamentoAssuntoETipoRegistro(planejamentoId, assuntoId, tipoRegistroId);
        if (etapas.size() > 1) {
            throw new PlanejamentoConflitoException("Mais de uma etapa corresponde ao registro");
        }
        return etapas.stream().findFirst();
    }

    @Transactional(readOnly = true)
    public Optional<List<PlanejamentoAproveitamentoDTO>> listarAproveitamentos(Long planejamentoId) {
        if (!planejamentoRepository.existsById(planejamentoId)) {
            return Optional.empty();
        }

        return Optional.of(registroRepository.somarQuestoesPorPlanejamento(planejamentoId));
    }

    @Transactional(readOnly = true)
    public Optional<List<PlanejamentoDestinoRegistroDTO>> listarDestinosRegistro(
            Long planejamentoId,
            Long assuntoId,
            Long tipoRegistroId) {
        Optional<Planejamento> planejamento = planejamentoRepository.findById(planejamentoId);
        Optional<Assunto> assunto = buscarAssuntoPorId(assuntoId);
        if (planejamento.isEmpty() || assunto.isEmpty() || tipoRegistroId == null) {
            return Optional.empty();
        }

        List<PlanejamentoMateria> materias = planejamentoMateriaRepository
                .findByPlanejamentoIdAndMateriaIdOrderByOrdemAsc(
                        planejamentoId,
                        assunto.get().getMateria().getId());

        return Optional.of(materias.stream().map(planejamentoMateria -> {
            Optional<PlanejamentoGrupoAssuntoItem> item = planejamentoGrupoAssuntoItemRepository
                    .findByPlanejamentoMateriaIdAndAssuntoId(
                            planejamentoMateria.getId(),
                            assuntoId)
                    .stream()
                    .findFirst();
            Optional<PlanejamentoItemEtapa> etapa = item.stream()
                    .flatMap(itemEncontrado -> planejamentoItemEtapaRepository
                            .findByPlanejamentoGrupoAssuntoItemIdOrderByOrdemAsc(
                                    itemEncontrado.getId())
                            .stream())
                    .filter(etapaEncontrada -> etapaEncontrada.getTipoRegistro().getId()
                            .equals(tipoRegistroId))
                    .findFirst();
            String nomeGrupo = item
                    .map(PlanejamentoGrupoAssuntoItem::getPlanejamentoGrupoAssunto)
                    .map(grupo -> grupo.getNome() != null
                            ? grupo.getNome()
                            : assunto.get().getNome())
                    .orElse(null);

            return new PlanejamentoDestinoRegistroDTO(
                    planejamentoMateria.getId(),
                    etapa.map(PlanejamentoItemEtapa::getId).orElse(null),
                    planejamento.get().getNome(),
                    planejamentoMateria.getPlanejamentoGrupoMateria() != null
                            ? planejamentoMateria.getPlanejamentoGrupoMateria().getNome()
                            : null,
                    planejamentoMateria.getMateria().getNome(),
                    planejamentoMateria.getOrdem(),
                    nomeGrupo);
        }).toList());
    }

    @Transactional
    public Optional<PlanejamentoItemEtapa> adicionarEtapaPorAssunto(
            Long planejamentoId,
            PlanejamentoEtapaPorAssuntoDTO dados) {
        Optional<Planejamento> planejamento = planejamentoRepository.findById(planejamentoId);
        Optional<Assunto> assunto = buscarAssuntoPorId(dados.getAssuntoId());
        Optional<TipoRegistro> tipoRegistro = buscarTipoRegistroPorId(dados.getTipoRegistroId());
        if (planejamento.isEmpty() || assunto.isEmpty() || tipoRegistro.isEmpty()) {
            return Optional.empty();
        }

        List<PlanejamentoMateria> materias = planejamentoMateriaRepository
                .findByPlanejamentoIdAndMateriaIdOrderByOrdemAsc(
                        planejamentoId,
                        assunto.get().getMateria().getId());
        PlanejamentoMateria planejamentoMateria;
        if (dados.getPlanejamentoMateriaId() != null) {
            planejamentoMateria = materias.stream()
                    .filter(materia -> materia.getId().equals(dados.getPlanejamentoMateriaId()))
                    .findFirst()
                    .orElse(null);
        } else {
            planejamentoMateria = materias.size() == 1 ? materias.get(0) : null;
        }
        if (planejamentoMateria == null) {
            return Optional.empty();
        }

        List<PlanejamentoGrupoAssuntoItem> itens = planejamentoGrupoAssuntoItemRepository
                .findByPlanejamentoMateriaIdAndAssuntoId(
                        planejamentoMateria.getId(),
                        dados.getAssuntoId());
        PlanejamentoGrupoAssuntoItem item;
        if (!itens.isEmpty()) {
            item = itens.get(0);
        } else {
            PlanejamentoGrupoAssunto grupo = planejamentoGrupoAssuntoRepository
                    .findByPlanejamentoMateriaIdOrderByOrdemAsc(planejamentoMateria.getId())
                    .stream()
                    .filter(existenteGrupo ->
                            existenteGrupo.getTipo() == PlanejamentoGrupoAssuntoTipo.ASSUNTO
                                    && !planejamentoGrupoAssuntoItemRepository
                                            .existsByPlanejamentoGrupoAssuntoId(
                                                    existenteGrupo.getId()))
                    .findFirst()
                    .orElseGet(() -> {
                        PlanejamentoGrupoAssunto novoGrupo = new PlanejamentoGrupoAssunto();
                        novoGrupo.setPlanejamentoMateria(planejamentoMateria);
                        novoGrupo.setTipo(PlanejamentoGrupoAssuntoTipo.ASSUNTO);
                        novoGrupo.setPeso((short) 3);
                        return planejamentoGrupoAssuntoRepository.save(novoGrupo);
                    });

            item = new PlanejamentoGrupoAssuntoItem();
            item.setPlanejamentoGrupoAssunto(grupo);
            item.setAssunto(assunto.get());
            item.setPeso((short) 3);
            item = planejamentoGrupoAssuntoItemRepository.save(item);
        }

        Optional<PlanejamentoItemEtapa> existente = planejamentoItemEtapaRepository
                .findByPlanejamentoGrupoAssuntoItemIdOrderByOrdemAsc(item.getId())
                .stream()
                .filter(etapa -> etapa.getTipoRegistro().getId().equals(dados.getTipoRegistroId()))
                .findFirst();
        if (existente.isPresent()) {
            return existente;
        }

        PlanejamentoItemEtapa etapa = new PlanejamentoItemEtapa();
        etapa.setPlanejamentoGrupoAssuntoItem(item);
        etapa.setTipoRegistro(tipoRegistro.get());
        etapa.setConcluida(false);
        etapa.setConcluidaManualmente(false);
        return Optional.of(planejamentoItemEtapaRepository.save(etapa));
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
        PlanejamentoGrupoMateria grupoMateria = buscarGrupoMateriaPorId(
                planejamentoMateria.getPlanejamento().getId(),
                dados.getPlanejamentoGrupoMateriaId()).orElse(null);
        if (dados.getPlanejamentoGrupoMateriaId() != null && grupoMateria == null) {
            throw new PlanejamentoConflitoException(
                    "O bloco de matérias não pertence a este planejamento");
        }
        planejamentoMateria.setPlanejamentoGrupoMateria(grupoMateria);
        planejamentoMateria.setPeso(dados.getPeso() != null ? dados.getPeso() : (short) 3);
        planejamentoMateria.setDificuldade(dados.getDificuldade());
        planejamentoMateria.setOrdem(dados.getOrdem());
        planejamentoMateria.setDataFinalizarPrevista(dados.getDataFinalizarPrevista());
        planejamentoMateria.setDataFinalizado(dados.getDataFinalizado());
        planejamentoMateria.setObservacoes(dados.getObservacoes());
    }

    private void atualizarCampos(
            PlanejamentoGrupoMateria grupo,
            PlanejamentoGrupoMateriaDTO dados) {
        grupo.setNome(dados.getNome().trim());
        grupo.setPeso(dados.getPeso() != null ? dados.getPeso() : (short) 3);
        grupo.setDificuldade(dados.getDificuldade());
        grupo.setOrdem(dados.getOrdem());
        grupo.setDataPrevista(dados.getDataPrevista());
        grupo.setDataFinalizada(dados.getDataFinalizada());
        grupo.setObservacoes(dados.getObservacoes());
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
        item.setDificuldade(dados.getDificuldade());
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

    private Optional<PlanejamentoGrupoMateria> buscarGrupoMateriaPorId(
            Long planejamentoId,
            Long grupoMateriaId) {
        if (grupoMateriaId == null) {
            return Optional.empty();
        }

        return planejamentoGrupoMateriaRepository.findByIdAndPlanejamentoId(
                grupoMateriaId,
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

        return materiaRepository.findByIdAndAtivoTrue(materiaId);
    }

    private Optional<Assunto> buscarAssuntoPorId(Long assuntoId) {
        if (assuntoId == null) {
            return Optional.empty();
        }

        return assuntoRepository.findByIdAndAtivoTrue(assuntoId);
    }

    private Optional<TipoRegistro> buscarTipoRegistroPorId(Long tipoRegistroId) {
        if (tipoRegistroId == null) {
            return Optional.empty();
        }

        return tipoRegistroRepository.findByIdAndAtivoTrue(tipoRegistroId);
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
        if (assuntoJaFoiAdicionadoNaMateriaDoPlanejamento(grupo, assunto, null)) {
            throw new PlanejamentoConflitoException(
                    "O assunto já está cadastrado nesta ocorrência da matéria");
        }

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
        if (assuntoJaFoiAdicionadoNaMateriaDoPlanejamento(grupo, assunto, itemId)) {
            throw new PlanejamentoConflitoException(
                    "O assunto já está cadastrado nesta ocorrência da matéria");
        }

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

    private boolean assuntoJaFoiAdicionadoNaMateriaDoPlanejamento(
            PlanejamentoGrupoAssunto grupo,
            Assunto assunto,
            Long itemIdIgnorado) {
        return planejamentoGrupoAssuntoItemRepository
                .findByPlanejamentoMateriaIdAndAssuntoId(
                        grupo.getPlanejamentoMateria().getId(),
                        assunto.getId())
                .stream()
                .anyMatch(item -> !item.getId().equals(itemIdIgnorado));
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

    private boolean reordenacaoValida(
            PlanejamentoReordenacaoDTO dados,
            Set<Long> idsPermitidos) {
        if (dados == null || dados.getItens() == null || dados.getItens().isEmpty()) {
            return false;
        }

        Set<Long> ids = new HashSet<>();
        Set<Integer> ordens = new HashSet<>();
        for (PlanejamentoReordenacaoDTO.Item item : dados.getItens()) {
            if (item == null
                    || item.getId() == null
                    || item.getOrdem() == null
                    || item.getOrdem() < 1
                    || !idsPermitidos.contains(item.getId())
                    || !ids.add(item.getId())
                    || !ordens.add(item.getOrdem())) {
                return false;
            }
        }

        return true;
    }
}
