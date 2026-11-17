package rfx_estudos.dto;

public class PlanejamentoEtapaPorAssuntoDTO {

    private Long assuntoId;
    private Long tipoRegistroId;
    private Long planejamentoMateriaId;

    public PlanejamentoEtapaPorAssuntoDTO() {}

    public Long getAssuntoId() { return assuntoId; }
    public void setAssuntoId(Long assuntoId) { this.assuntoId = assuntoId; }

    public Long getTipoRegistroId() { return tipoRegistroId; }
    public void setTipoRegistroId(Long tipoRegistroId) { this.tipoRegistroId = tipoRegistroId; }

    public Long getPlanejamentoMateriaId() { return planejamentoMateriaId; }
    public void setPlanejamentoMateriaId(Long planejamentoMateriaId) {
        this.planejamentoMateriaId = planejamentoMateriaId;
    }
}
