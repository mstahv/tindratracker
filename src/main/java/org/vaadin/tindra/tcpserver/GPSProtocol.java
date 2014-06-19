package org.vaadin.tindra.tcpserver;

import org.vaadin.tindra.domain.Update;

/**
 * Created by se on 19/06/14.
 */
public class GPSProtocol {

    public Update processInput(String input) {
        Update out = new Update();

        String[] parts = input.split(",");


        out.setImei(parts[17]);
        return out;
    }

}
