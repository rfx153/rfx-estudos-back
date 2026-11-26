package rfx_estudos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rfx_estudos.domain.Ciclo;
import rfx_estudos.domain.CicloItem;
import rfx_estudos.domain.Materia;
import rfx_estudos.domain.Assunto;
import rfx_estudos.domain.Planejamento;
import rfx_estudos.domain.Registro;
import rfx_estudos.domain.TipoRegistro;
import rfx_estudos.repository.CicloItemRepository;
import rfx_estudos.repository.PlanejamentoItemEtapaRepository;
import rfx_estudos.repository.PlanejamentoRepository;
import rfx_estudos.repository.RegistroRepository;

@ExtendWith(MockitoExtension.class)
class RegistroServiceTest {

    @Mock private RegistroRepository registroRepository;
    @Mock private PlanejamentoItemEtapaRepository planejamentoItemEtapaRepository;
    @Mock private PlanejamentoRepository planejamentoRepository;
    @Mock private CicloItemRepository cicloItemRepository;

    @InjectMocks private RegistroService service;

    @Test
    void deveConcluirSomenteAPrimeiraOcorrenciaPendentePorCiclo() {
        Materia matematica = new Materia();
        matematica.setId(10L);
        Registro registro = new Registro();
        registro.setMateria(matematica);

        Ciclo primeiroCiclo = ciclo(1L);
        CicloItem primeiraMatematica = item(primeiroCiclo, 1);
        CicloItem segundaMatematica = item(primeiroCiclo, 3);
        CicloItem matematicaOutroCiclo = item(ciclo(2L), 2);

        when(registroRepository.saveAndFlush(registro)).thenReturn(registro);
        when(cicloItemRepository.buscarPendentesDoRegistro(10L, null))
                .thenReturn(List.of(
                        segundaMatematica,
                        primeiraMatematica,
                        matematicaOutroCiclo));

        service.criar(registro).orElseThrow();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<CicloItem>> itensSalvos = ArgumentCaptor.forClass(List.class);
        verify(cicloItemRepository).saveAllAndFlush(itensSalvos.capture());
        assertThat(itensSalvos.getValue())
                .containsExactly(primeiraMatematica, matematicaOutroCiclo);
        assertThat(primeiraMatematica.getRegistroConclusao()).isSameAs(registro);
        assertThat(segundaMatematica.getRegistroConclusao()).isNull();
        assertThat(matematicaOutroCiclo.getRegistroConclusao()).isSameAs(registro);
    }

    @Test
    void naoDeveCriarRegistroEmPlanejamentoDesabilitado() {
        Planejamento planejamento = new Planejamento();
        planejamento.setId(1L);
        Assunto assunto = new Assunto();
        assunto.setId(2L);
        TipoRegistro tipoRegistro = new TipoRegistro();
        tipoRegistro.setId(3L);
        Registro registro = new Registro();
        registro.setPlanejamento(planejamento);
        registro.setAssunto(assunto);
        registro.setTipoRegistro(tipoRegistro);
        when(planejamentoRepository.existsByIdAndAtivoTrue(1L)).thenReturn(false);

        assertThat(service.criar(registro)).isEmpty();

        verify(planejamentoRepository).existsByIdAndAtivoTrue(1L);
        verifyNoInteractions(registroRepository, planejamentoItemEtapaRepository);
    }

    private Ciclo ciclo(Long id) {
        Ciclo ciclo = new Ciclo();
        ciclo.setId(id);
        return ciclo;
    }

    private CicloItem item(Ciclo ciclo, int ordem) {
        CicloItem item = new CicloItem();
        item.setCiclo(ciclo);
        item.setOrdem(ordem);
        return item;
    }
}
