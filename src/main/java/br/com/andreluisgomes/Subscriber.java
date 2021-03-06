package br.com.andreluisgomes;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.io.IOException;


public class Subscriber {

    private static final Logger logger = Logger.getLogger(Subscriber.class);

	public static void main(String[] args) throws JMSException {

		//getting topic name
        String topicName = null;
        try {
            topicName = args[0];
            System.out.println("Preparing for listening topic : " + topicName);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Erro ao recuperar o nome do Topico");
        }

        //TODO - extract to a propertie file
        // Getting JMS connection from the server
        String url = "active_mq_host:61616";

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topic = session.createTopic(topicName);
        System.out.println("Session create with : " + topic.getTopicName());

        MessageConsumer consumer = session.createConsumer(topic);

        MessageListener listner = new MessageListener() {
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("Received message :" + textMessage.getText() + "'");
                    }
                } catch (JMSException e) {
                    System.out.println("Caught:" + e);
                    e.printStackTrace();
                }
            }
        };
        consumer.setMessageListener(listner);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.close();

    }

}
