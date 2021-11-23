package wallet;

import entities.Stock;

/*make the action in a 'wallet' for now just a json file with the data:
string:of the current value.
string:of the action in format:date,action,symbol\n.
* */
public class WalletActionsImp implements IWalletAction {
    private String walletActionDataLogs ="";

    public  void sendAction(EWalletAction eWalletAction, Stock stock){
        walletActionDataLogs +="\n"+eWalletAction.getActionName()+" stock of "+stock.getSymbol();
    }

    @Override
    public String printAllWalletActions() {
        return walletActionDataLogs;
    }
}
