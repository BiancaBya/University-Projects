package drinkshop.service.interfaces;

import drinkshop.domain.Reteta;

import java.util.List;

public interface IRetetaService {
    void addReteta(Reteta reteta);
    void updateReteta(Reteta reteta);
    void deleteReteta(int id);
    Reteta findById(int id);
    List<Reteta> getAll();
}
