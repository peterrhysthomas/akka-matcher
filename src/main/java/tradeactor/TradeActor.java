package tradeactor;

import akka.actor.UntypedActor;

public class TradeActor extends UntypedActor{

    private Trade trade;

    public void onReceive(Object message) throws Throwable {
        if (message instanceof TradeNotificationCommand){
             if (trade == null){
                processNewTradeNotification((TradeNotificationCommand) message);
            } else {
                 processAmendTradeNotification((TradeNotificationCommand) message);
             }
        }
    }

    private void processNewTradeNotification(TradeNotificationCommand notification) {
        System.out.println("New Trade Received - " + notification.getId() + ":" + notification.getVersion());
        trade = new Trade(notification);
    }

    private void processAmendTradeNotification(TradeNotificationCommand notification) {
        if (notification.getVersion() <= trade.getVersion()){
            System.out.println("Old version - "+ notification.getId() + ":" + notification.getVersion());
        } else {
            System.out.println("New version - "+ notification.getId() + ":" + notification.getVersion());
            // this state change could be done via an event if we want to do proper event sourcing
            Trade newTrade = new Trade(notification);
            if (isCriticalAmend(trade, newTrade)){
                System.out.println("Critical amend - "+ notification.getId() + ":" + notification.getVersion());
            } else {
                System.out.println("Non critical amend - "+ notification.getId() + ":" + notification.getVersion());
            }
        }

    }

    private boolean isCriticalAmend(Trade trade, Trade newTrade) {
        return (trade.getIsin() != newTrade.getIsin() || trade.getTradeDate() != newTrade.getTradeDate());
    }
}



