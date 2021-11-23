package investProps;

import java.util.ArrayList;
import java.util.List;

public class SimpleInvestPropsFetcher implements InvestPropsFetcher{
    @Override
    public List<IInvestStrategyPropsReader> getAllInvestStrategyProps() {
        IInvestStrategyPropsReader iInvestStrategyPropsReader=new DefaultInvestStrategyProps();
        List<IInvestStrategyPropsReader> listInvestProps=new ArrayList<>();
        listInvestProps.add(iInvestStrategyPropsReader);
        return listInvestProps;
    }
}
