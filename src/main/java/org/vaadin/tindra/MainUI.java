/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.tindra;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.VaadinUI;
import org.vaadin.tindra.backend.UpdateRepository;
import org.vaadin.tindra.domain.Update;
import org.vaadin.tindra.tcpserver.Server;

/**
 *
 * @author mattitahvonenitmill
 */
//@TouchKitUI(path = "tk")
@VaadinUI
@Title("Root UI")
@Theme("dawn")
@Widgetset("org.peimari.maastokanta.AppWidgetSet")
public class MainUI extends UI {

    @Autowired
    Server server;

    @Autowired
    UpdateRepository repo;

    @Autowired
    LiveMap liveMap;

    @Override
    protected void init(VaadinRequest request) {
        listLastPoints();
    }

    private void listLastPoints() {
        boolean running = server.isRunning();

        setContent(new MVerticalLayout(
                new Label("Server running: " + running),
                new MTable<>(Update.class)
                        .setBeans(repo.findAll())
                        .withHeight("200px"),
                liveMap
        )
        );
    }

}
