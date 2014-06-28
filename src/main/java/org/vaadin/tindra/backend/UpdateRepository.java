
package org.vaadin.tindra.backend;

import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.vaadin.tindra.domain.Update;

/**
 *
 * @author mattitahvonenitmill
 */
@Service
public class UpdateRepository {
    
    private LinkedList<Update> updates = new LinkedList<>();

    public Update save(Update update) {
        updates.add(update);
        if(updates.size() > 50) {
            updates.remove();
        }
        return update;
    }

    public List<Update> getUpdates() {
        return new LinkedList(updates);
    }

    public Update getLatest() {
        return updates.getLast();
    }
    

}
