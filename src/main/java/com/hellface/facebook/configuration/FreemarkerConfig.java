package com.hellface.facebook.configuration;

import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 * @author Johno Crawford (johno@hellface.com)
 */
@Configuration
public class FreemarkerConfig {

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer() {
            @Override
            protected void postProcessConfiguration(freemarker.template.Configuration config) throws IOException, TemplateException {
                config.setDefaultEncoding("UTF-8");
                config.setOutputEncoding("UTF-8");
                config.setIncompatibleImprovements(new Version(2, 3, 21));
            }
        };
        configurer.setTemplateLoaderPath(FreeMarkerProperties.DEFAULT_TEMPLATE_LOADER_PATH);
        return configurer;
    }
}
