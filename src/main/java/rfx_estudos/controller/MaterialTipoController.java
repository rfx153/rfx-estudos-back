package rfx_estudos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rfx_estudos.domain.MaterialTipo;
import rfx_estudos.repository.MaterialTipoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/material-tipos")
@CrossOrigin(origins = "${app.frontend.url}")
public class MaterialTipoController {

    @Autowired
    private MaterialTipoRepository materialTipoRepository;

    // Rota: http://localhost:8080/api/material-tipos (Listar todos)
    @GetMapping
    public List<MaterialTipo> listarTodos() {
        return materialTipoRepository.findAll();
    }

    // Rota: http://localhost:8080/api/material-tipos (Criar um novo formato se precisar)
    @PostMapping
    public MaterialTipo criar(@RequestBody MaterialTipo novoTipo) {
        return materialTipoRepository.save(novoTipo);
    }
}
