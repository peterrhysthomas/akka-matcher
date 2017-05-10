package tradeactor;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
//import akkaexample.TradeMatchingCoordinator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.Duration;

public class TradeActorTest {
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
    public void testAmend() {
        new JavaTestKit(system) {{
            final TradeNotificationCommand command1 = new TradeNotificationCommand("123", 1, "ISIN1", "01012017", "foo");
            final TestActorRef<TradeActor> trade123 =
                    TestActorRef.create(system, Props.create(TradeActor.class), command1.getId());
            trade123.tell(command1, getTestActor());
            trade123.tell(command1, getTestActor());
            final TradeNotificationCommand command2 = new TradeNotificationCommand("123", 2, "ISIN1", "01012017", "bar");
            trade123.tell(command2, getTestActor());
            trade123.tell(command1, getTestActor());

        }};
    }

}