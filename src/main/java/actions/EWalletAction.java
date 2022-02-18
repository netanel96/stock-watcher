package actions;

public  enum EWalletAction {
    BUY(0,"BUY"),SELL(1,"SELL"),CHECK(2,"CHECK"),NONE(3,"NONE");


    private int walletAction;

    private String actionName;
    public int getWalletActionValue() {
        return walletAction;
    }
    public String getActionName() {
        return actionName;
    }

    EWalletAction(int i,String name) {
        this.walletAction=i;
        this.actionName=name;
    }
}
