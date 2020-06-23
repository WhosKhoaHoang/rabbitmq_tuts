package com.khoa.publish_subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;


public class ReceiveLogs {

  private static final String EXCHANGE_NAME = "logs";

  public static void main(String[] args) throws Exception {
    // EmitLog is tied to ReceiveLogs through the
    // common exchange that they both connect to

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    // When we supply no parameters to queueDeclare() we create
    // a non-durable, exclusive, autodelete queue with a generated name:
    String queueName = channel.queueDeclare().getQueue();

    // The relationship between an exchange and a queue is called a binding
    channel.queueBind(queueName, EXCHANGE_NAME, "");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [x] Received '" + message + "'");
    };
    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
  }
}
