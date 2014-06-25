package org.vaadin.tindra;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
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
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.spring.touchkit.TouchKitUI;
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
@Theme("touchkit")
public class MainUI extends UI {

    @Autowired
    Server server;

    @Autowired
    UpdateRepository repo;

    @Autowired
    LiveMap liveMap;
    
    Button listLastPoints = new MButton(FontAwesome.BARS, this::listLastPoints);

    Label speed = new Label(" - ");
    Label course = new Label(" - ");
    HorizontalLayout overlay = new MHorizontalLayout(speed, course);

    NumberFormat speedFormat = new DecimalFormat("0.00");
    NumberFormat angleFormat = new DecimalFormat("000");

    @Override
    protected void init(VaadinRequest request) {
        NavigationView navigationView = new NavigationView("Tindra tracker");
        navigationView.setRightComponent(listLastPoints);

        speed.setSizeFull();
        course.setSizeFull();
        overlay.setVisible(false);
        overlay.setStyleName("dataoverlay");
        CssLayout layout = new CssLayout(liveMap, overlay);
        layout.setSizeFull();
        navigationView.setContent(layout);

        setContent(navigationView);

        liveMap.addClickListener(this::onMapClick);
    }

    public void onMapClick(LeafletClickEvent e) {
        Point p = e.getPoint();

        Update selected = findUpdateByPoint(p);
        if (selected != null) {
            speed.setValue(""+speedFormat.format(selected.getSpeed())+"kts");
            course.setValue(""+angleFormat.format(selected.getCourse()));
        } else {
            speed.setValue(" - ");
            course.setValue(" - ");
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

}
