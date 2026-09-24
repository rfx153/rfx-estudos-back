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
import rfx_estudos.domain.Planejamento;
import rfx_estudos.repository.PlanejamentoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/planejamentos")
@CrossOrigin(origins = "${app.frontend.url}")
public class PlanejamentoController {

    @Autowired
    private PlanejamentoRepository planejamentoRepository;

    @GetMapping
    public List<Planejamento> listarTodos() {
        return planejamentoRepository.findAllByOrderByNomeAsc();
    }

    @PostMapping
    public ResponseEntity<Planejamento> criar(@RequestBody Planejamento planejamento) {
        Planejamento novoPlanejamento = planejamentoRepository.save(planejamento);
        return ResponseEntity.ok(novoPlanejamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Planejamento> atualizar(@PathVariable Long id, @RequestBody Planejamento dados) {
        return planejamentoRepository.findById(id)
                .map(planejamento -> {
                    planejamento.setNome(dados.getNome());
                    return ResponseEntity.ok(planejamentoRepository.save(planejamento));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!planejamentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        planejamentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
