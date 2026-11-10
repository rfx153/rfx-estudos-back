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

import rfx_estudos.domain.MetaSemanal;
import rfx_estudos.dto.MetaSemanalDTO;
import rfx_estudos.dto.MetaSemanalResumoDTO;
import rfx_estudos.service.MetaSemanalService;

@RestController
@RequestMapping("/api/metas-semanais")
@CrossOrigin(origins = "${app.frontend.url}")
public class MetaSemanalController {

    private final MetaSemanalService metaSemanalService;

    public MetaSemanalController(MetaSemanalService metaSemanalService) {
        this.metaSemanalService = metaSemanalService;
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<MetaSemanalResumoDTO>> resumir(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicioSemana) {
        try {
            return ResponseEntity.ok(metaSemanalService.resumir(inicioSemana));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<MetaSemanal> criar(@RequestBody MetaSemanalDTO dados) {
        return metaSemanalService.criar(dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaSemanal> atualizar(
            @PathVariable Long id,
            @RequestBody MetaSemanalDTO dados) {
        return metaSemanalService.atualizar(id, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return metaSemanalService.excluir(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
