package rfx_estudos.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class PingControllerTest {

    private final PingController controller = new PingController();

    @Test
    void deveResponderSemConteudoSemDependenciasExternas() {
        var resposta = controller.ping();

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(resposta.getBody()).isNull();
    }
}
