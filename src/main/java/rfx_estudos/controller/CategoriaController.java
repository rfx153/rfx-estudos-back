package rfx_estudos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rfx_estudos.domain.Categoria;
import rfx_estudos.repository.CategoriaRepository;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "${app.frontend.url}")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @PostMapping
    public Categoria criar(@RequestBody Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(@PathVariable Long id, @RequestBody Categoria dados) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setNome(dados.getNome());
                    return ResponseEntity.ok(categoriaRepository.save(categoria));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
