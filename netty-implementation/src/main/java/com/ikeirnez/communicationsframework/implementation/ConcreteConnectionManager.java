package com.ikeirnez.communicationsframework.implementation;

import com.ikeirnez.communicationsframework.api.Callback;
import com.ikeirnez.communicationsframework.api.HookType;
import com.ikeirnez.communicationsframework.api.config.connection.ClientConnectionConfig;
import com.ikeirnez.communicationsframework.api.config.connection.ServerConnectionConfig;
import com.ikeirnez.communicationsframework.api.connection.Connection;
import com.ikeirnez.communicationsframework.api.connection.ConnectionManager;
import com.ikeirnez.communicationsframework.api.packets.Packet;
import com.ikeirnez.communicationsframework.api.packets.PacketHandler;
import com.ikeirnez.communicationsframework.api.packets.PacketListener;
import com.ikeirnez.communicationsframework.implementation.standard.connection.ConcreteClientConnection;
import com.ikeirnez.communicationsframework.implementation.standard.connection.ConcreteConnection;
import com.ikeirnez.communicationsframework.implementation.standard.connection.ConcreteServerConnection;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public class ConcreteConnectionManager implements ConnectionManager {

    public final List<ConcreteConnection> connections = Collections.synchronizedList(new ArrayList<ConcreteConnection>());
    public final ClassLoader classLoader;
    private final Map<Class<? extends Packet>, List<PacketListener>> listeners = new ConcurrentHashMap<>();
    private final Map<HookType, List<Callback<Connection>>> hooks = new ConcurrentHashMap<>();

    /**
     * @param classLoader The class loader (can be gotten with getClass#getClassLoader)
     * @deprecated see {@link com.ikeirnez.communicationsframework.api.connection.ConnectionManagerFactory#getNewNettyConnectionManager(ClassLoader)}
     */
    @Deprecated
    public ConcreteConnectionManager(ClassLoader classLoader) {
        this.classLoader = classLoader;

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                close(); // todo test
            }
        }));
    }

    @Override
    public ConcreteClientConnection newClientConnection(ClientConnectionConfig clientConnectionConfig) {
        return new ConcreteClientConnection(this, clientConnectionConfig);
    }

    @Override
    public ConcreteServerConnection newServerConnection(ServerConnectionConfig serverConnectionConfig) {
        return new ConcreteServerConnection(this, serverConnectionConfig);
    }

    @Override
    public void registerListener(PacketListener packetListener) {
        for (Method method : packetListener.getClass().getMethods()) {
            if (isMethodApplicable(method)) {
                @SuppressWarnings("unchecked")
                Class<? extends Packet> parameter = (Class<? extends Packet>) (method.getParameterTypes().length == 2 ? method.getParameterTypes()[1] : method.getParameterTypes()[0]);
                List<PacketListener> list = listeners.containsKey(parameter) ? listeners.get(parameter) : new ArrayList<PacketListener>();
                list.add(packetListener);
                listeners.put(parameter, list);
            } else if (method.isAnnotationPresent(PacketHandler.class)) {
                throw new IllegalArgumentException("Method " + packetListener.getClass() + "#" + method.getName() + " has @PacketHandler annotation but does not take the correct parameters");
            }
        }
    }

    @Override
    public boolean isListenerRegistered(PacketListener packetListener) {
        for (Class<? extends Packet> clazz : listeners.keySet()) {
            List<PacketListener> list = listeners.get(clazz);

            if (list.contains(packetListener)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void unregisterListener(PacketListener packetListener) {
        for (Class<? extends Packet> clazz : listeners.keySet()) {
            List<PacketListener> list = listeners.get(clazz);
            list.remove(packetListener);
            listeners.put(clazz, list);
        }
    }

    public void callListeners(ConcreteConnection connection, Packet packet) {
        if (listeners.containsKey(packet.getClass())) {
            for (PacketListener packetListener : listeners.get(packet.getClass())) {
                for (Method method : packetListener.getClass().getMethods()) {
                    if (isMethodApplicable(packet, method)) {
                        try {
                            if (method.getParameterTypes().length == 2) {
                                method.invoke(packetListener, connection, packet);
                            } else {
                                method.invoke(packetListener, packet);
                            }
                        } catch (Throwable e) {
                            System.out.println("Error whilst invoking listener\nPacket: " + packet.getClass().getName() + "\nMethod: " + packetListener.getClass().getName() + "#" + method.getName());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private boolean isMethodApplicable(Packet packet, Method method) {
        return isMethodApplicable(packet.getClass(), method);
    }

    private boolean isMethodApplicable(Class<? extends Packet> packet, Method method) {
        Class<?>[] parameters = method.getParameterTypes();
        return method.isAnnotationPresent(PacketHandler.class)
                && (((parameters.length == 1) && packet.isAssignableFrom(parameters[0])) || ((parameters.length == 2) && parameters[0].equals(Connection.class) && packet.isAssignableFrom(parameters[1])));
    }

    private boolean isMethodApplicable(Method method) {
        Class<?>[] parameters = method.getParameterTypes();
        return method.isAnnotationPresent(PacketHandler.class)
                && (((parameters.length == 1) && Packet.class.isAssignableFrom(parameters[0])) || ((parameters.length == 2) && parameters[0].equals(Connection.class) && Packet.class
                .isAssignableFrom(parameters[1])));
    }

    @Override
    public void addHook(HookType hookType, Callback<Connection> consumer) {
        List<Callback<Connection>> list = hooks.containsKey(hookType) ? hooks.get(hookType) : new ArrayList<Callback<Connection>>();
        list.add(consumer);
        hooks.put(hookType, list);
    }

    public void callHook(ConcreteConnection connection, HookType hookType) {
        if (hooks.containsKey(hookType)) {
            for (Callback<Connection> consumer : hooks.get(hookType)) {
                consumer.call(connection);
            }
        }
    }

    @Override
    public List<Connection> getConnections() {
        return new ArrayList<Connection>(connections); // todo cache
    }

    @Override
    public void close(){
        try {
            close(true);
        } catch (IOException e) { // since we ignore exceptions, this shouldn't be thrown
            e.printStackTrace();
        }
    }

    public void close(boolean silent) throws IOException {
        System.out.println("Closing all connections...");

        for (ConcreteConnection connection : new ArrayList<>(connections)) { // create copy so we don't get Concurrent issues
            try {
                connection.close(false);
            } catch (IOException e){
                if (!silent){
                    throw e;
                }
            }
        }
    }

}
