package entities;

public  enum EStatus {
    ACQUIRED("ACQUIRED"),NOT_ACQUIRED("NOT_ACQUIRED");


    private String eStatus;

    public String getStringValue() {
        return eStatus;
    }

    EStatus(String i) {
        this.eStatus=i;
    }

}
