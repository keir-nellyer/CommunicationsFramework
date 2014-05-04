# Timings

This was a quick project developed to demonstrate the efficiency of CommunicationsFramework.
Connections are kept local to stop internet latency affecting the results.

This application consists of 3 threads

* Main thread (for starting the application, after that it does nothing)
* Progress monitor thread (displays the progress as a percentage every second whilst the application is running)
* Packet dispatch thread (this thread deals with dispatching packets, this has to be done on a seperate thread in this example as we sleep the thread often)

Packet processing & distribution to listeners is done on Netty threads.

# Running

Simply run the "Main" class from your IDE which will invoke the static main method which will handle everything else.
Feel free to change the amount of packets sent for more extensive testing.