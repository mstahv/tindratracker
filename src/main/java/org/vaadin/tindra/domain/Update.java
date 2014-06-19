package org.vaadin.tindra.domain;

import com.vividsolutions.jts.geom.Point;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Update  implements Serializable {
    
//      `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
//  `device_id` int(10) unsigned NOT NULL,
//  `serial` datetime NOT NULL,
//  `authorized_no` char(16) NOT NULL,
//  `timestamp` char(9) NOT NULL,
//  `validity` enum('A','V') NOT NULL,
//  `lat` char(9) NOT NULL,
//  `lat_type` enum('N','S') NOT NULL,
//  `lon` char(10) NOT NULL,
//  `lon_type` enum('E','W') NOT NULL,
//  `speed` float NOT NULL,
//  `course` float NOT NULL,
//  `datestamp` date NOT NULL,
//  `mag_variation` float NOT NULL,
//  `mag_variation_type` enum('E','W') NOT NULL,
//  `gprmc_checksum` char(3) NOT NULL,
//  `signal_level` enum('F','L') NOT NULL,
//  `sos_message` char(7) NOT NULL,
//  `fix_count` int(11) NOT NULL,
//  `altitude` float NOT NULL,
//  `battery_level` char(7) NOT NULL,
//  `charging` tinyint(4) NOT NULL,
//  `str_length` int(11) NOT NULL,
//  `checksum` int(11) NOT NULL,
//  `mcc` char(4) NOT NULL,
//  `mnc` char(4) NOT NULL,
//  `lac` char(4) NOT NULL,
//  `cell_id` char(4) NOT NULL,
//  `hidden` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'testausta varten',
//  `server_timestamp` datetime NOT NULL,
//  `fixed_lat` float DEFAULT NULL,
//  `fixed_lon` float DEFAULT NULL,

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    
    private Double lon;
    private Double lat;
    
    private String imei;

    public Update() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String toString() {
        return "Update{" + "id=" + id + ", timestamp=" + timestamp + ", lon=" + lon + ", lat=" + lat + ", imei=" + imei + '}';
    }

}