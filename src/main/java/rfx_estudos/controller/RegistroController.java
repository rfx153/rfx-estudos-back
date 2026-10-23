package rfx_estudos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import rfx_estudos.domain.Registro;
import rfx_estudos.repository.RegistroRepository;
import rfx_estudos.service.RegistroService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/registros")
@CrossOrigin(origins = "${app.frontend.url}")
public class RegistroController {

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private RegistroService registroService;

    // Rota: http://localhost:8080/api/registros (Listar histórico completo ordenado por data)
    @GetMapping
    public List<Registro> listarHistorico() {
        return registroRepository.findAllByOrderByDataEstudoDesc();
    }

    @GetMapping("/hoje")
    public List<Registro> listarRegistrosDeHoje() {
         LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));

    return registroRepository
        .findByDataEstudoOrderByDataEstudoDesc(hoje);
    }

    @GetMapping("/total")
    public long contarRegistros() {
        return registroRepository.count();
    }

    @GetMapping("/recentes")
    public Page<Registro> listarRecentes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "100") int limite,
            @RequestParam(required = false) Long materiaId
    ) {
        int tamanhoSolicitado = size != null ? size : limite;
        int tamanhoSeguro = Math.min(Math.max(tamanhoSolicitado, 1), 50);
        int paginaSegura = Math.max(page, 0);
        PageRequest pagina = PageRequest.of(
                paginaSegura,
                tamanhoSeguro,
                Sort.by(Sort.Direction.DESC, "dataEstudo").and(Sort.by(Sort.Direction.DESC, "id"))
        );

        if (materiaId != null) {
            return registroRepository.buscarHistoricoPorMateria(materiaId, pagina);
        }

        return registroRepository.buscarHistorico(pagina);
    }

    // Rota: http://localhost:8080/api/registros (Salvar uma nova sessão realizada)
    @PostMapping
    public ResponseEntity<Registro> salvarSessao(@RequestBody Registro novoRegistro) {
        return registroService.criar(novoRegistro)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Registro> atualizar(@PathVariable Long id, @RequestBody Registro registroAtualizado) {
        return registroService.atualizar(id, registroAtualizado)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!registroService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
