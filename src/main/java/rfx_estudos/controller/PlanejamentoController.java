package rfx_estudos.controller;

import java.net.URI;
import java.util.List;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import rfx_estudos.domain.Planejamento;
import rfx_estudos.service.PlanejamentoService;

@RestController
@RequestMapping("/api/planejamentos")
@CrossOrigin(origins = "${app.frontend.url}")
public class PlanejamentoController {

    private final PlanejamentoService planejamentoService;

    public PlanejamentoController(PlanejamentoService planejamentoService) {
        this.planejamentoService = planejamentoService;
    }

    @GetMapping
    public List<Planejamento> listarTodos() {
        return planejamentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planejamento> buscarPorId(@PathVariable Long id) {
        return planejamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Planejamento> criar(@RequestBody Planejamento planejamento) {
        Planejamento criado = planejamentoService.criar(planejamento);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();

        return ResponseEntity.created(localizacao).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Planejamento> atualizar(@PathVariable Long id, @RequestBody Planejamento dados) {
        return planejamentoService.atualizar(id, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!planejamentoService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
