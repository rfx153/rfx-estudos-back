package rfx_estudos.dto;

public class AssuntoNovoDTO {
    private String nome;
    private Long materiaId;

    // Construtores
    public AssuntoNovoDTO() {}

    public AssuntoNovoDTO(String nome, Long materiaId) {
        this.nome = nome;
        this.materiaId = materiaId;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Long getMateriaId() { return materiaId; }
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }
}