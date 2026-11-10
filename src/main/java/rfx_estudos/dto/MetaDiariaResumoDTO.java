package rfx_estudos.dto;

import java.time.LocalDate;

public record MetaDiariaResumoDTO(
        Long id,
        LocalDate data,
        Integer metaMinutos,
        Long tempoRealizadoMinutos) {}
