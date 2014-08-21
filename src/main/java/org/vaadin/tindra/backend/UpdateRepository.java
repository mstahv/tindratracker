
package org.vaadin.tindra.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.tindra.domain.Update;

import java.util.Date;
import java.util.List;

/**
 *
 * @author mattitahvonenitmill
 */
public interface UpdateRepository extends JpaRepository<Update, Long> {

    public List<Update> findByTimestampGreaterThanOrderByTimestampDesc(Date start);

    public List<Update> findByTimestampGreaterThanAndImeiOrderByTimestampDesc(Date start, String imei);

}
