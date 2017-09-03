package jms;

import lib.Runnables;
import org.apache.activemq.broker.BrokerService;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {

    private static final String QUEUE_NAME = "TEST_QUEUE";

    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();

        // configure the broker
        broker.addConnector("tcp://localhost:61616");
        broker.start();

        try (Producer producer = new Producer();
             Consumer consumer = new Consumer(QUEUE_NAME)) {
            IntStream.rangeClosed(1, 10)
                    .forEach(value -> {
                        System.out.println("Sending message to consumer");
                        producer.sendMessage("Hello: " + value, QUEUE_NAME);
                        Runnables.tryRun(() -> TimeUnit.SECONDS.sleep(2));
                    });
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

    }
}
