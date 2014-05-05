package com.ikeirnez.communicationsframework.examples.timings;

import com.ikeirnez.communicationsframework.api.connection.ClientConnection;
import com.ikeirnez.communicationsframework.api.connection.Connection;
import com.ikeirnez.communicationsframework.api.connection.ConnectionManager;
import com.ikeirnez.communicationsframework.api.connection.ConnectionManagerFactory;
import com.ikeirnez.communicationsframework.api.connection.ServerConnection;
import com.ikeirnez.communicationsframework.api.Callback;
import com.ikeirnez.communicationsframework.api.HookType;
import com.ikeirnez.communicationsframework.examples.timings.threads.PacketDispatchThread;
import com.ikeirnez.communicationsframework.examples.timings.threads.ProgressMonitorThread;

import java.text.NumberFormat;

/**
 * Main class for determining the average time for a packet to be sent & received.
 */
public class Main {

  public static final int AMOUNT_TO_SEND = 5000; // amount of packets to send
  public static final NumberFormat numberFormatDecimal = NumberFormat.getNumberInstance(); // for rounding percentages & averages
  public static final NumberFormat numberFormatGeneral = NumberFormat.getNumberInstance(); // for display things without decimals

  public static ConnectionManager connectionManager = ConnectionManagerFactory.getNewConnectionManager(Main.class.getClassLoader());
  public static ServerConnection serverConnection = connectionManager.newServerConnection("localhost", 25565);
  public static ClientConnection clientConnection = connectionManager.newClientConnection("localhost", 25565);

  static { // set number format options
    numberFormatDecimal.setMaximumFractionDigits(2);
    numberFormatDecimal.setMinimumFractionDigits(2);

    numberFormatGeneral.setMaximumFractionDigits(0);
  }

  public static void main(String[] args) {
    System.out.println("Connecting...");

    connectionManager.registerListener(new TimingsListener()); // register listener to receive the packet
    connectionManager.addHook(HookType.CONNECTED, new Callback<Connection>() { // listener for when the connection is established
      @Override
      public void call(Connection connection) {
        if (connection.equals(clientConnection)) {
          System.out.println("Connected, sending " + numberFormatGeneral.format(Main.AMOUNT_TO_SEND) + " packets.");
          PacketDispatchThread packetDispatchThread = new PacketDispatchThread();

          new Thread(new ProgressMonitorThread(packetDispatchThread)).start(); // start monitoring thread
          new Thread(packetDispatchThread).start(); // start dispatch thread
        }
      }
    });

    // start connecting
    serverConnection.bind();
    clientConnection.connect();
  }

}
