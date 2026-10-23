package rfx_estudos.dto;

import java.time.LocalDate;

public class PlanejamentoGrupoAssuntoItemDTO {

    private Long assuntoId;
    private Short peso = 3;
    private Integer ordem;
    private LocalDate dataPrevista;
    private String observacoes;

    public PlanejamentoGrupoAssuntoItemDTO() {}

    public Long getAssuntoId() { return assuntoId; }
    public void setAssuntoId(Long assuntoId) { this.assuntoId = assuntoId; }

    public Short getPeso() { return peso; }
    public void setPeso(Short peso) { this.peso = peso; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public LocalDate getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
