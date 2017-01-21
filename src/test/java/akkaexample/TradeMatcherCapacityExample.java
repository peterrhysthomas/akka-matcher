package akkaexample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class TradeMatcherCapacityExample {

    public static void main(String[] args){
        TradeMatcherCapacityExample example = new TradeMatcherCapacityExample();
        example.runVolumeTest();
    }

    public void runVolumeTest() {
        ActorSystem system = ActorSystem.create();
        final TestActorRef<TradeMatcher> matcher =
                TestActorRef.create(system, Props.create(TradeMatcher.class), "tradematcher");

        while(true){
            for (int i=1;i < 1000; i++){
                matcher.tell(tradeMsgGenerator(), ActorRef.noSender());
                matcher.tell(ccpTradeMsgGenerator(), ActorRef.noSender());
            }
            matcher.tell(new GetUnmatchedMessage(), ActorRef.noSender());
        }
    }

    private static Integer tradeExchangeReference = 0;

    private NewTradeMessage tradeMsgGenerator() {
        tradeExchangeReference += 2;
        return new NewTradeMessage(new Trade(randomAlphabetic(10), tradeExchangeReference.toString()));
    }

    private static Integer ccpTradeExchangeReference = 0;

    private NewCcpTradeMessage ccpTradeMsgGenerator() {
        ccpTradeExchangeReference += 3;
        return new NewCcpTradeMessage(new CcpTrade(ccpTradeExchangeReference.toString()));
    }

}
