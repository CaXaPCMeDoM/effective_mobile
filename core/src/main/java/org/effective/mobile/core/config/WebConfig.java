package org.effective.mobile.core.config;

import org.effective.mobile.core.util.PriorityConverter;
import org.effective.mobile.core.util.StatusConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StatusConverter());
        registry.addConverter(new PriorityConverter());
    }
}
