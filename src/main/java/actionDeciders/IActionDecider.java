package actionDeciders;

import entities.Stock;
import actions.EWalletAction;

public interface IActionDecider {
    EWalletAction decideWalletAction(Stock stock);
    String printDataForMonitor();
}
