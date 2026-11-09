package rfx_estudos.controller;

import java.net.URI;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import rfx_estudos.domain.Ciclo;
import rfx_estudos.domain.CicloMateria;
import rfx_estudos.dto.CicloMateriaDTO;
import rfx_estudos.dto.CicloReordenacaoDTO;
import rfx_estudos.service.CicloConflitoException;
import rfx_estudos.service.CicloService;

@RestController
@RequestMapping("/api/ciclos")
@CrossOrigin(origins = "${app.frontend.url}")
public class CicloController {

    private final CicloService cicloService;

    public CicloController(CicloService cicloService) {
        this.cicloService = cicloService;
    }

    @GetMapping
    public List<Ciclo> listarTodos() {
        return cicloService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ciclo> buscarPorId(@PathVariable Long id) {
        return cicloService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ciclo> criar(@RequestBody Ciclo dados) {
        if (cicloInvalido(dados)) {
            return ResponseEntity.badRequest().build();
        }
        Ciclo criado = cicloService.criar(dados);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();
        return ResponseEntity.created(localizacao).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ciclo> atualizar(@PathVariable Long id, @RequestBody Ciclo dados) {
        if (cicloInvalido(dados)) {
            return ResponseEntity.badRequest().build();
        }
        return cicloService.atualizar(id, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return cicloService.excluir(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{cicloId}/materias")
    public ResponseEntity<List<CicloMateria>> listarMaterias(@PathVariable Long cicloId) {
        return cicloService.listarMaterias(cicloId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{cicloId}/materias")
    public ResponseEntity<CicloMateria> adicionarMateria(
            @PathVariable Long cicloId,
            @RequestBody CicloMateriaDTO dados) {
        if (materiaInvalida(dados)) {
            return ResponseEntity.badRequest().build();
        }
        return cicloService.adicionarMateria(cicloId, dados)
                .map(item -> {
                    URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(item.getId())
                            .toUri();
                    return ResponseEntity.created(localizacao).body(item);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{cicloId}/materias/ordem")
    public ResponseEntity<List<CicloMateria>> reordenar(
            @PathVariable Long cicloId,
            @RequestBody CicloReordenacaoDTO dados) {
        try {
            return cicloService.reordenar(cicloId, dados)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{cicloId}/materias/{id}")
    public ResponseEntity<CicloMateria> atualizarMateria(
            @PathVariable Long cicloId,
            @PathVariable Long id,
            @RequestBody CicloMateriaDTO dados) {
        if (materiaInvalida(dados)) {
            return ResponseEntity.badRequest().build();
        }
        return cicloService.atualizarMateria(cicloId, id, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cicloId}/materias/{id}")
    public ResponseEntity<Void> excluirMateria(
            @PathVariable Long cicloId,
            @PathVariable Long id) {
        return cicloService.excluirMateria(cicloId, id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @ExceptionHandler({CicloConflitoException.class, DataIntegrityViolationException.class})
    public ResponseEntity<Void> tratarConflito() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    private boolean cicloInvalido(Ciclo ciclo) {
        return ciclo == null || ciclo.getNome() == null || ciclo.getNome().isBlank()
                || ciclo.getNome().trim().length() > 150;
    }

    private boolean materiaInvalida(CicloMateriaDTO dados) {
        return dados == null || dados.getMateriaId() == null
                || dados.getOrdem() == null || dados.getOrdem() < 1;
    }
}
