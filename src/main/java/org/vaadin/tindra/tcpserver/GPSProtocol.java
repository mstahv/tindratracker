package org.vaadin.tindra.tcpserver;

import org.geotools.measure.AngleFormat;
import org.vaadin.tindra.domain.Update;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by se on 19/06/14.
 */
public class GPSProtocol {

    DateFormat format = new SimpleDateFormat("yyMMddHHmmss");

    AngleFormat latFormat = new AngleFormat("DDMM.mmmmm");
    AngleFormat lonFormat = new AngleFormat("DDDMM.mmmmm");

    public Update processInput(String input) {
        Update out = new Update();

        String[] parts = input.split(",");

        try {
            Date ts = format.parse(parts[0]);
            out.setTimestamp(ts);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            out.setLat(latFormat.parse(parts[5]).degrees());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            out.setLon(lonFormat.parse(parts[7]).degrees());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            out.setImei(parts[17].split(":")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

}
