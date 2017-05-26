package org.vaadin.tindra;

import com.vaadin.spring.boot.internal.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableJpaRepositories
// Make config here (vaadinServlet) override stuff in VaadinServletConfiguration
@Import(VaadinServletConfiguration.class)
public class Application {

    public static void main(String[] args) {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public VaadinServlet vaadinServlet() {
        return new SpringAwareTouchKitServlet();
    }

}
