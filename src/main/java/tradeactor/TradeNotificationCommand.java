package tradeactor;

import java.io.Serializable;

public class TradeNotificationCommand implements Serializable {
    private String id;
    private int version;
    private String isin;
    private String tradeDate;
    private String nonCriticalData;
    // other attributes

    public TradeNotificationCommand(String id, int version, String isin, String tradeDate, String nonCriticalData) {
        this.id = id;
        this.version = version;
        this.isin = isin;
        this.tradeDate = tradeDate;
        this.nonCriticalData = nonCriticalData;
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

    public String getNonCriticalData() {
        return nonCriticalData;
    }
}
