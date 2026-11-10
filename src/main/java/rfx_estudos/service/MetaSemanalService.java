package rfx_estudos.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rfx_estudos.domain.MetaSemanal;
import rfx_estudos.dto.MetaSemanalDTO;
import rfx_estudos.dto.MetaSemanalResumoDTO;
import rfx_estudos.repository.MetaSemanalRepository;

@Service
public class MetaSemanalService {

    private final MetaSemanalRepository metaSemanalRepository;

    public MetaSemanalService(MetaSemanalRepository metaSemanalRepository) {
        this.metaSemanalRepository = metaSemanalRepository;
    }

    @Transactional(readOnly = true)
    public List<MetaSemanalResumoDTO> resumir(LocalDate inicioSemana) {
        if (!inicioSemanaValido(inicioSemana)) {
            throw new IllegalArgumentException("O início da semana deve ser um domingo");
        }

        return metaSemanalRepository.resumir(inicioSemana).stream()
                .map(meta -> new MetaSemanalResumoDTO(
                        meta.getId(),
                        meta.getInicioSemana(),
                        meta.getMetaMinutos(),
                        meta.getTempoRealizadoMinutos()))
                .toList();
    }

    @Transactional
    public Optional<MetaSemanal> criar(MetaSemanalDTO dados) {
        if (!dadosValidos(dados)
                || metaSemanalRepository.findByInicioSemana(dados.getInicioSemana()).isPresent()) {
            return Optional.empty();
        }

        MetaSemanal meta = new MetaSemanal();
        preencher(meta, dados);
        return Optional.of(metaSemanalRepository.save(meta));
    }

    @Transactional
    public Optional<MetaSemanal> atualizar(Long id, MetaSemanalDTO dados) {
        if (!dadosValidos(dados)) {
            return Optional.empty();
        }

        Optional<MetaSemanal> meta = metaSemanalRepository.findById(id);
        if (meta.isEmpty()) {
            return Optional.empty();
        }

        Optional<MetaSemanal> metaDaSemana = metaSemanalRepository
                .findByInicioSemana(dados.getInicioSemana());
        if (metaDaSemana.isPresent() && !metaDaSemana.get().getId().equals(id)) {
            return Optional.empty();
        }

        preencher(meta.get(), dados);
        return Optional.of(metaSemanalRepository.save(meta.get()));
    }

    @Transactional
    public boolean excluir(Long id) {
        return metaSemanalRepository.findById(id)
                .map(meta -> {
                    metaSemanalRepository.delete(meta);
                    return true;
                })
                .orElse(false);
    }

    private boolean dadosValidos(MetaSemanalDTO dados) {
        return dados != null
                && inicioSemanaValido(dados.getInicioSemana())
                && dados.getMetaMinutos() != null
                && dados.getMetaMinutos() > 0;
    }

    private boolean inicioSemanaValido(LocalDate inicioSemana) {
        return inicioSemana != null && inicioSemana.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private void preencher(MetaSemanal meta, MetaSemanalDTO dados) {
        meta.setInicioSemana(dados.getInicioSemana());
        meta.setMetaMinutos(dados.getMetaMinutos());
    }
}
