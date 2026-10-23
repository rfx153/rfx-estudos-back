package rfx_estudos.dto;

import java.time.LocalDate;

import rfx_estudos.domain.PlanejamentoGrupoAssuntoTipo;

public class PlanejamentoGrupoAssuntoDTO {

    private PlanejamentoGrupoAssuntoTipo tipo;
    private String nome;
    private Short peso = 3;
    private Integer ordem;
    private LocalDate dataPrevista;
    private LocalDate dataFinalizada;
    private String observacoes;

    public PlanejamentoGrupoAssuntoDTO() {}

    public PlanejamentoGrupoAssuntoTipo getTipo() { return tipo; }
    public void setTipo(PlanejamentoGrupoAssuntoTipo tipo) { this.tipo = tipo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Short getPeso() { return peso; }
    public void setPeso(Short peso) { this.peso = peso; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public LocalDate getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }

    public LocalDate getDataFinalizada() { return dataFinalizada; }
    public void setDataFinalizada(LocalDate dataFinalizada) { this.dataFinalizada = dataFinalizada; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
