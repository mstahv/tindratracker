package org.vaadin.tindra.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by se
 */
@Entity
public class Tracker {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSeen;

    private String name;

    private String imei;

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

}
