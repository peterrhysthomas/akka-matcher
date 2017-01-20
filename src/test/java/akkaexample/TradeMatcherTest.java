package akkaexample;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import scala.concurrent.duration.Duration;

import static org.junit.Assert.assertEquals;

public class TradeMatcherTest{
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        system.shutdown();
        system.awaitTermination(Duration.create("10 seconds"));
    }

    @Test
    public void canAddATrade() {
        new JavaTestKit(system) {{
            //TODO - can we create the actor ref once to avoid the hardcoding of the name?
            final TestActorRef<TradeMatcher> matcher =
                    TestActorRef.create(system, Props.create(TradeMatcher.class), "tradematcher1");

            final Trade trade = new Trade("123", "EX999");
            matcher.tell(new NewTradeMessage(trade), getTestActor());
            matcher.tell(new GetTradesMessage(), getTestActor());

            final TradesMessage trades = expectMsgClass(TradesMessage.class);

            new Within(duration("10 seconds")) {
                protected void run() {
                    assertEquals(trade, trades.getTrades().get(0));
                }
            };
        }};
    }

    @Test
    public void canAddACcpTrade() {
        new JavaTestKit(system) {{
            final TestActorRef<TradeMatcher> matcher =
                    TestActorRef.create(system, Props.create(TradeMatcher.class), "tradematcher2");

            final CcpTrade ccpTrade = new CcpTrade("EX999");
            matcher.tell(new NewCcpTradeMessage(ccpTrade), getTestActor());
            matcher.tell(new GetCcpTradesMessage(), getTestActor());

            final CcpTradesMessage ccpTrades = expectMsgClass(CcpTradesMessage.class);

            new Within(duration("10 seconds")) {
                protected void run() {
                    assertEquals(ccpTrade, ccpTrades.getCcpTrades().get(0));
                }
            };
        }};
    }

    @Test
    public void canCancelATrade() {
        new JavaTestKit(system) {{
            final TestActorRef<TradeMatcher> matcher =
                    TestActorRef.create(system, Props.create(TradeMatcher.class), "tradematcher3");

            final Trade trade1 = new Trade("123", "EX999");
            final Trade trade2 = new Trade("456", "EX888");
            matcher.tell(new NewTradeMessage(trade1), getTestActor());
            matcher.tell(new NewTradeMessage(trade2), getTestActor());
            matcher.tell(new CancelTradeMessage(trade1), getTestActor());
            matcher.tell(new GetTradesMessage(), getTestActor());

            final TradesMessage trades = expectMsgClass(TradesMessage.class);

            new Within(duration("10 seconds")) {
                protected void run() {
                    assertEquals(1, trades.getTrades().size());
                    assertEquals(trade2, trades.getTrades().get(0));
                }
            };
        }};
    }

    @Test
    public void canPerformAMatch() {
        new JavaTestKit(system) {
            {
                final TestActorRef<TradeMatcher> matcher =
                        TestActorRef.create(system, Props.create(TradeMatcher.class), "tradematcher4");
                final Trade trade = new Trade("123", "EX999");
                matcher.tell(new NewTradeMessage(trade), getTestActor());
                final CcpTrade ccpTrade = new CcpTrade("EX999");
                matcher.tell(new NewCcpTradeMessage(ccpTrade), getTestActor());

                matcher.tell(new GetUnmatchedMessage(), getTestActor());

                final MatchResultsMessage results = expectMsgClass(MatchResultsMessage.class);

                new Within(duration("10 seconds")) {
                    protected void run() {
                        assertEquals(0, results.getUnmatchedItems().getTrades().size());
                        assertEquals(0, results.getUnmatchedItems().getCcpTrades().size());
                    }
                };
            }
        };
    }

    @Test
    public void canIdentifyAnUnmatchedCcpTrade(){
        new JavaTestKit(system) {
            {
                final TestActorRef<TradeMatcher> matcher =
                        TestActorRef.create(system, Props.create(TradeMatcher.class), "tradematcher5");
                final CcpTrade ccpTrade = new CcpTrade("EX999");
                matcher.tell(new NewCcpTradeMessage(ccpTrade), getTestActor());
                matcher.tell(new GetUnmatchedMessage(), getTestActor());

                final MatchResultsMessage results = expectMsgClass(MatchResultsMessage.class);

                new Within(duration("10 seconds")) {
                    protected void run() {
                        assertEquals(ccpTrade, results.getUnmatchedItems().getCcpTrades().get(0));
                    }
                };
            }
        };
    }

    @Test
    public void canIdentifyAnUnmatchedTrade(){
        new JavaTestKit(system) {
            {
                final TestActorRef<TradeMatcher> matcher =
                        TestActorRef.create(system, Props.create(TradeMatcher.class), "tradematcher6");
                final Trade trade = new Trade("123", "EX999");
                matcher.tell(new NewTradeMessage(trade), getTestActor());
                matcher.tell(new GetUnmatchedMessage(), getTestActor());

                final MatchResultsMessage results = expectMsgClass(MatchResultsMessage.class);

                new Within(duration("10 seconds")) {
                    protected void run() {
                        assertEquals(trade, results.getUnmatchedItems().getTrades().get(0));
                    }
                };
            }
        };
    }

    @Test
    public void canIdentifyAnUnmatchPostCancel(){
        new JavaTestKit(system) {
            {
                final TestActorRef<TradeMatcher> matcher =
                        TestActorRef.create(system, Props.create(TradeMatcher.class), "tradematcher7");
                final Trade trade = new Trade("123", "EX999");
                matcher.tell(new NewTradeMessage(trade), getTestActor());
                final CcpTrade ccpTrade = new CcpTrade("EX999");
                matcher.tell(new NewCcpTradeMessage(ccpTrade), getTestActor());
                matcher.tell(new CancelTradeMessage(trade), getTestActor());

                matcher.tell(new GetUnmatchedMessage(), getTestActor());

                final MatchResultsMessage results = expectMsgClass(MatchResultsMessage.class);

                new Within(duration("10 seconds")) {
                    protected void run() {
                        assertEquals(ccpTrade, results.getUnmatchedItems().getCcpTrades().get(0));
                    }
                };
            }
        };
    }
}