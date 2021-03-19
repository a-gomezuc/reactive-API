package api.spring.reactive.service;

import api.spring.reactive.model.Ball;
import api.spring.reactive.repository.BallRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class BallService {

    private BallRepository ballRepository;

    @Autowired
    public BallService(BallRepository ballRepository) {
        this.ballRepository = ballRepository;
    }

    public Flux<Ball> getAllBalls() {
        return ballRepository.findAll();
    }

    public Mono<Ball> saveBall(Ball ball) {
        return ballRepository.save(ball);
    }

    public Mono<Ball> getBallById(Long id) {
        return ballRepository.findById(id);
    }

}
