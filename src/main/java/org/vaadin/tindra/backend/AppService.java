/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.tindra.backend;

import org.springframework.stereotype.Service;
import org.vaadin.tindra.domain.Tracker;
import org.vaadin.tindra.domain.Update;

@Service
public class AppService {

    private Tracker lastUpdateTracker;
    private Update lastUpdate;

    public Update getLastUpdate() {
        return lastUpdate;
    }

    public Tracker getLastUpdatedTracker() {
        return lastUpdateTracker;
    }

    public void setLastUpdate(Tracker tracker, Update lastUpdate) {
        this.lastUpdate = lastUpdate;
        this.lastUpdateTracker = tracker;
    }
    
}
