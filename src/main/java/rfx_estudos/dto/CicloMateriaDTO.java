package rfx_estudos.dto;

public class CicloMateriaDTO {

    private Long materiaId;
    private Integer ordem;
    private Boolean ativo = true;

    public CicloMateriaDTO() {}

    public Long getMateriaId() { return materiaId; }
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
