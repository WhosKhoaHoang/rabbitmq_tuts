package com.khoa.hello_world;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Send {

  private final static String QUEUE_NAME = "hello";

  public static void main(String[] args) {
    System.out.println("Test123");
  }

}
