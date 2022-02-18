package app;

import actionDeciders.IActionDecider;
import actionDeciders.StrategyA;
import actions.*;
import candidateStocks.ICandidateStocksFetcher;
import candidateStocks.JsonFileCandidateStocksFetcher1;
import entities.EStatus;
import entities.Stock;
import investProps.*;
import stockDataFetcher.IStockDataProvider;
import stockDataFetcher.StockDataProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Watcher {
    ICandidateStocksFetcher iCandidateStocksFetcher;
    IStockDataProvider stockDataFetcher;
    List<IInvestStrategyPropsReader> listOfIInvestStrategyPropsReader;
    List<Stock> stockList;
    IActionDecider iActionDecider;
    IWalletAction iWalletAction;
    ITestDataHandler iTestDataHandler;
    static String DEFAULT_ACQUIRED_DATE ="none";
    Logger logger = LoggerUtils.getLogger(Watcher.class.getName());

    public Watcher() throws FileNotFoundException {
        listOfIInvestStrategyPropsReader = new SimpleInvestPropsFetcher().getAllInvestStrategyProps();
        stockList = new ArrayList<>();
        iWalletAction = new WalletActionsImp();
        iCandidateStocksFetcher = new JsonFileCandidateStocksFetcher1("\\src\\main\\java\\configurations\\candidateStocks.json");
        stockList = iCandidateStocksFetcher.getCandidateStocks();
        iTestDataHandler=new TestDataHandlerJsonImp("\\app\\src\\main\\java\\configurations\\testData.json");
//        stockList.forEach(x->logger.info(x.toString()));
        stockDataFetcher=new StockDataProvider();
        stockDataFetcher.initStockData(getTickersStringArray());
    }

    private String createTickerList() {
        String stockListTickers = stockList.stream().
                map(stock -> " " + stock.getSymbol()).
                collect(Collectors.joining());
        String tickersOfStrategies=StrategyA.getRequiredTickers();
        return stockListTickers+" "+tickersOfStrategies;
    }

    private String[] getTickersStringArray() {
        List<String> stockStringList= stockList.stream().map(Stock::getSymbol).collect(Collectors.toList());
        String tickersOfStrategies=StrategyA.getRequiredTickers();
        stockStringList.add(tickersOfStrategies);
        return stockStringList.stream().toArray(String[]::new);
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
        iActionDecider = new StrategyA(stock, investProps, currentStockPrice,stockDataFetcher);
        EWalletAction eWalletAction = iActionDecider.decideWalletAction(stock);
        makeAction(eWalletAction.getWalletActionValue(), stock,investProps);
    }
    private void makeAction(int actionNum, Stock stock, InvestProps investProps) {
        switch (actionNum) {
            case 0:
                iWalletAction.sendAction(EWalletAction.BUY, stock);
                stock.setStatus(EStatus.ACQUIRED.getStringValue());
                stock.setAcquiredDate(getTodayDate());
                iCandidateStocksFetcher.UpdateStock(stock);
                break;
            case 1:
                iWalletAction.sendAction(EWalletAction.SELL, stock);
                iCandidateStocksFetcher.UpdateStock(new Stock(stock.getSymbol(), stock.getName(),
                        stock.getStartPrice(), EStatus.NOT_ACQUIRED.getStringValue(),
                        DEFAULT_ACQUIRED_DATE, String.valueOf(Integer.parseInt(stock.getCycles()) + 1)));
                iTestDataHandler.updateTestData(stock,investProps);
                break;
            case 2:
                iWalletAction.sendAction(EWalletAction.CHECK, stock);
                break;
        }
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = simpleDateFormat.format(cal.getTime());
        return formattedDate;
    }


//TODO:
// 1.look at print of acuired when changed, for some reason its not changed on print.
// 2.fix testData for counting loses (according to a configurable lose definition) and add more data on each win/lose.
// 3.fix matanya bug of python exec command for when the location of the project is in a name like ma tanya (with space) the solution is to put it inside "".
// 4.add seperaation of logs in ui. to show in one button the user output which is simple action buy/sell/check stock x.and in other button to show detailed state of each watched stock,and in another button to show detailed history of (test and real) actions and win lose values and a sum.
// 5. try using YahooFinanceAPI jar instead of python script.
}
