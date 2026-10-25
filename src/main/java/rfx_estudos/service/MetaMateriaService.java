package rfx_estudos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rfx_estudos.domain.Materia;
import rfx_estudos.domain.MetaMateria;
import rfx_estudos.dto.MetaMateriaDTO;
import rfx_estudos.dto.MetaMateriaResumoDTO;
import rfx_estudos.repository.MateriaRepository;
import rfx_estudos.repository.MetaMateriaRepository;

@Service
public class MetaMateriaService {

    private final MetaMateriaRepository metaMateriaRepository;
    private final MateriaRepository materiaRepository;

    public MetaMateriaService(
            MetaMateriaRepository metaMateriaRepository,
            MateriaRepository materiaRepository) {
        this.metaMateriaRepository = metaMateriaRepository;
        this.materiaRepository = materiaRepository;
    }

    @Transactional(readOnly = true)
    public List<MetaMateria> listarTodas() {
        return metaMateriaRepository.findAllByOrderByMateriaNomeAsc();
    }

    @Transactional
    public Optional<MetaMateria> criar(MetaMateriaDTO dados) {
        if (!dadosValidos(dados) || metaMateriaRepository.findByMateriaId(dados.getMateriaId()).isPresent()) {
            return Optional.empty();
        }

        return materiaRepository.findById(dados.getMateriaId()).map(materia -> {
            MetaMateria meta = new MetaMateria();
            preencher(meta, materia, dados);
            return metaMateriaRepository.save(meta);
        });
    }

    @Transactional
    public Optional<MetaMateria> atualizar(Long id, MetaMateriaDTO dados) {
        if (!dadosValidos(dados)) {
            return Optional.empty();
        }

        Optional<MetaMateria> meta = metaMateriaRepository.findById(id);
        Optional<Materia> materia = materiaRepository.findById(dados.getMateriaId());
        if (meta.isEmpty() || materia.isEmpty()) {
            return Optional.empty();
        }

        Optional<MetaMateria> outraMeta = metaMateriaRepository.findByMateriaId(dados.getMateriaId());
        if (outraMeta.isPresent() && !outraMeta.get().getId().equals(id)) {
            return Optional.empty();
        }

        preencher(meta.get(), materia.get(), dados);
        return Optional.of(metaMateriaRepository.save(meta.get()));
    }

    @Transactional
    public boolean excluir(Long id) {
        return metaMateriaRepository.findById(id)
                .map(meta -> {
                    metaMateriaRepository.delete(meta);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<MetaMateriaResumoDTO> resumir(
            LocalDate inicio,
            LocalDate fim,
            boolean mensal) {
        if (inicio == null || fim == null || inicio.isAfter(fim)) {
            throw new IllegalArgumentException("O período informado é inválido");
        }

        long multiplicador = mensal ? 4L : 1L;
        return metaMateriaRepository.resumirPorPeriodo(inicio, fim).stream()
                .map(meta -> new MetaMateriaResumoDTO(
                        meta.getId(),
                        meta.getMateriaId(),
                        meta.getMateriaNome(),
                        meta.getMetaSemanalMinutos(),
                        meta.getMetaSemanalMinutos().longValue() * multiplicador,
                        meta.getTempoRealizadoMinutos()))
                .toList();
    }

    private boolean dadosValidos(MetaMateriaDTO dados) {
        return dados != null
                && dados.getMateriaId() != null
                && dados.getMetaSemanalMinutos() != null
                && dados.getMetaSemanalMinutos() > 0;
    }

    private void preencher(MetaMateria meta, Materia materia, MetaMateriaDTO dados) {
        meta.setMateria(materia);
        meta.setMetaSemanalMinutos(dados.getMetaSemanalMinutos());
    }
}
