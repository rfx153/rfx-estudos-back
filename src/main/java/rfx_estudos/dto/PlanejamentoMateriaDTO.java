package rfx_estudos.dto;

import java.time.LocalDate;

public class PlanejamentoMateriaDTO {

    private Long materiaId;
    private Long planejamentoGrupoMateriaId;
    private Short peso = 3;
    private Integer ordem;
    private LocalDate dataFinalizarPrevista;
    private LocalDate dataFinalizado;
    private String observacoes;

    public PlanejamentoMateriaDTO() {}

    public Long getMateriaId() { return materiaId; }
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }

    public Long getPlanejamentoGrupoMateriaId() { return planejamentoGrupoMateriaId; }
    public void setPlanejamentoGrupoMateriaId(Long planejamentoGrupoMateriaId) {
        this.planejamentoGrupoMateriaId = planejamentoGrupoMateriaId;
    }

    public Short getPeso() { return peso; }
    public void setPeso(Short peso) { this.peso = peso; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public LocalDate getDataFinalizarPrevista() { return dataFinalizarPrevista; }
    public void setDataFinalizarPrevista(LocalDate dataFinalizarPrevista) {
        this.dataFinalizarPrevista = dataFinalizarPrevista;
    }

    public LocalDate getDataFinalizado() { return dataFinalizado; }
    public void setDataFinalizado(LocalDate dataFinalizado) { this.dataFinalizado = dataFinalizado; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
