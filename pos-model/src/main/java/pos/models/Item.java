package pos.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Data
@Table(name = "PRICE_BOOK")
@Embeddable
public class Item {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @Column
    private long code;
    @Column
    private String name;
    @Column
    private BigDecimal price;

    public Item(long code, String name, BigDecimal price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }
}
