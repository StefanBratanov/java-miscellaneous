package jms;

import lib.Suppliers;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.lang.IllegalStateException;

class Consumer implements MessageListener, AutoCloseable {

    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private final Connection connection;
    private final Session session;
    private final MessageConsumer messageConsumer;

    Consumer(String queueName) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        this.connection = Suppliers.tryGet(() -> {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            return connection;
        });
        this.session = Suppliers.tryGet(() ->
                connection.createSession(false, Session.AUTO_ACKNOWLEDGE));
        Destination destination = Suppliers.tryGet(() -> session.createQueue(queueName));
        this.messageConsumer = Suppliers.tryGet(() -> {
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
            return consumer;
        });
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage txtMsg = (TextMessage) message;
            String messageText = Suppliers.tryGet(txtMsg::getText);
            System.out.println("Message received from the producer: " + messageText);
        } else {
            throw new IllegalStateException("Unknown type of jms message received");
        }
    }

    @Override
    public void close() throws Exception {
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
