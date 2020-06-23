package com.khoa.publish_subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class EmitLog {

  private static final String EXCHANGE_NAME = "logs";

  public static void main(String[] args) throws Exception {
    // EmitLog is tied to ReceiveLogs through the
    // common exchange that they both connect to
    // - Basically, the flow of this example is that ReceiveLogs
    //   creates a queue (which doesn't need to be named) that is
    //   bound to the same exchange established here in EmitLog
    //   (which we've called "logs"). EmitLog then sends messages
    //   TO THE EXCHANGE, which then gets sent to all the queues
    //   within the exchange due to its type being "fanout".
    // - If no queue is bound to the "logs" exchange, then any messages
    //   sent by EmitLog here are lost. Since ReceiveLogs binds a queue to
    //   the "logs" exchange, you need to run ReceiveLogs first if you expect
    //   those messages to go anywhere. Also, run multiple instances of
    //   ReceiveLogs to see how multiple queues get bound to the "logs"
    //   exchange and how they all receive messages from EmitLog.
    // - Be sure to enable "Allow Parallel Runs" for ReceiveLogs' run configs
    //   so that you can run multiple instances of ReceiveLogs and get a better
    //   sense of how this pattern works.

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
