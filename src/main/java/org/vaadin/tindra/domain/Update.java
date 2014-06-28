package org.vaadin.tindra.domain;

import java.io.Serializable;
import java.util.Date;

public class Update  implements Serializable {
    
    private static int idCount = 0;

    private long id = idCount++;

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