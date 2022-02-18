package entities;

public class TestData {
    private double valueInPercentage;
    private String symbolHistory;
    private static String symbolHistoryDelimiter=",";
/*
lastSearchDate -  the date of the last time i have iterated over all the s&p500 to find candidate
stocks which are those that have many ups and downs around the same price and is a company that is in a good
place in regard of its earning in the past years.
 */

    public TestData(double valueInPercentage, String symbolHistory) {
        this.valueInPercentage = valueInPercentage;
        this.symbolHistory = symbolHistory;
    }

    public double getValueInPercentage() {
        return valueInPercentage;
    }

    public void setValueInPercentage(double valueInPercentage) {
        this.valueInPercentage = valueInPercentage;
    }

    public String getSymbolHistory() {
        return symbolHistory;
    }

    public static String getSymbolHistoryDelimiter(){
        return symbolHistoryDelimiter;
    }
}
