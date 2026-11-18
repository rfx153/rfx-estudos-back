package rfx_estudos.controller;

import java.util.List;

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

import rfx_estudos.domain.Assunto;
import rfx_estudos.dto.AssuntoNovoDTO;
import rfx_estudos.repository.AssuntoRepository;
import rfx_estudos.service.AssuntoService;

@RestController
@RequestMapping("/api/assuntos")
@CrossOrigin(origins = "${app.frontend.url}")
public class AssuntoController {

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private AssuntoService assuntoService;

    // Rota: http://localhost:8080/api/assuntos (Traz tudo)
    @GetMapping
    public List<Assunto> listarTodos(
            @RequestParam(defaultValue = "false") boolean incluirInativos) {
        return incluirInativos
                ? assuntoRepository.findAllByOrderByNomeAsc()
                : assuntoRepository.findByAtivoTrueAndMateriaAtivoTrueOrderByNomeAsc();
    }

    // Rota: http://localhost:8080/api/assuntos/materia/1 (Traz só os assuntos da matéria com ID 1)
    @GetMapping("/materia/{materiaId}")
    public List<Assunto> listarPorMateria(@PathVariable Long materiaId) {
        return assuntoRepository.findByMateriaIdAndAtivoTrueAndMateriaAtivoTrueOrderByNomeAsc(materiaId);
    }

    @PostMapping
    public ResponseEntity<Assunto> criar(@RequestBody AssuntoNovoDTO dto) {
        String nome = normalizarNome(dto.getNome());
        if (nome == null || dto.getMateriaId() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (assuntoRepository.contarPorMateriaENomeNormalizado(
                dto.getMateriaId(), nome, null) > 0) {
            return ResponseEntity.status(409).build();
        }

        dto.setNome(nome);
        // Busca a matéria pelo ID vindo do DTO, associa ao novo Assunto e salva
        Assunto assuntoSalvo = assuntoService.salvarComMateria(dto);
        return ResponseEntity.ok(assuntoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assunto> atualizar(@PathVariable Long id, @RequestBody Assunto dados) {
        return assuntoRepository.findByIdAndAtivoTrue(id)
                .map(assunto -> {
                    String nome = normalizarNome(dados.getNome());
                    if (nome == null) {
                        return ResponseEntity.badRequest().<Assunto>build();
                    }
                    if (assuntoRepository.contarPorMateriaENomeNormalizado(
                            assunto.getMateria().getId(), nome, id) > 0) {
                        return ResponseEntity.status(409).<Assunto>build();
                    }

                    assunto.setNome(nome);
                    return ResponseEntity.ok(assuntoRepository.save(assunto));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private String normalizarNome(String nome) {
        if (nome == null || nome.isBlank()) return null;
        return nome.trim();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return assuntoRepository.findByIdAndAtivoTrue(id)
                .map(assunto -> {
                    assunto.setAtivo(false);
                    assuntoRepository.save(assunto);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
   
}
