package rfx_estudos.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "materias")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToMany
    @JoinTable(
        name = "materia_categorias",
        joinColumns = @JoinColumn(name = "materia_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private java.util.Set<Categoria> categorias = new java.util.HashSet<>();

    // Construtor Padrão (Exigido pelo JPA)
    public Materia() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public java.util.Set<Categoria> getCategorias() { return categorias; }
    public void setCategorias(java.util.Set<Categoria> categorias) { this.categorias = categorias; }
}
