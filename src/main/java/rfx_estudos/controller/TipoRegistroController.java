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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rfx_estudos.domain.TipoRegistro;
import rfx_estudos.repository.TipoRegistroRepository;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-registro")
@CrossOrigin(origins = "${app.frontend.url}")
public class TipoRegistroController {

    @Autowired
    private TipoRegistroRepository tipoRegistroRepository;

    @GetMapping
    public List<TipoRegistro> listarTodos(
            @RequestParam(defaultValue = "false") boolean incluirInativos) {
        return incluirInativos
                ? tipoRegistroRepository.findAllByOrderByNomeAsc()
                : tipoRegistroRepository.findByAtivoTrueOrderByNomeAsc();
    }

    @PostMapping
    public ResponseEntity<TipoRegistro> criar(@RequestBody TipoRegistro tipoRegistro) {
        tipoRegistro.setId(null);
        tipoRegistro.setAtivo(true);
        return ResponseEntity.ok(tipoRegistroRepository.save(tipoRegistro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoRegistro> atualizar(@PathVariable Long id, @RequestBody TipoRegistro dados) {
        return tipoRegistroRepository.findByIdAndAtivoTrue(id)
                .map(tipoRegistro -> {
                    tipoRegistro.setNome(dados.getNome());
                    return ResponseEntity.ok(tipoRegistroRepository.save(tipoRegistro));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return tipoRegistroRepository.findByIdAndAtivoTrue(id)
                .map(tipoRegistro -> {
                    tipoRegistro.setAtivo(false);
                    tipoRegistroRepository.save(tipoRegistro);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
