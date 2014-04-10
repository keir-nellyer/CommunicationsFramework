package com.iKeirNez.packetapi.threads;

import com.iKeirNez.packetapi.HookType;
import com.iKeirNez.packetapi.connections.ConnectionManager;
import com.iKeirNez.packetapi.connections.ServerConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public class HeartbeatThread extends Thread implements Runnable {

    public static long HEARTBEAT_INTERVAL = 10000;
    public static List<ConnectionManager> connections = Collections.synchronizedList(new ArrayList<>());

    public HeartbeatThread(){
        super("Network Heartbeat Thread");
        start();
    }

    public void addConnectionManager(ConnectionManager connectionManager){
        connections.add(connectionManager);
    }

    public void removeConnectionManager(ConnectionManager connectionManager){
        connections.remove(connectionManager);
    }

    @Override
    public void run(){
        while (true){
            try {
                Thread.sleep(HEARTBEAT_INTERVAL);

                connections.forEach(cm -> cm.getConnections().stream().filter(c -> c instanceof ServerConnection).map(c -> (ServerConnection) c).forEach(s -> {
                    if ((System.currentTimeMillis() - s.lastHeartbeat) / 1000 > 10 && !s.offline){
                        s.offline = true;
                        cm.callHook(s, HookType.LOST_CONNECTION);
                    }
                }));
            } catch (InterruptedException e) {}
        }
    }

}
