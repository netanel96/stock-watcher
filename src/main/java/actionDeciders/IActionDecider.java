package actionDeciders;

import entities.Stock;
import investProps.InvestProps;
import wallet.EWalletAction;

public interface IActionDecider {
    public EWalletAction decideWalletAction(Stock stock);
    public String printDataForMonitor();
}
