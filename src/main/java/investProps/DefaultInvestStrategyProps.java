package investProps;

public class DefaultInvestStrategyProps implements IInvestStrategyPropsReader {
    private double percentageEntry=0.03;
    @Override
    public double getPercentageEntry() {
        return percentageEntry;
    }
}
