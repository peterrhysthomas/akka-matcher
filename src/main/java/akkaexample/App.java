package akkaexample;

import java.io.Serializable;
import java.util.List;

public class App
{

}

class Trade implements Serializable{
    private String reference;
    private String exchangeReference;
    public Trade(String reference, String exchangeReference) {
        this.reference = reference;
        this.exchangeReference = exchangeReference;
    }
    public String getReference() {
        return reference;
    }
    public String getExchangeReference() {
        return exchangeReference;
    }
}

class CcpTrade implements Serializable{
    private String exchangeReference;
    public CcpTrade(String exchangeReference) {
        this.exchangeReference = exchangeReference;
    }
    public String getExchangeReference() {
        return exchangeReference;
    }
}

class NewTradeMessage implements Serializable {
    private Trade trade;
    public NewTradeMessage(Trade trade){
        this.trade = trade;
    }
    public Trade getTrade() {
        return trade;
    }
}
class CancelTradeMessage implements Serializable {
    private Trade trade;
    public CancelTradeMessage(Trade trade){
        this.trade = trade;
    }
    public Trade getTrade() {
        return trade;
    }
}

class NewCcpTradeMessage implements Serializable {
    private CcpTrade trade;
    public NewCcpTradeMessage(CcpTrade trade){
        this.trade = trade;
    }
    public CcpTrade getTrade() {
        return trade;
    }
}

class GetTradesMessage implements Serializable {}
class GetCcpTradesMessage implements Serializable {}

class TradesMessage implements Serializable {
    private List<Trade> trades;
    public TradesMessage(List<Trade> trades){
        this.trades = trades;
    }
    public List<Trade> getTrades() {
        return trades;
    }
}

class CcpTradesMessage implements Serializable {
    private List<CcpTrade> ccpTrades;
    public CcpTradesMessage(List<CcpTrade> ccpTrades){
        this.ccpTrades = ccpTrades;
    }
    public List<CcpTrade> getCcpTrades() {
        return ccpTrades;
    }
}

class GetUnmatchedMessage implements Serializable {}

class MatchResultsMessage implements Serializable {
    private UnmatchedItems unmatchedItems;
    public MatchResultsMessage(UnmatchedItems unmatchedItems){
        this.unmatchedItems = unmatchedItems;
    }

    public UnmatchedItems getUnmatchedItems() {
        return unmatchedItems;
    }
}
