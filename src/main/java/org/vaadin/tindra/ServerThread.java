package org.vaadin.tindra;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by se on 19/06/14.
 */
public class ServerThread extends Thread {
    private final Server server;
    private Socket socket = null;

    public ServerThread(Server server, Socket socket) {
        super("ServerThread");
        this.socket = socket;
        this.server = server;
    }

    public void run() {

        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(this.socket.getInputStream()));
            String inputLine;
            Update update;

            // Initiate conversation with client
            GPSProtocol kkp = new GPSProtocol();

            while ((inputLine = in.readLine()) != null && server.isRunning()) {
                update = kkp.processInput(inputLine);
                System.out.println(update.getIMEI()); //TODO: save to database instead
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
