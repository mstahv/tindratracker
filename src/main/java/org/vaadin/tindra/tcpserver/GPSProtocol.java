package org.vaadin.tindra.tcpserver;

import org.geotools.measure.AngleFormat;
import org.vaadin.tindra.domain.Update;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/** Parsing
 * Created by samie on 19/06/14.
 */
public class GPSProtocol {

    DateFormat format = new SimpleDateFormat("ddMMyyHHmmss");

    AngleFormat latFormat = new AngleFormat("DDMM.mmmmm");
    AngleFormat lonFormat = new AngleFormat("DDDMM.mmmmm");

    public Update processInput(String input) throws ParseException {
        Update out = new Update();

        String[] parts = input.split(",");

        out.setValid("A".equals(parts[4]));

        String ts = parts[11] + parts[3].substring(0,6);
        out.setTimestamp(format.parse(ts));

        out.setLat(latFormat.parse(parts[5]).degrees());
        out.setLat(out.getLat()*("S".equals(parts[6])?-1:1));
        out.setLon(lonFormat.parse(parts[7]).degrees());
        out.setLon(out.getLon()*("W".equals(parts[8])?-1:1));

        out.setSpeed(Double.parseDouble(parts[9]));
        out.setCourse(Double.parseDouble(parts[10]));
        out.setAltitude(Double.parseDouble(parts[19]));

        out.setSignalLevel("F".equals(parts[15])?1d:0d);
        out.setFixCount(Integer.parseInt(parts[18]));
        out.setBatteryLevel(Double.parseDouble(parts[20].substring(2).replaceAll("V","")));
        out.setCharging("1".equals(parts[21]));

        out.setImei(parts[17].split(":")[1]);
        return out;
    }

}
