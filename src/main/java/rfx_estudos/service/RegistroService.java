package rfx_estudos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rfx_estudos.domain.PlanejamentoItemEtapa;
import rfx_estudos.domain.CicloItem;
import rfx_estudos.domain.Registro;
import rfx_estudos.repository.CicloItemRepository;
import rfx_estudos.repository.PlanejamentoItemEtapaRepository;
import rfx_estudos.repository.RegistroRepository;

@Service
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final PlanejamentoItemEtapaRepository planejamentoItemEtapaRepository;
    private final CicloItemRepository cicloItemRepository;

    public RegistroService(
            RegistroRepository registroRepository,
            PlanejamentoItemEtapaRepository planejamentoItemEtapaRepository,
            CicloItemRepository cicloItemRepository) {
        this.registroRepository = registroRepository;
        this.planejamentoItemEtapaRepository = planejamentoItemEtapaRepository;
        this.cicloItemRepository = cicloItemRepository;
    }

    @Transactional
    public Optional<Registro> criar(Registro registro) {
        EtapaInformada etapaInformada = buscarEtapaDoPlanejamento(registro);
        if (!etapaInformada.valida()) {
            return Optional.empty();
        }

        registro.setId(null);
        registro.setConcluido(Boolean.TRUE.equals(registro.getConcluido()));
        registro.setPlanejamentoEtapa(etapaInformada.etapa());

        Registro criado = registroRepository.saveAndFlush(registro);
        marcarItensDoCiclo(criado);
        concluirEtapaSeSolicitado(
                etapaInformada.etapa(),
                Boolean.TRUE.equals(criado.getConcluido()),
                criado.getDataEstudo());
        return Optional.of(criado);
    }

    @Transactional
    public Optional<Registro> atualizar(Long id, Registro dados) {
        Optional<Registro> registroExistente = registroRepository.findById(id);
        EtapaInformada etapaNova = buscarEtapaDoPlanejamento(dados);

        if (registroExistente.isEmpty() || !etapaNova.valida()) {
            return Optional.empty();
        }

        Registro registro = registroExistente.get();
        registro.setMateria(dados.getMateria());
        registro.setAssunto(dados.getAssunto());
        registro.setMaterialTipo(dados.getMaterialTipo());
        registro.setMaterialGuia(dados.getMaterialGuia());
        registro.setPlanejamento(dados.getPlanejamento());
        registro.setTipoRegistro(dados.getTipoRegistro());
        registro.setConcluido(Boolean.TRUE.equals(dados.getConcluido()));
        registro.setPlanejamentoEtapa(etapaNova.etapa());
        registro.setMaterialNome(dados.getMaterialNome());
        registro.setPuntoParada(dados.getPuntoParada());
        registro.setQuestoesFeitas(dados.getQuestoesFeitas());
        registro.setQuestoesAcertadas(dados.getQuestoesAcertadas());
        registro.setDataEstudo(dados.getDataEstudo());
        registro.setTempoEstudado(dados.getTempoEstudado());
        registro.setLinkDocumento(dados.getLinkDocumento());
        registro.setObservacoes(dados.getObservacoes());

        Registro atualizado = registroRepository.saveAndFlush(registro);
        limparConclusoesDoRegistro(atualizado.getId());
        marcarItensDoCiclo(atualizado);
        concluirEtapaSeSolicitado(
                etapaNova.etapa(),
                Boolean.TRUE.equals(atualizado.getConcluido()),
                atualizado.getDataEstudo());
        return Optional.of(atualizado);
    }

    @Transactional
    public boolean excluir(Long id) {
        Optional<Registro> registro = registroRepository.findById(id);
        if (registro.isEmpty()) {
            return false;
        }

        registroRepository.delete(registro.get());
        registroRepository.flush();
        return true;
    }

    private void marcarItensDoCiclo(Registro registro) {
        if (registro.getMateria() == null || registro.getMateria().getId() == null) return;

        Long planejamentoId = registro.getPlanejamento() != null
                ? registro.getPlanejamento().getId()
                : null;
        List<CicloItem> itens = cicloItemRepository.buscarPendentesDoRegistro(
                registro.getMateria().getId(),
                planejamentoId);
        itens.forEach(item -> item.setRegistroConclusao(registro));
        cicloItemRepository.saveAllAndFlush(itens);
    }

    private void limparConclusoesDoRegistro(Long registroId) {
        List<CicloItem> itens = cicloItemRepository.findByRegistroConclusaoId(registroId);
        itens.forEach(item -> item.setRegistroConclusao(null));
        cicloItemRepository.saveAllAndFlush(itens);
    }

    private EtapaInformada buscarEtapaDoPlanejamento(Registro registro) {
        if (registro.getPlanejamento() == null) {
            return new EtapaInformada(true, null);
        }

        Long planejamentoId = registro.getPlanejamento().getId();
        Long assuntoId = registro.getAssunto() != null
                ? registro.getAssunto().getId()
                : null;
        Long tipoRegistroId = registro.getTipoRegistro() != null
                ? registro.getTipoRegistro().getId()
                : null;

        if (planejamentoId == null || assuntoId == null || tipoRegistroId == null) {
            return new EtapaInformada(false, null);
        }

        List<PlanejamentoItemEtapa> etapas = planejamentoItemEtapaRepository
                .findByPlanejamentoAssuntoETipoRegistro(
                        planejamentoId,
                        assuntoId,
                        tipoRegistroId);

        if (etapas.isEmpty()) {
            return new EtapaInformada(true, null);
        }

        if (etapas.size() > 1) {
            return new EtapaInformada(false, null);
        }

        return new EtapaInformada(true, etapas.get(0));
    }

    private void concluirEtapaSeSolicitado(
            PlanejamentoItemEtapa etapa,
            boolean sessaoConcluida,
            LocalDate dataConclusao) {
        if (etapa == null || !sessaoConcluida || Boolean.TRUE.equals(etapa.getConcluida())) {
            return;
        }

        etapa.setConcluida(true);
        etapa.setDataConcluida(dataConclusao);
        planejamentoItemEtapaRepository.save(etapa);
    }

    private record EtapaInformada(boolean valida, PlanejamentoItemEtapa etapa) {}
}
