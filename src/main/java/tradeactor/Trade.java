package tradeactor;

public class Trade {
    private String id;
    private int version;
    private String isin;
    private String tradeDate;
    private String nonCriticalData;

    public Trade(TradeNotificationCommand notification) {
        this.id = notification.getId();
        this.version = notification.getVersion();
        this.isin = notification.getIsin();
        this.tradeDate = notification.getTradeDate();
        this.nonCriticalData = notification.getNonCriticalData();
    }

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getIsin() {
        return isin;
    }

    public String getTradeDate() {
        return tradeDate;
    }
}
