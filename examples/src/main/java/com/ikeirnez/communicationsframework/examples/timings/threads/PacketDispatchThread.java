package com.ikeirnez.communicationsframework.examples.timings.threads;

import com.ikeirnez.communicationsframework.examples.timings.Main;
import com.ikeirnez.communicationsframework.examples.timings.PacketTime;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Dispatches a specified amount of packets pausing every for a small amount of time after each packet is sent.
 */
public class PacketDispatchThread implements Runnable {

    public AtomicInteger AMOUNT_SENT = new AtomicInteger(0);

    @Override
    public void run() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < Main.AMOUNT_TO_SEND; i++) {
            boolean last = i == Main.AMOUNT_TO_SEND - 1; // determines if this is the last packet being sent so that the other side will work out averages and stuff
            Main.clientConnection.sendPacket(new PacketTime(System.nanoTime(), last)); // actually send
            AMOUNT_SENT.incrementAndGet();

            try {
                Thread.sleep(10); // pause to prevent overloading the listeners & therefore getting an incorrect average
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Sent " + Main.numberFormatGeneral.format(AMOUNT_SENT.get()) + " packets in " + Main.numberFormatGeneral.format((System.currentTimeMillis() - start) / 1000) + "s.");
    }
}
