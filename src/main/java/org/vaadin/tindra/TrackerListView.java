package org.vaadin.tindra;

import com.vaadin.addon.touchkit.ui.NavigationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.tindra.backend.UpdateRepository;
import org.vaadin.tindra.domain.Tracker;

import java.util.List;

public class TrackerListView extends NavigationView {

    private final MTable table;

    @Autowired
    UpdateRepository repo;

    public TrackerListView(List<Tracker> trackers) {
        setCaption("Available trackers");
        setContent(table = new MTable<>(Tracker.class)
                .setBeans(trackers).withFullHeight().withFullWidth());
    }

}
