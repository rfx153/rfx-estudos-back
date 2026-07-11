package rfx_estudos.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "material_tipos")
public class MaterialTipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    // Construtor Padrão
    public MaterialTipo() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
