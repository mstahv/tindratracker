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

    private Boolean valid;

    private Double lon;
    private Double lat;

    private Double speed;
    private Double course;
    private Double altitude;

    private Double signalLevel;
    private Integer fixCount;

    private Double batteryLevel;
    private Boolean charging;

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

    public Double getSpeed() { return speed; }

    public void setSpeed(Double speed) { this.speed = speed; }

    public Double getCourse() { return course; }

    public void setCourse(Double course) { this.course = course; }

    public Double getSignalLevel() { return signalLevel; }

    public void setSignalLevel(Double signalLevel) { this.signalLevel = signalLevel; }

    public Integer getFixCount() { return fixCount; }

    public void setFixCount(Integer fixCount) { this.fixCount = fixCount; }

    public Double getAltitude() { return altitude; }

    public void setAltitude(Double altitude) { this.altitude = altitude; }

    public Double getBatteryLevel() { return batteryLevel; }

    public void setBatteryLevel(Double batteryLevel) { this.batteryLevel = batteryLevel; }

    public Boolean getCharging() { return charging; }

    public void setCharging(Boolean charging) { this.charging = charging; }

    public Boolean getValid() { return valid; }

    public void setValid(Boolean valid) { this.valid = valid; }

    @Override
    public String toString() {
        return "Update{" + "id=" + id + ", timestamp=" + timestamp + ", lon=" + lon + ", lat=" + lat + ", imei=" + imei + '}';
    }

}