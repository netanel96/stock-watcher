package stockDataFetcher;

public interface IStockDataProvider {
    double getStockPrice(String stockSymbol);
    void initStockData(String[] tickersList);
}
