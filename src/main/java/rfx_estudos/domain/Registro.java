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
    @JoinColumn(name = "material_guia_fk")
    private MaterialGuia materialGuia;

    @ManyToOne
    @JoinColumn(name = "planejamento_fk")
    private Planejamento planejamento;

    @ManyToOne
    @JoinColumn(name = "tipo_registro_fk")
    private TipoRegistro tipoRegistro;

    @Column(nullable = false)
    private Boolean concluido = false;

    @ManyToOne
    @JoinColumn(name = "planejamento_etapa_fk")
    private PlanejamentoItemEtapa planejamentoEtapa;

    @Column(name = "questoes_feitas")
    private Integer questoesFeitas = 0;

    @Column(name = "questoes_acertadas")
    private Integer questoesAcertadas = 0;

    // Campos de Controle da Sessão
    @Column(name = "data_estudo", nullable = false)
    private LocalDate dataEstudo = LocalDate.now();

    @Column(name = "tempo_estudado", nullable = false)
    private LocalTime tempoEstudado;

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

    public MaterialGuia getMaterialGuia() { return materialGuia; }
    public void setMaterialGuia(MaterialGuia materialGuia) { this.materialGuia = materialGuia; }

    public Planejamento getPlanejamento() { return planejamento; }
    public void setPlanejamento(Planejamento planejamento) { this.planejamento = planejamento; }

    public TipoRegistro getTipoRegistro() { return tipoRegistro; }
    public void setTipoRegistro(TipoRegistro tipoRegistro) { this.tipoRegistro = tipoRegistro; }

    public Boolean getConcluido() { return concluido; }
    public void setConcluido(Boolean concluido) { this.concluido = concluido; }

    public PlanejamentoItemEtapa getPlanejamentoEtapa() { return planejamentoEtapa; }
    public void setPlanejamentoEtapa(PlanejamentoItemEtapa planejamentoEtapa) {
        this.planejamentoEtapa = planejamentoEtapa;
    }

    public Integer getQuestoesFeitas() { return questoesFeitas; }
    public void setQuestoesFeitas(Integer questoesFeitas) { this.questoesFeitas = questoesFeitas; }

    public Integer getQuestoesAcertadas() { return questoesAcertadas; }
    public void setQuestoesAcertadas(Integer questoesAcertadas) { this.questoesAcertadas = questoesAcertadas; }

    public LocalDate getDataEstudo() { return dataEstudo; }
    public void setDataEstudo(LocalDate dataEstudo) { this.dataEstudo = dataEstudo; }

    public LocalTime getTempoEstudado() { return tempoEstudado; }
    public void setTempoEstudado(LocalTime tempoEstudado) { this.tempoEstudado = tempoEstudado; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
