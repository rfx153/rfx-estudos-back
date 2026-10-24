package rfx_estudos.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import rfx_estudos.domain.Planejamento;
import rfx_estudos.domain.PlanejamentoGrupoAssunto;
import rfx_estudos.domain.PlanejamentoGrupoAssuntoItem;
import rfx_estudos.domain.PlanejamentoGrupoAssuntoTipo;
import rfx_estudos.domain.PlanejamentoGrupoMateria;
import rfx_estudos.domain.PlanejamentoItemEtapa;
import rfx_estudos.domain.PlanejamentoMateria;
import rfx_estudos.dto.PlanejamentoGrupoAssuntoDTO;
import rfx_estudos.dto.PlanejamentoEtapaPorAssuntoDTO;
import rfx_estudos.dto.PlanejamentoGrupoAssuntoItemDTO;
import rfx_estudos.dto.PlanejamentoGrupoMateriaDTO;
import rfx_estudos.dto.PlanejamentoItemEtapaDTO;
import rfx_estudos.dto.PlanejamentoMateriaDTO;
import rfx_estudos.service.PlanejamentoService;
import rfx_estudos.service.PlanejamentoConflitoException;

@RestController
@RequestMapping("/api/planejamentos")
@CrossOrigin(origins = "${app.frontend.url}")
public class PlanejamentoController {

    private final PlanejamentoService planejamentoService;

    public PlanejamentoController(PlanejamentoService planejamentoService) {
        this.planejamentoService = planejamentoService;
    }

