package actionDeciders;

import app.LoggerUtils;
import app.Watcher;
import entities.EStatus;
import entities.Stock;
import investProps.InvestProps;
import actions.EWalletAction;
import stockDataFetcher.IStockDataProvider;
import warnings.WarningData;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/*
first strategy to decide what action to make.
 */
public class StrategyA implements IActionDecider{
    double startPrice;
    EWalletAction decidedWalletAction;
    double percentageEntry;
    String eStatusStr;
    String cyclesView="";
    String actionStr="";
    double difference;
    double diffPercentage;
    double currentStockPrice;
    final static double CHECK_FACTOR=3;
    static String monitoringData="";
    static String SP_500_SYMBOL="^GSPC";
    static double SP_500_PRICE_CHANGE;
    static double SP_500_THRESHOLD_CHANGE=0.45;

    public StrategyA(Stock stock, InvestProps investProps, double currentStockPrice){
//        currentStockPrice=250;
        decidedWalletAction =EWalletAction.NONE;
        startPrice = stock.getStartPrice();
        percentageEntry = investProps.getPercentageEntry();
        eStatusStr=stock.getStatus();
        this.currentStockPrice=currentStockPrice;
        difference = currentStockPrice - startPrice;
        diffPercentage = difference / startPrice;
    }

    public StrategyA(Stock stock, InvestProps investProps, double currentStockPrice, IStockDataProvider stockDataProvider) {
        //        currentStockPrice=250;
        decidedWalletAction =EWalletAction.NONE;
        startPrice = stock.getStartPrice();
        percentageEntry = investProps.getPercentageEntry();
        eStatusStr=stock.getStatus();
        this.currentStockPrice=currentStockPrice;
        difference = currentStockPrice - startPrice;
        diffPercentage = difference / startPrice;
        SP_500_PRICE_CHANGE=stockDataProvider.getStockChangeInPercentage(SP_500_SYMBOL);
        checkForWarnings();
    }

    private void checkForWarnings() {
        if (Math.abs(SP_500_PRICE_CHANGE)>SP_500_THRESHOLD_CHANGE) {
            if (SP_500_PRICE_CHANGE > 0) {
                String warningString = "\nNOTICE: S&P500 GOES UP BY: " + SP_500_PRICE_CHANGE + ". THAT'S UNUSUAL. ACT WISELY!.";
                WarningData.addWarningData(warningString);
            } else if (SP_500_PRICE_CHANGE <= 0) {
                String warningString = "\nWARNING: S&P500 GOES DOWN BY: " + SP_500_PRICE_CHANGE + " . THAT'S UNUSUAL. ACT CAREFULLY!.";
                WarningData.addWarningData(warningString);
            }
        }
    }

    @Override
    public EWalletAction decideWalletAction(Stock stock) {
        shouldBuy();
        shouldSell();
        shouldCheck();
        printStockActionData(stock);
        return decidedWalletAction;
    }

    private void printStockActionData(Stock stock) {
        String diffPercentageView=new DecimalFormat("##.##").format(diffPercentage*100) +"%";
        if (Integer.parseInt(stock.getCycles())>0) cyclesView=", cycles: "+stock.getCycles();
        if (diffPercentage>0) diffPercentageView="+"+diffPercentageView;
        String daysSinceLastAcquired=setDaysSinceLastAcquired(stock.getAcquiredDate());
        monitoringData+="\n"+actionStr+"stock: "+stock.getName()+", current Price: "+
                (float)currentStockPrice+ ", change since watching: "+diffPercentageView+
                ", start price: "+startPrice+daysSinceLastAcquired+cyclesView;
//        System.out.println("\n"+actionStr+"stock: "+stock.getName()+", current Price: "+
//                (float)currentStockPrice+ ", change since watching: "+diffPercentageView+
//                ", start price: "+startPrice+daysSinceLastAcquired+cyclesView);
    }

    private void shouldCheck() {
        //condition to check to see any alert that its not a good buy although the strategy tell to buy.
        if (diffPercentage >= percentageEntry * CHECK_FACTOR) {
            decidedWalletAction = EWalletAction.CHECK;
        }

    }

    //TODO decide how to know how much to waite before sell/buy. maybe waite for more down/up days before action?.
    private void shouldSell() {
        //condition to sell -when the stock went up by a good percentage since acquired.
        if (!eStatusStr.equals(EStatus.NOT_ACQUIRED.getStringValue())&&
                (currentStockPrice>=startPrice)) {
            decidedWalletAction = EWalletAction.SELL;
            actionStr=EWalletAction.SELL.getActionName()+" ";
        }

        //only to test if it writes to a file.
//        if (startPrice==999){
//            decidedWalletAction = EWalletAction.SELL;
//            actionStr=EWalletAction.SELL.getActionName()+" ";
//        }
    }

    private void shouldBuy() {
        //condition to buy -when the stock went down by a good percentage since start price.
        if (!eStatusStr.equals(EStatus.ACQUIRED.getStringValue()) &&difference < 0 &&
                Math.abs(diffPercentage) >= percentageEntry) {
            decidedWalletAction = EWalletAction.BUY;
            actionStr=EWalletAction.BUY.getActionName()+" ";
        }
    }

    private String setDaysSinceLastAcquired(String acquiredDate) {
        String resStr="";
        if (!acquiredDate.equals("none")){
            LocalDate today = LocalDate.now();
            String[] parsedDate=acquiredDate.split("-");
            LocalDate localAcquiredDate =LocalDate.of(Integer.parseInt(parsedDate[0]),
                    Integer.parseInt(parsedDate[1]),
                    Integer.parseInt(parsedDate[2]));
            long numOfDays= ChronoUnit.DAYS.between(localAcquiredDate, today);
            resStr=", "+numOfDays+ " days since acquired";
            if (numOfDays>60){
                long numOfMonths= ChronoUnit.MONTHS.between(localAcquiredDate, today);
                resStr=", "+numOfMonths+" months since acquired";
            }
        }
        return resStr;
    }
//TODO things to do so it can run independently:
// 1.strategy for updating start price.
// 2.strategy for how much to waite when its passes the percentageEntry before buy/sell.(most of the time more waite would have given better results)
    @Override
    public String printDataForMonitor() {
        return monitoringData;
    }

    public static String getRequiredTickers(){
        return SP_500_SYMBOL;
    }
}
