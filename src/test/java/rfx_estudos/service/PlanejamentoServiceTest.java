package rfx_estudos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import rfx_estudos.dto.PlanejamentoDestinoRegistroLinhaDTO;
import rfx_estudos.repository.AssuntoRepository;
import rfx_estudos.repository.MateriaRepository;
import rfx_estudos.repository.PlanejamentoGrupoAssuntoItemRepository;
import rfx_estudos.repository.PlanejamentoGrupoAssuntoRepository;
import rfx_estudos.repository.PlanejamentoGrupoMateriaRepository;
import rfx_estudos.repository.PlanejamentoItemEtapaRepository;
import rfx_estudos.repository.PlanejamentoMateriaRepository;
import rfx_estudos.repository.PlanejamentoRepository;
import rfx_estudos.repository.RegistroRepository;
import rfx_estudos.repository.TipoRegistroRepository;

@ExtendWith(MockitoExtension.class)
class PlanejamentoServiceTest {

    @Mock private PlanejamentoRepository planejamentoRepository;
    @Mock private PlanejamentoMateriaRepository planejamentoMateriaRepository;
    @Mock private PlanejamentoGrupoAssuntoRepository planejamentoGrupoAssuntoRepository;
    @Mock private PlanejamentoGrupoMateriaRepository planejamentoGrupoMateriaRepository;
    @Mock private PlanejamentoGrupoAssuntoItemRepository planejamentoGrupoAssuntoItemRepository;
    @Mock private PlanejamentoItemEtapaRepository planejamentoItemEtapaRepository;
    @Mock private MateriaRepository materiaRepository;
    @Mock private AssuntoRepository assuntoRepository;
    @Mock private TipoRegistroRepository tipoRegistroRepository;
    @Mock private RegistroRepository registroRepository;
    @Mock private PlanejamentoDestinoRegistroLinhaDTO linha;

    @InjectMocks private PlanejamentoService service;

    @Test
    void deveListarDestinosComUmaUnicaConsulta() {
        when(planejamentoMateriaRepository.buscarDestinosRegistro(1L, 2L, 3L))
                .thenReturn(List.of(linha));
        when(linha.getPlanejamentoMateriaId()).thenReturn(10L);
        when(linha.getEtapaId()).thenReturn(20L);
        when(linha.getPlanejamentoNome()).thenReturn("Plano");
        when(linha.getMateriaNome()).thenReturn("Português");

        var destinos = service.listarDestinosRegistro(1L, 2L, 3L).orElseThrow();

        assertThat(destinos).singleElement().satisfies(destino -> {
            assertThat(destino.planejamentoMateriaId()).isEqualTo(10L);
            assertThat(destino.etapaId()).isEqualTo(20L);
            assertThat(destino.materiaNome()).isEqualTo("Português");
        });
        verify(planejamentoMateriaRepository).buscarDestinosRegistro(1L, 2L, 3L);
        verifyNoInteractions(
                planejamentoRepository,
                assuntoRepository,
                planejamentoGrupoAssuntoItemRepository,
                planejamentoItemEtapaRepository);
    }
}
