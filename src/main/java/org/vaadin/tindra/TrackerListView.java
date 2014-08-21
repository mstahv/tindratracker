package org.vaadin.tindra;

import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.tindra.domain.Tracker;

import java.util.List;

public class TrackerListView extends NavigationView {

    private final MTable table;

    public TrackerListView(List<Tracker> trackers) {
        setCaption("Available trackers");
        setContent(table = new MTable<>(Tracker.class)
                .setBeans(trackers).withFullHeight().withFullWidth());
        table.addGeneratedColumn("", (components, o, o2) -> new NavigationButton("Latest", new TabularView(null)));
    }

}
