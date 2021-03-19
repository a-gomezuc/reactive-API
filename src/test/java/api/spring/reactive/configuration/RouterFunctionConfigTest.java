package api.spring.reactive.configuration;

import api.spring.reactive.ReactiveApplication;
import api.spring.reactive.model.Ball;
import api.spring.reactive.service.BallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ReactiveApplication.class)
@ExtendWith(SpringExtension.class)
class RouterFunctionConfigTest {

    @Autowired
    private RouterFunctionConfig config;

    @MockBean
    private BallService ballService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToRouterFunction(config.ballsRouterFunction()).build();
    }

    @Test
    void shouldReturnAListOfBalls() throws IOException {
        when(ballService.getAllBalls()).thenReturn(getBallsFlux());
        ClassPathResource jsonResponse = new ClassPathResource("test/balls-response.json");
        String jsonResponseString = StreamUtils.copyToString(jsonResponse.getInputStream(), StandardCharsets.UTF_8);
        webTestClient.get().uri("/balls").exchange().expectStatus().isOk().expectBody().json(jsonResponseString);
    }

    @Test
    void shouldReturnTheBallWithIdOne() throws IOException {
        Ball ballOne = new Ball(1L, "Babolat", "Tennis", BigDecimal.valueOf(1.55), "Yellow");
        when(ballService.getBallById(1L)).thenReturn(Mono.just(ballOne));
        webTestClient.get().uri("/balls/1").exchange().expectStatus().isOk().expectBody(Ball.class).isEqualTo(ballOne);
    }

    @Test
    void shouldCreateANewBall() {
        Ball newBall = new Ball(1L, "Mikasa", "Volleyball", BigDecimal.valueOf(59.99), "Yellow and Blue");
        when(ballService.saveBall(newBall)).thenReturn(Mono.just(newBall));
        webTestClient.post().uri("/balls").contentType(MediaType.APPLICATION_JSON).bodyValue(newBall).exchange()
                .expectStatus().isCreated().expectBody(Ball.class).isEqualTo(newBall);
    }

    private Flux<Ball> getBallsFlux() {
        Ball ballOne = new Ball(1L, "Umbro", "Soccer", BigDecimal.valueOf(24.99), "Black");
        Ball ballTwo = new Ball(2L, "Adidas", "Basketball", BigDecimal.valueOf(35.99), "Orange");
        Ball ballThree = new Ball(3L, "Nike", "Handball", BigDecimal.valueOf(15.99), "White");
        return Flux.just(ballOne, ballTwo, ballThree);
    }

}