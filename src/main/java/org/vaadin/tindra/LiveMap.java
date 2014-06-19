package org.vaadin.tindra;

import com.vaadin.event.UIEvents;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.tindra.backend.AppService;
import org.vaadin.tindra.backend.UpdateRepository;
import org.vaadin.tindra.domain.Update;

/**
 *
 */
@UIScope
@VaadinComponent
public class LiveMap extends LMap implements UIEvents.PollListener {
    
    long lastUpdate;
    
    @Autowired
    AppService appService;

    @Autowired
    UpdateRepository repo;

    @Autowired
    EventBus bus;

    private LPolyline snake;
    private LCircleMarker marker;

    private final LinkedList<Point> updates = new LinkedList<>();

    public LiveMap() {
        setHeight("300px");
    }

    @PostConstruct
    void init() {
        addLayer(new LOpenStreetMapLayer());
        List<Update> content = repo.findAll(new PageRequest(0, 10, new Sort(
                Direction.DESC, "timestamp"))).getContent();
        boolean first = false;
        for (Update update : content) {
            updates.add(new Point(update.getLat(), update.getLon()));
            if (first) {
                updateHead(update);
                first = false;
            }
        }
        if (marker != null) {
            snake.setPoints(updates.toArray(new Point[0]));
            addLayer(snake);
            addLayer(marker);
            zoomToContent();
        }

    }

    @Override
    public void attach() {
        super.attach();
        getUI().setPollInterval(5000);
        getUI().addPollListener(this);
    }

    private void updateHead(Update update) {
        if (marker == null) {
            marker = new LCircleMarker(new Point(update.getLat(), update.
                    getLon()), 10);
        } else {
            marker.setPoint(new Point(update.getLat(), update.getLon()));
        }
        lastUpdate = update.getId();
    }

    private void addPoint(Update u) {
        updateHead(u);
        updates.add(new Point(u.getLat(), u.getLon()));
        if (updates.size() > 20) {
            updates.remove();
        }
        snake.setPoints(updates.toArray(new Point[0]));
    }

    @Override
    public void poll(UIEvents.PollEvent event) {
        if(appService.getLastUpdate() != null && appService.getLastUpdate() !=  lastUpdate) {
            updateHead(repo.findOne(appService.getLastUpdate()));
        }
    }
}
