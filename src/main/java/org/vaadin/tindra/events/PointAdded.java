
package org.vaadin.tindra.events;

import java.io.Serializable;
import org.vaadin.tindra.domain.Update;

/**
 *
 * @author mattitahvonenitmill
 */
public class PointAdded implements Serializable {

    private final Update update;

    public PointAdded(Update upload) {
        this.update = upload;
    }

    public Update getUpdate() {
        return update;
    }
}