    @GetMapping
    public List<Planejamento> listarTodos() {
        return planejamentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planejamento> buscarPorId(@PathVariable Long id) {
        return planejamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Planejamento> criar(@RequestBody Planejamento planejamento) {
        Planejamento criado = planejamentoService.criar(planejamento);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();

        return ResponseEntity.created(localizacao).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Planejamento> atualizar(@PathVariable Long id, @RequestBody Planejamento dados) {
        return planejamentoService.atualizar(id, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!planejamentoService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{planejamentoId}/grupos-materias")
    public ResponseEntity<List<PlanejamentoGrupoMateria>> listarGruposMaterias(
            @PathVariable Long planejamentoId) {
        return planejamentoService.listarGruposMaterias(planejamentoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{planejamentoId}/grupos-materias/{id}")
    public ResponseEntity<PlanejamentoGrupoMateria> buscarGrupoMateria(
            @PathVariable Long planejamentoId,
            @PathVariable Long id) {
        return planejamentoService.buscarGrupoMateria(planejamentoId, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{planejamentoId}/grupos-materias")
    public ResponseEntity<PlanejamentoGrupoMateria> adicionarGrupoMateria(
            @PathVariable Long planejamentoId,
            @RequestBody PlanejamentoGrupoMateriaDTO dados) {
        if (dadosGrupoMateriaInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.adicionarGrupoMateria(planejamentoId, dados)
                .map(grupo -> {
                    URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(grupo.getId())
                            .toUri();
                    return ResponseEntity.created(localizacao).body(grupo);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{planejamentoId}/grupos-materias/{id}")
    public ResponseEntity<PlanejamentoGrupoMateria> atualizarGrupoMateria(
            @PathVariable Long planejamentoId,
            @PathVariable Long id,
            @RequestBody PlanejamentoGrupoMateriaDTO dados) {
        if (dadosGrupoMateriaInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.atualizarGrupoMateria(planejamentoId, id, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{planejamentoId}/grupos-materias/{id}")
    public ResponseEntity<Void> excluirGrupoMateria(
            @PathVariable Long planejamentoId,
            @PathVariable Long id) {
        if (!planejamentoService.excluirGrupoMateria(planejamentoId, id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{planejamentoId}/materias")
    public ResponseEntity<List<PlanejamentoMateria>> listarMaterias(
            @PathVariable Long planejamentoId) {
        return planejamentoService.listarMaterias(planejamentoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{planejamentoId}/materias/{id}")
    public ResponseEntity<PlanejamentoMateria> buscarMateria(
            @PathVariable Long planejamentoId,
            @PathVariable Long id) {
        return planejamentoService.buscarMateria(planejamentoId, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{planejamentoId}/materias")
    public ResponseEntity<PlanejamentoMateria> adicionarMateria(
            @PathVariable Long planejamentoId,
            @RequestBody PlanejamentoMateriaDTO dados) {
        if (dadosMateriaInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.adicionarMateria(planejamentoId, dados)
                .map(planejamentoMateria -> {
                    URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(planejamentoMateria.getId())
                            .toUri();
                    return ResponseEntity.created(localizacao).body(planejamentoMateria);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{planejamentoId}/materias/{id}")
    public ResponseEntity<PlanejamentoMateria> atualizarMateria(
            @PathVariable Long planejamentoId,
            @PathVariable Long id,
            @RequestBody PlanejamentoMateriaDTO dados) {
        if (dadosMateriaInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.atualizarMateria(planejamentoId, id, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{planejamentoId}/materias/{id}")
    public ResponseEntity<Void> excluirMateria(
            @PathVariable Long planejamentoId,
            @PathVariable Long id) {
        if (!planejamentoService.excluirMateria(planejamentoId, id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos")
    public ResponseEntity<List<PlanejamentoGrupoAssunto>> listarGrupos(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId) {
        return planejamentoService.listarGrupos(planejamentoId, planejamentoMateriaId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{id}")
    public ResponseEntity<PlanejamentoGrupoAssunto> buscarGrupo(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long id) {
        return planejamentoService.buscarGrupo(planejamentoId, planejamentoMateriaId, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos")
    public ResponseEntity<PlanejamentoGrupoAssunto> adicionarGrupo(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @RequestBody PlanejamentoGrupoAssuntoDTO dados) {
        if (dadosGrupoInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.adicionarGrupo(planejamentoId, planejamentoMateriaId, dados)
                .map(grupo -> {
                    URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(grupo.getId())
                            .toUri();
                    return ResponseEntity.created(localizacao).body(grupo);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{id}")
    public ResponseEntity<PlanejamentoGrupoAssunto> atualizarGrupo(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long id,
            @RequestBody PlanejamentoGrupoAssuntoDTO dados) {
        if (dadosGrupoInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.atualizarGrupo(
                        planejamentoId,
                        planejamentoMateriaId,
                        id,
                        dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{id}")
    public ResponseEntity<Void> excluirGrupo(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long id) {
        if (!planejamentoService.excluirGrupo(planejamentoId, planejamentoMateriaId, id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{grupoId}/itens")
    public ResponseEntity<List<PlanejamentoGrupoAssuntoItem>> listarItens(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long grupoId) {
        return planejamentoService.listarItens(planejamentoId, planejamentoMateriaId, grupoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{grupoId}/itens/{id}")
    public ResponseEntity<PlanejamentoGrupoAssuntoItem> buscarItem(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long grupoId,
            @PathVariable Long id) {
        return planejamentoService.buscarItem(
                        planejamentoId,
                        planejamentoMateriaId,
                        grupoId,
                        id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{grupoId}/itens")
    public ResponseEntity<PlanejamentoGrupoAssuntoItem> adicionarItem(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long grupoId,
            @RequestBody PlanejamentoGrupoAssuntoItemDTO dados) {
        if (dadosItemInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.adicionarItem(
                        planejamentoId,
                        planejamentoMateriaId,
                        grupoId,
                        dados)
                .map(item -> {
                    URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(item.getId())
                            .toUri();
                    return ResponseEntity.created(localizacao).body(item);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{grupoId}/itens/{id}")
    public ResponseEntity<PlanejamentoGrupoAssuntoItem> atualizarItem(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long grupoId,
            @PathVariable Long id,
            @RequestBody PlanejamentoGrupoAssuntoItemDTO dados) {
        if (dadosItemInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.atualizarItem(
                        planejamentoId,
                        planejamentoMateriaId,
                        grupoId,
                        id,
                        dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{grupoId}/itens/{id}")
    public ResponseEntity<Void> excluirItem(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long grupoId,
            @PathVariable Long id) {
        if (!planejamentoService.excluirItem(
                planejamentoId,
                planejamentoMateriaId,
                grupoId,
                id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{grupoId}/itens/{itemId}/etapas")
    public ResponseEntity<List<PlanejamentoItemEtapa>> listarEtapas(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long grupoId,
            @PathVariable Long itemId) {
        return planejamentoService.listarEtapas(
                        planejamentoId,
                        planejamentoMateriaId,
                        grupoId,
                        itemId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{grupoId}/itens/{itemId}/etapas/{id}")
    public ResponseEntity<PlanejamentoItemEtapa> buscarEtapa(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long grupoId,
            @PathVariable Long itemId,
            @PathVariable Long id) {
        return planejamentoService.buscarEtapa(
                        planejamentoId,
                        planejamentoMateriaId,
                        grupoId,
                        itemId,
                        id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{planejamentoId}/etapas-por-assunto")
    public ResponseEntity<PlanejamentoItemEtapa> buscarEtapaPorAssunto(
            @PathVariable Long planejamentoId,
            @RequestParam Long assuntoId,
            @RequestParam Long tipoRegistroId) {
        return planejamentoService.buscarEtapaPorAssunto(planejamentoId, assuntoId, tipoRegistroId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/{planejamentoId}/etapas-por-assunto")
    public ResponseEntity<PlanejamentoItemEtapa> adicionarEtapaPorAssunto(
            @PathVariable Long planejamentoId,
            @RequestBody PlanejamentoEtapaPorAssuntoDTO dados) {
        if (dados.getAssuntoId() == null || dados.getTipoRegistroId() == null) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.adicionarEtapaPorAssunto(planejamentoId, dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{grupoId}/itens/{itemId}/etapas")
    public ResponseEntity<PlanejamentoItemEtapa> adicionarEtapa(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long grupoId,
            @PathVariable Long itemId,
            @RequestBody PlanejamentoItemEtapaDTO dados) {
        if (dadosEtapaInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.adicionarEtapa(
                        planejamentoId,
                        planejamentoMateriaId,
                        grupoId,
                        itemId,
                        dados)
                .map(etapa -> {
                    URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(etapa.getId())
                            .toUri();
                    return ResponseEntity.created(localizacao).body(etapa);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{grupoId}/itens/{itemId}/etapas/{id}")
    public ResponseEntity<PlanejamentoItemEtapa> atualizarEtapa(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long grupoId,
            @PathVariable Long itemId,
            @PathVariable Long id,
            @RequestBody PlanejamentoItemEtapaDTO dados) {
        if (dadosEtapaInvalidos(dados)) {
            return ResponseEntity.badRequest().build();
        }

        return planejamentoService.atualizarEtapa(
                        planejamentoId,
                        planejamentoMateriaId,
                        grupoId,
                        itemId,
                        id,
                        dados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{planejamentoId}/materias/{planejamentoMateriaId}/grupos/{grupoId}/itens/{itemId}/etapas/{id}")
    public ResponseEntity<Void> excluirEtapa(
            @PathVariable Long planejamentoId,
            @PathVariable Long planejamentoMateriaId,
            @PathVariable Long grupoId,
            @PathVariable Long itemId,
            @PathVariable Long id) {
        if (!planejamentoService.excluirEtapa(
                planejamentoId,
                planejamentoMateriaId,
                grupoId,
                itemId,
                id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private boolean dadosMateriaInvalidos(PlanejamentoMateriaDTO dados) {
        if (dados.getMateriaId() == null) {
            return true;
        }

        if (dados.getPeso() != null && (dados.getPeso() < 1 || dados.getPeso() > 5)) {
            return true;
        }

        return dados.getOrdem() != null && dados.getOrdem() <= 0;
    }

    private boolean dadosGrupoMateriaInvalidos(PlanejamentoGrupoMateriaDTO dados) {
        if (dados.getNome() == null
                || dados.getNome().isBlank()
                || dados.getNome().length() > 150) {
            return true;
        }

        if (dados.getPeso() != null && (dados.getPeso() < 1 || dados.getPeso() > 5)) {
            return true;
        }

        return dados.getOrdem() != null && dados.getOrdem() <= 0;
    }

    private boolean dadosGrupoInvalidos(PlanejamentoGrupoAssuntoDTO dados) {
        if (dados.getTipo() == null) {
            return true;
        }

        if (dados.getTipo() == PlanejamentoGrupoAssuntoTipo.ASSUNTO
                && dados.getNome() != null) {
            return true;
        }

        if (dados.getTipo() == PlanejamentoGrupoAssuntoTipo.BLOCO
                && (dados.getNome() == null
                        || dados.getNome().isBlank()
                        || dados.getNome().length() > 150)) {
            return true;
        }

        if (dados.getPeso() != null && (dados.getPeso() < 1 || dados.getPeso() > 5)) {
            return true;
        }

        return dados.getOrdem() != null && dados.getOrdem() <= 0;
    }

    private boolean dadosItemInvalidos(PlanejamentoGrupoAssuntoItemDTO dados) {
        if (dados.getAssuntoId() == null) {
            return true;
        }

        if (dados.getPeso() != null && (dados.getPeso() < 1 || dados.getPeso() > 5)) {
            return true;
        }

        return dados.getOrdem() != null && dados.getOrdem() <= 0;
    }

    private boolean dadosEtapaInvalidos(PlanejamentoItemEtapaDTO dados) {
        if (dados.getTipoRegistroId() == null) {
            return true;
        }

        if (dados.getOrdem() != null && dados.getOrdem() <= 0) {
            return true;
        }

        return Boolean.TRUE.equals(dados.getConcluidaManualmente())
                && !Boolean.TRUE.equals(dados.getConcluida());
    }

    @ExceptionHandler({
            PlanejamentoConflitoException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<Void> tratarConflito() {
        return ResponseEntity.status(409).build();
    }
}
