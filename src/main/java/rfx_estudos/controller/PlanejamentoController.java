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
import org.springframework.web.bind.annotation.RestController;
import rfx_estudos.domain.Ciclo;
import rfx_estudos.domain.Planejamento;
import rfx_estudos.domain.PlanejamentoCiclo;
import rfx_estudos.domain.PlanejamentoItem;
import rfx_estudos.repository.CicloRepository;
import rfx_estudos.repository.PlanejamentoCicloRepository;
import rfx_estudos.repository.PlanejamentoItemRepository;
import rfx_estudos.repository.PlanejamentoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/planejamentos")
@CrossOrigin(origins = "${app.frontend.url}")
public class PlanejamentoController {

    @Autowired
    private PlanejamentoRepository planejamentoRepository;

    @Autowired
    private PlanejamentoItemRepository planejamentoItemRepository;

    @Autowired
    private PlanejamentoCicloRepository planejamentoCicloRepository;

    @Autowired
    private CicloRepository cicloRepository;

    @GetMapping
    public List<Planejamento> listarTodos() {
        return planejamentoRepository.findAllByOrderByNomeAsc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planejamento> buscarPorId(@PathVariable Long id) {
        return planejamentoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Planejamento> criar(@RequestBody Planejamento planejamento) {
        Planejamento novoPlanejamento = planejamentoRepository.save(planejamento);
        return ResponseEntity.ok(novoPlanejamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Planejamento> atualizar(@PathVariable Long id, @RequestBody Planejamento dados) {
        return planejamentoRepository.findById(id)
                .map(planejamento -> {
                    planejamento.setNome(dados.getNome());
                    planejamento.setDescricao(dados.getDescricao());
                    planejamento.setDataInicio(dados.getDataInicio());
                    planejamento.setDataPrevista(dados.getDataPrevista());
                    planejamento.setStatus(dados.getStatus());
                    planejamento.setDataFinalizacao(dados.getDataFinalizacao());
                    return ResponseEntity.ok(planejamentoRepository.save(planejamento));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!planejamentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        planejamentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<PlanejamentoItem>> listarItens(@PathVariable Long id) {
        if (!planejamentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(planejamentoItemRepository.findByPlanejamentoIdOrderByOrdemAscIdAsc(id));
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<PlanejamentoItem> criarItem(@PathVariable Long id, @RequestBody PlanejamentoItem item) {
        return planejamentoRepository.findById(id)
                .map(planejamento -> {
                    item.setId(null);
                    item.setPlanejamento(planejamento);
                    return ResponseEntity.ok(planejamentoItemRepository.save(item));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/itens/{itemId}")
    public ResponseEntity<PlanejamentoItem> atualizarItem(@PathVariable Long itemId, @RequestBody PlanejamentoItem dados) {
        return planejamentoItemRepository.findById(itemId)
                .map(item -> {
                    item.setMateria(dados.getMateria());
                    item.setAssunto(dados.getAssunto());
                    item.setMaterialTipo(dados.getMaterialTipo());
                    item.setMaterialNome(dados.getMaterialNome());
                    item.setPrioridade(dados.getPrioridade());
                    item.setMeta(dados.getMeta());
                    item.setDataPrevista(dados.getDataPrevista());
                    item.setLinkDocumento(dados.getLinkDocumento());
                    item.setStatus(dados.getStatus());
                    item.setDataFinalizacao(dados.getDataFinalizacao());
                    item.setOrdem(dados.getOrdem());
                    item.setObservacoes(dados.getObservacoes());
                    return ResponseEntity.ok(planejamentoItemRepository.save(item));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/itens/{itemId}")
    public ResponseEntity<Void> excluirItem(@PathVariable Long itemId) {
        if (!planejamentoItemRepository.existsById(itemId)) {
            return ResponseEntity.notFound().build();
        }

        planejamentoItemRepository.deleteById(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/ciclos")
    public ResponseEntity<List<PlanejamentoCiclo>> listarCiclos(@PathVariable Long id) {
        if (!planejamentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(planejamentoCicloRepository.findByPlanejamentoIdOrderByCicloNomeAsc(id));
    }

    @PostMapping("/{id}/ciclos/{cicloId}")
    public ResponseEntity<PlanejamentoCiclo> vincularCiclo(@PathVariable Long id, @PathVariable Long cicloId) {
        return planejamentoRepository.findById(id)
                .flatMap(planejamento -> cicloRepository.findById(cicloId)
                        .map(ciclo -> criarOuBuscarVinculo(planejamento, ciclo)))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/ciclos/{cicloId}")
    public ResponseEntity<Void> desvincularCiclo(@PathVariable Long id, @PathVariable Long cicloId) {
        return planejamentoCicloRepository.findByPlanejamentoIdAndCicloId(id, cicloId)
                .map(vinculo -> {
                    planejamentoCicloRepository.delete(vinculo);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private PlanejamentoCiclo criarOuBuscarVinculo(Planejamento planejamento, Ciclo ciclo) {
        return planejamentoCicloRepository.findByPlanejamentoIdAndCicloId(planejamento.getId(), ciclo.getId())
                .orElseGet(() -> {
                    PlanejamentoCiclo vinculo = new PlanejamentoCiclo();
                    vinculo.setPlanejamento(planejamento);
                    vinculo.setCiclo(ciclo);
                    return planejamentoCicloRepository.save(vinculo);
                });
    }
}
