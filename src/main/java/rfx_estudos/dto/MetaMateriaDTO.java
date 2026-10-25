package rfx_estudos.dto;

public class MetaMateriaDTO {

    private Long materiaId;
    private Integer metaSemanalMinutos;

    public Long getMateriaId() { return materiaId; }
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }

    public Integer getMetaSemanalMinutos() { return metaSemanalMinutos; }
    public void setMetaSemanalMinutos(Integer metaSemanalMinutos) {
        this.metaSemanalMinutos = metaSemanalMinutos;
    }
}
