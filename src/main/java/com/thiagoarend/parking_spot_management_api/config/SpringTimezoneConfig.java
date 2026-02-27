package com.thiagoarend.parking_spot_management_api.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration // defines it as configuration class
public class SpringTimezoneConfig {

    @PostConstruct // after spring initializes this class, its constructor method is executed and then timezoneConfig()
    public void timezoneConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }

    // we could use a method to define the locale of the project, but we can use spring properties instead;
    // this is not available for timezone configuration
}
