package rfx_estudos.dto;

import java.time.LocalDate;

public record MetaSemanalResumoDTO(
        Long id,
        LocalDate inicioSemana,
        Integer metaMinutos,
        Long tempoRealizadoMinutos) {}
