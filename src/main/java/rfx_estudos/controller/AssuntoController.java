package rfx_estudos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rfx_estudos.domain.Assunto;
import rfx_estudos.dto.AssuntoNovoDTO;
import rfx_estudos.repository.AssuntoRepository;
import rfx_estudos.service.AssuntoService;

@RestController
@RequestMapping("/api/assuntos")
@CrossOrigin(origins = "http://localhost:4200")
public class AssuntoController {

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private AssuntoService assuntoService;

    // Rota: http://localhost:8080/api/assuntos (Traz tudo)
    @GetMapping
    public List<Assunto> listarTodos() {
        return assuntoRepository.findAll();
    }

    // Rota: http://localhost:8080/api/assuntos/materia/1 (Traz só os assuntos da matéria com ID 1)
    @GetMapping("/materia/{materiaId}")
    public List<Assunto> listarPorMateria(@PathVariable Long materiaId) {
        return assuntoRepository.findByMateriaId(materiaId);
    }

    @PostMapping
    public ResponseEntity<Assunto> criar(@RequestBody AssuntoNovoDTO dto) {
        // Busca a matéria pelo ID vindo do DTO, associa ao novo Assunto e salva
        Assunto assuntoSalvo = assuntoService.salvarComMateria(dto);
        return ResponseEntity.ok(assuntoSalvo);
}
}