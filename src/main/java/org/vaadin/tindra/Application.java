package org.vaadin.tindra;

import com.vaadin.server.VaadinServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Application {

    public static void main(String[] args) {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public VaadinServlet vaadinServlet() {
        return new SpringAwareTouchKitServlet();
    }

    // Stuff needed for widgetset CDN, not needed if using std widgetset.
    // Run priming build (mvn install) if (generated) resource is missing.
    @Bean
    public in.virit.WidgetSet viritinCdnInitializer() {
        return new in.virit.WidgetSet();
    }

}
