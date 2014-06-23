/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.tindra;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.maddon.button.MButton;
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
@Title("Tindra Tracker")
@Theme("dawn")
@Widgetset("org.peimari.maastokanta.AppWidgetSet")
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
        AbsoluteLayout absoluteLayout = new AbsoluteLayout();
        absoluteLayout.addComponent(liveMap, "top:0;right:0;bottom:0;left:0;");
        absoluteLayout.addComponent(new MVerticalLayout(listLastPoints), "top:0; right:0;");
        setContent(absoluteLayout);
    }
    
    private void listLastPoints(Button.ClickEvent e) {
        Window window = new Window("Last points", new MTable<>(Update.class)
                .setBeans(repo.findAll(new PageRequest(0, 100, new Sort(
                        Sort.Direction.DESC, "timestamp"))).
                        getContent())
                .withFullHeight().withFullWidth());
        window.setModal(true);
        window.setWidth("80%");
        window.setHeight("80%");
        getUI().addWindow(window);
    }

}
