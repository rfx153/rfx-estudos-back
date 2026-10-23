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
import rfx_estudos.domain.PlanejamentoMateria;
import rfx_estudos.dto.PlanejamentoMateriaDTO;
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

    @GetMapping("/{planejamentoId}/materias")
    public ResponseEntity<List<PlanejamentoMateria>> listarMaterias(
            @PathVariable Long planejamentoId) {
        return planejamentoService.listarMaterias(planejamentoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{planejamentoId}/materias/{id}")
    public ResponseEntity<PlanejamentoMateria> buscarMateria(
            @PathVariable Long planejamentoId,
            @PathVariable Long id) {
        return planejamentoService.buscarMateria(planejamentoId, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{planejamentoId}/materias")
    public ResponseEntity<PlanejamentoMateria> adicionarMateria(
            @PathVariable Long planejamentoId,
            @RequestBody PlanejamentoMateriaDTO dados) {
        if (dadosMateriaInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.adicionarMateria(planejamentoId, dados)
                .map(planejamentoMateria -> {
                    URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(planejamentoMateria.getId())
                            .toUri();
                    return ResponseEntity.created(localizacao).body(planejamentoMateria);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{planejamentoId}/materias/{id}")
    public ResponseEntity<PlanejamentoMateria> atualizarMateria(
            @PathVariable Long planejamentoId,
            @PathVariable Long id,
            @RequestBody PlanejamentoMateriaDTO dados) {
        if (dadosMateriaInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.atualizarMateria(planejamentoId, id, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{planejamentoId}/materias/{id}")
    public ResponseEntity<Void> excluirMateria(
            @PathVariable Long planejamentoId,
            @PathVariable Long id) {
        if (!planejamentoService.excluirMateria(planejamentoId, id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private boolean dadosMateriaInvalidos(PlanejamentoMateriaDTO dados) {
        if (dados.getMateriaId() == null) {
            return true;
        }

        if (dados.getPeso() != null && (dados.getPeso() < 1 || dados.getPeso() > 5)) {
            return true;
        }

        return dados.getOrdem() != null && dados.getOrdem() <= 0;
    }
}
