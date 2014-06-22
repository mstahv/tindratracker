
package org.vaadin.tindra.backend;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.tindra.domain.Update;

/**
 *
 * @author mattitahvonenitmill
 */
public interface UpdateRepository extends JpaRepository<Update, Long> {
    
    public List<Update> findByTimestampGreaterThanOrderByTimestampDesc(Date start);

}
