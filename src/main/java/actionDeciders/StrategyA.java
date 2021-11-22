package actionDeciders;

import entities.EStatus;
import entities.Stock;
import investProps.InvestProps;
import wallet.EWalletAction;
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
    static String monitoringData="";
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
    @Override
    public EWalletAction decideWalletAction(Stock stock) {
        shouldBuy();
        shouldCheck();
        shouldSell();
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
        if (diffPercentage >= percentageEntry * 2) {
            decidedWalletAction = EWalletAction.CHECK;
        }
    }

    private void shouldSell() {
        //condition to sell -when the stock went up by a good percentage.
        if (!eStatusStr.equals(EStatus.NOT_ACQUIRED.getStringValue())&&
                difference > 0 && diffPercentage >= percentageEntry) {
            decidedWalletAction = EWalletAction.SELL;
            actionStr=EWalletAction.SELL.getActionName()+" ";
        }
    }

    private void shouldBuy() {
        //condition to buy -when the stock went down by a good percentage.
        if (!eStatusStr.equals(EStatus.ACQUIRED.getStringValue()) &&difference < 0 &&
                diffPercentage >= percentageEntry) {
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

    @Override
    public String printDataForMonitor() {
        return monitoringData;
    }
}
