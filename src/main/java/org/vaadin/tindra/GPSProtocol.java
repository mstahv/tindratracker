package org.vaadin.tindra;

/**
 * Created by se on 19/06/14.
 */
public class GPSProtocol {

    public Update processInput(String input) {
        Update out = new Update();

        String[] parts = input.split(",");


        out.setIMEI(parts[17]);
        return out;
    }

}
