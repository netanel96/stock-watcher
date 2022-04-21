package actions.testActions;

import entities.Stock;
import investProps.InvestProps;

public interface ITestDataHandler {
    void updateTestData(Stock stock, InvestProps investProps);
}
