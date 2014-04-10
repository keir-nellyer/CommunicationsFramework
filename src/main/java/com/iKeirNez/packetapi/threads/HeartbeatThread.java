package com.iKeirNez.packetapi.threads;

import com.iKeirNez.packetapi.api.HookType;
import com.iKeirNez.packetapi.implementations.IConnectionManager;
import com.iKeirNez.packetapi.implementations.IServerConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public class HeartbeatThread extends Thread implements Runnable {

    public static long HEARTBEAT_INTERVAL = 10000;
    public static List<IConnectionManager> connections = Collections.synchronizedList(new ArrayList<>());

    public HeartbeatThread(){
        super("Network Heartbeat Thread");
        start();
    }

    public void addConnectionManager(IConnectionManager connectionManager){
        connections.add(connectionManager);
    }

    public void removeConnectionManager(IConnectionManager connectionManager){
        connections.remove(connectionManager);
    }

    @Override
    public void run(){
        while (true){
            try {
                Thread.sleep(HEARTBEAT_INTERVAL);

                connections.forEach(cm -> cm.getConnections().stream().filter(c -> c instanceof IServerConnection).map(c -> (IServerConnection) c).forEach(s -> {
                    if ((System.currentTimeMillis() - s.lastHeartbeat) / 1000 > 10 && !s.offline){
                        s.offline = true;
                        cm.callHook(s, HookType.LOST_CONNECTION);
                    }
                }));
            } catch (InterruptedException e) {}
        }
    }

}
