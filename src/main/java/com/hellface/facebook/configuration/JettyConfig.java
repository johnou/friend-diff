package com.hellface.facebook.configuration;

import com.hellface.facebook.filter.ClickjackFilter;
import com.hellface.facebook.filter.NoCacheFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Johno Crawford (johno@hellface.com)
 */
@Configuration
@PropertySource("classpath:application.properties")
public class JettyConfig {

    private static final Logger logger = LoggerFactory.getLogger(JettyConfig.class);

    @Value("${application.hostname:#{null}}")
    private String hostname;

    @Value("${application.port:8090}")
    private int port;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        JettyEmbeddedServletContainerFactory containerFactory = new JettyEmbeddedServletContainerFactory(port);
        if (hostname != null && !hostname.equals("0.0.0.0")) { // avoid dns lookup (timeout) for 0.0.0.0
            try {
                containerFactory.setAddress(InetAddress.getByName(hostname));
            } catch (UnknownHostException e) {
                logger.error("Error looking up " + hostname, e);
            }
        }
        return containerFactory;
    }

    @Bean
    public ClickjackFilter clickjackFilter() {
        return new ClickjackFilter();
    }

    @Bean
    public NoCacheFilter noCacheFilter() {
        return new NoCacheFilter();
    }
}
