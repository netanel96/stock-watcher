package actions;

import entities.Stock;

public interface IWalletAction {
    public void sendAction(EWalletAction eWalletAction, Stock stock);

    public String printAllWalletActions();
}
