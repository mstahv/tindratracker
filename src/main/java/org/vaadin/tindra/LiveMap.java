package org.vaadin.tindra;

import com.vaadin.event.UIEvents;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LTileLayer;
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

    private final LinkedList<Coordinate> updates = new LinkedList<>();

    private final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(
            null);

    public LiveMap() {
        setHeight("100%");
        setCenter(60.449, 22.221);
        setZoomLevel(14);
    }

    @PostConstruct
    void init() {
//        addLayer(new LOpenStreetMapLayer());
        addLayer( new LTileLayer("http://v3.tahvonen.fi/mvm71/tiles/peruskartta/{z}/{x}/{y}.png"));
        initRoute();
    }

    private void initRoute() {

        Coordinate[] coords
                = new Coordinate[]{new Coordinate(0, 2), new Coordinate(2, 0), new Coordinate(
                            8, 6)};

        List<Update> content = repo.getUpdates();
        boolean first = true;
        LineString ls = null;
        for (Update update : content) {
            updates.add(new Coordinate(update.getLon(), update.getLat()));
            if (first) {
                updateHead(update);
                first = false;
                setCenter(new Point(update.getLat(), update.getLon()));
            }
        }

        if (marker != null) {
            drawSnake(geometryFactory);
        }
    }

    private void drawSnake(GeometryFactory geometryFactory) {
        LineString line = geometryFactory.createLineString(updates.toArray(
                new Coordinate[0]));
        line = (LineString) DouglasPeuckerSimplifier.simplify(line, 0.001);
        if (snake != null) {
            removeComponent(snake);
        }
        snake = new LPolyline(line);
        addLayer(snake);
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
            snake = new LPolyline(new Point(update.getLat(), update.
                    getLon()));
            addLayer(snake);
            addLayer(marker);
        } else {
            marker.setPoint(new Point(update.getLat(), update.getLon()));
        }
        lastUpdate = update.getId();
    }

    private void addPoint(Update u) {
        updateHead(u);
        updates.add(new Coordinate(u.getLon(), u.getLat()));
        if (updates.size() > 20) {
            updates.remove();
        }
        drawSnake(null);
    }

    @Override
    public void poll(UIEvents.PollEvent event) {
        if (appService.getLastUpdate() != null && appService.getLastUpdate() != lastUpdate) {
            final Update latest = repo.getLatest();
            addPoint(latest);
            zoomToContent();
        }
    }
    
    public Coordinate getClosest(double lat, double lon) {
        Double d;
        Coordinate u = null;
        for (Coordinate c : updates) {
            
        }
        return u;
    }
}
