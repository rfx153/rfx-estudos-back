package rfx_estudos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rfx_estudos.domain.Materia;
import rfx_estudos.repository.MateriaRepository;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
@CrossOrigin(origins = "http://localhost:4200")
public class MateriaController {

    @Autowired
    private MateriaRepository materiaRepository;

    @GetMapping
    public List<Materia> listarTodas() {
        return materiaRepository.findAll();
    }
} 