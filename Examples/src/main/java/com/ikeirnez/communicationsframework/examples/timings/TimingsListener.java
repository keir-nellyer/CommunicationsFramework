package com.ikeirnez.communicationsframework.examples.timings;

import com.ikeirnez.communicationsframework.api.packets.PacketHandler;
import com.ikeirnez.communicationsframework.api.packets.PacketListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Listener to receiving packets & calculate the average time
 */
public class TimingsListener implements PacketListener {

  private final List<Long> values = new ArrayList<>();

  @PacketHandler
  public void onTimePacket(PacketTime packet) {
    values.add(System.nanoTime() - packet.getNanosSent());

    if (packet.isLast()) {
      long sum = 0;
      for (Long value : values) {
        sum += value;
      }

      long average = sum / values.size(); // works out the average, simple math
      values.clear();

      System.out.println("Average was " + Main.numberFormatGeneral.format(average) + " nanoseconds (" + Main.numberFormatDecimal.format(average * 0.000001) + " milliseconds).");

      try {
        Main.connectionManager.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.exit(0); // finally exit when we're done
    }
  }

}
