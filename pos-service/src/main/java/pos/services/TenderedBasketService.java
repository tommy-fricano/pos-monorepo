package pos.services;

import pos.models.Basket;
import pos.repositories.TenderedBasketRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenderedBasketService {

    private final TenderedBasketRepo tenderedBasketRepo;

    public List<Basket> getTenderedBaskets(){
        return tenderedBasketRepo.findAll();
    }

    public void saveTenderedBasket(Basket basket){
        tenderedBasketRepo.save(basket);
    }
}
