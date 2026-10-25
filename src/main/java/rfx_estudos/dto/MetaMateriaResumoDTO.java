package rfx_estudos.dto;

public record MetaMateriaResumoDTO(
        Long id,
        Long materiaId,
        String materiaNome,
        Integer metaSemanalMinutos,
        Long metaPeriodoMinutos,
        Long tempoRealizadoMinutos) {}
