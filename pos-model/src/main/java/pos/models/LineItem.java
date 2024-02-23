package pos.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@Data
@Entity
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Item item;
    private BigDecimal price;
    private int quantity;
    private boolean voided;

    public LineItem(){

    }

    public LineItem(Long id, Item item, BigDecimal price, int quantity, boolean voided) {
        this.id = id;
        this.item = item;
        this.price = price;
        this.quantity = quantity;
        this.voided = voided;
    }

    @JsonCreator
    public LineItem(@JsonProperty("item") Item item, @JsonProperty("price") BigDecimal price, @JsonProperty("quantity") int quantity, @JsonProperty("voided") boolean voided) {
        this.item = item;
        this.price = price;
        this.quantity = quantity;
        this.voided = voided;
    }

}
