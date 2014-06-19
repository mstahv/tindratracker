/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.peimari.tindra;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.peimari.tindra.backend.UpdateRepository;
import org.peimari.tindra.domain.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.vaadin.spring.touchkit.TouchKitUI;

/**
 *
 * @author mattitahvonenitmill
 */
@TouchKitUI(path = "tk")
@EnableAutoConfiguration
@Widgetset("org.peimari.maastokanta.AppWidgetSet")
public class MainUI extends UI {

    @Autowired
    UpdateRepository repo;

    NavigationView navigationView = new NavigationView("Tindra tracker");
        
    @Override
    protected void init(VaadinRequest request) {
        setContent(navigationView);
        listLastPoints();
    }

    private void listLastPoints() {
        Pageable topTen = new PageRequest(0, 10);
        Page<Update> topten = repo.findAll(topTen);
        Pageable first = topTen.first();
    }

}
