package rfx_estudos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import rfx_estudos.domain.Ciclo;
import rfx_estudos.domain.CicloItem;
import rfx_estudos.domain.Materia;
import rfx_estudos.dto.CicloItemDTO;
import rfx_estudos.repository.CicloItemRepository;
import rfx_estudos.repository.CicloRepository;
import rfx_estudos.repository.MateriaRepository;
import rfx_estudos.repository.PlanejamentoGrupoMateriaRepository;

@ExtendWith(MockitoExtension.class)
class CicloServiceTest {

    @Mock private CicloRepository cicloRepository;
    @Mock private CicloItemRepository cicloItemRepository;
    @Mock private MateriaRepository materiaRepository;
    @Mock private PlanejamentoGrupoMateriaRepository planejamentoGrupoMateriaRepository;
    @Mock private EntityManager entityManager;

    @InjectMocks private CicloService service;

    @Test
    void devePermitirAdicionarMateriaQueJaExisteNoCiclo() {
        Ciclo ciclo = new Ciclo();
        ciclo.setId(1L);
        Materia materia = new Materia();
        materia.setId(2L);
        CicloItemDTO dados = new CicloItemDTO();
        dados.setMateriaId(2L);
        dados.setOrdem(3);

        when(cicloRepository.findById(1L)).thenReturn(Optional.of(ciclo));
        when(materiaRepository.findByIdAndAtivoTrue(2L)).thenReturn(Optional.of(materia));
        lenient().when(cicloItemRepository.existsByCicloIdAndMateriaId(1L, 2L)).thenReturn(true);
        when(cicloItemRepository.save(any(CicloItem.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        CicloItem item = service.adicionarItem(1L, dados).orElseThrow();

        assertThat(item.getCiclo()).isSameAs(ciclo);
        assertThat(item.getMateria()).isSameAs(materia);
        assertThat(item.getOrdem()).isEqualTo(3);
        verify(cicloItemRepository, never()).existsByCicloIdAndMateriaId(1L, 2L);
        verify(cicloItemRepository).save(item);
    }
}
