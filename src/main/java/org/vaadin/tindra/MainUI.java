package org.vaadin.tindra;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.event.UIEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.spring.touchkit.TouchKitUI;
import org.vaadin.tindra.backend.AppService;
import org.vaadin.tindra.backend.UpdateRepository;
import org.vaadin.tindra.domain.Update;
import org.vaadin.tindra.tcpserver.Server;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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
    CssLayout overlay = new CssLayout(speed, course);

    NumberFormat speedFormat = new DecimalFormat("0.00");
    NumberFormat angleFormat = new DecimalFormat("000");
    private boolean boatTracking = false;

    @Override
    protected void init(VaadinRequest request) {
        NavigationView navigationView = new NavigationView("Tindra");

        navigationView.setLeftComponent(trackBoat);
        navigationView.setRightComponent(listLastPoints);

        // Overlay display for data
        speed.setCaption("Speed");
        course.setCaption("Course");
        overlay.setVisible(false);
        overlay.setStyleName("dataoverlay");
        final CssLayout cssLayout = new CssLayout(liveMap, overlay);
        cssLayout.setSizeFull();
        navigationView.setContent(cssLayout);

        setContent(navigationView);

        getUI().setPollInterval(5000);
        getUI().addPollListener(this);
        getUI().addPollListener(liveMap);

    }

    public void trackBoat(Button.ClickEvent e) {
        boatTracking = !boatTracking;
        overlay.setVisible(boatTracking);
        if (boatTracking) {
            liveMap.showLastUpdate();
            updateOverlayData();
        }
    }

    private void updateOverlayData() {
        if (appService.getLastUpdate() != null) {
            final Update latest = repo.findOne(appService.getLastUpdate());
             speed.setValue(""+speedFormat.format(latest.getSpeed())+" kts");
             course.setValue(""+angleFormat.format(latest.getCourse())+" °");
        } else {
            speed.setValue("-.-- kts");
            course.setValue("--- °");
        }
        overlay.setVisible(true);
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
            updateOverlayData();
        }
    }

}
