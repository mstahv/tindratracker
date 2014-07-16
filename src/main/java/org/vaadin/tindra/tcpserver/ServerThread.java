package org.vaadin.tindra.tcpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.tindra.domain.Update;

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
                try {
                    inputLine = inputLine.trim();
                    if (!inputLine.isEmpty()) {
                        Logger.getLogger(getClass().getName()).log(Level.INFO,
                                "MSG from device:{0}", inputLine);
                        update = kkp.processInput(inputLine);
                        server.persist(update);
                    }
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                            "Failed to parse message", e);
                }
            }

        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE,
                    "Failed to parse message2", e);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }
}
