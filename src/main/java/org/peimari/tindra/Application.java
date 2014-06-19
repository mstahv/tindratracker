package org.peimari.tindra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.vaadin.addon.leaflet.LTileLayer;
import org.vaadin.addon.leaflet.util.AbstractJTSField;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
public class Application {
    
    public static void main(String[] args) {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        SpringApplication.run(Application.class, args);
    }

    public Application() {
        AbstractJTSField.setDefaultConfigurator(new AbstractJTSField.Configurator() {

            @Override
            public void configure(AbstractJTSField<?> field) {
                LTileLayer basemap = new LTileLayer(
                        "http://v3.tahvonen.fi/mvm71/tiles/peruskartta/{z}/{x}/{y}.png");
                field.getMap().addLayer(basemap);
            }
        });
    }
    
}
