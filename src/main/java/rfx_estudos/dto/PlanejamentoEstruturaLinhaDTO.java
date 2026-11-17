package rfx_estudos.dto;

import java.time.LocalDate;

public interface PlanejamentoEstruturaLinhaDTO {
    Long getGrupoId();
    Long getPlanejamentoMateriaId();
    String getGrupoTipo();
    String getGrupoNome();
    Short getGrupoPeso();
    Integer getGrupoOrdem();
    Long getItemId();
    Long getAssuntoId();
    String getAssuntoNome();
    Short getItemPeso();
    Short getItemDificuldade();
    Integer getItemOrdem();
    Long getEtapaId();
    Long getTipoRegistroId();
    String getTipoRegistroNome();
    Integer getEtapaOrdem();
    Boolean getEtapaConcluida();
    LocalDate getEtapaDataConcluida();
}
