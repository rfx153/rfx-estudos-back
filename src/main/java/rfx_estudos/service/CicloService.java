package rfx_estudos.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import rfx_estudos.domain.Ciclo;
import rfx_estudos.domain.CicloItem;
import rfx_estudos.domain.Materia;
import rfx_estudos.domain.PlanejamentoGrupoMateria;
import rfx_estudos.dto.CicloItemDTO;
import rfx_estudos.dto.CicloReordenacaoDTO;
import rfx_estudos.repository.CicloItemRepository;
import rfx_estudos.repository.CicloRepository;
import rfx_estudos.repository.MateriaRepository;
import rfx_estudos.repository.PlanejamentoGrupoMateriaRepository;

@Service
public class CicloService {

    private final CicloRepository cicloRepository;
    private final CicloItemRepository cicloItemRepository;
    private final MateriaRepository materiaRepository;
    private final PlanejamentoGrupoMateriaRepository planejamentoGrupoMateriaRepository;
    private final EntityManager entityManager;

    public CicloService(
            CicloRepository cicloRepository,
            CicloItemRepository cicloItemRepository,
            MateriaRepository materiaRepository,
            PlanejamentoGrupoMateriaRepository planejamentoGrupoMateriaRepository,
            EntityManager entityManager) {
        this.cicloRepository = cicloRepository;
        this.cicloItemRepository = cicloItemRepository;
        this.materiaRepository = materiaRepository;
        this.planejamentoGrupoMateriaRepository = planejamentoGrupoMateriaRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<Ciclo> listarTodos() {
        return cicloRepository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Ciclo> buscarPorId(Long id) {
        return cicloRepository.findById(id);
    }

    @Transactional
    public Ciclo criar(Ciclo dados) {
        Ciclo ciclo = new Ciclo();
        preencherCiclo(ciclo, dados);
        return cicloRepository.save(ciclo);
    }

    @Transactional
    public Optional<Ciclo> atualizar(Long id, Ciclo dados) {
        return cicloRepository.findById(id).map(ciclo -> {
            preencherCiclo(ciclo, dados);
            return cicloRepository.save(ciclo);
        });
    }

    @Transactional
    public boolean excluir(Long id) {
        return cicloRepository.findById(id).map(ciclo -> {
            cicloRepository.delete(ciclo);
            return true;
        }).orElse(false);
    }

    @Transactional(readOnly = true)
    public Optional<List<CicloItem>> listarItens(Long cicloId) {
        if (!cicloRepository.existsById(cicloId)) {
            return Optional.empty();
        }
        return Optional.of(cicloItemRepository.findByCicloIdOrderByOrdemAsc(cicloId));
    }

    @Transactional
    public Optional<CicloItem> adicionarItem(Long cicloId, CicloItemDTO dados) {
        Optional<Ciclo> ciclo = cicloRepository.findById(cicloId);
        if (ciclo.isEmpty()) {
            return Optional.empty();
        }

        CicloItem item = new CicloItem();
        item.setCiclo(ciclo.get());
        if (!preencherReferencia(item, dados, cicloId, false)) {
            return Optional.empty();
        }
        preencherItem(item, dados);
        ciclo.get().marcarAtualizado();
        cicloRepository.save(ciclo.get());
        return Optional.of(cicloItemRepository.save(item));
    }

    @Transactional
    public Optional<CicloItem> atualizarItem(Long cicloId, Long id, CicloItemDTO dados) {
        Optional<CicloItem> item = cicloItemRepository.findByIdAndCicloId(id, cicloId);
        if (item.isEmpty() || !preencherReferencia(item.get(), dados, cicloId, true)) {
            return Optional.empty();
        }

        preencherItem(item.get(), dados);
        item.get().getCiclo().marcarAtualizado();
        cicloRepository.save(item.get().getCiclo());
        return Optional.of(cicloItemRepository.save(item.get()));
    }

    private boolean preencherReferencia(
            CicloItem item,
            CicloItemDTO dados,
            Long cicloId,
            boolean atualizacao) {
        if (dados.getMateriaId() != null) {
            Optional<Materia> materia = materiaRepository.findById(dados.getMateriaId());
            if (materia.isEmpty()) return false;
            boolean mesmaMateria = atualizacao && item.getMateria() != null
                    && item.getMateria().getId().equals(dados.getMateriaId());
            if (!mesmaMateria && cicloItemRepository.existsByCicloIdAndMateriaId(cicloId, dados.getMateriaId())) {
                throw new CicloConflitoException("A matéria já pertence a este ciclo");
            }
            item.setMateria(materia.get());
            item.setPlanejamentoGrupoMateria(null);
            return true;
        }

        Optional<PlanejamentoGrupoMateria> grupo = planejamentoGrupoMateriaRepository
                .findById(dados.getPlanejamentoGrupoMateriaId());
        if (grupo.isEmpty()) return false;
        boolean mesmoGrupo = atualizacao && item.getPlanejamentoGrupoMateria() != null
                && item.getPlanejamentoGrupoMateria().getId().equals(dados.getPlanejamentoGrupoMateriaId());
        if (!mesmoGrupo && cicloItemRepository.existsByCicloIdAndPlanejamentoGrupoMateriaId(
                cicloId, dados.getPlanejamentoGrupoMateriaId())) {
            throw new CicloConflitoException("O bloco já pertence a este ciclo");
        }
        item.setMateria(null);
        item.setPlanejamentoGrupoMateria(grupo.get());
        return true;
    }

    @Transactional
    public boolean excluirItem(Long cicloId, Long id) {
        Optional<CicloItem> item = cicloItemRepository.findByIdAndCicloId(id, cicloId);
        if (item.isEmpty()) return false;

        Ciclo ciclo = item.get().getCiclo();
        cicloItemRepository.delete(item.get());
        cicloItemRepository.flush();
        List<CicloItem> restantes = cicloItemRepository.findByCicloIdOrderByOrdemAsc(cicloId);
        reordenarInternamente(cicloId, restantes.stream().map(CicloItem::getId).toList());
        ciclo.marcarAtualizado();
        cicloRepository.save(ciclo);
        return true;
    }

    @Transactional
    public Optional<List<CicloItem>> reordenar(Long cicloId, CicloReordenacaoDTO dados) {
        Optional<Ciclo> ciclo = cicloRepository.findById(cicloId);
        if (ciclo.isEmpty()) return Optional.empty();

        List<CicloItem> atuais = cicloItemRepository.findByCicloIdOrderByOrdemAsc(cicloId);
        validarReordenacao(atuais, dados);
        List<Long> idsOrdenados = dados.getItens().stream()
                .sorted((a, b) -> a.getOrdem().compareTo(b.getOrdem()))
                .map(CicloReordenacaoDTO.Item::getId)
                .toList();
        reordenarInternamente(cicloId, idsOrdenados);
        ciclo.get().marcarAtualizado();
        cicloRepository.save(ciclo.get());
        return Optional.of(cicloItemRepository.findByCicloIdOrderByOrdemAsc(cicloId));
    }

    private void reordenarInternamente(Long cicloId, List<Long> idsOrdenados) {
        if (idsOrdenados.isEmpty()) return;
        cicloItemRepository.tornarOrdensTemporarias(cicloId);
        for (int indice = 0; indice < idsOrdenados.size(); indice++) {
            int atualizados = cicloItemRepository.atualizarOrdem(cicloId, idsOrdenados.get(indice), indice + 1);
            if (atualizados != 1) {
                throw new CicloConflitoException("A sequência informada é inválida");
            }
        }
        entityManager.flush();
        entityManager.clear();
    }

    private void validarReordenacao(List<CicloItem> atuais, CicloReordenacaoDTO dados) {
        if (dados == null || dados.getItens() == null || dados.getItens().size() != atuais.size()) {
            throw new IllegalArgumentException("A sequência deve conter todos os itens do ciclo");
        }

        Set<Long> idsAtuais = atuais.stream().map(CicloItem::getId)
                .collect(java.util.stream.Collectors.toSet());
        Set<Long> idsInformados = new HashSet<>();
        Set<Integer> ordens = new HashSet<>();
        for (CicloReordenacaoDTO.Item item : dados.getItens()) {
            if (item == null || item.getId() == null || item.getOrdem() == null
                    || item.getOrdem() < 1 || item.getOrdem() > atuais.size()
                    || !idsInformados.add(item.getId()) || !ordens.add(item.getOrdem())) {
                throw new IllegalArgumentException("A sequência informada é inválida");
            }
        }
        if (!idsAtuais.equals(idsInformados)) {
            throw new IllegalArgumentException("A sequência contém itens de outro ciclo");
        }
    }

    private void preencherCiclo(Ciclo ciclo, Ciclo dados) {
        ciclo.setNome(dados.getNome().trim());
        ciclo.setAtivo(dados.getAtivo() == null ? true : dados.getAtivo());
        ciclo.setObservacoes(limparTexto(dados.getObservacoes()));
    }

    private void preencherItem(CicloItem item, CicloItemDTO dados) {
        item.setOrdem(dados.getOrdem());
        item.setAtivo(dados.getAtivo() == null ? true : dados.getAtivo());
    }

    private String limparTexto(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }
}
