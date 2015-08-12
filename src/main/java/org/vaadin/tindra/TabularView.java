package org.vaadin.tindra;

import com.vaadin.addon.touchkit.ui.NavigationView;
import java.util.List;
import org.vaadin.tindra.domain.Update;
import org.vaadin.viritin.fields.MTable;

public class TabularView extends NavigationView {

    public TabularView(List<Update> updates) {
        setCaption("Last points");
        setContent(new MTable<>(Update.class)
                .setBeans(updates).withFullHeight().withFullWidth());
    }

}
