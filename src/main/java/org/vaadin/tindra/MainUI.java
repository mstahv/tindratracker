package org.vaadin.tindra;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.spring.touchkit.TouchKitUI;
import org.vaadin.tindra.backend.UpdateRepository;
import org.vaadin.tindra.domain.Update;
import org.vaadin.tindra.tcpserver.Server;

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

    @Override
    protected void init(VaadinRequest request) {
        NavigationView navigationView = new NavigationView("Tindra tracker");
        navigationView.setRightComponent(listLastPoints);
        navigationView.setContent(liveMap);
        setContent(navigationView);
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
