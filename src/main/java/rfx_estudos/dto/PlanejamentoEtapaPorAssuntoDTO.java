package rfx_estudos.dto;

public class PlanejamentoEtapaPorAssuntoDTO {

    private Long assuntoId;
    private Long tipoRegistroId;

    public PlanejamentoEtapaPorAssuntoDTO() {}

    public Long getAssuntoId() { return assuntoId; }
    public void setAssuntoId(Long assuntoId) { this.assuntoId = assuntoId; }

    public Long getTipoRegistroId() { return tipoRegistroId; }
    public void setTipoRegistroId(Long tipoRegistroId) { this.tipoRegistroId = tipoRegistroId; }
}
