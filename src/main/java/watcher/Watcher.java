package watcher;

import actionDeciders.IActionDecider;
import actionDeciders.StrategyA;
import candidateStocks.ICandidateStocksFetcher;
import candidateStocks.JsonFileCandidateStocksFetcher;
import entities.EStatus;
import entities.Stock;
import investProps.*;
import stockDataFetcher.StockDataFetcher;
import wallet.EWalletAction;
import wallet.IWalletAction;
import wallet.WalletActionsImp;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Watcher {
    ICandidateStocksFetcher iCandidateStocksFetcher;
    StockDataFetcher stockDataFetcher;
    List<IInvestStrategyPropsReader> listOfIInvestStrategyPropsReader;
    List<Stock> stockList;
    IActionDecider iActionDecider;
    IWalletAction iWalletAction;
    Logger logger = LoggerUtils.getLogger(Watcher.class.getName());

    public Watcher() throws FileNotFoundException {
        listOfIInvestStrategyPropsReader = new SimpleInvestPropsFetcher().getAllInvestStrategyProps();
        stockList = new ArrayList<>();
        iWalletAction = new WalletActionsImp();
        iCandidateStocksFetcher = new JsonFileCandidateStocksFetcher();
        stockList = iCandidateStocksFetcher.getCandidateStocks();
//        stockList.forEach(x->logger.info(x.toString()));
        stockDataFetcher = new StockDataFetcher(stockList.stream().
                map(stock -> " " + stock.getSymbol()).
                collect(Collectors.joining()));
    }

    /*
    iterating over each strategyProps.and for each Strategy kind iterate over all stocks
    and for each stock check if there is a need for action.
    needs to be scheduled method.
     */
    public void watch() {
        List<Stock> finalStockList = stockList;
        listOfIInvestStrategyPropsReader.forEach(investPropsStrategyReader ->
                finalStockList.forEach(stock -> watchStock(stock, investPropsStrategyReader)));
        System.out.println(iActionDecider.printDataForMonitor());
        logger.info(iActionDecider.printDataForMonitor());
        if (!iCandidateStocksFetcher.printUpdateStockFileActions().equals("")) System.out.println("\nupdates in candidate file:" + iCandidateStocksFetcher.printUpdateStockFileActions());
        System.out.println("\nWALLET_ACTIONS:" + iWalletAction.printAllWalletActions());
        logger.info("\n"+iWalletAction.printAllWalletActions());
    }

    private void watchStock(Stock stock, IInvestStrategyPropsReader investStrategyPropsReader) {
        InvestProps investProps = new InvestProps(investStrategyPropsReader.getPercentageEntry());
        double currentStockPrice = stockDataFetcher.getStockPrice(stock.getSymbol());
        iActionDecider = new StrategyA(stock, investProps, currentStockPrice);
        EWalletAction eWalletAction = iActionDecider.decideWalletAction(stock);
        makeAction(eWalletAction.getWalletActionValue(), stock);
    }

    private void makeAction(int actionNum, Stock stock) {
        switch (actionNum) {
            case 0:
                iWalletAction.sendAction(EWalletAction.BUY, stock);
                stock.setStatus(EStatus.ACQUIRED.getStringValue());
                iCandidateStocksFetcher.UpdateStock(stock);
                break;
            case 1:
                iWalletAction.sendAction(EWalletAction.SELL, stock);
                iCandidateStocksFetcher.UpdateStock(new Stock(stock.getSymbol(), stock.getName(),
                        stock.getStartPrice(), EStatus.NOT_ACQUIRED.getStringValue(),
                        stock.getAcquiredDate(), String.valueOf(Integer.parseInt(stock.getCycles()) + 1)));
                break;
            case 2:
                iWalletAction.sendAction(EWalletAction.CHECK, stock);
                break;
        }
    }

}
