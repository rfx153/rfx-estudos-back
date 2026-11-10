package rfx_estudos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rfx_estudos.domain.MetaDiaria;
import rfx_estudos.dto.MetaDiariaDTO;
import rfx_estudos.dto.MetaDiariaResumoDTO;
import rfx_estudos.repository.MetaDiariaRepository;

@Service
public class MetaDiariaService {

    private final MetaDiariaRepository metaDiariaRepository;

    public MetaDiariaService(MetaDiariaRepository metaDiariaRepository) {
        this.metaDiariaRepository = metaDiariaRepository;
    }

    @Transactional(readOnly = true)
    public List<MetaDiariaResumoDTO> resumir(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null || inicio.isAfter(fim)) {
            throw new IllegalArgumentException("O período informado é inválido");
        }

        return metaDiariaRepository.resumirPorPeriodo(inicio, fim).stream()
                .map(meta -> new MetaDiariaResumoDTO(
                        meta.getId(),
                        meta.getData(),
                        meta.getMetaMinutos(),
                        meta.getTempoRealizadoMinutos()))
                .toList();
    }

    @Transactional
    public Optional<MetaDiaria> criar(MetaDiariaDTO dados) {
        if (!dadosValidos(dados) || metaDiariaRepository.findByData(dados.getData()).isPresent()) {
            return Optional.empty();
        }

        MetaDiaria meta = new MetaDiaria();
        preencher(meta, dados);
        return Optional.of(metaDiariaRepository.save(meta));
    }

    @Transactional
    public Optional<MetaDiaria> atualizar(Long id, MetaDiariaDTO dados) {
        if (!dadosValidos(dados)) {
            return Optional.empty();
        }

        Optional<MetaDiaria> meta = metaDiariaRepository.findById(id);
        if (meta.isEmpty()) {
            return Optional.empty();
        }

        Optional<MetaDiaria> metaDaData = metaDiariaRepository.findByData(dados.getData());
        if (metaDaData.isPresent() && !metaDaData.get().getId().equals(id)) {
            return Optional.empty();
        }

        preencher(meta.get(), dados);
        return Optional.of(metaDiariaRepository.save(meta.get()));
    }

    @Transactional
    public boolean excluir(Long id) {
        return metaDiariaRepository.findById(id)
                .map(meta -> {
                    metaDiariaRepository.delete(meta);
                    return true;
                })
                .orElse(false);
    }

    private boolean dadosValidos(MetaDiariaDTO dados) {
        return dados != null
                && dados.getData() != null
                && dados.getMetaMinutos() != null
                && dados.getMetaMinutos() > 0;
    }

    private void preencher(MetaDiaria meta, MetaDiariaDTO dados) {
        meta.setData(dados.getData());
        meta.setMetaMinutos(dados.getMetaMinutos());
    }
}
