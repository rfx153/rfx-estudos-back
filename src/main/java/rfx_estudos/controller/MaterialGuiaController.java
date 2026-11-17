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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import rfx_estudos.domain.MaterialGuia;
import rfx_estudos.repository.MaterialGuiaRepository;

@RestController
@RequestMapping("/api/materiais-guia")
@CrossOrigin(origins = "${app.frontend.url}")
public class MaterialGuiaController {

    private final MaterialGuiaRepository materialGuiaRepository;

    public MaterialGuiaController(MaterialGuiaRepository materialGuiaRepository) {
        this.materialGuiaRepository = materialGuiaRepository;
    }

    @GetMapping
    public List<MaterialGuia> listarTodos(
            @RequestParam(defaultValue = "false") boolean incluirInativos) {
        return incluirInativos
                ? materialGuiaRepository.findAllByOrderByNomeAsc()
                : materialGuiaRepository.findByAtivoTrueOrderByNomeAsc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialGuia> buscarPorId(@PathVariable Long id) {
        return materialGuiaRepository.findByIdAndAtivoTrue(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MaterialGuia> criar(@RequestBody MaterialGuia materialGuia) {
        materialGuia.setId(null);
        materialGuia.setAtivo(true);
        MaterialGuia criado = materialGuiaRepository.save(materialGuia);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();
        return ResponseEntity.created(localizacao).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialGuia> atualizar(
            @PathVariable Long id,
            @RequestBody MaterialGuia dados) {
        return materialGuiaRepository.findByIdAndAtivoTrue(id)
                .map(materialGuia -> {
                    materialGuia.setNome(dados.getNome());
                    return ResponseEntity.ok(materialGuiaRepository.save(materialGuia));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return materialGuiaRepository.findByIdAndAtivoTrue(id)
                .map(materialGuia -> {
                    materialGuia.setAtivo(false);
                    materialGuiaRepository.save(materialGuia);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
