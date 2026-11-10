package rfx_estudos.dto;

import java.time.LocalDate;

public class MetaDiariaDTO {

    private LocalDate data;
    private Integer metaMinutos;

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Integer getMetaMinutos() { return metaMinutos; }
    public void setMetaMinutos(Integer metaMinutos) { this.metaMinutos = metaMinutos; }
}
