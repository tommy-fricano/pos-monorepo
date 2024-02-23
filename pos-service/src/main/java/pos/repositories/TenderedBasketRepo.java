package pos.repositories;

import pos.models.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenderedBasketRepo extends JpaRepository<Basket, Long> {


}
