package akkaexample;

import akka.actor.UntypedActor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeMatcher extends UntypedActor {
    private Map<String, Trade> trades = new HashMap<String, Trade>();
    private Map<String, CcpTrade> ccpTrades = new HashMap<String, CcpTrade>();

    public void onReceive(Object message) throws Throwable {
        if (message instanceof NewTradeMessage){
            Trade trade = ((NewTradeMessage) message).getTrade();
            trades.put(trade.getExchangeReference(), trade);

        } else if (message instanceof CancelTradeMessage){
            trades.remove(((CancelTradeMessage) message).getTrade().getExchangeReference());

        } else if (message instanceof NewCcpTradeMessage){
            CcpTrade ccpTrade = ((NewCcpTradeMessage) message).getTrade();
            ccpTrades.put(ccpTrade.getExchangeReference(), ccpTrade);

        } else if (message instanceof GetTradesMessage){
            getSender().tell(new TradesMessage(new ArrayList<Trade>(trades.values())), getSelf());
        } else if (message instanceof GetCcpTradesMessage){
            getSender().tell(new CcpTradesMessage(new ArrayList<CcpTrade>(ccpTrades.values())), getSelf());
        } else if (message instanceof GetUnmatchedMessage){
            getSender().tell(new MatchResultsMessage(performMatch()), getSelf());
        }
        else unhandled(message);
    }

    private UnmatchedItems performMatch() {
        List<Trade> unmatchedTrades = new ArrayList<Trade>();
        List<CcpTrade> unmatchedCcpTrades = new ArrayList<CcpTrade>();

        for (Trade trade : trades.values()){
            if (!ccpTrades.containsKey(trade.getExchangeReference())){
                unmatchedTrades.add(trade);
            }
        }

        for (CcpTrade ccpTrade : ccpTrades.values()){
            if (!trades.containsKey(ccpTrade.getExchangeReference())){
                unmatchedCcpTrades.add(ccpTrade);
            }
        }

        return new UnmatchedItems(unmatchedTrades, unmatchedCcpTrades);
    }
}

class UnmatchedItems {
    private List<Trade> trades = new ArrayList<Trade>();
    private List<CcpTrade> ccpTrades = new ArrayList<CcpTrade>();
    public UnmatchedItems(List<Trade> trades, List<CcpTrade> ccpTrades){
        this.trades = trades;
        this.ccpTrades = ccpTrades;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public List<CcpTrade> getCcpTrades() {
        return ccpTrades;
    }
}
