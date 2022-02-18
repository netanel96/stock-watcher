package stockDataFetcher;

import app.LoggerUtils;
import app.Watcher;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockDataProvider implements IStockDataProvider {
    Map<String, Stock> yahooStocksData;

    public void initStockData(String[] tickersList)  {
        try {
            yahooStocksData = YahooFinance.get(tickersList); // single request
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getStockPrice(String stockSymbol)  {
        double stockPrice=-1;
        try {
            stockPrice= yahooStocksData.get(stockSymbol).getQuote(true).getPrice().doubleValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stockPrice;
    }
}
