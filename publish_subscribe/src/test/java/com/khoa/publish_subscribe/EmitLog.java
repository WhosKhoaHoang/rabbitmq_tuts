package com.khoa.publish_subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class EmitLog {

  private static final String EXCHANGE_NAME = "logs";

  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {

      // "fanout" will cause the exchange to broadcast all
      // the messages it receives to all queues it knows.
      channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

      String message = args.length < 1 ? "info: Hello World!" :
                                         String.join(" ", args);

      channel.basicPublish(EXCHANGE_NAME, "", null,
          message.getBytes("UTF-8"));
      System.out.println(" [x] Sent '" + message + "'");
    }
  }
}
