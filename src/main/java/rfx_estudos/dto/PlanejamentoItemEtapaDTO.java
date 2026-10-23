package rfx_estudos.dto;

import java.time.LocalDate;

public class PlanejamentoItemEtapaDTO {

    private Long tipoRegistroId;
    private Boolean concluida = false;
    private Boolean concluidaManualmente = false;
    private Integer ordem;
    private LocalDate dataPrevista;
    private LocalDate dataConcluida;
    private String observacoes;

    public PlanejamentoItemEtapaDTO() {}

    public Long getTipoRegistroId() { return tipoRegistroId; }
    public void setTipoRegistroId(Long tipoRegistroId) { this.tipoRegistroId = tipoRegistroId; }

    public Boolean getConcluida() { return concluida; }
    public void setConcluida(Boolean concluida) { this.concluida = concluida; }

    public Boolean getConcluidaManualmente() { return concluidaManualmente; }
    public void setConcluidaManualmente(Boolean concluidaManualmente) {
        this.concluidaManualmente = concluidaManualmente;
    }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public LocalDate getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }

    public LocalDate getDataConcluida() { return dataConcluida; }
    public void setDataConcluida(LocalDate dataConcluida) { this.dataConcluida = dataConcluida; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
