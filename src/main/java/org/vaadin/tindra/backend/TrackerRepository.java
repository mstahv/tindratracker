
package org.vaadin.tindra.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.tindra.domain.Tracker;

/**
 *
 * @author se
 */
public interface TrackerRepository extends JpaRepository<Tracker, Long> {

    public Tracker getByImei(String imei);

}
