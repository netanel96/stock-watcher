package entities;

public class Stock {
    String symbol;
    String Name;
    double startPrice;
    String status;
    String acquiredDate;
    /*
    cycles is the number of cycles passed since startPrice.
    1 cycle - starts with a buy in a low(lower percentageEntry then start price) price
    and end when the price returns to the startPrice.
    for example:1 cycle will pass if an amzn stock was 100$ since start price then it became 97$ then
    it became 100$ again.now 1 cycle passed.
     */
    String cycles;
    public Stock(String symbol, String name, double startPrice, String status, String acquiredDate,String cycles) {
        this.symbol = symbol;
        Name = name;
        this.startPrice = startPrice;
        this.status = status;
        this.acquiredDate=acquiredDate;
        this.cycles=cycles;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", Name='" + Name + '\'' +
                ", startPrice=" + startPrice +
                ", status='" + status + '\'' +
                ", acquiredDate='" + acquiredDate + '\'' +
                ", cycles='" + cycles + '\'' +
                '}';
    }

    public String getCycles() {
        return cycles;
    }

    public String getAcquiredDate() {
        return acquiredDate;
    }

    public String getStatus() {
        return status;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return Name;
    }

    public double getStartPrice() {
        return startPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCycles(String cycles) {
        this.cycles = cycles;
    }
}
