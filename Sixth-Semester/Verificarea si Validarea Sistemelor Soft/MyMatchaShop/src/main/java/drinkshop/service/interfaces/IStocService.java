package drinkshop.service.interfaces;

import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;

import java.util.List;

public interface IStocService {
    List<Stoc> getAll();
    void add(Stoc stoc);
    void update(Stoc stoc);
    void delete(int id);
    boolean areSuficient(Reteta reteta);
    void consuma(Reteta reteta);
}
