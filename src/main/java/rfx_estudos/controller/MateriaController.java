package rfx_estudos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rfx_estudos.domain.Materia;
import rfx_estudos.domain.Categoria;
import rfx_estudos.repository.CategoriaRepository;
import rfx_estudos.repository.MateriaRepository;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
@CrossOrigin(origins = "${app.frontend.url}")
public class MateriaController {

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Materia> listarTodas() {
        return materiaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Materia> criar(@RequestBody Materia materia) {
        materia.setCategorias(categoriasExistentes(materia));
        Materia novaMateria = materiaRepository.save(materia);
        return ResponseEntity.ok(novaMateria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Materia> atualizar(@PathVariable Long id, @RequestBody Materia dados) {
        return materiaRepository.findById(id)
                .map(materia -> {
                    materia.setNome(dados.getNome());
                    materia.setCategorias(categoriasExistentes(dados));
                    return ResponseEntity.ok(materiaRepository.save(materia));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return materiaRepository.findById(id)
                .map(materia -> {
                    materia.getCategorias().clear();
                    materiaRepository.delete(materia);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private java.util.Set<Categoria> categoriasExistentes(Materia materia) {
        if (materia.getCategorias() == null) return new java.util.HashSet<>();
        return materia.getCategorias().stream()
                .map(categoria -> categoriaRepository.findById(categoria.getId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Categoria não encontrada: " + categoria.getId())))
                .collect(java.util.stream.Collectors.toSet());
    }
}
