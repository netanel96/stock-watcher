package candidateStocks;

import entities.Stock;

import java.util.List;

public interface ICandidateStocksFetcher {
    public List<Stock> getCandidateStocks();

    public void UpdateStock(Stock stock);
    public String printUpdateStockFileActions();
}
