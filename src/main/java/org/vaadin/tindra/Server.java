package org.vaadin.tindra;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by se on 19/06/14.
 */
public class Server {

    private final int portNumber;
    private boolean running = false;

    public Server(int portNumber) {
        this.portNumber = portNumber;

    }

    public boolean isRunning() {
        return running;
    }

    public void start() {

        running = true;

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (isRunning()) {
                new ServerThread(this,serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }

    public void stop() {
        this.running = false;
    }


    static public void main(String[] args) {

        //if (args.length != 1) {
        //    System.err.println("Usage: java Server <port number>");
        //    System.exit(1);
        //}

        int portNumber = Integer.parseInt("50123");
        new Server(portNumber).start();

    }

}
