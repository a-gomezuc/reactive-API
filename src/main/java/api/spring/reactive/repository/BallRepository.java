package api.spring.reactive.repository;

import api.spring.reactive.model.Ball;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallRepository extends ReactiveCrudRepository<Ball, Long> {
}
