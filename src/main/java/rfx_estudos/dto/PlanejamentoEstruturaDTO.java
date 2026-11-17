package rfx_estudos.dto;

import java.time.LocalDate;
import java.util.List;

import rfx_estudos.domain.PlanejamentoGrupoAssuntoTipo;

public record PlanejamentoEstruturaDTO(
        List<Grupo> grupos,
        List<Item> itens,
        List<Etapa> etapas) {

    public record Grupo(
            Long id,
            Long planejamentoMateriaId,
            PlanejamentoGrupoAssuntoTipo tipo,
            String nome,
            Short peso,
            Integer ordem) {
    }

    public record Item(
            Long id,
            Long grupoId,
            Long assuntoId,
            String assuntoNome,
            Short peso,
            Short dificuldade,
            Integer ordem) {
    }

    public record Etapa(
            Long id,
            Long itemId,
            Long tipoRegistroId,
            String tipoRegistroNome,
            Integer ordem,
            Boolean iniciada,
            Boolean concluida,
            LocalDate dataConcluida) {
    }
}
