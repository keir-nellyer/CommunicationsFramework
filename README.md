# CommunicationsAPI
Formally named PacketAPI.
This was originally a small wrapper to connect Clients <-> Servers using the java.net library (non NIO) I wrote to aid me in writing some private projects, it quickly evolved into a very neat API to allow listeners and hooks for different packets and events. java.net was quickly replaced with the superior Netty (using NIO) and many improvements were made.
This project was also I way for me to learn how to use the Netty API and it has been very fun. I do not claim to be an experienced with Netty so I may have made some mistakes.

## Features

* Easy persistent Client <-> Server connections.
* Secure connections with a key.
* Create your own packets with ease.
* Listen for connection events (such as connected, lost connection, reconnect and more to be added).
* Automatic handling of reconnects.
* Packet queue system which allows packets to be queued for sending when the connection currently isn't available (waiting for connection or reconnecting).
* Graceful disconnecting, no more exception spams when one side disconnects for whatever reason.
* Heartbeats which allow us to determine if the other side has disconnected quickly.
* Fully documented frontend, no need to deal with confusing internals (just make sure you only use classes within the "api" package).

## Usage

Examples are included in the "example" package.

## Contributing

* Install the Java 8 JRE & JDK if you haven't already.
* Install [Maven 3](http://maven.apache.org/download.html).
* Fork this repo and make your changes.
* To compile: ```mvn clean install```.
* Submit the PR

### Code Requirements

* No tabs; please use 4 spaces instead.
* No trailing whitespaces
* No CRLF line endings, LF only, set your Gits 'core.autocrlf' to 'true'.

    Eclipse: http://stackoverflow.com/a/11596227/532590
    NetBeans: http://stackoverflow.com/a/1866385/532590
    IntelliJ IDEA: http://stackoverflow.com/a/17597851/1870318
* No 80 column limit or 'weird' midstatement newlines.
* Changes made in the "api" package should have appropriate documentation.
* New line at the end of every file.
* Each PR should only have one attached commit and it should briefly explain what has been changed.
* Must compile with JDK 8.

# FAQ

### My connection(s) won't connect but I don't get any errors
  Make sure your calling ```clientConnection.connect()``` or ```serverConnection.bind()``` depending on the type of connection your using.
  If that still doesn't solve it, check each connections hostname and port is setup correctly.

# Licensing

We're GPL v3, for more details, see [here](LICENSE.txt)