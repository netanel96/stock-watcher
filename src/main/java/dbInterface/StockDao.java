package dbInterface;

import entities.Stock;

import java.util.List;
import java.util.Optional;

public class StockDao implements Dao<Stock>{
    @Override
    public Optional<Stock> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Stock> getAll() {
        return null;
    }

    @Override
    public void save(Stock stock) {

    }

    @Override
    public void update(Stock stock, String[] params) {

    }

    @Override
    public void delete(Stock stock) {

    }
}
