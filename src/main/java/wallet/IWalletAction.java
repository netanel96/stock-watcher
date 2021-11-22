package wallet;

import entities.EStatus;
import entities.Stock;

public interface IWalletAction {
    public void sendAction(EWalletAction eWalletAction, Stock stock);

    public String printAllWalletActions();
}
