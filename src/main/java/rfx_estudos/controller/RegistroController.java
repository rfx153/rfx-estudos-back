package rfx_estudos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rfx_estudos.domain.Registro;
import rfx_estudos.repository.RegistroRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/registros")
@CrossOrigin(origins = "${app.frontend.url}")
public class RegistroController {

    @Autowired
    private RegistroRepository registroRepository;

    // Rota: http://localhost:8080/api/registros (Listar histórico completo ordenado por data)
    @GetMapping
    public List<Registro> listarHistorico() {
        return registroRepository.findAllByOrderByDataEstudoDesc();
    }

    @GetMapping("/hoje")
    public List<Registro> listarRegistrosDeHoje() {
        return registroRepository.findByDataEstudoOrderByDataEstudoDesc(LocalDate.now());
    }

    @GetMapping("/total")
    public long contarRegistros() {
        return registroRepository.count();
    }

    @GetMapping("/recentes")
    public List<Registro> listarRecentes(
            @RequestParam(defaultValue = "100") int limite,
            @RequestParam(required = false) Long materiaId
    ) {
        int limiteSeguro = Math.min(Math.max(limite, 1), 200);
        PageRequest pagina = PageRequest.of(0, limiteSeguro);

        if (materiaId != null) {
            return registroRepository.findByMateriaIdOrderByDataEstudoDesc(materiaId, pagina);
        }

        return registroRepository.findByOrderByDataEstudoDesc(pagina);
    }

    // Rota: http://localhost:8080/api/registros (Salvar uma nova sessão realizada)
    @PostMapping
    public Registro salvarSessao(@RequestBody Registro novoRegistro) {
        return registroRepository.save(novoRegistro);
    }
}
