package api.spring.reactive.configuration;

import api.spring.reactive.model.Ball;
import api.spring.reactive.service.BallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RouterFunctionConfig {

    private BallService ballService;

    @Autowired
    public RouterFunctionConfig(BallService ballService) {
        this.ballService = ballService;
    }

    @Bean
    public RouterFunction<?> ballsRouterFunction() {
        return route(GET("/balls"), request -> ok().body(ballService.getAllBalls(), Ball.class))
                .and(route(GET("/balls/{id}"), this::getBallByID))
                .and(route(POST("/balls").and(accept(MediaType.APPLICATION_JSON)), this::createBall));
    }

    private Mono<ServerResponse> getBallByID(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return ok().body(ballService.getBallById(id), Ball.class);
    }

    public Mono<ServerResponse> createBall(ServerRequest request) {
        return request.bodyToMono(Ball.class)
                .flatMap(ball -> ballService.saveBall(ball))
                .flatMap(ball -> ServerResponse.created(URI.create("/balls/" + ball.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(ball)));

    }
}
