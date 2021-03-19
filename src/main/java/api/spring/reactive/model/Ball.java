package api.spring.reactive.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Balls")
public class Ball {
    @Id
    private long id;
    private String brand;
    private String sport;
    private BigDecimal price;
    private String colour;
}
