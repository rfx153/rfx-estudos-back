package rfx_estudos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rfx_estudos.domain.Registro;
import rfx_estudos.repository.RegistroRepository;

import java.util.List;

@RestController
@RequestMapping("/api/registros")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroController {

    @Autowired
    private RegistroRepository registroRepository;

    // Rota: http://localhost:8080/api/registros (Listar histórico completo ordenado por data)
    @GetMapping
    public List<Registro> listarHistorico() {
        return registroRepository.findAllByOrderByDataEstudoDesc();
    }

    // Rota: http://localhost:8080/api/registros (Salvar uma nova sessão realizada)
    @PostMapping
    public Registro salvarSessao(@RequestBody Registro novoRegistro) {
        return registroRepository.save(novoRegistro);
    }
}
