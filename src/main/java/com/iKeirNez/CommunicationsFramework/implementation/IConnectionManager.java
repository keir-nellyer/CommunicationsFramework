package com.iKeirNez.CommunicationsFramework.implementation;

import com.iKeirNez.CommunicationsFramework.api.connection.AuthenticatedClientConnection;
import com.iKeirNez.CommunicationsFramework.api.connection.AuthenticatedServerConnection;
import com.iKeirNez.CommunicationsFramework.api.connection.Connection;
import com.iKeirNez.CommunicationsFramework.api.connection.ConnectionManager;
import com.iKeirNez.CommunicationsFramework.api.HookType;
import com.iKeirNez.CommunicationsFramework.api.packets.Packet;
import com.iKeirNez.CommunicationsFramework.api.packets.PacketHandler;
import com.iKeirNez.CommunicationsFramework.api.packets.PacketListener;
import com.iKeirNez.CommunicationsFramework.implementation.secure.connection.IAuthenticatedClientConnection;
import com.iKeirNez.CommunicationsFramework.implementation.secure.connection.IAuthenticatedServerConnection;
import com.iKeirNez.CommunicationsFramework.implementation.standard.connection.IClientConnection;
import com.iKeirNez.CommunicationsFramework.implementation.standard.connection.IConnection;
import com.iKeirNez.CommunicationsFramework.implementation.standard.connection.IServerConnection;

import java.io.IOException;
import java.lang.IllegalArgumentException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by iKeirNez on 06/04/2014.
 */
public class IConnectionManager implements ConnectionManager {

    public final List<IConnection> connections = Collections.synchronizedList(new ArrayList<>());
    private final Map<Class<? extends Packet>, List<PacketListener>> listeners = new ConcurrentHashMap<>();
    private final Map<HookType, List<Consumer<Connection>>> hooks = new ConcurrentHashMap<>();
    public final ClassLoader classLoader;

    /**
     * @deprecated see {@link com.iKeirNez.CommunicationsFramework.api.connection.ConnectionManager#getNewInstance(ClassLoader)}
     */
    public IConnectionManager(ClassLoader classLoader){
        this.classLoader = classLoader;
    }

    @Override
    public IClientConnection newClientConnection(String serverAddress, int port){
        return new IClientConnection(this, serverAddress, port);
    }

    @Override
    public AuthenticatedClientConnection newAuthenticatedClientConnection(String serverAddress, int port, char[] key) {
        return new IAuthenticatedClientConnection(this, serverAddress, port, key);
    }

    @Override
    public IServerConnection newServerConnection(String clientAddress, int port){
        return new IServerConnection(this, clientAddress, port);
    }

    @Override
    public AuthenticatedServerConnection newAuthenticatedServerConnection(String clientAddress, int port, char[] key) {
        return new IAuthenticatedServerConnection(this, clientAddress, port, key);
    }

    @Override
    public void registerListener(PacketListener packetListener){
        for (Method method : packetListener.getClass().getMethods()){
            if (isMethodApplicable(method)){
                Class<? extends Packet> parameter = (Class<? extends Packet>) (method.getParameterCount() == 2 ? method.getParameterTypes()[1] : method.getParameterTypes()[0]);
                List<PacketListener> list = listeners.getOrDefault(parameter, new ArrayList<>());
                list.add(packetListener);
                listeners.put(parameter, list);
            } else if (method.isAnnotationPresent(PacketHandler.class)){
                throw new IllegalArgumentException("Method " + packetListener.getClass() + "#" + method.getName() + " has @PacketHandler annotation but does not take the correct parameters");
            }
        }
    }

    @Override
    public boolean isListenerRegistered(PacketListener packetListener){
        for (Class<? extends Packet> clazz : listeners.keySet()){
            List<PacketListener> list = listeners.get(clazz);

            if (list.contains(packetListener)){
                return true;
            }
        }

        return false;
    }

    @Override
    public void unregisterListener(PacketListener packetListener){
        for (Class<? extends Packet> clazz : listeners.keySet()){
            List<PacketListener> list = listeners.get(clazz);
            list.remove(packetListener);
            listeners.put(clazz, list);
        }
    }

    public void callListeners(IConnection connection, Packet packet){
        if (listeners.containsKey(packet.getClass())){
            for (PacketListener packetListener : listeners.get(packet.getClass())){
                for (Method method : packetListener.getClass().getMethods()){
                    if (isMethodApplicable(packet, method)){
                        try {
                            if (method.getParameterCount() == 2){
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

    private boolean isMethodApplicable(Packet packet, Method method){
        return isMethodApplicable(packet.getClass(), method);
    }

    private boolean isMethodApplicable(Class<? extends Packet> packet, Method method){
        Class<?>[] parameters = method.getParameterTypes();
        return method.isAnnotationPresent(PacketHandler.class) && ((parameters.length == 1 && packet.isAssignableFrom(parameters[0])) || (parameters.length == 2 && parameters[0].equals(Connection.class) && packet.isAssignableFrom(parameters[1])));
    }

    private boolean isMethodApplicable(Method method){
        Class<?>[] parameters = method.getParameterTypes();
        return method.isAnnotationPresent(PacketHandler.class) && ((parameters.length == 1 && Packet.class.isAssignableFrom(parameters[0])) || (parameters.length == 2 && parameters[0].equals(Connection.class) && Packet.class.isAssignableFrom(parameters[1])));
    }

    @Override
    public void addHook(HookType hookType, Consumer<Connection> consumer){
        List<Consumer<Connection>> list = hooks.getOrDefault(hookType, new ArrayList<>());
        list.add(consumer);
        hooks.put(hookType, list);
    }

    public void callHook(IConnection connection, HookType hookType){
        if (hooks.containsKey(hookType)){
            for (Consumer<Connection> consumer : hooks.get(hookType)){
                consumer.accept(connection);
            }
        }
    }

    @Override
    public List<Connection> getConnections(){
        return new ArrayList<>(connections); // todo cache
    }

    @Override
    public void close() throws IOException {
        System.out.println("Closing all connections...");

        for (IConnection connection : new ArrayList<>(connections)){ // create copy so we don't get Concurrent issues
            connection.close(false);
        }
    }

}
