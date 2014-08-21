package org.vaadin.tindra;

import com.vaadin.event.UIEvents;
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
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.tindra.backend.AppService;
import org.vaadin.tindra.backend.TrackerRepository;
import org.vaadin.tindra.backend.UpdateRepository;
import org.vaadin.tindra.domain.Tracker;
import org.vaadin.tindra.domain.Update;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 *
 */
@UIScope
@VaadinComponent
public class LiveMap extends LMap implements UIEvents.PollListener {

    long lastUpdateId;

    @Autowired
    AppService appService;

    @Autowired
    UpdateRepository repo;

    @Autowired
    TrackerRepository repo2;

    @Autowired
    EventBus bus;

    private LPolyline snake;
    private LCircleMarker marker;

    // Imei to update list mapping
    private final Map<String,LinkedList<Coordinate>> trackerUpdates = new HashMap<>();

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

        List<Tracker> trackers = repo2.findAll();
        for(Tracker tracker: trackers) {
            List<Update> content = repo.
                    findByTimestampGreaterThanAndImeiOrderByTimestampDesc(cal.getTime(), tracker.getImei());
            boolean first = true;
            for (Update update : content) {
                LinkedList<Coordinate> updates = getOrCreateUpdates(tracker.getImei());

                updates.addFirst(new Coordinate(update.getLon(), update.getLat()));
                if (first) {
                    updateHead(update);
                    setCenter(new Point(update.getLat(), update.getLon()));
                    first = false;
                }
            }
        }

        if (marker != null) {
            for(Tracker tracker: trackers) {
                drawSnake(tracker);
            }
        }
    }

    private void drawSnake(Tracker tracker) {
        LinkedList<Coordinate> updates = getOrCreateUpdates(tracker.getImei());
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

        LinkedList<Coordinate> updates = getOrCreateUpdates(u.getImei());
        updates.add(new Coordinate(u.getLon(), u.getLat()));
        if (updates.size() > 20) {
            updates.remove();
        }
        drawSnake(getOrCreateTracker(u.getImei()));
    }

    private LinkedList<Coordinate> getOrCreateUpdates(String imei) {

        LinkedList<Coordinate> updates = trackerUpdates.get(imei);
        if (updates == null) {
            updates = new LinkedList<>();
            trackerUpdates.put(imei,updates);
        }
        return updates;
    }

    private Tracker getOrCreateTracker(String imei) {
        Tracker t = repo2.getByImei(imei);
        if (t == null) {
            t = new Tracker();
            t.setImei(imei);
            t.setName("[Unknown tracker]");
            t = repo2.save(t);
        }
        return t;
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
