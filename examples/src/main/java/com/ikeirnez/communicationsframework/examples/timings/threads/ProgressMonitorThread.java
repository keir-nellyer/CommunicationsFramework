package com.ikeirnez.communicationsframework.examples.timings.threads;

import com.ikeirnez.communicationsframework.examples.timings.Main;

/**
 * Prints the current progress every second.
 */
public class ProgressMonitorThread implements Runnable {

    private PacketDispatchThread packetDispatchThread;

    public ProgressMonitorThread(PacketDispatchThread packetDispatchThread) {
        this.packetDispatchThread = packetDispatchThread;
    }

    @Override
    public void run() {
        while (true) {
            double percent = (packetDispatchThread.AMOUNT_SENT.get() * 100.0) / Main.AMOUNT_TO_SEND;
            System.out.println(Main.numberFormatDecimal.format(percent) + "% done...");

            if (percent < 100) {
                try {
                    Thread.sleep(1000); // sleep for a second before trying to work out the percentage again
                } catch (InterruptedException e) {
                    break;
                }
            } else {
                break; // break the while loop if we are finished
            }
        }
    }
}
