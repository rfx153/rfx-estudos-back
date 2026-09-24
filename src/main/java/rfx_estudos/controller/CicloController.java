package rfx_estudos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rfx_estudos.domain.Ciclo;
import rfx_estudos.repository.CicloRepository;

import java.util.List;

@RestController
@RequestMapping("/api/ciclos")
@CrossOrigin(origins = "${app.frontend.url}")
public class CicloController {

    @Autowired
    private CicloRepository cicloRepository;

    @GetMapping
    public List<Ciclo> listarTodos() {
        return cicloRepository.findAllByOrderByNomeAsc();
    }

    @PostMapping
    public ResponseEntity<Ciclo> criar(@RequestBody Ciclo ciclo) {
        return ResponseEntity.ok(cicloRepository.save(ciclo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ciclo> atualizar(@PathVariable Long id, @RequestBody Ciclo dados) {
        return cicloRepository.findById(id)
                .map(ciclo -> {
                    ciclo.setNome(dados.getNome());
                    return ResponseEntity.ok(cicloRepository.save(ciclo));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!cicloRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        cicloRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
