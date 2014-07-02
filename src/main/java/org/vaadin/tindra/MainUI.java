package org.vaadin.tindra;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.event.UIEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.levelindicator.Levelindicator;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.spring.touchkit.TouchKitUI;
import org.vaadin.tindra.backend.AppService;
import org.vaadin.tindra.backend.UpdateRepository;
import org.vaadin.tindra.domain.Update;
import org.vaadin.tindra.tcpserver.Server;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 *
 */
@TouchKitUI
@Title("Tindra Tracker")
@Widgetset("org.vaadin.tindra.AppWidgetSet")
@Theme("tindra")
public class MainUI extends UI implements UIEvents.PollListener {

    @Autowired
    Server server;

    @Autowired
    UpdateRepository repo;

    @Autowired
    LiveMap liveMap;

    @Autowired
    AppService appService;

    Button listLastPoints = new MButton(FontAwesome.BARS, this::listLastPoints);
    Button trackBoat = new MButton(FontAwesome.CROSSHAIRS, this::trackBoat);

    Label speed = new Label(" - ");
    Label course = new Label(" - ");
    Levelindicator fixCount = new Levelindicator(5,0);
    Levelindicator batteryLevel = new Levelindicator(5,0);
    Label lastUpdate = new Label("---------- --:--");
    CssLayout trackingOverlay = new CssLayout(speed, course);
    CssLayout statusOverlay = new CssLayout(fixCount, batteryLevel,lastUpdate);

    NumberFormat speedFormat = new DecimalFormat("0.00");
    NumberFormat angleFormat = new DecimalFormat("000");
    private boolean boatTracking = false;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void init(VaadinRequest request) {
        NavigationView navigationView = new NavigationView("Tindra");

        navigationView.setLeftComponent(trackBoat);
        navigationView.setRightComponent(listLastPoints);

        // Overlay display for data
        speed.setCaption("Speed");
        course.setCaption("Course");
        fixCount.setCaption("Status");
        fixCount.setSizeUndefined();
        batteryLevel.setCaption("Battery");
        batteryLevel.setSizeUndefined();
        lastUpdate.setCaption("Last update");
        trackingOverlay.setVisible(false);
        trackingOverlay.setStyleName("dataoverlay");
        statusOverlay.setStyleName("statusoverlay");
        final CssLayout cssLayout = new CssLayout(liveMap, statusOverlay, trackingOverlay);
        cssLayout.setSizeFull();
        navigationView.setContent(cssLayout);

        setContent(navigationView);

        getUI().setPollInterval(5000);
        getUI().addPollListener(this);
        getUI().addPollListener(liveMap);

    }

    public void trackBoat(Button.ClickEvent e) {
        boatTracking = !boatTracking;
        trackingOverlay.setVisible(boatTracking);
        statusOverlay.setVisible(false);
        statusOverlay.setVisible(true);
        if (boatTracking) {
            liveMap.showLastUpdate();
            updateBoatData();
        }
    }

    private void updateBoatData() {
        if (appService.getLastUpdate() != null) {
            final Update latest = repo.findOne(appService.getLastUpdate());
             speed.setValue("" + speedFormat.format(latest.getSpeed()) + " kts");
             course.setValue("" + angleFormat.format(latest.getCourse()) + " °");
        } else {
            speed.setValue("-.-- kts");
            course.setValue("--- °");
            fixCount.setFilledBars(0);
        }
    }

    private void updateStatusData() {
        if (appService.getLastUpdate() != null) {
            final Update latest = repo.findOne(appService.getLastUpdate());
            fixCount.setFilledBars(levelFromFixCoun(latest.getFixCount()));
            batteryLevel.setFilledBars(levelFromBattery(latest.getBatteryLevel()));
            lastUpdate.setValue(dateFormat.format(latest.getTimestamp()));
        } else {
            fixCount.setFilledBars(0);
            batteryLevel.setFilledBars(0);
            lastUpdate.setValue("---------- --:--");
        }
    }

    private int levelFromFixCoun(Integer fixCount) {
        if (fixCount > 10) {
            return 5;
        } else if (fixCount > 2) {
            return fixCount/2;
        }
        return 0;
    }

    private int levelFromBattery(Double batteryVoltage) {
        if (batteryVoltage > 4.1) {
            return 5;
        } else if (batteryVoltage > 4.0) {
            return 4;
        } else if (batteryVoltage > 3.95) {
            return 3;
        } else if (batteryVoltage > 3.85) {
            return 2;
        } else if (batteryVoltage > 3.75) {
            return 1;
        }
        return 0;
    }

    private Update findUpdateByPoint(Point p) {
        Update up = new Update();
        up.setSpeed(1.23);
        up.setCourse(234.0);
        return up; //TODO: database lookup
    }

    private void listLastPoints(Button.ClickEvent e) {
        Popover popover = new Popover(new NavigationView("Last points", new MTable<>(Update.class)
                .setBeans(repo.findAll(new PageRequest(0, 100, new Sort(
                        Sort.Direction.DESC, "timestamp"))).
                        getContent())
                .withFullHeight().withFullWidth()));
        popover.setModal(true);
        popover.setWidth("80%");
        popover.setHeight("80%");
        popover.showRelativeTo(listLastPoints);
    }

    @Override
    public void poll(UIEvents.PollEvent event) {
        if (boatTracking) {
            updateBoatData();
        }
        updateStatusData();
    }

}
