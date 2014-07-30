package com.ikeirnez.communicationsframework.api.filter;

import com.ikeirnez.communicationsframework.api.connection.ServerConnection;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple whitelist implementation of {@link com.ikeirnez.communicationsframework.api.filter.ConnectionFilter}
 * If the client address is contained
 */
public class SimpleWhitelistConnectionFilter implements ConnectionFilter {

    private final Set<InetAddress> whitelist;

    public SimpleWhitelistConnectionFilter(final InetAddress inetAddress){
        this(new HashSet<InetAddress>() {{
            add(inetAddress);
        }});
    }

    public SimpleWhitelistConnectionFilter(Set<InetAddress> whitelist){
        this.whitelist = whitelist;
    }

    public Set<InetAddress> getWhitelist() {
        return whitelist;
    }

    public void addAddress(InetAddress inetAddress){
        whitelist.add(inetAddress);
    }

    public void removeAddress(InetAddress inetAddress){
        whitelist.remove(inetAddress);
    }

    @Override
    public boolean shouldAccept(ServerConnection serverConnection, InetAddress incomingConnectionAddress) {
        return whitelist.contains(incomingConnectionAddress);
    }

}
