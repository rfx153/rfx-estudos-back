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

import rfx_estudos.domain.MetaMateria;
import rfx_estudos.dto.MetaMateriaDTO;
import rfx_estudos.dto.MetaMateriaResumoDTO;
import rfx_estudos.service.MetaMateriaService;

@RestController
@RequestMapping("/api/metas-materias")
@CrossOrigin(origins = "${app.frontend.url}")
public class MetaMateriaController {

    private final MetaMateriaService metaMateriaService;

    public MetaMateriaController(MetaMateriaService metaMateriaService) {
        this.metaMateriaService = metaMateriaService;
    }

    @GetMapping
    public List<MetaMateria> listarTodas() {
        return metaMateriaService.listarTodas();
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<MetaMateriaResumoDTO>> resumir(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(defaultValue = "SEMANAL") PeriodoMeta periodo) {
        try {
            return ResponseEntity.ok(metaMateriaService.resumir(
                    inicio,
                    fim,
                    periodo == PeriodoMeta.MENSAL));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<MetaMateria> criar(@RequestBody MetaMateriaDTO dados) {
        return metaMateriaService.criar(dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaMateria> atualizar(
            @PathVariable Long id,
            @RequestBody MetaMateriaDTO dados) {
        return metaMateriaService.atualizar(id, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return metaMateriaService.excluir(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    public enum PeriodoMeta {
        SEMANAL,
        MENSAL
    }
}
