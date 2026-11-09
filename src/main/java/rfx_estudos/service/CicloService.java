package rfx_estudos.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import rfx_estudos.domain.Ciclo;
import rfx_estudos.domain.CicloMateria;
import rfx_estudos.domain.Materia;
import rfx_estudos.dto.CicloMateriaDTO;
import rfx_estudos.dto.CicloReordenacaoDTO;
import rfx_estudos.repository.CicloMateriaRepository;
import rfx_estudos.repository.CicloRepository;
import rfx_estudos.repository.MateriaRepository;

@Service
public class CicloService {

    private final CicloRepository cicloRepository;
    private final CicloMateriaRepository cicloMateriaRepository;
    private final MateriaRepository materiaRepository;
    private final EntityManager entityManager;

    public CicloService(
            CicloRepository cicloRepository,
            CicloMateriaRepository cicloMateriaRepository,
            MateriaRepository materiaRepository,
            EntityManager entityManager) {
        this.cicloRepository = cicloRepository;
        this.cicloMateriaRepository = cicloMateriaRepository;
        this.materiaRepository = materiaRepository;
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
    public Optional<List<CicloMateria>> listarMaterias(Long cicloId) {
        if (!cicloRepository.existsById(cicloId)) {
            return Optional.empty();
        }
        return Optional.of(cicloMateriaRepository.findByCicloIdOrderByOrdemAsc(cicloId));
    }

    @Transactional
    public Optional<CicloMateria> adicionarMateria(Long cicloId, CicloMateriaDTO dados) {
        Optional<Ciclo> ciclo = cicloRepository.findById(cicloId);
        Optional<Materia> materia = materiaRepository.findById(dados.getMateriaId());

        if (ciclo.isEmpty() || materia.isEmpty()) {
            return Optional.empty();
        }
        if (cicloMateriaRepository.existsByCicloIdAndMateriaId(cicloId, dados.getMateriaId())) {
            throw new CicloConflitoException("A matéria já pertence a este ciclo");
        }

        CicloMateria item = new CicloMateria();
        item.setCiclo(ciclo.get());
        item.setMateria(materia.get());
        preencherMateria(item, dados);
        ciclo.get().marcarAtualizado();
        cicloRepository.save(ciclo.get());
        return Optional.of(cicloMateriaRepository.save(item));
    }

    @Transactional
    public Optional<CicloMateria> atualizarMateria(Long cicloId, Long id, CicloMateriaDTO dados) {
        Optional<CicloMateria> item = cicloMateriaRepository.findByIdAndCicloId(id, cicloId);
        Optional<Materia> materia = materiaRepository.findById(dados.getMateriaId());
        if (item.isEmpty() || materia.isEmpty()) {
            return Optional.empty();
        }

        if (!item.get().getMateria().getId().equals(dados.getMateriaId())
                && cicloMateriaRepository.existsByCicloIdAndMateriaId(cicloId, dados.getMateriaId())) {
            throw new CicloConflitoException("A matéria já pertence a este ciclo");
        }

        item.get().setMateria(materia.get());
        preencherMateria(item.get(), dados);
        item.get().getCiclo().marcarAtualizado();
        cicloRepository.save(item.get().getCiclo());
        return Optional.of(cicloMateriaRepository.save(item.get()));
    }

    @Transactional
    public boolean excluirMateria(Long cicloId, Long id) {
        Optional<CicloMateria> item = cicloMateriaRepository.findByIdAndCicloId(id, cicloId);
        if (item.isEmpty()) {
            return false;
        }

        Ciclo ciclo = item.get().getCiclo();
        cicloMateriaRepository.delete(item.get());
        cicloMateriaRepository.flush();
        List<CicloMateria> restantes = cicloMateriaRepository.findByCicloIdOrderByOrdemAsc(cicloId);
        reordenarInternamente(cicloId, restantes.stream()
                .map(CicloMateria::getId)
                .toList());
        ciclo.marcarAtualizado();
        cicloRepository.save(ciclo);
        return true;
    }

    @Transactional
    public Optional<List<CicloMateria>> reordenar(
            Long cicloId,
            CicloReordenacaoDTO dados) {
        Optional<Ciclo> ciclo = cicloRepository.findById(cicloId);
        if (ciclo.isEmpty()) {
            return Optional.empty();
        }

        List<CicloMateria> atuais = cicloMateriaRepository.findByCicloIdOrderByOrdemAsc(cicloId);
        validarReordenacao(atuais, dados);
        List<Long> idsOrdenados = dados.getItens().stream()
                .sorted((a, b) -> a.getOrdem().compareTo(b.getOrdem()))
                .map(CicloReordenacaoDTO.Item::getId)
                .toList();
        reordenarInternamente(cicloId, idsOrdenados);
        ciclo.get().marcarAtualizado();
        cicloRepository.save(ciclo.get());
        return Optional.of(cicloMateriaRepository.findByCicloIdOrderByOrdemAsc(cicloId));
    }

    private void reordenarInternamente(Long cicloId, List<Long> idsOrdenados) {
        if (idsOrdenados.isEmpty()) {
            return;
        }

        cicloMateriaRepository.tornarOrdensTemporarias(cicloId);
        for (int indice = 0; indice < idsOrdenados.size(); indice++) {
            int atualizados = cicloMateriaRepository.atualizarOrdem(
                    cicloId,
                    idsOrdenados.get(indice),
                    indice + 1);
            if (atualizados != 1) {
                throw new CicloConflitoException("A sequência informada é inválida");
            }
        }
        entityManager.flush();
        entityManager.clear();
    }

    private void validarReordenacao(
            List<CicloMateria> atuais,
            CicloReordenacaoDTO dados) {
        if (dados == null || dados.getItens() == null || dados.getItens().size() != atuais.size()) {
            throw new IllegalArgumentException("A sequência deve conter todas as matérias do ciclo");
        }

        Set<Long> idsAtuais = atuais.stream().map(CicloMateria::getId).collect(java.util.stream.Collectors.toSet());
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
            throw new IllegalArgumentException("A sequência contém matérias de outro ciclo");
        }
    }

    private void preencherCiclo(Ciclo ciclo, Ciclo dados) {
        ciclo.setNome(dados.getNome().trim());
        ciclo.setAtivo(dados.getAtivo() == null ? true : dados.getAtivo());
        ciclo.setObservacoes(limparTexto(dados.getObservacoes()));
    }

    private void preencherMateria(CicloMateria item, CicloMateriaDTO dados) {
        item.setOrdem(dados.getOrdem());
        item.setAtivo(dados.getAtivo() == null ? true : dados.getAtivo());
    }

    private String limparTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        return texto.trim();
    }
}
