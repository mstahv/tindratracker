package org.vaadin.tindra;

import com.vaadin.event.UIEvents;
import com.vaadin.spring.annotation.UIScope;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.simplify.DouglasPeuckerSimplifier;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.leaflet.LCircleMarker;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.spring.events.EventBus;
import org.vaadin.tindra.backend.AppService;
import org.vaadin.tindra.backend.UpdateRepository;
import org.vaadin.tindra.domain.Update;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 */
@UIScope
@Component
public class LiveMap extends LMap implements UIEvents.PollListener {

    long lastUpdateId;

    @Autowired
    AppService appService;

    @Autowired
    UpdateRepository repo;

    @Autowired
    EventBus.UIEventBus bus;

    private LPolyline snake;
    private LCircleMarker marker;

    private final LinkedList<Coordinate> updates = new LinkedList<>();

    private final GeometryFactory geometryFactory = JTSFactoryFinder.
            getGeometryFactory(
                    null);

    public LiveMap() {
        setHeight("100%");
        setCenter(60.375, 22.125);
        setZoomLevel(13);
    }

    @PostConstruct
    void init() {
        addLayer(new LOpenStreetMapLayer());
        initRoute();
    }

    private void initRoute() {

        Coordinate[] coords
                = new Coordinate[]{new Coordinate(0, 2), new Coordinate(2, 0), new Coordinate(
                            8, 6)};

        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.DATE, -7);
        List<Update> content = repo.
                findByTimestampGreaterThanOrderByTimestampDesc(cal.getTime());
//        List<Update> content = repo.findAll(new PageRequest(0, 10, new Sort(
//                Direction.DESC, "timestamp"))).getContent();
        boolean first = true;
        LineString ls = null;
        for (Update update : content) {
            updates.addFirst(new Coordinate(update.getLon(), update.getLat()));
            if (first) {
                updateHead(update);
                first = false;
                setCenter(new Point(update.getLat(), update.getLon()));
            }
        }

        if (marker != null) {
            drawSnake(geometryFactory);
            if (updates.size() > 1) {
                zoomToContent();
            }
        }
    }

    private void drawSnake(GeometryFactory geometryFactory) {
        if (updates.size() > 2) {
            LineString line = geometryFactory.createLineString(updates.toArray(
                    new Coordinate[0]));
            line = (LineString) DouglasPeuckerSimplifier.simplify(line, 0.001);
            if (snake != null) {
                removeComponent(snake);
            }
            snake = new LPolyline(line);
            addLayer(snake);
        }
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
        lastUpdateId = update.getId();
    }

    private void addPoint(Update u) {
        updateHead(u);
        updates.add(new Coordinate(u.getLon(), u.getLat()));
        if (updates.size() > 20) {
            updates.remove();
        }
        drawSnake(geometryFactory);
    }

    @Override
    public void poll(UIEvents.PollEvent event) {
        final Update latest = appService.getLastUpdate();
        if (latest != null && latest.getId() != lastUpdateId) {
            addPoint(latest);
        }
    }

    void centerToLastPoint() {
        Update latest = appService.getLastUpdate();
        if (latest != null) {
            setCenter(latest.getLat(), latest.getLon());
        }
    }
}
