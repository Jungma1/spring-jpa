package jpabook.jpashop.domain.item;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "item_type")
public abstract class Item {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;

        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }

        this.stockQuantity = restStock;
    }
}
