package rfx_estudos.dto;

import java.time.LocalDate;

public class MetaSemanalDTO {

    private LocalDate inicioSemana;
    private Integer metaMinutos;

    public LocalDate getInicioSemana() { return inicioSemana; }
    public void setInicioSemana(LocalDate inicioSemana) { this.inicioSemana = inicioSemana; }

    public Integer getMetaMinutos() { return metaMinutos; }
    public void setMetaMinutos(Integer metaMinutos) { this.metaMinutos = metaMinutos; }
}
