package rfx_estudos.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "planejamento_itens")
public class PlanejamentoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "planejamento_fk", nullable = false)
    private Planejamento planejamento;

    @ManyToOne
    @JoinColumn(name = "materia_fk", nullable = false)
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "assunto_fk")
    private Assunto assunto;

    @ManyToOne
    @JoinColumn(name = "material_tipo_fk")
    private MaterialTipo materialTipo;

    @Column(name = "material_nome", length = 255)
    private String materialNome;

    @Column(length = 50)
    private String prioridade = "Media";

    @Column(columnDefinition = "TEXT")
    private String meta;

    @Column(name = "data_prevista")
    private LocalDate dataPrevista;

    @Column(name = "link_documento", columnDefinition = "TEXT")
    private String linkDocumento;

    @Column(length = 50)
    private String status = "Pendente";

    @Column(name = "data_finalizacao")
    private LocalDate dataFinalizacao;

    @Column(name = "ordem")
    private Integer ordem;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    public PlanejamentoItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Planejamento getPlanejamento() { return planejamento; }
    public void setPlanejamento(Planejamento planejamento) { this.planejamento = planejamento; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public Assunto getAssunto() { return assunto; }
    public void setAssunto(Assunto assunto) { this.assunto = assunto; }

    public MaterialTipo getMaterialTipo() { return materialTipo; }
    public void setMaterialTipo(MaterialTipo materialTipo) { this.materialTipo = materialTipo; }

    public String getMaterialNome() { return materialNome; }
    public void setMaterialNome(String materialNome) { this.materialNome = materialNome; }

    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }

    public String getMeta() { return meta; }
    public void setMeta(String meta) { this.meta = meta; }

    public LocalDate getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }

    public String getLinkDocumento() { return linkDocumento; }
    public void setLinkDocumento(String linkDocumento) { this.linkDocumento = linkDocumento; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDataFinalizacao() { return dataFinalizacao; }
    public void setDataFinalizacao(LocalDate dataFinalizacao) { this.dataFinalizacao = dataFinalizacao; }

    public Integer getOrdem() { return ordem; }
    public void setOrdem(Integer ordem) { this.ordem = ordem; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
