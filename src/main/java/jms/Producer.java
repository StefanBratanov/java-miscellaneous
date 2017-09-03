package jms;

import com.google.common.collect.Sets;
import lib.Runnables;
import lib.Suppliers;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Set;

class Producer implements AutoCloseable {

    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private final Connection connection;
    private final Session session;
    private final Set<MessageProducer> messageProducers;

    Producer() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        this.connection = Suppliers.tryGet(() -> {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            return connection;
        });
        this.session = Suppliers.tryGet(() ->
                connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
        this.messageProducers = Sets.newConcurrentHashSet();
    }

    void sendMessage(String message, String queueName) {
        Runnables.tryRun(() -> {
            Destination destination = session.createQueue(queueName);
            MessageProducer messageProducer = session.createProducer(destination);
            messageProducers.add(messageProducer);
            TextMessage textMessage = session.createTextMessage(message);
            messageProducer.send(textMessage);
        });
    }

    @Override
    public void close() throws Exception {
        messageProducers.forEach((messageProducer) ->
                Runnables.tryRun(messageProducer::close));
        session.close();
        connection.close();
    }
}
