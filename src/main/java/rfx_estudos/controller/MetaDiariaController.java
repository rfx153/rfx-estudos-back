package rfx_estudos.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rfx_estudos.domain.MetaDiaria;
import rfx_estudos.dto.MetaDiariaDTO;
import rfx_estudos.dto.MetaDiariaResumoDTO;
import rfx_estudos.service.MetaDiariaService;

@RestController
@RequestMapping("/api/metas-diarias")
@CrossOrigin(origins = "${app.frontend.url}")
public class MetaDiariaController {

    private final MetaDiariaService metaDiariaService;

    public MetaDiariaController(MetaDiariaService metaDiariaService) {
        this.metaDiariaService = metaDiariaService;
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<MetaDiariaResumoDTO>> resumir(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        try {
            return ResponseEntity.ok(metaDiariaService.resumir(inicio, fim));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<MetaDiaria> criar(@RequestBody MetaDiariaDTO dados) {
        return metaDiariaService.criar(dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaDiaria> atualizar(
            @PathVariable Long id,
            @RequestBody MetaDiariaDTO dados) {
        return metaDiariaService.atualizar(id, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return metaDiariaService.excluir(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
