package rfx_estudos.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rfx_estudos.domain.PlanejamentoItemEtapa;
import rfx_estudos.domain.Registro;
import rfx_estudos.repository.PlanejamentoItemEtapaRepository;
import rfx_estudos.repository.RegistroRepository;

@Service
public class RegistroService {

    private final RegistroRepository registroRepository;
    private final PlanejamentoItemEtapaRepository planejamentoItemEtapaRepository;

    public RegistroService(
            RegistroRepository registroRepository,
            PlanejamentoItemEtapaRepository planejamentoItemEtapaRepository) {
        this.registroRepository = registroRepository;
        this.planejamentoItemEtapaRepository = planejamentoItemEtapaRepository;
    }

    @Transactional
    public Optional<Registro> criar(Registro registro) {
        EtapaInformada etapaInformada = buscarEtapaInformada(registro);
        if (!etapaInformada.valida()) {
            return Optional.empty();
        }

        registro.setId(null);
        registro.setConcluido(Boolean.TRUE.equals(registro.getConcluido()));
        registro.setPlanejamentoEtapa(etapaInformada.etapa());

        Registro criado = registroRepository.saveAndFlush(registro);
        sincronizarEtapa(idEtapa(criado));
        return Optional.of(criado);
    }

    @Transactional
    public Optional<Registro> atualizar(Long id, Registro dados) {
        Optional<Registro> registroExistente = registroRepository.findById(id);
        EtapaInformada etapaNova = buscarEtapaInformada(dados);

        if (registroExistente.isEmpty() || !etapaNova.valida()) {
            return Optional.empty();
        }

        Registro registro = registroExistente.get();
        Long etapaAnteriorId = idEtapa(registro);

        registro.setMateria(dados.getMateria());
        registro.setAssunto(dados.getAssunto());
        registro.setMaterialTipo(dados.getMaterialTipo());
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
        Long etapaNovaId = idEtapa(atualizado);
        sincronizarEtapa(etapaAnteriorId);

        if (etapaNovaId != null && !etapaNovaId.equals(etapaAnteriorId)) {
            sincronizarEtapa(etapaNovaId);
        }

        return Optional.of(atualizado);
    }

    @Transactional
    public boolean excluir(Long id) {
        Optional<Registro> registro = registroRepository.findById(id);
        if (registro.isEmpty()) {
            return false;
        }

        Long etapaId = idEtapa(registro.get());
        registroRepository.delete(registro.get());
        registroRepository.flush();
        sincronizarEtapa(etapaId);
        return true;
    }

    private EtapaInformada buscarEtapaInformada(Registro registro) {
        if (registro.getPlanejamentoEtapa() == null) {
            return new EtapaInformada(true, null);
        }

        Long etapaId = registro.getPlanejamentoEtapa().getId();
        if (etapaId == null) {
            return new EtapaInformada(false, null);
        }

        Optional<PlanejamentoItemEtapa> etapa = planejamentoItemEtapaRepository.findById(etapaId);
        if (etapa.isEmpty() || !registroCompativelComEtapa(registro, etapa.get())) {
            return new EtapaInformada(false, null);
        }

        return new EtapaInformada(true, etapa.get());
    }

    private void sincronizarEtapa(Long etapaId) {
        if (etapaId == null) {
            return;
        }

        planejamentoItemEtapaRepository.findById(etapaId).ifPresent(etapa -> {
            Optional<Registro> ultimoRegistroConcluido = registroRepository
                    .findFirstByPlanejamentoEtapaIdAndConcluidoTrueOrderByDataEstudoDescIdDesc(
                            etapaId);

            if (ultimoRegistroConcluido.isPresent()) {
                etapa.setConcluida(true);
                if (!Boolean.TRUE.equals(etapa.getConcluidaManualmente())) {
                    etapa.setDataConcluida(ultimoRegistroConcluido.get().getDataEstudo());
                }
            } else if (!Boolean.TRUE.equals(etapa.getConcluidaManualmente())) {
                etapa.setConcluida(false);
                etapa.setDataConcluida(null);
            }

            planejamentoItemEtapaRepository.save(etapa);
        });
    }

    private Long idEtapa(Registro registro) {
        return registro.getPlanejamentoEtapa() != null
                ? registro.getPlanejamentoEtapa().getId()
                : null;
    }

    private boolean registroCompativelComEtapa(
            Registro registro,
            PlanejamentoItemEtapa etapa) {
        if (registro.getAssunto() == null
                || registro.getAssunto().getId() == null
                || registro.getTipoRegistro() == null
                || registro.getTipoRegistro().getId() == null) {
            return false;
        }

        Long assuntoDaEtapaId = etapa.getPlanejamentoGrupoAssuntoItem().getAssunto().getId();
        Long tipoDaEtapaId = etapa.getTipoRegistro().getId();

        return registro.getAssunto().getId().equals(assuntoDaEtapaId)
                && registro.getTipoRegistro().getId().equals(tipoDaEtapaId);
    }

    private record EtapaInformada(boolean valida, PlanejamentoItemEtapa etapa) {}
}
