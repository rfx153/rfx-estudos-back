package rfx_estudos.dto;

public record PlanejamentoDestinoRegistroDTO(
        Long planejamentoMateriaId,
        Long etapaId,
        String planejamentoNome,
        String blocoMateriaNome,
        String materiaNome,
        Integer materiaOrdem,
        String grupoAssuntoNome) {}

