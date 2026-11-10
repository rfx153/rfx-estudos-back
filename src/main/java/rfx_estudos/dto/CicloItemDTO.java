package rfx_estudos.dto;

public class CicloItemDTO {

    private Long materiaId;
    private Long planejamentoGrupoMateriaId;
    private Integer ordem;
    private Boolean ativo = true;

    public CicloItemDTO() {}

    public Long getMateriaId() { return materiaId; }
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }

    public Long getPlanejamentoGrupoMateriaId() { return planejamentoGrupoMateriaId; }
    public void setPlanejamentoGrupoMateriaId(Long planejamentoGrupoMateriaId) {
        this.planejamentoGrupoMateriaId = planejamentoGrupoMateriaId;
    }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
