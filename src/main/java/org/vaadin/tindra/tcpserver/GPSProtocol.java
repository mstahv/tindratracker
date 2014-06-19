package org.vaadin.tindra.tcpserver;

import org.vaadin.tindra.domain.Update;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by se on 19/06/14.
 */
public class GPSProtocol {

    DateFormat format = new SimpleDateFormat("YYMMddHHmmss");

    public Update processInput(String input) {
        Update out = new Update();

        String[] parts = input.split(",");

        out.setId(Long.parseLong(parts[0]));
        out.setLat(Double.parseDouble(parts[5]));
        out.setLon(Double.parseDouble(parts[7]));
        try {
            Date ts = format.parse(parts[0]);
            out.setTimestamp(ts);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        out.setImei(parts[17]);
        return out;
    }

}
