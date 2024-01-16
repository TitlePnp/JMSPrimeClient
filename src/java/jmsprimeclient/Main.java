/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package jmsprimeclient;

import java.util.Scanner;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;


/**
 *
 * @author tleku
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/SimpleJMSQueue")
    private static Queue simpleJMSQueue;
            
    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        MessageProducer producer = null;
        TextMessage message = null;
        TextListener listener = null;
        
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(
                    false,
                    Session.AUTO_ACKNOWLEDGE
            );
            producer = session.createProducer(simpleJMSQueue);
            consumer = session.createConsumer(simpleJMSQueue);
            listener = new TextListener();
            consumer.setMessageListener(listener);
            connection.start();
            
            Scanner sc = new Scanner(System.in);
            
            while (true) {
                System.out.println("Enter two numbers. Use ',' to seperate each number. To end the program press enter");
                String primeRange = sc.nextLine();
                message = session.createTextMessage();
                message.setText(primeRange);
                System.out.println("Sending Message: " + message.getText());
                producer.send(message);
            }
            
        } catch (JMSException e) {
            System.err.println("Exception occrurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    
                }
            }
        }
    }
    
}
