package rfx_estudos.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "registros")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Conexões com as tabelas de suporte
    @ManyToOne
    @JoinColumn(name = "materia_fk", nullable = false)
    private Materia materia;

    @ManyToOne
    @JoinColumn(name = "assunto_fk", nullable = false)
    private Assunto assunto;

    @ManyToOne
    @JoinColumn(name = "material_tipo_fk", nullable = false)
    private MaterialTipo materialTipo;

    @Column(name = "material_nome", length = 255)
    private String materialNome;

    @Column(name = "ponto_parada", length = 255)
    private String puntoParada;

    @Column(name = "questoes_feitas")
    private Integer questoesFeitas = 0;

    @Column(name = "questoes_acertadas")
    private Integer questoesAcertadas = 0;

    // Campos focados em Revisão
    @ManyToOne
    @JoinColumn(name = "revisao_fk_assunto")
    private Assunto revisaoAssunto;

    @Column(name = "revisao_complemento", columnDefinition = "TEXT")
    private String revisaoComplemento;

    // Campos de Controle da Sessão
    @Column(name = "data_estudo", nullable = false)
    private LocalDate dataEstudo = LocalDate.now();

    @Column(name = "tempo_estudado", nullable = false)
    private LocalTime tempoEstudado;

    @Column(name = "link_documento", columnDefinition = "TEXT")
    private String linkDocumento;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Construtor Padrão
    public Registro() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public Assunto getAssunto() { return assunto; }
    public void setAssunto(Assunto assunto) { this.assunto = assunto; }

    public MaterialTipo getMaterialTipo() { return materialTipo; }
    public void setMaterialTipo(MaterialTipo materialTipo) { this.materialTipo = materialTipo; }

    public String getMaterialNome() { return materialNome; }
    public void setMaterialNome(String materialNome) { this.materialNome = materialNome; }

    public String getPuntoParada() { return puntoParada; }
    public void setPuntoParada(String puntoParada) { this.puntoParada = puntoParada; }

    public Integer getQuestoesFeitas() { return questoesFeitas; }
    public void setQuestoesFeitas(Integer questoesFeitas) { this.questoesFeitas = questoesFeitas; }

    public Integer getQuestoesAcertadas() { return questoesAcertadas; }
    public void setQuestoesAcertadas(Integer questoesAcertadas) { this.questoesAcertadas = questoesAcertadas; }

    public Assunto getRevisaoAssunto() { return revisaoAssunto; }
    public void setRevisaoAssunto(Assunto revisaoAssunto) { this.revisaoAssunto = revisaoAssunto; }

    public String getRevisaoComplemento() { return revisaoComplemento; }
    public void setRevisaoComplemento(String revisaoComplemento) { this.revisaoComplemento = revisaoComplemento; }

    public LocalDate getDataEstudo() { return dataEstudo; }
    public void setDataEstudo(LocalDate dataEstudo) { this.dataEstudo = dataEstudo; }

    public LocalTime getTempoEstudado() { return tempoEstudado; }
    public void setTempoEstudado(LocalTime tempoEstudado) { this.tempoEstudado = tempoEstudado; }

    public String getLinkDocumento() { return linkDocumento; }
    public void setLinkDocumento(String linkDocumento) { this.linkDocumento = linkDocumento; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
